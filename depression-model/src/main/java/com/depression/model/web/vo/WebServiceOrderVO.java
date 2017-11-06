package com.depression.model.web.vo;

import java.util.Date;

import com.depression.model.ServiceOrder;

public class WebServiceOrderVO extends ServiceOrder
{
	/**
	 * 开始时间
	 */
	private Date createTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	
}
