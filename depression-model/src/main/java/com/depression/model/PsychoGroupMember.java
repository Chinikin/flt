package com.depression.model;

import java.math.BigDecimal;
import java.util.Date;

public class PsychoGroupMember extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.pgm_id
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment() */
    private Long pgmId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.pg_id
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(咨询师组id) */
    private Long pgId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.mid
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(咨询师id) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.statistical_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(统计时间) */
    private Date statisticalTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.eap_advisory_duration
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(EAP咨询时长) */
    private Long eapAdvisoryDuration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.eap_advisory_times
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(EAP咨询单数) */
    private Integer eapAdvisoryTimes;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.total_advisory_duration
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(总咨询时长) */
    private Long totalAdvisoryDuration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.total_advisory_times
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(总咨询单数) */
    private Integer totalAdvisoryTimes;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.eap_score
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(EAP用户评分) */
    private BigDecimal eapScore;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.common_score
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment(普通用户评分) */
    private BigDecimal commonScore;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.create_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment() */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.update_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment() */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.delete_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment() */
    private Date deleteTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.is_enable
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group_member.is_delete
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    /* @Comment() */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.pgm_id
     *
     * @return the value of psycho_group_member.pgm_id
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Long getPgmId() {
        return pgmId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.pgm_id
     *
     * @param pgmId the value for psycho_group_member.pgm_id
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setPgmId(Long pgmId) {
        this.pgmId = pgmId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.pg_id
     *
     * @return the value of psycho_group_member.pg_id
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Long getPgId() {
        return pgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.pg_id
     *
     * @param pgId the value for psycho_group_member.pg_id
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setPgId(Long pgId) {
        this.pgId = pgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.mid
     *
     * @return the value of psycho_group_member.mid
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.mid
     *
     * @param mid the value for psycho_group_member.mid
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.statistical_time
     *
     * @return the value of psycho_group_member.statistical_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Date getStatisticalTime() {
        return statisticalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.statistical_time
     *
     * @param statisticalTime the value for psycho_group_member.statistical_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setStatisticalTime(Date statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.eap_advisory_duration
     *
     * @return the value of psycho_group_member.eap_advisory_duration
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Long getEapAdvisoryDuration() {
        return eapAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.eap_advisory_duration
     *
     * @param eapAdvisoryDuration the value for psycho_group_member.eap_advisory_duration
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setEapAdvisoryDuration(Long eapAdvisoryDuration) {
        this.eapAdvisoryDuration = eapAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.eap_advisory_times
     *
     * @return the value of psycho_group_member.eap_advisory_times
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Integer getEapAdvisoryTimes() {
        return eapAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.eap_advisory_times
     *
     * @param eapAdvisoryTimes the value for psycho_group_member.eap_advisory_times
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setEapAdvisoryTimes(Integer eapAdvisoryTimes) {
        this.eapAdvisoryTimes = eapAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.total_advisory_duration
     *
     * @return the value of psycho_group_member.total_advisory_duration
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Long getTotalAdvisoryDuration() {
        return totalAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.total_advisory_duration
     *
     * @param totalAdvisoryDuration the value for psycho_group_member.total_advisory_duration
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setTotalAdvisoryDuration(Long totalAdvisoryDuration) {
        this.totalAdvisoryDuration = totalAdvisoryDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.total_advisory_times
     *
     * @return the value of psycho_group_member.total_advisory_times
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Integer getTotalAdvisoryTimes() {
        return totalAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.total_advisory_times
     *
     * @param totalAdvisoryTimes the value for psycho_group_member.total_advisory_times
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setTotalAdvisoryTimes(Integer totalAdvisoryTimes) {
        this.totalAdvisoryTimes = totalAdvisoryTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.eap_score
     *
     * @return the value of psycho_group_member.eap_score
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public BigDecimal getEapScore() {
        return eapScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.eap_score
     *
     * @param eapScore the value for psycho_group_member.eap_score
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setEapScore(BigDecimal eapScore) {
        this.eapScore = eapScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.common_score
     *
     * @return the value of psycho_group_member.common_score
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public BigDecimal getCommonScore() {
        return commonScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.common_score
     *
     * @param commonScore the value for psycho_group_member.common_score
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setCommonScore(BigDecimal commonScore) {
        this.commonScore = commonScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.create_time
     *
     * @return the value of psycho_group_member.create_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.create_time
     *
     * @param createTime the value for psycho_group_member.create_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.update_time
     *
     * @return the value of psycho_group_member.update_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.update_time
     *
     * @param updateTime the value for psycho_group_member.update_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.delete_time
     *
     * @return the value of psycho_group_member.delete_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Date getDeleteTime() {
        return deleteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.delete_time
     *
     * @param deleteTime the value for psycho_group_member.delete_time
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.is_enable
     *
     * @return the value of psycho_group_member.is_enable
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.is_enable
     *
     * @param isEnable the value for psycho_group_member.is_enable
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group_member.is_delete
     *
     * @return the value of psycho_group_member.is_delete
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group_member.is_delete
     *
     * @param isDelete the value for psycho_group_member.is_delete
     *
     * @mbggenerated Thu Dec 15 16:13:57 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}