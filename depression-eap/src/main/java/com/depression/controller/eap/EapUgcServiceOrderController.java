package com.depression.controller.eap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
import com.depression.entity.ResultEntity;
import com.depression.model.EapEmployee;
import com.depression.model.EapEnterprise;
import com.depression.model.Member;
import com.depression.model.PsychoInfo;
import com.depression.model.ServiceCustomerStatistics;
import com.depression.model.ServiceOrder;
import com.depression.model.eap.dto.EapEmployeeDTO;
import com.depression.model.eap.dto.EapServiceOrderDTO;
import com.depression.model.eap.vo.EapEmployeeVO;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.EapService;
import com.depression.service.MemberAdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.MemberUpdateService;
import com.depression.service.Permission;
import com.depression.service.PsychoInfoService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.service.TestingService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/EapUgcServiceOrder")
public class EapUgcServiceOrderController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private EapEmployeeService employeeService;
	@Autowired
	private ServiceStatisticsService serviceStatService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private EapEnterpriseService enterpriseService;
	@Autowired
	private MemberAdvisoryService memberAdvisoryService;
	@Autowired
	private MemberUpdateService memberUpdateService;
	@Autowired
	private TestingService testingService;
	@Autowired
	private ServiceOrderService serviceOrderService;
	@Autowired
	private PsychoInfoService psychoInfoService;
	@Autowired
	private EapService eapService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 获取咨询流水列表
	 * @param eeId 企业主键
 	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainOrderList.json")
	@ResponseBody
	@Permission("3")
	Object obtainOrderList(Long eeId,Long mid, Integer pageIndex, Integer pageSize){
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
			soDTOs=getEapServiceOrderList(eeId,mid, pageIndex, pageSize);
			ServiceOrder so=new ServiceOrder();
			so.setEeId(eeId);
			so.setMid(mid);
			count=serviceOrderService.selectCount(so);
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
	
	
	@Permission("3")
	@RequestMapping(method = RequestMethod.POST, value = "/exportServiceOrder.json")
	void exportServiceOrder(HttpServletRequest request, HttpServletResponse response, Long eeId,Long mid){
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
	        List<EapServiceOrderDTO> list = getEapServiceOrderList(eeId,mid, null, null);
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
			        Integer t=list.get(i).getPracticalDuration();
			        row.createCell(8).setCellValue(secToTime(t));
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
	
	
		// a integer to xx:xx:xx  
	    public static String secToTime(int time) {  
	        String timeStr = null;  
	        int hour = 0;  
	        int minute = 0;  
	        int second = 0;  
	        if (time <= 0)  
	            return "00\"00";  
	        else {  
	            minute = time / 60;  
	            if (minute < 60) {  
	                second = time % 60;  
	                timeStr = unitFormat(minute) + "'" + unitFormat(second);  
	            } else {  
	                hour = minute / 60;  
	                if (hour > 99)  
	                    return "99:59:59";  
	                minute = minute % 60;  
	                second = time - hour * 3600 - minute * 60;  
	                timeStr = unitFormat(hour) + "\"" + unitFormat(minute) + "'" + unitFormat(second);  
	            }  
	        }  
	        return timeStr;  
	    }  
	  
	    public static String unitFormat(int i) {  
	        String retStr = null;  
	        if (i >= 0 && i < 10)  
	            retStr = "0" + Integer.toString(i);  
	        else  
	            retStr = "" + i;  
	        return retStr;  
	    }
	
	public List<EapServiceOrderDTO> getEapServiceOrderList(Long eeId,Long mid, Integer pageIndex, Integer pageSize) throws Exception{
		List<EapServiceOrderDTO> soDTOs=new ArrayList<EapServiceOrderDTO>();
		
		
		List<ServiceOrder> sos= serviceOrderService.getServiceOrderListInEap(eeId,mid,pageIndex,pageSize);
		
		
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
	
	
	
	
}
