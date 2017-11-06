package com.depression.model.web.vo;

import java.math.BigDecimal;

/**
 * @author:ziye_huang
 * @date:2016年8月28日
 */

public class CapitalPersonalAssetsVO
{
	private Long paid;
	private Long mid;// 会员id
	private String account;// 账号
	private BigDecimal cashBalance;// 现金余额
	private BigDecimal serviceIncomeAmount;// 服务收入总额
	private BigDecimal payAmount;// 充值总额
	private BigDecimal expenseAmount;// 支出总额
	private BigDecimal withdrawAmount;// 提现总额

	public Long getPaid()
	{
		return paid;
	}

	public void setPaid(Long paid)
	{
		this.paid = paid;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
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

	public BigDecimal getPayAmount()
	{
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount)
	{
		this.payAmount = payAmount;
	}

	public BigDecimal getExpenseAmount()
	{
		return expenseAmount;
	}

	public void setExpenseAmount(BigDecimal expenseAmount)
	{
		this.expenseAmount = expenseAmount;
	}

	public BigDecimal getWithdrawAmount()
	{
		return withdrawAmount;
	}

	public void setWithdrawAmount(BigDecimal withdrawAmount)
	{
		this.withdrawAmount = withdrawAmount;
	}

}
