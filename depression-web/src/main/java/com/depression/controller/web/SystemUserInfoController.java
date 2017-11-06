package com.depression.controller.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.CCPRestSDK;
import com.depression.dao.SystemMenuMapper;
import com.depression.dao.SystemUserinfoMenuMappingMapper;
import com.depression.entity.AdminUserType;
import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.MemberAuthCode;
import com.depression.model.SystemDepartment;
import com.depression.model.SystemMenu;
import com.depression.model.SystemOperationLog;
import com.depression.model.SystemUserInfo;
import com.depression.model.SystemUserinfoMenuMapping;
import com.depression.model.web.dto.WebSystemUserInfoDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.model.web.vo.WebSystemUserInfoVO;
import com.depression.service.MemberAuthCodeService;
import com.depression.service.SystemDepartmentService;
import com.depression.service.SystemOperationLogService;
import com.depression.service.SystemUserInfoService;
import com.depression.utils.AuthCodeUtil;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;
import com.depression.utils.SmsUtil;

@Controller
@RequestMapping("/depression/system")
public class SystemUserInfoController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	SystemUserInfoService systemUserInfoService;

	@Autowired
	MemberAuthCodeService memberAuthCodeService;

	@Autowired
	SystemDepartmentService systemDepartmentService;
	
	@Autowired
	SystemMenuMapper systemMenuMapper;

	@Autowired
	SystemUserinfoMenuMappingMapper systemUserinfoMenuMappingMapper;
	
	@Autowired
	SystemOperationLogService systemOperationLogService;

	/**
	 * 用户登录接口
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/systemUserLogin.json")
	@ResponseBody
	public Object systemUserLogin(HttpSession session, HttpServletRequest request, SystemUserInfo userInfo, String code)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 参数检查
			if (null == userInfo || null == code || null == userInfo.getMobilePhone() || userInfo.getMobilePhone().equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户登录数据为空");
				return result;
			}

			// 检查验证码是否正确
			MemberAuthCode memberAuthCode = new MemberAuthCode();
			memberAuthCode.setMobilePhone(userInfo.getMobilePhone());
			MemberAuthCode authCode = memberAuthCodeService.getAuthCode(memberAuthCode);
			if (null == authCode)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("手机号不正确");
				return result;
			}
			if (!code.equals(authCode.getAuthCode()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("验证码不正确");
				return result;
			}

			// 检查用户名和密码是否正确
			String mobilePhone = userInfo.getMobilePhone();
			String password = userInfo.getUserPsw();
			if (StringUtils.isEmpty(mobilePhone) || StringUtils.isEmpty(password))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户登录数据为空");
				return result;
			}
			userInfo.setUserPsw(MD5Util.getMD5String(password));
			SystemUserInfo systemUserInfo = systemUserInfoService.userLogin(userInfo);
			if (null == systemUserInfo)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户名或密码错误");
				return result;
			}
			
			// 检查用户是否被禁用
			Byte isEnable = systemUserInfo.getIsEnable();
			if (isEnable.intValue() == 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("登录失败");
				return result;
			}
			
			// 操作日志
			SystemOperationLog systemOperationLog = new SystemOperationLog();
			systemOperationLog.setOperatorId(systemUserInfo.getUserId());
			systemOperationLog.setOperationContent("登录");
			systemOperationLog.setOperationTime(new Date());
			systemOperationLogService.insertSelective(systemOperationLog);

			// 登录成功
			session.setAttribute(Constant.ADMIN_LOGINED, systemUserInfo.getUserId());// 存储session
			result.put("userId", systemUserInfo.getUserId());
			result.put("username", systemUserInfo.getShowName());
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("登录成功");
			return result;
		} catch (Exception e)
		{
			log.error("调用用户登录接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户登录接口失败");
			return result;
		}

	}
	
	/**
	 * 用户登录接口(老管理平台接口)
	 * 
	 * @param session
	 * @param request
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/systemUserLoginOldPlt.json")
	@ResponseBody
	public Object systemUserLoginOldPlt(HttpSession session, HttpServletRequest request, SystemUserInfo userInfo)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == userInfo)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户登录数据为空");
				return result;
			}

			String username = userInfo.getUsername();
			String password = userInfo.getUserPsw();
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户登录数据为空");
				return result;
			}
			userInfo.setUserPsw(MD5Util.getMD5String(password));

			SystemUserInfo systemUserInfo = systemUserInfoService.userLogin(userInfo);

			if (null == systemUserInfo)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户名或密码错误");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("userId", systemUserInfo.getUserId());
			result.put("username", systemUserInfo.getUsername());
			result.setMsg("登录成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户登录接口失败");
			return result;
		}

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
	@RequestMapping(method = RequestMethod.POST, value = "/systemUserLogout.json")
	@ResponseBody
	public Object systemUserLogout(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 会话注销
			session.removeAttribute(Constant.ADMIN_LOGINED);// 删除session中对应条目
			session.invalidate();
			
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("会话注销成功");
			return result;
		} catch (Exception e)
		{
			log.error("调用会话注销接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("调用会话注销接口失败");
			return result;
		}

	}

	/**
	 * 获取验证码
	 * 
	 * @param session
	 * @param request
	 * @param mobilePhone
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getSmsAuthCode.json")
	@ResponseBody
	public Object getSmsAuthCode(HttpSession session, HttpServletRequest request, String mobilePhone)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (StringUtils.isEmpty(mobilePhone))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传入的数据为空");
				return result;
			}
			if (StringUtils.isEmpty(mobilePhone) || 11 != mobilePhone.length() || !mobilePhone.startsWith("1"))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("非法的手机号");
				return result;
			}
			
			String ac = AuthCodeUtil.getAuthCode(6);
			if (!SmsUtil.sendSms(mobilePhone, "149441", ac, "5"))
			{
				// 异常返回输出错误码和错误信息
				result.setCode(ResultEntity.ERROR);
				result.setError("发送验证码失败");
				return result;
			}

			MemberAuthCode ma = new MemberAuthCode();
			ma.setMobilePhone(mobilePhone);
			MemberAuthCode authCode = memberAuthCodeService.getAuthCode(ma);
			ma.setAuthCode(ac);
			Integer ret = 0;
			if (null == authCode)
			{
				ret = memberAuthCodeService.insert(ma);
				if (1 != ret)
				{
					result.setError("调用获取验证码接口插入数据失败");
					result.setCode(ResultEntity.ERROR);
					return result;
				}
			} else
			{
				ret = memberAuthCodeService.update(ma);
				if (1 != ret)
				{
					result.setError("调用获取验证码接口更新数据失败");
					result.setCode(ResultEntity.ERROR);
					return result;
				}
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("获取验证码成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用修改密码接口失败");
			return result;
		}

	}

	/**
	 * 新增用户
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addUser.json")
	@ResponseBody
	public Object addUser(HttpSession session, HttpServletRequest request, WebSystemUserInfoVO userInfo)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 参数检查
			if (null == userInfo)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			if (null == userInfo.getMobilePhone() || userInfo.getMobilePhone().equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("手机号不能为空");
				return result;
			}
			if (null == userInfo.getUserPsw() || userInfo.getUserPsw().equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户密码不能为空");
				return result;
			}
			if (null == userInfo.getShowName() || userInfo.getShowName().equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("显示名不能为空");
				return result;
			}
			if (null == userInfo.getDptId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("部门id不能为空");
				return result;
			}

			// 检查部门是否存在
			SystemDepartment systemDepartment = systemDepartmentService.selectByPrimaryKey(userInfo.getDptId());
			if (systemDepartment == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("部门不存在，请核实");
				return result;
			}

			// 检查手机号是否存在
			SystemUserInfo querySystemUserInfo = new SystemUserInfo();
			querySystemUserInfo.setMobilePhone(userInfo.getMobilePhone());
			SystemUserInfo existSystemUserInfo = systemUserInfoService.getSystemUserInfo(querySystemUserInfo);
			if (existSystemUserInfo != null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("手机号已存在，请核实");
				return result;
			}

			// 插入用户信息
			SystemUserInfo systemUserInfo = new SystemUserInfo();
			BeanUtils.copyProperties(userInfo, systemUserInfo);
			systemUserInfo.setUserPsw(MD5Util.getMD5String(systemUserInfo.getUserPsw()));
			systemUserInfoService.createUser(systemUserInfo);

			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e)
		{
			log.error("调用用户新增接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户新增接口失败");
			return result;
		}
	}

	/**
	 * 新增用户
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyUser.json")
	@ResponseBody
	public Object modifyUser(HttpSession session, HttpServletRequest request, WebSystemUserInfoVO userInfo)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 参数检查
			if (null == userInfo)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			if (null == userInfo.getUserId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户id不能为空");
				return result;
			}
			if (null == userInfo.getMobilePhone() || userInfo.getMobilePhone().equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("手机号不能为空");
				return result;
			}
			if (null == userInfo.getShowName() || userInfo.getShowName().equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("显示名不能为空");
				return result;
			}
			if (null == userInfo.getDptId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("部门id不能为空");
				return result;
			}

			// 检查部门是否存在
			SystemDepartment systemDepartment = systemDepartmentService.selectByPrimaryKey(userInfo.getDptId());
			if (systemDepartment == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("部门不存在，请核实");
				return result;
			}

			// 检查手机号是否存在
			SystemUserInfo querySystemUserInfo = new SystemUserInfo();
			querySystemUserInfo.setMobilePhone(userInfo.getMobilePhone());
			SystemUserInfo existSystemUserInfo = systemUserInfoService.getSystemUserInfo(querySystemUserInfo);
			if (existSystemUserInfo != null && existSystemUserInfo.getUserId().longValue() != userInfo.getUserId().longValue())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("手机号已存在，请核实");
				return result;
			}

			// 插入用户信息
			SystemUserInfo systemUserInfo = new SystemUserInfo();
			BeanUtils.copyProperties(userInfo, systemUserInfo);
			systemUserInfoService.updateByKey(systemUserInfo);

			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e)
		{
			log.error("调用用户修改接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户修改接口失败");
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
	 */
	@RequestMapping(value = "/listUser.json")
	@ResponseBody
	public Object listUser(HttpServletRequest request, Integer pageIndex, Integer pageSize, String searchKeyWords)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查
		if (PropertyUtils.examineOneNull(pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 设置查询条件
		SystemUserInfo querySystemUserInfo = new SystemUserInfo();
		if (searchKeyWords != null && !searchKeyWords.equals(""))
		{
			querySystemUserInfo.setMobilePhone(searchKeyWords);
			querySystemUserInfo.setShowName(searchKeyWords);
		}
		querySystemUserInfo.setPageIndex(pageIndex);
		querySystemUserInfo.setPageSize(pageSize);
		querySystemUserInfo.setUserType(Byte.parseByte(AdminUserType.TYPE_ADMIN_ORDINARY.getCode().toString()));// 只显示普通管理员列表

		// 查询列表和总数
		List<SystemUserInfo> sysUserList = systemUserInfoService.selectFuzzyListWithPage(querySystemUserInfo);
		int sysUserCount = systemUserInfoService.countFuzzyList(querySystemUserInfo);
		List<WebSystemUserInfoDTO> sysUserDTOList = new ArrayList<WebSystemUserInfoDTO>();
		for (SystemUserInfo systemUserInfo : sysUserList)
		{
			WebSystemUserInfoDTO webSystemUserInfoDTO = new WebSystemUserInfoDTO();
			BeanUtils.copyProperties(systemUserInfo, webSystemUserInfoDTO);
			List<String> privilegesMenuList = new ArrayList<String>();
			if (systemUserInfo != null && systemUserInfo.getDptId() != null)
			{
				// 查询部门信息
				SystemDepartment systemDepartment = systemDepartmentService.selectByPrimaryKey(systemUserInfo.getDptId());
				webSystemUserInfoDTO.setDptName(systemDepartment.getDepName());
				
				// 查询权限信息
				SystemMenu queryMenu = new SystemMenu();
				queryMenu.setMenuType(Byte.parseByte("0"));
				queryMenu.setIsEnable(Byte.parseByte("0"));
				
				// 查询父目录列表
				List<SystemMenu> parentMenuList = systemMenuMapper.selectSelective(queryMenu);
				for (SystemMenu parentMenu : parentMenuList)
				{
					JSONObject parentJsonObject = new JSONObject();
					parentJsonObject.put("menuId", parentMenu.getMenuId());
					parentJsonObject.put("menuName", parentMenu.getMenuName());

					// 查询菜单是否已选择
					SystemUserinfoMenuMapping queryMappingParent = new SystemUserinfoMenuMapping();
					queryMappingParent.setUserId(systemUserInfo.getUserId());
					queryMappingParent.setMenuId(parentMenu.getMenuId());
					queryMappingParent.setIsEnable(Byte.parseByte("0"));
					List<SystemUserinfoMenuMapping> mappingListParent = systemUserinfoMenuMappingMapper.selectSelective(queryMappingParent);
					if (mappingListParent != null && mappingListParent.size() > 0)
					{
						privilegesMenuList.add(parentMenu.getMenuName());
					}

					SystemMenu queryChildMenu = new SystemMenu();
					queryChildMenu.setIsEnable(Byte.parseByte("0"));
					queryChildMenu.setParentMenuId(parentMenu.getMenuId());
					// 查询子目录列表
					List<SystemMenu> childMenuList = systemMenuMapper.selectSelective(queryChildMenu);
					for (SystemMenu childMenu : childMenuList)
					{
						JSONObject childJsonObject = new JSONObject();
						childJsonObject.put("menuId", childMenu.getMenuId());
						childJsonObject.put("menuName", childMenu.getMenuName());

						// 查询菜单是否已选择
						SystemUserinfoMenuMapping queryMappingChild = new SystemUserinfoMenuMapping();
						queryMappingChild.setUserId(systemUserInfo.getUserId());
						queryMappingChild.setMenuId(childMenu.getMenuId());
						queryMappingChild.setIsEnable(Byte.parseByte("0"));
						List<SystemUserinfoMenuMapping> mappingListChild = systemUserinfoMenuMappingMapper.selectSelective(queryMappingChild);
						if (mappingListChild != null && mappingListChild.size() > 0)
						{
							privilegesMenuList.add(childMenu.getMenuName());
						}

					}
				}
				webSystemUserInfoDTO.setPrivilegesMenuList(privilegesMenuList);
			}
			
			sysUserDTOList.add(webSystemUserInfoDTO);
		}

		// 返回列表
		result.put("list", sysUserDTOList);
		result.put("count", sysUserCount);

		return result;
	}
	
	/**
	 * 重置密码
	 * 
	 * @param session
	 * @param request
	 * @param userInfo
	 * @param code
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/resetPass.json")
	@ResponseBody
	public Object resetPass(HttpSession session, HttpServletRequest request, WebSystemUserInfoVO userInfo)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 参数检查
			if (null == userInfo)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			if (null == userInfo.getUserId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户id不能为空");
				return result;
			}

			// 查询用户信息
			SystemUserInfo systemUserInfo = systemUserInfoService.selectByPrimaryKey(userInfo.getUserId());
			if(systemUserInfo == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户不存在");
				return result;
			}
			
			// 重置用户密码，默认123456
			systemUserInfo.setUserPsw(MD5Util.getMD5String("123456"));
			systemUserInfoService.updateByKey(systemUserInfo);

			// 操作成功
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e)
		{
			log.error("调用用户重置密码接口失败:" + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户重置密码接口失败");
			return result;
		}
	}
	
	/**
	 * 修改用户状态
	 * 
	 * @param request
	 * @param modelMap
	 * @param testingId
	 * @param isDel
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, WebIdsVO userIds, String isDel)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (userIds == null)
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}
		if (!isDel.equals("0") && !isDel.equals("1"))
		{
			result.setCode(-1);
			result.setMsg("状态错误：不允许的状态");
			return result;
		}

		// 修改记录状态：0启用，1禁用
		systemUserInfoService.enableByPrimaryKeyBulk(userIds.getIds(), Byte.parseByte(isDel));
		
		result.setCode(0);
		result.setMsg("状态更新成功");
		return result;
	}
	
	/**
	 * 部门列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listDepartment.json")
	@ResponseBody
	public Object listDepartment(HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 查询列表
		SystemDepartment queryRecord = new SystemDepartment();
		queryRecord.setIsEnable(Byte.parseByte("0"));
		List<SystemDepartment> departmentList = systemDepartmentService.selectSelective(queryRecord);

		// 返回列表
		result.put("list", departmentList);

		return result;
	}
}
