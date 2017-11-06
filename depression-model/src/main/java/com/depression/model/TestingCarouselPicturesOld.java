package com.depression.model;

/**
 * 问卷轮播图片
 * 
 * @author fanxinhui
 * @date 2016/7/11
 */
public class TestingCarouselPicturesOld extends Page
{
	private Long cpid;
	private Long type;
	private String descp;
	private String imgPath;
	private Long testingId;
	private Integer isDelete;
	
	// show
	private String filePath;//图片路径（显示）

	public Long getCpid()
	{
		return cpid;
	}

	public void setCpid(Long cpid)
	{
		this.cpid = cpid;
	}

	public Long getType()
	{
		return type;
	}

	public void setType(Long type)
	{
		this.type = type;
	}

	public String getDescp()
	{
		return descp;
	}

	public void setDescp(String descp)
	{
		this.descp = descp;
	}

	public String getImgPath()
	{
		return imgPath;
	}

	public void setImgPath(String imgPath)
	{
		this.imgPath = imgPath;
	}

	public Long getTestingId()
	{
		return testingId;
	}

	public void setTestingId(Long testingId)
	{
		this.testingId = testingId;
	}

	public Integer getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Integer isDelete)
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

}
