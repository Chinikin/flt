package com.depression.model.web.dto;

import java.util.Date;

public class WebPsychoGroupDTO{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group.pg_id
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    /* @Comment() */
    private Long pgId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group.group_name
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    /* @Comment(组名) */
    private String groupName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group.token
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    /* @Comment(记号) */
    private String token;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column psycho_group.type
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    /* @Comment(0 EAP类型组) */
    private Byte type;

    /* @Comment() */
    private Date createTime;

    /* @Comment() */
    private Date updateTime;
    
    private Byte isEnable;
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group.pg_id
     *
     * @return the value of psycho_group.pg_id
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public Long getPgId() {
        return pgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group.pg_id
     *
     * @param pgId the value for psycho_group.pg_id
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public void setPgId(Long pgId) {
        this.pgId = pgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group.group_name
     *
     * @return the value of psycho_group.group_name
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group.group_name
     *
     * @param groupName the value for psycho_group.group_name
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group.token
     *
     * @return the value of psycho_group.token
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public String getToken() {
        return token;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group.token
     *
     * @param token the value for psycho_group.token
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column psycho_group.type
     *
     * @return the value of psycho_group.type
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column psycho_group.type
     *
     * @param type the value for psycho_group.type
     *
     * @mbggenerated Thu Dec 15 16:13:47 CST 2016
     */
    public void setType(Byte type) {
        this.type = type;
    }

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public Byte getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Byte isEnable)
	{
		this.isEnable = isEnable;
	}
    
    
}