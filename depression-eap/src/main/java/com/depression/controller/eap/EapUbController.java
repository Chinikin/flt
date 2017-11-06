package com.depression.controller.eap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.EapEmployee;
import com.depression.model.EapEmployeeCustom;
import com.depression.model.EapEnterprise;
import com.depression.model.ServiceCustomerStatistics;
import com.depression.model.eap.dto.EapEmployeeDTO;
import com.depression.model.eap.vo.EapEmployeeVO;
import com.depression.model.web.vo.WebIdsVO;
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
@RequestMapping("/EapUb")
public class EapUbController
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
	 * 新增企业员工
	 * @param eeId	企业id
	 * @param name	员工名字
	 * @param phoneNum	手机号码
	 * @param number	学/工号
	 * @param grade	年级
	 * @param college	学院
	 * @param remark1 	备注1
	 * @param remark2	 备注2
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/newEmployee.json")
	@ResponseBody
	@Permission("2")
	Object newEmployee(EapEmployeeVO eemVO){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eemVO.getName(),
										eemVO.getEeId(),
										eemVO.getPhoneNum(),
										eemVO.getNumber()
										)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			EapEmployee eem=new EapEmployee();
			BeanUtils.copyProperties(eemVO, eem);
			employeeService.newEmployee(eem);
		} catch (Exception e){
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
	 * 获取搜索属性 
	 * @param eeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEapAttributes.json")
	@ResponseBody
	@Permission("2")
	Object obtainEapAttributes(Long eeId){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		List<String> colleges=new ArrayList<String>();
		List<Long> grades=new ArrayList<Long>();
		try{
			
			colleges=employeeService.getCollegesByEeId(eeId);
			grades=employeeService.getGradesByEeId(eeId);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		result.put("colleges", colleges);
		result.put("grades", grades);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	/**
	 * 获取用户数据
	 * @param eeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEmployeeData.json")
	@ResponseBody
	@Permission("2")
	Object obtainEmployeeData(Long eeId){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		//导入
		Integer expedEmployeeNumber=0;
		//注册
		Integer regedEmployee=0;
		//活跃
		Integer activeEmployee=0;
		try{
			EapEmployeeVO eemVO=new EapEmployeeVO();
			eemVO.setEeId(eeId);
			expedEmployeeNumber=employeeService.countEapEmployeeList(eemVO);
			List<Long> list=enterpriseService.getRegedEmployee(eeId);
			regedEmployee=list.size();
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		result.put("expedEmployeeNumber", expedEmployeeNumber);
		result.put("regedEmployee", regedEmployee);
		result.put("activeEmployee", activeEmployee);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	/**
	 * 
	 * @param tag
	 * @param eemId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateEmployee.json")
	@ResponseBody
	@Permission("2")
	Object updateEmployee(EapEmployeeVO eemVO){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eemVO.getEemId())){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		Integer num;
		try{
			
			EapEmployee eem=new EapEmployee();
			BeanUtils.copyProperties(eemVO, eem);
			num=employeeService.updateEapEmployeeByEemId(eem);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		if(num == 0){
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
		}else{
			result.setCode(ErrorCode.SUCCESS.getCode());
			result.setMsg(ErrorCode.SUCCESS.getMessage());
		}
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
	@Permission("2")
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
		} catch (Exception e){
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
	@Permission("2")
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
	 * @param startTime 注册日期（开始）
	 * @param endTime 注册日期（结束）
	 * @param grade 年级
	 * @param college 学院
 	 * @param pageIndex
	 * @param pageSize
	 * @param isEnable
	 * @param sortTag  排序标记   
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEmployeeList.json")
	@ResponseBody
	@Permission("2")
	Object obtainEmployeeList(EapEmployeeVO eemVO,Integer sortTag, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex,pageSize,eemVO.getEeId()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<EapEmployeeDTO> eemDTOs = new ArrayList<EapEmployeeDTO>();
		Integer count;
		Integer eeType;
		
		ResultEntity totalCount=new ResultEntity();
		try{
			EapEnterprise ee=enterpriseService.obtainEnterpriseByKey(eemVO.getEeId());
			eeType=ee.getType();
			count = employeeService.countEapEmployeeList(eemVO);
			totalCount=getTotalCount(eemVO);
			eemDTOs=getEapEmployeeList(eemVO,sortTag,ee, pageIndex, pageSize);
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("eemDTOs", eemDTOs);
		result.put("count", count);
		result.put("eeType", eeType);
		result.putAll(totalCount);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	Object importEmployee(MultipartHttpServletRequest request, Long eeId, Integer mode){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		EapEnterprise ee = enterpriseService.obtainEnterpriseByKey(eeId);
		if(ee == null){
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return result;
		}
		
		// 创建迭代器
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
	
		// 检查是否有文件
		if (!itr.hasNext()){
			result.setCode(ErrorCode.ERROR_UPLOAD_FILE_NOT_FOUND.getCode());
			result.setMsg(ErrorCode.ERROR_UPLOAD_FILE_NOT_FOUND.getMessage());
			return result;
		}
		
		Integer count=0;
		Integer failCount=0;
		// 循环文件
		while (itr.hasNext()){
			// 遍历文件
			mpf = request.getFile(itr.next());
			if (mpf != null){
				try{
					 Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(mpf.getBytes()));
					 Sheet sheet = wb.getSheetAt(0);
					 
					 //根据企业类型选择不同的导入方式
					 //学校类型
					 if(ee.getType() == EapEnterpriseService.TYPE_COLLEGE){
						 
						 //验证Excel格式
						 String[] row0Str = {"姓名", "手机号", "学工号","年级","学院"};
						 Row row0 = sheet.getRow(0);					 
						 DataFormatter formatter = new DataFormatter();
						 for(int i=0; i<5; i++){
							 Cell cell = row0.getCell(i);
							 String colName = formatter.formatCellValue(cell);	
					         if(!colName.contains(row0Str[i])){
					        	 throw new Exception("incorect format");
					         }
						 }

						 //读取Excel中的数据
						 List<EapEmployee> eems = new ArrayList<EapEmployee>();
						 for(int i=1; i<=sheet.getLastRowNum(); i++) {
							 Row row = sheet.getRow(i);
							 EapEmployee eem = new EapEmployee();
							 //读取 姓名 手机号 工号/学号 年级 学院
							 Cell cell = row.getCell(0);
							 String name = formatter.formatCellValue(cell);	
							 eem.setName(name);
							 
							 cell = row.getCell(1);
							 String phoneNum = formatter.formatCellValue(cell);	
							 eem.setPhoneNum(phoneNum);

							 cell = row.getCell(2);
							 String number = formatter.formatCellValue(cell);	
							 eem.setNumber(number);
							 
							 cell = row.getCell(3);
							 String grade = formatter.formatCellValue(cell);	
							 eem.setGrade(Long.parseLong(grade));
							 
							 cell = row.getCell(4);
							 String college = formatter.formatCellValue(cell);	
							 eem.setCollege(college);
							 eem.setEeId(eeId);
							 eem.setCreateTime(new Date());
							 eems.add(eem);
						 }
						 //清楚所有原始记录
						 if(mode == 0){
							 employeeService.deleteEmployeeByEeId(eeId);
						 }
						 
						 //插入新纪录
						 for(EapEmployee eem : eems){
							 Integer num=employeeService.newEmployee(eem);
							 if(num == 1){
								 count+=1;
							 }
							 if(num == 0){
								 failCount+=1;
							 }
						 }
						 
					 }
					 
					//企业类型
					 if(ee.getType() == EapEnterpriseService.TYPE_COMPANY){
						 
						 //验证Excel格式
						 String[] row0Str = {"姓名", "手机号", "学工号","备注1","备注2"};
						 Row row0 = sheet.getRow(0);					 
						 DataFormatter formatter = new DataFormatter();
						 for(int i=0; i<5; i++){
							 Cell cell = row0.getCell(i);
							 String colName = formatter.formatCellValue(cell);	
					         if(!colName.contains(row0Str[i])){
					        	 throw new Exception("incorect format");
					         }
						 }

						 //读取Excel中的数据
						 List<EapEmployee> eems = new ArrayList<EapEmployee>();
						 for(int i=1; i<=sheet.getLastRowNum(); i++) {
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
							 
							 cell = row.getCell(3);
							 String remark1 = formatter.formatCellValue(cell);	
							 eem.setRemark1(remark1);
							 
							 cell = row.getCell(4);
							 String remark2 = formatter.formatCellValue(cell);	
							 eem.setRemark2(remark2);
							 
							 eem.setEeId(eeId);
							 eem.setCreateTime(new Date());
							 eems.add(eem);
						 }
						 //清楚所有原始记录
						 if(mode == 0){
							 employeeService.deleteEmployeeByEeId(eeId);
						 }
						 
						 //插入新纪录
						 for(EapEmployee eem : eems){
							 Integer num=employeeService.newEmployee(eem);
							 if(num == 1){
								 count+=1;
							 }
							 if(num == 0){
								 failCount+=1;
							 }
						 }
					 }
					 
					 
					
				} catch (Exception e){
					log.error(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage(), e);
					result.setCode(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getCode());
					result.setMsg(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage());
					return result;
				}
			}
		}
		
		
		
		result.put("sucCount", count);
		result.put("failCount", failCount);
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
	@Permission("2")
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
	@Permission("2")
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
	@Permission("2")
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
			codedFileName = java.net.URLEncoder.encode(ee.getName() + "员工EAP信息", "UTF-8");
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
	        
	        //填写员工信息
	        EapEmployeeVO eemVO=new EapEmployeeVO();
	        eemVO.setEeId(eeId);
	        List<EapEmployeeDTO> eems = getEapEmployeeList(eemVO,null,ee, null, null);
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
		        row.createCell(5).setCellValue("电话\\预约咨询");
		        row.createCell(6).setCellValue("提问");
		        row.createCell(7).setCellValue("发表心情");
		        row.createCell(8).setCellValue("检索词");
		        row.createCell(9).setCellValue("测试");
		        row.createCell(10).setCellValue("自杀系数");
		        
		      
		        for(short i=0; i< eems.size(); i++){
		        	row = sheet.createRow(i+1);
			        row.createCell(0).setCellValue(eems.get(i).getName());
			        row.createCell(1).setCellValue(eems.get(i).getPhoneNum());
			        row.createCell(2).setCellValue(eems.get(i).getNumber());
			        if(eems.get(i).getGrade() == null){
			        	row.createCell(3).setCellValue("");
			        }else{
			        	row.createCell(3).setCellValue(eems.get(i).getGrade());
			        }			        
			        row.createCell(4).setCellValue(eems.get(i).getCollege());
			        row.createCell(5).setCellValue(eems.get(i).getEapAdvisoryTimes());
			        row.createCell(6).setCellValue(eems.get(i).getMemberAdvisoryCount());
			        row.createCell(7).setCellValue(eems.get(i).getMemberUpdateCount());
			        row.createCell(8).setCellValue(eems.get(i).getSearchCount());
			        row.createCell(9).setCellValue(eems.get(i).getTestingCount());
			        row.createCell(10).setCellValue(eems.get(i).getIdiCoe());
		        }
		        //公司类型
	        }else if(ee.getType() == EapEnterpriseService.TYPE_COMPANY){
	        	//填写列名
		        Row row = sheet.createRow((short)0);
		        row.createCell(0).setCellValue("姓名");
		        row.createCell(1).setCellValue("手机号");
		        row.createCell(2).setCellValue("学工号");
		        row.createCell(3).setCellValue("备注1");
		        row.createCell(4).setCellValue("备注2");
		        row.createCell(5).setCellValue("电话\\预约咨询");
		        row.createCell(6).setCellValue("提问");
		        row.createCell(7).setCellValue("发表心情");
		        row.createCell(8).setCellValue("检索词");
		        row.createCell(9).setCellValue("测试");
		        row.createCell(10).setCellValue("自杀系数");
		        
		      
		        for(short i=0; i< eems.size(); i++){
		        	row = sheet.createRow(i+1);
			        row.createCell(0).setCellValue(eems.get(i).getName());
			        row.createCell(1).setCellValue(eems.get(i).getPhoneNum());
			        row.createCell(2).setCellValue(eems.get(i).getNumber());
			        row.createCell(3).setCellValue(eems.get(i).getRemark1());
			        row.createCell(4).setCellValue(eems.get(i).getRemark2());
			        row.createCell(5).setCellValue(eems.get(i).getOrderCount());
			        row.createCell(6).setCellValue(eems.get(i).getMemberAdvisoryCount());
			        row.createCell(7).setCellValue(eems.get(i).getMemberUpdateCount());
			        row.createCell(8).setCellValue(eems.get(i).getSearchCount());
			        row.createCell(9).setCellValue(eems.get(i).getTestingCount());
			        row.createCell(10).setCellValue(eems.get(i).getIdiCoe());
		        }
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
	
	/**
	 * 
	 * @param eemVO
	 * @param ee 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<EapEmployeeDTO> getEapEmployeeList(EapEmployeeVO eemVO,Integer sortTag,EapEnterprise ee,Integer pageIndex, Integer pageSize){
			List<EapEmployeeDTO> eemDTOs = new ArrayList<EapEmployeeDTO>();
			
			
			List<EapEmployeeCustom> eems =  employeeService.getEapEmployeeList(eemVO,sortTag, pageIndex, pageSize);
			for(EapEmployeeCustom eem : eems){
				EapEmployeeDTO eemDTO = new EapEmployeeDTO();
				BeanUtils.copyProperties(eem, eemDTO);
				eemDTO.setEnterpriseType(ee.getType());
				if(eemDTO.getMid() != null){
					ServiceCustomerStatistics scs = serviceStatService.getOrCreateCustomerStat(eemDTO.getMid());
					eemDTO.setTotalAdvisoryDuration(scs.getTotalAdvisoryDuration());
					eemDTO.setTotalAdvisoryTimes(scs.getTotalAdvisoryTimes());
					
					//预约次数  (订单次数) 
					//自杀系数 后续完成---------------------------
					//eemDTO.setApptTimes(0);
					eemDTO.setIdiCoe(0.0);
					
				}else{
					//未注册用户数据都为0
					eemDTO.setMemberAdvisoryCount(0);
					eemDTO.setMemberUpdateCount(0);
					eemDTO.setTestingCount(0);
					eemDTO.setSearchCount(0);
					eemDTO.setApptTimes(0);
					eemDTO.setIdiCoe(0.0);
				}
				eemDTOs.add(eemDTO);
			}
		
		return eemDTOs;
	}
	
	public ResultEntity getTotalCount(EapEmployeeVO eemVO) throws Exception{
		//统计总数返回
		Integer totalMemberAdvisoryCount=0;
		Integer totalMemberUpdateCount=0;
		Integer totalSearchCount=0;
		Integer totalTestingCount=0;
		Integer totalApptCount=0;
		
		List<EapEmployeeCustom> eems =  employeeService.getEapEmployeeList(eemVO,null, null, null);
		//List<EapEmployeeDTO> eemDTOs=new ArrayList<EapEmployeeDTO>(); 
		for(EapEmployeeCustom eem : eems){
			
				totalMemberAdvisoryCount+=(eem.getMemberAdvisoryCount()==null?0:eem.getMemberAdvisoryCount());
				totalMemberUpdateCount+=(eem.getMemberUpdateCount()==null?0:eem.getMemberUpdateCount());
				totalSearchCount+=(eem.getSearchCount()==null?0:eem.getSearchCount());
				totalTestingCount+=(eem.getTestingCount()==null?0:eem.getTestingCount());
				//咨询统计（命名不一致  注意：原orderCount  统计apptCount）
				totalApptCount+=(eem.getOrderCount()==null?0:eem.getOrderCount());
		}
		
		ResultEntity result = new ResultEntity();
		
		result.put("totalMemberAdvisoryCount", totalMemberAdvisoryCount);
		result.put("totalMemberUpdateCount",totalMemberUpdateCount );
		result.put("totalSearchCount",totalSearchCount );
		result.put("totalTestingCount", totalTestingCount);
		result.put("totalApptCount", totalApptCount);
		
		return result;
		
	}
	
	
}
