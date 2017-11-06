package com.depression.model.api.dto;

import java.util.Date;

/**
 * 会员动态(member_update)
 * 
 * 
 * @version 1.0.0 2016-05-05
 */
public class ApiMemberUpdateDTO {

	/** 动态id */
	private Long updateId;

	/** 会员id */
	private Long mid;
	
    private String avatarThumbnailAbs;
    
    private String nickname;

	/** 内容 */
	private String content;

	/** 拥抱（点赞）数量 */
	private Long embraceNum;
	
	private Long commentNum;

	/** 发表位置 */
	private String writeLocation;

	/** 创建时间 */
	private Date createTime;

	/** 0 不匿名发布 1匿名发布 */
	private Integer updateType;

	/**
	 * 获取动态id
	 * 
	 * @return 动态id
	 */
	public Long getUpdateId()
	{
		return this.updateId;
	}

	/**
	 * 设置动态id
	 * 
	 * @param updateId
	 *            动态id
	 */
	public void setUpdateId(Long updateId)
	{
		this.updateId = updateId;
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
	 * 获取拥抱（点赞）数量
	 * 
	 * @return 拥抱（点赞）数量
	 */
	public Long getEmbraceNum()
	{
		return this.embraceNum;
	}

	/**
	 * 设置拥抱（点赞）数量
	 * 
	 * @param embraceNum
	 *            拥抱（点赞）数量
	 */
	public void setEmbraceNum(Long embraceNum)
	{
		this.embraceNum = embraceNum;
	}

	/**
	 * 获取发表位置
	 * 
	 * @return 发表位置
	 */
	public String getWriteLocation()
	{
		return this.writeLocation;
	}

	/**
	 * 设置发表位置
	 * 
	 * @param writeLocation
	 *            发表位置
	 */
	public void setWriteLocation(String writeLocation)
	{
		this.writeLocation = writeLocation;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public Date getCreateTime()
	{
		return this.createTime;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Integer getUpdateType()
	{
		return updateType;
	}

	public void setUpdateType(Integer updateType)
	{
		this.updateType = updateType;
	}

	public String getAvatarThumbnailAbs()
	{
		return avatarThumbnailAbs;
	}

	public void setAvatarThumbnailAbs(String avatarThumbnailAbs)
	{
		this.avatarThumbnailAbs = avatarThumbnailAbs;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public Long getCommentNum()
	{
		return commentNum;
	}

	public void setCommentNum(Long commentNum)
	{
		this.commentNum = commentNum;
	}

}