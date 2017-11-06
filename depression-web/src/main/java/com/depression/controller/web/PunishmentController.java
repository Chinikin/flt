package com.depression.controller.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.service.PunishmentService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/Punishment")
public class PunishmentController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	PunishmentService punishmentService;
	
	/**
	 * 增加用户禁言天数
	 * @param mid
	 * @param days
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addDisableMessageDays.json")
	@ResponseBody
	public Object addDisableMessageDays(Long mid, Integer days)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, days))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			punishmentService.addDisableMessageDays(mid, days);
		}catch (Exception e)
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
	 * 获取用户剩余禁言天数
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainDisableMessageDays.json")
	@ResponseBody
	public Object obtainDisableMessageDays(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		Integer days;
		try{
			days = punishmentService.obtainDisableMessageDays(mid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.put("days", days);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 清楚用户禁言天数
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/clearDisableMessageDays.json")
	@ResponseBody
	public Object clearDisableMessageDays(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try{
			punishmentService.clearDisableMessageDays(mid);
		}catch (Exception e)
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
