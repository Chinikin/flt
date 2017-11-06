package com.depression.model;

/******************************************************************
 ** 类    名：SystemMessage
 ** 描    述：系统消息
 ** 创 建 者：ziye_huang
 ** 创建时间：2016-05-05 11:39:18
 ******************************************************************/

import java.util.Date;

/**
 * 系统消息(SYSTEM_MESSAGE)
 * 
 * @author ziye_huang
 * @version 1.0.0 2016-05-05
 */
public class SystemMessage extends Page implements java.io.Serializable
{
	
	static public final int TYPE_CONCORN = 1;
	static public final int TYPE_UNCONCORN = 2;
	
	/** 版本号 */
	private static final long serialVersionUID = -8029976787060913965L;

	/** 主键 */
	private Long messageId;

	/** 主键 */
	private Long mid;

	/** 类型 */
	private Integer type;
	
	/** 引用id */
	private Long refId;

	/** 发送时间 */
	private Date sendTime;

	/** 是否已读：0.未读 1.已读 */
	private Integer readStatus;

	/** 默认0:不删除 1：删除 */
	private Integer isDelete;
	
	private Object refObj;
	

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public Long getMessageId()
	{
		return this.messageId;
	}

	/**
	 * 设置主键
	 * 
	 * @param messageId
	 *            主键
	 */
	public void setMessageId(Long messageId)
	{
		this.messageId = messageId;
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public Long getMid()
	{
		return this.mid;
	}

	/**
	 * 设置主键
	 * 
	 * @param mid
	 *            主键
	 */
	public void setMid(Long mid)
	{
		this.mid = mid;
	}
	
	public Object getRefObj()
	{
		return refObj;
	}

	public void setRefObj(Object refObj)
	{
		this.refObj = refObj;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public Long getRefId()
	{
		return refId;
	}

	public void setRefId(Long refId)
	{
		this.refId = refId;
	}

	/**
	 * 获取发送时间
	 * 
	 * @return 发送时间
	 */
	public Date getSendTime()
	{
		return this.sendTime;
	}

	/**
	 * 设置发送时间
	 * 
	 * @param sendTime
	 *            发送时间
	 */
	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	/**
	 * 获取是否已读：0.未读 1.已读
	 * 
	 * @return 是否已读
	 */
	public Integer getReadStatus()
	{
		return this.readStatus;
	}

	/**
	 * 设置是否已读：0.未读 1.已读
	 * 
	 * @param readStatus
	 *            是否已读：0.未读 1.已读
	 */
	public void setReadStatus(Integer readStatus)
	{
		this.readStatus = readStatus;
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
}