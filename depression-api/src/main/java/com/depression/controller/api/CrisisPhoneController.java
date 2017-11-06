package com.depression.controller.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CrisisPhone;
import com.depression.service.CrisisPhoneService;

@Controller
@RequestMapping("/CrisisPhone")
public class CrisisPhoneController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	CrisisPhoneService crisisPhoneService;
	
	@RequestMapping(value = "/V1/obtainCrisisPhone.json")
	@ResponseBody
	public Object obtainCrisisPhoneV1()
	{
		ResultEntity result = new ResultEntity();
		String phoneNum ;
		try{
			CrisisPhone crisisPhone = crisisPhoneService.obtainCrisisPhone();
			phoneNum = crisisPhone.getPhoneNum();
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		
		result.put("crisisPhone", phoneNum);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
