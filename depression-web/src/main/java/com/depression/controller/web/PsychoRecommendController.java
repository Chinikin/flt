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

import com.alibaba.fastjson.JSONArray;
import com.depression.base.ucpaas.DateUtil;
import com.depression.entity.ErrorCode;
import com.depression.entity.OrderState;
import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.PsychoRecommend;
import com.depression.model.Recommend;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.ServiceOrderEvaluation;
import com.depression.model.ServicePsychoStatistics;
import com.depression.model.web.dto.WebPsychoRecommendDTO;
import com.depression.model.web.dto.WebRecommendDTO;
import com.depression.model.web.dto.WebServiceOrderDTO;
import com.depression.service.MemberService;
import com.depression.service.PsychoRecommendService;
import com.depression.service.RecommendService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderCustomService;
import com.depression.service.ServiceOrderEvaluationService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.BigDecimalUtil;
import com.depression.utils.PropertyUtils;
import com.depression.utils.WebUtil;
import com.github.pagehelper.Page;

@Controller
@RequestMapping("/PsychoRecommend")
public class PsychoRecommendController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	PsychoRecommendService psychoRecommendService;
	@Autowired
	MemberService memberService;
	@Autowired
	ServiceOrderService serviceOrderService;
	@Autowired
	RecommendService recommendService;
	@Autowired
	ServiceOrderService mServiceOrderService;
	@Autowired
	ServiceOrderCustomService serviceOrderCustomService;
	@Autowired
	MemberService mMemberService;
	@Autowired
	ServiceGoodsService mServiceGoodsService;
	@Autowired
	ServiceOrderEvaluationService serviceOrderEvaluationService;
	@Autowired
	ServiceStatisticsService serviceStatService;
	
	
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 邀请专家到推荐列表中
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addPsychoRecommend.json")
	@ResponseBody
	public Object addPsychoRecommend(String mids){
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mids)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		
		List<Long> pids = JSONArray.parseArray(mids, Long.class);
		Integer count;
		try{
			count = psychoRecommendService.addPsycho2Recommend(pids);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPsychoRecommendList.json")
	@ResponseBody
	public Object obtainPsychoRecommendList(Integer pageIndex,Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex,pageSize)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		
		List<WebPsychoRecommendDTO> prDTOs = new ArrayList<WebPsychoRecommendDTO>();
		Page<PsychoRecommend> prs =new Page<PsychoRecommend>();
		Integer count;
		try{
			PsychoRecommend pr = new PsychoRecommend();
			pr.setPageIndex(pageIndex);
			pr.setPageSize(pageSize);
			prs = psychoRecommendService.selectRecommendPsychos(pr);
			//处理返回数据
			for(PsychoRecommend p: prs){
				WebPsychoRecommendDTO wprDTO = new WebPsychoRecommendDTO();
				packWebPsychoRecommendDTO(p,wprDTO);
				prDTOs.add(wprDTO);
			}
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", prs.getTotal());
		result.put("prDTOs", prDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	private void packWebPsychoRecommendDTO(PsychoRecommend p,
			WebPsychoRecommendDTO wprDTO) {
		BeanUtils.copyProperties(p, wprDTO);
		Member m = memberService.selectMemberByMid(p.getMid());
		BeanUtils.copyProperties(m, wprDTO);
		
		//设置接单统计
		ServiceOrder so = new ServiceOrder();
		so.setServiceProviderId(p.getMid());
		Integer soc = serviceOrderService.countServiceOrder(so);
		wprDTO.setOrderCount(soc);
		
		//用户评分
		/*List<ServiceOrder> sos = serviceOrderService.selectSelective(so);
		List<Long> soids=new ArrayList<Long>();
		for(ServiceOrder s:sos){
			soids.add(s.getSoid());
		}
		
		List<ServiceOrderEvaluation> soes=serviceOrderEvaluationService.selectBySoidsBulk(soids);
		Integer count = 0;
		Integer total = 0;
		for(ServiceOrderEvaluation soe :soes){
			Integer eva = soe.getScore();
			if(eva != null){
				count ++;
				total+=eva;
			}
		}
		if(count != 0){
			BigDecimal bg = new BigDecimal(total/count.doubleValue());  
	        double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
			wprDTO.setOrderEvaluation(f1);
			}else{
				wprDTO.setOrderEvaluation(0.0);
			}*/
		
		//从咨询师服务统计中获取信息
		ServicePsychoStatistics sps = serviceStatService.getOrCreatePsychoStat(p.getMid());
		wprDTO.setOrderEvaluation(sps.getEapScore());
			
	}
	
	/**
	 * 更新每日固定名额
	 * @param pr
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updatePsychoRecommend.json")
	@ResponseBody
	public Object updatePsychoRecommend(PsychoRecommend pr){
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pr.getPrId(),pr.getDailyNumber())){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			 //修改剩余
			PsychoRecommend psychoRecommend = psychoRecommendService.selectPsychoRecommendByPrimaryKey(pr.getPrId());
			
			//今日剩余  逻辑处理
			Integer usedRecommend = psychoRecommend.getDailyNumber() - psychoRecommend.getRemainNumber();
			Integer remain = pr.getDailyNumber() - usedRecommend;
			
			if(remain < 0){
				pr.setRemainNumber(0);
			}else{
				pr.setRemainNumber(remain);
			}
			
			 psychoRecommendService.updatePsychoRecommend(pr);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	/**
	 * 更新推荐属性
	 * @param pr
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainRecommend.json")
	@ResponseBody
	public Object obtainRecommend(){
		ResultEntity result = new ResultEntity();
		Recommend r;
		WebRecommendDTO recommendDTO = new WebRecommendDTO();
		try{
			r = recommendService.getRecommend();
			BeanUtils.copyProperties(r, recommendDTO);
			
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		result.put("recommend", recommendDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 更新推荐属性
	 * @param pr
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateRecommend.json")
	@ResponseBody
	public Object updateRecommend(Recommend recommend){
		ResultEntity result = new ResultEntity();
		
		if (PropertyUtils.examineOneNull(recommend.getDetailLink(),
										 recommend.getIsOpened(),
										 recommend.getMemberLimit(),
										 recommend.getPageIndex(),
									 	 recommend.getPageSize(),
										 recommend.getTitle()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		
		//活动价格开启  价格必传
		if(recommend.getPriceOpened() == 1){
			if (PropertyUtils.examineOneNull(recommend.getPrice())){
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}
		}
		
		WebRecommendDTO wrDTO = new WebRecommendDTO();
		try{
			//先删除当前数据库中recommend  然后再添加一个
			 Recommend r = recommendService.getRecommend();
			 recommendService.deleteRecommendByPrimaryKey(r.getrId());
			 
			 if("".equals(recommend.getCover()) || null == recommend.getCover()){
				 recommend.setCover("-1");
				 recommend.setDetailLink("-1");
			 }
			 
			 if(recommend.getPriceOpened() == 0){
				 BigDecimal price = new BigDecimal(-1);
				 recommend.setPrice(price);
			 }
			 
			 recommend.setCreateTime(new Date());
			 recommendService.insertRecommend(recommend);
			 BeanUtils.copyProperties(recommend, wrDTO);
			  
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		result.put("recommendDTO", wrDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/removePsychoRecommend.json")
	@ResponseBody
	public Object removePsychoRecommend(Long prId){
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(prId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			 psychoRecommendService.removePsychoRecommend(prId);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
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
	public Object getServiceOrderByPage(Date createTime, Date endTime,  Integer pageIndex, Integer pageSize, String nickname, Byte createTimeDirection)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex, pageSize)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		Page<ServiceOrder> orders;
		List<WebServiceOrderDTO> wods;
		try
		{
			orders = serviceOrderCustomService.searchRecommendOrder(nickname,pageIndex,pageSize, createTimeDirection, createTime, endTime);
			wods = new ArrayList<>();
			for (ServiceOrder serviceOrder : orders){
				// 查询订单组装数据
				WebServiceOrderDTO wod = new WebServiceOrderDTO();
				packServiceOrder(serviceOrder, wod);
				wods.add(wod);
			}
			result.put("count", orders.getTotal());
			result.put("list", wods);
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error(e);
		}
		return result;
	}
	
	
	void packServiceOrder(ServiceOrder serviceOrder,WebServiceOrderDTO wod){
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
			wod.setpLevel(specialist.getpLevel());
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
			Byte status, String words, Byte createTimeDirection,String ids,String nickname){
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
	       
	        //sheet.setColumnWidth(16, 16*16);
	        //填写列名
	        Row row = sheet.createRow((short)0);
	        row.createCell(0).setCellValue("订单时间");
	        row.createCell(1).setCellValue("专家姓名");
	        row.createCell(2).setCellValue("电话号码");
	        row.createCell(3).setCellValue("服务类型");
	        row.createCell(4).setCellValue("求助者电话");
	        row.createCell(5).setCellValue("求助者实付");
	        row.createCell(6).setCellValue("专家实得");
	        row.createCell(7).setCellValue("拨打时长");
	        row.createCell(8).setCellValue("咨询定价");
	        //row.createCell(4).setCellValue("通话超过5min");
	        //填写员工信息
	        
	        //if(no != null) words = no;
	        List<WebServiceOrderDTO> wods = new ArrayList<>();
	        List<ServiceOrder> orders = new ArrayList<ServiceOrder>();
	        if(ids == null){
	        	orders = serviceOrderCustomService.searchRecommendOrder(nickname,null,null, createTimeDirection, createTime, endTime);
	        	//orders = serviceOrderCustomService.getAllRecommendOrder();
	        }else{
	        	List<Long> soids = JSONArray.parseArray(ids, Long.class);
	        	orders = serviceOrderService.selectByPrimaryKeyBulk(soids);
	        }
			for (ServiceOrder serviceOrder : orders)
			{
				// 查询订单组装数据
				WebServiceOrderDTO wod = new WebServiceOrderDTO();
				packServiceOrder(serviceOrder, wod);
				wods.add(wod);
			}
			
			for(short i=0; i< orders.size(); i++)
	        {
	        	row = sheet.createRow(i+1);
		        row.createCell(0).setCellValue(DateUtil.dateToStr(wods.get(i).getServiceBeginTime(), "yyyy-MM-dd HH:mm:ss"));//订单号
		        row.createCell(1).setCellValue(wods.get(i).getSpecialistName());//咨询时间
		        row.createCell(2).setCellValue(wods.get(i).getSpecialistPhone());//求助者账号
		        //String serviceType = wods.get(i).getpLevel()==0?"专业咨询":"轻咨询";
		        row.createCell(3).setCellValue(wods.get(i).getpLevel()==0?"专业咨询":"轻咨询");//求助者手机号
		        row.createCell(4).setCellValue(wods.get(i).getConsumersPhone());//专家姓名
		        row.createCell(5).setCellValue(wods.get(i).getCashAmount()+"");//专家手机号
		        row.createCell(6).setCellValue(wods.get(i).getServiceRealityAmount()+"");//价格
		        String time = WebUtil.secToTime(wods.get(i).getPracticalDuration());
		        row.createCell(7).setCellValue(time);//通话时长
		        row.createCell(8).setCellValue((wods.get(i).getCost()+""));//咨询形式
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
