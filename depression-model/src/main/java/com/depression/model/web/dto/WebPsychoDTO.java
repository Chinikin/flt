package com.depression.model.web.dto;

import java.util.Date;
import java.util.List;

import com.depression.model.MemberTag;

public class WebPsychoDTO{

    /* @Comment(主键) */
    private Long mid;

    /* @Comment(容联账号) */
    private String imAccount;

    /* @Comment(容联密码) */
    private String imPsw;

    /* @Comment(手机号) */
    private String mobilePhone;

    /* @Comment() */
    private String avatarThumbnail;
    
    private String avatarThumbnailAbs;

    /* @Comment(头像) */
    private String avatar;
    
    private String avatarAbs;

    /* @Comment(用户名) */
    private String userName;

    /* @Comment(邮箱) */
    private String email;

    /* @Comment(昵称) */
    private String nickname;

    /* @Comment(爱好) */
    private String hobby;

    /* @Comment(等级) */
    private Long mLevel;

    /* @Comment(积分) */
    private Long bonusPoint;

    /* @Comment(状态) */
    private Byte status;

    /* @Comment(用户类型: 1.普通会员 2. 咨询师) */
    private Byte userType;

    /* @Comment(是否临时用户：0会员，1游客) */
    private Byte isTemp;

    /* @Comment(注册时间) */
    private Date regTime;
    
    private String location;

    /* @Comment(修改时间) */
    private Date modifyTime;

    /* @Comment() */
    private String title;

    /* @Comment(回应的咨询数) */
    private Integer answerCount;

    /* @Comment(被感谢次数) */
    private Integer appreciatedCount;
    
    /* @Comment(真实电话咨询增量) */
    private Integer phoneCount;
    
    /* @Comment(电话咨询增量) */
    private Integer phoneCountIncrement;
    
    /* @Comment(显示电话咨询增量) */
    private Integer phoneCountShow;    

    /* @Comment(生活照) */
    private String candidPhoto;
    
    private String candidPhotoAbs;

    /* @Comment() */
    private Byte isEnable;

    /* @Comment(简介) */
    private String profile;
    
    private String profileNoTag;
    
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
    
    /* @Comment(处理过的资格证照) */
    private String photoCertificationDealtAbs;

    /* @Comment(处理过的资格证照预览) */
    private String photoCertificationDealtPreviewRel;

    /* @Comment(处理过的资格证照预览) */
    private String photoCertificationDealtPreviewAbs;
    
    /* @Comment(处理过的身份证照) */
    private String photoIdentityCardDealtRel;
    
    /* @Comment(处理过的身份证照) */
    private String photoIdentityCardDealtAbs;

    /* @Comment(处理过的身份证照预览) */
    private String photoIdentityCardDealtPreviewRel;
    
    /* @Comment(处理过的身份证照预览) */
    private String photoIdentityCardDealtPreviewAbs;
    
    private List<WebMemberTagDTO> tags;
    
    /* @Comment(是否心猫审核, 0 已审核 1未审核) */
    private Byte isAudited;

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getImAccount()
	{
		return imAccount;
	}

	public void setImAccount(String imAccount)
	{
		this.imAccount = imAccount;
	}

	public String getImPsw()
	{
		return imPsw;
	}

	public void setImPsw(String imPsw)
	{
		this.imPsw = imPsw;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public String getAvatarThumbnail()
	{
		return avatarThumbnail;
	}

	public void setAvatarThumbnail(String avatarThumbnail)
	{
		this.avatarThumbnail = avatarThumbnail;
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

	public Long getBonusPoint()
	{
		return bonusPoint;
	}

	public void setBonusPoint(Long bonusPoint)
	{
		this.bonusPoint = bonusPoint;
	}

	public Byte getStatus()
	{
		return status;
	}

	public void setStatus(Byte status)
	{
		this.status = status;
	}

	public Byte getUserType()
	{
		return userType;
	}

	public void setUserType(Byte userType)
	{
		this.userType = userType;
	}

	public Byte getIsTemp()
	{
		return isTemp;
	}

	public void setIsTemp(Byte isTemp)
	{
		this.isTemp = isTemp;
	}

	public Date getRegTime()
	{
		return regTime;
	}

	public void setRegTime(Date regTime)
	{
		this.regTime = regTime;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Integer getAnswerCount()
	{
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount)
	{
		this.answerCount = answerCount;
	}

	public Integer getAppreciatedCount()
	{
		return appreciatedCount;
	}

	public void setAppreciatedCount(Integer appreciatedCount)
	{
		this.appreciatedCount = appreciatedCount;
	}
	
	public Integer getPhoneCountIncrement()
	{
		return phoneCountIncrement;
	}

	public void setPhoneCountIncrement(Integer phoneCountIncrement)
	{
		this.phoneCountIncrement = phoneCountIncrement;
	}

	public Integer getPhoneCount()
	{
		return phoneCount;
	}

	public void setPhoneCount(Integer phoneCount)
	{
		this.phoneCount = phoneCount;
	}

	public Integer getPhoneCountShow()
	{
		return phoneCountShow;
	}

	public void setPhoneCountShow(Integer phoneCountShow)
	{
		this.phoneCountShow = phoneCountShow;
	}

	public String getCandidPhoto()
	{
		return candidPhoto;
	}

	public void setCandidPhoto(String candidPhoto)
	{
		this.candidPhoto = candidPhoto;
	}

	public Byte getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Byte isEnable)
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

	public List<WebMemberTagDTO> getTags()
	{
		return tags;
	}

	public void setTags(List<WebMemberTagDTO> tags)
	{
		this.tags = tags;
	}

	public String getAvatarThumbnailAbs()
	{
		return avatarThumbnailAbs;
	}

	public void setAvatarThumbnailAbs(String avatarThumbnailAbs)
	{
		this.avatarThumbnailAbs = avatarThumbnailAbs;
	}

	public String getAvatarAbs()
	{
		return avatarAbs;
	}

	public void setAvatarAbs(String avatarAbs)
	{
		this.avatarAbs = avatarAbs;
	}

	public String getCandidPhotoAbs()
	{
		return candidPhotoAbs;
	}

	public void setCandidPhotoAbs(String candidPhotoAbs)
	{
		this.candidPhotoAbs = candidPhotoAbs;
	}

	public String getProfileNoTag()
	{
		return profileNoTag;
	}

	public void setProfileNoTag(String profileNoTag)
	{
		this.profileNoTag = profileNoTag;
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

	public String getPhotoCertificationDealtAbs()
	{
		return photoCertificationDealtAbs;
	}

	public void setPhotoCertificationDealtAbs(String photoCertificationDealtAbs)
	{
		this.photoCertificationDealtAbs = photoCertificationDealtAbs;
	}

	public String getPhotoCertificationDealtPreviewAbs()
	{
		return photoCertificationDealtPreviewAbs;
	}

	public void setPhotoCertificationDealtPreviewAbs(String photoCertificationDealtPreviewAbs)
	{
		this.photoCertificationDealtPreviewAbs = photoCertificationDealtPreviewAbs;
	}

	public String getPhotoIdentityCardDealtAbs()
	{
		return photoIdentityCardDealtAbs;
	}

	public void setPhotoIdentityCardDealtAbs(String photoIdentityCardDealtAbs)
	{
		this.photoIdentityCardDealtAbs = photoIdentityCardDealtAbs;
	}

	public String getPhotoIdentityCardDealtPreviewAbs()
	{
		return photoIdentityCardDealtPreviewAbs;
	}

	public void setPhotoIdentityCardDealtPreviewAbs(String photoIdentityCardDealtPreviewAbs)
	{
		this.photoIdentityCardDealtPreviewAbs = photoIdentityCardDealtPreviewAbs;
	}

	public Byte getIsAudited()
	{
		return isAudited;
	}

	public void setIsAudited(Byte isAudited)
	{
		this.isAudited = isAudited;
	}
}