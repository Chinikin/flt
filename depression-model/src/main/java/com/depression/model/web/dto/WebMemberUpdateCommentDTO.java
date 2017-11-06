package com.depression.model.web.dto;

import java.util.Date;

import com.depression.model.Page;

/**
 * (会员类型为咨询师)对会员咨询的回复支持盖楼模式，对回复进行回复(member_advisory_comment)
 * 
 */
public class WebMemberUpdateCommentDTO
{
	/** 主键 */
	private Long commentId;

	/** 会员id */
	private Long mid;
	
	/** 动态ID，关联动态表 */
	private Long updateId;

	/** 父级回复，指向它的父结点，就是另一条回复的id */
	private Long parentId;

	/** 评论内容 */
	private String commentContent;

	/** 创建时间 */
	private Date commentTime;

	/** 咨询师昵称 */
	private String nickname;
	
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

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public Long getUpdateId()
	{
		return updateId;
	}

	public void setUpdateId(Long updateId)
	{
		this.updateId = updateId;
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
