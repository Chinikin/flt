package com.depression.model;

import java.util.Date;

public class ArticleCollection extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_collection.col_id
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    /* @Comment(科普文章收藏id) */
    private Long colId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_collection.mid
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    /* @Comment(会员id，关联会员信息表) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_collection.article_id
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    /* @Comment(文章id，关联文章表) */
    private Long articleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_collection.collection_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    /* @Comment(收藏时间) */
    private Date collectionTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_collection.create_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    /* @Comment(创建时间) */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_collection.modify_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    /* @Comment(修改时间) */
    private Date modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_collection.is_delete
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    /* @Comment(默认0:不删除  1：删除) */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_collection.col_id
     *
     * @return the value of article_collection.col_id
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public Long getColId() {
        return colId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_collection.col_id
     *
     * @param colId the value for article_collection.col_id
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public void setColId(Long colId) {
        this.colId = colId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_collection.mid
     *
     * @return the value of article_collection.mid
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_collection.mid
     *
     * @param mid the value for article_collection.mid
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_collection.article_id
     *
     * @return the value of article_collection.article_id
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public Long getArticleId() {
        return articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_collection.article_id
     *
     * @param articleId the value for article_collection.article_id
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_collection.collection_time
     *
     * @return the value of article_collection.collection_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public Date getCollectionTime() {
        return collectionTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_collection.collection_time
     *
     * @param collectionTime the value for article_collection.collection_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public void setCollectionTime(Date collectionTime) {
        this.collectionTime = collectionTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_collection.create_time
     *
     * @return the value of article_collection.create_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_collection.create_time
     *
     * @param createTime the value for article_collection.create_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_collection.modify_time
     *
     * @return the value of article_collection.modify_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_collection.modify_time
     *
     * @param modifyTime the value for article_collection.modify_time
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_collection.is_delete
     *
     * @return the value of article_collection.is_delete
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_collection.is_delete
     *
     * @param isDelete the value for article_collection.is_delete
     *
     * @mbggenerated Thu Mar 16 11:08:34 CST 2017
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}