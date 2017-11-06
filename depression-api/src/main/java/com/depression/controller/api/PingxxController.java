package com.depression.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPersonalAssets;
import com.depression.service.CapitalIncomeExpenseService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.CapitalPlatformCashService;
import com.depression.service.PingxxService;
import com.depression.utils.PropertyUtils;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Transfer;

@Controller
@RequestMapping("/Pingxx")
public class PingxxController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	PingxxService pingxxService;
	@Autowired
	CapitalIncomeExpenseService capitalIncomeExpenseService;
	@Autowired
	CapitalPersonalAssetsService capitalPersonalAssetsService;
	@Autowired
	CapitalPlatformCashService capitalPlatformCashService;
	
	@ResponseBody
	@RequestMapping(value = "/obtainCharge.json")
	public Object obtainCharge(HttpServletRequest request, String orderNo, String channel)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(orderNo, channel))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		//TODO 检查order,获取支付要素
		
		Integer amount = 100;
		String clientIp = request.getRemoteAddr();
		String subject = "subject";
		String body = "body";
		
		if(!pingxxService.checkChargeChannel(channel))
		{
			result.setCode(ErrorCode.ERROR_PINGXX_CHANNEL.getCode());
			result.setMsg(ErrorCode.ERROR_PINGXX_CHANNEL.getMessage());
			return result;
		}
		
		Charge charge = pingxxService.startCharge(orderNo, amount, channel, clientIp, subject, body);
		if(charge == null)
		{
			result.setCode(ErrorCode.ERROR_PINGXX_CHARGE.getCode());
			result.setMsg(ErrorCode.ERROR_PINGXX_CHARGE.getMessage());
			return result;
		}
		
		return charge;
	}
	
	@ResponseBody
	@RequestMapping(value = "/performTransfer.json")
	public Object performTransfer(HttpServletRequest request, String orderNo, String channel)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(orderNo, channel))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(!pingxxService.checkTransferChannel(channel))
		{
			result.setCode(ErrorCode.ERROR_PINGXX_CHANNEL.getCode());
			result.setMsg(ErrorCode.ERROR_PINGXX_CHANNEL.getMessage());
			return result;
		}
		
		//TODO 检查order,获取支付要素
		Integer amount = 100;
		String openId = "USER_OPENID";
		String description = "description";
		String userName = "User Name";
		String cardNum = "6225220317083517";
		String openBankCode = "0102";
		
		Transfer transfer = pingxxService.startTransfer(orderNo, amount, channel, openId, description, userName, cardNum, openBankCode);
		if(transfer == null)
		{
			result.setCode(ErrorCode.ERROR_PINGXX_TRANSFER.getCode());
			result.setMsg(ErrorCode.ERROR_PINGXX_TRANSFER.getMessage());
			return result;
		}
		//TODO 处理order状态
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(value = "/onWebhooks")
	public void onWebhooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        request.setCharacterEncoding("UTF8");

        // 获得 http body 内容
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String string;
        while ((string = reader.readLine()) != null) {
            buffer.append(string);
        }
        reader.close();
        
        //获取签名字段
        String signatureString = request.getHeader("x-pingplusplus-signature");
        if(signatureString == null){
        	response.setStatus(500);
        	return;
        }
        
        // 解析异步通知数据
        Event event = pingxxService.eventVerifyAndParse(buffer.toString(), signatureString);
        if(event == null){
        	response.setStatus(500);
        } else if ("charge.succeeded".equals(event.getType())) {
        	response.setStatus(200);
        	//处理充值结果
        	Charge charge = (Charge)(event.getData().getObject());
        	CapitalIncomeExpenses incomeExpenses = 
        			capitalIncomeExpenseService.getIncomeExpenses(charge.getOrderNo());
        	if(incomeExpenses != null)
        	{
        		if(incomeExpenses.getStatus() != CapitalIncomeExpenseService.STATUS_PAYING)
        		{//避免重复操作
        			return;
        		}
        		//修改收支订单状态
        		if(!charge.getPaid())
        		{//支付失败
        			CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
        			cie.setIeid(incomeExpenses.getIeid());
        			cie.setStatus(CapitalIncomeExpenseService.STATUS_FAILED);
        			capitalIncomeExpenseService.update(cie);
        			return;
        		}else{
	        		//修改用户余额
	        		CapitalPersonalAssets cpa 
	        			= capitalPersonalAssetsService.selectByMid(incomeExpenses.getMid());
	        		if(cpa != null)
	        		{
	        			//更新用户余额
	        			capitalPersonalAssetsService.transCashBalance(cpa.getPaid(), incomeExpenses.getAmount());
	        			//更新用户充值总额
	        			capitalPersonalAssetsService.transPayAmount(cpa.getPaid(), incomeExpenses.getAmount());
	        			//更新平台用户充值总额
	        			capitalPlatformCashService.transTopUpAmount(incomeExpenses.getAmount());
	        		}
	        		
        			CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
        			cie.setIeid(incomeExpenses.getIeid());
        			cie.setStatus(CapitalIncomeExpenseService.STATUS_PAID);
        			cie.setFinishTime(new Date());
        			cie.setCashBalance(capitalPersonalAssetsService.selectByMid(incomeExpenses.getMid()).getCashBalance());
        			capitalIncomeExpenseService.update(cie);
        		}
        	}
            
        } else if ("transfer.succeeded".equals(event.getType())) {
            response.setStatus(200);
        	//处理充值结果
        	Transfer transfer = (Transfer)(event.getData().getObject());
        	CapitalIncomeExpenses incomeExpenses = 
        			capitalIncomeExpenseService.getIncomeExpenses(transfer.getOrderNo());
        	if(incomeExpenses != null)
        	{
        		if(incomeExpenses.getStatus() != CapitalIncomeExpenseService.STATUS_PAYING)
        		{//避免重复操作
        			return;
        		}
        		 		
        		//修改收支订单状态
        		if(transfer.getStatus().equals("failed"))
        		{//支付失败
	        		CapitalPersonalAssets cpa 
        				= capitalPersonalAssetsService.selectByMid(incomeExpenses.getMid());
    				// 加回余额
    				capitalPersonalAssetsService.transCashBalance(cpa.getPaid(), incomeExpenses.getAmount());
    				// 回退现总金额
    				capitalPersonalAssetsService.transWithdrawAmount(cpa.getPaid(), incomeExpenses.getAmount().negate());
    				// 更新平台用户提现总额
    				capitalPlatformCashService.transWithdrawAmount(incomeExpenses.getAmount().negate());
        			
        			CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
        			cie.setIeid(incomeExpenses.getIeid());
        			cie.setStatus(CapitalIncomeExpenseService.STATUS_FAILED);
           			cie.setFinishTime(new Date());
           			cie.setCashBalance(capitalPersonalAssetsService.selectByMid(incomeExpenses.getMid()).getCashBalance());
        			capitalIncomeExpenseService.update(cie);
        			//是否退款待测试
        			return;
        		}else if(transfer.getStatus().equals("paid")){	        		
        			CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
        			cie.setIeid(incomeExpenses.getIeid());
        			cie.setStatus(CapitalIncomeExpenseService.STATUS_PAID);
           			cie.setFinishTime(new Date());
        			cie.setCashBalance(capitalPersonalAssetsService.selectByMid(incomeExpenses.getMid()).getCashBalance());
        			capitalIncomeExpenseService.update(cie);
        		}
        	}
        } else if ("refund.succeeded".equals(event.getType())) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
        }
	}
	
	
}
