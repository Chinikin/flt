package com.depression.model;

import java.util.Date;

/******************************************************************
 ** 类    名：MemberStepCounter
 ** 描    述：会员计步
 ** 创 建 者：ziye_huang
 ** 创建时间：2016-05-05 11:39:18
 ******************************************************************/

/**
 * 会员计步(MEMBER_STEP_COUNTER)
 * 
 * @author ziye_huang
 * @version 1.0.0 2016-05-05
 */
public class MemberStepCounter extends Page implements java.io.Serializable
{
	/** 版本号 */
	private static final long serialVersionUID = -7915970937849468846L;

	/** 主键 */
	private Long stepCounterId;

	/** 会员外键 */
	private Long mid;

	/** 总计步数 */
	private Long allStepCounter;

	/** 默认0:不删除 1：删除 */
	private Integer isDelete;

	/** 开始时间 */
	private Date startTime;

	/** 结束时间 */
	private Date endTime;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public Long getStepCounterId()
	{
		return this.stepCounterId;
	}

	/**
	 * 设置主键
	 * 
	 * @param stepCounterId
	 *            主键
	 */
	public void setStepCounterId(Long stepCounterId)
	{
		this.stepCounterId = stepCounterId;
	}

	/**
	 * 获取会员外键
	 * 
	 * @return 会员外键
	 */
	public Long getMid()
	{
		return this.mid;
	}

	/**
	 * 设置会员外键
	 * 
	 * @param mid
	 *            会员外键
	 */
	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	/**
	 * 获取总计步数
	 * 
	 * @return 总计步数
	 */
	public Long getAllStepCounter()
	{
		return this.allStepCounter;
	}

	/**
	 * 设置总计步数
	 * 
	 * @param allStepCounter
	 *            总计步数
	 */
	public void setAllStepCounter(Long allStepCounter)
	{
		this.allStepCounter = allStepCounter;
	}

	/**
	 * 获取默认0:不删除 1：删除
	 * 
	 * @return 默认0:不删除 1
	 */
	public Integer getIsDelete()
	{
		return this.isDelete;
	}

	/**
	 * 设置默认0:不删除 1：删除
	 * 
	 * @param isDelete
	 *            默认0:不删除 1：删除
	 */
	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

	public Date getStartTime()
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
	}

}