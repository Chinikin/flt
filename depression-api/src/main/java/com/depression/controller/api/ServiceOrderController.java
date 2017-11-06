package com.depression.controller.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.MemberType;
import com.depression.entity.MembersOnlineStatus;
import com.depression.entity.OrderState;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.EvaluationLabel;
import com.depression.model.Member;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.ServiceOrderEvaluation;
import com.depression.model.api.dto.ApiCapitalCouponEntityDTO;
import com.depression.model.api.dto.ApiServiceOrderDTO;
import com.depression.service.CapitalCouponService;
import com.depression.service.CapitalIncomeExpenseService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.EapService;
import com.depression.service.MemberService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderEvaluationService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.PropertyUtils;

/**
 * 服务订单
 * 
 * @author xinhui_fan
 * 
 */
@Controller
@RequestMapping("/serviceOrder")
public class ServiceOrderController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ServiceOrderService serviceOrderService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CapitalCouponService capitalCouponService;
	
	@Autowired
	private CapitalPersonalAssetsService capitalPersonalAssetsService;
	
	@Autowired
	private ServiceGoodsService serviceGoodsService;
	
	@Autowired
	private ServiceOrderEvaluationService serviceOrderEvaluationService;
		
	@Autowired
	private ServiceStatisticsService serviceStatisticsService;
	
	@Autowired
	private EapService eapService;
	
	@Autowired
	private CapitalIncomeExpenseService capitalIncomeExpenseService;

	/**
	 * 查询客户订单列表
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/getCallOrderForCusList.json")
	@ResponseBody
	public Object getCallOrderForCusList(HttpSession session, HttpServletRequest request, Long mid, ServiceOrder order)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			// 参数检查
			if (null == mid)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setError("会员id不能为空");
				return result;
			}
			if (order.getPageIndex() == null)
			{
				result.setCode(-1);
				result.setMsg("分页页码不能为空");
				return result;
			}
			if (order.getPageSize() == null)
			{
				result.setCode(-1);
				result.setMsg("每页条数不能为空");
				return result;
			}

			// 查询用户信息
			Member queryMember = new Member();
			queryMember.setMid(mid);
			Member member = memberService.getMember(queryMember);
			if (member == null)
			{
				result.setCode(ErrorCode.ERROR_USERINFO_NOT_FOUND.getCode());
				result.setError("用户信息未找到");
				return result;
			}

			// 查询普通会员的订单列表
			Byte userType = member.getUserType();
			ServiceOrder queryOrder = new ServiceOrder();
			if (userType.intValue() == MemberType.TYPE_ORDINARY.getCode().intValue())// 普通会员
			{
				queryOrder.setMid(mid);
			} else 
			{
				result.setCode(ErrorCode.ERROR_USERINFO_NOT_FOUND.getCode());
				result.setError("用户类型不匹配");
				return result;
			}
			if (order != null && order.getStatus() != null)
			{
				queryOrder.setStatus(order.getStatus());
			}
			if (order != null && order.getPageIndex() != null)
			{
				queryOrder.setPageIndex(order.getPageIndex());
			}
			if (order != null && order.getPageSize() != null)
			{
				queryOrder.setPageSize(order.getPageSize());
			}
			queryOrder.setIsEnable(new Byte("0"));
			queryOrder.setInvisibleUser((byte) 0);
			List<ServiceOrder> orders = serviceOrderService.selectSelective3Page0TmDesc(queryOrder);
			int count = serviceOrderService.selectCount(queryOrder);
			List<ApiServiceOrderDTO> orderDTOList = new ArrayList<>();
			for (ServiceOrder serviceOrder : orders)
			{
				ApiServiceOrderDTO orderDTO = new ApiServiceOrderDTO();
				
				// 查询订单对应商品信息
				Long serviceGoodsId = serviceOrder.getSgid();
				ServiceGoods serviceGoods = serviceGoodsService.selectByPrimaryKey(serviceGoodsId);
				
				// 刷新订单状态
				serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
				
				// 查询订单组装数据
				BeanUtils.copyProperties(serviceOrder, orderDTO);
				if (serviceGoods != null)
				{
					orderDTO.setGoodsType(serviceGoods.getType());// 商品类型
					orderDTO.setGoodsPrice(serviceGoods.getPrice());// 商品价格
					orderDTO.setRemainingDuration(serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration());// 剩余服务时长
				}
				

				// 获取消费者信息
				Member consumers = memberService.selectMemberByMid(orderDTO.getMid());
				if (null != consumers)
				{
					orderDTO.setConsumersName(consumers.getNickname());
					orderDTO.setConsumersPhone(consumers.getMobilePhone());
					String avatar = consumers.getAvatar();
					if (!StringUtils.isEmpty(avatar))
					{
						orderDTO.setConsumersAvatar(avatar);
					} else
					{
						orderDTO.setConsumersAvatar("");
					}
					orderDTO.setConsumersImAccount(consumers.getImAccount());
					orderDTO.setConsumersType(consumers.getUserType());
				}

				// 获取专家信息
				Member specialist = memberService.selectMemberByMid(orderDTO.getServiceProviderId());
				if (null != specialist)
				{
					orderDTO.setSpecialistName(specialist.getNickname());
					orderDTO.setSpecialistPhone(specialist.getMobilePhone());
					orderDTO.setSpecialistTitle(specialist.getTitle());
					String avatar = specialist.getAvatar();
					if (!StringUtils.isEmpty(avatar))
					{
						orderDTO.setSpecialistAvatar(avatar);
					} else
					{
						orderDTO.setSpecialistAvatar("");
					}
					orderDTO.setSpecialistImAccount(specialist.getImAccount());
					orderDTO.setSpecialistType(specialist.getUserType());
				}
				orderDTOList.add(orderDTO);
			}
			result.setCount(count);
			result.setList(orderDTOList);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error("System error:" + e);
		}
		return result;
	}
	
	/**
	 * 新版本
	 * 查询客户订单列表
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/V1/getCallOrderForCusListByVersion.json")
	@ResponseBody
	public Object getCallOrderForCusListByVersion(HttpSession session, HttpServletRequest request, Long mid,Long eeid, ServiceOrder order)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			// 参数检查
			if (null == mid)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setError("会员id不能为空");
				return result;
			}
			if (order.getPageIndex() == null)
			{
				result.setCode(-1);
				result.setMsg("分页页码不能为空");
				return result;
			}
			if(null == eeid){
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setError("会员EEID不能为空");
				return result;
			}
			if (order.getPageSize() == null)
			{
				result.setCode(-1);
				result.setMsg("每页条数不能为空");
				return result;
			}

			// 查询用户信息
			Member queryMember = new Member();
			queryMember.setMid(mid);
			Member member = memberService.getMember(queryMember);
			if (member == null)
			{
				result.setCode(ErrorCode.ERROR_USERINFO_NOT_FOUND.getCode());
				result.setError("用户信息未找到");
				return result;
			}

			// 查询普通会员的订单列表
			Byte userType = member.getUserType();
			ServiceOrder queryOrder = new ServiceOrder();
			if (userType.intValue() == MemberType.TYPE_ORDINARY.getCode().intValue())// 普通会员
			{
				queryOrder.setMid(mid);
			} else 
			{
				result.setCode(ErrorCode.ERROR_USERINFO_NOT_FOUND.getCode());
				result.setError("用户类型不匹配");
				return result;
			}
			if (order != null && order.getStatus() != null)
			{
				queryOrder.setStatus(order.getStatus());
			}
			if (order != null && order.getPageIndex() != null)
			{
				queryOrder.setPageIndex(order.getPageIndex());
			}
			if (order != null && order.getPageSize() != null)
			{
				queryOrder.setPageSize(order.getPageSize());
			}
			queryOrder.setIsEnable(new Byte("0"));
			queryOrder.setInvisibleUser((byte) 0);
			queryOrder.setEeId(eeid);
			List<ServiceOrder> orders = serviceOrderService.selectSelective3Page0TmDesc(queryOrder);
			int count = serviceOrderService.selectCount(queryOrder);
			List<ApiServiceOrderDTO> orderDTOList = new ArrayList<>();
			for (ServiceOrder serviceOrder : orders)
			{
				ApiServiceOrderDTO orderDTO = new ApiServiceOrderDTO();
				
				// 查询订单对应商品信息
				Long serviceGoodsId = serviceOrder.getSgid();
				ServiceGoods serviceGoods = serviceGoodsService.selectByPrimaryKey(serviceGoodsId);
				
				// 刷新订单状态
				serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
				
				// 查询订单组装数据
				BeanUtils.copyProperties(serviceOrder, orderDTO);
				orderDTO.setServiceRealityBeginTimestamp(serviceOrder.getServiceRealityBeginTime().getTime());
				
				if (serviceGoods != null)
				{
					orderDTO.setGoodsType(serviceGoods.getType());// 商品类型
					orderDTO.setGoodsPrice(serviceGoods.getPrice());// 商品价格
					orderDTO.setRemainingDuration(serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration());// 剩余服务时长
				}
				

				// 获取消费者信息
				Member consumers = memberService.selectMemberByMid(orderDTO.getMid());
				if (null != consumers)
				{
					orderDTO.setConsumersName(consumers.getNickname());
					orderDTO.setConsumersPhone(consumers.getMobilePhone());
					String avatar = consumers.getAvatar();
					if (!StringUtils.isEmpty(avatar))
					{
						orderDTO.setConsumersAvatar(avatar);
					} else
					{
						orderDTO.setConsumersAvatar("");
					}
					orderDTO.setConsumersImAccount(consumers.getImAccount());
					orderDTO.setConsumersType(consumers.getUserType());
				}

				// 获取专家信息
				Member specialist = memberService.selectMemberByMid(orderDTO.getServiceProviderId());
				if (null != specialist)
				{
					orderDTO.setSpecialistName(specialist.getNickname());
					orderDTO.setSpecialistPhone(specialist.getMobilePhone());
					orderDTO.setSpecialistTitle(specialist.getTitle());
					String avatar = specialist.getAvatar();
					if (!StringUtils.isEmpty(avatar))
					{
						orderDTO.setSpecialistAvatar(avatar);
					} else
					{
						orderDTO.setSpecialistAvatar("");
					}
					orderDTO.setSpecialistImAccount(specialist.getImAccount());
					orderDTO.setSpecialistType(specialist.getUserType());
				}
				orderDTOList.add(orderDTO);
			}
			result.setCount(count);
			result.setList(orderDTOList);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error("System error:" + e);
		}
		return result;
	}		
	
	/**
	 * 查询咨询师订单列表
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/getCallOrderForConsList.json")
	@ResponseBody
	public Object getCallOrderForConsList(HttpSession session, HttpServletRequest request, Long mid, ServiceOrder order)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			// 参数检查
			if (null == mid)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setError("会员id不能为空");
				return result;
			}
			if (order.getPageIndex() == null)
			{
				result.setCode(-1);
				result.setMsg("分页页码不能为空");
				return result;
			}
			if (order.getPageSize() == null)
			{
				result.setCode(-1);
				result.setMsg("每页条数不能为空");
				return result;
			}

			// 查询用户信息
			Member queryMember = new Member();
			queryMember.setMid(mid);
			Member member = memberService.getMember(queryMember);
			if (member == null)
			{
				result.setCode(ErrorCode.ERROR_USERINFO_NOT_FOUND.getCode());
				result.setError("用户信息未找到");
				return result;
			}

			// 查询咨询师订单列表
			Byte userType = member.getUserType();
			ServiceOrder queryOrder = new ServiceOrder();
			if (userType.intValue() == MemberType.TYPE_CONSULTANT.getCode().intValue())// 咨询师
			{
				queryOrder.setServiceProviderId(mid);
			} else
			{
				result.setCode(ErrorCode.ERROR_USERINFO_NOT_FOUND.getCode());
				result.setError("用户类型不匹配");
				return result;
			}
			if (order != null && order.getStatus() != null)
			{
				queryOrder.setStatus(order.getStatus());
			}
			if (order != null && order.getPageIndex() != null)
			{
				queryOrder.setPageIndex(order.getPageIndex());
			}
			if (order != null && order.getPageSize() != null)
			{
				queryOrder.setPageSize(order.getPageSize());
			}
			queryOrder.setIsEnable(new Byte("0"));
			List<ServiceOrder> orders = serviceOrderService.selectSelective3Page0TmDescByzj(queryOrder);
			int count = serviceOrderService.selectCount(queryOrder);
			List<ApiServiceOrderDTO> wods = new ArrayList<>();
			for (ServiceOrder serviceOrder : orders)
			{
				ApiServiceOrderDTO orderDTO = new ApiServiceOrderDTO();
				
				// 查询订单对应商品信息
				Long serviceGoodsId = serviceOrder.getSgid();
				ServiceGoods serviceGoods = serviceGoodsService.selectByPrimaryKey(serviceGoodsId);
				
				// 刷新订单状态
				serviceOrderService.refreshOrderStatus(serviceOrder.getSoid());
				
				// 查询订单组装数据
				BeanUtils.copyProperties(serviceOrder, orderDTO);
				if (serviceGoods != null)
				{
					orderDTO.setGoodsType(serviceGoods.getType());// 商品类型
					orderDTO.setGoodsPrice(serviceGoods.getPrice());// 商品价格
					orderDTO.setRemainingDuration(serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration());// 剩余服务时长
				}
				

				// 获取消费者信息
				Member consumers = memberService.selectMemberByMid(orderDTO.getMid());
				if (null != consumers)
				{
					orderDTO.setConsumersName(consumers.getNickname());
					orderDTO.setConsumersPhone(consumers.getMobilePhone());
					String avatar = consumers.getAvatar();
					if (!StringUtils.isEmpty(avatar))
					{
						orderDTO.setConsumersAvatar(avatar);
					} else
					{
						orderDTO.setConsumersAvatar("");
					}
					orderDTO.setConsumersImAccount(consumers.getImAccount());
					orderDTO.setConsumersType(consumers.getUserType());
				}

				// 获取专家信息
				Member specialist = memberService.selectMemberByMid(orderDTO.getServiceProviderId());
				if (null != specialist)
				{
					orderDTO.setSpecialistName(specialist.getNickname());
					orderDTO.setSpecialistPhone(specialist.getMobilePhone());
					orderDTO.setSpecialistTitle(specialist.getTitle());
					String avatar = specialist.getAvatar();
					if (!StringUtils.isEmpty(avatar))
					{
						orderDTO.setSpecialistAvatar(avatar);
					} else
					{
						orderDTO.setSpecialistAvatar("");
					}
					orderDTO.setSpecialistImAccount(specialist.getImAccount());
					orderDTO.setSpecialistType(specialist.getUserType());
				}
				wods.add(orderDTO);
			}
			result.setCount(count);
			result.setList(wods);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error("System error:" + e);
		}
		return result;
	}
	
	/**
	 * 获取订单支付信息
	 * @param mid
	 * @param price
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getPaymentInfo.json")
	@ResponseBody
	public Object getPaymentInfo(Long mid, Long calledId,  BigDecimal price)
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
		Map<String, Long> mapStatusAndEeId = eapService.obtainServiceStatusAndEeId(mid, calledId);
		Long serviceStatus = mapStatusAndEeId.get("status");
		if (serviceStatus.longValue() == 1) // eap订单, 不需要扣款
		{
			result.put("needTopUp", 2);
			result.put("cashBalance", 0);
			result.put("discount", 0);
			return result;
		}
		
		// 查询是否尚有未完成订单，如果有，返回该订单
		ServiceOrder queryServiceOrder = new ServiceOrder();
		queryServiceOrder.setMid(caller.getMid());// 主叫人id
		queryServiceOrder.setServiceProviderId(called.getMid());// 被叫人id
		ServiceOrder existServiceOrder = serviceOrderService.selectUnCompleteOrderByServiceProviderId(queryServiceOrder);
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
	 * 查询订单详情
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/getServiceOrderDetail.json")
	@ResponseBody
	public Object getServiceOrderDetail(HttpSession session, HttpServletRequest request, String orderNo)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			// 参数检查
			if (null == orderNo)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setError("订单编号不能为空");
				return result;
			}

			// 根据订单编号查询订单
			ServiceOrder queryOrder = new ServiceOrder();
			queryOrder.setNo(orderNo);
			List<ServiceOrder> orders = serviceOrderService.selectSelective(queryOrder);
			ApiServiceOrderDTO serviceOrderDTO = new ApiServiceOrderDTO();
			if ( orders != null && orders.size() > 0)
			{
				ServiceOrder serviceOrder = orders.get(0);
				if (serviceOrder != null)
				{
					BeanUtils.copyProperties(serviceOrder, serviceOrderDTO);
					
					// 查询订单对应商品信息
					Long serviceGoodsId = serviceOrder.getSgid();
					ServiceGoods serviceGoods = serviceGoodsService.selectByPrimaryKey(serviceGoodsId);
					if (serviceGoods != null)
					{
						serviceOrderDTO.setGoodsType(serviceGoods.getType());// 商品类型
						serviceOrderDTO.setGoodsPrice(serviceGoods.getPrice());// 商品价格
						serviceOrderDTO.setRemainingDuration(serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration());// 剩余服务时长
					}
					// 平台收取金额
					serviceOrderDTO.setPlatformCommissionAmount( (serviceOrder.getCost().multiply(new BigDecimal(serviceOrder.getCommissionRate()))).divide(new BigDecimal(100.00), 2, RoundingMode.UP));
					
					// 获取消费者信息
					Member consumers = memberService.selectMemberByMid(serviceOrderDTO.getMid());
					if (null != consumers)
					{
						serviceOrderDTO.setConsumersName(consumers.getNickname());
						serviceOrderDTO.setConsumersPhone(consumers.getMobilePhone());
						String avatar = consumers.getAvatar();
						if (!StringUtils.isEmpty(avatar))
						{
							serviceOrderDTO.setConsumersAvatar(avatar);
						} else
						{
							serviceOrderDTO.setConsumersAvatar("");
						}
					}

					// 获取专家信息
					Member specialist = memberService.selectMemberByMid(serviceOrderDTO.getServiceProviderId());
					if (null != specialist)
					{
						serviceOrderDTO.setSpecialistName(specialist.getNickname());
						serviceOrderDTO.setSpecialistPhone(specialist.getMobilePhone());
						serviceOrderDTO.setSpecialistTitle(specialist.getTitle());
						String avatar = specialist.getAvatar();
						if (!StringUtils.isEmpty(avatar))
						{
							serviceOrderDTO.setSpecialistAvatar(avatar);
						} else
						{
							serviceOrderDTO.setSpecialistAvatar("");
						}
					}
					
					// 获取评价信息

					ServiceOrderEvaluation serviceOrderEvaluation = serviceOrderEvaluationService.obtainEvaluationBySoid(serviceOrder.getSoid());

					if (serviceOrderEvaluation != null)
					{
						serviceOrderDTO.setSoeId(serviceOrderEvaluation.getSoeId());
						serviceOrderDTO.setScore(serviceOrderEvaluation.getScore());
					}

					
					// 咨询师实际所得
					CapitalIncomeExpenses capitalIncomeExpenses = capitalIncomeExpenseService.getIncomeExpensesByOrderNo(serviceOrder.getNo());
					if (capitalIncomeExpenses == null) // 无扣款记录，咨询师所得为零
					{
						serviceOrderDTO.setSpecialistRealityAmount(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP));
					} else // 有扣款记录
					{
						if (serviceOrder.getOrderType().intValue() == 1) // eap订单，不扣款
						{
							serviceOrderDTO.setSpecialistRealityAmount(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP));
						} else // 普通已扣款订单计算公式： 咨询师实际收入所得 = 商品总价 - 平台收取金额
						{
							serviceOrderDTO.setSpecialistRealityAmount(serviceOrder.getCost().subtract(serviceOrderDTO.getPlatformCommissionAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
						}
					}
					
				}
				
			}
			
			result.put("obj", serviceOrderDTO);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error("System error:" + e);
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
	 * 获取评价标签列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getAllEvalLabel.json")
	@ResponseBody
	public Object getAllEvalLabel(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		
		List<EvaluationLabel> labels = serviceOrderEvaluationService.selectAllLabel();
		result.put("list", labels);
		
		return result;
	}
	
	/**
	 * 新增评价
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addOrderEvaluation.json")
	@ResponseBody
	public Object addOrderEvaluation(HttpSession session, HttpServletRequest request, ServiceOrderEvaluation record, @RequestParam(value = "labelIds", required = false) String labelIds)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		
		try
		{
			// 参数检查
			if (null == record)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg("参数错误");
				return result;
			}
			if (null == record.getSoid())
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg("订单id不能为空");
				return result;
			}
			if (null == record.getScore())
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg("评分不能为空");
				return result;
			}
			
			ServiceOrder serviceOrder = serviceOrderService.selectOrderByPrimaryKey(record.getSoid());
			if (serviceOrder == null)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg("订单不存在");
				return result;
			}
			if (!serviceOrder.getStatus().equals(OrderState.STATE_CALL_NOT_EVALUATION.getCode()))
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg("订单不可评价");
				return result;
			}
			serviceOrder.setStatus(OrderState.STATE_CALL_EVALUATION_FINISH.getCode().byteValue());
			serviceOrderService.updateByPrimaryKeySelective(serviceOrder);
			
			record.setMid(serviceOrder.getMid());
			record.setPid(serviceOrder.getServiceProviderId());
			record.setCreateTime(new Date());
			serviceOrderEvaluationService.newEvaluation(record);
			if (labelIds != null && labelIds.length() > 0)
			{
				String[] labelArray = labelIds.split(",");
				for (String lableId : labelArray)
				{
					if (!lableId.equals(""))
					{	
						serviceOrderEvaluationService.newLabelMapping(record.getSoid(), record.getSoeId(), Long.parseLong(lableId));
						EvaluationLabel eLabel = serviceOrderEvaluationService.obtainLabelById(Long.parseLong(lableId));
						if(eLabel!=null && eLabel.getScore()==0)
						{//增加无效订单数
							serviceStatisticsService.updatePsychoInvalidTimes(record.getMid());
						}
					
					}
				}
			}
			
			if (serviceOrder != null)
			{
				if (serviceOrder.getOrderType().longValue() == 1) // eap订单
				{
					serviceStatisticsService.updatePsychoScore(serviceOrder.getServiceProviderId(), record.getScore(), 1);
				} else // 普通订单
				{
					serviceStatisticsService.updatePsychoScore(serviceOrder.getServiceProviderId(), record.getScore(), 0);
				}
			}
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error("System error:" + e);
			e.printStackTrace();
			return result;
		}
		
		return result;
	}
	
	/**
	 * 在用户列表中使订单不可见
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value = "/V1/makeOrderUserInvisible.json")
	@ResponseBody
	public Object makeOrderUserInvisibleV1(String orderNo)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(orderNo))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			ServiceOrder order = serviceOrderService.selectByNo(orderNo);
			order.setInvisibleUser((byte) 1);
			serviceOrderService.updateByPrimaryKeySelective(order);
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
	 * 删除订单
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/delServiceOrder.json")
	@ResponseBody
	public Object delServiceOrder(HttpSession session, HttpServletRequest request, String orderNo)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			// 参数检查
			if (null == orderNo)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setError("订单编号不能为空");
				return result;
			}

			// 删除订单
			ServiceOrder queryOrder = new ServiceOrder();
			queryOrder.setNo(orderNo);
			List<ServiceOrder> orders = serviceOrderService.selectSelective(queryOrder);
			for (ServiceOrder order : orders)
			{
				int status = order.getStatus().intValue();
				if (status == OrderState.STATE_CALL_NOT_EVALUATION.getCode() || status == OrderState.STATE_CALL_EVALUATION_FINISH.getCode())
				{
					order.setIsEnable(new Byte("1"));
					serviceOrderService.updateByPrimaryKeySelective(order);
				} else 
				{
					result.setCode(-1);
					result.setMsg("订单未完成，无法删除");
					return result;
				}
			}
			
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error("System error:" + e);
		}
		return result;
	}
	
	/**
	 * 获取订单播报条码，取15条
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainServiceOrderBroadcast.json")
	@ResponseBody
	public Object obtainServiceOrderBroadcastV1()
	{
		ResultEntity result = new ResultEntity();
		List<String> broadcasts = new ArrayList<String>();
		try{
			List<ServiceOrder> sos = serviceOrderService.search(null, null, 1, 15, (byte) 1, null, null);
			for(ServiceOrder so : sos)
			{
				Member user = memberService.selectMemberByMid(so.getMid());
				Member psycho = memberService.selectMemberByMid(so.getServiceProviderId());
				if(user==null || psycho==null)
				{
					continue;
				}
				String userName = user.getNickname();
				String bc = String.format("咨询师%s收到%s****的语音咨询", psycho.getNickname(), 
						userName.substring(0, userName.length()/2==0?1:userName.length()/2));
				broadcasts.add(bc);
			}
			
			//倒序
			Collections.reverse(broadcasts);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("broadcasts", broadcasts);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
