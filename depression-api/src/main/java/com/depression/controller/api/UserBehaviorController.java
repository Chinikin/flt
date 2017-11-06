package com.depression.controller.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.UbAccessRecord;
import com.depression.service.UserBehaviorService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/UserBehavior")
public class UserBehaviorController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	UserBehaviorService userBehaviorService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/createAccessRecord.json")
	@ResponseBody
	public Object createAccessRecord(String accessRecord)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(accessRecord))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<UbAccessRecord> accessRecords;
		try{
			accessRecords = JSON.parseArray(accessRecord, UbAccessRecord.class);
		}catch(Exception e)
		{
			log.error(ErrorCode.ERROR_PARAM_JSON.getMessage(), e);
			result.setCode(ErrorCode.ERROR_PARAM_JSON.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_JSON.getMessage());
			return result;
		}
		
		try{
			for(UbAccessRecord ar : accessRecords)
			{
				//避免json注入非法字段值
				ar.setUarId(null);
				ar.setIsDelete(null);
				ar.setIsEnable(null);
				
				userBehaviorService.createAccessRecord(ar);
			}
		}catch(Exception e)
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
