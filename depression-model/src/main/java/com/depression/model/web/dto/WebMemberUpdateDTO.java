package com.depression.model.web.dto;

import java.util.Date;
import java.util.List;

import com.depression.model.Page;

/**
 * 会员咨询VO
 * 
 */
public class WebMemberUpdateDTO {
    
	/** 动态id */
	private Long updateId;
	
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
    
    private String nickname;
    
    private String thumbnail;
    
    /** 0 不匿名发布 1匿名发布 */
	private Integer updateType;
	
    /**禁言天数*/
    private Integer disableMessageDays;
    
    private List<WebMemberUpdateImgsDTO> imgsDTOs;
    
    private Integer commentCount;
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
    
    public Long getUpdateId()
	{
		return updateId;
	}

	public void setUpdateId(Long updateId)
	{
		this.updateId = updateId;
	}

	public Integer getUpdateType()
	{
		return updateType;
	}

	public void setUpdateType(Integer updateType)
	{
		this.updateType = updateType;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
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

	public List<WebMemberUpdateImgsDTO> getImgsDTOs()
	{
		return imgsDTOs;
	}

	public void setImgsDTOs(List<WebMemberUpdateImgsDTO> imgsDTOs)
	{
		this.imgsDTOs = imgsDTOs;
	}

	public Integer getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(Integer commentCount)
	{
		this.commentCount = commentCount;
	}  
}
