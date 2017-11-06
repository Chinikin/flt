package com.depression.model.web.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:ziye_huang
 * @date:2016年8月30日
 */

public class CapitalPlatformUnitFundStatisticsDTO
{

	private BigDecimal incomeAmount;// 收入
	private BigDecimal expensesAmount;// 支出
    /* @Comment(充值统计) */
    private BigDecimal topUpAmount;

    /* @Comment(提现统计) */
    private BigDecimal withdrawAmount;
    
    /* @Comment(统计日期) */
    private Date statisticsDate;

	public BigDecimal getIncomeAmount()
	{
		return incomeAmount;
	}

	public void setIncomeAmount(BigDecimal incomeAmount)
	{
		this.incomeAmount = incomeAmount;
	}

	public BigDecimal getExpensesAmount()
	{
		return expensesAmount;
	}

	public void setExpensesAmount(BigDecimal expensesAmount)
	{
		this.expensesAmount = expensesAmount;
	}

	public BigDecimal getTopUpAmount()
	{
		return topUpAmount;
	}

	public void setTopUpAmount(BigDecimal topUpAmount)
	{
		this.topUpAmount = topUpAmount;
	}

	public BigDecimal getWithdrawAmount()
	{
		return withdrawAmount;
	}

	public void setWithdrawAmount(BigDecimal withdrawAmount)
	{
		this.withdrawAmount = withdrawAmount;
	}

	public Date getStatisticsDate()
	{
		return statisticsDate;
	}

	public void setStatisticsDate(Date statisticsDate)
	{
		this.statisticsDate = statisticsDate;
	}

	
}
