package com.depression.model;

import java.util.Date;

public class PersonalityTestResultDesc extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.ptrd_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(主键) */
    private Long ptrdId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.type
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(D，I，S，C四种人格) */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.pic
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(图片) */
    private String pic;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.create_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(创建时间) */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.modify_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(修改时间) */
    private Date modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.is_enable
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(0启用，1禁用) */
    private Byte isEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.is_delete
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(0正常，1删除) */
    private Byte isDelete;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column personality_test_result_desc.descp
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    /* @Comment(描述) */
    private String descp;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.ptrd_id
     *
     * @return the value of personality_test_result_desc.ptrd_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Long getPtrdId() {
        return ptrdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.ptrd_id
     *
     * @param ptrdId the value for personality_test_result_desc.ptrd_id
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setPtrdId(Long ptrdId) {
        this.ptrdId = ptrdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.type
     *
     * @return the value of personality_test_result_desc.type
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.type
     *
     * @param type the value for personality_test_result_desc.type
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.pic
     *
     * @return the value of personality_test_result_desc.pic
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public String getPic() {
        return pic;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.pic
     *
     * @param pic the value for personality_test_result_desc.pic
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.create_time
     *
     * @return the value of personality_test_result_desc.create_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.create_time
     *
     * @param createTime the value for personality_test_result_desc.create_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.modify_time
     *
     * @return the value of personality_test_result_desc.modify_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.modify_time
     *
     * @param modifyTime the value for personality_test_result_desc.modify_time
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.is_enable
     *
     * @return the value of personality_test_result_desc.is_enable
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.is_enable
     *
     * @param isEnable the value for personality_test_result_desc.is_enable
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.is_delete
     *
     * @return the value of personality_test_result_desc.is_delete
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.is_delete
     *
     * @param isDelete the value for personality_test_result_desc.is_delete
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column personality_test_result_desc.descp
     *
     * @return the value of personality_test_result_desc.descp
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public String getDescp() {
        return descp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column personality_test_result_desc.descp
     *
     * @param descp the value for personality_test_result_desc.descp
     *
     * @mbggenerated Thu Aug 04 14:18:29 CST 2016
     */
    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }
}