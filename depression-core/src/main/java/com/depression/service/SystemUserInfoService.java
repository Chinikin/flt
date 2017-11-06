package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.SystemUserInfoMapper;
import com.depression.model.SystemUserInfo;

@Service
public class SystemUserInfoService
{
	@Autowired
	SystemUserInfoMapper userInfoMapper;

	public Integer createUser(SystemUserInfo userInfo)
	{
		userInfo.setCreateTime(new Date());
		return userInfoMapper.insertSelective(userInfo);
	}

	public SystemUserInfo getSystemUserInfo(SystemUserInfo userInfo)
	{
		List<SystemUserInfo> suis = userInfoMapper.selectSelective(userInfo);
		if (suis.size() > 0)
		{
			return suis.get(0);
		} else
		{
			return null;
		}
	}

	public SystemUserInfo getSystemUserInfo(String username)
	{
		SystemUserInfo userInfo = new SystemUserInfo();
		userInfo.setUsername(username);
		List<SystemUserInfo> suis = userInfoMapper.selectSelective(userInfo);
		if (suis.size() > 0)
		{
			return suis.get(0);
		} else
		{
			return null;
		}
	}

	public Integer updateByKey(SystemUserInfo userInfo)
	{
		userInfo.setModifyTime(new Date());
		return userInfoMapper.updateByPrimaryKeySelective(userInfo);
	}

	public Integer updatePsw(SystemUserInfo userInfo)
	{
		userInfo.setModifyTime(new Date());
		return userInfoMapper.updateByPrimaryKeySelective(userInfo);
	}

	public SystemUserInfo userLogin(SystemUserInfo userInfo)
	{
		return getSystemUserInfo(userInfo);
	}

	/**
	 * 分页模糊查询列表
	 * 
	 * @param record
	 * @return
	 */
	public List<SystemUserInfo> selectFuzzyListWithPage(SystemUserInfo record)
	{
		return userInfoMapper.selectFuzzyListWithPage(record);
	}

	/**
	 * 分页模糊查询数量
	 * 
	 * @param record
	 * @return
	 */
	public int countFuzzyList(SystemUserInfo record)
	{
		return userInfoMapper.countFuzzyList(record);
	}
	
	public SystemUserInfo selectByPrimaryKey(Long userId)
	{
		return userInfoMapper.selectByPrimaryKey(userId);
	}
	
	public int enableByPrimaryKeyBulk(List<Long> ids, Byte enable)
	{
		return userInfoMapper.enableByPrimaryKeyBulk(ids, enable);
	}
	
	public List<SystemUserInfo> selectSelective(SystemUserInfo record)
	{
		return userInfoMapper.selectSelective(record);
	}
}
