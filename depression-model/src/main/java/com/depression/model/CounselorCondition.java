package com.depression.model;

import java.math.BigDecimal;

/**
 * 咨询师资金账户查询model
 * 
 * @author:ziye_huang
 * @date:2016年9月10日
 */

public class CounselorCondition extends Page
{
	private Integer userType;// 用户类型：1 普通用户，2咨询师
	private String account;// 账户
	private String nickname;// 姓名
	private String title;// 资质
//	private Date startTime;// 开始时间
//	private Date endTime;// 结束时间
	private Integer startAnswerCount;// 咨询数
	private Integer endAnswerCount;// 咨询数
	private BigDecimal startServiceIncomeAmount;// 收入金额
	private BigDecimal endServiceIncomeAmount;// 收入金额
	private Integer topNum;// 前多少名
	private Integer topPercent;// 前多少名的百分比
	private String sortName;// 排序的字段名
	private String sortType;// 排序方式,"asc","desc"
	private Integer pageIndex;
	private Integer pageSize;

	public Integer getUserType()
	{
		return userType;
	}

	public void setUserType(Integer userType)
	{
		this.userType = userType;
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	/*public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}*/

	public Integer getStartAnswerCount()
	{
		return startAnswerCount;
	}

	public void setStartAnswerCount(Integer startAnswerCount)
	{
		this.startAnswerCount = startAnswerCount;
	}

	public Integer getEndAnswerCount()
	{
		return endAnswerCount;
	}

	public void setEndAnswerCount(Integer endAnswerCount)
	{
		this.endAnswerCount = endAnswerCount;
	}

	public BigDecimal getStartServiceIncomeAmount()
	{
		return startServiceIncomeAmount;
	}

	public void setStartServiceIncomeAmount(BigDecimal startServiceIncomeAmount)
	{
		this.startServiceIncomeAmount = startServiceIncomeAmount;
	}

	public BigDecimal getEndServiceIncomeAmount()
	{
		return endServiceIncomeAmount;
	}

	public void setEndServiceIncomeAmount(BigDecimal endServiceIncomeAmount)
	{
		this.endServiceIncomeAmount = endServiceIncomeAmount;
	}

	public Integer getTopNum()
	{
		return topNum;
	}

	public void setTopNum(Integer topNum)
	{
		this.topNum = topNum;
	}

	public Integer getTopPercent()
	{
		return topPercent;
	}

	public void setTopPercent(Integer topPercent)
	{
		this.topPercent = topPercent;
	}

	public String getSortName()
	{
		return sortName;
	}

	public void setSortName(String sortName)
	{
		this.sortName = sortName;
	}

	public String getSortType()
	{
		return sortType;
	}

	public void setSortType(String sortType)
	{
		this.sortType = sortType;
	}

	public Integer getPageIndex()
	{
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

}
