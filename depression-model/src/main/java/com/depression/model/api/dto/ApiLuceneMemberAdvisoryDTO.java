package com.depression.model.api.dto;

import java.util.Date;
import java.util.List;

import com.depression.model.AdvisoryTag;

/**
 * 会员咨询(member_advisory)
 * 
 * @author hongqian_li
 * @version 1.0.0 2016-05-09
 */
public class ApiLuceneMemberAdvisoryDTO implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -980531813967496142L;
    
    /** 动态id */
    private Long advisoryId;
    
    /** 会员id */
    private Long mid;
    
    /** 内容 */
    private String content;
    
    /** 发表位置 */
    private String writeLocation;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 修改时间 */
    private Date modifyTime;
    
    /** 默认0:不删除  1：删除 */
    private Integer isDelete;
    
    private Long commentCount;
    
    private String avatar;
    
    private String avatarThumbnail;
    
    private String nickname;
    
    List<ApiAdvisoryTagDTO> tags;
    
    private String detail;
    
    private Integer luceneFlag;
    

    
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
    
    

	public Long getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(Long commentCount)
	{
		this.commentCount = commentCount;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getAvatarThumbnail()
	{
		return avatarThumbnail;
	}

	public void setAvatarThumbnail(String avatarThumbnail)
	{
		this.avatarThumbnail = avatarThumbnail;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	
	public List<ApiAdvisoryTagDTO> getTags()
	{
		return tags;
	}

	public void setTags(List<ApiAdvisoryTagDTO> tags)
	{
		this.tags = tags;
	}

	public Integer getLuceneFlag()
	{
		return luceneFlag;
	}

	public void setLuceneFlag(Integer luceneFlag)
	{
		this.luceneFlag = luceneFlag;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}
}