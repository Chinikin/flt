package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.LicenseType;
import com.depression.model.PsychoInfo;
import com.depression.model.api.dto.ApiLicenseTypeDTO;
import com.depression.model.api.vo.ApiPsychoInfoVO;
import com.depression.service.PsychoInfoService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/PsychoInfo")
public class PsychoInfoController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	PsychoInfoService psychoInfoService;
	
	/**
	 * 获取执照类型列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getLicenseTypeList.json")
	@ResponseBody
	public Object getLicenseTypeList()
	{
		ResultEntity result = new ResultEntity();
		
		List<LicenseType> licenseTypes;
		try{
			licenseTypes = psychoInfoService.getAllLicenseTypes();
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<ApiLicenseTypeDTO> apiLicenseTypeDTOs = new ArrayList<ApiLicenseTypeDTO>();
		for(LicenseType lt : licenseTypes)
		{
			ApiLicenseTypeDTO apiLicenseTypeDTO = new ApiLicenseTypeDTO();
			BeanUtils.copyProperties(lt, apiLicenseTypeDTO);
			apiLicenseTypeDTOs.add(apiLicenseTypeDTO);
		}
		
		result.put("licenseTypes", apiLicenseTypeDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 新建专家入住信息
	 * @param apiPsychoInfoVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/newPsychoInfo.json")
	@ResponseBody
	public Object newPsychoInfo(ApiPsychoInfoVO apiPsychoInfoVO)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				apiPsychoInfoVO,
				apiPsychoInfoVO.getName(), 
				apiPsychoInfoVO.getSex(),
				apiPsychoInfoVO.getMobilePhone(),
				apiPsychoInfoVO.getWxAccount(),
				apiPsychoInfoVO.getLocation(),
				apiPsychoInfoVO.getLtid(),
				apiPsychoInfoVO.getTitle(),
				apiPsychoInfoVO.getPrimaryDomains(),
				apiPsychoInfoVO.getPhotoCandidPreviewRel(),
				apiPsychoInfoVO.getPhotoCandidRel(),
				apiPsychoInfoVO.getPhotoCertificationPreviewRel(),
				apiPsychoInfoVO.getPhotoCertificationRel(),
				apiPsychoInfoVO.getPhotoIdentityCardPreviewRel(),
				apiPsychoInfoVO.getPhotoIdentityCardRel(),
				apiPsychoInfoVO.getBrief()
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		//避免重复数据
		Integer existence = psychoInfoService.checkValidPsychoInfo(apiPsychoInfoVO.getMobilePhone());
		if(existence > 0)
		{
			result.setCode(ErrorCode.ERROR_MOBILEPHONE_EXISTED.getCode());
			result.setMsg(ErrorCode.ERROR_MOBILEPHONE_EXISTED.getMessage());
			return result;
		}
		
		//填充信息
		PsychoInfo psychoInfo =  new PsychoInfo();
		BeanUtils.copyProperties(apiPsychoInfoVO, psychoInfo);
		//保存擅长领域 json数组
		psychoInfo.setPrimaryDomain(apiPsychoInfoVO.getPrimaryDomains());
		
		try{
			List<String> domains = JSONArray.parseArray(apiPsychoInfoVO.getPrimaryDomains(), String.class);
			psychoInfoService.newPsychoInfo(psychoInfo, domains);
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
	 * 根据手机号确认是否存在有效的入住信息(未审核或者审核通过)
	 * @param mobilePhone
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/checkExistence.json")
	@ResponseBody
	public Object checkExistence(String mobilePhone)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				mobilePhone
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		Integer existence;
		try{
			existence = psychoInfoService.checkValidPsychoInfo(mobilePhone);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("existence", existence);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
}
