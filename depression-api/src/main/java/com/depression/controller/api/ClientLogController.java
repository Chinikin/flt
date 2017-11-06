package com.depression.controller.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.service.ClientLogService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/ClientLog")
public class ClientLogController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	ClientLogService clientLogService;
	
	@RequestMapping(value = "/newClientLog.json")
	@ResponseBody
	public Object newClientLog(Byte type, String marking,  @RequestParam("log")String clog)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(type,
				marking,
				clog))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			clientLogService.newClientLog(type, marking, clog);
		}catch (Exception e)
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
}
