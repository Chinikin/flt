package com.depression.model;

import java.util.Date;

/**
 * 试卷分类
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
public class TestingTypeOld extends Page
{
	private Integer typeId;
	private Integer parentId;
	private String testingName;
	private Integer tsType;
	private String thumbnail;
	private Date createTime;
	private Date modifyTime;
	private Integer isDelete;

	public Integer getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	public String getTestingName()
	{
		return testingName;
	}

	public void setTestingName(String testingName)
	{
		this.testingName = testingName;
	}

	public Integer getTsType()
	{
		return tsType;
	}

	public void setTsType(Integer tsType)
	{
		this.tsType = tsType;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
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

	public Integer getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

}
