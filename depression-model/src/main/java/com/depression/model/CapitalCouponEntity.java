package com.depression.model;

import java.math.BigDecimal;
import java.util.Date;

public class CapitalCouponEntity extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_coupon.cid
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment(主键) */
    private Long dbid;
    
    /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column capital_discount_bestowal.mid
    *
    * @mbggenerated Sat Aug 27 14:35:49 CST 2016
    */
    /* @Comment(会员外键) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_coupon.name
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment() */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_coupon.type
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment(0 现金抵扣，1 折扣) */
    private Byte type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_coupon.discount
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment(金额，或者折扣率（0-100）) */
    private BigDecimal discount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_coupon.state
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment() */
    private String state;

    /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column capital_discount_bestowal.bestowal_time
    *
    * @mbggenerated Sat Aug 27 14:35:49 CST 2016
    */
   /* @Comment(发放时间) */
   private Date bestowalTime;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column capital_discount_bestowal.validity_begin_time
    *
    * @mbggenerated Sat Aug 27 14:35:49 CST 2016
    */
   /* @Comment() */
   private Date validityBeginTime;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column capital_discount_bestowal.validity_end_time
    *
    * @mbggenerated Sat Aug 27 14:35:49 CST 2016
    */
   /* @Comment() */
   private Date validityEndTime;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column capital_discount_bestowal.status
    *
    * @mbggenerated Sat Aug 27 14:35:49 CST 2016
    */
   /* @Comment(0 未使用，1 已使用) */
   private Byte status;
    
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_coupon.is_enable
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    public Long getDbid()
	{
		return dbid;
	}

	public void setDbid(Long dbid)
	{
		this.dbid = dbid;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Date getBestowalTime()
	{
		return bestowalTime;
	}

	public void setBestowalTime(Date bestowalTime)
	{
		this.bestowalTime = bestowalTime;
	}

	public Date getValidityBeginTime()
	{
		return validityBeginTime;
	}

	public void setValidityBeginTime(Date validityBeginTime)
	{
		this.validityBeginTime = validityBeginTime;
	}

	public Date getValidityEndTime()
	{
		return validityEndTime;
	}

	public void setValidityEndTime(Date validityEndTime)
	{
		this.validityEndTime = validityEndTime;
	}

	public Byte getStatus()
	{
		return status;
	}

	public void setStatus(Byte status)
	{
		this.status = status;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_coupon.name
     *
     * @return the value of capital_coupon.name
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_coupon.name
     *
     * @param name the value for capital_coupon.name
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_coupon.type
     *
     * @return the value of capital_coupon.type
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_coupon.type
     *
     * @param type the value for capital_coupon.type
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_coupon.discount
     *
     * @return the value of capital_coupon.discount
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_coupon.discount
     *
     * @param discount the value for capital_coupon.discount
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_coupon.state
     *
     * @return the value of capital_coupon.state
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_coupon.state
     *
     * @param state the value for capital_coupon.state
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_coupon.is_enable
     *
     * @return the value of capital_coupon.is_enable
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_coupon.is_enable
     *
     * @param isEnable the value for capital_coupon.is_enable
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

}