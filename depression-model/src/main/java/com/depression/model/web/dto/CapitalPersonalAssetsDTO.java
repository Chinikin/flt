package com.depression.model.web.dto;

import java.math.BigDecimal;

/**
 * @author:ziye_huang
 * @date:2016年8月28日
 */

public class CapitalPersonalAssetsDTO
{
	private Long mid;
	private Long paid;
	private String account;
	private String nickname;
	private String regTime;
	private String title;
	private Integer answerCount;
	private BigDecimal cashBalance;
	private BigDecimal serviceIncomeAmount;
	private String mobilePhone;

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Long getPaid()
	{
		return paid;
	}

	public void setPaid(Long paid)
	{
		this.paid = paid;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getRegTime()
	{
		return regTime;
	}

	public void setRegTime(String regTime)
	{
		this.regTime = regTime;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Integer getAnswerCount()
	{
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount)
	{
		this.answerCount = answerCount;
	}

	public BigDecimal getCashBalance()
	{
		return cashBalance;
	}

	public void setCashBalance(BigDecimal cashBalance)
	{
		this.cashBalance = cashBalance;
	}

	public BigDecimal getServiceIncomeAmount()
	{
		return serviceIncomeAmount;
	}

	public void setServiceIncomeAmount(BigDecimal serviceIncomeAmount)
	{
		this.serviceIncomeAmount = serviceIncomeAmount;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

}
