package com.depression.model;

import java.util.Date;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */

public class MemberMoodRecord extends Page
{

	private Long moodRecordId;
	private Long mid;
	private String moodRecord;
	private Integer isDelete;
	private Date recordDate;

	public Long getMoodRecordId()
	{
		return moodRecordId;
	}

	public void setMoodRecordId(Long moodRecordId)
	{
		this.moodRecordId = moodRecordId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getMoodRecord()
	{
		return moodRecord;
	}

	public void setMoodRecord(String moodRecord)
	{
		this.moodRecord = moodRecord;
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
