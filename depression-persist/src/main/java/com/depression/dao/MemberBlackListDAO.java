package com.depression.dao;

import java.util.List;

import com.depression.model.MemberBlackList;

/**
 * @author:ziye_huang
 * @date:2016年5月9日
 */

public interface MemberBlackListDAO
{

	public Integer insert(MemberBlackList memberBlackList);

	public Integer delete(MemberBlackList memberBlackList);

	public Integer update(MemberBlackList memberBlackList);

	public MemberBlackList getMemberBlackList(MemberBlackList memberBlackList);

	public List<MemberBlackList> findAllBlackListByMid(MemberBlackList memberBlackList);

}
