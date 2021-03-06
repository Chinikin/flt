package com.depression.model;

import java.util.Date;

public class MemberUpdateComment extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.comment_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(主键) */
    private Long commentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.mid
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(会员id) */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.update_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(试卷id，关联试卷表) */
    private Long updateId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.is_anony
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(0 匿名 1不匿名) */
    private Byte isAnony;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.comment_content
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(评论内容) */
    private String commentContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.write_location
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(发布地点) */
    private String writeLocation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.parent_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(父级评论，指向它的父结点，就是另一条评论的id) */
    private Long parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.comment_time
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(创建时间) */
    private Date commentTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.read_status
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(是否已读) */
    private Byte readStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_update_comment.is_delete
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    /* @Comment(默认0:不删除  1：删除) */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.comment_id
     *
     * @return the value of member_update_comment.comment_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Long getCommentId() {
        return commentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.comment_id
     *
     * @param commentId the value for member_update_comment.comment_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.mid
     *
     * @return the value of member_update_comment.mid
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.mid
     *
     * @param mid the value for member_update_comment.mid
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.update_id
     *
     * @return the value of member_update_comment.update_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Long getUpdateId() {
        return updateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.update_id
     *
     * @param updateId the value for member_update_comment.update_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.is_anony
     *
     * @return the value of member_update_comment.is_anony
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Byte getIsAnony() {
        return isAnony;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.is_anony
     *
     * @param isAnony the value for member_update_comment.is_anony
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setIsAnony(Byte isAnony) {
        this.isAnony = isAnony;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.comment_content
     *
     * @return the value of member_update_comment.comment_content
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.comment_content
     *
     * @param commentContent the value for member_update_comment.comment_content
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.write_location
     *
     * @return the value of member_update_comment.write_location
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public String getWriteLocation() {
        return writeLocation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.write_location
     *
     * @param writeLocation the value for member_update_comment.write_location
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setWriteLocation(String writeLocation) {
        this.writeLocation = writeLocation == null ? null : writeLocation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.parent_id
     *
     * @return the value of member_update_comment.parent_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.parent_id
     *
     * @param parentId the value for member_update_comment.parent_id
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.comment_time
     *
     * @return the value of member_update_comment.comment_time
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Date getCommentTime() {
        return commentTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.comment_time
     *
     * @param commentTime the value for member_update_comment.comment_time
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.read_status
     *
     * @return the value of member_update_comment.read_status
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Byte getReadStatus() {
        return readStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.read_status
     *
     * @param readStatus the value for member_update_comment.read_status
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setReadStatus(Byte readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_update_comment.is_delete
     *
     * @return the value of member_update_comment.is_delete
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_update_comment.is_delete
     *
     * @param isDelete the value for member_update_comment.is_delete
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}