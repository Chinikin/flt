package com.depression.dao;

import java.util.List;

import com.depression.model.MemberPrivateLetter;

public interface MemberPrivateLetterDAO
{

	public int insert(MemberPrivateLetter memberPrivateLetter);

	public long selectCount(MemberPrivateLetter memberPrivateLetter);

	public List<MemberPrivateLetter> selectByPage(MemberPrivateLetter memberPrivateLetter);

}
