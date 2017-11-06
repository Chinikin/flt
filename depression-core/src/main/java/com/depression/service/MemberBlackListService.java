package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberBlackListDAO;
import com.depression.model.MemberBlackList;

/**
 * @author:ziye_huang
 * @date:2016年5月9日
 */

@Service
public class MemberBlackListService
{

	@Autowired
	MemberBlackListDAO memberBlackListDAO;

	public Integer insert(MemberBlackList memberBlackList)
	{
		memberBlackList.setIsDelete(0);
		return memberBlackListDAO.insert(memberBlackList);
	}

	public Integer delete(MemberBlackList memberBlackList)
	{
		return memberBlackListDAO.delete(memberBlackList);
	}

	public Integer update(MemberBlackList memberBlackList)
	{
		return memberBlackListDAO.update(memberBlackList);
	}

	public MemberBlackList getMemberBlackList(MemberBlackList memberBlackList)
	{
		return memberBlackListDAO.getMemberBlackList(memberBlackList);
	}

	public List<MemberBlackList> findAllBlackListByMid(MemberBlackList memberBlackList)
	{
		return memberBlackListDAO.findAllBlackListByMid(memberBlackList);
	}

}
