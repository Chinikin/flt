package com.depression.model;

import java.util.Date;

public class UbSearchWords extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ub_search_words.usk_id
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    /* @Comment(主键) */
    private Long uskId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ub_search_words.mid
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    /* @Comment(用户外键) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ub_search_words.words
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    /* @Comment(搜索词) */
    private String words;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ub_search_words.create_time
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    /* @Comment(创建时间) */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ub_search_words.modify_time
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    /* @Comment(修改时间) */
    private Date modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ub_search_words.is_delete
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    /* @Comment(默认0:不删除  1：删除) */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ub_search_words.usk_id
     *
     * @return the value of ub_search_words.usk_id
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public Long getUskId() {
        return uskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ub_search_words.usk_id
     *
     * @param uskId the value for ub_search_words.usk_id
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public void setUskId(Long uskId) {
        this.uskId = uskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ub_search_words.mid
     *
     * @return the value of ub_search_words.mid
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ub_search_words.mid
     *
     * @param mid the value for ub_search_words.mid
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ub_search_words.words
     *
     * @return the value of ub_search_words.words
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public String getWords() {
        return words;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ub_search_words.words
     *
     * @param words the value for ub_search_words.words
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public void setWords(String words) {
        this.words = words == null ? null : words.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ub_search_words.create_time
     *
     * @return the value of ub_search_words.create_time
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ub_search_words.create_time
     *
     * @param createTime the value for ub_search_words.create_time
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ub_search_words.modify_time
     *
     * @return the value of ub_search_words.modify_time
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ub_search_words.modify_time
     *
     * @param modifyTime the value for ub_search_words.modify_time
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ub_search_words.is_delete
     *
     * @return the value of ub_search_words.is_delete
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ub_search_words.is_delete
     *
     * @param isDelete the value for ub_search_words.is_delete
     *
     * @mbggenerated Wed Apr 05 15:33:41 CST 2017
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}