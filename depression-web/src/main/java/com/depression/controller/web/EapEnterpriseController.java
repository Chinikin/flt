package com.depression.controller.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.EapEnterprise;
import com.depression.model.eap.vo.EapUserVO;
import com.depression.model.web.dto.WebEapEnterpriseDTO;
import com.depression.model.web.vo.WebEapEnterpriseVO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.EapUserService;
import com.depression.utils.Configuration;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/EapEnterprise")
public class EapEnterpriseController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	EapEnterpriseService eapEnterpriseService;
	@Autowired
	EapEmployeeService employeeService;
	@Autowired
	EapUserService eapUserService;
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 新建Eap客户企业
	 * @param enterpriseVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/newEnterprise.json")
	@ResponseBody
	Object newEnterprise(WebEapEnterpriseVO enterpriseVO)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(
				enterpriseVO, 
				enterpriseVO.getName(),
				enterpriseVO.getContacts(),
				enterpriseVO.getContactsPhoneNum(),
				enterpriseVO.getLogoRel(),
				enterpriseVO.getLogoPreviewRel(),
				enterpriseVO.getPurchasedCashAmount(),
				enterpriseVO.getPurchasedOrderAmount(),
				enterpriseVO.getOrderSingleLimit(),
				enterpriseVO.getServiceStartDate(),
				enterpriseVO.getServiceEndDate(),
				enterpriseVO.getPgId()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			EapEnterprise eapEnterprise = new EapEnterprise();
			BeanUtils.copyProperties(enterpriseVO, eapEnterprise);
			
			eapEnterpriseService.newEnterprise(eapEnterprise);
			//新增完成后返回主键
			//设置当前用户为EAP服务超级管理员（企业EAP服务不设置）
			if(eapEnterprise.getEeId()!=null&&eapEnterprise.getType()==0){
				EapUserVO eapUser = new EapUserVO();				
				eapUser.setEeId(eapEnterprise.getEeId());
				eapUser.setUserType((byte) 99);
				eapUser.setMobilePhone(eapEnterprise.getContactsPhoneNum());
				eapUser.setUsername(eapEnterprise.getContacts());
				eapUser.setEapPassword(MD5Util.encode(Constant.INITIALPASSWORD));
				eapUser.setSpareInt(0);
				eapUser.setShowName(eapEnterprise.getContacts());
				eapUser.setRemark("超级管理员");
				// 插入用户信息
				eapUserService.createUser(eapUser);
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
	
	public int daysBetween(Date end, Date begin)  
    {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(begin);  
        Long timeB = cal.getTimeInMillis();               
        cal.setTime(end);  
        Long timeE = cal.getTimeInMillis();       
        Long between_days=(timeE-timeB)/(1000*3600*24);  
        
        return between_days.intValue();         
    }  
	
	void fillEapEnterpriseDTO(EapEnterprise ee, WebEapEnterpriseDTO eeDTO)
	{
		BeanUtils.copyProperties(ee, eeDTO);
		
		//设置log路径
		eeDTO.setLogo(ee.getLogoRel());
		eeDTO.setLogoPreview(ee.getLogoPreviewRel());
		
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
			eeDTO.setRemainingDays(0);
		}else
		{// 2 进行中
			//进行中的天数显示+1，将当天计入
			eeDTO.setRemainingDays(daysBetween(ee.getServiceEndDate(), current) + 1);
		}
		
		//统计员工数量
		eeDTO.setEmployeeAmount(employeeService.countEmployeeInEmterprise(ee.getEeId()));
		
	}
	
	/**
	 * 获取Eap企业列表
	 * @param words 关键词，匹配名称和联系人
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEnterpriseList.json")
	@ResponseBody
	Object obtainEnterpriseList(String words, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex, pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<WebEapEnterpriseDTO> eeDTOs = new ArrayList<WebEapEnterpriseDTO>();
		Integer count;
		try{
			List<EapEnterprise> ees = eapEnterpriseService.searchEnterprise(words, pageIndex, pageSize);
			count = eapEnterpriseService.countSearchEnterprise(words);
			
			for(EapEnterprise ee : ees)
			{
				WebEapEnterpriseDTO eeDTO = new WebEapEnterpriseDTO();
				fillEapEnterpriseDTO(ee, eeDTO);
				eeDTOs.add(eeDTO);
			}
			

		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("eeDTOs", eeDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 根据eeId获取企业信息
	 * @param eeId 企业id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainEnterprise.json")
	@ResponseBody
	Object obtainEnterprise(Long eeId)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(eeId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		WebEapEnterpriseDTO eeDTO = new WebEapEnterpriseDTO();

		try{
			EapEnterprise ee = eapEnterpriseService.obtainEnterpriseByKey(eeId);
			if(ee != null)
			{
				fillEapEnterpriseDTO(ee, eeDTO);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("eeDTO", eeDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}	
	
	/**
	 * 更新EAP企业资料
	 * @param enterpriseVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateEnterprise.json")
	@ResponseBody
	Object updateEnterprise(WebEapEnterpriseVO enterpriseVO)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(
				enterpriseVO, 
				enterpriseVO.getEeId(),
				enterpriseVO.getName(),
				enterpriseVO.getContacts(),
				enterpriseVO.getContactsPhoneNum(),
				enterpriseVO.getLogoRel(),
				enterpriseVO.getLogoPreviewRel(),
				enterpriseVO.getPurchasedCashAmount(),
				enterpriseVO.getPurchasedOrderAmount(),
				enterpriseVO.getOrderSingleLimit(),
				enterpriseVO.getServiceStartDate(),
				enterpriseVO.getServiceEndDate(),
				enterpriseVO.getPgId()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			EapEnterprise eapEnterprise = new EapEnterprise();
			BeanUtils.copyProperties(enterpriseVO, eapEnterprise);
			eapEnterpriseService.updateEnterprise(eapEnterprise);
			if(eapEnterprise.getEeId()!=null&&eapEnterprise.getType()==0){
				EapUserVO eapUser = new EapUserVO();				
				eapUser.setEeId(eapEnterprise.getEeId());
				eapUser.setMobilePhone(eapEnterprise.getContactsPhoneNum());
				eapUser.setUsername(eapEnterprise.getContacts());
				eapUser.setShowName(eapEnterprise.getContacts());
				eapUser.setUserType((byte) 99);
				// 插入用户信息
				eapUserService.updateByEeId(eapUser);
			}
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
	 * 启用/禁用EAP企业
	 * @param idsVO 企业id（eeId）列表
	 * @param isEnable 0 启用 1禁用
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enableEnterprise.json")
	@ResponseBody
	Object enableEnterprise(WebIdsVO idsVO, Byte isEnable)
	{
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(idsVO, isEnable)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			eapEnterpriseService.enableEnterprise(idsVO.getIds(), isEnable);
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
}
