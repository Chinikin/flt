package com.depression.model.api.dto;

import java.util.Date;
import java.util.List;

public class ApiAdvisoryDTO{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.advisory_id
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(动态id) */
    private Long advisoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.mid
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(会员id) */
    private Long mid;
    
    private Long createTimestamp;
    
    /* @Comment(标题) */
    private String title;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.content
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(内容) */
    private String content;
    
    private String detail;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.write_location
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(发表位置) */
    private String writeLocation;
    
    /* @Comment(0 匿名 1不匿名) */
    private Byte isAnony;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.read_count
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(阅读数) */
    private Integer readCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.share_count
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(分享数) */
    private Integer shareCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.is_recommended
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(0 未推荐 1被推荐) */
    private Byte isRecommended;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.create_time
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(创建时间) */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory.modify_time
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    /* @Comment(修改时间) */
    private Date modifyTime;
    
    List<ApiAdvisoryImgsDTO> imgs;
    
    private Integer answerCount;
    
    ApiMemberEssentialDTO author;
    
    ApiAdvisoryTagDTO tag;
    
    ApiAdvisoryCommentDTO chosenComment;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.advisory_id
     *
     * @return the value of member_advisory.advisory_id
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public Long getAdvisoryId() {
        return advisoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.advisory_id
     *
     * @param advisoryId the value for member_advisory.advisory_id
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setAdvisoryId(Long advisoryId) {
        this.advisoryId = advisoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.mid
     *
     * @return the value of member_advisory.mid
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.mid
     *
     * @param mid the value for member_advisory.mid
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }
    
    public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.content
     *
     * @return the value of member_advisory.content
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.content
     *
     * @param content the value for member_advisory.content
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
    
    public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.write_location
     *
     * @return the value of member_advisory.write_location
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public String getWriteLocation() {
        return writeLocation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.write_location
     *
     * @param writeLocation the value for member_advisory.write_location
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setWriteLocation(String writeLocation) {
        this.writeLocation = writeLocation == null ? null : writeLocation.trim();
    }
    
    public Byte getIsAnony()
	{
		return isAnony;
	}

	public void setIsAnony(Byte isAnony)
	{
		this.isAnony = isAnony;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.read_count
     *
     * @return the value of member_advisory.read_count
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public Integer getReadCount() {
        return readCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.read_count
     *
     * @param readCount the value for member_advisory.read_count
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.share_count
     *
     * @return the value of member_advisory.share_count
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public Integer getShareCount() {
        return shareCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.share_count
     *
     * @param shareCount the value for member_advisory.share_count
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.is_recommended
     *
     * @return the value of member_advisory.is_recommended
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public Byte getIsRecommended() {
        return isRecommended;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.is_recommended
     *
     * @param isRecommended the value for member_advisory.is_recommended
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setIsRecommended(Byte isRecommended) {
        this.isRecommended = isRecommended;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.create_time
     *
     * @return the value of member_advisory.create_time
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.create_time
     *
     * @param createTime the value for member_advisory.create_time
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory.modify_time
     *
     * @return the value of member_advisory.modify_time
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory.modify_time
     *
     * @param modifyTime the value for member_advisory.modify_time
     *
     * @mbggenerated Mon Mar 20 18:47:10 CST 2017
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    
	public List<ApiAdvisoryImgsDTO> getImgs()
	{
		return imgs;
	}

	public void setImgs(List<ApiAdvisoryImgsDTO> imgs)
	{
		this.imgs = imgs;
	}

	public Integer getAnswerCount()
	{
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount)
	{
		this.answerCount = answerCount;
	}
	
	public ApiMemberEssentialDTO getAuthor()
	{
		return author;
	}

	public void setAuthor(ApiMemberEssentialDTO author)
	{
		this.author = author;
	}

	public ApiAdvisoryTagDTO getTag()
	{
		return tag;
	}

	public void setTag(ApiAdvisoryTagDTO tag)
	{
		this.tag = tag;
	}

	public ApiAdvisoryCommentDTO getChosenComment()
	{
		return chosenComment;
	}

	public void setChosenComment(ApiAdvisoryCommentDTO chosenComment)
	{
		this.chosenComment = chosenComment;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
}