package com.depression.model;

import java.util.Date;

/**
 * 跳转方式问卷测试结论
 * 
 * @author fanxinhui
 * @date 2016/6/30
 */
public class TestingResultForMemberOld extends Page
{
	private Integer tid;
	private Long mid;
	private Integer resId;
	private Date testTime;
	private Integer isDelete;
	
	public Integer getTid()
	{
		return tid;
	}
	public void setTid(Integer tid)
	{
		this.tid = tid;
	}
	public Long getMid()
	{
		return mid;
	}
	public void setMid(Long mid)
	{
		this.mid = mid;
	}
	public Integer getResId()
	{
		return resId;
	}
	public void setResId(Integer resId)
	{
		this.resId = resId;
	}
	public Date getTestTime()
	{
		return testTime;
	}
	public void setTestTime(Date testTime)
	{
		this.testTime = testTime;
	}
	public Integer getIsDelete()
	{
		return isDelete;
	}
	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

}
