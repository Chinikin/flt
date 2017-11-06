package com.depression.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.SystemDepartment;
import com.depression.model.SystemUserInfo;
import com.depression.service.PrivilegesService;
import com.depression.service.SystemDepartmentService;
import com.depression.service.SystemUserInfoService;

@Controller
@RequestMapping("/privileges/core")
public class PrivilegesController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	PrivilegesService privilegesService;
	
	@Autowired
	SystemUserInfoService systemUserInfoService;
	
	@Autowired
	SystemDepartmentService systemDepartmentService;

	/**
	 * 菜单列表
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listMenu.json")
	@ResponseBody
	public Object listMenu(HttpServletRequest request, @RequestParam(value = "userId", required = false)Long userId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 返回菜单json数据
		String jsonMenu = privilegesService.getMenuList(userId);
		result.put("jsonMenu", jsonMenu);
		
		SystemUserInfo systemUserInfo = systemUserInfoService.selectByPrimaryKey(userId);
		SystemDepartment systemDepartment = systemDepartmentService.selectByPrimaryKey(systemUserInfo.getDptId());
		result.put("userId", userId);
		if (systemUserInfo != null)
		{
			result.put("mobilePhone", systemUserInfo.getMobilePhone());
			result.put("showName", systemUserInfo.getShowName());
		}
		if (systemDepartment != null)
		{
			result.put("depName", systemDepartment.getDepName());
		} else
		{
			result.put("depName", "");
		}

		return result;
	}

	/**
	 * 更新菜单权限
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updMenuList.json")
	@ResponseBody
	public Object updMenuList(HttpServletRequest request, Long userId, String jsonMenu)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查
		if (userId == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("用户id不能为空");
			return result;
		}
		if (jsonMenu == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("菜单数据不能为空");
			return result;
		}

		// 保存菜单权限
		privilegesService.updateMenuList(userId, jsonMenu);
		
		SystemUserInfo systemUserInfo = systemUserInfoService.selectByPrimaryKey(userId);
		SystemDepartment systemDepartment = systemDepartmentService.selectByPrimaryKey(systemUserInfo.getDptId());
		result.put("userId", userId);
		if (systemUserInfo != null)
		{
			result.put("mobilePhone", systemUserInfo.getMobilePhone());
			result.put("showName", systemUserInfo.getShowName());
		}
		if (systemDepartment != null)
		{
			result.put("depName", systemDepartment.getDepName());
		} else
		{
			result.put("depName", "");
		}
		
		return result;
	}
}
