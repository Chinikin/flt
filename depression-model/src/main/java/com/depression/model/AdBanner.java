package com.depression.model;

import java.util.Date;

public class AdBanner extends Page {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.banner_id
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Long bannerId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.show_location
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Byte showLocation;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.banner_title
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private String bannerTitle;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.pic_path
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private String picPath;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.sorting
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Long sorting;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.link_type
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Byte linkType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.outside_link
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private String outsideLink;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.inside_link_type
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Byte insideLinkType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.inside_link_id
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Long insideLinkId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.create_time
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Date createTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.modify_time
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Date modifyTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.release_from
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Long releaseFrom;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.is_enable
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Byte isEnable;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_banner.is_delete
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	private Byte isDelete;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.banner_id
	 * @return  the value of ad_banner.banner_id
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Long getBannerId() {
		return bannerId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.banner_id
	 * @param bannerId  the value for ad_banner.banner_id
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setBannerId(Long bannerId) {
		this.bannerId = bannerId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.show_location
	 * @return  the value of ad_banner.show_location
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Byte getShowLocation() {
		return showLocation;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.show_location
	 * @param showLocation  the value for ad_banner.show_location
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setShowLocation(Byte showLocation) {
		this.showLocation = showLocation;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.banner_title
	 * @return  the value of ad_banner.banner_title
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public String getBannerTitle() {
		return bannerTitle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.banner_title
	 * @param bannerTitle  the value for ad_banner.banner_title
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setBannerTitle(String bannerTitle) {
		this.bannerTitle = bannerTitle == null ? null : bannerTitle.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.pic_path
	 * @return  the value of ad_banner.pic_path
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public String getPicPath() {
		return picPath;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.pic_path
	 * @param picPath  the value for ad_banner.pic_path
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setPicPath(String picPath) {
		this.picPath = picPath == null ? null : picPath.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.sorting
	 * @return  the value of ad_banner.sorting
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Long getSorting() {
		return sorting;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.sorting
	 * @param sorting  the value for ad_banner.sorting
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setSorting(Long sorting) {
		this.sorting = sorting;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.link_type
	 * @return  the value of ad_banner.link_type
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Byte getLinkType() {
		return linkType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.link_type
	 * @param linkType  the value for ad_banner.link_type
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setLinkType(Byte linkType) {
		this.linkType = linkType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.outside_link
	 * @return  the value of ad_banner.outside_link
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public String getOutsideLink() {
		return outsideLink;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.outside_link
	 * @param outsideLink  the value for ad_banner.outside_link
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setOutsideLink(String outsideLink) {
		this.outsideLink = outsideLink == null ? null : outsideLink.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.inside_link_type
	 * @return  the value of ad_banner.inside_link_type
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Byte getInsideLinkType() {
		return insideLinkType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.inside_link_type
	 * @param insideLinkType  the value for ad_banner.inside_link_type
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setInsideLinkType(Byte insideLinkType) {
		this.insideLinkType = insideLinkType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.inside_link_id
	 * @return  the value of ad_banner.inside_link_id
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Long getInsideLinkId() {
		return insideLinkId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.inside_link_id
	 * @param insideLinkId  the value for ad_banner.inside_link_id
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setInsideLinkId(Long insideLinkId) {
		this.insideLinkId = insideLinkId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.create_time
	 * @return  the value of ad_banner.create_time
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.create_time
	 * @param createTime  the value for ad_banner.create_time
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.modify_time
	 * @return  the value of ad_banner.modify_time
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.modify_time
	 * @param modifyTime  the value for ad_banner.modify_time
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.release_from
	 * @return  the value of ad_banner.release_from
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Long getReleaseFrom() {
		return releaseFrom;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.release_from
	 * @param releaseFrom  the value for ad_banner.release_from
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setReleaseFrom(Long releaseFrom) {
		this.releaseFrom = releaseFrom;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.is_enable
	 * @return  the value of ad_banner.is_enable
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Byte getIsEnable() {
		return isEnable;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.is_enable
	 * @param isEnable  the value for ad_banner.is_enable
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setIsEnable(Byte isEnable) {
		this.isEnable = isEnable;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_banner.is_delete
	 * @return  the value of ad_banner.is_delete
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public Byte getIsDelete() {
		return isDelete;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_banner.is_delete
	 * @param isDelete  the value for ad_banner.is_delete
	 * @mbggenerated  Wed May 10 09:58:39 CST 2017
	 */
	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}
}