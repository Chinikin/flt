package com.depression.model;

import java.util.Date;

public class UnreadComment extends Page
{
	private Long unreadId;
	private Long mid;
	private Long commentId;
	private Long type;
	// 0 未读 1已读
	private Byte isRead;

	public static Long TYPE_ARTICLE = 1L;
	public static Long TYPE_TESTING = 2L;
	public static Long TYPE_UPDATE_BOTTLE = 3L;
	public static Long TYPE_UPDATE_CIRCLE = 4L;
	public static Long TYPE_ADVISORY = 5L;
	
	private Long cmid;
	private String nickname;
    private Byte userType;
	private String title;
	private String avatar;
	private String avatarThumbnail;
	private Date commentTime;
	private String commentContent;
	private String parentContent;
	private Long chapterId; //文章、动态、试卷等主体的ID
    /* @Comment(0 匿名 1不匿名) */
    private Byte isAnony;
	
	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public Date getCommentTime()
	{
		return commentTime;
	}

	public void setCommentTime(Date commentTime)
	{
		this.commentTime = commentTime;
	}

	public String getCommentContent()
	{
		return commentContent;
	}

	public void setCommentContent(String commentContent)
	{
		this.commentContent = commentContent;
	}

	public String getParentContent()
	{
		return parentContent;
	}

	public void setParentContent(String parentContent)
	{
		this.parentContent = parentContent;
	}

	public Long getChapterId()
	{
		return chapterId;
	}

	public void setChapterId(Long chapterId)
	{
		this.chapterId = chapterId;
	}

	public Long getType()
	{
		return type;
	}

	public void setType(Long type)
	{
		this.type = type;
	}

	public Long getUnreadId()
	{
		return unreadId;
	}

	public void setUnreadId(Long unreadId)
	{
		this.unreadId = unreadId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Long getCommentId()
	{
		return commentId;
	}

	public void setCommentId(Long commentId)
	{
		this.commentId = commentId;
	}

	public String getAvatarThumbnail()
	{
		return avatarThumbnail;
	}

	public void setAvatarThumbnail(String avatarThumbnail)
	{
		this.avatarThumbnail = avatarThumbnail;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Byte getUserType()
	{
		return userType;
	}

	public void setUserType(Byte userType)
	{
		this.userType = userType;
	}

	public Long getCmid()
	{
		return cmid;
	}

	public void setCmid(Long cmid)
	{
		this.cmid = cmid;
	}

	public Byte getIsRead()
	{
		return isRead;
	}

	public void setIsRead(Byte isRead)
	{
		this.isRead = isRead;
	}

	public Byte getIsAnony()
	{
		return isAnony;
	}

	public void setIsAnony(Byte isAnony)
	{
		this.isAnony = isAnony;
	}
	
	
	
}
