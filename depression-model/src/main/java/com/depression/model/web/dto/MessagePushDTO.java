package com.depression.model.web.dto;

import java.util.Date;

public class MessagePushDTO {
    
	
   /* @Comment(主键) */
   private Long mpId;

  
   /* @Comment(标题) */
   private String title;

  
   /* @Comment(推送状态(0:未推送 1:正在推送 2:已推送 )) */
   private Byte pushStatus;

   /* @Comment(推送类型(0:全部 1:IM 2:PUSH)) */
   private Byte pushType;

  
   /* @Comment(点击统计) */
   private Long clickCount;

   /* @Comment(推送链接) */
   private String pushLink;

  
   /* @Comment(链接承接文字) */
   private String linkText;

  
   /* @Comment(推送时间) */
   private Date pushTime;

   
   /* @Comment(图片地址) */
   private String img;
   
   /* @Comment(图片预览地址) */
   private String viewImg;

   
   /* @Comment(是否全部用户(0:否 1:是)) */
   private Byte isAll;

  
   /* @Comment(缩略图地址) */
   private String preImg;

   /* @Comment(内容) */
   private String content;
   
   public String getPushLink() {
	return pushLink;
}

public void setPushLink(String pushLink) {
	this.pushLink = pushLink;
}

public String getLinkText() {
	return linkText;
}

public void setLinkText(String linkText) {
	this.linkText = linkText;
}

public Byte getIsAll() {
	return isAll;
}

public void setIsAll(Byte isAll) {
	this.isAll = isAll;
}

public String getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}


	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPreImg() {
		return preImg;
	}

	public void setPreImg(String preImg) {
		this.preImg = preImg;
	}

	public Long getMpId() {
		return mpId;
	}

	public void setMpId(Long mpId) {
		this.mpId = mpId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public Byte getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Byte pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Byte getPushType() {
		return pushType;
	}

	public void setPushType(Byte pushType) {
		this.pushType = pushType;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public String getViewImg() {
		return viewImg;
	}

	public void setViewImg(String viewImg) {
		this.viewImg = viewImg;
	}

   
}