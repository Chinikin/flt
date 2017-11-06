package com.depression.model.api.vo;

import java.util.Date;

public class ApiPsychoDataVO{

    /* @Comment(主键) */
    private Long mid;

    /* @Comment() */
    private String avatarThumbnail;
    
    /* @Comment(头像) */
    private String avatar;
    
    /* @Comment(用户名) */
    private String userName;

    /* @Comment(邮箱) */
    private String email;

    /* @Comment(昵称) */
    private String nickname;
    
    /* @Comment(1时是男性，2时是女性，0时是未知) */
    private Byte sex;

    /* @Comment(爱好) */
    private String hobby;

    private String location;

    /* @Comment() */
    private String title;
    
    /* @Comment(生活照) */
    private String candidPhoto;
    
    /* @Comment(简介) */
    private String profile;
    
    private String profileNoTag;
    
    /* @Comment(微信号) */
    private String wxAccount;
    
    /* @Comment(铭言) */
    private String motto;
    
    /* @Comment(毕业学校) */
    private String school;

    /* @Comment(所学专业) */
    private String major;

    /* @Comment(学历) */
    private String degree;
   
    /* @Comment(培训经历) */
    private String trainingExperience;
    
    /* @Comment(擅长领域， json数组) */
    private String primaryDomain;
    
    /* @Comment(单位年) */
    private Byte workYears;

    /* @Comment(单位小时) */
    private Integer caseHours;
    
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

	public Byte getSex()
	{
		return sex;
	}

	public void setSex(Byte sex)
	{
		this.sex = sex;
	}

	public String getHobby()
	{
		return hobby;
	}

	public void setHobby(String hobby)
	{
		this.hobby = hobby;
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

	public String getProfile()
	{
		return profile;
	}

	public void setProfile(String profile)
	{
		this.profile = profile;
	}

	public String getProfileNoTag()
	{
		return profileNoTag;
	}

	public void setProfileNoTag(String profileNoTag)
	{
		this.profileNoTag = profileNoTag;
	}

	public String getWxAccount()
	{
		return wxAccount;
	}

	public void setWxAccount(String wxAccount)
	{
		this.wxAccount = wxAccount;
	}

	public String getMotto()
	{
		return motto;
	}

	public void setMotto(String motto)
	{
		this.motto = motto;
	}

	public String getSchool()
	{
		return school;
	}

	public void setSchool(String school)
	{
		this.school = school;
	}

	public String getMajor()
	{
		return major;
	}

	public void setMajor(String major)
	{
		this.major = major;
	}

	public String getDegree()
	{
		return degree;
	}

	public void setDegree(String degree)
	{
		this.degree = degree;
	}

	public String getTrainingExperience()
	{
		return trainingExperience;
	}

	public void setTrainingExperience(String trainingExperience)
	{
		this.trainingExperience = trainingExperience;
	}

	public String getPrimaryDomain()
	{
		return primaryDomain;
	}

	public void setPrimaryDomain(String primaryDomain)
	{
		this.primaryDomain = primaryDomain;
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
}
