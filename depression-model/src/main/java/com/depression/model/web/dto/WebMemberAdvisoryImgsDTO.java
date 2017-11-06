package com.depression.model.web.dto;

/**
 * 会员咨询中的图片列表(member_advisory_imgs)
 * 
 * @author hongqian_li
 * @version 1.0.0 2016-05-09
 */
public class WebMemberAdvisoryImgsDTO
{
	/** 会员动态中的图片id */
	private Long advisoryImgId;

	/** 会员咨询id */
	private Long advisoryId;

	/** 图片路径 */
	private String imgPath;
	
	private String imgPathAbs;

	/** 默认0:不删除 1：删除 */
	private Integer isDelete;
	
	/** 图片预览路径 */
	private String imgPreviewPath;
	
	private String imgPreviewPathAbs;

	/**
	 * 获取会员动态中的图片id
	 * 
	 * @return 会员动态中的图片id
	 */
	public Long getAdvisoryImgId()
	{
		return this.advisoryImgId;
	}

	/**
	 * 设置会员动态中的图片id
	 * 
	 * @param advisoryImgId
	 *            会员动态中的图片id
	 */
	public void setAdvisoryImgId(Long advisoryImgId)
	{
		this.advisoryImgId = advisoryImgId;
	}

	/**
	 * 获取会员咨询id
	 * 
	 * @return 会员咨询id
	 */
	public Long getAdvisoryId()
	{
		return this.advisoryId;
	}

	/**
	 * 设置会员咨询id
	 * 
	 * @param advisoryId
	 *            会员咨询id
	 */
	public void setAdvisoryId(Long advisoryId)
	{
		this.advisoryId = advisoryId;
	}

	/**
	 * 获取图片路径
	 * 
	 * @return 图片路径
	 */
	public String getImgPath()
	{
		return this.imgPath;
	}

	/**
	 * 设置图片路径
	 * 
	 * @param imgPath
	 *            图片路径
	 */
	public void setImgPath(String imgPath)
	{
		this.imgPath = imgPath;
	}

	/**
	 * 获取默认0:不删除 1：删除
	 * 
	 * @return 默认0:不删除 1
	 */
	public Integer getIsDelete()
	{
		return this.isDelete;
	}

	/**
	 * 设置默认0:不删除 1：删除
	 * 
	 * @param isDelete
	 *            默认0:不删除 1：删除
	 */
	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getImgPreviewPath()
	{
		return imgPreviewPath;
	}

	public void setImgPreviewPath(String imgPreviewPath)
	{
		this.imgPreviewPath = imgPreviewPath;
	}

	public String getImgPathAbs()
	{
		return imgPathAbs;
	}

	public void setImgPathAbs(String imgPathAbs)
	{
		this.imgPathAbs = imgPathAbs;
	}

	public String getImgPreviewPathAbs()
	{
		return imgPreviewPathAbs;
	}

	public void setImgPreviewPathAbs(String imgPreviewPathAbs)
	{
		this.imgPreviewPathAbs = imgPreviewPathAbs;
	}
}
