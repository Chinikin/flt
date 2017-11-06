package com.depression.model.api.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ApiEapEnterpriseDTO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_enterprise.ee_id
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    /* @Comment() */
    private Long eeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_enterprise.name
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    /* @Comment(名称) */
    private String name;
    
    /* @Comment(类型 0 学校 1企业) */
    private Integer type;

    /* @Comment(logo) */
    private String logo;

    /* @Comment(logo缩略图) */
    private String logoPreview;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_enterprise.service_start_date
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    /* @Comment(服务开始日期) */
    private Date serviceStartDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_enterprise.service_end_date
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    /* @Comment(服务结束日期) */
    private Date serviceEndDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_enterprise.is_enable
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_enterprise.ee_id
     *
     * @return the value of eap_enterprise.ee_id
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public Long getEeId() {
        return eeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_enterprise.ee_id
     *
     * @param eeId the value for eap_enterprise.ee_id
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public void setEeId(Long eeId) {
        this.eeId = eeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_enterprise.name
     *
     * @return the value of eap_enterprise.name
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_enterprise.name
     *
     * @param name the value for eap_enterprise.name
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_enterprise.service_start_date
     *
     * @return the value of eap_enterprise.service_start_date
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public Date getServiceStartDate() {
        return serviceStartDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_enterprise.service_start_date
     *
     * @param serviceStartDate the value for eap_enterprise.service_start_date
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public void setServiceStartDate(Date serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_enterprise.service_end_date
     *
     * @return the value of eap_enterprise.service_end_date
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public Date getServiceEndDate() {
        return serviceEndDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_enterprise.service_end_date
     *
     * @param serviceEndDate the value for eap_enterprise.service_end_date
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public void setServiceEndDate(Date serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    
    public String getLogo()
	{
		return logo;
	}

	public void setLogo(String logo)
	{
		this.logo = logo;
	}

	public String getLogoPreview()
	{
		return logoPreview;
	}

	public void setLogoPreview(String logoPreview)
	{
		this.logoPreview = logoPreview;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_enterprise.is_enable
     *
     * @return the value of eap_enterprise.is_enable
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_enterprise.is_enable
     *
     * @param isEnable the value for eap_enterprise.is_enable
     *
     * @mbggenerated Mon Dec 19 16:04:50 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }
}