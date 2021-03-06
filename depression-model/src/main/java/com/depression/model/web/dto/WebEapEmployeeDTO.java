package com.depression.model.web.dto;

import java.util.Date;

public class WebEapEmployeeDTO{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.eem_id
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment() */
    private Long eemId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.ee_id
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(企业id) */
    private Long eeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.name
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(姓名) */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.phone_num
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(手机号码) */
    private String phoneNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.number
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(学/工号) */
    private String number;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.statistical_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(统计时间) */
    private Date statisticalTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.eap_advisory_duration
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(EAP咨询时长) */
    private Long eapAdvisoryDuration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.eap_advisory_times
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(EAP咨询单数) */
    private Integer eapAdvisoryTimes;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.total_advisory_duration
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(总咨询时长) */
    private Long totalAdvisoryDuration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.total_advisory_times
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment(总咨询单数) */
    private Integer totalAdvisoryTimes;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.create_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment() */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.update_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment() */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_employee.is_enable
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.eem_id
     *
     * @return the value of eap_employee.eem_id
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Long getEemId() {
        return eemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.eem_id
     *
     * @param eemId the value for eap_employee.eem_id
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setEemId(Long eemId) {
        this.eemId = eemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.ee_id
     *
     * @return the value of eap_employee.ee_id
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Long getEeId() {
        return eeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.ee_id
     *
     * @param eeId the value for eap_employee.ee_id
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setEeId(Long eeId) {
        this.eeId = eeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.name
     *
     * @return the value of eap_employee.name
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.name
     *
     * @param name the value for eap_employee.name
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.phone_num
     *
     * @return the value of eap_employee.phone_num
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.phone_num
     *
     * @param phoneNum the value for eap_employee.phone_num
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.number
     *
     * @return the value of eap_employee.number
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public String getNumber() {
        return number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.number
     *
     * @param number the value for eap_employee.number
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.statistical_time
     *
     * @return the value of eap_employee.statistical_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Date getStatisticalTime() {
        return statisticalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.statistical_time
     *
     * @param statisticalTime the value for eap_employee.statistical_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setStatisticalTime(Date statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.eap_advisory_duration
     *
     * @return the value of eap_employee.eap_advisory_duration
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Long getEapAdvisoryDuration() {
        return eapAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.eap_advisory_duration
     *
     * @param eapAdvisoryDuration the value for eap_employee.eap_advisory_duration
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setEapAdvisoryDuration(Long eapAdvisoryDuration) {
        this.eapAdvisoryDuration = eapAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.eap_advisory_times
     *
     * @return the value of eap_employee.eap_advisory_times
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Integer getEapAdvisoryTimes() {
        return eapAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.eap_advisory_times
     *
     * @param eapAdvisoryTimes the value for eap_employee.eap_advisory_times
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setEapAdvisoryTimes(Integer eapAdvisoryTimes) {
        this.eapAdvisoryTimes = eapAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.total_advisory_duration
     *
     * @return the value of eap_employee.total_advisory_duration
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Long getTotalAdvisoryDuration() {
        return totalAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.total_advisory_duration
     *
     * @param totalAdvisoryDuration the value for eap_employee.total_advisory_duration
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setTotalAdvisoryDuration(Long totalAdvisoryDuration) {
        this.totalAdvisoryDuration = totalAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.total_advisory_times
     *
     * @return the value of eap_employee.total_advisory_times
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Integer getTotalAdvisoryTimes() {
        return totalAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.total_advisory_times
     *
     * @param totalAdvisoryTimes the value for eap_employee.total_advisory_times
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setTotalAdvisoryTimes(Integer totalAdvisoryTimes) {
        this.totalAdvisoryTimes = totalAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.create_time
     *
     * @return the value of eap_employee.create_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.create_time
     *
     * @param createTime the value for eap_employee.create_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.update_time
     *
     * @return the value of eap_employee.update_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.update_time
     *
     * @param updateTime the value for eap_employee.update_time
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_employee.is_enable
     *
     * @return the value of eap_employee.is_enable
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_employee.is_enable
     *
     * @param isEnable the value for eap_employee.is_enable
     *
     * @mbggenerated Fri Dec 16 17:09:30 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }
}