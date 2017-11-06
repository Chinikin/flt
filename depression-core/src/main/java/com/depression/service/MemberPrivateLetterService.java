package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberPrivateLetterDAO;
import com.depression.model.MemberPrivateLetter;

@Service
public class MemberPrivateLetterService
{
	@Autowired
	MemberPrivateLetterDAO memberPrivateLetterDAO;

	public int insert(MemberPrivateLetter memberPrivateLetter)
	{
		memberPrivateLetter.setReceiveTime(new Date());
		return memberPrivateLetterDAO.insert(memberPrivateLetter);
	}

	public long selectCount(MemberPrivateLetter memberPrivateLetter)
	{
		return memberPrivateLetterDAO.selectCount(memberPrivateLetter);
	}

	public List<MemberPrivateLetter> selectByPage(MemberPrivateLetter memberPrivateLetter)
	{
		return memberPrivateLetterDAO.selectByPage(memberPrivateLetter);
	}
}
