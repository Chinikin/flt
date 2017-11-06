package com.depression.model.web.dto;

import java.util.Date;
import java.util.List;

import com.depression.model.Page;

/**
 * 会员咨询VO
 * 
 */
public class WebMemberAdvisoryDetailDTO {
    
	/** 动态id */
    private Long advisoryId;
    
    /** 会员id */
    private Long mid;
    
    /** 内容 */
    private String content;
    
    private String detail;
    
    /** 发表位置 */
    private String writeLocation;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 修改时间 */
    private Date modifyTime;
    
    /** 默认0:不删除  1：删除 */
    private Integer isDelete;
    
    /** 标签id */
    private Long tagId;
    
    /** 咨询者账号 */
    private String advNickname;
    
    /** 咨询者头像 */
    private String advThumbnail;
    
    private Integer disableMessageDays;
    
    /** 标签名称 */
    private String tagName;
    
    /** 回答数 */
    private Long answerNum;
    
    private List<WebMemberAdvisoryImgsDTO> imgsDTOs;
    
    /**
     * 获取动态id
     * 
     * @return 动态id
     */
    public Long getAdvisoryId() {
        return this.advisoryId;
    }
     
    /**
     * 设置动态id
     * 
     * @param advisoryId
     *          动态id
     */
    public void setAdvisoryId(Long advisoryId) {
        this.advisoryId = advisoryId;
    }
    
    /**
     * 获取会员id
     * 
     * @return 会员id
     */
    public Long getMid() {
        return this.mid;
    }
     
    /**
     * 设置会员id
     * 
     * @param mid
     *          会员id
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }
    
    /**
     * 获取内容
     * 
     * @return 内容
     */
    public String getContent() {
        return this.content;
    }
     
    /**
     * 设置内容
     * 
     * @param content
     *          内容
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * 获取发表位置
     * 
     * @return 发表位置
     */
    public String getWriteLocation() {
        return this.writeLocation;
    }
     
    /**
     * 设置发表位置
     * 
     * @param writeLocation
     *          发表位置
     */
    public void setWriteLocation(String writeLocation) {
        this.writeLocation = writeLocation;
    }
    
    /**
     * 获取创建时间
     * 
     * @return 创建时间
     */
    public Date getCreateTime() {
        return this.createTime;
    }
     
    /**
     * 设置创建时间
     * 
     * @param createTime
     *          创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * 获取修改时间
     * 
     * @return 修改时间
     */
    public Date getModifyTime() {
        return this.modifyTime;
    }
     
    /**
     * 设置修改时间
     * 
     * @param modifyTime
     *          修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    
    /**
     * 获取默认0:不删除  1：删除
     * 
     * @return 默认0:不删除  1
     */
    public Integer getIsDelete() {
        return this.isDelete;
    }
     
    /**
     * 设置默认0:不删除  1：删除
     * 
     * @param isDelete
     *          默认0:不删除  1：删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

	public Long getTagId()
	{
		return tagId;
	}

	public void setTagId(Long tagId)
	{
		this.tagId = tagId;
	}

	public String getAdvNickname()
	{
		return advNickname;
	}

	public void setAdvNickname(String advNickname)
	{
		this.advNickname = advNickname;
	}

	public String getAdvThumbnail()
	{
		return advThumbnail;
	}

	public void setAdvThumbnail(String advThumbnail)
	{
		this.advThumbnail = advThumbnail;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public Long getAnswerNum()
	{
		return answerNum;
	}

	public void setAnswerNum(Long answerNum)
	{
		this.answerNum = answerNum;
	}

	public List<WebMemberAdvisoryImgsDTO> getImgsDTOs()
	{
		return imgsDTOs;
	}

	public void setImgsDTOs(List<WebMemberAdvisoryImgsDTO> imgsDTOs)
	{
		this.imgsDTOs = imgsDTOs;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public Integer getDisableMessageDays()
	{
		return disableMessageDays;
	}

	public void setDisableMessageDays(Integer disableMessageDays)
	{
		this.disableMessageDays = disableMessageDays;
	}
}
