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
import com.depression.model.Page;
import com.depression.model.UbSearchWords;
import com.depression.model.eap.dto.EapServiceOrderDTO;
import com.depression.model.eap.dto.EapTestingDTO;
import com.depression.model.eap.dto.EapTestingResultCustom;
import com.depression.model.eap.dto.EapUbSearchWordsDTO;
import com.depression.model.eap.dto.TestingResultCustom;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberService;
import com.depression.service.Permission;
import com.depression.service.UbSearchWordsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/EapUgcSearchWords")
public class EapUgcSearchWordsController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	EapEnterpriseService enterpriseService;
	@Autowired
	UbSearchWordsService ubSearchWordsService;
	@Autowired
	MemberService memberService;
	@Autowired
	EapEmployeeService eapEmployeeService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 获取企业用户搜索列表
	 * @param eeId 企业主键
 	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainSearchWordsList.json")
	@ResponseBody
	@Permission("3")
	Object obtainSearchWordsList(Long eeId,Long mid, Integer pageIndex, Integer pageSize){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex,pageSize,eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<EapUbSearchWordsDTO> swDTOs= new ArrayList<EapUbSearchWordsDTO>();
		Integer count=0;
		try{
			List<UbSearchWords> list=ubSearchWordsService.getUbSearchWordsInEap(eeId,mid,pageIndex,pageSize);
			swDTOs=getEapUbSearchWordsDTOList(list, eeId);
			count=ubSearchWordsService.countUbSearchWordsInEap(eeId,mid);
		}catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("list", swDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@Permission("3")
	@RequestMapping(method = RequestMethod.POST, value = "/exportSearchWords.json")
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
	        List<Long> ids=enterpriseService.getRegedEmployee(eeId);
	        List<UbSearchWords> list = ubSearchWordsService.getUbSearchWordsInEap(eeId,mid, null, null);
	        List<EapUbSearchWordsDTO> swDTOs=getEapUbSearchWordsDTOList(list, eeId);		
	        //设置列宽
	        sheet.setColumnWidth(1, 16*256);
	        sheet.setColumnWidth(2, 16*256);
	        sheet.setColumnWidth(3, 16*256);
	        
	      
		        //填写列名
		        Row row = sheet.createRow((short)0);
		        row.createCell(0).setCellValue("日期");
		        row.createCell(1).setCellValue("用户");
		        row.createCell(2).setCellValue("检索词");
		        
		        
		      
		        for(short i=0; i< list.size(); i++){
		        	row = sheet.createRow(i+1);
			        row.createCell(0).setCellValue(DateUtil.dateToStr(swDTOs.get(i).getCreateTime(),DateUtil.DATE_TIME_LINE));
			        row.createCell(1).setCellValue(swDTOs.get(i).getName());
			        row.createCell(2).setCellValue(list.get(i).getWords());
		        }
		        //公司类型
	        
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
	
	
	public List<EapUbSearchWordsDTO> getEapUbSearchWordsDTOList(List<UbSearchWords> list,Long eeId){
		List<EapUbSearchWordsDTO> swDTOs=new ArrayList<EapUbSearchWordsDTO>();
		for(UbSearchWords sw:list){
			EapUbSearchWordsDTO swDTO=new EapUbSearchWordsDTO();
			BeanUtils.copyProperties(sw, swDTO);
			Member m=memberService.selectMemberByMid(sw.getMid());
			EapEmployee e=eapEmployeeService.getEapEmployeeListByPhoneNumAndEeId(m.getMobilePhone(), eeId);
			swDTO.setName(e.getName());
			swDTOs.add(swDTO);
		}
		return swDTOs;
	}
	
	
	
	
}
