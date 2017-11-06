package com.depression.controller.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.depression.base.ucpaas.DateUtil;
import com.depression.entity.ErrorCode;
import com.depression.entity.OrderState;
import com.depression.entity.ResultEntity;
import com.depression.model.EapEmployee;
import com.depression.model.EapEnterprise;
import com.depression.model.Member;
import com.depression.model.ServiceCallRecord;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.web.dto.WebServiceOrderDTO;
import com.depression.service.CapitalCouponService;
import com.depression.service.CapitalIncomeExpenseService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.CapitalPlatformCashService;
import com.depression.service.MemberService;
import com.depression.service.ServiceCallRecordService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderService;
import com.depression.utils.BigDecimalUtil;
import com.depression.utils.PropertyUtils;
import com.depression.utils.SmsUtil;

/**
 * 服务订单
 * 
 * @author hongqian_li
 * 
 */
@Controller
@RequestMapping("/ServiceOrder")
public class ServiceOrderController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	ServiceOrderService mServiceOrderService;
	@Autowired
	MemberService mMemberService;
	@Autowired
	CapitalPersonalAssetsService mCapitalPersonalAssetsService;
	@Autowired
	CapitalPlatformCashService mCapitalPlatformCashService;
	@Autowired
	CapitalCouponService mCapitalCouponService;
	@Autowired
	ServiceGoodsService mServiceGoodsService;
	@Autowired
	CapitalIncomeExpenseService mCapitalIncomeExpenseService;
	@Autowired
	ServiceCallRecordService serviceCallRecordService;
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	/**
	 * 分页条件查询
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getServiceOrderByPage.json")
	@ResponseBody
	public Object getServiceOrderByPage(Date createTime, Date endTime, String no, Integer pageIndex, Integer pageSize,
			Byte status, String words, Byte createTimeDirection)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex, pageSize)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		if(no != null) words = no;
		try
		{
			List<ServiceOrder> orders = mServiceOrderService.search(words, status, pageIndex, pageSize, createTimeDirection, createTime, endTime);
			int count = mServiceOrderService.countSearch(words, status, createTime, endTime);
			List<WebServiceOrderDTO> wods = new ArrayList<>();
			for (ServiceOrder serviceOrder : orders)
			{
				// 查询订单组装数据
				WebServiceOrderDTO wod = new WebServiceOrderDTO();
				BeanUtils.copyProperties(serviceOrder, wod);
				// 查询订单类型
				ServiceGoods sg = mServiceGoodsService.selectByPrimaryKey(wod.getSgid());
				if (null != sg)
				{
					wod.setConsultType(sg.getType());
				}
				// 获取消费者信息
				Member consumers = mMemberService.selectMemberByMid(wod.getMid());
				if (null != consumers)
				{
					wod.setConsumersAccount(consumers.getUserName());
					wod.setConsumersPhone(consumers.getMobilePhone());
				}
				// 获取专家信息
				Member specialist = mMemberService.selectMemberByMid(wod.getServiceProviderId());
				if (null != specialist)
				{
					wod.setSpecialistName(specialist.getNickname());
					wod.setSpecialistPhone(specialist.getMobilePhone());
				}
				
				// 以下三种情况"平台收入"和"咨询师实得"字段均金额设置为0： 
				// 		1.订单未接通，2.订单未扣款,3. EAP订单
				if (serviceOrder.getStatus().equals(OrderState.STATE_CALL_NOT_CONNECTED.getCode())
						|| serviceOrder.getStatus().equals(OrderState.STATE_CALL_NOT_CHARGEBECKS.getCode())
							|| serviceOrder.getOrderType().longValue() == 1)
				{
					wod.setServiceRealityAmount(BigDecimal.valueOf(0));
					wod.setPlatformIncome(BigDecimal.valueOf(0));
				} else 
				{
					//未扣款但超时的订单
					if(serviceOrder.getDiscountAmount().intValue()==0&&
							serviceOrder.getCashAmount().intValue()==0)
					{
						wod.setServiceRealityAmount(BigDecimal.valueOf(0));
						wod.setPlatformIncome(BigDecimal.valueOf(0));
					}else
					{
						// 计算专家实际获得的利润 总服务金额-（总服务费*平台佣金比例）
						BigDecimal cost = wod.getCost();
						// 佣金 commission
						double commission = BigDecimalUtil.mul(cost.doubleValue(), Double.valueOf(100- wod.getCommissionRate()) / 100);
						wod.setServiceRealityAmount(BigDecimal.valueOf(commission));
						
						//计算平台收支
						double platfromIncome = wod.getCashAmount().doubleValue() - commission;
						wod.setPlatformIncome(BigDecimal.valueOf(platfromIncome));
					}
				}
				
				wods.add(wod);
			}
			result.setCount(count);
			result.setList(wods);
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error(e);
		}
		return result;
	}

	/**
	 * 订单退款 纯现金退款 ：rate和amount互斥，设置rate时以rate为先计算退款金额。 原封退款（含优惠券）：rate和amount都未设置
	 * 
	 * @param no
	 *            订单编号
	 * @param amount
	 *            退款金额
	 * @param rate
	 *            退款比例 1-100
	 * @param reclaimServer
	 *            服务者是否退款
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/refundServiceOrder.json")
	@ResponseBody
	public Object refundServiceOrder(String no, BigDecimal amount, Integer rate, Integer reclaimServer)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(no, reclaimServer))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		ServiceOrder serviceOrder = mServiceOrderService.selectByNo(no);
		if (serviceOrder == null)
		{
			result.setCode(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getMessage());
			return result;
		}

		if(serviceOrder.getStatus() == ServiceOrderService.STATUS_PAYING)
		{//未支付
			result.setCode(ErrorCode.ERROR_ORDER_UNPAID.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_UNPAID.getMessage());
			return result;
		}
		
		if(serviceOrder.getRefundStatus() != ServiceOrderService.REFUND_STATUS_NO)
		{
			result.setCode(ErrorCode.ERROR_ORDER_REFUNDED.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_REFUNDED.getMessage());
			return result;
		}

		if (rate != null)
		{
			if (rate > 100 || rate < 0)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INVALID.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INVALID.getMessage());
				return result;
			}
			amount = serviceOrder.getCost().multiply(BigDecimal.valueOf((double) rate / 100));
		}

		if (amount != null)
		{// 此处为全现金退款方式
			// 修改用户现余额
			mCapitalPersonalAssetsService.transCashBalance(amount, serviceOrder.getMid());
			// 修改用户支出总额
			mCapitalPersonalAssetsService.transExpenseAmount(amount.negate(), serviceOrder.getMid());
			// 添加资金明细
			mCapitalIncomeExpenseService.insertRefund(serviceOrder.getMid(), amount);
			// 修改平台现金余额
			mCapitalPlatformCashService.transCashBalance(amount.negate());
			// 修改平台支出总额（收入和支出有待商榷）
			mCapitalPlatformCashService.transExpensesAmount(amount);
			// 修改订单退款记录
			serviceOrder.setRefundPlatformAmount(amount);
			serviceOrder.setRefundStatus(ServiceOrderService.REFUND_STATUS_CASH);
			
		} else
		{// 此处为原封（含优惠券）退款方式
			// 修改用户现余额
			mCapitalPersonalAssetsService.transCashBalance(serviceOrder.getCashAmount(), serviceOrder.getMid());
			// 修改用户支出总额
			mCapitalPersonalAssetsService.transExpenseAmount(serviceOrder.getCashAmount().negate(), serviceOrder.getMid());
			// 添加资金明细
			mCapitalIncomeExpenseService.insertRefund(serviceOrder.getMid(), serviceOrder.getCashAmount());
			// 修改平台现金余额
			mCapitalPlatformCashService.transCashBalance(serviceOrder.getCashAmount().negate());
			// 修改平台支出总额（收入和支出有待商榷）
			mCapitalPlatformCashService.transExpensesAmount(serviceOrder.getCashAmount());
			// 如有优惠券
			if (serviceOrder.getDbid() != null)
			{
				mCapitalCouponService.restoreCoupon(serviceOrder.getDbid());
			}
			// 修改订单退款记录
			serviceOrder.setRefundPlatformAmount(serviceOrder.getCashAmount());
			serviceOrder.setRefundStatus(ServiceOrderService.REFUND_STATUS_RESTORE);
		}

		if (reclaimServer == 1)
		{
			BigDecimal refundAmount = serviceOrder.getCost().multiply(BigDecimal.valueOf(1.0 - (double) serviceOrder.getCommissionRate() / 100));
			// 修改用户现余额
			mCapitalPersonalAssetsService.transCashBalance(refundAmount.negate(), serviceOrder.getServiceProviderId());
			// 修改用户服务收入总额
			mCapitalPersonalAssetsService.transServiceIncomeAmount(refundAmount.negate(), serviceOrder.getServiceProviderId());
			// 添加资金明细
			mCapitalIncomeExpenseService.insertServiceReturn(serviceOrder.getServiceProviderId(), refundAmount);
			// 修改平台现金余额
			mCapitalPlatformCashService.transCashBalance(refundAmount);
			// 修改平台收入总额（收入和支出有待商榷）
			mCapitalPlatformCashService.transIncomeAmount(refundAmount);
			// 修改订单退款记录
			serviceOrder.setRefundServerAmount(refundAmount);
		}

		mServiceOrderService.updateByPrimaryKeySelective(serviceOrder);
		
		//短信提醒
		Member member = mMemberService.selectMemberByMid(serviceOrder.getMid());
		String refundAmount = amount!=null?amount.toString():serviceOrder.getCashAmount().toString();
		SmsUtil.sendSms(member.getMobilePhone(), "159982", refundAmount);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/complaintServiceOrder.json")
	@ResponseBody
	public Object complaintServiceOrder(String no, String reason)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(no))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		ServiceOrder serviceOrder = mServiceOrderService.selectByNo(no);
		if (serviceOrder == null)
		{
			result.setCode(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getMessage());
			return result;
		}

		serviceOrder.setStatus(ServiceOrderService.STATUS_COMPLAINT_UNHANDLED);
		if(reason != null )
			serviceOrder.setOperationLog(serviceOrder.getOperationLog() + reason + "\n");

		mServiceOrderService.updateByPrimaryKeySelective(serviceOrder);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/handleComplaintServiceOrder.json")
	@ResponseBody
	public Object handleComplaintServiceOrder(String no, String reason)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(no))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		ServiceOrder serviceOrder = mServiceOrderService.selectByNo(no);
		if (serviceOrder == null)
		{
			result.setCode(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getMessage());
			return result;
		}

		serviceOrder.setStatus(ServiceOrderService.STATUS_COMPLAINT_HANDLED);
		if(reason != null )
			serviceOrder.setOperationLog(serviceOrder.getOperationLog() + reason + "\n");

		mServiceOrderService.updateByPrimaryKeySelective(serviceOrder);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/handleExceptionServiceOrder.json")
	@ResponseBody
	public Object handleExceptionServiceOrder(String no, String reason)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(no))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		ServiceOrder serviceOrder = mServiceOrderService.selectByNo(no);
		if (serviceOrder == null)
		{
			result.setCode(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ORDER_NO_INEXISTENT.getMessage());
			return result;
		}

		serviceOrder.setStatus(ServiceOrderService.STATUS_EXCEPTION_HANDLED);
		if(reason != null )
			serviceOrder.setOperationLog(serviceOrder.getOperationLog() + reason + "\n");

		mServiceOrderService.updateByPrimaryKeySelective(serviceOrder);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	/**
	 * 获取订单录音名称
	 * @param soid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/getRecordName.json")
	@ResponseBody
	public Object  getRecordName(Long soid){
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		// 参数检查
		if (soid == null)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg("订单id不能为空");
				return result;
			}
		List<String> recordName = new ArrayList<String>();
		//StringBuffer recordName = new StringBuffer();
		ServiceCallRecord record =  new ServiceCallRecord();
		record.setServiceOrderId(soid);
		List<ServiceCallRecord> serviceCallRecords = serviceCallRecordService.selectSelective(record);
		if(serviceCallRecords.size()>0){
			for(ServiceCallRecord call : serviceCallRecords){
				if(call.getRecordUrl()!=null&&call.getRecordUrl().contains("@")){
					recordName.add(call.getRecordUrl().split("@")[1]);
				}
			}
		}
		if(recordName.size()==0){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg("该订单没有录音文件");
			return result;
		}
		result.put("recordName", recordName);
		return result;
	}

	/**
	 * 手动关闭订单
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/closeServiceOrder.json")
	@ResponseBody
	public Object closeServiceOrder(String no)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
	
		// 参数检查
		if (no == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg("订单id不能为空");
			return result;
		}
	
		// 查询订单
		ServiceOrder queryRecord = new ServiceOrder();
		queryRecord.setNo(no);
		List<ServiceOrder> serviceOrderList = mServiceOrderService.selectSelective(queryRecord);
		if (serviceOrderList != null && serviceOrderList.size() > 0)
		{
			// 关闭订单
			ServiceOrder serviceOrder = serviceOrderList.get(0);
			if (serviceOrder.getStatus().equals(OrderState.STATE_CALL_IN_PROGRESS.getCode()))
			{
				serviceOrder.setStatus(OrderState.STATE_CALL_NOT_EVALUATION.getCode());
			}
			mServiceOrderService.updateByPrimaryKeySelective(serviceOrder);
		}
	
		return result;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param createTime
	 * @param endTime
	 * @param no
	 * @param status
	 * @param words
	 * @param createTimeDirection
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportServiceOrder.json")
	void exportServiceOrder(HttpServletRequest request, HttpServletResponse response, 
			Date createTime, 
			Date endTime, 
			Byte status, String words, Byte createTimeDirection){
		ResultEntity result = new ResultEntity();
		
		response.setContentType("application/vnd.ms-excel");  
		String codedFileName;
		try{
			codedFileName = java.net.URLEncoder.encode("订单信息", "UTF-8");
		} catch (UnsupportedEncodingException e1){
			codedFileName = "Order";
		} 
		
		response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");  
				
		OutputStream  fOut = null;
		try{
			Workbook wb = new XSSFWorkbook();
	        CreationHelper createHelper = wb.getCreationHelper();
	        Sheet sheet = wb.createSheet("order");
	        //设置列宽
	        sheet.setColumnWidth(1, 16*256);
	        sheet.setColumnWidth(2, 16*256);
	        sheet.setColumnWidth(3, 16*256);
	        sheet.setColumnWidth(4, 16*256);
	        sheet.setColumnWidth(5, 16*256);
	        sheet.setColumnWidth(6, 16*256);
	        sheet.setColumnWidth(7, 16*256);
	        sheet.setColumnWidth(8, 16*256);
	        sheet.setColumnWidth(9, 16*256);
	        sheet.setColumnWidth(10, 16*256);
	        sheet.setColumnWidth(11, 16*256);
	        sheet.setColumnWidth(12, 16*256);
	        sheet.setColumnWidth(13, 16*256);
	        sheet.setColumnWidth(14, 16*256);
	        sheet.setColumnWidth(15, 16*256);
	        //sheet.setColumnWidth(16, 16*16);
	        //填写列名
	        Row row = sheet.createRow((short)0);
	        row.createCell(0).setCellValue("订单号");
	        row.createCell(1).setCellValue("咨询时间");
	        row.createCell(2).setCellValue("求助者账号");
	        row.createCell(3).setCellValue("求助者手机号");
	        row.createCell(4).setCellValue("专家姓名");
	        row.createCell(5).setCellValue("专家手机号");
	        row.createCell(6).setCellValue("价格");
	        row.createCell(7).setCellValue("通话时长\\s");
	        row.createCell(8).setCellValue("咨询形式");
	        row.createCell(9).setCellValue("使用券面额");
	        row.createCell(10).setCellValue("佣金比例");
	        row.createCell(11).setCellValue("平台收入");
	        row.createCell(12).setCellValue("咨询师实得");
	        row.createCell(13).setCellValue("求助者实付");
	        row.createCell(14).setCellValue("状态");
	        //row.createCell(4).setCellValue("通话超过5min");
	        //填写员工信息
	        
	        //if(no != null) words = no;
			List<ServiceOrder> orders = mServiceOrderService.searchAll(words, status,  createTimeDirection, createTime, endTime);
			List<WebServiceOrderDTO> wods = new ArrayList<>();
			for (ServiceOrder serviceOrder : orders)
			{
				// 查询订单组装数据
				WebServiceOrderDTO wod = new WebServiceOrderDTO();
				BeanUtils.copyProperties(serviceOrder, wod);
				// 查询订单类型
				ServiceGoods sg = mServiceGoodsService.selectByPrimaryKey(wod.getSgid());
				if (null != sg)
				{
					wod.setConsultType(sg.getType());
				}
				// 获取消费者信息
				Member consumers = mMemberService.selectMemberByMid(wod.getMid());
				if (null != consumers)
				{
					wod.setConsumersAccount(consumers.getUserName());
					wod.setConsumersPhone(consumers.getMobilePhone());
				}
				// 获取专家信息
				Member specialist = mMemberService.selectMemberByMid(wod.getServiceProviderId());
				if (null != specialist)
				{
					wod.setSpecialistName(specialist.getNickname());
					wod.setSpecialistPhone(specialist.getMobilePhone());
				}
				
				// 以下三种情况"平台收入"和"咨询师实得"字段均金额设置为0： 
				// 		1.订单未接通，2.订单未扣款,3. EAP订单
				if (serviceOrder.getStatus().equals(OrderState.STATE_CALL_NOT_CONNECTED.getCode())
						|| serviceOrder.getStatus().equals(OrderState.STATE_CALL_NOT_CHARGEBECKS.getCode())
							|| serviceOrder.getOrderType().longValue() == 1)
				{
					wod.setServiceRealityAmount(BigDecimal.valueOf(0));
					wod.setPlatformIncome(BigDecimal.valueOf(0));
				} else 
				{
					//未扣款但超时的订单
					if(serviceOrder.getDiscountAmount().intValue()==0&&
							serviceOrder.getCashAmount().intValue()==0)
					{
						wod.setServiceRealityAmount(BigDecimal.valueOf(0));
						wod.setPlatformIncome(BigDecimal.valueOf(0));
					}else
					{
						// 计算专家实际获得的利润 总服务金额-（总服务费*平台佣金比例）
						BigDecimal cost = wod.getCost();
						// 佣金 commission
						double commission = BigDecimalUtil.mul(cost.doubleValue(), Double.valueOf(100- wod.getCommissionRate()) / 100);
						wod.setServiceRealityAmount(BigDecimal.valueOf(commission));
						
						//计算平台收支
						double platfromIncome = wod.getCashAmount().doubleValue() - commission;
						wod.setPlatformIncome(BigDecimal.valueOf(platfromIncome));
					}
				}
				wods.add(wod);
			}
			
			for(short i=0; i< orders.size(); i++)
	        {
	        	row = sheet.createRow(i+1);
		        row.createCell(0).setCellValue(wods.get(i).getNo());//订单号
		        row.createCell(1).setCellValue(DateUtil.dateToStr(wods.get(i).getServiceBeginTime(), "yyyy-MM-dd HH:mm:ss") );//咨询时间
		        row.createCell(2).setCellValue(wods.get(i).getConsumersAccount());//求助者账号
		        row.createCell(3).setCellValue(wods.get(i).getConsumersPhone());//求助者手机号
		        row.createCell(4).setCellValue(wods.get(i).getSpecialistName());//专家姓名
		        row.createCell(5).setCellValue(wods.get(i).getSpecialistPhone());//专家手机号
		        row.createCell(6).setCellValue(wods.get(i).getCost().intValue());//价格
		        row.createCell(7).setCellValue(wods.get(i).getPracticalDuration());//通话时长
		        row.createCell(8).setCellValue((wods.get(i).getConsultType()==0?"实时语音咨询":"实时语音倾述"));//咨询形式
		        row.createCell(9).setCellValue(wods.get(i).getDiscountAmount().intValue());//使用券面额
		        row.createCell(10).setCellValue(wods.get(i).getCommissionRate().intValue());//佣金比例
		        row.createCell(11).setCellValue(wods.get(i).getPlatformIncome().intValue());//平台收入
		        row.createCell(12).setCellValue(wods.get(i).getServiceRealityAmount().intValue());//咨询师所得
		        row.createCell(13).setCellValue(wods.get(i).getCashAmount().intValue());//求助者实付
		        
		        row.createCell(14).setCellValue(getStatus(wods.get(i).getStatus()));//状态
		        //row.createCell(15).setCellValue(wods.get(i).getPracticalDuration()>300?"是":"否");//通话超过5min
	        }
	        	       	        
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

	//获得状态
	public String getStatus(Byte status){
		String result="";
		switch (status){
			case -1:result= "全部";break;
			case 11:result= "未开通";break;
			case 12:result= "未扣款";break;
			case 13:result= "进行中";break;
			case 14:result= "完成未评价";break;
			case 15:result= "完成且评价";break;
			case 4:result= "异常未处理";break;
			case 5:result= "异常已处理";break;
			case 7:result= "投诉未处理";break;
			case 8:result= "投诉已处理";break;
			case 16:result= "过期无效";break;
		}
		return result;
	}

}
