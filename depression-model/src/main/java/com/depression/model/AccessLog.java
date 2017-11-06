package com.depression.model;

public class AccessLog
{
	private Long acid;
	private String remoteIp;

	public Long getAcid()
	{
		return acid;
	}

	public void setAcid(Long acid)
	{
		this.acid = acid;
	}

	public String getRemoteIp()
	{
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp)
	{
		this.remoteIp = remoteIp;
	}
}
