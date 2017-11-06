package com.depression.controller.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.EapEmployee;
import com.depression.model.EapEnterprise;
import com.depression.model.Member;
import com.depression.model.PsychoGroup;
import com.depression.model.PsychoGroupMember;
import com.depression.model.ServiceOrder;
import com.depression.model.ServiceOrderEvaluation;
import com.depression.model.ServicePsychoStatistics;
import com.depression.model.web.dto.WebPGServiceOrderDTO;
import com.depression.model.web.dto.WebPsychoGroupDTO;
import com.depression.model.web.dto.WebPsychoGroupMemberDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberService;
import com.depression.service.PsychoGroupService;
import com.depression.service.ServiceOrderEvaluationService;
import com.depression.service.ServiceOrderService;
import com.depression.service.ServiceStatisticsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/PsychoGroup")
public class PsychoGroupController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
    PsychoGroupService psychoGroupService;
	@Autowired
	ServiceOrderService serviceOrderService;
	@Autowired
	ServiceStatisticsService serviceStatService;
	@Autowired
	MemberService memberService;
	@Autowired
	ServiceOrderEvaluationService evaluationService;
	@Autowired
	EapEnterpriseService eapEnterpriseService;
	
	/**
	 * 获取咨询师分组列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPsychoGroupList.json")
	@ResponseBody
	Object obtainPsychoGroupList()
	{
		ResultEntity result = new ResultEntity();
		
		List<WebPsychoGroupDTO> psychoGroupDTOs = new ArrayList<WebPsychoGroupDTO>();
		
		try{
			List<PsychoGroup> psychoGroups = psychoGroupService.obtainPsychoGroupList();
			
			for(PsychoGroup pg : psychoGroups)
			{
				WebPsychoGroupDTO psychoGroupDTO = new WebPsychoGroupDTO();
				BeanUtils.copyProperties(pg, psychoGroupDTO);
				psychoGroupDTOs.add(psychoGroupDTO);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("psychoGroups", psychoGroupDTOs);
		result.put("count", psychoGroupDTOs.size());
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;		
	}
	
	/**
	 * 添加新的咨询师组
	 * @param groupName 组名
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addPsychoGroup.json")
	@ResponseBody
	Object addPsychoGroup(String groupName)
	{
		ResultEntity result = new ResultEntity();
				
		try{
			PsychoGroup psychoGroup = new PsychoGroup();
			psychoGroup.setGroupName(groupName);

			psychoGroupService.addPsychoGroup(psychoGroup);
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
	 * 移除咨询师组
	 * @param pgId 分组id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/removePsychoGroup.json")
	@ResponseBody
	Object removePsychoGroup(Long pgId)
	{
		ResultEntity result = new ResultEntity();
				
		try{
			
			List<EapEnterprise> ees = eapEnterpriseService.obtainEnterpriseBypgId(pgId);
			if(ees.size() > 0)
			{//咨询师组为企业服务
				String enterName = "[";
				for(EapEnterprise ee : ees)
				{
					enterName += ee.getName();
					enterName += " ";
				}
				enterName = enterName.trim() + "]";

				result.setCode(ErrorCode.ERROR_PSYCHO_GROUP_IN_USE.getCode());
				result.setMsg("咨询师组正在为下列企业服务 ： " + enterName);
				return result;
			}
			
			psychoGroupService.removePsychoGroup(pgId);
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
	 * 添加咨询师到分组
	 * @param pgId  分组id
	 * @param idsVO   咨询师id列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addPsychosToGroup.json")
	@ResponseBody
	Object addPsychosToGroup(Long pgId, WebIdsVO idsVO)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pgId, idsVO)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			for(Long id : idsVO.getIds())
			{
				psychoGroupService.addMemberToGroup(pgId, id);
			}
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
	 * 获取分组内咨询师列表
	 * @param pgId 分组id
	 * @param pageIndex 页码
	 * @param pageSize 页大小
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPsychoGroupMemberList.json")
	@ResponseBody
	Object obtainPsychoGroupMemberList(Long pgId, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pgId, pageIndex, pageSize)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<PsychoGroupMember> pgms;
		Integer count;
		try{
			pgms = psychoGroupService.getPsychoGroupMemberList(pgId, pageIndex, pageSize);
			count = psychoGroupService.countPsychoGroupMember(pgId);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<WebPsychoGroupMemberDTO> pgmDTOs = new ArrayList<WebPsychoGroupMemberDTO>();
		for(PsychoGroupMember pgm : pgms)
		{
			WebPsychoGroupMemberDTO pgmDTO = new WebPsychoGroupMemberDTO();
			BeanUtils.copyProperties(pgm, pgmDTO);
			
			//从咨询师服务统计中获取信息
			ServicePsychoStatistics sps = serviceStatService.getOrCreatePsychoStat(pgm.getMid());
			pgmDTO.setEapAdvisoryDuration(sps.getEapAdvisoryDuration());
			pgmDTO.setEapAdvisoryTimes(sps.getEapAdvisoryTimes());
			pgmDTO.setTotalAdvisoryDuration(sps.getTotalAdvisoryDuration());
			pgmDTO.setTotalAdvisoryTimes(sps.getTotalAdvisoryTimes());
			pgmDTO.setEapScore(sps.getEapScore());
			pgmDTO.setCommonScore(sps.getCommonScore());
			Member member = memberService.selectMemberByMid(pgm.getMid());
			if(member!=null)
			{
				pgmDTO.setNickname(member.getNickname());
				pgmDTO.setPhoneNum(member.getMobilePhone());
			}
			pgmDTOs.add(pgmDTO);
		}
		
		result.put("pgmDTOs", pgmDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 导出组内咨询师列表
	 * @param pgId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportPsychoGroupMemberList.json")
	void exportPsychoGroupMemberList(HttpServletRequest request, HttpServletResponse response, Long pgId)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pgId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return;
		}
		
		PsychoGroup pg = psychoGroupService.obtainPsychoGroup(pgId);
		if(pg == null)
		{
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return;
		}
		
		response.setContentType("application/vnd.ms-excel");  
		String codedFileName;
		try
		{
			codedFileName = java.net.URLEncoder.encode(pg.getGroupName() + "组分成员列表", "UTF-8");
		} catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
			codedFileName = "PsychoGroupMember";
		} 
		
		response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");  
				
		OutputStream  fOut = null;
		try{
			Workbook wb = new XSSFWorkbook();
	        CreationHelper createHelper = wb.getCreationHelper();
	        Sheet sheet = wb.createSheet("Member");
	        //设置列宽
	        sheet.setColumnWidth(1, 20*256);
	        sheet.setColumnWidth(2, 16*256);
	        sheet.setColumnWidth(3, 24*256);
	        sheet.setColumnWidth(4, 16*256);
	        sheet.setColumnWidth(5, 24*256);
	        sheet.setColumnWidth(6, 16*256);
	        sheet.setColumnWidth(7, 16*256);
	        sheet.setColumnWidth(8, 16*256);
	        sheet.setColumnWidth(9, 16*256);
	        //填写列名
	        Row row = sheet.createRow((short)0);
	        row.createCell(0).setCellValue("姓名");
	        row.createCell(1).setCellValue("手机号");
	        row.createCell(2).setCellValue("进组时间");
	        row.createCell(3).setCellValue("EAP咨询时长（分钟）");
	        row.createCell(4).setCellValue("EAP已接单数");
	        row.createCell(5).setCellValue("全部咨询时长（分钟）");
	        row.createCell(6).setCellValue("全部已接单数");
	        row.createCell(7).setCellValue("EAP用户平均打分");
	        row.createCell(8).setCellValue("普通用户平均打分");
	        row.createCell(9).setCellValue("启用状态");
	        //填写员工信息
	        List<PsychoGroupMember> pgms = psychoGroupService.getPsychoGroupMemberList(pgId);;
	        for(short i=0; i< pgms.size(); i++)
	        {
				//从咨询师服务统计中获取信息
				ServicePsychoStatistics sps = serviceStatService.getOrCreatePsychoStat(pgms.get(i).getMid());
				Member member = memberService.selectMemberByMid(pgms.get(i).getMid());
	        	
	        	row = sheet.createRow(i+1);
		        row.createCell(0).setCellValue(member.getNickname());
		        row.createCell(1).setCellValue(member.getMobilePhone());
		        
		        CellStyle cellStyle = wb.createCellStyle();
		        cellStyle.setDataFormat(
		            createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
		        Cell cell = row.createCell(2);
		        cell.setCellStyle(cellStyle);
		        cell.setCellValue(pgms.get(i).getCreateTime());
		        
		        row.createCell(3).setCellValue(sps.getEapAdvisoryDuration()/60);
		        row.createCell(4).setCellValue(sps.getEapAdvisoryTimes());
		        row.createCell(5).setCellValue(sps.getTotalAdvisoryDuration()/60);
		        row.createCell(6).setCellValue(sps.getTotalAdvisoryTimes());
		        
		        NumberFormat nf = NumberFormat.getNumberInstance();
		        // 保留两位小数
		        nf.setMaximumFractionDigits(2); 
		        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
		        nf.setRoundingMode(RoundingMode.UP);
		        
			    row.createCell(7).setCellValue(nf.format(sps.getEapScore().floatValue()));
				row.createCell(8).setCellValue(nf.format(sps.getCommonScore().floatValue()));
				row.createCell(9).setCellValue(pgms.get(i).getIsEnable()==0?"启用":"禁用");
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
	 * 从分组中移除咨询师
	 * @param idsVO 分组成员id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/removePsychosFromGroup.json")
	@ResponseBody
	Object removePsychosFromGroup(WebIdsVO idsVO)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(idsVO)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			psychoGroupService.removeMembersFromGroup(idsVO.getIds());
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
	 * 启用/禁用组内成员
	 * @param idsVO 组内成员id（pgmId)列表
	 * @param isEnable 0 启用 1禁用
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enablePsychoGroupMembers.json")
	@ResponseBody
	Object enablePsychoGroupMembers(WebIdsVO idsVO, Byte isEnable)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(idsVO, isEnable)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			psychoGroupService.enablePsychoGroupMembers(idsVO.getIds(), isEnable);
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
	 * （未完成，等订单设计确认后完善）
	 * 获取组内某咨询师流水
	 * @param pid  咨询师id
	 * @param pageIndex  页码
	 * @param pageSize  页大小
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPsychoOrderList.json")
	@ResponseBody
	Object obtainPsychoOrderList(Long pid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pid, pageIndex, pageSize)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<WebPGServiceOrderDTO> soDTOs = new ArrayList<WebPGServiceOrderDTO>();
		Integer count;
		try{
			ServiceOrder so = new ServiceOrder();
			so.setServiceProviderId(pid);
			so.setPageIndex(pageIndex);
			so.setPageSize(pageSize);
			
			List<ServiceOrder> sos =  serviceOrderService.selectSelectiveByPage(so);
			count = serviceOrderService.selectCount(so);
			
			for(ServiceOrder serviceOrder : sos)
			{
				WebPGServiceOrderDTO soDTO = new WebPGServiceOrderDTO();
				BeanUtils.copyProperties(serviceOrder, soDTO);
				
				soDTO.setCustomerPhone(memberService.obtainMobilePhoneByMid(serviceOrder.getMid()));
				Member m = memberService.selectMemberByMid(serviceOrder.getServiceProviderId());
				soDTO.setProviderPhone(m.getMobilePhone());
				soDTO.setProviderName(m.getNickname());
				
				ServiceOrderEvaluation soe = evaluationService.obtainEvaluationBySoid(serviceOrder.getSoid());
				if(soe != null)
				{
					soDTO.setEvaluationScore(soe.getScore());
				}
				soDTOs.add(soDTO);
			}
		} catch (Exception e)
		{
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
	
	/**
	 * 导出咨询师订单流水
	 * @param request
	 * @param response
	 * @param eeId
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/exportPsychoOrderList.json")
	void exportPsychoOrderList(HttpServletRequest request, HttpServletResponse response, Long pid)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pid
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return;
		}
		
		Member provider = memberService.selectMemberByMid(pid);
		if(provider == null)
		{
			result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
			result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
			return;
		}
		
		response.setContentType("application/vnd.ms-excel");  
		String codedFileName;
		try
		{
			codedFileName = java.net.URLEncoder.encode(provider.getNickname() + "订单流水", "UTF-8");
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
	        sheet.setColumnWidth(1, 20*256);
	        sheet.setColumnWidth(2, 16*256);
	        sheet.setColumnWidth(3, 16*256);
	        sheet.setColumnWidth(4, 16*256);
	        sheet.setColumnWidth(5, 28*256);
	        sheet.setColumnWidth(6, 16*256);
	        sheet.setColumnWidth(7, 16*256);
	        sheet.setColumnWidth(8, 16*256);
	        //填写列名
	        Row row = sheet.createRow((short)0);
	        row.createCell(0).setCellValue("专家姓名");
	        row.createCell(1).setCellValue("订单编号");
	        row.createCell(2).setCellValue("求助者类型");
	        row.createCell(3).setCellValue("求助者电话");
	        row.createCell(4).setCellValue("专家电话");
	        row.createCell(5).setCellValue("拨打时间");
	        row.createCell(6).setCellValue("拨打时长（分）");
	        row.createCell(7).setCellValue("用户评分");
	        row.createCell(8).setCellValue("求助者实付");
	        //填写员工信息
	        List<ServiceOrder> sos =  serviceOrderService.obtainServiceOrderByPid(pid);
	        for(short i=0; i< sos.size(); i++)
	        {
	        	row = sheet.createRow(i+1);
		        row.createCell(0).setCellValue(provider.getNickname());
		        row.createCell(1).setCellValue(sos.get(i).getNo());
		        row.createCell(2).setCellValue(sos.get(i).getOrderType()==0?"普通用户":"EAP用户");
		        row.createCell(3).setCellValue(memberService.obtainMobilePhoneByMid(sos.get(i).getMid()));
		        row.createCell(4).setCellValue(provider.getMobilePhone());
		        CellStyle cellStyle = wb.createCellStyle();
		        cellStyle.setDataFormat(
		            createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
		        Cell cell = row.createCell(5);
		        cell.setCellStyle(cellStyle);
		        cell.setCellValue(sos.get(i).getServiceBeginTime());
		        row.createCell(6).setCellValue(sos.get(i).getPracticalDuration()/60);
				ServiceOrderEvaluation soe = evaluationService.obtainEvaluationBySoid(sos.get(i).getSoid());
				if(soe != null)
				{
			        row.createCell(7).setCellValue(soe.getScore());
				}
				row.createCell(8).setCellValue(sos.get(i).getCashAmount().intValue());


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
