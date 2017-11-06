package com.depression.controller.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.MembersOnlineStatus;
import com.depression.entity.ResultEntity;
import com.depression.entity.VoipUser;
import com.depression.model.EapEnterprise;
import com.depression.model.Member;
import com.depression.model.MemberAuthCode;
import com.depression.model.MemberBasic;
import com.depression.model.MemberTemp;
import com.depression.model.NeteaseUinfoEx;
import com.depression.model.PsychologyContentCategory;
import com.depression.model.PushDevType;
import com.depression.model.api.dto.ApiEapEnterpriseDTO;
import com.depression.model.api.dto.ApiPsychologyContentCategoryDTO;
import com.depression.model.api.dto.ApiUserDataDTO;
import com.depression.model.api.dto.ApiUserHomepageDTO;
import com.depression.model.api.vo.ApiIdsVO;
import com.depression.model.api.vo.ApiUserDataVO;
import com.depression.service.EapEnterpriseService;
import com.depression.service.EapService;
import com.depression.service.IMMessageService;
import com.depression.service.MemberAuthCodeService;
import com.depression.service.MemberConcernService;
import com.depression.service.MemberInterestedContentService;
import com.depression.service.MemberService;
import com.depression.service.MemberWechatService;
import com.depression.service.PunishmentService;
import com.depression.service.PushService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.AuthCodeUtil;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;
import com.depression.utils.SmsUtil;

@Controller
@RequestMapping(value={"/depression/api", "/superClass" })
public class MemberController
{
	
	private Logger log = Logger.getLogger(MemberController.class);
	
	@Autowired
	MemberAuthCodeService memberAuthCodeService;
	@Autowired
	MemberService memberService;
	@Autowired
	PushService pushService;
	@Autowired
	EapService eapService;
	@Autowired
	EapEnterpriseService eapEnterpriseService;
	@Autowired
	MemberInterestedContentService miContentService;
	@Autowired
	MemberWechatService memberWechatService;
	@Autowired
	MemberConcernService memberConcernService;
	@Autowired
	PunishmentService punishmentService;
	@Autowired
	IMMessageService imMessageService;
	@Autowired	
	ServiceStatisticsService serviceStatisticsService; 
	
	@InitBinder
	protected void initBinder(HttpServletRequest request,
	            ServletRequestDataBinder binder) throws Exception 
	 { 
       DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
       binder.registerCustomEditor(Date.class, dateEditor);
	 }
	
	/**
	 * 获取用户行业选项列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainIndustryList.json")
	@ResponseBody
	public Object obtainIndustryList()
	{
		ResultEntity result = new ResultEntity();
		List<String> industryList;
		try{
			industryList = memberService.obtainIndustryList();
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("industryList", industryList);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取用户资料，用于修改
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainUserData.json")
	@ResponseBody
	public Object obtainUserData(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				mid
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiUserDataDTO userDataDTO = new ApiUserDataDTO();
		try{
			//查询基本信息
			Member member = memberService.selectMemberByMid(mid);
			BeanUtils.copyProperties(member, userDataDTO);
			userDataDTO.setAvatarAbs(member.getAvatar());
			userDataDTO.setAvatarThumbnailAbs(member.getAvatarThumbnail());
		    if(member.getSignature()==null)
		    {//个人简介未设置，生成随机签名
				Integer num = 0;
				if(member.getMobilePhone()!=null && member.getMobilePhone().length()==11)
				{
					num = Integer.valueOf(member.getMobilePhone().substring(7));
				}
		    	userDataDTO.setSignature(memberService.genUserSignature(num));
		    }
			//查询感兴趣领域
			List<ApiPsychologyContentCategoryDTO> pccDTOs = new ArrayList<ApiPsychologyContentCategoryDTO>();
			List<PsychologyContentCategory> pccs = miContentService.obtainMiContents(mid);
			for(PsychologyContentCategory pcc : pccs)
			{
				ApiPsychologyContentCategoryDTO pccDTO = new ApiPsychologyContentCategoryDTO();
				BeanUtils.copyProperties(pcc, pccDTO);
				
				pccDTOs.add(pccDTO);
			}
			
			userDataDTO.setPccDTOs(pccDTOs);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("userDataDTO", userDataDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 修改用户资料
	 * @param userDataVO
	 * @param pccIds 用户感兴趣内容id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyUserData.json")
	@ResponseBody
	public Object modifyUserData(ApiUserDataVO userDataVO, ApiIdsVO pccIds)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				userDataVO,
				userDataVO.getMid()
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Member member = new Member();
			BeanUtils.copyProperties(userDataVO, member);
			memberService.update(member);
			
			if(pccIds!=null && pccIds.getIds()!=null)
			{
				miContentService.storeMiContents(userDataVO.getMid(), pccIds.getIds());
			}
			
			//修改网易im信息
			if(userDataVO.getAvatar()!=null || userDataVO.getNickname()!=null)
			{
				Member m = memberService.selectMemberByMid(userDataVO.getMid());
				String icon = m.getAvatar()==null?"":m.getAvatar();
				IMUtil.neteaseUserUpdateUinfo(m.getImAccount(), m.getNickname(), icon, null);
			}
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 计算年龄
	 * @param birthday
	 * @return
	 */
	Integer calcAge(Date birthday)
	{
		if(birthday == null)
		{//未设置生日，返回 0岁
			return 0;
		}
		Calendar cal =  Calendar.getInstance();
		if(cal.getTime().before(birthday))
		{//生日在未来，返回0岁
			return 0;
		}
        
		int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        
        int age = yearNow - yearBirth;
        
        if(monthNow == monthBirth) 
        {
            if (dayOfMonthNow < dayOfMonthBirth) 
            {//月相等，天小于
                age--;
            } 
        }else if (monthNow < monthBirth)
        {//月小于
        	age--;
        }
        
        return age;
	}
	
	/**
	 * 获取用户主页上的用户资料
	 * @param mid 会员id
	 * @param vid 查看者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainUserHomepage.json")
	@ResponseBody
	public Object obtainUserHomepage(Long mid, Long vid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				mid
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiUserHomepageDTO userHomepageDTO = new ApiUserHomepageDTO();
		try{
			//查询基本信息
			Member member = memberService.selectMemberByMid(mid);
			BeanUtils.copyProperties(member, userHomepageDTO);
			userHomepageDTO.setAvatarAbs(member.getAvatar());
			userHomepageDTO.setAvatarThumbnailAbs(member.getAvatarThumbnail());
		    if(member.getSignature()==null)
		    {//个人简介未设置，生成随机签名
				Integer num = 0;
				if(member.getMobilePhone()!=null && member.getMobilePhone().length()==11)
				{
					num = Integer.valueOf(member.getMobilePhone().substring(7));
				}
		    	userHomepageDTO.setSignature(memberService.genUserSignature(num));
		    }
		    userHomepageDTO.setConcernNum(memberConcernService.countConcernNum(mid));
		    userHomepageDTO.setFansNum(memberConcernService.countFansNum(mid));
		    
		    //计算年龄
		    userHomepageDTO.setAge(calcAge(member.getBirthday()));
		    
		    //IM消息
		    if(vid != null && !vid.equals(mid))
		    {
		    	Member mv = memberService.selectMemberByMid(vid);
		    	if(mv != null)
		    	{
		    		
		    		imMessageService.sendViewMsgWaitMoment(mv.getNickname(),vid, mv.getUserType(), member.getImAccount());
		    	}
		    }

		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("userHomepageDTO", userHomepageDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
		
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateMember.json")
	@ResponseBody
	public Object updateMember(HttpSession session, HttpServletRequest request, Member member)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == member)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			Member queryMember = memberService.getMember(member);
			if (null != queryMember && null != member.getMid() && !member.getMid().equals(queryMember.getMid()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("您已注册，请直接登录");
				return result;
			}

			// 存在图片文件
			if (null != member.getAvatar() && member.getAvatar().length() > 0)
			{
				JSONArray jsonArr = new JSONArray();
				jsonArr = JSON.parseArray(member.getAvatar());
				for (int idx = 0; idx < jsonArr.size(); idx++)
				{
					JSONObject obj = (JSONObject) jsonArr.get(idx);
					String imgPath = obj.getString("fileRelPath");
					String imgPreviewPath = obj.getString("filePreviewRelPath");
					member.setAvatar(imgPath);
					member.setAvatarThumbnail(imgPreviewPath);
				}

				result.put("avatar", member.getAvatar());
				String avatarThumbnail = member.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					result.put("avatarThumbnail", avatarThumbnail);
				} else
				{
					result.put("avatarThumbnail", "");
				}
			}

			Integer ret = memberService.update(member);
			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("更新失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("更新成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户更新接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllMember.json")
	@ResponseBody
	public Object getAllMember(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			Integer pageIndex = request.getParameter("pageIndex") == null ? null : Integer.parseInt(request.getParameter("pageIndex"));
			Integer pageSize = request.getParameter("pageSize") == null ? null : Integer.parseInt(request.getParameter("pageSize"));
			Member member = new Member();
			member.setPageIndex(pageIndex);
			member.setPageSize(pageSize);

			int count = memberService.selectCount();
			List<Member> memberLists = memberService.selectByPage(member);
			if (null == memberLists || 0 == memberLists.size())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("查找数据为空");
				return result;
			}

			for (Member m : memberLists)
			{
				m.setAvatar(m.getAvatar());
				String avatarThumbnail = m.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					m.setAvatarThumbnail(avatarThumbnail);
				} else
				{
					m.setAvatarThumbnail("");
				}
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setCount(count);
			result.setList(memberLists);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取所有用户接口失败");
			return result;
		}

	}

	@RequestMapping(value = "/getMember.json")
	@ResponseBody
	public Object getMember(HttpSession session, HttpServletRequest request, Long mid)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (mid == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传入的用户ID为空");
				return result;
			}
			Member member = new Member();
			member.setMid(mid);

			Member m = memberService.getMember(member);
			if (m != null)
			{
				m.setAvatar(m.getAvatar());
				String avatarThumbnail = m.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					m.setAvatarThumbnail(avatarThumbnail);
				} else
				{
					m.setAvatarThumbnail("");
				}
				m.setUserPassword("");
			} else
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("没有该用户");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("member", m);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取用户接口失败");
			return result;
		}

	}

	/*
	 * @RequestMapping(method = RequestMethod.POST, value = "/getSmsAuthCode.json")
	 * 
	 * @ResponseBody public Object getSmsAuthCode(HttpSession session, HttpServletRequest request, String mobilePhone) { ResultEntity result = new ResultEntity(); try { if
	 * (StringUtils.isEmpty(mobilePhone)) { result.setCode(ResultEntity.ERROR); result.setError("获取客户端传入的数据为空"); return result; } if (StringUtils.isEmpty(mobilePhone) || 11 != mobilePhone.length() ||
	 * !mobilePhone.startsWith("1")) { result.setCode(ResultEntity.ERROR); result.setError("非法的手机号"); return result; }
	 * 
	 * String url = "http://gw.api.taobao.com/router/rest"; String appkey = "23360103"; String secret = "285db1365cae6f93d43011502d1c013b"; TaobaoClient client = new DefaultTaobaoClient(url, appkey,
	 * secret); AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest(); extend 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
	 * req.setExtend("123456"); sms_type 短信类型，传入值请填写normal req.setSmsType("normal"); sms_free_sign_name
	 * 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。如“阿里大鱼”已在短信签名管理中通过审核，则可传入”阿里大鱼“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务 req.setSmsFreeSignName("注册验证"); sms_param
	 * 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"} String ac = AuthCodeUtil.getAuthCode(6);
	 * req.setSmsParamString("{\"code\":" + ac + ",\"product\":\"心猫\"}"); rec_num 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
	 * req.setRecNum(mobilePhone); sms_template_code 短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014 req.setSmsTemplateCode("SMS_2235146"); AlibabaAliqinFcSmsNumSendResponse rsp =
	 * client.execute(req); System.out.println(rsp.getBody()); if (!parseJson(rsp.getBody())) { result.setCode(ResultEntity.ERROR); result.setError("获取验证码失败"); return result; }
	 * 
	 * MemberAuthCode ma = new MemberAuthCode(); ma.setMobilePhone(mobilePhone); MemberAuthCode authCode = memberAuthCodeService.getAuthCode(ma); ma.setAuthCode(ac); Integer ret = 0; if (null ==
	 * authCode) { ret = memberAuthCodeService.insert(ma); if (1 != ret) { result.setError("调用获取验证码接口插入数据失败"); result.setCode(ResultEntity.ERROR); return result; } } else { ret =
	 * memberAuthCodeService.update(ma); if (1 != ret) { result.setError("调用获取验证码接口更新数据失败"); result.setCode(ResultEntity.ERROR); return result; } }
	 * 
	 * result.setCode(ResultEntity.SUCCESS); result.setMsg("获取验证码成功"); return result; } catch (Exception e) { e.printStackTrace(); result.setCode(ResultEntity.ERROR); result.setError("调用获取验证码接口失败");
	 * return result; }
	 * 
	 * }
	 */

	@RequestMapping(method = RequestMethod.POST, value = "/appRegisterMember.json")
	@ResponseBody
	public Object appRegisterMember(HttpSession session, HttpServletRequest request, String phoneNumber, String authCode, String password, String tempMid, Integer devType, String nickname)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(authCode) || StringUtils.isEmpty(password))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("获取客户端传入的数据为空");
				return result;
			}

			if (!isPhoneNumAvailable(phoneNumber))
			{
				result.setMsg("该手机号已经被注册");
				result.setCode(-2);
				return result;
			}

			MemberAuthCode mac = new MemberAuthCode();
			mac.setMobilePhone(phoneNumber);

			mac = memberAuthCodeService.getAuthCode(mac);

			if (null == mac)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("您输入的验证码不正确");
				return result;
			}

			if (!authCode.equals(mac.getAuthCode()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("验证码错误");
				return result;
			}

			Member member = new Member();
			member.setMobilePhone(phoneNumber);
			member.setUserPassword(MD5Util.getMD5String(password));
			member.setUserName(phoneNumber);
			member.setUserType((byte) 1);
			member.setStatus(new Byte("0"));
			if(nickname == null)
			{//已手机号后4位作为随机数生成昵称
				nickname = memberService.genNickname(Integer.valueOf(phoneNumber.substring(7)));
			}
			member.setNickname(nickname);
			member.setLocation("喵星球");
			// 注册网易im账号
			
			VoipUser voipUser = IMUtil.neteaseUserCreate(member.getNickname(), "", null);
			if (null == voipUser)
			{
				member.setImAccount("");
				member.setImPsw("");
			}else
			{
				member.setImAccount(voipUser.getVoipAccount());
				member.setImPsw(voipUser.getVoipPwd());
			}
			// 设置用户为正式会员
			member.setIsTemp((byte) 0);

			Long mid = memberService.insert(member);
			if (mid.longValue() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("注册失败");
				return result;
			}
			
			//网易名片
			NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
			uinfoEx.setMid(member.getMid());
			uinfoEx.setUserType(member.getUserType()==null?1:member.getUserType());
			uinfoEx.setTitle(member.getTitle()==null?"":member.getTitle());
			IMUtil.neteaseUserUpdateUinfo(member.getImAccount(), null, null, JSON.toJSONString(uinfoEx));

			//IM消息
			imMessageService.sendRegisterMsg(member.getImAccount());
			
			result.put("mid", mid);
			result.put("nickname", nickname);
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("注册成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用注册接口失败");
			return result;
		}

	}
	
	/**
	 * 获取所有心理内容分类
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainAllPcCategory.json")
	@ResponseBody
	public Object obtainAllPcCategory()
	{
		ResultEntity result = new ResultEntity();
		
		List<ApiPsychologyContentCategoryDTO> pccDTOs = new ArrayList<ApiPsychologyContentCategoryDTO>();
		try
		{
			List<PsychologyContentCategory> pccs = miContentService.obtainAllPsyConCategory();
			for(PsychologyContentCategory pcc : pccs)
			{
				ApiPsychologyContentCategoryDTO pccDTO = new ApiPsychologyContentCategoryDTO();
				BeanUtils.copyProperties(pcc, pccDTO);
				
				pccDTOs.add(pccDTO);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("pccDTOs", pccDTOs);
		result.put("count", pccDTOs.size());
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	

	/**
	 * 注册后收集基本信息
	 * @param mid
	 * @param sex
	 * @param birthday
	 * @param pccIds
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/collectMemberInfo.json")
	@ResponseBody
	public Object collectMemberInfo(Long mid, Byte sex, Date birthday, ApiIdsVO pccIds)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, sex))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try
		{
			Member member = new Member();
			member.setMid(mid);
			member.setSex(sex);
			member.setBirthday(birthday);
			memberService.update(member);
			
			if(pccIds!=null && pccIds.getIds()!=null)
			{
				miContentService.storeMiContents(mid, pccIds.getIds());
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getTempMember.json")
	@ResponseBody
	public Object getTempMember(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			MemberTemp mt = memberService.newMemberTemp();

			result.put("tempMid", mt.getMid());
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("成功获取临时游客");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用临时游客信息获取接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/appMemberLogin.json")
	@ResponseBody
	public Object appMemberLogin(HttpSession session, HttpServletRequest request, String username, String password, String channelId, String userId, Integer devType)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("获取客户端传入的数据为空");
				return result;
			}

			Member member = new Member();
			member.setMobilePhone(username);

			Member rtnMem = memberService.getMember(member);

			if (null == rtnMem)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("该手机号未注册");
				return result;
			}
			if (!rtnMem.getUserPassword().equals(MD5Util.getMD5String(password)))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("密码错误");
				return result;
			}

			rtnMem.setAvatar(rtnMem.getAvatar());
			String avatarThumbnail = rtnMem.getAvatarThumbnail();
			if (!StringUtils.isEmpty(avatarThumbnail))
			{
				rtnMem.setAvatarThumbnail(avatarThumbnail);
			} else
			{
				rtnMem.setAvatarThumbnail("");
			}
			
			//检查网易im
			if(!IMUtil.neteaseIsAccid(rtnMem.getImAccount()))
			{//未注册
				// 注册voip账号
				String icon = rtnMem.getAvatar()==null?"":rtnMem.getAvatar();
				VoipUser voipUser = IMUtil.neteaseUserCreate(rtnMem.getNickname(), icon, null);
				if (null == voipUser)
				{
					rtnMem.setImAccount("");
					rtnMem.setImPsw("");
				}else
				{
					rtnMem.setImAccount(voipUser.getVoipAccount());
					rtnMem.setImPsw(voipUser.getVoipPwd());
				}
				
				//网易名片
				NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
				uinfoEx.setMid(rtnMem.getMid());
				uinfoEx.setUserType(rtnMem.getUserType()==null?1:rtnMem.getUserType());
				uinfoEx.setTitle(rtnMem.getTitle()==null?"":rtnMem.getTitle());
				IMUtil.neteaseUserUpdateUinfo(rtnMem.getImAccount(), null, null, JSON.toJSONString(uinfoEx));
				
				Member mu = new Member();
				mu.setMid(rtnMem.getMid());
				mu.setImAccount(rtnMem.getImAccount());
				mu.setImPsw(rtnMem.getImPsw());
				memberService.update(mu);
			}
			
			// 存储session
			session.setAttribute(Constant.LOGINED, rtnMem.getMid());
			// 存登录设备类型以及推送ID
			if (null != devType && null != channelId && null != userId)
			{				
				pushService.maintainDevType(rtnMem.getMid(), devType, channelId, userId);
			}
			result.setCode(ResultEntity.SUCCESS);
			ArrayList<Member> arrayList = new ArrayList<Member>();
			arrayList.add(rtnMem);
			result.setList(arrayList);
			//获取EAP有效企业信息
			List<Long> validEeIds = eapService.obtainValidEeIds4EapEmployee(rtnMem.getMid());
			if(validEeIds.size() > 0)
			{
				//validEeIds 中的值已保证存在
				EapEnterprise ee = eapEnterpriseService.obtainEnterpriseByKey(validEeIds.get(0));

				ApiEapEnterpriseDTO eapEntDTO = new ApiEapEnterpriseDTO();
				BeanUtils.copyProperties(ee, eapEntDTO);
				
				eapEntDTO.setLogo(ee.getLogoRel());
				eapEntDTO.setLogoPreview(ee.getLogoPreviewRel());
				
				result.put("validEapEntCount", 1);
				result.put("validEapEntDTO", eapEntDTO);
			}else
			{
				result.put("validEapEntCount", 0);
			}
			//获取EAP失效企业信息
			List<Long> invalidEeIds = eapService.obtainInvalidEeIds4EapEmployee(rtnMem.getMid());
			if(invalidEeIds.size() > 0)
			{
				//invalidEeIds 中的值已保证存在
				EapEnterprise ee = eapEnterpriseService.obtainEnterpriseByKey(invalidEeIds.get(0));

				ApiEapEnterpriseDTO eapEntDTO = new ApiEapEnterpriseDTO();
				BeanUtils.copyProperties(ee, eapEntDTO);
				
				eapEntDTO.setLogo(ee.getLogoRel());
				eapEntDTO.setLogoPreview(ee.getLogoPreviewRel());
				
				result.put("invalidEapEntCount", 1);
				result.put("invalidEapEntDTO", eapEntDTO);
			}else
			{
				result.put("invalidEapEntCount", 0);
			}
			
			//获取禁言记录
			result.put("disableMessageDays", punishmentService.obtainDisableMessageDays(rtnMem.getMid()));
			result.setMsg("登录成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用登录接口失败");
			return result;
		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/V1/appLogin.json")
	@ResponseBody
	public Object appLogin(HttpSession session, HttpServletRequest request, String username, String password, String channelId, String userId, Integer devType)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传入的数据为空");
				return result;
			}

			Member member = new Member();
			member.setMobilePhone(username);

			Member rtnMem = memberService.getMember(member);

			if (null == rtnMem)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("该手机号未注册");
				return result;
			}
			if (!rtnMem.getUserPassword().equals(MD5Util.getMD5String(password)))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("密码错误");
				return result;
			}

			rtnMem.setAvatar(rtnMem.getAvatar());
			String avatarThumbnail = rtnMem.getAvatarThumbnail();
			if (!StringUtils.isEmpty(avatarThumbnail))
			{
				rtnMem.setAvatarThumbnail(avatarThumbnail);
			} else
			{
				rtnMem.setAvatarThumbnail("");
			}
			
			//检查网易im
			if(!IMUtil.neteaseIsAccid(rtnMem.getImAccount()))
			{//未注册
				// 注册voip账号
				String icon = rtnMem.getAvatar()==null?"":rtnMem.getAvatar();
				VoipUser voipUser = IMUtil.neteaseUserCreate(rtnMem.getNickname(), icon, null);
				if (null == voipUser)
				{
					rtnMem.setImAccount("");
					rtnMem.setImPsw("");
				}else
				{
					rtnMem.setImAccount(voipUser.getVoipAccount());
					rtnMem.setImPsw(voipUser.getVoipPwd());
				}
				
				//网易名片
				NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
				uinfoEx.setMid(rtnMem.getMid());
				uinfoEx.setUserType(rtnMem.getUserType()==null?1:rtnMem.getUserType());
				uinfoEx.setTitle(rtnMem.getTitle()==null?"":rtnMem.getTitle());
				IMUtil.neteaseUserUpdateUinfo(rtnMem.getImAccount(), null, null, JSON.toJSONString(uinfoEx));
				
				Member mu = new Member();
				mu.setMid(rtnMem.getMid());
				mu.setImAccount(rtnMem.getImAccount());
				mu.setImPsw(rtnMem.getImPsw());
				memberService.update(mu);
			}
			
			// 存储session
			session.setAttribute(Constant.LOGINED, rtnMem.getMid());
			// 存登录设备类型以及推送ID
			if (null != devType && null != channelId && null != userId)
			{				
				pushService.maintainDevType(rtnMem.getMid(), devType, channelId, userId);
			}
			result.setCode(ResultEntity.SUCCESS);
			ArrayList<Member> arrayList = new ArrayList<Member>();
			arrayList.add(rtnMem);
			result.setList(arrayList);
			
			List<ApiEapEnterpriseDTO> validEapEntDTOs = new ArrayList<ApiEapEnterpriseDTO>();
			
			//根据用户类型查询eap信息
			if( rtnMem.getUserType() == 1){
				//获取EAP有效企业信息
				List<Long> validEeIds = eapService.obtainValidEeIds4EapEmployee(rtnMem.getMid());
				if(validEeIds.size() > 0)
				{
					//validEeIds 中的值已保证存在
					
					Byte isOpened = 1; 
					
					for(Long validEeId : validEeIds){
						EapEnterprise ee = eapEnterpriseService.obtainEnterpriseByKey(validEeId);
						
						ApiEapEnterpriseDTO eapEntDTO = new ApiEapEnterpriseDTO();
						BeanUtils.copyProperties(ee, eapEntDTO);
						
						eapEntDTO.setLogo(ee.getLogoRel());
						eapEntDTO.setLogoPreview(ee.getLogoPreviewRel());
						
						if(ee.getIsOpened() == 0){
							isOpened = 0;
						}
						
						validEapEntDTOs.add(eapEntDTO);
					}
					
					//用户公开，增加心猫信息
					if(isOpened == 0 ){
						ApiEapEnterpriseDTO xm = new ApiEapEnterpriseDTO();
						xm.setEeId(0L);
						xm.setLogo(Configuration.XINMAO_LOGOPATH);
						xm.setName("心猫心理");
						validEapEntDTOs.add(xm);
					}
					result.put("validEapEntCount", validEapEntDTOs.size());
					result.put("validEapEntDTOs", validEapEntDTOs);
				}
			}else if( rtnMem.getUserType() == 2){
				//获取EAP有效企业信息
				
				
				
				List<Long> validEeIds = eapService.obtainValidEeIds4EapPsycho(rtnMem.getMid());
				if(validEeIds.size() > 0)
				{
					
					//validEeIds 中的值已保证存在
					
					for(Long validEeId : validEeIds){
						EapEnterprise ee = eapEnterpriseService.obtainEnterpriseByKey(validEeId);
						
						ApiEapEnterpriseDTO eapEntDTO = new ApiEapEnterpriseDTO();
						BeanUtils.copyProperties(ee, eapEntDTO);
						
						eapEntDTO.setLogo(ee.getLogoRel());
						eapEntDTO.setLogoPreview(ee.getLogoPreviewRel());
						
						validEapEntDTOs.add(eapEntDTO);
					}
					
					//用户公开，增加心猫信息
					if(rtnMem.getIsAudited() == 0 ){
						ApiEapEnterpriseDTO xm = new ApiEapEnterpriseDTO();
						xm.setEeId(0L);
						xm.setLogo(Configuration.XINMAO_LOGOPATH);
						xm.setName("心猫心理");
						validEapEntDTOs.add(xm);
					}
					
					result.put("validEapEntCount", validEapEntDTOs.size());
					result.put("validEapEntDTOs", validEapEntDTOs);
				}
			}
			
			else
			{
				result.put("validEapEntCount", 0);
			}
			//获取EAP失效企业信息
			List<Long> invalidEeIds = eapService.obtainInvalidEeIds4EapEmployee(rtnMem.getMid());
			if(invalidEeIds.size() > 0)
			{
				//invalidEeIds 中的值已保证存在
				EapEnterprise ee = eapEnterpriseService.obtainEnterpriseByKey(invalidEeIds.get(0));

				ApiEapEnterpriseDTO eapEntDTO = new ApiEapEnterpriseDTO();
				BeanUtils.copyProperties(ee, eapEntDTO);
				
				eapEntDTO.setLogo(ee.getLogoRel());
				eapEntDTO.setLogoPreview(ee.getLogoPreviewRel());
				
				result.put("invalidEapEntCount", 1);
				result.put("invalidEapEntDTO", eapEntDTO);
			}else
			{
				result.put("invalidEapEntCount", 0);
			}
			
			//获取禁言记录
			result.put("disableMessageDays", punishmentService.obtainDisableMessageDays(rtnMem.getMid()));
			result.setMsg("登录成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用登录接口失败");
			return result;
		}

	}
	
	
	

	@RequestMapping(method = RequestMethod.POST, value = "/verifyCode.json")
	@ResponseBody
	public Object verifyCode(HttpSession session, HttpServletRequest request, String phone, String code)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传入的数据为空");
				return result;
			}

			MemberAuthCode memberAuthCode = new MemberAuthCode();
			memberAuthCode.setMobilePhone(phone);
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

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("验证成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用验证校验码接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/updatePassword.json")
	@ResponseBody
	public Object updatePassword(HttpSession session, HttpServletRequest request, String phone, String code, String password)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("获取客户端传入的数据为空");
				return result;
			}
			
			if(code != null)
			{
				MemberAuthCode memberAuthCode = new MemberAuthCode();
				memberAuthCode.setMobilePhone(phone);
				MemberAuthCode authCode = memberAuthCodeService.getAuthCode(memberAuthCode);

				if (null == authCode)
				{
					result.setCode(ResultEntity.ERROR);
					result.setMsg("手机号不正确");
					return result;
				}

				if (!code.equals(authCode.getAuthCode()))
				{
					result.setCode(ResultEntity.ERROR);
					result.setMsg("验证码不正确");
					return result;
				}
			}

			
			Member m = memberService.selectMemberByMobilePhone(phone);

			if (null == m)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("没有找到该会员信息");
				return result;
			}

			Member member = new Member();
			member.setMid(m.getMid());
			member.setUserPassword(MD5Util.getMD5String(password));
			Integer ret = memberService.update(member);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("修改密码失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("修改密码成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setMsg("调用修改密码接口失败");
			return result;
		}

	}

	public boolean parseJson(String json)
	{
		JSONObject o1 = JSONObject.parseObject(json);
		String msg = o1.getString("alibaba_aliqin_fc_sms_num_send_response");

		if (StringUtils.isEmpty(msg))
		{
			return false;
		}

		JSONObject o2 = JSONObject.parseObject(msg);
		String result = o2.getString("result");

		if (StringUtils.isEmpty(result))
		{
			return false;
		}

		JSONObject o3 = JSONObject.parseObject(result);
		return "0".equals(o3.getString("err_code"));
	}

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

	@RequestMapping(value = "/getBonusPoint.json")
	@ResponseBody
	public Object getBonusPoint(HttpSession session, HttpServletRequest request, Long mid)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (mid == null)
			{
				result.setError("会员ID不能为空");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			Member member = new Member();
			member.setMid(mid);

			Member qm = memberService.getMember(member);
			if (qm == null)
			{
				result.setError("没有该用户");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("查询成功");
			result.put("bonusPoint", qm.getBonusPoint());
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
		return result;
	}

	private boolean isPhoneNumAvailable(String phoneNum)
	{
		Member infoMem = new Member();
		infoMem.setMobilePhone(phoneNum);

		Member queriedMem = memberService.getMember(infoMem);
		return queriedMem == null;
	}

	@RequestMapping(value = "/checkPhoneAvailablity.json")
	@ResponseBody
	public Object checkPhoneAvailablity(HttpSession session, HttpServletRequest request, String phoneNum)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (phoneNum == null)
			{
				result.setError("手机号码不能为空");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			result.put("availablity", isPhoneNumAvailable(phoneNum) ? 1 : 0);
			result.setCode(ResultEntity.SUCCESS);

		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
		return result;
	}

	@RequestMapping(value = "/changeMemberPhone.json")
	@ResponseBody
	public Object changeMemberPhone(HttpSession session, HttpServletRequest request, Long mid, String phoneNum, String authCode)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (mid == null || phoneNum == null || authCode == null)
			{
				result.setError("会员id，手机号码，验证不能为空");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			if (!isPhoneNumAvailable(phoneNum))
			{
				result.setError("手机已经被账号绑定");
				result.setCode(-2);
				return result;
			}

			MemberAuthCode memberAuthCode = new MemberAuthCode();
			memberAuthCode.setMobilePhone(phoneNum);
			MemberAuthCode mAuthCode = memberAuthCodeService.getAuthCode(memberAuthCode);

			if (null == mAuthCode)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("手机号不正确");
				return result;
			}

			if (!authCode.equals(mAuthCode.getAuthCode()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("验证码不正确");
				return result;
			}

			if (!memberService.checkMid(mid))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户不存在");
				return result;
			}

			Member m = new Member();
			m.setMid(mid);
			m.setMobilePhone(phoneNum);

			Integer ret = memberService.update(m);
			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("更新失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("更新成功");
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
		return result;
	}

	@RequestMapping(value = "/getMemberInfoByImAccounts.json")
	@ResponseBody
	public Object getMemberInfoByImAccounts(HttpSession session, HttpServletRequest request, @RequestParam(value = "imAccounts[]", required = false) String[] imAccounts)
	{
		ResultEntity result = new ResultEntity();

		if (imAccounts == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("imAccount不能为空");
			return result;
		}

		List<MemberBasic> mbl = memberService.getMembersByImAccounts(Arrays.asList(imAccounts));

		result.put("members", mbl);
		result.put("count", mbl.size());
		result.setCode(ResultEntity.SUCCESS);
		return result;
	}
	
	/**
	 * 绑定微信开放平台用户
	 * @param session
	 * @param request
	 * @param openid 开放平台openid
	 * @param mid 会员id
	 * @return
	 */
	@RequestMapping(value = "/bindWechatAccount.json")
	@ResponseBody
	public Object bindWechatAccount(HttpSession session, HttpServletRequest request, @RequestParam(value = "openid", required = false)String openid, @RequestParam(value = "mid", required = false) Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, openid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try
		{
			// 检查会员是否存在
			Member queryMember = new Member();
			queryMember.setMid(mid);
			Member curMember = memberService.getMember(queryMember);
			if (curMember == null)
			{
				result.setCode(ErrorCode.ERROR_USER_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_USER_INEXISTENT.getMessage());
				return result;
			}

			// 绑定微信用户
			Integer ret = memberWechatService.transBindWechatOpen(mid, openid);
			if(ret == 0)
			{
				result.setCode(ErrorCode.SUCCESS.getCode());
				result.setMsg(ErrorCode.SUCCESS.getMessage());
			}else if(ret == 1)
			{
				result.setCode(ErrorCode.ERROR_USER_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_USER_INEXISTENT.getMessage());
			}else if(ret == 2)
			{
				result.setCode(ErrorCode.ERROR_WECHAT_HAS_BEEN_BOUND.getCode());
				result.setMsg(ErrorCode.ERROR_WECHAT_HAS_BEEN_BOUND.getMessage());
			}else
			{
				result.setCode(ErrorCode.SUCCESS.getCode());
				result.setMsg(ErrorCode.SUCCESS.getMessage());
			}

		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		return result;
	}
	
	@RequestMapping(value = "/changeMemberStatus.json")
	@ResponseBody
	public Object changeMemberStatus(HttpSession session, HttpServletRequest request, Member member, Byte status)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (null == member || status == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("获取用户传入的参数为空");
			return result;
		}

		Member queryMember = new Member();
		queryMember.setMid(member.getMid());
		Member curMember = memberService.getMember(queryMember);
		if (curMember == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("查询用户数据为空");
			return result;
		}
		if ( status.intValue() != MembersOnlineStatus.STATUS_NOT_ONLINE.getCode().intValue() && 
				status.intValue() != MembersOnlineStatus.STATUS_ONLINE.getCode().intValue() &&
				status.intValue() != MembersOnlineStatus.STATUS_IN_THE_CALL.getCode().intValue() )
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("状态错误");
			return result;
		}

		if(curMember.getStatus().equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode()) &&
				status.equals(MembersOnlineStatus.STATUS_ONLINE.getCode()))
		{//更新在线时间
			curMember.setOnlineTime(new Date());
		}

		if(curMember.getStatus().equals(MembersOnlineStatus.STATUS_ONLINE.getCode()) &&
				status.equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode()) &&
				curMember.getOnlineTime() != null)
		{//统计在线时长
			Long sec = (new Date().getTime() - curMember.getOnlineTime().getTime())/1000;
			serviceStatisticsService.updatePsychoOnlineDuration(member.getMid(), sec);
			//eap 企业统计
			serviceStatisticsService.updateEnterpriseOnlineDurationByPsycho(member.getMid(), sec);
		}

		curMember.setStatus(status);
		memberService.update(curMember);

		result.setMsg("操作成功");
		result.setCode(ResultEntity.SUCCESS);
		return result;
	}
	
}
