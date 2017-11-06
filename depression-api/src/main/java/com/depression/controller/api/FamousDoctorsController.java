package com.depression.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.FamousDoctors;
import com.depression.service.FamousDoctorsService;
import com.depression.utils.Configuration;

/**
 * 名医介绍
 * 
 * @author fanxinhui
 * @date 2016/6/20
 */
@Controller
@RequestMapping("/famousDoctors")
public class FamousDoctorsController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	FamousDoctorsService famousDoctorsService;

	@RequestMapping(method = RequestMethod.POST, value = "/getAll.json")
	@ResponseBody
	public Object getAll(HttpServletRequest request, String doctId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		// 查询所有名医列表
		List<FamousDoctors> famousDoctorsList = famousDoctorsService.getValidFamousDoctorsList();
		for (FamousDoctors doctors : famousDoctorsList)
		{
			// 转换实际文件路径
			if (doctors.getAvatar() != null && !doctors.getAvatar().equals(""))
			{
				doctors.setImgPreviewPath(doctors.getAvatar());
			}
		}

		result.put("list", famousDoctorsList);
		return result;
	}
}
