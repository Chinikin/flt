package com.depression.model.web.dto;

import java.util.Date;

public class AdStartupPageDTO {
	
    /* @Comment(主键) */
    private Long stPageId;

    /* @Comment(标题) */
    private String pageTitle;

    /* @Comment(图片地址) */
    private String picPath;

    /* @Comment(排序) */
    private Long sorting;

    /* @Comment(链接) */
    private String link;

    /* @Comment(开始时间) */
    private Date startTime;

    /* @Comment(结束时间) */
    private Date endTime;

    /* @Comment(创建时间) */
    private Date createTime;

    /* @Comment(修改时间) */
    private Date modifyTime;

    /* @Comment(是否启用：0启用，1禁用) */
    private Byte isEnable;

    /* @Comment(是否删除：0不删除，1删除) */
    private Byte isDelete;
    
    /* @Comment(图片网络路径) */
 	private String filePath;
 	
 	/* @Comment(状态) */
 	private String startPageStatus;

	public Long getStPageId()
	{
		return stPageId;
	}

	public void setStPageId(Long stPageId)
	{
		this.stPageId = stPageId;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}

	public void setPageTitle(String pageTitle)
	{
		this.pageTitle = pageTitle;
	}

	public String getPicPath()
	{
		return picPath;
	}

	public void setPicPath(String picPath)
	{
		this.picPath = picPath;
	}

	public Long getSorting()
	{
		return sorting;
	}

	public void setSorting(Long sorting)
	{
		this.sorting = sorting;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public Byte getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Byte isEnable)
	{
		this.isEnable = isEnable;
	}

	public Byte getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Byte isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getStartPageStatus()
	{
		return startPageStatus;
	}

	public void setStartPageStatus(String startPageStatus)
	{
		this.startPageStatus = startPageStatus;
	}

}
