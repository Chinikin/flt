package com.depression.model.web.dto;

import java.util.Date;

public class AdBannerDTO {
	
    private Long bannerId;

    /* @Comment(标题) */
    private String bannerTitle;

    /* @Comment(图片地址) */
    private String picPath;

    /* @Comment(排序) */
    private Long sorting;

    /* @Comment(链接类型: 0 内链，1外链) */
    private Byte linkType;

    /* @Comment(外链地址) */
    private String outsideLink;

    /* @Comment(內链模块类型: 0 砖家，1文章，测试) */
    private Byte insideLinkType;

    /* @Comment(內链id) */
    private Long insideLinkId;

    /* @Comment(创建时间) */
    private Date createTime;

    /* @Comment(修改时间) */
    private Date modifyTime;

    /* @Comment(是否启用：0启用，1禁用) */
    private Byte isEnable;

    /* @Comment(图片网络路径) */
 	private String filePath;
 	
 	/* @Comment(问卷计分方式) */
 	private Integer calcMethod4Testing;
 	
    private Byte showLocation;
    
    private Long releaseFrom;
    

	public Long getBannerId()
	{
		return bannerId;
	}

	public void setBannerId(Long bannerId)
	{
		this.bannerId = bannerId;
	}

	public String getBannerTitle()
	{
		return bannerTitle;
	}

	public void setBannerTitle(String bannerTitle)
	{
		this.bannerTitle = bannerTitle;
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

	public Byte getLinkType()
	{
		return linkType;
	}

	public void setLinkType(Byte linkType)
	{
		this.linkType = linkType;
	}

	public String getOutsideLink()
	{
		return outsideLink;
	}

	public void setOutsideLink(String outsideLink)
	{
		this.outsideLink = outsideLink;
	}

	public Byte getInsideLinkType()
	{
		return insideLinkType;
	}

	public void setInsideLinkType(Byte insideLinkType)
	{
		this.insideLinkType = insideLinkType;
	}

	public Long getInsideLinkId()
	{
		return insideLinkId;
	}

	public void setInsideLinkId(Long insideLinkId)
	{
		this.insideLinkId = insideLinkId;
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

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public Integer getCalcMethod4Testing()
	{
		return calcMethod4Testing;
	}

	public void setCalcMethod4Testing(Integer calcMethod4Testing)
	{
		this.calcMethod4Testing = calcMethod4Testing;
	}

	public Byte getShowLocation()
	{
		return showLocation;
	}

	public void setShowLocation(Byte showLocation)
	{
		this.showLocation = showLocation;
	}

	public Long getReleaseFrom() {
		return releaseFrom;
	}

	public void setReleaseFrom(Long releaseFrom) {
		this.releaseFrom = releaseFrom;
	}
}
