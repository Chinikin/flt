package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.AppReleaseInfoDAO;
import com.depression.model.AppReleaseInfo;

@Service
public class AppReleaseInfoService
{
	@Autowired
	AppReleaseInfoDAO dao; 
	
	public int releaseNewVersion(AppReleaseInfo appReleaseInfo)
	{
		return dao.insert(appReleaseInfo);
	}
	
	public int changeReleaseInfo(AppReleaseInfo appReleaseInfo)
	{
		return dao.update(appReleaseInfo);
	}
	
	public int deleteReleaseInfo(Long releaseId)
	{
		return dao.delete(releaseId);
	}
	
	public List<AppReleaseInfo> listReleaseInfoByPage(AppReleaseInfo appReleaseInfo)
	{
		return dao.listByPage(appReleaseInfo);
	}
	
	public AppReleaseInfo getLatestReleaseInfo(Integer osType)
	{
		return dao.getLatestOne(osType);
	}
}
