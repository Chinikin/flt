package com.depression.entity;

import java.util.Date;

/**
 * 注册voip返回的实体类信息
 * 
 * @author ziye_huang
 * 
 */
public class VoipUser
{

	private String subAccountSid;
	private String voipAccount;
	private String voipPwd;
	private String subToken;
	private Date dateCreated;

	public String getSubAccountSid()
	{
		return subAccountSid;
	}

	public void setSubAccountSid(String subAccountSid)
	{
		this.subAccountSid = subAccountSid;
	}

	public String getVoipAccount()
	{
		return voipAccount;
	}

	public void setVoipAccount(String voipAccount)
	{
		this.voipAccount = voipAccount;
	}

	public String getVoipPwd()
	{
		return voipPwd;
	}

	public void setVoipPwd(String voipPwd)
	{
		this.voipPwd = voipPwd;
	}

	public String getSubToken()
	{
		return subToken;
	}

	public void setSubToken(String subToken)
	{
		this.subToken = subToken;
	}

	public Date getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString()
	{
		return "VoipUser [subAccountSid=" + subAccountSid + ", voipAccount=" + voipAccount + ", voipPwd=" + voipPwd + ", subToken=" + subToken + ", dateCreated=" + dateCreated + "]";
	}

}
