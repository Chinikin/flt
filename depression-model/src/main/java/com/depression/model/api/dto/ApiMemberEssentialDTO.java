package com.depression.model.api.dto;


public class ApiMemberEssentialDTO{

    /* @Comment(主键) */
    private Long mid;

    /* @Comment(容联账号) */
    private String imAccount;

    /* @Comment(头像) */
    private String avatar;
    
    /* @Comment(用户名) */
    private String userName;

    /* @Comment(昵称) */
    private String nickname;
    
    /* @Comment(0 匿名 1不匿名) */
    private Byte isAnony;
    
    /* @Comment(1时是男性，2时是女性，0时是未知) */
    private Byte sex;
    
    private Integer age;

    /* @Comment(咨询师级别) */
    private Byte pLevel;

    /* @Comment(用户类型: 1.普通会员 2. 咨询师) */
    private Byte userType;

    private String location;

    /* @Comment() */
    private String title;
    
    private String licenseName;

    private Integer eapServiceStatus;

    /* @Comment() */
    private Byte isEnable;

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

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	
	public Byte getIsAnony()
	{
		return isAnony;
	}

	public void setIsAnony(Byte isAnony)
	{
		this.isAnony = isAnony;
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

	public Byte getUserType()
	{
		return userType;
	}

	public void setUserType(Byte userType)
	{
		this.userType = userType;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	
	public String getLicenseName()
	{
		return licenseName;
	}

	public void setLicenseName(String licenseName)
	{
		this.licenseName = licenseName;
	}

	public Byte getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Byte isEnable)
	{
		this.isEnable = isEnable;
	}

	public Byte getpLevel()
	{
		return pLevel;
	}

	public void setpLevel(Byte pLevel)
	{
		this.pLevel = pLevel;
	}

	public Integer getEapServiceStatus()
	{
		return eapServiceStatus;
	}

	public void setEapServiceStatus(Integer eapServiceStatus)
	{
		this.eapServiceStatus = eapServiceStatus;
	}
}
