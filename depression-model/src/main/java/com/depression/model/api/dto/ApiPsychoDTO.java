package com.depression.model.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApiPsychoDTO{

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
    
    /* @Comment(1时是男性，2时是女性，0时是未知) */
    private Byte sex;

    /* @Comment(爱好) */
    private String hobby;

    /* @Comment(等级) */
    private Long mLevel;
    
    /* @Comment(咨询师级别) */
    private Byte pLevel;

    /* @Comment(积分) */
    private Long bonusPoint;

    /* @Comment(0 不在线，1在线，2 正在通话) */
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
    
    private String licenseName;

    /* @Comment(回应的咨询数) */
    private Integer answerCount;

    /* @Comment(被感谢次数) */
    private Integer appreciatedCount;
    
    /* @Comment(回应的咨询数增量) */
    private Integer answerCountIncrement;
	
    /* @Comment(电话咨询数增量) */
	private Integer phoneCountIncrement;

    /* @Comment(生活照) */
    private String candidPhoto;
    
    private String candidPhotoAbs;
    
    private Integer eapServiceStatus;

    /* @Comment() */
    private Byte isEnable;

    /* @Comment(简介) */
    private String profile;
    
    private String profileNoTag;
    
    private List<ApiMemberTagDTO> tags;
    
    private BigDecimal voicePrice;
    
    private Integer voiceDuration;
    
    private Integer voiceTimes;
    
    private Integer concernedNum;
    
    // 剩余数量
    private Integer surplusNum;  

	//推选价格
    private BigDecimal favourablePrice;
    //价格名目
	private String priceName;
	
	private Byte priceOpened ;
    
    // 0 已经关注 1未关注
    private Integer isConcern;
    
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
    
    /* @Comment(从业数据修改时间) */
    private Date workDataModifyTime;

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

	
	public String getLicenseName()
	{
		return licenseName;
	}

	public void setLicenseName(String licenseName)
	{
		this.licenseName = licenseName;
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

	public List<ApiMemberTagDTO> getTags()
	{
		return tags;
	}

	public void setTags(List<ApiMemberTagDTO> tags)
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

	public BigDecimal getVoicePrice()
	{
		return voicePrice;
	}

	public void setVoicePrice(BigDecimal voicePrice)
	{
		this.voicePrice = voicePrice;
	}

	public Integer getVoiceDuration()
	{
		return voiceDuration;
	}

	public void setVoiceDuration(Integer voiceDuration)
	{
		this.voiceDuration = voiceDuration;
	}

	public Integer getVoiceTimes()
	{
		return voiceTimes;
	}

	public void setVoiceTimes(Integer voiceTimes)
	{
		this.voiceTimes = voiceTimes;
	}

	public Integer getConcernedNum()
	{
		return concernedNum;
	}

	public void setConcernedNum(Integer concernedNum)
	{
		this.concernedNum = concernedNum;
	}

	public Byte getpLevel()
	{
		return pLevel;
	}

	public void setpLevel(Byte pLevel)
	{
		this.pLevel = pLevel;
	}

	public Integer getAnswerCountIncrement()
	{
		return answerCountIncrement;
	}

	public void setAnswerCountIncrement(Integer answerCountIncrement)
	{
		this.answerCountIncrement = answerCountIncrement;
	}

	public Integer getPhoneCountIncrement()
	{
		return phoneCountIncrement;
	}

	public void setPhoneCountIncrement(Integer phoneCountIncrement)
	{
		this.phoneCountIncrement = phoneCountIncrement;
	}

	public Integer getEapServiceStatus()
	{
		return eapServiceStatus;
	}

	public void setEapServiceStatus(Integer eapServiceStatus)
	{
		this.eapServiceStatus = eapServiceStatus;
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

	public Date getWorkDataModifyTime()
	{
		return workDataModifyTime;
	}

	public void setWorkDataModifyTime(Date workDataModifyTime)
	{
		this.workDataModifyTime = workDataModifyTime;
	}

	public Integer getIsConcern()
	{
		return isConcern;
	}

	public void setIsConcern(Integer isConcern)
	{
		this.isConcern = isConcern;
	}
	 public Integer getSurplusNum() {
			return surplusNum;
		}

		public void setSurplusNum(Integer surplusNum) {
			this.surplusNum = surplusNum;
		}

		public BigDecimal getFavourablePrice() {
			return favourablePrice;
		}

		public void setFavourablePrice(BigDecimal favourablePrice) {
			this.favourablePrice = favourablePrice;
		}

		public String getPriceName() {
			return priceName;
		}

		public void setPriceName(String priceName) {
			this.priceName = priceName;
		}

		public Byte getPriceOpened() {
			return priceOpened;
		}

		public void setPriceOpened(Byte priceOpened) {
			this.priceOpened = priceOpened;
		}
	
}
