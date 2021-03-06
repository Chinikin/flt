package com.depression.model;

import java.util.Date;

public class EapValidEnterpriseHistory extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_valid_enterprise_history.eveh_id
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    /* @Comment() */
    private Long evehId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_valid_enterprise_history.mid
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    /* @Comment(员工会员id) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_valid_enterprise_history.ee_id
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    /* @Comment(EAP企业id) */
    private Long eeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_valid_enterprise_history.create_time
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    /* @Comment() */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_valid_enterprise_history.delete_time
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    /* @Comment() */
    private Date deleteTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_valid_enterprise_history.is_enable
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column eap_valid_enterprise_history.is_delete
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    /* @Comment() */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_valid_enterprise_history.eveh_id
     *
     * @return the value of eap_valid_enterprise_history.eveh_id
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public Long getEvehId() {
        return evehId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_valid_enterprise_history.eveh_id
     *
     * @param evehId the value for eap_valid_enterprise_history.eveh_id
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public void setEvehId(Long evehId) {
        this.evehId = evehId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_valid_enterprise_history.mid
     *
     * @return the value of eap_valid_enterprise_history.mid
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_valid_enterprise_history.mid
     *
     * @param mid the value for eap_valid_enterprise_history.mid
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_valid_enterprise_history.ee_id
     *
     * @return the value of eap_valid_enterprise_history.ee_id
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public Long getEeId() {
        return eeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_valid_enterprise_history.ee_id
     *
     * @param eeId the value for eap_valid_enterprise_history.ee_id
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public void setEeId(Long eeId) {
        this.eeId = eeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_valid_enterprise_history.create_time
     *
     * @return the value of eap_valid_enterprise_history.create_time
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_valid_enterprise_history.create_time
     *
     * @param createTime the value for eap_valid_enterprise_history.create_time
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_valid_enterprise_history.delete_time
     *
     * @return the value of eap_valid_enterprise_history.delete_time
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public Date getDeleteTime() {
        return deleteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_valid_enterprise_history.delete_time
     *
     * @param deleteTime the value for eap_valid_enterprise_history.delete_time
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_valid_enterprise_history.is_enable
     *
     * @return the value of eap_valid_enterprise_history.is_enable
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_valid_enterprise_history.is_enable
     *
     * @param isEnable the value for eap_valid_enterprise_history.is_enable
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column eap_valid_enterprise_history.is_delete
     *
     * @return the value of eap_valid_enterprise_history.is_delete
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column eap_valid_enterprise_history.is_delete
     *
     * @param isDelete the value for eap_valid_enterprise_history.is_delete
     *
     * @mbggenerated Tue Dec 20 18:35:07 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}