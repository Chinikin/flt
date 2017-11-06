package com.depression.controller.eap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.listener.web.UserLoginListener;
import com.depression.model.EapEnterprise;
import com.depression.model.MemberAuthCode;
import com.depression.model.SystemOperationLog;
import com.depression.model.eap.dto.EapMenuDTO;
import com.depression.model.eap.vo.EapUserVO;
import com.depression.service.EapEnterpriseService;
import com.depression.service.EapMenuService;
import com.depression.service.EapUserService;
import com.depression.service.MemberAuthCodeService;
import com.depression.service.PrivilegesService;
import com.depression.service.SystemOperationLogService;
import com.depression.service.Permission;
import com.depression.utils.AuthCodeUtil;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;
import com.depression.utils.SmsUtil;
@Controller
@RequestMapping("/eapUser")
public class EapUserController {

	@Autowired
	private MemberAuthCodeService memberAuthCodeService;
	@Autowired
	private EapUserService eapUserService;
	@Autowired
	private SystemOperationLogService systemOperationLogService;
	@Autowired
	private EapEnterpriseService eapEnterpriseService;
	@Autowired
	private PrivilegesService privilegesService;
	@Autowired  
    private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private EapMenuService eapMenuService;
	Logger log = Logger.getLogger(this.getClass());

	/**
	 * 用户登录接口（包含验证是否为首次登录，根据返回值确定，前端相应做处理）
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 *            （验证码）
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/eapUserLogin.json")
	@ResponseBody
	public Object eapUserLogin(HttpServletRequest request, HttpServletResponse response,HttpSession session,EapUserVO eapUser, String code) {
		ResultEntity result = new ResultEntity();
		try {			
			// 参数检查
			if (null == eapUser || null == code
					|| null == eapUser.getMobilePhone()
					|| eapUser.getMobilePhone().equals("")) {
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}		
			String mobilePhone = eapUser.getMobilePhone();
			if(this.redisTemplate.boundValueOps(mobilePhone).get()!=null){
				String thisAuthCode = this.redisTemplate.boundValueOps(mobilePhone).get().toString();
				if(!thisAuthCode.equals(code)){
					result.setCode(ErrorCode.ERROR_LOGIN_CODE_ERROR.getCode());
					result.setMsg(ErrorCode.ERROR_LOGIN_CODE_ERROR.getMessage());
					return result;
				}
			}else{
				result.setCode(ErrorCode.ERROR_LOGIN_CODE_EXPIRE.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_CODE_EXPIRE.getMessage());
				return result;
			}	
			String password = eapUser.getEapPassword();	
			eapUser.setEapPassword(MD5Util.encode(password));
			if (StringUtils.isEmpty(mobilePhone)
					|| StringUtils.isEmpty(password)) {
				result.setCode(ErrorCode.ERROR_LOGIN_NO_USER.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_NO_USER.getMessage());
				return result;
			}
			Long eeId = eapUser.getEeId();
			if(eeId!=null){
				eapUser.setEeId(null);
			}
			EapUserVO dto = eapUserService.getEapUser(eapUser);
			// 检查用户名和密码是否正确
			if (null == dto) {
				result.setCode(ErrorCode.ERROR_LOGIN_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_ERROR.getMessage());
				return result;
			}
											
			// 检查用户是否被禁用
			Byte isEnable = dto.getIsEnable();
			if (isEnable.intValue() == 1) {
				result.setCode(ErrorCode.ERROR_LOGIN_LOSE_POWER.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_LOSE_POWER.getMessage());
				return result;
			}
			//判定用户是否重复登录
			boolean orLogin = UserLoginListener.isLogonUser(Long.valueOf(dto.getEapUserId().toString()));
			if(orLogin){
				result.put("onLine","您正在别的地方登录");
			}
			//判断是不是万能账户登录
			if(dto.getEeId()==0){
				if(eeId==null){
					result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
					result.setMsg("万能账户登录必须包含EAP服务ID");
					return result;
				}
				dto.setEeId(eeId);
			}
			//企业信息
			EapEnterprise eapEnterprise = eapEnterpriseService.obtainEnterpriseByKey(dto.getEeId());
			if(eapEnterprise==null){
				result.setCode(ErrorCode.ERROR_LOGIN_NO_EAPID.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_NO_EAPID.getMessage());
				return result;
			}else{
				//判断企业是否在服务中
				Date endDate = eapEnterprise.getServiceEndDate();
				if(new Date().getTime()>endDate.getTime()){
					eapEnterprise.setIsEnable((byte) 1);
				}
				result.put("eapEnterprise", eapEnterprise);
			}

			// 判断用户是否是首次登录(只适用于超级管理员)
			if (dto.getSpareInt() == 0 && dto.getUserType()==99) {
				// 更改用户登录状态
				EapUserVO user = new EapUserVO();
				user.setEapUserId(dto.getEapUserId());
				user.setSpareInt(1);
				eapUserService.updateByKey(user);
				result.put("userType", dto.getUserType());
				result.put("userId", dto.getEapUserId());
				result.put("username", dto.getShowName());
				result.put("firstLogin", "1");	
				result.put("eeId", dto.getEeId());
				result.put("mobilePhone", mobilePhone);
				result.setCode(ResultEntity.SUCCESS);
				result.setMsg("首次登录");
			
			} else {
				// 操作日志
				SystemOperationLog systemOperationLog = new SystemOperationLog();
				systemOperationLog.setOperatorId(dto.getEapUserId());
				systemOperationLog.setOperationContent("登录");
				systemOperationLog.setOperationTime(new Date());
				systemOperationLogService.insertSelective(systemOperationLog);

				// 登录成功
				result.put("userType", dto.getUserType());
				result.put("userId", dto.getEapUserId());
				result.put("eeId", dto.getEeId());
				result.put("username", dto.getShowName());
				result.put("mobilePhone", mobilePhone);
				result.setCode(ResultEntity.SUCCESS);
				result.setMsg("登录成功");
			}
			//删除redis存储验证码
			this.redisTemplate.delete(mobilePhone);
			result.put("sessionId", session.getId());
		    memberAuthCodeService.delete(dto.getMobilePhone());
			session.setAttribute(Constant.ADMIN_EEID, dto.getEeId());// 存储session
			session.setAttribute(Constant.ADMIN_LOGINED, dto.getEapUserId());// 存储session
			session.setAttribute(Constant.ADMIN_LEVEL, dto.getUserType());// 存储session		
			return result;
		} catch (Exception e) {
			log.error("调用用户登录接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用用户登录接口失败");
			return result;
		}

	}

	/**
	 * 万能账户登录接口
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/universalAccountLogin.json")
	@ResponseBody
	public  Object universalAccountLogin(HttpSession session, String username ,String password,String eeId) {
		ResultEntity result = new ResultEntity();
		try {
			if(username == null || password == null){
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}	
			//企业信息
			EapEnterprise eapEnterprise = eapEnterpriseService.obtainEnterpriseByKey(Long.valueOf(eeId));
			if(eapEnterprise==null){
				result.setCode(ErrorCode.ERROR_LOGIN_NO_EAPID.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_NO_EAPID.getMessage());
				return result;
			}else{
				result.put("eapEnterprise", eapEnterprise);
			}
				//判断用户名密码是否正确
				EapUserVO powerUser = new EapUserVO();
				//对密码进行加密
				String passWd = MD5Util.encode(password);
				powerUser.setEapPassword(passWd);					
				powerUser.setUsername(username);
				EapUserVO dto = eapUserService.getEapUser(powerUser);
				// 检查用户名和密码是否正确
				if (null == dto) {
					result.setCode(ErrorCode.ERROR_LOGIN_ERROR.getCode());
					result.setMsg(ErrorCode.ERROR_LOGIN_ERROR.getMessage());
					return result;
				}
				// 操作日志
				SystemOperationLog systemOperationLog = new SystemOperationLog();
				systemOperationLog.setOperatorId(dto.getEapUserId());
				systemOperationLog.setOperationContent("登录");
				systemOperationLog.setOperationTime(new Date());
				systemOperationLogService.insertSelective(systemOperationLog);
				
				session.setAttribute(Constant.ADMIN_EEID, eeId);// 存储session
				session.setAttribute(Constant.ADMIN_LOGINED, dto.getEapUserId());// 存储session
				session.setAttribute(Constant.ADMIN_LEVEL, dto.getUserType());// 用户级别
				session.setAttribute(Constant.ADMIN_AUTHORITY,dto.getSpareChar());// 用户权限

				// 登录成功
				result.put("userType", dto.getUserType());
				result.put("userId", dto.getEapUserId());
				result.put("username", dto.getShowName());
				result.put("mobilePhone", dto.getMobilePhone());
				result.setCode(ResultEntity.SUCCESS);
				result.setMsg("登录成功");
				return result;			
		} catch (Exception e) {
			log.error("调用万能用户登录接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用万能用户登录接口失败");
			return result;
		}
	}
	/**
	 * 登录时获取验证码
	 * 
	 * @param session
	 * @param request
	 * @param mobilePhone
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getSmsAuthCode.json")
	@ResponseBody
	public Object getSmsAuthCode( HttpSession session,String mobilePhone) {
		ResultEntity result = new ResultEntity();
		try {
			if (StringUtils.isEmpty(mobilePhone)) {
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}
			String sessionId = session.getId();
			System.out.println(sessionId);
			if (StringUtils.isEmpty(mobilePhone) || 11 != mobilePhone.length()
					|| !mobilePhone.startsWith("1")) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_ERROR.getMessage());
				return result;
			}

			String ac = AuthCodeUtil.getAuthCode(6);

			if (!SmsUtil.sendSms(mobilePhone, "149441", ac, "5")) {
				// 异常返回输出错误码和错误信息
				result.setCode(ErrorCode.ERROR_LOGIN_SEND_CODE_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_SEND_CODE_ERROR.getMessage());
				return result;
			}

			MemberAuthCode ma = new MemberAuthCode();
			ma.setMobilePhone(mobilePhone);
			MemberAuthCode authCode = memberAuthCodeService.getAuthCode(ma);
			ma.setAuthCode(ac);
			Integer ret = 0;
			if (null == authCode) {
				ret = memberAuthCodeService.insert(ma);
				if (1 != ret) {
					result.setMsg("调用获取验证码接口插入数据失败");
					result.setCode(ResultEntity.ERROR);
					return result;
				}
			} else {
				ret = memberAuthCodeService.update(ma);
				if (1 != ret) {
					result.setMsg("调用获取验证码接口更新数据失败");
					result.setCode(ResultEntity.ERROR);
					return result;
				}
			}


	        redisTemplate.opsForValue().set(mobilePhone,ac);
	        this.redisTemplate.expire(mobilePhone, Constant.TIME, TimeUnit.SECONDS);
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("获取验证码成功");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setMsg("获取验证码失败");
			return result;
		}

	}
	
	/**
	 * 获取菜单权限信息
	 * 
	 * @param session
	 * @param request
	 * @param mobilePhone
	 * @return
	 */
	@Permission("1") 
	@RequestMapping(method = RequestMethod.POST, value = "/getMenuList.json")
	@ResponseBody
	public Object getMenuList() {
		ResultEntity result = new ResultEntity();
		try {			
			// 查询出所有菜单
			List<EapMenuDTO> menu = eapMenuService.getAllMenu();			
			result.put("list", menu);
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("获取菜单成功");
			return result; 
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setMsg("获取菜单信息失败");
			return result;
		}

	}

	/**
	 * 新增超级管理员(设置用户为首次登录状态)
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addAdminUser.json")
	@ResponseBody
	public Object addAdminUser(HttpSession session ,EapUserVO eapUser) {
		ResultEntity result = new ResultEntity();
		try {
			// 参数检查
			if (null == eapUser) {
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}
			if (null == eapUser.getMobilePhone()
					|| eapUser.getMobilePhone().equals("")) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_NULL.getMessage());
				return result;
			}			
			if (null == eapUser.getUsername()
					|| eapUser.getUsername().equals("")) {
				result.setCode(ErrorCode.ERROR_LOGIN_USERNAME_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_USERNAME_NULL.getMessage());
				return result;
			}			

			// 检查eap企业是否存在
			EapEnterprise eapEnterprise = eapEnterpriseService
					.obtainEnterpriseByKey(eapUser.getEeId());
			if (eapEnterprise == null) {
				result.setCode(ErrorCode.ERROR_LOGIN_NO_EAPID.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_NO_EAPID.getMessage());
				return result;
			}

			// 检查手机号是否存在
			EapUserVO vo = new EapUserVO();
			vo.setMobilePhone(eapUser.getMobilePhone());
			EapUserVO eapUserVO = eapUserService.getEapUser(vo);
			if (eapUserVO != null) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_EXIST.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_EXIST.getMessage());
				return result;
			}					
			EapUserVO newEapUser = new EapUserVO();
			BeanUtils.copyProperties(eapUser, newEapUser);
			newEapUser.setEapPassword(MD5Util.encode(Constant.INITIALPASSWORD));
			newEapUser.setSpareInt(0);
			newEapUser.setUserType((byte) 99);
			// 插入用户信息
			eapUserService.createUser(newEapUser);

			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e) {
			log.error("调用用户新增接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用用户新增接口失败");
			return result;
		}
	}

	/**
	 * 新增普通管理员(设置用户为首次登录状态)
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@Permission("1") 
	@RequestMapping(method = RequestMethod.POST, value = "/addEapUser.json")
	@ResponseBody
	public Object addEapUser(HttpSession session ,EapUserVO eapUser) {
		ResultEntity result = new ResultEntity();
		try {
			// 参数检查
			if (null == eapUser) {
				result.setCode(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getMessage());
				return result;
			}
			if (null == eapUser.getMobilePhone()
					|| eapUser.getMobilePhone().equals("")) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_NULL.getMessage());
				return result;
			}			
			if (null == eapUser.getUsername()
					|| eapUser.getUsername().equals("")) {
				result.setCode(ErrorCode.ERROR_LOGIN_USERNAME_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_USERNAME_NULL.getMessage());
				return result;
			}			
			// 检查eap企业是否存在
			EapEnterprise eapEnterprise = eapEnterpriseService
					.obtainEnterpriseByKey(Long.valueOf(session.getAttribute(Constant.ADMIN_EEID).toString()));
			
			if (eapEnterprise == null) {
				result.setCode(ErrorCode.ERROR_LOGIN_NO_EAPID.getCode());
			    result.setMsg(ErrorCode.ERROR_LOGIN_NO_EAPID.getMessage());
				return result;
			}

			// 检查手机号是否存在
			EapUserVO vo = new EapUserVO();
			vo.setMobilePhone(eapUser.getMobilePhone());
			EapUserVO eapUserVO = eapUserService.getEapUser(vo);
			if (eapUserVO != null) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_EXIST.getCode());
			    result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_EXIST.getMessage());
				return result;
			}
			if(eapUser.getEapPassword()==null){
				result.setCode(ErrorCode.ERROR_LOGIN_PASSWORD_NULL.getCode());
			    result.setMsg(ErrorCode.ERROR_LOGIN_PASSWORD_NULL.getMessage());
				return result;
			}
			EapUserVO newEapUser = new EapUserVO();
			BeanUtils.copyProperties(eapUser, newEapUser);
			newEapUser.setEapPassword(MD5Util.encode(eapUser.getEapPassword()));
			newEapUser.setSpareInt(0);
			newEapUser.setEeId(Long.valueOf(session.getAttribute(Constant.ADMIN_EEID).toString()));
			newEapUser.setUserType((byte) 11);
			// 插入用户信息
			eapUserService.createUser(newEapUser);

			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e) {
			log.error("调用用户新增接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用用户新增接口失败");
			return result;
		}
	}

	/**
	 * 保存修改超级管理员（只能是超级管理员）
	 * 
	 * @param session
	 * @param request
	 * @param eapUser
	 * @return
	 */
	@Permission("1") 
	@RequestMapping(method = RequestMethod.POST, value = "/saveSuperUser.json")
	@ResponseBody
	public Object saveSuperUser(HttpSession session,String userId,String mobilePhone, String code,String username) {
		ResultEntity result = new ResultEntity();
		try {
			// 参数检查
			if (null == userId||null==mobilePhone||null==code) {
				result.setCode(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getMessage());
				return result;
			}
			// 检查验证码是否正确
						MemberAuthCode memberAuthCode = new MemberAuthCode();
						memberAuthCode.setMobilePhone(mobilePhone);
						MemberAuthCode authCode = memberAuthCodeService
								.getAuthCode(memberAuthCode);
						if (!code.equals(authCode.getAuthCode())) {
							result.setCode(ErrorCode.ERROR_LOGIN_CODE_ERROR.getCode());
							result.setMsg(ErrorCode.ERROR_LOGIN_CODE_ERROR.getMessage());
							return result;
						}
			// 检查手机号是否存在
			EapUserVO vo = new EapUserVO();
			vo.setMobilePhone(mobilePhone);
			EapUserVO eapUserVO = eapUserService.getEapUser(vo);
			if (eapUserVO != null) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_EXIST.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_EXIST.getMessage());
				return result;
			}
		
			// 插入用户信息
			EapUserVO newEapUser = new EapUserVO();
			newEapUser.setMobilePhone(mobilePhone);
			newEapUser.setUsername(username);
			newEapUser.setEapUserId(Long.valueOf(userId));
			eapUserService.updateByKey(newEapUser);
			//更新管理端
			EapEnterprise eapVO = new EapEnterprise();
			eapVO.setContacts(username);
			eapVO.setContactsPhoneNum(mobilePhone);
			eapVO.setEeId(Long.valueOf(session.getAttribute(Constant.ADMIN_EEID).toString()));
			eapEnterpriseService.updateEnterprise(eapVO);
			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e) {
			log.error("调用用户修改接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用用户修改接口失败");
			return result;
		}
	}
	/**
	 * 保存修改管理员（只能超级管理员修改普通管理员）
	 * 
	 * @param session
	 * @param request
	 * @param eapUser
	 * @return
	 */
	@Permission("1") 
	@RequestMapping(method = RequestMethod.POST, value = "/saveUser.json")
	@ResponseBody
	public Object saveUser(HttpSession session,EapUserVO eapUser) {
		ResultEntity result = new ResultEntity();
		try {
			// 参数检查
			if (null == eapUser) {
				result.setCode(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getMessage());
				return result;
			}
			if (null == eapUser.getMobilePhone()
					|| eapUser.getMobilePhone().equals("")) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_NULL.getMessage());
				return result;
			}
			if (null != eapUser.getEapPassword()
					&& !eapUser.getEapPassword().equals("")) {
				//密码长度校验				
				eapUser.setEapPassword(MD5Util.encode(eapUser.getEapPassword()));				
			}						
			if (null == eapUser.getUsername()
					|| eapUser.getUsername().equals("")) {
				result.setCode(ErrorCode.ERROR_LOGIN_USERNAME_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_USERNAME_NULL.getMessage());
				return result;
			}
			//用户名长度校验
			if(eapUser.getUsername().length()>64){
				result.setCode(ErrorCode.ERROR_LOGIN_USERNAME_LENGTH.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_USERNAME_LENGTH.getMessage());
				return result;
			}
			if (null == eapUser.getEeId()) {
				result.setCode(ErrorCode.ERROR_LOGIN_NO_EAPID.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_NO_EAPID.getMessage());
				return result;
			}

			// 检查eap企业是否存在
			EapEnterprise eapEnterprise = eapEnterpriseService
					.obtainEnterpriseByKey(eapUser.getEeId());
			if (eapEnterprise == null) {
				result.setCode(ErrorCode.ERROR_LOGIN_EAP_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_EAP_NULL.getMessage());
				return result;
			}

			// 检查手机号是否存在
			EapUserVO vo = new EapUserVO();
			vo.setMobilePhone(eapUser.getMobilePhone());
			EapUserVO eapUserVO = eapUserService.getEapUser(vo);
			if (eapUserVO == null) {
				result.setCode(ErrorCode.ERROR_LOGIN_EAP_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_EAP_NULL.getMessage());
				return result;
			}
			
			// 插入用户信息
			EapUserVO newEapUser = new EapUserVO();
			BeanUtils.copyProperties(eapUser, newEapUser);	
			if(newEapUser.getEapPassword().equals("")){
				newEapUser.setEapPassword(null);
			}
			eapUserService.updateByKey(newEapUser);
			// 操作成功		
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e) {
			log.error("调用用户修改接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用用户修改接口失败");
			return result;
		}
	}

	/**
	 * 用户列表
	 * 
	 * @param session
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param begin
	 * @param end
	 * @return
	 * 
	 **/
	@Permission("1") 
	@RequestMapping(value = "/listUser.json")
	@ResponseBody
	public Object listUser(Integer pageIndex, Integer pageSize, String eeId) {
		ResultEntity result = new ResultEntity();
		// 参数检查
		if (PropertyUtils.examineOneNull(pageIndex, pageSize)) {
			result.setCode(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_LOGIN_PARAM_ERROR.getMessage());
			return result;
		}

		// 设置查询条件
		EapUserVO eapUser = new EapUserVO();
		if (eeId != null && !eeId.equals("")) {
			eapUser.setEeId(Long.valueOf(eeId));
		}
		eapUser.setPageIndex(pageIndex);
		eapUser.setPageSize(pageSize);

		// 查询列表和总数
		List<EapUserVO> eapUserList = eapUserService.selectByPage(eapUser);
		int userCount = eapUserService.selectCountByEapUser(eapUser);
		List<EapUserVO> userList = new ArrayList<EapUserVO>();
		for (EapUserVO user : eapUserList) {
			EapUserVO newUser = new EapUserVO();
			BeanUtils.copyProperties(user, newUser);
			//超级管理员具有所有菜单权限		
			userList.add(newUser);
		}
		result.put("list", userList);
		result.put("count", userCount);
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("操作成功");
		return result;
	}

	/**
	 * 设置密码
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyPass.json")
	@ResponseBody
	public Object modifyPass(String userId, String newPassWord) {
		ResultEntity result = new ResultEntity();
		try {
			if (null == userId) {
				result.setCode(ErrorCode.ERROR_LOGIN_NO_USER.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_NO_USER.getMessage());
				return result;
			}

			// 查询用户信息
			EapUserVO eapUserVO = eapUserService.selectByPrimaryKey(Long.valueOf(userId));
			if (eapUserVO == null) {
				result.setCode(ErrorCode.ERROR_LOGIN_USER_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_USER_NULL.getMessage());
				return result;
			}

			// 修改用户密码
			eapUserVO.setEapPassword(MD5Util.encode(newPassWord));
			eapUserService.updateBymobilePhone(eapUserVO);

			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e) {
			log.error("调用用户修改密码接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用用户修改密码接口失败");
			return result;
		}
	}

	/**
	 * 找回密码
	 * 
	 * @param session
	 * @param request
	 * @param eapUser
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/findPassWord.json")
	@ResponseBody
	public Object findPassWord(EapUserVO eapUser, String code) {
		ResultEntity result = new ResultEntity();
		try {
			// 参数检查
			if (null == eapUser || null == code
					|| null == eapUser.getMobilePhone()
					|| eapUser.getMobilePhone().equals("")) {
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}

			// 检查验证码是否正确
			MemberAuthCode memberAuthCode = new MemberAuthCode();
			memberAuthCode.setMobilePhone(eapUser.getMobilePhone());
			MemberAuthCode authCode = memberAuthCodeService
					.getAuthCode(memberAuthCode);
			if (null == authCode) {
				result.setCode(ErrorCode.ERROR_LOGIN_PHONE_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_ERROR.getMessage());
				return result;
			}
			if (!code.equals(authCode.getAuthCode())) {
				result.setCode(ErrorCode.ERROR_LOGIN_CODE_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_CODE_ERROR.getMessage());
				return result;
			}

			// 查询用户信息
			EapUserVO eapUserVO = eapUserService.selectBymobilePhone(eapUser.getMobilePhone());
			if (eapUserVO == null) {
				result.setCode(ErrorCode.ERROR_LOGIN_USER_NULL.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_USER_NULL.getMessage());
				return result;
			}
			// 检查用户是否被禁用
			Byte isEnable = eapUserVO.getIsEnable();
			if (isEnable.intValue() == 1) {
				result.setCode(ErrorCode.ERROR_LOGIN_LOSE_POWER.getCode());
				result.setMsg(ErrorCode.ERROR_LOGIN_LOSE_POWER.getMessage());
				return result;
			}
			// 插入新密码
			EapUserVO newEapUser = new EapUserVO();
			BeanUtils.copyProperties(eapUser, newEapUser);
			newEapUser.setEapPassword(MD5Util.encode(eapUser
					.getEapPassword()));
			newEapUser.setEapUserId(eapUserVO.getEapUserId());
			eapUserService.updateByKey(newEapUser);
			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e) {
			log.error("调用用户找回密码接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用用户找回密码接口失败");
			return result;
		}
	}

	/**
	 * 修改用户状态
	 * @param userId
	 * @param isDel
	 * @return
	 */
	@Permission("1") 
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(String userId,String changeStatus) {
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (userId == null) {
			result.setCode(ErrorCode.ERROR_LOGIN_ID_NULL.getCode());
			result.setMsg(ErrorCode.ERROR_LOGIN_ID_NULL.getMessage());
			return result;
		}
		if (!changeStatus.equals("0") && !changeStatus.equals("1")) {
			result.setCode(ErrorCode.ERROR_LOGIN_STATUS_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_LOGIN_STATUS_ERROR.getMessage());
			return result;
		}
		EapUserVO eapUserVO = eapUserService.selectByPrimaryKey(Long
				.valueOf(userId));
		if (eapUserVO == null) {
			result.setCode(ResultEntity.ERROR);
			result.setMsg("用户不存在");
			return result;
		}

		eapUserVO.setIsEnable(Byte.parseByte(changeStatus));		
		eapUserService.updateByKey(eapUserVO);
		// 修改记录状态：0启用，1禁用
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("操作成功");
		return result;
	}

	/**
	 * 删除用户
	 * @param isDel
	 * @return
	 */
	@Permission("1") 
	@RequestMapping(method = RequestMethod.POST, value = "/deleteUser.json")
	@ResponseBody
	public Object deleteUser(String userId) {
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (userId == null) {
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 删除用户
		eapUserService.deleteUser(Long.valueOf(userId));
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("操作成功");
		return result;
	}
	
	/*
	 * 重置密码
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/resetPassWord.json")
	@ResponseBody
	public Object resetPassWord(String mobilePhone,String code) {
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (mobilePhone == null || code == null) {
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		// 检查验证码是否正确
		MemberAuthCode memberAuthCode = new MemberAuthCode();
		memberAuthCode.setMobilePhone(mobilePhone);
		MemberAuthCode authCode = memberAuthCodeService
				.getAuthCode(memberAuthCode);
		if (null == authCode) {
			result.setCode(ErrorCode.ERROR_LOGIN_PHONE_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_LOGIN_PHONE_ERROR.getMessage());
			return result;
		}
		if (!code.equals(authCode.getAuthCode())) {
			result.setCode(ErrorCode.ERROR_LOGIN_CODE_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_LOGIN_CODE_ERROR.getMessage());
			return result;
		}
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("操作成功");
		return result;
	}

	/**
	 * 用户注销接口
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/eapUserLogout.json")
	@ResponseBody
	public Object eapUserLogout(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 会话注销
			session.removeAttribute(Constant.ADMIN_LOGINED);// 删除session中对应条目
			session.removeAttribute(Constant.ADMIN_EEID);
			session.removeAttribute(Constant.ADMIN_LEVEL);
			session.removeAttribute(Constant.ADMIN_AUTHORITY);
			session.removeAttribute(Constant.CODE_TIME);
			session.invalidate();
			
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("OK");
			return result;
		} catch (Exception e)
		{
			log.error("调用会话注销接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("调用会话注销接口失败");
			return result;
		}

	}
}
