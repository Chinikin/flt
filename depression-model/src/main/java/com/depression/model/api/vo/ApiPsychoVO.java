package com.depression.model.api.vo;

import java.util.Date;

public class ApiPsychoVO {

    /* @Comment(主键) */
    private Long mid;

    /* @Comment(手机号) */
    private String mobilePhone;

    /* @Comment(头像) */
    private String avatar;

    /* @Comment() */
    private String avatarThumbnail;
    
    /* @Comment(用户名) */
    private String userName;

    /* @Comment(密码) */
    private String userPassword;

    /* @Comment(邮箱) */
    private String email;

    /* @Comment(昵称) */
    private String nickname;

    /* @Comment(爱好) */
    private String hobby;

    /* @Comment(等级) */
    private Long mLevel;

    /* @Comment(状态) */
    private Byte status;

    /* @Comment() */
    private String title;

    /* @Comment(生活照) */
    private String candidPhoto;

    /* @Comment() */
    private Integer isEnable;

    /* @Comment(简介) */
    private String profile;

    private String location;
    
	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserPassword()
	{
		return userPassword;
	}

	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getHobby()
	{
		return hobby;
	}

	public void setHobby(String hobby)
	{
		this.hobby = hobby;
	}

	public Long getmLevel()
	{
		return mLevel;
	}

	public void setmLevel(Long mLevel)
	{
		this.mLevel = mLevel;
	}

	public Byte getStatus()
	{
		return status;
	}

	public void setStatus(Byte status)
	{
		this.status = status;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getCandidPhoto()
	{
		return candidPhoto;
	}

	public void setCandidPhoto(String candidPhoto)
	{
		this.candidPhoto = candidPhoto;
	}

	public Integer getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Integer isEnable)
	{
		this.isEnable = isEnable;
	}

	public String getProfile()
	{
		return profile;
	}

	public void setProfile(String profile)
	{
		this.profile = profile;
	}

	public String getAvatarThumbnail()
	{
		return avatarThumbnail;
	}

	public void setAvatarThumbnail(String avatarThumbnail)
	{
		this.avatarThumbnail = avatarThumbnail;
	}
	
}