package com.depression.controller.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.MembersOnlineStatus;
import com.depression.entity.OrderState;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.CapitalWithdrawRequest;
import com.depression.model.CapitalWithdrawRequestDuration;
import com.depression.model.Member;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.api.dto.ApiCapitalCouponEntityDTO;
import com.depression.model.api.dto.ApiCapitalIncomeExpensesDTO;
import com.depression.model.api.dto.ApiCapitalPersonalAssetsDTO;
import com.depression.model.api.dto.ApiCapitalWithdrawRequestDurationDTO;
import com.depression.service.CapitalCouponService;
import com.depression.service.CapitalIncomeExpenseService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.CapitalWithdrawRequestDurationService;
import com.depression.service.CapitalWithdrawRequestService;
import com.depression.service.MemberService;
import com.depression.service.PingxxService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderService;
import com.depression.utils.AccountUtil;
import com.depression.utils.Configuration;
import com.depression.utils.PropertyUtils;
import com.pingplusplus.model.Charge;

@Controller
@RequestMapping("/CapitalPersonalAssets")
public class CapitalPersonalAssetsController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	CapitalPersonalAssetsService capitalPersonalAssetsService;
	@Autowired
	PingxxService pingxxService;
	@Autowired
	CapitalIncomeExpenseService capitalIncomeExpenseService;
	@Autowired
	CapitalWithdrawRequestService capitalWithdrawRequestService;
	@Autowired
	CapitalWithdrawRequestDurationService capitalWithdrawRequestDurationService;
	@Autowired
	CapitalCouponService capitalCouponService;
	@Autowired
	private ServiceOrderService serviceOrderService;
	@Autowired
	private MemberService memberService;
	@Autowired
	ServiceGoodsService serviceGoodsService;
	
	/**
	 * 根据会员id获取个人资产
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPersonnalAssets.json")
	@ResponseBody
	public Object obtainPersonnalAssets(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiCapitalPersonalAssetsDTO personalAssetsDTO = new ApiCapitalPersonalAssetsDTO();
		ApiCapitalCouponEntityDTO capitalCouponEntityDTO = new ApiCapitalCouponEntityDTO();
		try{
			CapitalPersonalAssets personalAssets = capitalPersonalAssetsService.selectByMid(mid);
			if(personalAssets == null)
			{//如果个人资产账户不存在，创建一个
				capitalPersonalAssetsService.insert(mid);
				personalAssets = capitalPersonalAssetsService.selectByMid(mid);
			}
			BeanUtils.copyProperties(personalAssets, personalAssetsDTO);
			
			List<CapitalWithdrawRequest> cwrs = capitalWithdrawRequestService.selectMemberInaudited(mid);
		    BigDecimal inWithdraw = new BigDecimal(0);
			for(CapitalWithdrawRequest cws : cwrs)
			{
				inWithdraw = inWithdraw.add(cws.getAmount());
			}
			personalAssetsDTO.setWithdrawable(personalAssets.getCashBalance().subtract(inWithdraw));
			
			CapitalCouponEntity couponEntity = capitalCouponService.getValuabestUsableCashCouponEntity(mid);

			if(couponEntity != null)
				BeanUtils.copyProperties(couponEntity, capitalCouponEntityDTO);
			
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("personalAssets", personalAssetsDTO);
		result.put("valuabestCoupon", capitalCouponEntityDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 用户充值
	 * @param request
	 * @param amount	元为单位
	 * @param channel
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/topUpCash.json")
	@ResponseBody
	public Object topUpCash(HttpServletRequest request, BigDecimal amount, String channel, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(amount, channel, mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if(amount.compareTo(new BigDecimal(0)) == -1 || !pingxxService.checkChargeChannel(channel))
		{//amount 小于 0 或者支付通道不支持
			result.setCode(ErrorCode.ERROR_PARAM_INVALID.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INVALID.getMessage());
			return result;			
		}
		
		//构建充值信息
		CapitalIncomeExpenses incomeExpenses = new CapitalIncomeExpenses(); 
		incomeExpenses.setNo(AccountUtil.generateNo());
		incomeExpenses.setMid(mid);
		incomeExpenses.setAmount(amount);
		incomeExpenses.setItems(CapitalIncomeExpenseService.ITEMS_TOPUP);
		incomeExpenses.setDirection(CapitalIncomeExpenseService.DIRECTION_INCOME);
		incomeExpenses.setChannel(CapitalIncomeExpenseService.getChannelCode(channel));
		incomeExpenses.setStatus(CapitalIncomeExpenseService.STATUS_PAYING);
		incomeExpenses.setRemark("充值");
		incomeExpenses.setCreateTime(new Date());
		
		//获取pingxx charge,
		Charge charge = pingxxService.startCharge(incomeExpenses.getNo(), (int) (amount.doubleValue()*100), channel, 
				request.getRemoteAddr(), "杭州心猫网络科技有限公司", incomeExpenses.getRemark());
		
		if(charge == null)
		{
			result.setCode(ErrorCode.ERROR_PINGXX_CHARGE.getCode());
			result.setMsg(ErrorCode.ERROR_PINGXX_CHARGE.getMessage());
			return result;
		}
		
		//创建充值记录
		incomeExpenses.setPingxxPayId(charge.getId());
		capitalIncomeExpenseService.insert(incomeExpenses);
		
		result.put("charge", charge);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainWithdrawDuration.json")
	@ResponseBody
	public Object obtainWithdrawDuration(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiCapitalWithdrawRequestDurationDTO cwrdDTO = new ApiCapitalWithdrawRequestDurationDTO();
		Integer unAuditRequest = 0;
		try{
			CapitalWithdrawRequestDuration cwrd = capitalWithdrawRequestDurationService.getTheOnlyOne();
			BeanUtils.copyProperties(cwrd, cwrdDTO);
			
			List<CapitalWithdrawRequest> cwrs = capitalWithdrawRequestService.selectMemberInaudited(mid);
			unAuditRequest = cwrs.size();
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("unAuditRequest", unAuditRequest);
		result.put("withdrawDuration", cwrdDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 申请提现
	 * @param amount
	 * @param channel
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/withdrawCash.json")
	@ResponseBody
	public Object withdrawCash(BigDecimal amount,String channel, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(amount, channel, mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		CapitalWithdrawRequestDuration cwrd = capitalWithdrawRequestDurationService.getTheOnlyOne();
		Integer now = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); 
		if(now<cwrd.getBeginDate() || now > cwrd.getEndDate())
		{
			result.setCode(ErrorCode.ERROR_CAPITAL_NOT_WITHDRAW_DURATION.getCode());
			result.setMsg(ErrorCode.ERROR_CAPITAL_NOT_WITHDRAW_DURATION.getMessage());
			return result;
		}
		
		CapitalPersonalAssets personalAssets = capitalPersonalAssetsService.selectByMid(mid);
		//查询在体现中的金额
		List<CapitalWithdrawRequest> cwrs = capitalWithdrawRequestService.selectMemberInaudited(mid);
	    BigDecimal inWithdraw = new BigDecimal(0);
		for(CapitalWithdrawRequest cws : cwrs)
		{
			inWithdraw = inWithdraw.add(cws.getAmount());
		}
		
		if(amount.compareTo(BigDecimal.valueOf(0)) < 1 ||
				amount.compareTo(personalAssets.getCashBalance().subtract(inWithdraw)) == 1 || 
				!pingxxService.checkTransferChannel(channel))
		{//amount 大于可提现余额 或者支付通道不支持
			result.setCode(ErrorCode.ERROR_PARAM_INVALID.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INVALID.getMessage());
			return result;			
		}
		
		CapitalWithdrawRequest withdrawRequest = new CapitalWithdrawRequest();
		withdrawRequest.setMid(mid);
		withdrawRequest.setNo(AccountUtil.generateNo());
		withdrawRequest.setRequestTime(new Date());
		withdrawRequest.setChannel(CapitalWithdrawRequestService.getChannelCode(channel));
		withdrawRequest.setAccount(memberService.selectMemberByMid(mid).getOpenid()); //账号从绑定信息获取
		withdrawRequest.setAmount(amount);
		withdrawRequest.setStatus(CapitalWithdrawRequestService.STATUS_INAUDITED);
		
		capitalWithdrawRequestService.insert(withdrawRequest);
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainTopUpRecords.json")
	@ResponseBody
	public Object obtainTopUpRecords(Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiCapitalIncomeExpensesDTO> capitalIncomeExpensesDTOs = new ArrayList<ApiCapitalIncomeExpensesDTO>();
		Integer count;
		try{
			List<CapitalIncomeExpenses> cies = capitalIncomeExpenseService.getIncomeExpensesWithPage(mid, CapitalIncomeExpenseService.ITEMS_TOPUP, 
					pageIndex, pageSize);
			for(CapitalIncomeExpenses cie : cies)
			{
				ApiCapitalIncomeExpensesDTO acieDTO = new ApiCapitalIncomeExpensesDTO();
				BeanUtils.copyProperties(cie, acieDTO);
				acieDTO.setChannel(CapitalIncomeExpenseService.getChannelStr(cie.getChannel()));
				capitalIncomeExpensesDTOs.add(acieDTO);
			}
			count = capitalIncomeExpenseService.countIncomeExpenses(mid, CapitalIncomeExpenseService.ITEMS_TOPUP);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("incomeExpenses", capitalIncomeExpensesDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainDetails.json")
	@ResponseBody
	public Object obtainDetails(Long mid, Integer pageIndex, Integer pageSize, Byte item)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiCapitalIncomeExpensesDTO> capitalIncomeExpensesDTOs = new ArrayList<ApiCapitalIncomeExpensesDTO>();
		Integer count;
		try{
			List<CapitalIncomeExpenses> cies = capitalIncomeExpenseService.getIncomeExpensesWithPage(mid, item, 
					pageIndex, pageSize);
			for(CapitalIncomeExpenses cie : cies)
			{
				ApiCapitalIncomeExpensesDTO acieDTO = new ApiCapitalIncomeExpensesDTO();
				BeanUtils.copyProperties(cie, acieDTO);
				Byte channel = cie.getChannel();
				if (channel != null)
				{
					acieDTO.setChannel(CapitalIncomeExpenseService.getChannelStr(channel));
				}
				
				// 根据订单编号查询订单
				ServiceOrder queryOrder = new ServiceOrder();
				queryOrder.setNo(cie.getOrderNo());
				List<ServiceOrder> orders = serviceOrderService.selectSelective(queryOrder);
				if ( orders != null && orders.size() > 0)
				{
					ServiceOrder serviceOrder = orders.get(0);
					
					// 获取专家信息
					Member specialist = memberService.selectMemberByMid(serviceOrder.getServiceProviderId());
					if (null != specialist)
					{
						acieDTO.setSpecialistName(specialist.getNickname());
						acieDTO.setSpecialistPhone(specialist.getMobilePhone());
						acieDTO.setSpecialistTitle(specialist.getTitle());
						String avatar = specialist.getAvatar();
						if (!StringUtils.isEmpty(avatar))
						{
							acieDTO.setSpecialistAvatar(avatar);
						} else
						{
							acieDTO.setSpecialistAvatar("");
						}
						acieDTO.setOrderStatus(serviceOrder.getStatus());
					}
					
				}
				
				capitalIncomeExpensesDTOs.add(acieDTO);
			}
			count = capitalIncomeExpenseService.countIncomeExpenses(mid, item);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("incomeExpenses", capitalIncomeExpensesDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
}
