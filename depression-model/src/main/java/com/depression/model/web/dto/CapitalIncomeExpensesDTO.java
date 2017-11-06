package com.depression.model.web.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:ziye_huang
 * @date:2016年8月29日
 */

public class CapitalIncomeExpensesDTO
{
	private Long ieid;
	private String no;// 明细编号
	private String orderNo;// 订单号
	private BigDecimal amount;// 金额
	private Byte items;// 0 收入，1支出
	private Byte channel;//
	private Date createTime;
	private BigDecimal cashBalance;// 余额
	private BigDecimal serviceIncomeAmount;// 总收入
	private BigDecimal expenseAmount;// 总支出
	private BigDecimal withdrawAmount;// 总提现

	public Long getIeid()
	{
		return ieid;
	}

	public void setIeid(Long ieid)
	{
		this.ieid = ieid;
	}

	public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public BigDecimal getAmount()
	{
		return amount;
	}

	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}

	public Byte getItems()
	{
		return items;
	}

	public void setItems(Byte items)
	{
		this.items = items;
	}

	public Byte getChannel()
	{
		return channel;
	}

	public void setChannel(Byte channel)
	{
		this.channel = channel;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
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
