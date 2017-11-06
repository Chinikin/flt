package com.depression.model.web.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费记录
 * 
 * @author:ziye_huang
 * @date:2016年8月30日
 */

public class ExpensesRecordDTO
{
	private Long ieid;
	private String no;// 订单编号
	private String orderNo;// 订单号
	private BigDecimal amount;// 金额
	private String nickname;// 专家名
	private String practicalDuration;// 通话时长
	private Date createTime;
	private BigDecimal cashBalance;// 余额
	private BigDecimal serviceIncomeAmount;// 总收入
	private BigDecimal expenseAmount;// 总支出
	private BigDecimal withdrawAmount;// 总提现
	private BigDecimal orderPrice;// 订单金额

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

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getPracticalDuration()
	{
		return practicalDuration;
	}

	public void setPracticalDuration(String practicalDuration)
	{
		this.practicalDuration = practicalDuration;
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

	public BigDecimal getOrderPrice()
	{
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice)
	{
		this.orderPrice = orderPrice;
	}

}
