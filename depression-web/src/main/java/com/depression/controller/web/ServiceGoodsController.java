package com.depression.controller.web;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.ServiceGoodsPriceScope;
import com.depression.model.web.dto.WebServiceGoodsPriceScopeDTO;
import com.depression.service.ServiceGoodsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/ServiceGoods")
public class ServiceGoodsController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	ServiceGoodsService serviceGoodsService;
	
	/**
	 * 获取商品价格范围
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPriceScope.json")
	@ResponseBody
	public Object obtainPriceScope()
	{
		ResultEntity result = new ResultEntity();
		
		WebServiceGoodsPriceScopeDTO voiceListenPriceScopeDTO = new WebServiceGoodsPriceScopeDTO();
		WebServiceGoodsPriceScopeDTO voiceAdvisoryPriceScopeDTO = new WebServiceGoodsPriceScopeDTO();
		try{
			ServiceGoodsPriceScope voiceListenPriceScope = serviceGoodsService.sgpsObtainVoiceListenScope();
			BeanUtils.copyProperties(voiceListenPriceScope, voiceListenPriceScopeDTO);
			
			ServiceGoodsPriceScope voiceAdvisoryPriceScope = serviceGoodsService.sgpsObtainVoiceAdvisoryScope();
			BeanUtils.copyProperties(voiceAdvisoryPriceScope, voiceAdvisoryPriceScopeDTO);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("voiceListenPriceScopeDTO", voiceListenPriceScopeDTO);
		result.put("voiceAdvisoryPriceScopeDTO", voiceAdvisoryPriceScopeDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 修改商品价格范围
	 * @param sgpsId
	 * @param floor
	 * @param ceiling
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyPriceScope.json")
	@ResponseBody
	public Object modifyPriceScope(Long sgpsId, Integer floor, Integer ceiling)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(sgpsId, floor, ceiling))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			serviceGoodsService.sgpsModifyPriceScope(sgpsId, floor, ceiling);
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
