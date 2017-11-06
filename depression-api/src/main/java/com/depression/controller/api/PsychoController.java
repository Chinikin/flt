package com.depression.controller.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.depression.dao.LicenseTypeMapper;
import com.depression.entity.ErrorCode;
import com.depression.entity.MembersOnlineStatus;
import com.depression.entity.OrderState;
import com.depression.entity.ResultEntity;
import com.depression.model.EapEnterprise;
import com.depression.model.EvaluationLabel;
import com.depression.model.LicenseType;
import com.depression.model.Member;
import com.depression.model.MemberConcern;
import com.depression.model.MemberTag;
import com.depression.model.NeteaseUinfoEx;
import com.depression.model.PsychoGroup;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.ServiceOrderEvaluation;
import com.depression.model.ServicePsychoStatistics;
import com.depression.model.api.dto.ApiEvaluationLabelDTO;
import com.depression.model.api.dto.ApiEvaluationStatisticsDTO;
import com.depression.model.api.dto.ApiLicenseTypeDTO;
import com.depression.model.api.dto.ApiMemberTagDTO;
import com.depression.model.api.dto.ApiPsychoDTO;
import com.depression.model.api.dto.ApiPsychoDataDTO;
import com.depression.model.api.dto.ApiServiceOrderEvaluationDTO;
import com.depression.model.api.dto.ApiSortModeDTO;
import com.depression.model.api.vo.ApiPsychoDataVO;
import com.depression.service.EapEnterpriseService;
import com.depression.service.EapService;
import com.depression.service.AdvisoryService;
import com.depression.service.IMMessageService;
import com.depression.service.MemberConcernService;
import com.depression.service.MemberService;
import com.depression.service.MemberTagService;
import com.depression.service.PsychoGroupService;
import com.depression.service.PsychoInfoService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderEvaluationService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.Configuration;
import com.depression.utils.HtmlUtil;
import com.depression.utils.IMUtil;
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
	
	public void member2PsychoDTO(Member m, ApiPsychoDTO p, Long mid)
	{
		BeanUtils.copyProperties(m, p);
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
		
		// 设置关注状态
		p.setIsConcern(1);
		if(mid != null)
		{
			p.setIsConcern(memberConcernService.checkConcern(mid, m.getMid())?0:1);
		}
		// 修改咨询师电话服务数量(增量数量由数据库中手动设置)
		if (m.getPhoneCountIncrement() != null)
		{
			p.setVoiceTimes(m.getPhoneCountIncrement() + p.getVoiceTimes());
		}
		
		// 查询Eap服务状态
		if(mid != null)
		{
			p.setEapServiceStatus(eapService.isEmployeeServedByPsycho(mid, m.getMid()));
		}
		
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllMember.json")
	@ResponseBody
	public Object getAllMember(HttpSession session, HttpServletRequest request, Integer pageIndex, Integer pageSize, Byte pLevel, Byte sortMode, Long mid)
	{
		ResultEntity result = getAll(pageIndex, pageSize, pLevel, sortMode, mid);
		return result;
	}

	private ResultEntity getAll(Integer pageIndex, Integer pageSize, Byte pLevel, Byte sortMode, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (pLevel == null)
			pLevel = 0;

		List<Member> members;
		Integer count;
		try
		{

			List<Long> ids = memberService.selectPyschoIds();
			
			members = memberService.getByKeysSortableWithPageEnabled(ids, pageIndex, pageSize, pLevel, sortMode);
			count = memberService.getCountByPrimaryKeysEnabled(ids, pLevel);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		List<ApiPsychoDTO> psychoDTOs = new ArrayList<ApiPsychoDTO>();
		for (Member m : members)
		{
			ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
			
			member2PsychoDTO(m, psychoDTO, mid);
			
			psychoDTOs.add(psychoDTO);
		}

		result.put("psychoDTOs", psychoDTOs);

		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMemberListByTag.json")
	@ResponseBody
	public Object getMemberListByTag(HttpSession session, HttpServletRequest request, Integer pageIndex, Integer pageSize, Long tagId, Byte pLevel, Byte sortMode, Long mid)
	{
		ResultEntity result = getAllByTag(pageIndex, pageSize, tagId, pLevel, sortMode, mid);
		return result;
	}

	private ResultEntity getAllByTag(Integer pageIndex, Integer pageSize, Long tagId, Byte pLevel, Byte sortMode, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, tagId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (pLevel == null)
			pLevel = 0;

		List<Long> ids = new ArrayList<Long>();
		try
		{
			ids = memberTagService.getMemberIdsByTag(tagId);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		List<Member> members;
		Integer count;
		try
		{
			members = memberService.getByKeysSortableWithPageEnabled(ids, pageIndex, pageSize, pLevel, sortMode);
			count = memberService.getCountByPrimaryKeysEnabled(ids, pLevel);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		List<ApiPsychoDTO> psychoDTOs = new ArrayList<ApiPsychoDTO>();
		for (Member m : members)
		{
			// 拷贝属性
			ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
			member2PsychoDTO(m, psychoDTO, mid);

			psychoDTOs.add(psychoDTO);
		}

		result.put("psychoDTOs", psychoDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMember.json")
	@ResponseBody
	public Object getMember(HttpSession session, HttpServletRequest request, Long id, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(id))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		Member member;
		try
		{
			member = memberService.selectMemberByMid(id);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		if (member == null)
		{
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return result;
		}
		ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
		member2PsychoDTO(member, psychoDTO, mid);
		
		//咨询师主页的回答数已计算值为准
		Integer count = memberAdvisoryService.countAdvisory4Psychos(id);
		psychoDTO.setAnswerCount(count);
		//IM消息
		if(mid != null && !mid.equals(id))
	    {
	    	Member mv = memberService.selectMemberByMid(mid);
	    	if(mv != null)
	    	{
	    		imMessageService.sendViewMsgWaitMoment(mv.getNickname(),mid, mv.getUserType(), member.getImAccount());
	    	}
	    }

		result.put("psychoDTO", psychoDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllPsychoDoctors.json")
	@ResponseBody
	public Object getAllPsychoDoctors(HttpSession session, HttpServletRequest request, Integer pageIndex, Integer pageSize, Long tagId, Byte pLevel, Byte sortMode, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if (tagId == null)// 获取所有
		{
			result = getAll(pageIndex, pageSize, pLevel, sortMode, mid);
		} else
		{
			result = getAllByTag(pageIndex, pageSize, tagId, pLevel, sortMode, mid);
		} 
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getSortMode.json")
	@ResponseBody
	public Object getSortMode()
	{
		ResultEntity result = new ResultEntity();
		
		List<ApiSortModeDTO> modeDTOs = new ArrayList<ApiSortModeDTO>();
		modeDTOs.add(new ApiSortModeDTO(1, "最多咨询"));		
		modeDTOs.add(new ApiSortModeDTO(2, "最多感谢 "));		
		modeDTOs.add(new ApiSortModeDTO(3, "在线状态 "));		
		modeDTOs.add(new ApiSortModeDTO(4, "价格从低到高 "));	
		modeDTOs.add(new ApiSortModeDTO(5, "价格从高到低 "));	
		
		result.put("modeDTOs", modeDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取用户惯了的EAP咨询师
	 * @param pageIndex
	 * @param pageSize
	 * @param sortMode 排序模式
	 * @param mid 用户会员id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getEapPsychos.json")
	@ResponseBody
	public Object getEapPsychos(Integer pageIndex, Integer pageSize, Byte sortMode, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		Set<Long> ids = new HashSet<Long>();
		try
		{
			List<Long> pgIds = eapService.obtainPgIds4EapEmployee(mid);
			for(Long pgId : pgIds)
			{
				PsychoGroup psychoGroup = psychoGroupService.obtainPsychoGroup(pgId);
				if(psychoGroup==null || psychoGroup.getIsEnable().equals(1))
				{//组被删除或者禁用
					continue;
				}
				List<Long> pids = psychoGroupService.getPsychoIdOfGroup(pgId);
				ids.addAll(pids);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<Long> idsl = new ArrayList<Long>(ids);

		List<Member> members;
		Integer count;
		try
		{
			members = memberService.getByKeysSortableWithPageEnabled(idsl, pageIndex, pageSize, null, sortMode);
			count = memberService.getCountByPrimaryKeysEnabled(idsl, null);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		List<ApiPsychoDTO> psychoDTOs = new ArrayList<ApiPsychoDTO>();
		for (Member m : members)
		{
			// 拷贝属性
			ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
			member2PsychoDTO(m, psychoDTO, mid);

			psychoDTOs.add(psychoDTO);
		}

		result.put("psychoDTOs", psychoDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
		/**
	 * 检查电话服务是否超时
	 * 
	 * @param serviceGoods
	 * @param existServiceOrder
	 * @return
	 */
	private boolean checkCallServiceTimeout(ServiceGoods serviceGoods, ServiceOrder existServiceOrder)
	{
		long curInterval = (new Date().getTime() - existServiceOrder.getServiceRealityBeginTime().getTime()) / 1000;
		boolean timeoutFlag = false;
		if (curInterval >= 3 * 24 * 60 * 60)// 超过3天，订单结束
		{
			timeoutFlag = true;
		}

		Long maxDuration = Long.parseLong(serviceGoods.getDuration() * 60 + "");
		Long curDur = existServiceOrder.getPracticalDuration().longValue();
		if (curDur >= maxDuration.longValue()) // 超过订单最大时长，订单结束
		{
			timeoutFlag = true;
		}
		return timeoutFlag;
	}
	
	/**
	 * 获取咨询师资料
	 * @param mid 咨询师会员id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPsychoData.json")
	@ResponseBody
	public Object obtainPsychoData(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiPsychoDataDTO psychoDataDTO = new ApiPsychoDataDTO();
		try{
			Member m = memberService.selectMemberByMid(mid);
			
			BeanUtils.copyProperties(m, psychoDataDTO);
			psychoDataDTO.setAvatarAbs(m.getAvatar());
			psychoDataDTO.setAvatarThumbnailAbs(m.getAvatarThumbnail());
			psychoDataDTO.setCandidPhotoAbs(m.getCandidPhoto());
			// 处理profile中的标签，适配app
			psychoDataDTO.setProfileNoTag(HtmlUtil.getTextFromHtml(m.getProfile()));
			
			if(m.getMotto() == null)
			{//铭言没有设置，生成随机值
				Integer num = 0;
				if(m.getMobilePhone()!=null && m.getMobilePhone().length()==11)
				{
					num = Integer.valueOf(m.getMobilePhone().substring(7));
				}

				psychoDataDTO.setMotto(memberService.genPyschoMotto(num));
			}
			
			//是否允许修改从业年限
			Integer canModify = is6MonthPass(m.getWorkDataModifyTime()) ? 0 : 1;
			psychoDataDTO.setCanWorkDataModify(canModify);
			
			//是否允许修改累计个案
			canModify = is6MonthPass(m.getCaseHoursModifyTime()) ? 0 : 1;
			psychoDataDTO.setCanCaseHoursModify(canModify);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("psychoDataDTO", psychoDataDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 是否过去6个月
	 * @param lastDate
	 * @return
	 */
	boolean is6MonthPass(Date lastDate)
	{
		if(lastDate == null)
		{//未设置
			return true;
		}
		
		Calendar cal = Calendar.getInstance();
		
		if(cal.before(lastDate))
		{//时间在现在之后
			return false;
		}
		
		int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.setTime(lastDate);
        int yearLast = cal.get(Calendar.YEAR);
        int monthLast = cal.get(Calendar.MONTH);
        int dayOfMonthLast = cal.get(Calendar.DAY_OF_MONTH);
        
        int monthPass = (yearNow-yearLast)*12 + (monthNow-monthLast);
        if(dayOfMonthNow < dayOfMonthLast)
        {
        	monthPass--;
        }
        
        return monthPass >= 6;
	}
	
	/**
	 * 修改咨询师资料
	 * @param psychoDataVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyPsychoData.json")
	@ResponseBody
	public Object modifyPsychoData(ApiPsychoDataVO psychoDataVO)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(psychoDataVO, psychoDataVO.getMid()))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Member member = new Member();
			BeanUtils.copyProperties(psychoDataVO, member);
			//确认从业年限是否被修改，6个月内不能被修改
			Member m = memberService.selectMemberByMid(psychoDataVO.getMid());
			if(!is6MonthPass(m.getWorkDataModifyTime()))
			{
				member.setWorkYears(null);
			}else if(!m.getWorkYears().equals(psychoDataVO.getWorkYears())&&psychoDataVO.getWorkYears()!=null)
			{
				member.setWorkDataModifyTime(new Date());
			}
			
			//确定累计个案是否被修改，6个月内不能被修改
			if(!is6MonthPass(m.getCaseHoursModifyTime()))
			{
				member.setCaseHours(null);
			}else if(!m.getCaseHours().equals(psychoDataVO.getCaseHours())&&psychoDataVO.getCaseHours()!=null)
			{
				member.setCaseHoursModifyTime(new Date());
			}
			
			memberService.update(member);
			
			//修改网易im信息
			if(psychoDataVO.getAvatar()!=null || psychoDataVO.getNickname()!=null)
			{
				Member m1 = memberService.selectMemberByMid(psychoDataVO.getMid());
				String icon = m1.getAvatar()==null?"":m1.getAvatar();
				String ex = null;
				if(psychoDataVO.getTitle()!=null)
				{
					NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
					uinfoEx.setMid(m1.getMid());
					uinfoEx.setUserType(m1.getUserType()==null?1:m1.getUserType());
					uinfoEx.setTitle(m1.getTitle()==null?"":m1.getTitle());
					ex = JSON.toJSONString(uinfoEx);
				}
				IMUtil.neteaseUserUpdateUinfo(m1.getImAccount(), m1.getNickname(), icon, ex);
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
	 * 获取咨询师的订单评价列表
	 * @param pid	咨询师会员id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEvaluations4Psycho.json")
	@ResponseBody
	public Object obtainEvaluations4Psycho(Long pid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pid, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiServiceOrderEvaluationDTO> evaluationDTOs = new ArrayList<ApiServiceOrderEvaluationDTO>();
		Integer count = 0;
		try{
			List<ServiceOrderEvaluation> evaluations = 
					evaluationService.obtainEvaluations4Psycho(pid, pageIndex, pageSize);
			for(ServiceOrderEvaluation e : evaluations)
			{
				ApiServiceOrderEvaluationDTO evaluationDTO = new ApiServiceOrderEvaluationDTO();
				BeanUtils.copyProperties(e, evaluationDTO);
				if(evaluationDTO.getComment() == null)
				{
					evaluationDTO.setComment("");
				}
				
				//查询标签
				List<ApiEvaluationLabelDTO> labelDTOs = new ArrayList<ApiEvaluationLabelDTO>();
				List<EvaluationLabel> labels = evaluationService.obtainLabels4Evaluation(e.getSoeId());
				for(EvaluationLabel l : labels)
				{
					ApiEvaluationLabelDTO labelDTO = new ApiEvaluationLabelDTO();
					BeanUtils.copyProperties(l, labelDTO);
					labelDTOs.add(labelDTO);
				}
				evaluationDTO.setLabelDTOs(labelDTOs);
				
				evaluationDTOs.add(evaluationDTO);				
			}
			
			count = evaluationService.countEvaluations4Psycho(pid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("evaluationDTOs", evaluationDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取咨询师评价统计
	 * @param pid 咨询师会员id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEvaluationStatis4Psycho.json")
	@ResponseBody
	public Object obtainEvaluationStatis4Psycho(Long pid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiEvaluationStatisticsDTO statisDTO = new ApiEvaluationStatisticsDTO();
		try{
			//计算平均得分  (commonScore*commonTime + eapScore*eapTime)/(commonTimes + eapTime)
			ServicePsychoStatistics pschoStatis = serviceStatisticsService.getOrCreatePsychoStat(pid);
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
			statisDTO.setScore(BigDecimal.valueOf(score));
			
			//设置评价数量
			statisDTO.setTimes(times);
			
			//获取标签列表
			List<ApiEvaluationLabelDTO> labelDTOs = new ArrayList<ApiEvaluationLabelDTO>();
			List<EvaluationLabel> labels = evaluationService.selectAllLabel();
			for(EvaluationLabel l : labels)
			{
				ApiEvaluationLabelDTO labelDTO = new ApiEvaluationLabelDTO();
				BeanUtils.copyProperties(l, labelDTO);
				labelDTO.setCount(evaluationService.countLabels4Psycho(l.getElId(), pid));
				
				labelDTOs.add(labelDTO);
			}
			//设置标签列表
			statisDTO.setLabelDTOs(labelDTOs);
			
			//计算有效率
			ServiceOrder so = new ServiceOrder();
			so.setServiceProviderId(pid);
			Integer orderCount = serviceOrderService.selectCount(so);
			if(orderCount == 0)
			{//单数0时，置1，避免异常
				orderCount = 1;
			}
			Integer invalidCount = 0;
			for(ApiEvaluationLabelDTO labelDTO : labelDTOs)
			{
				if(labelDTO.getScore() == 0)
				{//分值为0，即"没效果"
					invalidCount = labelDTO.getCount();
					break;
				}
			}
			BigDecimal effectiveRate = BigDecimal.valueOf((double)1 - (double)invalidCount/orderCount);
			//设置有效率
			statisDTO.setEffectiveRate(effectiveRate);
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.put("statisDTO", statisDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 获取咨询师标签
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
			psychoTags = memberTagService.getTagList4Psycho(pageIndex, pageSize, 0);
			count = memberTagService.getTagCount4Psycho(0);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		List<ApiMemberTagDTO> psychoTagDTOs = new ArrayList<ApiMemberTagDTO>();
		for(MemberTag mt : psychoTags)
		{
			ApiMemberTagDTO tagDTO = new ApiMemberTagDTO();
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
	 * 获取咨询师标签
	 * @param session
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainPsychoTagListAndLicenseList.json")
	@ResponseBody
	public Object obtainPsychoTagListAndLicenseListV1()
	{
		ResultEntity result = new ResultEntity();
		
		List<MemberTag> psychoTags = null;
		List<LicenseType> licenseTypes;
		try{
			psychoTags = memberTagService.getTagList4Psycho(1, 20, 0);
			licenseTypes = psychoInfoService.getAllLicenseTypes();
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		//封装标签DTO
		List<ApiMemberTagDTO> psychoTagDTOs = new ArrayList<ApiMemberTagDTO>();
		for(MemberTag mt : psychoTags)
		{
			ApiMemberTagDTO tagDTO = new ApiMemberTagDTO();
			BeanUtils.copyProperties(mt, tagDTO);
			psychoTagDTOs.add(tagDTO);
		}
		result.put("psychoTags", psychoTagDTOs);
		//封装执照类型
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
	 * 解析json数组到List<Long>
	 * @param str
	 * @return
	 */
	private List<Long> parseJsonToLinsLong(String str)
	{
		List<Long> ll = null;
		if(str != null)
		{
			try{
				 ll = JSON.parseArray(str, Long.class);
			}catch(Exception e)
			{
				
			}
		}
		
		return ll;
	}
	
	/**
	 * 搜索咨询师
	 * @param pageIndex
	 * @param pageSize
	 * @param pLevel 0咨询师  1倾听师 99EAP
	 * @param sortMode 1默认 2在线 3最长从业年限 4价格从高到低 5价格从低到高 6最多经验 7最高效率
	 * @param city 城市
	 * @param tagIds 标签id
	 * @param degreeIds 1博士 2硕士 3本科及以下
	 * @param priceFloor 价格下限
	 * @param priceCeil 价格上限
	 * @param sex 1时是男性，2时是女性，0时是未知
	 * @param vid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/searchPsycho.json")
	@ResponseBody
	public Object searchPsychoV1(Integer pageIndex, Integer pageSize, 
			Byte pLevel, 
			Byte sortMode, 
			String city, String tagIdsJsn,
			String licenseIdsJsn,String degreeIdsJsn,
			Integer priceFloor,Integer priceCeil,
			Byte sex,
			Long vid,Long eeId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize,
				pLevel
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(pLevel==99 && vid==null)
		{//EAP查询必须mid
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		//地址做处理, 去掉市
		if(city != null && city.endsWith("市"))
		{
			city = city.substring(0, city.length() -1);
		}
		
		//解析数组
		List<Long> tagIds = parseJsonToLinsLong(tagIdsJsn);
		List<Long> licenseIds = parseJsonToLinsLong(licenseIdsJsn);
		List<Long> degreeIds = parseJsonToLinsLong(degreeIdsJsn);
		
		List<ApiPsychoDTO> psychoDTOs = new ArrayList<ApiPsychoDTO>();
		Integer count;
		try{
			Set<Long> ids = new HashSet<Long>();
			
			//EAP搜索方式 新版本
			if(vid != null  && eeId != null && eeId != 0)
			{
				/*List<Long> pgIds = eapService.obtainPgIds4EapEmployee(vid);
				for(Long pgId : pgIds)
				{
					PsychoGroup psychoGroup = psychoGroupService.obtainPsychoGroup(pgId);
					if(psychoGroup==null || psychoGroup.getIsEnable().equals(1))
					{//组被删除或者禁用
						continue;
					}
					List<Long> pids = psychoGroupService.getPsychoIdOfGroup(pgId);
					ids.addAll(pids);
				}*/
				EapEnterprise ee = eapEnterpriseService.selectByPrimaryKey(eeId);
				List<Long> pids = psychoGroupService.getPsychoIdOfGroup(ee.getPgId());
				ids.addAll(pids);
				
				//兼容老的搜索方式
			}else if(vid != null && eeId == null){
				List<Long> pgIds = eapService.obtainPgIds4EapEmployee(vid);
				for(Long pgId : pgIds)
				{
					PsychoGroup psychoGroup = psychoGroupService.obtainPsychoGroup(pgId);
					if(psychoGroup==null || psychoGroup.getIsEnable().equals(1))
					{//组被删除或者禁用
						continue;
					}
					List<Long> pids = psychoGroupService.getPsychoIdOfGroup(pgId);
					ids.addAll(pids);
				}
			}
			List<Member> psychos;
			if(pLevel==99)
			{//EAP
				List<Long> pIds = new ArrayList<Long>(ids);
				
				psychos = memberService.searchPsychoV1(pageIndex, pageSize, null, sortMode, city, tagIds, licenseIds, degreeIds, priceFloor, priceCeil, sex, pIds);
				count = memberService.countSearchPsychoV1(null, city, tagIds, licenseIds, degreeIds, priceFloor, priceCeil, sex, pIds);
			}else
			{
				//获取心猫公众咨询师id
				List<Long> pids = memberService.selectPsychoIds8Audited((byte) 0);
				//EAP + 心猫公众咨询师
				ids.addAll(pids);
				List<Long> pIds = new ArrayList<Long>(ids);
				
				psychos = memberService.searchPsychoV1(pageIndex, pageSize, pLevel, sortMode, city, tagIds, licenseIds, degreeIds, priceFloor, priceCeil, sex,pIds);
				count = memberService.countSearchPsychoV1(pLevel, city, tagIds, licenseIds, degreeIds, priceFloor, priceCeil, sex, pIds);
			}
			for (Member p : psychos)
			{
				ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
				
				member2PsychoDTO(p, psychoDTO, vid);
				
				psychoDTOs.add(psychoDTO);
			}
			
			
		}catch(Exception e){
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
	 * 搜索咨询师
	 * @param pageIndex
	 * @param pageSize
	 * @param pLevel 0咨询师  1倾听师 99EAP
	 * @param sortMode 1默认 2在线 3最长从业年限 4价格从高到低 5价格从低到高 6最多经验 7最高效率
	 * @param city 城市
	 * @param tagIds 标签id
	 * @param degreeIds 1博士 2硕士 3本科及以下
	 * @param priceFloor 价格下限
	 * @param priceCeil 价格上限
	 * @param sex 1时是男性，2时是女性，0时是未知
	 * @param vid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V2/searchPsycho.json")
	@ResponseBody
	public Object searchPsychoV2(Integer pageIndex, Integer pageSize, 
			Byte pLevel, 
			Byte sortMode, 
			String city, String tagIdsJsn,
			String licenseIdsJsn,String degreeIdsJsn,
			Integer priceFloor,Integer priceCeil,
			Byte sex,
			Long vid,Long eeId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize,
				pLevel,
				eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(pLevel==99 && vid==null)
		{//EAP查询必须mid
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		//地址做处理, 去掉市
		if(city != null && city.endsWith("市"))
		{
			city = city.substring(0, city.length() -1);
		}
		
		//全部查询
		if(pLevel == -1){
			pLevel = null;
		}
		
		//解析数组
		List<Long> tagIds = parseJsonToLinsLong(tagIdsJsn);
		List<Long> licenseIds = parseJsonToLinsLong(licenseIdsJsn);
		List<Long> degreeIds = parseJsonToLinsLong(degreeIdsJsn);
		
		List<ApiPsychoDTO> psychoDTOs = new ArrayList<ApiPsychoDTO>();
		Integer count;
		try{
			
			List<Member> psychos;
			List<Long> pids = null;
			if(eeId != 0 )
			{//EAP
				EapEnterprise ee = eapEnterpriseService.selectByPrimaryKey(eeId);
				pids = psychoGroupService.getPsychoIdOfGroup(ee.getPgId());
			}else
			{
				//获取心猫公众咨询师id
				pids = memberService.selectPsychoIds8Audited((byte) 0);
			}
			psychos = memberService.searchPsychoV1(pageIndex, pageSize, pLevel, sortMode, city, tagIds, licenseIds, degreeIds, priceFloor, priceCeil, sex,pids);
			count = memberService.countSearchPsychoV1(pLevel, city, tagIds, licenseIds, degreeIds, priceFloor, priceCeil, sex, pids);
			for (Member p : psychos)
			{
				ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
				
				member2PsychoDTO(p, psychoDTO, vid);
				
				psychoDTOs.add(psychoDTO);
			}
			
			
		}catch(Exception e){
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
	 * 获取咨询师主页
	 * @param session
	 * @param request
	 * @param pid 咨询师id
	 * @param vid 查询者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainPsychoHomepage.json")
	@ResponseBody
	public Object obtainPsychoHomepageV1(HttpSession session, HttpServletRequest request, Long pid, Long vid)
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
		ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
		member2PsychoDTO(psycho, psychoDTO, vid);
		
		//IM消息
		if(vid != null)
	    {
	    	Member mv = memberService.selectMemberByMid(vid);
	    	if(mv != null)
	    	{
	    		imMessageService.sendViewMsg(mv.getNickname(),vid, mv.getUserType(), psycho.getImAccount());
	    	}
	    }
	
		result.put("psychoDTO", psychoDTO);
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
			EapEnterprise enterprise = eapEnterpriseService.obtainEnterpriseByKey(eeId);
			psychoGroupService.addMemberToGroup(pgId, pid);
			Member psycho = memberService.selectMemberByMid(pid);
			//推送同意后的消息
			//Im 推送
			String state="";
			if(enterprise.getType() == EapEnterpriseService.TYPE_COLLEGE){
			state = 
				String.format("%s老师，您已经同意%s的邀请,成为他们的校园心理专家。你需要免费为其用户提供心理服务。", 
				psycho.getNickname(), enterprise.getName());
			}
			
			if(enterprise.getType() == EapEnterpriseService.TYPE_COMPANY){
			state = 
				String.format("%s老师，您已经同意%s的邀请,成为他们的企业心理专家。你需要免费为其用户提供心理服务。", 
				psycho.getNickname(), enterprise.getName());
			}
			imMessageService.sendInviteSuccessMsg(state, psycho.getImAccount());
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
	 * 每日更新排序因子
	 */
    @Scheduled(cron="0 0 2 * * ? ")	
    public void dailyUpdateOrderFactor()
    {
    	log.info("Update Psycho Order Factor");
    	Integer pageIndex = 1;
    	Integer pageSize = 1000;
    	//查询所有的咨询师
    	while(true)
    	{
	    	Member m = new Member();
	    	m.setUserType((byte) 2);
	    	m.setPageIndex(pageIndex);
	    	m.setPageSize(pageSize);
	    	List<Member> psychos = memberService.selectByPage(m);
	    	if(psychos.size() == 0)
	    	{
	    		break;
	    	}
	    	pageIndex++;
	    	
	    	for(Member p : psychos)
	    	{
	    		try{//所有因子区间归一到100分
		    		//回答问题数
		    		Integer answerCount = p.getAnswerCount();
		    		answerCount = answerCount * 100 / 200;//当前基准200
		    		//是否新咨询师
		    		Date regTime = memberService.selectPsychoRegTime(p.getMid());
		    		Integer isNew = 0;
		    		if(regTime != null)
		    		{//三天内为新咨询师
		    			isNew = (new Date().getTime() - regTime.getTime())/1000/86400 < 3? 1 : 0;
		    		}
		    		isNew =  isNew * 100;
		    		//接单量
					ServicePsychoStatistics pschoStatis = serviceStatisticsService.getOrCreatePsychoStat(p.getMid());
					Integer serviceTimes = pschoStatis.getCommonScoreTimes() + pschoStatis.getEapScoreTimes();
					serviceTimes = serviceTimes * 100 / 50;//当前基准50
					//用户评分
					Double score;
					if(serviceTimes > 10)
					{//10次也上有效
						score = pschoStatis.getCommonScore().doubleValue()*pschoStatis.getCommonScoreTimes();
						score += pschoStatis.getEapScore().doubleValue()*pschoStatis.getEapScoreTimes();
						score /= serviceTimes;
					}else
					{
						score = (double) 0;
					}
					Integer serviceScore = (int) (score * 100 / 5);
					//点赞数
					Integer praiseNum = p.getAppreciatedCount();
					praiseNum = praiseNum * 100 /300;//当前基准300
					//从业年限
					Integer workYears = p.getWorkYears().intValue();
					workYears = workYears * 100 / 10;
					//学历
					Integer degree = 0;
					if(p.getDegree().contains("博士"))
					{
						degree = 3;
					}
					else if(p.getDegree().contains("硕士"))
					{
						degree = 2;
					}
					else if(p.getDegree().contains("本科")||p.getDegree().contains("学士"))
					{
						degree = 1;
					}
					degree = degree * 100 / 3;
					//计算排序因子
					Random random = new Random();
					Integer orderFactor = 
							(int) ((answerCount + isNew * 0.5 + serviceTimes + serviceScore + praiseNum + workYears + degree * 0.5)
							+ random.nextInt(400));
					
		    		p.setOrderFactor(orderFactor);
		    		memberService.update(p);
	    		}catch(Exception e)
	    		{
	    			continue;
	    		}
	    	}
	    }
    }
}

