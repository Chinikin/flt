package com.depression.model;

import java.util.Date;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */

public class MemberSleepQuality extends Page
{

	private Long sleepQualityId;
	private Long mid;
	private Integer sleepQuality;
	private Integer isDelete;
	private Date recordDate;

	public Long getSleepQualityId()
	{
		return sleepQualityId;
	}

	public void setSleepQualityId(Long sleepQualityId)
	{
		this.sleepQualityId = sleepQualityId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Integer getSleepQuality()
	{
		return sleepQuality;
	}

	public void setSleepQuality(Integer sleepQuality)
	{
		this.sleepQuality = sleepQuality;
	}

	public Integer getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

	public Date getRecordDate()
	{
		return recordDate;
	}

	public void setRecordDate(Date recordDate)
	{
		this.recordDate = recordDate;
	}

}
