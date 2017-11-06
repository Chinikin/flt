package com.depression.controller.eap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.EapEnterprise;
import com.depression.model.eap.dto.EapEnterpriseDTO;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberAdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.MemberUpdateService;
import com.depression.service.Permission;
import com.depression.service.ServiceStatisticsService;
import com.depression.service.TestingService;
import com.depression.service.UbSearchWordsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/EapEnterprise")
public class EapEnterpriseController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	EapEmployeeService employeeService;
	@Autowired
	ServiceStatisticsService serviceStatService;
	@Autowired
	MemberService memberService;
	@Autowired
	EapEnterpriseService enterpriseService;
	@Autowired
	MemberAdvisoryService memberAdvisoryService;
	@Autowired
	MemberUpdateService memberUpdateService;
	@Autowired
	TestingService testingService;
	@Autowired
	UbSearchWordsService ubSearchWordsService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 获取企业信息
	 * @param eeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEnterprise.json")
	@ResponseBody
	@Permission("6")
	Object obtainEnterpriseInfo(Long eeId){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId
										)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		EapEnterpriseDTO  eeDTO=new EapEnterpriseDTO();
		EapEnterprise ee=new EapEnterprise(); 
		try{
			ee= enterpriseService.obtainEnterpriseByKey(eeId);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		//服务状态 0 被禁止 1未开始 2服务中 3已结束
		BeanUtils.copyProperties(ee, eeDTO);
		
		//设置log路径
		eeDTO.setLogo(ee.getLogoRel());
		eeDTO.setLogoPreview(ee.getLogoPreviewRel());
		
		//设置购买总时长
		eeDTO.setPurchasedTotalTime(eeDTO.getPurchasedOrderAmount() * 50);
		//设置已使用时长
		eeDTO.setUsedTime(eeDTO.getConsumedOrderAmount() * 50 );
		//剩余时长
		eeDTO.setRemainTime(eeDTO.getPurchasedTotalTime() - eeDTO.getUsedTime());
		
		
		//计算服务状态
		Date current = new Date();
		if(ee.getIsEnable()==1)
		{// 0 被禁用
			eeDTO.setServiceStatus((byte)0);
		}else if(ee.getServiceStartDate().after(current))
		{// 1 未开始
			eeDTO.setServiceStatus((byte)1);
		}else if(ee.getServiceEndDate().before(current))
		{// 3 已结束
			eeDTO.setServiceStatus((byte)3);
		}else
		{// 2 进行中
			eeDTO.setServiceStatus((byte)2);
		}
		
		//计算剩余天数
		if(ee.getServiceStartDate().after(current))
		{// 1 未开始
			eeDTO.setRemainingDays(daysBetween(ee.getServiceEndDate(), ee.getServiceStartDate()));
		}else if(ee.getServiceEndDate().before(current))
		{// 3 已结束
			eeDTO.setRemainingDays(0l);
		}else
		{// 2 进行中
			//进行中的天数显示+1，将当天计入
			eeDTO.setRemainingDays(daysBetween(ee.getServiceEndDate(), current) + 1);
		}
		
		
		result.put("eeDTO", eeDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	//获取剩余天数
	public Long daysBetween(Date end, Date begin)  
    {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(begin);  
        Long timeB = cal.getTimeInMillis();               
        cal.setTime(end);  
        Long timeE = cal.getTimeInMillis();       
        return (timeE-timeB)/(1000*3600*24);  
        
    } 
	
	
	
	/**
	 	 * 员工模板
	 	 * @param request
	 	 * @param response
	 	 * @param eeId
	 	 */
	 	@RequestMapping(method = RequestMethod.POST, value = "/exportEmployee.json")
	 	void exportEmployee(HttpServletRequest request, HttpServletResponse response, Long eeId){
	 		ResultEntity result = new ResultEntity();
	 		
	 		if(PropertyUtils.examineOneNull(eeId
	 				)){
	 			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
	 			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
	 			return;
	 		}
	 		
	 		EapEnterprise ee = enterpriseService.obtainEnterpriseByKey(eeId);
	 		if(ee == null){
	 			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
	 			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
	 			return;
	 		}
	 		
	 		response.setContentType("application/vnd.ms-excel");  
	 		String codedFileName;
	 		try{
	 			codedFileName = java.net.URLEncoder.encode(ee.getName() +  "员工EAP信息", "UTF-8");
	 		} catch (UnsupportedEncodingException e1){
  			codedFileName = "EapEmloyee";
	 		} 
	 		
	 		response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");  
	 				
	 		OutputStream  fOut = null;
	 		try{
	 			Workbook wb = new XSSFWorkbook();
	 	        CreationHelper createHelper = wb.getCreationHelper();
	 	        Sheet sheet = wb.createSheet("EAP");
	 	        
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
	 	        
	 	        //学校类型
	 	        if(ee.getType() == EapEnterpriseService.TYPE_COLLEGE){
	 	        
	 		        //填写列名
	 		        Row row = sheet.createRow((short)0);
	 		        row.createCell(0).setCellValue("姓名");
	 		        row.createCell(1).setCellValue("手机号");
	 		        row.createCell(2).setCellValue("学工号");
	 		        row.createCell(3).setCellValue("年级");
	 		        row.createCell(4).setCellValue("学院");
	 		        
	 		        //数据模板
	 		        Row row1 = sheet.createRow((short)1);
	 		        row1.createCell(0).setCellValue("张三");
	 		        row1.createCell(1).setCellValue("13312345678");
	 		        row1.createCell(2).setCellValue("2015123546");
	 		        row1.createCell(3).setCellValue("2015");
	 		        row1.createCell(4).setCellValue("体育学院");
	 		        
	 		      
	 		        //公司类型
	 	        }else if(ee.getType() == EapEnterpriseService.TYPE_COMPANY){
	 	        	//填写列名
	 		        Row row = sheet.createRow((short)0);
	 		        row.createCell(0).setCellValue("姓名");
	 		        row.createCell(1).setCellValue("手机号");
	 		        row.createCell(2).setCellValue("学工号");
	 		        row.createCell(3).setCellValue("备注1");
	 		        row.createCell(4).setCellValue("备注2");
	 		        
	 		    
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
	 					log.error(ErrorCode.ERROR_GENERATE_FILE_FAILED.getMessage(), e);
	 				}
	 			}
	 		}	
	  }
	
}
