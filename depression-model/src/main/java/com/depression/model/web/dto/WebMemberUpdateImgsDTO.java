package com.depression.model.web.dto;

/**
 * 会员动态中的图片列表(member_update_imgs)
 * 
 * @author hongqian_li
 * @version 1.0.0 2016-05-05
 */
public class WebMemberUpdateImgsDTO
{
	/** 会员动态中的图片id */
	private Long updImgId;

	/** 会员动态id */
	private Long updateId;

	/** 图片路径 */
	private String imgPath;
	
	private String imgPathAbs;

	/** 图片预览路径 */
	private String imgPreviewPath;
	
	private String imgPreviewPathAbs;

	/**
	 * 获取会员动态中的图片id
	 * 
	 * @return 会员动态中的图片id
	 */
	public Long getUpdImgId()
	{
		return this.updImgId;
	}

	/**
	 * 设置会员动态中的图片id
	 * 
	 * @param updImgId
	 *            会员动态中的图片id
	 */
	public void setUpdImgId(Long updImgId)
	{
		this.updImgId = updImgId;
	}

	/**
	 * 获取会员动态id
	 * 
	 * @return 会员动态id
	 */
	public Long getUpdateId()
	{
		return this.updateId;
	}

	/**
	 * 设置会员动态id
	 * 
	 * @param updateId
	 *            会员动态id
	 */
	public void setUpdateId(Long updateId)
	{
		this.updateId = updateId;
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
