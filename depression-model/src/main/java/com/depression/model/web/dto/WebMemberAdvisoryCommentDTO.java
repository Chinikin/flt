package com.depression.model.web.dto;

import java.util.Date;

import com.depression.model.Page;

/**
 * (会员类型为咨询师)对会员咨询的回复支持盖楼模式，对回复进行回复(member_advisory_comment)
 * 
 */
public class WebMemberAdvisoryCommentDTO extends Page implements java.io.Serializable
{

	private static final long serialVersionUID = 325971756454453613L;

	/** 主键 */
	private Long commentId;

	/** 会员id */
	private Long mid;

	/** 咨询id，关联咨询表 */
	private Long advisoryId;

	/** 有用数量 */
	private Long praiseNum;

	/** 父级回复，指向它的父结点，就是另一条回复的id */
	private Long parentId;

	/** 评论内容 */
	private String commentContent;

	/** 创建时间 */
	private Date commentTime;

	/** 是否已读：0.未读 1.已读 */
	private Integer readStatus;

	/** 默认0:不删除 1：删除 */
	private Integer isDelete;
	
	/** 咨询师昵称 */
	private String nickname;
	
	/** 咨询师级别 */
	private String title;
	
	/** 咨询师头像 */
    private String thumbnail;
    
    /**禁言天数*/
    private Integer disableMessageDays;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public Long getCommentId()
	{
		return this.commentId;
	}

	/**
	 * 设置主键
	 * 
	 * @param commentId
	 *            主键
	 */
	public void setCommentId(Long commentId)
	{
		this.commentId = commentId;
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
	 * 获取咨询id，关联咨询表
	 * 
	 * @return 咨询id
	 */
	public Long getAdvisoryId()
	{
		return this.advisoryId;
	}

	/**
	 * 设置咨询id，关联咨询表
	 * 
	 * @param advisoryId
	 *            咨询id，关联咨询表
	 */
	public void setAdvisoryId(Long advisoryId)
	{
		this.advisoryId = advisoryId;
	}

	/**
	 * 获取有用数量
	 * 
	 * @return 有用数量
	 */
	public Long getPraiseNum()
	{
		return this.praiseNum;
	}

	/**
	 * 设置有用数量
	 * 
	 * @param praiseNum
	 *            有用数量
	 */
	public void setPraiseNum(Long praiseNum)
	{
		this.praiseNum = praiseNum;
	}

	/**
	 * 获取父级回复，指向它的父结点，就是另一条回复的id
	 * 
	 * @return 父级回复
	 */
	public Long getParentId()
	{
		return this.parentId;
	}

	/**
	 * 设置父级回复，指向它的父结点，就是另一条回复的id
	 * 
	 * @param parentId
	 *            父级回复，指向它的父结点，就是另一条回复的id
	 */
	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	/**
	 * 获取评论内容
	 * 
	 * @return 评论内容
	 */
	public String getCommentContent()
	{
		return this.commentContent;
	}

	/**
	 * 设置评论内容
	 * 
	 * @param commentContent
	 *            评论内容
	 */
	public void setCommentContent(String commentContent)
	{
		this.commentContent = commentContent;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public Date getCommentTime()
	{
		return this.commentTime;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param commentTime
	 *            创建时间
	 */
	public void setCommentTime(Date commentTime)
	{
		this.commentTime = commentTime;
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

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public Integer getDisableMessageDays()
	{
		return disableMessageDays;
	}

	public void setDisableMessageDays(Integer disableMessageDays)
	{
		this.disableMessageDays = disableMessageDays;
	}
}
