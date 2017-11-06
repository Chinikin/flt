package com.depression.model;

/******************************************************************
 ** 类    名：MemberPrivateLetter
 ** 描    述：私信
 ** 创 建 者：ziye_huang
 ** 创建时间：2016-05-05 11:39:18
 ******************************************************************/

import java.util.Date;

/**
 * 私信(MEMBER_PRIVATE_LETTER)
 * 
 * @author ziye_huang
 * @version 1.0.0 2016-05-05
 */
public class MemberPrivateLetter extends Page implements java.io.Serializable
{
	/** 版本号 */
	private static final long serialVersionUID = 1393633065818405230L;

	/** 主键 */
	private Long memberPrivateLetterId;

	/** 发起方 */
	private Long senderId;

	/** 接收方 */
	private Long receiverId;

	/** 类型 */
	private Integer type;

	/** 内容 */
	private String content;

	/** 发送日期 */
	private Date sendTime;

	/** 接收日期 */
	private Date receiveTime;

	/**  */
	private Integer readStatus;

	/** 默认0:不删除 1：删除 */
	private Integer isDelete;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public Long getMemberPrivateLetterId()
	{
		return this.memberPrivateLetterId;
	}

	/**
	 * 设置主键
	 * 
	 * @param memberPrivateLetterId
	 *            主键
	 */
	public void setMemberPrivateLetterId(Long memberPrivateLetterId)
	{
		this.memberPrivateLetterId = memberPrivateLetterId;
	}

	/**
	 * 获取发起方
	 * 
	 * @return 发起方
	 */
	public Long getSenderId()
	{
		return this.senderId;
	}

	/**
	 * 设置发起方
	 * 
	 * @param senderId
	 *            发起方
	 */
	public void setSenderId(Long senderId)
	{
		this.senderId = senderId;
	}

	/**
	 * 获取接收方
	 * 
	 * @return 接收方
	 */
	public Long getReceiverId()
	{
		return this.receiverId;
	}

	/**
	 * 设置接收方
	 * 
	 * @param receiverId
	 *            接收方
	 */
	public void setReceiverId(Long receiverId)
	{
		this.receiverId = receiverId;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Integer getType()
	{
		return this.type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Integer type)
	{
		this.type = type;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent()
	{
		return this.content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * 获取发送日期
	 * 
	 * @return 发送日期
	 */
	public Date getSendTime()
	{
		return this.sendTime;
	}

	/**
	 * 设置发送日期
	 * 
	 * @param sendTime
	 *            发送日期
	 */
	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	/**
	 * 获取接收日期
	 * 
	 * @return 接收日期
	 */
	public Date getReceiveTime()
	{
		return this.receiveTime;
	}

	/**
	 * 设置接收日期
	 * 
	 * @param receiveTime
	 *            接收日期
	 */
	public void setReceiveTime(Date receiveTime)
	{
		this.receiveTime = receiveTime;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public Integer getReadStatus()
	{
		return this.readStatus;
	}

	/**
	 * 设置
	 * 
	 * @param readStatus
	 * 
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