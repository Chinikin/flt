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
import com.depression.model.UbSearchWords;
import com.depression.model.eap.dto.EapTestingDTO;
import com.depression.model.eap.dto.EapTestingResultCustom;
import com.depression.model.eap.dto.EapUbSearchWordsDTO;
import com.depression.model.eap.dto.TestingResultCustom;
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
import com.depression.service.TestingResultService;
import com.depression.service.TestingService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/EapUgcTesting")
public class EapUgcTestingController
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
	@Autowired
	private TestingResultService testingResultService;
	@Autowired
	private EapEmployeeService eapEmployeeService;
	
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
	@RequestMapping(method = RequestMethod.POST, value = "/obtainTestingList.json")
	@ResponseBody
	@Permission("3")
	Object obtainTestingList(Long eeId,Long mid, Integer pageIndex, Integer pageSize){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex,pageSize,eeId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<EapTestingDTO>  tDTOs= new ArrayList<EapTestingDTO>();
		Integer count=0;
		try{
			tDTOs=getTestingList(eeId,mid, pageIndex, pageSize);
			count=getTestingCount(eeId,mid);
		}catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("tDTOs", tDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	@Permission("3")
	@RequestMapping(method = RequestMethod.POST, value = "/exportTesting.json")
	void exportTesting(HttpServletRequest request, HttpServletResponse response, Long eeId,Long mid){
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
			codedFileName = java.net.URLEncoder.encode(ee.getName() + "测试列表", "UTF-8");
		} catch (UnsupportedEncodingException e1){
			codedFileName = "testing";
		} 
		
		response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");  
				
		OutputStream  fOut = null;
		try{
			Workbook wb = new XSSFWorkbook();
	        CreationHelper createHelper = wb.getCreationHelper();
	        Sheet sheet = wb.createSheet("EAP");
	        
	        //获取信息
	        List<EapTestingDTO> list = getTestingList(eeId,mid, null, null);
	        //设置列宽
	        sheet.setColumnWidth(1, 16*256);
	        sheet.setColumnWidth(2, 16*256);
	        sheet.setColumnWidth(3, 16*256);
	        sheet.setColumnWidth(4, 16*256);
	        sheet.setColumnWidth(5, 16*256);
	        
	      
		        //填写列名
		        Row row = sheet.createRow((short)0);
		        row.createCell(0).setCellValue("量表");
		        row.createCell(1).setCellValue("用户");
		        row.createCell(2).setCellValue("日期");
		        row.createCell(3).setCellValue("得分");
		        row.createCell(4).setCellValue("结论");
		        
		        
		      
		        for(short i=0; i< list.size(); i++){
		        	row = sheet.createRow(i+1);
		        	row.createCell(0).setCellValue(list.get(i).getTitle());
		        	row.createCell(1).setCellValue(list.get(i).getName());
			        row.createCell(2).setCellValue(DateUtil.dateToStr(list.get(i).getTestTime(),DateUtil.DATE_TIME_LINE));
			        row.createCell(3).setCellValue(list.get(i).getResult());
			        row.createCell(4).setCellValue(list.get(i).getLevel());
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
					log.error(ErrorCode.ERROR_GENERATE_FILE_FAILED.getMessage(), e);
				}
			}
		}	
	}
	
	
	
	public List<EapTestingDTO> getTestingList(Long eeId,Long mid, Integer pageIndex, Integer pageSize) throws Exception{
		
		List<EapTestingDTO>  tDTOs= new ArrayList<EapTestingDTO>();
		//获取当前企业下的所有用户测试的条目
		List<EapTestingResultCustom> trcs=enterpriseService.getEapTestingResultCustomByEeId(eeId,mid,pageIndex,pageSize);
		
		if(trcs != null){
			for(EapTestingResultCustom trc:trcs){
				EapTestingDTO tDTO=new EapTestingDTO();
				tDTO.setTitle(testingService.getTestingByPrimaryKey(trc.getTestingId()).getTitle());
				//设置名字
				Member m=memberService.selectMemberByMid(trc.getMid());
				EapEmployee e=eapEmployeeService.getEapEmployeeListByPhoneNumAndEeId(m.getMobilePhone(),eeId);
				
				//没有该用户就不返回该测试数据
				if(e != null){
					tDTO.setName(e.getName());
					//设置测试结果
					TestingResultCustom t=testingResultService.getTestingResultByMidAndTestingId(trc.getTestingId(), trc.getMid(), trc.getCalc());
					tDTO.setLevel(t.getLevel());
					tDTO.setCalc(trc.getCalc());
					tDTO.setTestTime(trc.getTestTime());
					if(trc.getCalc() == 0){
						tDTO.setResult(t.getScore()+"");
					}
					if(trc.getCalc() == 1){
						tDTO.setResult(t.getResultTag());
					}
					tDTOs.add(tDTO);
				}
			}
		}
		return tDTOs;
	}
	
	
	
	public Integer getTestingCount(Long eeId,Long mid) throws Exception{
		Integer count =0;
		//获取当前企业下的所有用户测试的条目
		List<EapTestingResultCustom> trcs=enterpriseService.getEapTestingResultCustomByEeId(eeId,mid,null,null);
		if(trcs != null && trcs.size() != 0){
		for(EapTestingResultCustom trc:trcs){
			EapTestingDTO tDTO=new EapTestingDTO();
			tDTO.setTitle(testingService.getTestingByPrimaryKey(trc.getTestingId()).getTitle());
			//设置名字
			Member m=memberService.selectMemberByMid(trc.getMid());
			EapEmployee e=eapEmployeeService.getEapEmployeeListByPhoneNumAndEeId(m.getMobilePhone(),eeId);
			//没有该用户就不增加测试数据
			if(e != null){
				count++;
			}
		 }
		}
		return count;
	}
	
	
	
	
}
