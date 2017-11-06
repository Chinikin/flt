package com.depression.model;

import java.util.Date;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */

public class MemberMoodIndex extends Page
{

	private Long moodIndexId;
	private Long mid;
	private Integer moodIndex;
	private Integer isDelete;
	private Date recordDate;

	public Long getMoodIndexId()
	{
		return moodIndexId;
	}

	public void setMoodIndexId(Long moodIndexId)
	{
		this.moodIndexId = moodIndexId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Integer getMoodIndex()
	{
		return moodIndex;
	}

	public void setMoodIndex(Integer moodIndex)
	{
		this.moodIndex = moodIndex;
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
