package com.depression.model.api.vo;

public class ApiPsychoInfoVO{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.piid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(主键) */
    private Long piid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.ltid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private Long ltid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.mid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(主键) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.name
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(姓名) */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.sex
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(0 男 1女 2未知) */
    private Byte sex;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.mobile_phone
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(手机号) */
    private String mobilePhone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.wx_account
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(微信号) */
    private String wxAccount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.location
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(所在地) */
    private String location;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.title
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String title;
    
    /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column psycho_info.work_years
    *
    * @mbggenerated Thu Dec 08 10:00:02 CST 2016
    */
   /* @Comment(单位年) */
   private Byte workYears;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column psycho_info.case_hours
    *
    * @mbggenerated Thu Dec 08 10:00:02 CST 2016
    */
   /* @Comment(单位小时) */
   private Integer caseHours;
   
   /* @Comment(咨询师级别) */
   private Byte pLevel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_certification_dealt
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoCertificationDealtRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_certification_dealt_preview
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoCertificationDealtPreviewRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_certification
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoCertificationRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_certification_preview
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoCertificationPreviewRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_identity_card_dealt
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoIdentityCardDealtRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_identity_card_dealt_preview
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoIdentityCardDealtPreviewRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_identity_card
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoIdentityCardRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_identity_card_preview
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoIdentityCardPreviewRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_candid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoCandidRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.photo_candid_preview
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String photoCandidPreviewRel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.brief
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String brief;
    
    /* @Comment(信息来源) */
    private String infoChannel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.referrer
     *
     * @mbggenerated Thu Apr 13 13:06:42 CST 2017
     */
    /* @Comment(推荐人) */
    private String referrer;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.referrer_wechat
     *
     * @mbggenerated Thu Apr 13 13:06:42 CST 2017
     */
    /* @Comment(推荐人微信) */
    private String referrerWechat;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.audit_status
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment(0 未审核， 1 审核通过， 2审核不通过) */
    private Byte auditStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.nogo_reason
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private String nogoReason;
    
    private String primaryDomains;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_info.is_enable
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.piid
     *
     * @return the value of psycho_info.piid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public Long getPiid() {
        return piid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.piid
     *
     * @param piid the value for psycho_info.piid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setPiid(Long piid) {
        this.piid = piid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.ltid
     *
     * @return the value of psycho_info.ltid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public Long getLtid() {
        return ltid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.ltid
     *
     * @param ltid the value for psycho_info.ltid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setLtid(Long ltid) {
        this.ltid = ltid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.mid
     *
     * @return the value of psycho_info.mid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.mid
     *
     * @param mid the value for psycho_info.mid
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.name
     *
     * @return the value of psycho_info.name
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.name
     *
     * @param name the value for psycho_info.name
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.sex
     *
     * @return the value of psycho_info.sex
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.sex
     *
     * @param sex the value for psycho_info.sex
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.mobile_phone
     *
     * @return the value of psycho_info.mobile_phone
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.mobile_phone
     *
     * @param mobilePhone the value for psycho_info.mobile_phone
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.wx_account
     *
     * @return the value of psycho_info.wx_account
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public String getWxAccount() {
        return wxAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.wx_account
     *
     * @param wxAccount the value for psycho_info.wx_account
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setWxAccount(String wxAccount) {
        this.wxAccount = wxAccount == null ? null : wxAccount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.location
     *
     * @return the value of psycho_info.location
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.location
     *
     * @param location the value for psycho_info.location
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.title
     *
     * @return the value of psycho_info.title
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.title
     *
     * @param title the value for psycho_info.title
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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

	public Byte getpLevel()
	{
		return pLevel;
	}

	public void setpLevel(Byte pLevel)
	{
		this.pLevel = pLevel;
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

	public String getPhotoCertificationRel()
	{
		return photoCertificationRel;
	}

	public void setPhotoCertificationRel(String photoCertificationRel)
	{
		this.photoCertificationRel = photoCertificationRel;
	}

	public String getPhotoCertificationPreviewRel()
	{
		return photoCertificationPreviewRel;
	}

	public void setPhotoCertificationPreviewRel(String photoCertificationPreviewRel)
	{
		this.photoCertificationPreviewRel = photoCertificationPreviewRel;
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

	public String getPhotoIdentityCardRel()
	{
		return photoIdentityCardRel;
	}

	public void setPhotoIdentityCardRel(String photoIdentityCardRel)
	{
		this.photoIdentityCardRel = photoIdentityCardRel;
	}

	public String getPhotoIdentityCardPreviewRel()
	{
		return photoIdentityCardPreviewRel;
	}

	public void setPhotoIdentityCardPreviewRel(String photoIdentityCardPreviewRel)
	{
		this.photoIdentityCardPreviewRel = photoIdentityCardPreviewRel;
	}

	public String getPhotoCandidRel()
	{
		return photoCandidRel;
	}

	public void setPhotoCandidRel(String photoCandidRel)
	{
		this.photoCandidRel = photoCandidRel;
	}

	public String getPhotoCandidPreviewRel()
	{
		return photoCandidPreviewRel;
	}

	public void setPhotoCandidPreviewRel(String photoCandidPreviewRel)
	{
		this.photoCandidPreviewRel = photoCandidPreviewRel;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.brief
     *
     * @return the value of psycho_info.brief
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public String getBrief() {
        return brief;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.brief
     *
     * @param brief the value for psycho_info.brief
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }
    
    public String getInfoChannel()
	{
		return infoChannel;
	}

	public void setInfoChannel(String infoChannel)
	{
		this.infoChannel = infoChannel;
	}

	public String getReferrer()
	{
		return referrer;
	}

	public void setReferrer(String referrer)
	{
		this.referrer = referrer;
	}

	public String getReferrerWechat()
	{
		return referrerWechat;
	}

	public void setReferrerWechat(String referrerWechat)
	{
		this.referrerWechat = referrerWechat;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.audit_status
     *
     * @return the value of psycho_info.audit_status
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public Byte getAuditStatus() {
        return auditStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.audit_status
     *
     * @param auditStatus the value for psycho_info.audit_status
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.nogo_reason
     *
     * @return the value of psycho_info.nogo_reason
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public String getNogoReason() {
        return nogoReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.nogo_reason
     *
     * @param nogoReason the value for psycho_info.nogo_reason
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setNogoReason(String nogoReason) {
        this.nogoReason = nogoReason == null ? null : nogoReason.trim();
    }

    public String getPrimaryDomains()
	{
		return primaryDomains;
	}

	public void setPrimaryDomains(String primaryDomains)
	{
		this.primaryDomains = primaryDomains;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_info.is_enable
     *
     * @return the value of psycho_info.is_enable
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_info.is_enable
     *
     * @param isEnable the value for psycho_info.is_enable
     *
     * @mbggenerated Mon Sep 12 15:29:15 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }
}