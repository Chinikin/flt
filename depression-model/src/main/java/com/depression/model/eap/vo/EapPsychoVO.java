package com.depression.model.eap.vo;

import java.util.Date;

public class EapPsychoVO {

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
    
    /* @Comment(执照类型id) */
    private Long ltid;
    
    /* @Comment(1时是男性，2时是女性，0时是未知) */
    private Byte sex;
    
    /* @Comment(咨询师级别) */
    private Byte pLevel;
    
    /* @Comment(微信号) */
    private String wxAccount;
    
    /* @Comment(单位年) */
    private Byte workYears;

    /* @Comment(单位小时) */
    private Integer caseHours;
    
    /* @Comment(擅长领域) */
    private String primaryDomain;
    
    /* @Comment(处理过的资格证照) */
    private String photoCertificationDealtRel;

    /* @Comment(处理过的资格证照预览) */
    private String photoCertificationDealtPreviewRel;
    
    /* @Comment(处理过的身份证照) */
    private String photoIdentityCardDealtRel;

    /* @Comment(处理过的身份证照预览) */
    private String photoIdentityCardDealtPreviewRel;
    // 0 不需要审核入住心猫  1需要
    private Byte needAudit;
    // 企业id
    private Long eeId;
    
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

	public Long getLtid()
	{
		return ltid;
	}

	public void setLtid(Long ltid)
	{
		this.ltid = ltid;
	}

	public Byte getSex()
	{
		return sex;
	}

	public void setSex(Byte sex)
	{
		this.sex = sex;
	}

	public Byte getpLevel()
	{
		return pLevel;
	}

	public void setpLevel(Byte pLevel)
	{
		this.pLevel = pLevel;
	}

	public String getWxAccount()
	{
		return wxAccount;
	}

	public void setWxAccount(String wxAccount)
	{
		this.wxAccount = wxAccount;
	}

	public Byte getWorkYears()
	{
		return workYears;
	}

	public void setWorkYears(Byte workYears)
	{
		this.workYears = workYears;
	}

	public Integer getCaseHours()
	{
		return caseHours;
	}

	public void setCaseHours(Integer caseHours)
	{
		this.caseHours = caseHours;
	}

	public String getPrimaryDomain()
	{
		return primaryDomain;
	}

	public void setPrimaryDomain(String primaryDomain)
	{
		this.primaryDomain = primaryDomain;
	}

	public String getPhotoCertificationDealtRel()
	{
		return photoCertificationDealtRel;
	}

	public void setPhotoCertificationDealtRel(String photoCertificationDealtRel)
	{
		this.photoCertificationDealtRel = photoCertificationDealtRel;
	}

	public String getPhotoCertificationDealtPreviewRel()
	{
		return photoCertificationDealtPreviewRel;
	}

	public void setPhotoCertificationDealtPreviewRel(String photoCertificationDealtPreviewRel)
	{
		this.photoCertificationDealtPreviewRel = photoCertificationDealtPreviewRel;
	}

	public String getPhotoIdentityCardDealtRel()
	{
		return photoIdentityCardDealtRel;
	}

	public void setPhotoIdentityCardDealtRel(String photoIdentityCardDealtRel)
	{
		this.photoIdentityCardDealtRel = photoIdentityCardDealtRel;
	}

	public String getPhotoIdentityCardDealtPreviewRel()
	{
		return photoIdentityCardDealtPreviewRel;
	}

	public void setPhotoIdentityCardDealtPreviewRel(String photoIdentityCardDealtPreviewRel)
	{
		this.photoIdentityCardDealtPreviewRel = photoIdentityCardDealtPreviewRel;
	}

	public Byte getNeedAudit()
	{
		return needAudit;
	}

	public void setNeedAudit(Byte needAudit)
	{
		this.needAudit = needAudit;
	}

	public Long getEeId()
	{
		return eeId;
	}

	public void setEeId(Long eeId)
	{
		this.eeId = eeId;
	}	
}