package com.depression.model;

/******************************************************************
 ** 类    名：MemberBlackList
 ** 描    述：黑名单
 ** 创 建 者：ziye_huang
 ** 创建时间：2016-05-05 11:39:18
 ******************************************************************/

/**
 * 黑名单(MEMBER_BLACK_LIST)
 * 
 * @author ziye_huang
 * @version 1.0.0 2016-05-05
 */
public class MemberBlackList extends Page implements java.io.Serializable
{
	/** 版本号 */
	private static final long serialVersionUID = 3989265924996177894L;

	/** 主键 */
	private Long memberBlackId;

	/** 会员id */
	private Long mid;

	/** 黑名单会员id */
	private Long blackMid;

	/** 默认0:不删除 1：删除 */
	private Integer isDelete;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public Long getMemberBlackId()
	{
		return this.memberBlackId;
	}

	/**
	 * 设置主键
	 * 
	 * @param memberBlackId
	 *            主键
	 */
	public void setMemberBlackId(Long memberBlackId)
	{
		this.memberBlackId = memberBlackId;
	}

	/**
	 * 获取会员id
	 * 
	 * @return 会员id
	 */
	public Long getMid()
	{
		return this.mid;
	}

	/**
	 * 设置会员id
	 * 
	 * @param mid
	 *            会员id
	 */
	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	/**
	 * 获取黑名单会员id
	 * 
	 * @return 黑名单会员id
	 */
	public Long getBlackMid()
	{
		return this.blackMid;
	}

	/**
	 * 设置黑名单会员id
	 * 
	 * @param blackMid
	 *            黑名单会员id
	 */
	public void setBlackMid(Long blackMid)
	{
		this.blackMid = blackMid;
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