package com.depression.controller.eap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.depression.base.ucpaas.DateUtil;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.entity.VoipUser;
import com.depression.model.EapEmployee;
import com.depression.model.EapEnterprise;
import com.depression.model.LicenseType;
import com.depression.model.Member;
import com.depression.model.MemberConcern;
import com.depression.model.MemberTag;
import com.depression.model.NeteaseUinfoEx;
import com.depression.model.PsychoInfo;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.ServicePsychoStatistics;
import com.depression.model.api.dto.ApiLicenseTypeDTO;
import com.depression.model.api.dto.ApiMemberTagDTO;
import com.depression.model.eap.dto.EapMemberTagDTO;
import com.depression.model.eap.dto.EapPsychoDTO;
import com.depression.model.eap.dto.EapServiceOrderDTO;
import com.depression.model.eap.vo.EapIdsVO;
import com.depression.model.eap.vo.EapPsychoVO;
import com.depression.model.web.dto.WebMemberTagDTO;
import com.depression.service.AdvisoryService;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.EapService;
import com.depression.service.IMMessageService;
import com.depression.service.MemberConcernService;
import com.depression.service.MemberService;
import com.depression.service.MemberTagService;
import com.depression.service.Permission;
import com.depression.service.PsychoGroupService;
import com.depression.service.PsychoInfoService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderEvaluationService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.HtmlUtil;
import com.depression.utils.IMUtil;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;
import com.depression.utils.SmsUtil;

@Controller
@RequestMapping("/Psychor")
public class PsychoController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	MemberService memberService;
	@Autowired
	MemberTagService memberTagService;
	@Autowired
	ServiceGoodsService serviceGoodsService;
	@Autowired
	MemberConcernService memberConcernService;
	@Autowired
	ServiceOrderService serviceOrderService;
	@Autowired
	PsychoInfoService psychoInfoService;
	@Autowired
	EapService eapService;
	@Autowired
	PsychoGroupService psychoGroupService;
	@Autowired
	ServiceOrderEvaluationService evaluationService;
	@Autowired
	ServiceStatisticsService serviceStatisticsService;
	@Autowired
	AdvisoryService memberAdvisoryService;
	@Autowired
	IMMessageService imMessageService;
	@Autowired	
	EapEnterpriseService eapEnterpriseService; 
	@Autowired
	EapEmployeeService employeeService;
	
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	public void member2PsychoDTO(Member m, EapPsychoDTO p, Long vid)
	{
		BeanUtils.copyProperties(m, p);
		if(p.getProfile() == null){
			p.setProfile("");
		}
		p.setAvatar(m.getAvatar());
		p.setAvatarThumbnail(m.getAvatarThumbnail());
		p.setCandidPhoto(m.getCandidPhoto());
		// 处理profile中的标签，适配app
		p.setProfileNoTag(HtmlUtil.getTextFromHtml(m.getProfile()));
		
		if(m.getMotto() == null)
		{//铭言没有设置，生成随机值
			Integer num = 0;
			if(m.getMobilePhone()!=null && m.getMobilePhone().length()==11)
			{
				num = Integer.valueOf(m.getMobilePhone().substring(7));
			}
			p.setMotto(memberService.genPyschoMotto(num));
		}

		// 查询tags
		List<ApiMemberTagDTO> psychoTagDTOs = new ArrayList<ApiMemberTagDTO>();
		for (MemberTag mt : memberTagService.getTagList(m.getMid()))
		{
			ApiMemberTagDTO tagDTO = new ApiMemberTagDTO();
			BeanUtils.copyProperties(mt, tagDTO);
			psychoTagDTOs.add(tagDTO);
		}
		p.setTags(psychoTagDTOs);
		
        if(m.getLtid()!=null)
        {
        	String licenseName =
        			psychoInfoService.getLicenseTypeByPrimaryKey(m.getLtid()).getLicenseName();
        	p.setLicenseName(licenseName);
        }

		// 查询语音服务信息
		ServiceGoods serviceGood = serviceGoodsService.selectImmVoiceByMid(m.getMid());
		if (serviceGood != null)
		{
			p.setVoicePrice(serviceGood.getPrice());
			p.setVoiceDuration(serviceGood.getDuration());
			p.setVoiceTimes(serviceGood.getTimes());
		}
		
		// 查询服务提供者提供的服务，目前只有一条商品记录
		// 该记录在咨询师设置服务价格时，由系统自动插入
		ServiceGoods serviceGoods = serviceGoodsService.selectImmVoiceByMid(m.getMid());
		if (serviceGoods != null)
		{
			// 查询是否尚有未完成订单，如果有，返回该订单
			ServiceOrder queryServiceOrder = new ServiceOrder();
			queryServiceOrder.setServiceProviderId(m.getMid());// 被叫人id

			List<ServiceOrder> orderList = serviceOrderService.selectUnCompleteOrderListByServiceProviderId(queryServiceOrder);
			if (orderList != null)
			{
				for (ServiceOrder serviceOrder : orderList)
				{
					// 刷新订单状态
					serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
				}
			}
		}
		// 重新查询会员状态
		Member queryMember = new Member();
		queryMember.setMid(m.getMid());
		Member member = memberService.getMember(queryMember);
		p.setStatus(member.getStatus());
		
		// 查询被关注数量
		MemberConcern mc = new MemberConcern();
		mc.setConcernTo(m.getMid());
		p.setConcernedNum(memberConcernService.selectCount(mc));
		
		// 修改咨询师电话服务数量(增量数量由数据库中手动设置)
		if (m.getPhoneCountIncrement() != null)
		{
			p.setVoiceTimes(m.getPhoneCountIncrement() + p.getVoiceTimes());
		}
		
		// 查询Eap服务状态
		if(vid != null)
		{
			p.setEapServiceStatus(eapService.isEmployeeServedByPsycho(vid, m.getMid()));
		}
		
	}

	/**
	 * 获取企业关联的EAP咨询师
	 * @param pageIndex
	 * @param pageSize
	 * @param eeId 企业id
	 * @return
	 */
	@Permission("4")
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainPsychos.json")
	@ResponseBody
	public Object obtainPsychosV1(Integer pageIndex, Integer pageSize, Long eeId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, eeId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
	
		Integer count;
		List<EapPsychoDTO> psychoDTOs = new ArrayList<EapPsychoDTO>();
		try
		{
			Set<Long> ids = new HashSet<Long>();
			Long pgId = eapService.obtainPgId4EapEnterprise(eeId);
			List<Long> pids = psychoGroupService.getPsychoIdOfGroup(pgId);
			ids.addAll(pids);
			
			List<Long> idsl = new ArrayList<Long>(ids);
			
			List<Member> psychos = memberService.searchPsychoV1(pageIndex, pageSize, null, (byte) 2, null, null, null, null, null, null, null, idsl);
			count = memberService.countSearchPsychoV1(null, null, null, null, null, null, null, null, idsl);

			for (Member p : psychos)
			{
				// 拷贝属性
				EapPsychoDTO psychoDTO = new EapPsychoDTO();
				member2PsychoDTO(p, psychoDTO, null);
				
				//计算平均得分  (commonScore*commonTime + eapScore*eapTime)/(commonTimes + eapTime)
				ServicePsychoStatistics pschoStatis = serviceStatisticsService.getOrCreatePsychoStat(p.getMid());
				Integer times = pschoStatis.getCommonScoreTimes() + pschoStatis.getEapScoreTimes();
				Double score;
				if(times > 0)
				{
					score = pschoStatis.getCommonScore().doubleValue()*pschoStatis.getCommonScoreTimes();
					score += pschoStatis.getEapScore().doubleValue()*pschoStatis.getEapScoreTimes();
					score /= times;
				}else
				{
					score = (double) 0;
				}
				//设置平均得分
				psychoDTO.setTotalScore(BigDecimal.valueOf(score));
				//设置评价数量
				psychoDTO.setTotalScoreTimes(times);
				//设置eap评价分数
				psychoDTO.setEapScore(pschoStatis.getEapScore());
				//设置eap评价次数
				psychoDTO.setEapScoreTimes(pschoStatis.getEapScoreTimes());
				//设置总电话次数
				psychoDTO.setTotalAdvisoryTimes(pschoStatis.getTotalAdvisoryTimes());
				//设置eap电话次数
				psychoDTO.setEapAdvisoryTimes(pschoStatis.getEapAdvisoryTimes());
				
				psychoDTOs.add(psychoDTO);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
	
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 获取咨询师详情
	 * @param pid 咨询师id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainPsychoDetail.json")
	@ResponseBody
	public Object obtainPsychoDetailV1(Long pid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
	
		Member psycho;
		try
		{
			psycho = memberService.selectMemberByMid(pid);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
	
		if (psycho == null)
		{
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return result;
		}
		EapPsychoDTO psychoDTO = new EapPsychoDTO();
		member2PsychoDTO(psycho, psychoDTO, null);
	
		result.put("psychoDTO", psychoDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取执照类型列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/getLicenseTypeList.json")
	@ResponseBody
	public Object getLicenseTypeListV1()
	{
		ResultEntity result = new ResultEntity();
		
		List<LicenseType> licenseTypes;
		try{
			licenseTypes = psychoInfoService.getAllLicenseTypes();
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<ApiLicenseTypeDTO> apiLicenseTypeDTOs = new ArrayList<ApiLicenseTypeDTO>();
		for(LicenseType lt : licenseTypes)
		{
			ApiLicenseTypeDTO apiLicenseTypeDTO = new ApiLicenseTypeDTO();
			BeanUtils.copyProperties(lt, apiLicenseTypeDTO);
			apiLicenseTypeDTOs.add(apiLicenseTypeDTO);
		}
		
		result.put("licenseTypes", apiLicenseTypeDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取咨询师标签列表
	 * @param session
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainPsychoTagList.json")
	@ResponseBody
	public Object obtainPsychoTagListV1(HttpSession session, HttpServletRequest request,
			Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<MemberTag> psychoTags = null;
		Integer count = null;
		try{
			psychoTags = memberTagService.getTagList4Psycho(pageIndex, pageSize, null);
			count = memberTagService.getTagCount4Psycho(null);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		List<EapMemberTagDTO> psychoTagDTOs = new ArrayList<EapMemberTagDTO>();
		for(MemberTag mt : psychoTags)
		{
			EapMemberTagDTO tagDTO = new EapMemberTagDTO();
			BeanUtils.copyProperties(mt, tagDTO);
			psychoTagDTOs.add(tagDTO);
		}
		
		result.put("psychoTags", psychoTagDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 添加咨询师
	 * 生成咨询师资料，立即对本企业有效，入住心猫需后续审核
	 * @param psychoVO
	 * @param idsVo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/addPsycho.json")
	@ResponseBody
	public Object addPsychoV1(EapPsychoVO psychoVO,	EapIdsVO idsVo)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				psychoVO,
				psychoVO.getNickname(),
				psychoVO.getMobilePhone(),
				psychoVO.getNeedAudit(),
				psychoVO.getEeId()
				//psychoVO.getUserPassword(),
				//psychoVO.getTitle(),
				//psychoVO.getAvatar(),
				//psychoVO.getCandidPhoto(),
				//psychoVO.getLocation(),
				//psychoVO.getProfile()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try
		{
			Integer preemption = memberService.insertPreemption(psychoVO.getMobilePhone());
	
			Member queryMember = memberService.selectMemberByMobilePhone(psychoVO.getMobilePhone());
	
			if(queryMember.getUserType() == 2)
			{
				result.setCode(ErrorCode.ERROR_MOBILEPHONE_EXISTED.getCode());
				result.setMsg(ErrorCode.ERROR_MOBILEPHONE_EXISTED.getMessage());
				return result;
			}
			
			Member member = new Member();
			// 设置个人信息
			BeanUtils.copyProperties(psychoVO, member);
			
			member.setMid(queryMember.getMid());
			// 设置用户为正式会员
			if (1 == preemption)
			{//未注册过会员时
				member.setIsTemp((byte) 0);
				member.setUserPassword(MD5Util.getMD5String("123456"));
			}
			
			// 注册网易im账号
			if (1 == preemption)
			{//未注册过会员时
				String icon = member.getAvatar()==null?"": member.getAvatar();
				VoipUser voipUser = IMUtil.neteaseUserCreate(member.getNickname(), icon, null);
				if (null == voipUser)
				{
					member.setImAccount("");
					member.setImPsw("");
				}else
				{
					member.setImAccount(voipUser.getVoipAccount());
					member.setImPsw(voipUser.getVoipPwd());
				}
			}
			
			//设置审核状态
			if(psychoVO.getNeedAudit() == 1)
			{
				member.setIsAudited((byte) 2);
			}else
			{
				member.setIsAudited((byte) 1);
			}
			member.setpSource(psychoVO.getEeId());
			
			memberService.updateToPsycho(member);
			//添加标签
			for(Long id: idsVo.getIds())
			{
				memberTagService.insertNewTagMap(member.getMid(), id);
			}
			
			//网易名片
			NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
			uinfoEx.setMid(member.getMid());
			uinfoEx.setUserType(member.getUserType()==null?1:member.getUserType());
			uinfoEx.setTitle(member.getTitle()==null?"":member.getTitle());
			String icon = member.getAvatar()==null?"": member.getAvatar();
			IMUtil.neteaseUserUpdateUinfo(member.getImAccount(), member.getNickname(), icon, JSON.toJSONString(uinfoEx));
			
			//添加到咨询师组
			Long pgId = eapService.obtainPgId4EapEnterprise(psychoVO.getEeId());
			psychoGroupService.addMemberToGroup(pgId, member.getMid());
	
		} catch (Exception e)
		{
			e.printStackTrace();
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	
	}

	/**
	 * 修改咨询师资料
	 * @param psychoVO
	 * @param idsVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/modifyPsycho.json")
	@ResponseBody
	public Object modifyPsychoV1(EapPsychoVO psychoVO, EapIdsVO idsVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				psychoVO,
				psychoVO.getMid()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try
		{
			
			Member queryMember = memberService.selectMemberByMid(psychoVO.getMid());
			if(queryMember == null)
			{
				result.setCode(ErrorCode.ERROR_USER_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_USER_INEXISTENT.getMessage());
				return result;
			}
			
			if(!queryMember.getpSource().equals(psychoVO.getEeId()))
			{
				result.setCode(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getCode());
				result.setMsg(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getMessage());
				return result;
			}
	
			Member member = new Member();
			// 设置个人信息
			BeanUtils.copyProperties(psychoVO, member);
			// 设置用户
			member.setMid(psychoVO.getMid());
			if(psychoVO.getUserPassword()!=null && psychoVO.getUserPassword()!="")
			{
				if (!psychoVO.getUserPassword().equals(queryMember.getUserPassword()))
				{
					member.setUserPassword(MD5Util.getMD5String(psychoVO.getUserPassword()));
				}	 
			}
			
			memberService.update(member);
			
			//修改网易im信息
			if(psychoVO.getAvatar()!=null || psychoVO.getNickname()!=null)
			{
				Member m = memberService.selectMemberByMid(psychoVO.getMid());
				String icon = m.getAvatar()==null?"":m.getAvatar();
				String ex = null;
				if(psychoVO.getTitle()!=null)
				{
					NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
					uinfoEx.setMid(m.getMid());
					uinfoEx.setUserType(m.getUserType()==null?1:m.getUserType());
					uinfoEx.setTitle(m.getTitle()==null?"":m.getTitle());
					ex = JSON.toJSONString(uinfoEx);
				}
				IMUtil.neteaseUserUpdateUinfo(m.getImAccount(), m.getNickname(), icon, ex);
			}
			
			//更新标签
			memberTagService.removeAllTagMap(member.getMid());
			for(Long id: idsVO.getIds())
			{
				memberTagService.insertNewTagMap(member.getMid(), id);
			}
	

		} catch (Exception e)
		{
			e.printStackTrace();
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
	 * 根据手机号查询咨询师目前的状态
	 * @param eeId
	 * @param mobilePhone
	 * @return status 0 咨询师不存在或者是普通用户 1咨询师不在企业专家库  2咨询师已在企业专家库
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/checkPsychoStatus.json")
	@ResponseBody
	public Object checkPsychoStatusV1(Long eeId, String mobilePhone)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				eeId,
				mobilePhone
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		Integer status;
		try{
			Member psycho = memberService.selectMemberByMobilePhone(mobilePhone);
			if(psycho == null || psycho.getUserType() != 2)
			{//咨询师不存在，或者还是普通用户
				status = 0;
				result.put("mid", 0);
			}else
			{
				Long pgId = eapService.obtainPgId4EapEnterprise(eeId);
				if(0 == psychoGroupService.isPsychoInGroup(pgId, psycho.getMid()))
				{//不在企业专家组
					status = 1;
					result.put("mid", psycho.getMid());
				}else
				{//已在企业专家组
					status = 2;
					result.put("mid", 0);
				}
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("status", status);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 邀请咨询师到组，需要咨询师同意
	 * @param eeId
	 * @param pid
	 * @param words 邀请语
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/invitePsychoToGroup.json")
	@ResponseBody
	public Object invitePsychoToGroupV1(Long eeId, Long pid, String words)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				eeId,
				pid
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			//TODO 实现邀请逻辑
			Member psycho = memberService.selectMemberByMid(pid);
			EapEnterprise enterprise = eapEnterpriseService.obtainEnterpriseByKey(eeId);
			//Im 推送
			String state="";
			if(enterprise.getType() == EapEnterpriseService.TYPE_COLLEGE){
			state = 
					String.format("%s老师，%s邀请你成为他们的校园心理专家%s 温馨提示：同意后，你需要免费为其用户提供心理服务。", 
					psycho.getNickname(), enterprise.getName(), words==null?"":"："+words);
			
			}
			
			if(enterprise.getType() == EapEnterpriseService.TYPE_COMPANY){
			state = 
					String.format("%s老师，%s邀请你成为他们的企业心理专家%s 温馨提示：同意后，你需要免费为其用户提供心理服务。", 
					psycho.getNickname(), enterprise.getName(), words==null?"":"："+words);
			
			}
			imMessageService.sendInvitePsyMsg(pid, eeId, null, state, psycho.getImAccount(),words);
			//短信通知
			SmsUtil.sendSms(psycho.getMobilePhone(), "165454", psycho.getNickname(), enterprise.getName());
		}catch (Exception e)
		{
			e.printStackTrace();
			log.error(ErrorCode.ERROR_SYSTEM_ERROR.getMessage(), e);
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	/**
	 * 将咨询师添加到专家库
	 * @param eeId 企业id
	 * @param pid 咨询师id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/addPsychoToGroup.json")
	@ResponseBody
	public Object addPsychoToGroupV1(Long eeId, Long pid)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				eeId,
				pid
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Long pgId = eapService.obtainPgId4EapEnterprise(eeId);
			psychoGroupService.addMemberToGroup(pgId, pid);
		}catch (Exception e)
		{
			e.printStackTrace();
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 将咨询师从专家库移除
	 * @param eeId 企业id
	 * @param pid 咨询师id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/removePsychoFromGroup.json")
	@ResponseBody
	public Object removePsychoFromGroupV1(Long eeId, Long pid)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				eeId,
				pid
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Long pgId = eapService.obtainPgId4EapEnterprise(eeId);
			psychoGroupService.removeMembersFromGroup(pgId, pid);
			memberService.deleteMemberById(pid);
		}catch (Exception e)
		{
			e.printStackTrace();
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
	 * 申请心猫审核，审核后可以对外服务
	 * @param pid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/requestAudit.json")
	@ResponseBody
	public Object requestAuditV1(Long pid, Long eeId,  Byte cancel)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pid,
				eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		Member psycho = new Member();
		try{
			psycho = memberService.selectMemberByMid(pid);
			if(!psycho.getpSource().equals(eeId))
			{
				result.setCode(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getCode());
				result.setMsg(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getMessage());
				return result;
			}
			
			if(cancel != null && cancel == 1)
			{
				psycho.setIsAudited((byte) 1);
			}else
			{
				psycho.setIsAudited((byte) 2);
			}
			memberService.update(psycho);
		}catch (Exception e)
		{
			e.printStackTrace();
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.put("isAudited", psycho.getIsAudited());
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取咨询流水列表
	 * @param eeId 企业主键
 	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Permission("5")
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainOrderList.json")
	@ResponseBody
	Object obtainOrderList(String words,Date begin,Date end,Long eeId, Integer pageIndex, Integer pageSize){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex,pageSize,eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<EapServiceOrderDTO> soDTOs=new ArrayList<EapServiceOrderDTO>();
		Integer count=0;
		try{
			soDTOs=getEapServiceOrderList(eeId,words,begin,end, pageIndex, pageSize);
			count=serviceOrderService.countServiceOrder(eeId,words,begin,end);
		}catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("soDTOs", soDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	public List<EapServiceOrderDTO> getEapServiceOrderList(Long eeId,String words,Date begin,Date end, Integer pageIndex, Integer pageSize) throws Exception{
		List<EapServiceOrderDTO> soDTOs=new ArrayList<EapServiceOrderDTO>();
		
		List<ServiceOrder> sos= serviceOrderService.getServiceOrderList(eeId,words,begin,end,pageIndex,pageSize);
		for(ServiceOrder s:sos){
			EapServiceOrderDTO soDTO=new EapServiceOrderDTO();
			BeanUtils.copyProperties(s, soDTO);
			//咨询者信息
			Member m1=memberService.selectMemberByMid(s.getMid());
			if(m1 != null){
				EapEmployee ee=employeeService.getEapEmployeeListByPhoneNumAndEeId(m1.getMobilePhone(), eeId);
				if(ee != null){
				soDTO.setName(ee.getName());
				soDTO.setPhoneNum(m1.getMobilePhone());
				}
			}
			
			//专家信息
			Member pi=eapService.getPsychoInfoByMidAndEeId(s.getServiceProviderId(),eeId);
			if(pi != null){
				soDTO.setPsyName(pi.getNickname());
				soDTO.setPsyPhoneNum(pi.getMobilePhone());
			}else{
				soDTO.setPsyName("匿名，不可查看");
				soDTO.setPsyPhoneNum("");
			}
			soDTO.setApptForm("电话咨询");
			soDTOs.add(soDTO);
		}
		
		return soDTOs;
	}
	
	
	
	/**
	 * 导出咨询流水列表
	 * @param request
	 * @param response
	 * @param eeId
	 * @param words
	 * @param begin
	 * @param end
	 */
	@Permission("5")
	@RequestMapping(method = RequestMethod.POST, value = "/exportServiceOrder.json")
	void exportServiceOrder(HttpServletRequest request, HttpServletResponse response, Long eeId,String words,Date begin,Date end){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return;
		}
		
		EapEnterprise ee = eapEnterpriseService.obtainEnterpriseByKey(eeId);
		if(ee == null){
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return;
		}
		
		response.setContentType("application/vnd.ms-excel");  
		String codedFileName;
		try{
			codedFileName = java.net.URLEncoder.encode(ee.getName() + "咨询流水", "UTF-8");
		} catch (UnsupportedEncodingException e1){
			// TODO Auto-generated catch block
			codedFileName = "EapEmloyee";
		} 
		
		response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");  
				
		OutputStream  fOut = null;
		try{
			Workbook wb = new XSSFWorkbook();
	        CreationHelper createHelper = wb.getCreationHelper();
	        Sheet sheet = wb.createSheet("EAP");
	        
	        //获取信息
	        List<EapServiceOrderDTO> list = getEapServiceOrderList(eeId,words,begin,end, null, null);
	        //设置列宽
	        sheet.setColumnWidth(1, 16*256);
	        sheet.setColumnWidth(2, 16*256);
	        sheet.setColumnWidth(3, 16*256);
	        sheet.setColumnWidth(4, 16*256);
	        sheet.setColumnWidth(5, 16*256);
	        sheet.setColumnWidth(6, 16*256);
	        sheet.setColumnWidth(7, 16*256);
	        sheet.setColumnWidth(8, 16*256);
	        
	      
		        //填写列名
		        Row row = sheet.createRow((short)0);
		        row.createCell(0).setCellValue("流水号");
		        row.createCell(1).setCellValue("求助者姓名");
		        row.createCell(2).setCellValue("求助者电话");
		        row.createCell(3).setCellValue("专家姓名");
		        row.createCell(4).setCellValue("专家电话");
		        row.createCell(5).setCellValue("类别");
		        row.createCell(6).setCellValue("形式");
		        row.createCell(7).setCellValue("时间");
		        row.createCell(8).setCellValue("时长");
		        
		      
		        for(short i=0; i< list.size(); i++){
		        	row = sheet.createRow(i+1);
			        row.createCell(0).setCellValue(list.get(i).getNo());
			        row.createCell(1).setCellValue(list.get(i).getName());
			        row.createCell(2).setCellValue(list.get(i).getPhoneNum());
			        row.createCell(3).setCellValue(list.get(i).getPsyName());
			        row.createCell(4).setCellValue(list.get(i).getPhoneNum());
			        row.createCell(5).setCellValue(list.get(i).getOrderType()==0?"付费咨询":"eap服务");
			        row.createCell(6).setCellValue("电话直拨");
			        row.createCell(7).setCellValue(DateUtil.dateToStr(list.get(i).getServiceBeginTime(),DateUtil.DATE_TIME_LINE));
			        Integer seconds=list.get(i).getPracticalDuration();
			        if(seconds == null || seconds == 0){
			        	row.createCell(8).setCellValue("无");
			        }else if (seconds > 0 && seconds <= 60){
			        	row.createCell(8).setCellValue("1'");
			        }else{
			        	Integer min=seconds/60;
				        row.createCell(8).setCellValue(min+"'");
			        }
		        }
		        //公司类型
	        
	        fOut = response.getOutputStream();  
	        wb.write(fOut);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_GENERATE_FILE_FAILED.getMessage(), e);
			result.setCode(ErrorCode.ERROR_GENERATE_FILE_FAILED.getCode());
			result.setMsg(ErrorCode.ERROR_GENERATE_FILE_FAILED.getMessage());
		}finally
		{
			if(fOut != null)
			{
				try
				{
					fOut.flush();
					fOut.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					log.error(ErrorCode.ERROR_GENERATE_FILE_FAILED.getMessage(), e);
				}
			}
		}	
	}
	
	
}

