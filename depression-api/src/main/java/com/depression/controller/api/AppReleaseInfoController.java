package com.depression.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.AppReleaseInfo;
import com.depression.service.AppReleaseInfoService;

@Controller
@RequestMapping("/AppReleaseInfoController")
public class AppReleaseInfoController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	AppReleaseInfoService appReleaseInfoService;
	
	private Object releaseNewVersion(AppReleaseInfo appReleaseInfo, Integer osType)
	{
		ResultEntity result = new ResultEntity();
		try{
			if(appReleaseInfo==null || appReleaseInfo.getSvnNum()==null ||
					appReleaseInfo.getVersionNum()==null || appReleaseInfo.getVersionName()==null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("发布信息不能为空");
				return result;
			}
			
			appReleaseInfoService.releaseNewVersion(appReleaseInfo);
			
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("发布成功");
			return result;
		}catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
			return result;
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/releaseAndroidNewVersion.json")
	@ResponseBody
	public Object releaseAndroidNewVersion(HttpSession session, HttpServletRequest request, AppReleaseInfo appReleaseInfo)
	{
		return releaseNewVersion(appReleaseInfo, AppReleaseInfo.OS_TYPE_ANDROID);
	}

	@RequestMapping(method=RequestMethod.POST, value="/releaseIosNewVersion.json")
	@ResponseBody
	public Object releaseIosNewVersion(HttpSession session, HttpServletRequest request, AppReleaseInfo appReleaseInfo)
	{
		return releaseNewVersion(appReleaseInfo, AppReleaseInfo.OS_TYPE_IOS);
	}
	
	private Object getLatestReleaseInfo(Integer osType)
	{
		ResultEntity result = new ResultEntity();
		try{

			AppReleaseInfo appReleaseInfo = appReleaseInfoService.getLatestReleaseInfo(osType);
			
			result.put("appReleaseInfo", appReleaseInfo);
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("获取成功");
			return result;
		}catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
			return result;
		}
	}
	
	@RequestMapping(value="/getLatestAndroidReleaseInfo.json")
	@ResponseBody
	public Object getLatestAndroidReleaseInfo(HttpSession session, HttpServletRequest request)
	{
		return getLatestReleaseInfo(AppReleaseInfo.OS_TYPE_ANDROID);
	}
	
	@RequestMapping(value="/getLatestIosReleaseInfo.json")
	@ResponseBody
	public Object getLatestIosReleaseInfo(HttpSession session, HttpServletRequest request)
	{
		return getLatestReleaseInfo(AppReleaseInfo.OS_TYPE_IOS);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/modifyReleaseInfo.json")
	@ResponseBody
	public Object modifyReleaseInfo(HttpSession session, HttpServletRequest request, AppReleaseInfo appReleaseInfo)
	{
		ResultEntity result = new ResultEntity();
		if(appReleaseInfo==null || appReleaseInfo.getReleaseId()==null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("releaseId不能为空");
			return result;
		}
		appReleaseInfoService.changeReleaseInfo(appReleaseInfo);
		
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("修改成功");
		return result;
	}
	
	private Object listReleaseInfoByPage(AppReleaseInfo appReleaseInfo, Integer osType)
	{
		ResultEntity result = new ResultEntity();
		if(appReleaseInfo==null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("分页信息不能为空");
			return result;
		}
		
		appReleaseInfo.setOsType(osType);
		List<AppReleaseInfo> ariList = appReleaseInfoService.listReleaseInfoByPage(appReleaseInfo);
		
		result.put("list", ariList);
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("查询成功");
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/listAndroidReleaseInfoByPage.json")
	@ResponseBody
	public Object listAndroidReleaseInfoByPage(HttpSession session, HttpServletRequest request, AppReleaseInfo appReleaseInfo)
	{
		return listReleaseInfoByPage(appReleaseInfo,AppReleaseInfo.OS_TYPE_ANDROID);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/listIosReleaseInfoByPage.json")
	@ResponseBody
	public Object listIosReleaseInfoByPage(HttpSession session, HttpServletRequest request, AppReleaseInfo appReleaseInfo)
	{
		return listReleaseInfoByPage(appReleaseInfo,AppReleaseInfo.OS_TYPE_IOS);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/deleteReleaseInfo.json")
	@ResponseBody
	public Object deleteReleaseInfo(HttpSession session, HttpServletRequest request, Long releaseId)
	{
		ResultEntity result = new ResultEntity();
		if(releaseId==null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("releaseId不能为空");
			return result;
		}
		
		appReleaseInfoService.deleteReleaseInfo(releaseId);
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("删除成功");
		return result;
	}
}
