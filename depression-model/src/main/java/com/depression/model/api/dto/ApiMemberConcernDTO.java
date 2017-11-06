package com.depression.model.api.dto;


public class ApiMemberConcernDTO {
	
	private Long mid;
	
    private String imAccount;
	
    private String nickname;
    
    private String avatarAbs;
    
    private String avatarThumbnailAbs;
    
    private Byte userType;
    
    private String location;
    
    private String title;
    
    /* @Comment(1时是男性，2时是女性，0时是未知) */
    private Byte sex;
    
    private Integer age;
    
    private String licenseName;
    // 0 已经关注 1未关注
    private Integer isConcern;
    
	private Byte isRead;

    public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getImAccount()
	{
		return imAccount;
	}

	public void setImAccount(String imAccount)
	{
		this.imAccount = imAccount;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	
	public String getAvatarAbs()
	{
		return avatarAbs;
	}

	public void setAvatarAbs(String avatarAbs)
	{
		this.avatarAbs = avatarAbs;
	}

	public String getAvatarThumbnailAbs()
	{
		return avatarThumbnailAbs;
	}

	public void setAvatarThumbnailAbs(String avatarThumbnailAbs)
	{
		this.avatarThumbnailAbs = avatarThumbnailAbs;
	}

	public Byte getUserType()
	{
		return userType;
	}

	public void setUserType(Byte userType)
	{
		this.userType = userType;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Byte getSex()
	{
		return sex;
	}

	public void setSex(Byte sex)
	{
		this.sex = sex;
	}

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}

	public String getLicenseName()
	{
		return licenseName;
	}

	public void setLicenseName(String licenseName)
	{
		this.licenseName = licenseName;
	}

	public Integer getIsConcern()
	{
		return isConcern;
	}

	public void setIsConcern(Integer isConcern)
	{
		this.isConcern = isConcern;
	}

	public Byte getIsRead()
	{
		return isRead;
	}

	public void setIsRead(Byte isRead)
	{
		this.isRead = isRead;
	}
}