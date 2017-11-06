package com.depression.dao;

import com.depression.model.MemberAuthCode;

public interface MemberAuthCodeDAO
{
	public Integer insert(MemberAuthCode memberAuthCode);

	public MemberAuthCode getAuthCode(MemberAuthCode memberAuthCode);

	public Integer update(MemberAuthCode memberAuthCode);
	
	public Integer delete(String mobilePhone);

}
