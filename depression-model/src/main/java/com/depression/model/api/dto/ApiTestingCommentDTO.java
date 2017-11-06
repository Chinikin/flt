package com.depression.model.api.dto;

import java.util.Date;

import com.depression.model.TestingComment;

public class ApiTestingCommentDTO  {
	

   /* @Comment(主键) */
   private Long commentId;

  
   /* @Comment(试卷id，关联试卷表) */
   private Long testingId;

  
   /* @Comment(会员id) */
   private Long mid;

   
   /* @Comment(主键) */
   private Long testCommentId;

   
   /* @Comment(评论内容) */
   private String commentContent;

   
   /* @Comment(评论时间) */
   private Date commentTime;

   
   public Long getCommentId() {
	return commentId;
}
public void setCommentId(Long commentId) {
	this.commentId = commentId;
}
public Long getTestingId() {
	return testingId;
}
public void setTestingId(Long testingId) {
	this.testingId = testingId;
}
public Long getMid() {
	return mid;
}
public void setMid(Long mid) {
	this.mid = mid;
}
public Long getTestCommentId() {
	return testCommentId;
}
public void setTestCommentId(Long testCommentId) {
	this.testCommentId = testCommentId;
}
public String getCommentContent() {
	return commentContent;
}
public void setCommentContent(String commentContent) {
	this.commentContent = commentContent;
}
public Date getCommentTime() {
	return commentTime;
}
public void setCommentTime(Date commentTime) {
	this.commentTime = commentTime;
}
public Byte getIsEnable() {
	return isEnable;
}
public void setIsEnable(Byte isEnable) {
	this.isEnable = isEnable;
}
/* @Comment() */
   private Byte isEnable;

  

	 // 会员昵称
    private String nickname;
    //会员类型
    private Byte userType;

    // 被艾特的会员id
    private Long eitMid;
    // 被艾特的会员类型
    private Byte eitUserType;
    // 被艾特的会员昵称
    private String eitNickname;
    // 会员头像
    private String avatar;
    public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Byte getUserType() {
		return userType;
	}
	public void setUserType(Byte userType) {
		this.userType = userType;
	}
	public Long getEitMid() {
		return eitMid;
	}
	public void setEitMid(Long eitMid) {
		this.eitMid = eitMid;
	}
	public Byte getEitUserType() {
		return eitUserType;
	}
	public void setEitUserType(Byte eitUserType) {
		this.eitUserType = eitUserType;
	}
	public String getEitNickname() {
		return eitNickname;
	}
	public void setEitNickname(String eitNickname) {
		this.eitNickname = eitNickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getAvatarThumbnail() {
		return avatarThumbnail;
	}
	public void setAvatarThumbnail(String avatarThumbnail) {
		this.avatarThumbnail = avatarThumbnail;
	}
	public String getEitAvatar() {
		return eitAvatar;
	}
	public void setEitAvatar(String eitAvatar) {
		this.eitAvatar = eitAvatar;
	}
	public String getEitAvatarThumbnail() {
		return eitAvatarThumbnail;
	}
	public void setEitAvatarThumbnail(String eitAvatarThumbnail) {
		this.eitAvatarThumbnail = eitAvatarThumbnail;
	}
	public String getImAccount() {
		return imAccount;
	}
	public void setImAccount(String imAccount) {
		this.imAccount = imAccount;
	}
	public String getEitImAccount() {
		return eitImAccount;
	}
	public void setEitImAccount(String eitImAccount) {
		this.eitImAccount = eitImAccount;
	}
	// 会员头像缩略图
    private String avatarThumbnail;
    // 被艾特的会员头像
    private String eitAvatar;
    // 被艾特的会员头像缩略图
    private String eitAvatarThumbnail;
    // 会员im账号
    private String imAccount;
    // 被艾特的会员im账号
    private String eitImAccount;

	
}
