package com.depression.base.wechat.entity;

public class AccessTicket
{
	private String ticket;
	private int expiresIn;

	public String getTicket()
	{
		return ticket;
	}

	public void setTicket(String ticket)
	{
		this.ticket = ticket;
	}

	public int getExpiresIn()
	{
		return this.expiresIn;
	}

	public void setExpiresIn(int expiresIn)
	{
		this.expiresIn = expiresIn;
	}

	public AccessTicket(String ticket, int expiresIn)
	{
		this.ticket = ticket;
		this.expiresIn = expiresIn;
	}

	public AccessTicket()
	{
	}
}
