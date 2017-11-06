package com.depression.model.api.dto;

import java.math.BigDecimal;

public class ApiServiceGoodsDTO{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.sgid
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment() */
    private Long sgid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.mid
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment(服务者id) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.name
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment(商品名称) */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.description
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment() */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.type
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment(0  实时语音咨询，1 实时语音倾述) */
    private Byte type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.duration
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment(咨询的时间长度) */
    private Integer duration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.price
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment() */
    private BigDecimal price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.times
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment(服务次数) */
    private Integer times;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_goods.is_enable
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.sgid
     *
     * @return the value of service_goods.sgid
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public Long getSgid() {
        return sgid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.sgid
     *
     * @param sgid the value for service_goods.sgid
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.mid
     *
     * @return the value of service_goods.mid
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.mid
     *
     * @param mid the value for service_goods.mid
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.name
     *
     * @return the value of service_goods.name
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.name
     *
     * @param name the value for service_goods.name
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.description
     *
     * @return the value of service_goods.description
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.description
     *
     * @param description the value for service_goods.description
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.type
     *
     * @return the value of service_goods.type
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.type
     *
     * @param type the value for service_goods.type
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.duration
     *
     * @return the value of service_goods.duration
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.duration
     *
     * @param duration the value for service_goods.duration
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.price
     *
     * @return the value of service_goods.price
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.price
     *
     * @param price the value for service_goods.price
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.times
     *
     * @return the value of service_goods.times
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public Integer getTimes() {
        return times;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.times
     *
     * @param times the value for service_goods.times
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setTimes(Integer times) {
        this.times = times;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_goods.is_enable
     *
     * @return the value of service_goods.is_enable
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_goods.is_enable
     *
     * @param isEnable the value for service_goods.is_enable
     *
     * @mbggenerated Sat Sep 10 15:13:24 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }
}