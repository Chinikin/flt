package com.depression.model.web.dto;

import java.math.BigDecimal;

import com.depression.model.CapitalWithdrawRequest;

/**
 * 提现申请DTO
 * 
 * @author hongqian_li
 * 
 */
public class WebCapitalWithdrawRequestDTO extends CapitalWithdrawRequest
{
	/**
	 * 咨询师姓名
	 */
	private String psychologicalName;
	/**
	 * 咨询师电话
	 */
	private String psycholoPhone;

	/**
	 * 账户余额
	 */
	private BigDecimal cashBalance;

	public String getPsychologicalName()
	{
		return psychologicalName;
	}

	public void setPsychologicalName(String psychologicalName)
	{
		this.psychologicalName = psychologicalName;
	}

	public String getPsycholoPhone()
	{
		return psycholoPhone;
	}

	public void setPsycholoPhone(String psycholoPhone)
	{
		this.psycholoPhone = psycholoPhone;
	}

	public BigDecimal getCashBalance()
	{
		return cashBalance;
	}

	public void setCashBalance(BigDecimal cashBalance)
	{
		this.cashBalance = cashBalance;
	}

}
