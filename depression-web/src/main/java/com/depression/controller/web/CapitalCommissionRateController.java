package com.depression.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalCommissionRate;
import com.depression.service.CapitalCommissionRateService;

/**
 * 平台佣金比例配置
 * 
 * @author hongqian_li
 * 
 */
@Controller
@RequestMapping("/CapitalCommissionRate")
public class CapitalCommissionRateController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	CapitalCommissionRateService mCapitalCommissionRateService;

	@RequestMapping(method = RequestMethod.POST, value = "/updateCapitalCommissionRate.json")
	@ResponseBody
	public Object updateCapitalCommissionRate(HttpSession session, HttpServletRequest request, Integer type, Integer rate)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			// 类型：1 倾述佣金，0 咨询佣金
			if (null == type || null == rate)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}
			CapitalCommissionRate ccr = new CapitalCommissionRate();
			ccr.setType(type);
			ccr = mCapitalCommissionRateService.selectSelective(ccr);
			if (null == ccr)
			{
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			ccr.setRate(rate);
			mCapitalCommissionRateService.update(ccr);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.debug(e.toString());
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getAllCapitalCommissionRate.json")
	@ResponseBody
	public Object getAllCapitalCommissionRate(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			List<CapitalCommissionRate> all = mCapitalCommissionRateService.selectAll(new CapitalCommissionRate());
			result.put("list", all);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.debug(e.toString());
		}
		return result;
	}
}
