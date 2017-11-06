package com.depression.dao;

import java.util.List;

import com.depression.model.AppReleaseInfo;

public interface AppReleaseInfoDAO
{
	int insert(AppReleaseInfo appReleaseInfo);
	
	int update(AppReleaseInfo appReleaseInfo);
	
	int delete(Long releaseId);
	
	List<AppReleaseInfo> listByPage(AppReleaseInfo appReleaseInfo);
	
	AppReleaseInfo getLatestOne(Integer osType);
}
