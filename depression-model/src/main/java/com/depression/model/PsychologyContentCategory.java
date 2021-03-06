package com.depression.model;

import java.util.Date;

public class PsychologyContentCategory extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.pcc_id
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment() */
    private Long pccId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.name
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment(名称) */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.sort_order
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment(顺序) */
    private Integer sortOrder;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.create_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment() */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.update_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment() */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.delete_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment() */
    private Date deleteTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.is_enable
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psychology_content_category.is_delete
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    /* @Comment() */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.pcc_id
     *
     * @return the value of psychology_content_category.pcc_id
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public Long getPccId() {
        return pccId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.pcc_id
     *
     * @param pccId the value for psychology_content_category.pcc_id
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setPccId(Long pccId) {
        this.pccId = pccId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.name
     *
     * @return the value of psychology_content_category.name
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.name
     *
     * @param name the value for psychology_content_category.name
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.sort_order
     *
     * @return the value of psychology_content_category.sort_order
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.sort_order
     *
     * @param sortOrder the value for psychology_content_category.sort_order
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.create_time
     *
     * @return the value of psychology_content_category.create_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.create_time
     *
     * @param createTime the value for psychology_content_category.create_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.update_time
     *
     * @return the value of psychology_content_category.update_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.update_time
     *
     * @param updateTime the value for psychology_content_category.update_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.delete_time
     *
     * @return the value of psychology_content_category.delete_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public Date getDeleteTime() {
        return deleteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.delete_time
     *
     * @param deleteTime the value for psychology_content_category.delete_time
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.is_enable
     *
     * @return the value of psychology_content_category.is_enable
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.is_enable
     *
     * @param isEnable the value for psychology_content_category.is_enable
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psychology_content_category.is_delete
     *
     * @return the value of psychology_content_category.is_delete
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psychology_content_category.is_delete
     *
     * @param isDelete the value for psychology_content_category.is_delete
     *
     * @mbggenerated Thu Dec 15 16:14:39 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}