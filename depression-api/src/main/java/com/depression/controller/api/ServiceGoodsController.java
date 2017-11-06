package com.depression.controller.api;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceGoodsPriceScope;
import com.depression.model.api.dto.ApiServiceGoodsDTO;
import com.depression.model.api.dto.ApiServiceGoodsPriceScopeDTO;
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
	 * 获取实时咨询服务商品信息
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getImmAdvisoryServiceGoods.json")
	@ResponseBody
	public Object getImmAdvisoryServiceGoods(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ServiceGoods serviceGoods;
		try{
			serviceGoods = serviceGoodsService.selectImmVoiceByMid(mid);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		ApiServiceGoodsDTO apiServiceGoodsDTO = new ApiServiceGoodsDTO();
		BeanUtils.copyProperties(serviceGoods, apiServiceGoodsDTO);
		
		result.put("serviceGoods", apiServiceGoodsDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取商品价格范围
	 * @param sgid 商品id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainServiceGoodsPriceScope.json")
	@ResponseBody
	public Object obtainServiceGoodsPriceScope(Long sgid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(sgid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiServiceGoodsPriceScopeDTO serviceGoodsPriceScopeDTO = new ApiServiceGoodsPriceScopeDTO();
		
		try{
			ServiceGoodsPriceScope serviceGoodsPriceScope = serviceGoodsService.sgpsObtainPriceScope(sgid);
			if(serviceGoodsPriceScope != null)
			{
				BeanUtils.copyProperties(serviceGoodsPriceScope, serviceGoodsPriceScopeDTO);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("serviceGoodsPriceScopeDTO", serviceGoodsPriceScopeDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 修改服务商品价格
	 * @param sgid
	 * @param price
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyServiceGoodsPrice.json")
	@ResponseBody
	public Object modifyServiceGoodsPrice(Long sgid, BigDecimal price)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(sgid, price))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ServiceGoodsPriceScope priceScope = serviceGoodsService.sgpsObtainPriceScope(sgid);
		if(priceScope != null)
		{
			if(price.intValue() < priceScope.getFloor() || price.intValue() > priceScope.getCeiling())
			{
				result.setCode(ErrorCode.ERROR_PARAM_INVALID.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INVALID.getMessage());
				return result;
			}
		}
		
		try{
			serviceGoodsService.updatePrice(sgid, price);
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
	 * 修改服务商品时长
	 * @param sgid
	 * @param duration
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyServiceGoodsDuration.json")
	@ResponseBody
	public Object modifyServiceGoodsDuration(Long sgid, Integer duration)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(sgid, duration))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			serviceGoodsService.updateDuration(sgid, duration);
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
	 * 修改服务商品时长和价格
	 * @param sgid
	 * @param duration
	 * @param price
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/modifyServiceGoodsDurationAndPrice.json")
	@ResponseBody
	public Object modifyServiceGoodsDurationAndPrice(Long sgid, Integer duration, BigDecimal price)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(sgid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ServiceGoodsPriceScope priceScope = serviceGoodsService.sgpsObtainPriceScope(sgid);
		if(priceScope != null)
		{
			if(price.intValue() < priceScope.getFloor() || price.intValue() > priceScope.getCeiling())
			{
				result.setCode(ErrorCode.ERROR_PARAM_INVALID.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INVALID.getMessage());
				return result;
			}
		}
		
		try{
			serviceGoodsService.updatePrice(sgid, price);
			serviceGoodsService.updateDuration(sgid, duration);
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
