package com.depression.controller.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.service.QiniuService;

@Controller
@RequestMapping("/Qiniu")
public class QiniuController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	QiniuService qiniuService;
	
	/**
	 * 获取文件上传token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainFileUploadToken.json")
	@ResponseBody
	public Object obtainFileUploadToken()
	{
		ResultEntity result = new ResultEntity();
		
		String token = qiniuService.obtainFileUploadToken();
		
		result.put("token", token);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取图片上传token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPictureUploadToken.json")
	@ResponseBody
	public Object obtainPictureUploadToken()
	{
		ResultEntity result = new ResultEntity();
		
		String token = qiniuService.obtainPictureUploadToken();
		
		result.put("token", token);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
