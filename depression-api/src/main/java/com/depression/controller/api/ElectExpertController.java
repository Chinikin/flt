package com.depression.controller.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.MembersOnlineStatus;
import com.depression.entity.OrderState;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.Member;
import com.depression.model.MemberConcern;
import com.depression.model.MemberTag;
import com.depression.model.PsychoRecommend;
import com.depression.model.Recommend;
import com.depression.model.ServiceCallRecord;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.api.dto.ApiCapitalCouponEntityDTO;
import com.depression.model.api.dto.ApiMemberTagDTO;
import com.depression.model.api.dto.ApiPsychoDTO;
import com.depression.model.api.dto.ApiServiceCallDTO;
import com.depression.service.AdvisoryService;
import com.depression.service.CapitalCouponService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.EapService;
import com.depression.service.ElectExpertService;
import com.depression.service.IMMessageService;
import com.depression.service.MemberConcernService;
import com.depression.service.MemberService;
import com.depression.service.MemberTagService;
import com.depression.service.PsychoInfoService;
import com.depression.service.ServiceCallRecordService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.AliyunIMUtil;
import com.depression.utils.CallCommunicationUtil;
import com.depression.utils.HtmlUtil;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/electExpert")
public class ElectExpertController {
	
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	ElectExpertService electExpertService;
	@Autowired
	MemberService memberService;
	@Autowired
	MemberTagService memberTagService;
	@Autowired
	PsychoInfoService psychoInfoService;
	@Autowired
	ServiceGoodsService serviceGoodsService;
	@Autowired
	ServiceOrderService serviceOrderService;
	@Autowired
	MemberConcernService memberConcernService;
	@Autowired
	AdvisoryService memberAdvisoryService;
	@Autowired
	IMMessageService imMessageService;
	@Autowired
	ServiceCallRecordService serviceCallRecordService;
	@Autowired
	ServiceStatisticsService serviceStatisticsService;
	@Autowired
	EapService eapService;
	@Autowired
	CapitalCouponService capitalCouponService;
	@Autowired
	CapitalPersonalAssetsService capitalPersonalAssetsService;
	//获取今日推选专家 10个
	@RequestMapping(method = RequestMethod.POST, value = "/getElectExpert.json")
	@ResponseBody
	public Object getElectExpert(){
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try{			
		    PsychoRecommend psychoRecommend = new PsychoRecommend();
		    psychoRecommend.setPageIndex(1);
		    psychoRecommend.setPageSize(10);
		    List<Member> list = electExpertService.getElectExpertUser(psychoRecommend);
		    Recommend recommend = electExpertService.getRecommend();
		    result.put("topic",recommend.getTitle());
		    result.put("isOpen", recommend.getIsOpened());
		    result.put("psychoRecommends",list);
		}catch(Exception e){
			result.setCode(ResultEntity.ERROR);
			result.setError("获取推选专家失败");
		}
		return result;
	}
	
	//获取推选主题
	@RequestMapping(method = RequestMethod.POST, value = "/getTopic.json")
	@ResponseBody
	public Object getTopic(){
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try{
			Recommend recommend = electExpertService.getRecommend();
			if(recommend!=null){
			result.put("topic",recommend.getTitle());
			result.put("picture", recommend.getCover());
			}
			}catch(Exception e){
				result.setCode(ResultEntity.ERROR);
				result.setError("获取推选主题失败");
			}
			return result;
	}
	
	//获取推选专家列表
	@RequestMapping(method = RequestMethod.POST, value = "/getElectConsultant.json")
	@ResponseBody
	public Object getElectConsultant(Integer pageIndex,Integer	pageSize,Long mid){
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		List<Member> members;
		Integer count;
		try{
			//查询所有推选专家
			List<Long> ids = electExpertService.getAllMid();
			members = memberService.getByKeysSortableWithPageEnabled(ids, pageIndex, pageSize, null, null);
			count = memberService.getCountByPrimaryKeysEnabled(ids, null);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		Recommend recommend = electExpertService.getRecommend();

		BigDecimal favourablePrice = recommend.getPrice();
		String priceName =recommend.getPriceName();		
		List<ApiPsychoDTO> psychoDTOs = new ArrayList<ApiPsychoDTO>();
		for (Member m : members)
		{
			ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
			
			member2PsychoDTO(m, psychoDTO, mid,favourablePrice,priceName);
			
			psychoDTOs.add(psychoDTO);
		}
		if(recommend!=null){				
		result.put("contextpicture", recommend.getCover());		
		result.put("detailLink", recommend.getDetailLink());
		}
		//查询推选价格
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	public void member2PsychoDTO(Member m, ApiPsychoDTO p, Long mid,BigDecimal favourablePrice,String priceName)
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
		Recommend recommends = electExpertService.getRecommend();
		//
		p.setPriceOpened(recommends.getPriceOpened());
		//价格名目
		p.setPriceName(priceName);
		//推选价格
		p.setFavourablePrice(favourablePrice);
		//剩余人数
		PsychoRecommend psychoRecommends = new PsychoRecommend();
		psychoRecommends.setMid(m.getMid());
		PsychoRecommend psychoRecommend = electExpertService.getNum(psychoRecommends);
		p.setSurplusNum(psychoRecommend.getRemainNumber());
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
		Recommend recommend = electExpertService.getRecommend();

		BigDecimal favourablePrice = recommend.getPrice();
		String priceName = recommend.getPriceName();		
		ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
		member2PsychoDTO(member, psychoDTO, mid,favourablePrice,priceName);
		
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
	
	/**
	 * 正常首次拨打电话 正常创建订单 然后拨打电话
	 * 
	 * @param session
	 * @param request
	 * @return
	 * @throws ParseException 
	 */	
	//新版本
	@RequestMapping(value = "/callStartSelect.json")
	@ResponseBody
	public Object callStartSelect(HttpSession session, HttpServletRequest request, Long callerId, Long calledId, Integer callType,Integer callFlag) throws ParseException
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查		
		if (PropertyUtils.examineOneNull(callerId, calledId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}		

		// 查询主叫人信息
		Member caller = memberService.selectMemberByMid(callerId);
		if (caller == null || caller.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setMsg("主叫人信息未找到");
			return result;
		}

		// 查询被叫人信息
		Member called = memberService.selectMemberByMid(calledId);
		if (called == null || called.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("被叫人信息未找到");
			return result;
		}
		//咨询师是否被禁用
		if (called.getIsEnable().intValue() == 1)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("此专家违反平台规则，已被禁止提供咨询服务");
			return result;
		}

		// 查询咨询师是否离线
		Byte calledStatus = called.getStatus();
		if (calledStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode()))
		{
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR.getCode());
			result.setMsg("被叫人离线");
			return result;
		}

		// 查询服务提供者提供的服务，目前只有一条商品记录
		// 该记录在咨询师设置服务价格时，由系统自动插入
		ServiceGoods serviceGoods = serviceGoodsService
				.selectImmVoiceByMid(calledId);
		if (serviceGoods == null) {
			result.setCode(ErrorCode.ERROR_SERVICE_GOODS_INFO_INEXISTENT
					.getCode());
			result.setMsg("服务商品信息未找到");
			return result;
		}
		try
		{
		// 加锁抢占咨询师
		Byte oldStatus = memberService.transCasStatus(calledId,
				MembersOnlineStatus.STATUS_ONLINE.getCode(),
				MembersOnlineStatus.STATUS_IN_THE_CALL.getCode());
		if (oldStatus.equals(MembersOnlineStatus.STATUS_IN_THE_CALL.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_BUSY_ERROR
					.getCode());
			result.setMsg("被叫方正忙，请稍后再拨");
			return result;
		} else if (oldStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE
				.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR
					.getCode());
			result.setMsg("被叫人不在线");
			return result;
		}

		// 查询是否尚有未完成订单
		ServiceOrder queryServiceOrder = new ServiceOrder();
		queryServiceOrder.setMid(caller.getMid());// 主叫人id
		queryServiceOrder.setServiceProviderId(called.getMid());//被叫人id
		ServiceOrder existServiceOrder = serviceOrderService.selectSelectUnCompleteOrderByServiceProviderId(queryServiceOrder);
		if (existServiceOrder != null) // 订单已创建
		{
			//刷新订单状态
			serviceOrderService.refreshOrderStatus(existServiceOrder.getSoid()); 
			//重新查询订单
			existServiceOrder = serviceOrderService.selectOrderByPrimaryKey(existServiceOrder.getSoid());
			// 检查订单是否已经结束
			if (serviceOrderService.isUncompletedStatus(existServiceOrder.getStatus()))
			{//订单未结束，继续订单流程
				long timeRemind = countCallTimeRemind(serviceGoods, existServiceOrder); // 电话剩余时长
				ApiServiceCallDTO callDTO = CallCommunicationUtil.callServiceByalidayu(caller.getMobilePhone(), called.getMobilePhone(), Integer.parseInt(timeRemind + ""),callFlag);
				if (callDTO == null || callDTO.getCallsid() == null)
				{//拨打失败，还原咨询师状态
					memberService.transCasStatus(calledId,MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),MembersOnlineStatus.STATUS_ONLINE.getCode());
					result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
					result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
					return result;
				}
				//计算允许通话时间戳
				Date lowDate = AliyunIMUtil.getLowDate(timeRemind);
				// 新增通话记录		
				String[] str = callDTO.getRecordUrl().split("@");
				ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
				serviceCallRecord.setServiceOrderId(existServiceOrder.getSoid());
				serviceCallRecord.setCallsid(callDTO.getCallsid());
				serviceCallRecord.setOrderId(callDTO.getOrderId()+"@"+serviceGoods.getDuration());
				serviceCallRecord.setCaller(callDTO.getCaller());
				serviceCallRecord.setCalled(callDTO.getCalled());
				serviceCallRecord.setRecordUrl(callDTO.getRecordUrl());
				serviceCallRecord.setBeginTime(lowDate);
				serviceCallRecord.setIsCalling((byte) 1);
				serviceCallRecordService.insertSelective(serviceCallRecord);
				result.put("orderNo", existServiceOrder.getNo());
				result.put("specific_x",str[2]);
				return result;
			}			
		 } 
		} catch (Exception e)
		{//拨打失败，还原咨询师状态
			memberService.transCasStatus(calledId, 
					MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
					MembersOnlineStatus.STATUS_ONLINE.getCode()
					);
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			return result;
		}
			
		// 新订单
		{
			try
			{
			Map<String, Long> mapStatusAndEeId = eapService.obtainServiceStatusAndEeId(callerId, calledId);
			Long eeId = mapStatusAndEeId.get("eeId");
			//订单金额
			Recommend recommend = electExpertService.getRecommend();
			int isOpenPrice = recommend.getPriceOpened();
			if(isOpenPrice==1){
			
			}
			// 新建订单对象		
			ServiceOrder serviceOrder = new ServiceOrder();
			serviceOrder.setSgid(serviceGoods.getSgid());
			serviceOrder.setMid(callerId);
			serviceOrder.setServiceProviderId(calledId);
			serviceOrder.setNo(Constant.idWorker.nextId() + "");
			serviceOrder.setCreateTime(new Date());
			serviceOrder.setServiceBeginTime(new Date());
			serviceOrder.setServiceRealityBeginTime(new Date());
			serviceOrder.setGoodsQuantity(1); // 目前商品数量设置为1，后期需求有可能会变为多个商品数量
			
			if(isOpenPrice==1){
				serviceOrder.setCost(recommend.getPrice());
				serviceOrder.setOrderType(new Byte("2"));
			}else{
				serviceOrder.setCost(serviceGoods.getPrice().multiply(new BigDecimal(serviceOrder.getGoodsQuantity())));
				serviceOrder.setOrderType(new Byte("0"));
			}
			serviceOrder.setEeId((long) 0);
			serviceOrder.setStatus(OrderState.STATE_CALL_NOT_CONNECTED.getCode()); // 首次拨打，将订单状态设置为“未接通”
			
				
					serviceOrder.setIsRecommend(new Byte("1"));
					serviceOrder.setEeId((long) 0);
					serviceStatisticsService.updateTimes(callerId, calledId, 0, eeId);
				
				serviceOrderService.insert(serviceOrder);
                
				
				// 拨打电话
				ApiServiceCallDTO callDTO = CallCommunicationUtil.callServiceByalidayu(caller.getMobilePhone(), called.getMobilePhone(), serviceGoods.getDuration() * 60,callFlag);
				if (callDTO == null || callDTO.getCallsid() == null)
				{//拨打失败，还原咨询师状态
					memberService.transCasStatus(calledId, 
							MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
							MembersOnlineStatus.STATUS_ONLINE.getCode()
							);
					result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
					result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
					return result;
				}
				//计算允许通话时间戳
				Date lowDate = AliyunIMUtil.getLowDate(serviceGoods.getDuration() * 60);
				// 新增订单通话记录
				ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
				serviceCallRecord.setServiceOrderId(serviceOrder.getSoid());
				serviceCallRecord.setCallsid(callDTO.getCallsid());
				serviceCallRecord.setOrderId(callDTO.getOrderId()+"@"+serviceGoods.getDuration());
				serviceCallRecord.setCaller(callDTO.getCaller());
				serviceCallRecord.setCalled(callDTO.getCalled());
				serviceCallRecord.setRecordUrl(callDTO.getRecordUrl());
				serviceCallRecord.setBeginTime(lowDate);
				serviceCallRecord.setIsCalling((byte) 1);
				serviceCallRecordService.insertSelective(serviceCallRecord);
				String[] str = callDTO.getRecordUrl().split("@");
                result.put("specific_x",str[2]);
                //记录callsid
				//session.setAttribute("callsid", callDTO.getCallsid());
				// 更新咨询数量
				if (serviceGoods != null)
				{
					if (serviceGoods.getTimes() != null)
					{
						serviceGoods.setTimes(serviceGoods.getTimes() + 1);
					} else
					{
						serviceGoods.setTimes(1);
					}
				}
				//更新今日剩余数
				electExpertService.transChangeNum(calledId);
				serviceGoodsService.updateByPrimaryKey(serviceGoods);
				result.put("orderNo", serviceOrder.getNo());
			} catch (Exception e)
			{//拨打失败，还原咨询师状态
				memberService.transCasStatus(calledId, 
						MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
						MembersOnlineStatus.STATUS_ONLINE.getCode()
						);
				result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
				return result;
			}
			
		}
		
		return result;
	}

	/**
	 * 处于进行中的订单，还在时效范围内， 再次拨打电话
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/callReStartSelect.json")
	@ResponseBody
	public Object callReStartByaliyun(HttpSession session, HttpServletRequest request, String orderId,Integer callType,Integer callFlag)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(orderId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 查询订单
		ServiceOrder serviceOrder = serviceOrderService.selectByNo(orderId);
		if (serviceOrder == null)
		{
			result.setCode(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getMessage());
			return result;
		}
		
		//刷新订单状态
		serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
		//重新查询
		serviceOrder = serviceOrderService.selectOrderByPrimaryKey(serviceOrder.getSoid());
		// 检查订单是否已经结束
		if (!serviceOrderService.isUncompletedStatus(serviceOrder.getStatus()))
		{
			result.setCode(ErrorCode.ERROR_CALL_SERVICE_IS_OVER.getCode());
			result.setMsg(ErrorCode.ERROR_CALL_SERVICE_IS_OVER.getMessage());
			return result;
		}

		// 查询订单的商品信息
		ServiceGoods serviceGoods = serviceOrderService.selectGoodsByPrimaryKey(serviceOrder.getSgid());

		// 查询主叫人信息
		Member caller = memberService.selectMemberByMid(serviceOrder.getMid());
		if (caller == null || caller.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getMessage());
			return result;
		}

		// 查询被叫人状态
		Member called = memberService.selectMemberByMid(serviceOrder.getServiceProviderId());
		if (called == null || called.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getMessage());
			return result;
		}
		
		if (called.getIsEnable().intValue() == 1) {
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("此专家违反平台规则，已被禁止提供咨询服务");
			return result;
		}

		// 加锁抢占咨询师
		Byte oldStatus = memberService.transCasStatus(
				serviceOrder.getServiceProviderId(),
				MembersOnlineStatus.STATUS_ONLINE.getCode(),
				MembersOnlineStatus.STATUS_IN_THE_CALL.getCode());
		if (oldStatus.equals(MembersOnlineStatus.STATUS_IN_THE_CALL.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_BUSY_ERROR.getCode());
			result.setMsg("被叫方正忙，请稍后再拨");
			return result;
		} else if (oldStatus.equals(MembersOnlineStatus.STATUS_NOT_ONLINE.getCode())) {
			result.setCode(ErrorCode.ERROR_SERVICE_PROVIDER_OFFLINE_ERROR.getCode());
			result.setMsg("被叫人不在线");
			return result;
		}
		try
		{			
			// 拨打电话
			long timeRemind = countCallTimeRemind(serviceGoods, serviceOrder); // 电话剩余时长
			ApiServiceCallDTO callDTO = CallCommunicationUtil.callServiceByalidayu(caller.getMobilePhone(), called.getMobilePhone(), Integer.parseInt(timeRemind + ""),callFlag);
			if (callDTO == null || callDTO.getCallsid() == null)
			{   //拨打失败，还原咨询师状态
				memberService.transCasStatus(serviceOrder.getServiceProviderId(),MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),MembersOnlineStatus.STATUS_ONLINE.getCode());				
				result.setCode(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getCode());
				result.setMsg(ErrorCode.ERROR_CALL_SERVICE_CONNECTION_ERROR.getMessage());
				return result;
			}
			//计算允许通话时间戳
			Date lowDate = AliyunIMUtil.getLowDate(timeRemind);
			// 新增通话记录
			String[] str = callDTO.getRecordUrl().split("@");
			ServiceCallRecord serviceCallRecord = new ServiceCallRecord();
			serviceCallRecord.setServiceOrderId(serviceOrder.getSoid());
			serviceCallRecord.setCallsid(callDTO.getCallsid());
			serviceCallRecord.setOrderId(callDTO.getOrderId()+"@"+serviceGoods.getDuration());
			serviceCallRecord.setCaller(callDTO.getCaller());
			serviceCallRecord.setCalled(callDTO.getCalled());
			serviceCallRecord.setRecordUrl(callDTO.getRecordUrl());
			serviceCallRecord.setBeginTime(lowDate);
			serviceCallRecord.setIsCalling((byte) 1);
			serviceCallRecordService.insertSelective(serviceCallRecord);
			result.put("orderNo", serviceOrder.getNo());
            result.put("specific_x",str[2]);
		} catch (Exception e)
		{
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
	 * 获取活动订单支付信息
	 * @param mid
	 * @param price
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getPaymentInfoSelect.json")
	@ResponseBody
	public Object getPaymentInfoSelect(Long mid, Long calledId,  BigDecimal price) throws ParseException
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查
		if (PropertyUtils.examineOneNull(mid, calledId, price))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 查询主叫人信息
		Member queryMember = new Member();
		queryMember.setMid(mid);
		Member caller = memberService.getMember(queryMember);
		if (caller == null || caller.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLER_INFO_INEXISTENT.getCode());
			result.setMsg("主叫人信息未找到");
			return result;
		}

		//新建订单前判断今天是否新建过订单（规则每日每人只能产生一单）
		ServiceOrder so = new ServiceOrder();
		so.setMid(caller.getMid());// 主叫人id
		so.setOrderType((byte) 2);		
		ServiceOrder thisSO = serviceOrderService.countServiceOrderElect(so);			
		if(thisSO!=null){
			if(!thisSO.getServiceProviderId().equals(calledId)){
			result.setCode(ErrorCode.ERROR_CALL_SERVICE_ELECT_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_CALL_SERVICE_ELECT_ERROR.getMessage());
			memberService.transCasStatus(calledId, 
					MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
					MembersOnlineStatus.STATUS_ONLINE.getCode()
					);
			return result;
			}
		}else{
			//查看该咨询师剩余名额是否允许接听
			PsychoRecommend psychoRecommends = new PsychoRecommend();
			psychoRecommends.setMid(calledId);
			PsychoRecommend psychoRecommend = electExpertService.getNum(psychoRecommends);
			if(psychoRecommend.getRemainNumber()==0){
				result.setCode(ErrorCode.ERROR_CALL_SERVICE_OUT_MAX.getCode());
				result.setMsg(ErrorCode.ERROR_CALL_SERVICE_OUT_MAX.getMessage());
			}
		}
		boolean flag = electExpertService.IsOutnumber(caller.getMid());
		if(!flag){
			result.setCode(ErrorCode.ERROR_CALL_SERVICE_OUT_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_CALL_SERVICE_OUT_ERROR.getMessage());
			memberService.transCasStatus(calledId, 
					MembersOnlineStatus.STATUS_IN_THE_CALL.getCode(),
					MembersOnlineStatus.STATUS_ONLINE.getCode()
					);
			return result;
		}
		// 查询被叫人信息
		queryMember = new Member();
		queryMember.setMid(calledId);
		Member called = memberService.getMember(queryMember);
		if (called == null || called.getMobilePhone() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("被叫人信息未找到");
			return result;
		}
		if (called.getStatus() == null)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("该咨询师不在线");
			return result;
		}
		if (called.getIsEnable().intValue() == 1 || called.getIsDelete().intValue() == 1)
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("此专家违反平台规则，已被禁止提供咨询服务");
			return result;
		}
		
		// 查询咨询师是否离线
		Byte calledStatus = called.getStatus();
		if (calledStatus.intValue() == MembersOnlineStatus.STATUS_NOT_ONLINE.getCode().intValue())
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("该咨询师已离线");
			return result;
		}
		if (calledStatus.intValue() == MembersOnlineStatus.STATUS_IN_THE_CALL.getCode().intValue())
		{
			result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
			result.setMsg("该咨询师通话中");
			return result;
		}

		// 查询服务提供者提供的服务，目前只有一条商品记录
		// 该记录在咨询师设置服务价格时，由系统自动插入
		ServiceGoods serviceGoods = serviceGoodsService.selectImmVoiceByMid(calledId);
		if (serviceGoods == null)
		{
			result.setCode(ErrorCode.ERROR_SERVICE_GOODS_INFO_INEXISTENT.getCode());
			result.setMsg("服务商品信息未找到");
			return result;
		}
		
		// 检查是否eap客户，如果是，不需要扣款
		/*Map<String, Long> mapStatusAndEeId = eapService.obtainServiceStatusAndEeId(mid, calledId);
		Long serviceStatus = mapStatusAndEeId.get("status");
		if (serviceStatus.longValue() == 1) // eap订单, 不需要扣款
		{
			result.put("needTopUp", 2);
			result.put("cashBalance", 0);
			result.put("discount", 0);
			return result;
		}*/
		
		// 查询是否尚有未完成订单，如果有，返回该订单
		ServiceOrder queryServiceOrder = new ServiceOrder();
		queryServiceOrder.setMid(caller.getMid());// 主叫人id
		queryServiceOrder.setServiceProviderId(called.getMid());// 被叫人id
		queryServiceOrder.setOrderType((byte) 2);
		ServiceOrder existServiceOrder = serviceOrderService.selectSelectUnCompleteOrderByServiceProviderId(queryServiceOrder);
		if (existServiceOrder != null) // 存在订单
		{
			// 更新订单状态
			boolean timeoutFlag = checkCallServiceTimeout(serviceGoods, existServiceOrder);
			if (!timeoutFlag) 
				// 通话订单进行中且未超时，不需要扣款，可以继续拨打
			{

				result.put("needTopUp", 2);
				result.put("cashBalance", 0);
				result.put("discount", 0);
				return result;

			} else // 通话订单已超时或当前未扣款，需要扣款，检查优惠券和用户的个人账户信息，返回预支付信息
			{
				CapitalCouponEntity capitalCoupon = capitalCouponService.getValuabestUsableCashCouponEntity(mid);
				CapitalPersonalAssets personalAssets = capitalPersonalAssetsService.selectByMid(mid);
				if (personalAssets == null)
				{
					result.setCode(ErrorCode.ERROR_CAPITAL_PERSONAL_ASSETS_INEXISTENT.getCode());
					result.setMsg(ErrorCode.ERROR_CAPITAL_PERSONAL_ASSETS_INEXISTENT.getMessage());
					return result;
				}
				ApiCapitalCouponEntityDTO couponEntityDTO = new ApiCapitalCouponEntityDTO(); 
				BigDecimal discount;
				if (capitalCoupon == null)
				{
					discount = new BigDecimal(0);
				} else
				{
					discount = capitalCoupon.getDiscount();
					BeanUtils.copyProperties(capitalCoupon, couponEntityDTO);
				}
				Integer needTopUp = price.compareTo(personalAssets.getCashBalance().add(discount));
				result.put("cashBalance", personalAssets.getCashBalance());
				result.put("discount", discount);
				result.put("needTopUp", needTopUp < 1 ? 0 : 1);
				result.put("couponEntityDTO", couponEntityDTO);
			}
		} else // 不存在通话订单记录
		{
			// 只有主叫人和订单里的主叫人是同一个人，在咨询师在通话中状态，才能拨打电话
			if (called.getStatus().intValue() != MembersOnlineStatus.STATUS_ONLINE.getCode().intValue())
			{
				result.setCode(ErrorCode.ERROR_CALLED_INFO_INEXISTENT.getCode());
				if (called.getStatus().intValue() == MembersOnlineStatus.STATUS_IN_THE_CALL.getCode().intValue())
				{
					result.setMsg("该咨询师通话中");
				} else if (called.getStatus().intValue() == MembersOnlineStatus.STATUS_NOT_ONLINE.getCode().intValue())
				{
					result.setMsg("该咨询师已离线");
				}

				return result;
			}
			
			// 返回预支付信息
			CapitalCouponEntity capitalCoupon = capitalCouponService.getValuabestUsableCashCouponEntity(mid);
			CapitalPersonalAssets personalAssets = capitalPersonalAssetsService.selectByMid(mid);
			if (personalAssets == null)
			{
				result.setCode(ErrorCode.ERROR_CAPITAL_PERSONAL_ASSETS_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_CAPITAL_PERSONAL_ASSETS_INEXISTENT.getMessage());
				return result;
			}
			ApiCapitalCouponEntityDTO couponEntityDTO = new ApiCapitalCouponEntityDTO(); 
			BigDecimal discount;
			if (capitalCoupon == null)
			{
				discount = new BigDecimal(0);
			}else if(capitalCoupon.getDiscount().compareTo(price) >= 0)
			{//咨询券额度大于等于价格，不能使用
				discount = new BigDecimal(0);
			}
			else
			{
				discount = capitalCoupon.getDiscount();
				BeanUtils.copyProperties(capitalCoupon, couponEntityDTO);
			}
			
			Integer needTopUp = price.compareTo(personalAssets.getCashBalance().add(discount));
			result.put("cashBalance", personalAssets.getCashBalance());
			result.put("discount", discount);
			result.put("needTopUp", needTopUp < 1 ? 0 : 1);
			result.put("couponEntityDTO", couponEntityDTO);
		}
		
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
	 * 计算电话服务剩余时间
	 * 
	 * @param serviceGoods
	 * @param existServiceOrder
	 * @return
	 */
	private long countCallTimeRemind(ServiceGoods serviceGoods, ServiceOrder existServiceOrder)
	{
		Long maxDuration = Long.parseLong(serviceGoods.getDuration() * 60 + "");
		Long curDur = existServiceOrder.getPracticalDuration().longValue();
		Long timeRemind = maxDuration - curDur;
		return timeRemind;
	}
}
