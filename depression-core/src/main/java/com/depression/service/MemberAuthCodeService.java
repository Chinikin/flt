package com.depression.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberAuthCodeDAO;
import com.depression.model.MemberAuthCode;

/**
 * @author:ziye_huang
 * @date:2016年5月6日
 */
@Service
public class MemberAuthCodeService
{
	@Autowired
	MemberAuthCodeDAO memberAuthCodeDAO;

	public Integer insert(MemberAuthCode memberAuthCode)
	{
		memberAuthCode.setCreateTime(new Date());
		return memberAuthCodeDAO.insert(memberAuthCode);
	}

	public MemberAuthCode getAuthCode(MemberAuthCode memberAuthCode)
	{
		return memberAuthCodeDAO.getAuthCode(memberAuthCode);
	}

	public Integer update(MemberAuthCode memberAuthCode)
	{
		return memberAuthCodeDAO.update(memberAuthCode);
	}

	public Integer delete(String mobilePhone){
		return memberAuthCodeDAO.delete(mobilePhone);
	}
}
