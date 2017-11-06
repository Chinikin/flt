package com.depression.controller.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.EapEmployee;
import com.depression.model.EapEnterprise;
import com.depression.model.Member;
import com.depression.model.ServiceCustomerStatistics;
import com.depression.model.ServiceOrder;
import com.depression.model.web.dto.WebEapEmployeeDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberService;
import com.depression.service.ServiceOrderCustomService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/EapEmployee")
public class EapEmployeeController
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
	ServiceOrderCustomService serviceOrderCustomService;
	
	/**
	 * 新增企业员工
	 * @param eeId	企业id
	 * @param name	员工名字
	 * @param phoneNum	手机号码
	 * @param number	学/工号
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/newEmployee.json")
	@ResponseBody
	Object newEmployee(Long eeId, String name, String phoneNum, String number)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId, name, phoneNum)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			employeeService.newEmployee(eeId, name, phoneNum, number);
		} catch (Exception e)
		{
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
	 * 启用/禁用企业员工
	 * @param idsVO 企业员工id（eemId）列表
	 * @param isEnable 0 启用 1禁用
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enableEmployee.json")
	@ResponseBody
	Object enableEmployee(WebIdsVO idsVO, Byte isEnable)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(idsVO, idsVO.getIds(), isEnable)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			employeeService.enableEmployee(idsVO.getIds(), isEnable);
		} catch (Exception e)
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
	 * 删除企业员工
	 * @param idsVO 企业员工id（eemId）列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteEmployee.json")
	@ResponseBody
	Object deleteEmployee(WebIdsVO idsVO)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(idsVO, idsVO.getIds())){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			employeeService.deleteEmployee(idsVO.getIds());
		} catch (Exception e)
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
	 * 获取员工列表
	 * @param words 关键字
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEmployeeList.json")
	@ResponseBody
	Object obtainEmployeeList(Long eeId, String words, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex, pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<WebEapEmployeeDTO> eemDTOs = new ArrayList<WebEapEmployeeDTO>();
		Integer count;
		try{
			List<EapEmployee> eems =  employeeService.searchEapEmployee(eeId, words, pageIndex, pageSize);
			count = employeeService.countSearchEapEmployee(eeId, words);
			
			for(EapEmployee eem : eems)
			{
				WebEapEmployeeDTO eemDTO = new WebEapEmployeeDTO();
				BeanUtils.copyProperties(eem, eemDTO);
				
				//从用户服务统计中获取总消费信息
				Member m = memberService.selectMemberByMobilePhone(eem.getPhoneNum());
				if(m != null)
				{
					
					ServiceCustomerStatistics scs = serviceStatService.getOrCreateCustomerStatV1(m.getMid(),eeId);
					eemDTO.setEapAdvisoryDuration(scs.getEapAdvisoryDuration());
					eemDTO.setEapAdvisoryTimes(scs.getEapAdvisoryTimes());
					
					//eap订单处理
					/*ServiceOrder so = new ServiceOrder();
					so.setEeId(eeId);
					so.setMid(m.getMid());
					List<ServiceOrder> sos = serviceOrderCustomService.getEapServiceOrder(so);
					Long eapDuration = 0l;
					for(ServiceOrder s:sos){
						eapDuration+=s.getPracticalDuration();
					}
					eemDTO.setEapAdvisoryDuration(eapDuration/60);
					eemDTO.setEapAdvisoryTimes(sos.size());*/
				}
				eemDTOs.add(eemDTO);
			}
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("eemDTOs", eemDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	Object importEmployee(MultipartHttpServletRequest request, Long eeId, Integer mode)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		EapEnterprise ee = enterpriseService.obtainEnterpriseByKey(eeId);
		if(ee == null)
		{
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return result;
		}
		
		// 创建迭代器
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
	
		// 检查是否有文件
		if (!itr.hasNext())
		{
			result.setCode(ErrorCode.ERROR_UPLOAD_FILE_NOT_FOUND.getCode());
			result.setMsg(ErrorCode.ERROR_UPLOAD_FILE_NOT_FOUND.getMessage());
			return result;
		}
	
		// 循环文件
		while (itr.hasNext())
		{
			// 遍历文件
			mpf = request.getFile(itr.next());
			if (mpf != null)
			{
				try
				{
					 Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(mpf.getBytes()));
					 Sheet sheet = wb.getSheetAt(0);
					 //验证Excel格式
					 String[] row0Str = {"姓名", "手机号", "工号"};
					 Row row0 = sheet.getRow(0);					 
					 DataFormatter formatter = new DataFormatter();
					 for(int i=0; i<3; i++)
					 {
						 Cell cell = row0.getCell(i);
						 String colName = formatter.formatCellValue(cell);	
				         if(!colName.contains(row0Str[i]))
				         {
				        	 throw new Exception("incorect format");
				         }
					 }

					 //读取Excel中的数据
					 List<EapEmployee> eems = new ArrayList<EapEmployee>();
					 for(int i=1; i<=sheet.getLastRowNum(); i++)
					 {
						 Row row = sheet.getRow(i);
						 EapEmployee eem = new EapEmployee();
						 //读取 姓名 手机号 工号/学号
						 Cell cell = row.getCell(0);
						 String name = formatter.formatCellValue(cell);	
						 eem.setName(name);
						 
						 cell = row.getCell(1);
						 String phoneNum = formatter.formatCellValue(cell);	
						 eem.setPhoneNum(phoneNum);

						 cell = row.getCell(2);
						 String number = formatter.formatCellValue(cell);	
						 eem.setNumber(number);
						 
						 eem.setEeId(eeId);
						 eems.add(eem);
					 }
					 
					 //清楚所有原始记录
					 if(mode == 0)
					 {
						 employeeService.deleteEmployeeByEeId(eeId);
					 }
					 
					 //插入新纪录
					 for(EapEmployee eem : eems)
					 {
						 employeeService.newEmployee(eem.getEeId(), eem.getName(), 
								 eem.getPhoneNum(), eem.getNumber());
					 }
				} catch (Exception e)
				{
					log.error(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage(), e);
					result.setCode(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getCode());
					result.setMsg(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage());
					return result;
				}
			}
		}
	
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 全新导入员工列表，删除原有记录
	 * @param request
	 * @param eeId 企业id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/entireImportEmployee.json")
	@ResponseBody
	Object entireImportEmployee(MultipartHttpServletRequest request, Long eeId)
	{
		return importEmployee(request, eeId, 0);
	}
	
	/**
	 * 部分导入员工列表，增量导入
	 * @param request
	 * @param eeId 企业id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/partialImportEmployee.json")
	@ResponseBody
	Object partialImportEmployee(MultipartHttpServletRequest request, Long eeId)
	{
		return importEmployee(request, eeId, 1);
	}
	
	/**
	 * 导出企业所有员工记录
	 * @param request
	 * @param response
	 * @param eeId
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportEmployee.json")
	void exportEmployee(HttpServletRequest request, HttpServletResponse response, Long eeId)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return;
		}
		
		EapEnterprise ee = enterpriseService.obtainEnterpriseByKey(eeId);
		if(ee == null)
		{
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return;
		}
		
		response.setContentType("application/vnd.ms-excel");  
		String codedFileName;
		try
		{
			codedFileName = java.net.URLEncoder.encode(ee.getName() + "员工EAP信息", "UTF-8");
		} catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
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
	        //填写列名
	        Row row = sheet.createRow((short)0);
	        row.createCell(0).setCellValue("姓名");
	        row.createCell(1).setCellValue("手机号");
	        row.createCell(2).setCellValue("工号/学号");
	        row.createCell(3).setCellValue("EAP咨询次数");
	        row.createCell(4).setCellValue("EAP咨询时长(分)");
	        
	        //填写员工信息
	        List<EapEmployee> eems = employeeService.obtainEmployeeByEeId(eeId);
	        for(short i=0; i< eems.size(); i++)
	        {
	        	row = sheet.createRow(i+1);
		        row.createCell(0).setCellValue(eems.get(i).getName());
		        row.createCell(1).setCellValue(eems.get(i).getPhoneNum());
		        row.createCell(2).setCellValue(eems.get(i).getNumber());
		        row.createCell(3).setCellValue(eems.get(i).getEapAdvisoryTimes());
		        row.createCell(4).setCellValue(eems.get(i).getEapAdvisoryDuration()/60);
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
	
}
