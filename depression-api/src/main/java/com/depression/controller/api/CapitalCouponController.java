package com.depression.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalDiscountBestowal;
import com.depression.model.api.dto.ApiCapitalCouponEntityDTO;
import com.depression.service.CapitalCouponService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/CapitalCoupon")
public class CapitalCouponController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	CapitalCouponService capitalCouponService;
	@Autowired
	CapitalPersonalAssetsService capitalPersonalAssetsService;
	
	/**
	 * 根据mid获取用户优惠券列表
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainCapitalCoupon.json")
	@ResponseBody
	public Object obtainCapitalCoupon(Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiCapitalCouponEntityDTO> allAcces = new ArrayList<ApiCapitalCouponEntityDTO>();

		try{
			//初始化用户账户，避免账户没有生成
			capitalPersonalAssetsService.selectByMid(mid);
			
			List<CapitalDiscountBestowal> usableCdbs = capitalCouponService.getUsableBestowal(mid);
			for(CapitalDiscountBestowal cdb : usableCdbs)
			{
				if(cdb.getValidityEndTime().before(new Date()))
				{//更新过期优惠券状态
					capitalCouponService.expireCoupon(cdb.getDbid());
				}
			}
			
			List<CapitalCouponEntity> allCces = capitalCouponService.getAllCouponEntity(mid, pageIndex, pageSize);
			for(CapitalCouponEntity cce : allCces)
			{
				ApiCapitalCouponEntityDTO acces = new ApiCapitalCouponEntityDTO();
				BeanUtils.copyProperties(cce, acces);
				allAcces.add(acces);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.put("allCoupons", allAcces);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 根据mid获取用户有效优惠券数量
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/countUsableCapitalCoupon.json")
	@ResponseBody
	public Object countUsableCapitalCoupon(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		Integer count = 0;
		
		try{
			List<CapitalDiscountBestowal> usableCdbs = capitalCouponService.getUsableBestowal(mid);
			for(CapitalDiscountBestowal cdb : usableCdbs)
			{
				if(cdb.getValidityEndTime().before(new Date()))
				{//更新过期优惠券状态
					capitalCouponService.expireCoupon(cdb.getDbid());
				}else{
					count++;
				}
			}

		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
