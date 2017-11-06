package com.depression.model;

import java.util.Date;

public class PersonalityTestMemberMapping extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_member_mapping.mapping_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(主键) */
    private Long mappingId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_member_mapping.mid
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment() */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_member_mapping.ptrd_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment() */
    private Long ptrdId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_member_mapping.create_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment() */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_member_mapping.modify_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment() */
    private Date modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_member_mapping.is_enable
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(0启用，1禁用) */
    private Byte isEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_member_mapping.is_delete
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(0正常，1删除) */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_member_mapping.mapping_id
     *
     * @return the value of personality_test_member_mapping.mapping_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Long getMappingId() {
        return mappingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_member_mapping.mapping_id
     *
     * @param mappingId the value for personality_test_member_mapping.mapping_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_member_mapping.mid
     *
     * @return the value of personality_test_member_mapping.mid
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_member_mapping.mid
     *
     * @param mid the value for personality_test_member_mapping.mid
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_member_mapping.ptrd_id
     *
     * @return the value of personality_test_member_mapping.ptrd_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Long getPtrdId() {
        return ptrdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_member_mapping.ptrd_id
     *
     * @param ptrdId the value for personality_test_member_mapping.ptrd_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setPtrdId(Long ptrdId) {
        this.ptrdId = ptrdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_member_mapping.create_time
     *
     * @return the value of personality_test_member_mapping.create_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_member_mapping.create_time
     *
     * @param createTime the value for personality_test_member_mapping.create_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_member_mapping.modify_time
     *
     * @return the value of personality_test_member_mapping.modify_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_member_mapping.modify_time
     *
     * @param modifyTime the value for personality_test_member_mapping.modify_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_member_mapping.is_enable
     *
     * @return the value of personality_test_member_mapping.is_enable
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_member_mapping.is_enable
     *
     * @param isEnable the value for personality_test_member_mapping.is_enable
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_member_mapping.is_delete
     *
     * @return the value of personality_test_member_mapping.is_delete
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_member_mapping.is_delete
     *
     * @param isDelete the value for personality_test_member_mapping.is_delete
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}