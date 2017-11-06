package com.depression.model;

/**
 * 名医介绍
 * 
 * @author fanxinhui
 * @date 2016/6/20
 */
public class FamousDoctors extends Page
{
	private Integer doctId;
	private String avatar;
	private String name;
	private String hospital;
	private String department;
	private String position;
	private String specializes;
	private String briefIntroduction;
	private String isDelete;
	
	//show
	private String imgPreviewPath;
	
	public Integer getDoctId()
	{
		return doctId;
	}
	public void setDoctId(Integer doctId)
	{
		this.doctId = doctId;
	}
	
	public String getAvatar()
	{
		return avatar;
	}
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getHospital()
	{
		return hospital;
	}
	public void setHospital(String hospital)
	{
		this.hospital = hospital;
	}
	public String getDepartment()
	{
		return department;
	}
	public void setDepartment(String department)
	{
		this.department = department;
	}
	public String getPosition()
	{
		return position;
	}
	public void setPosition(String position)
	{
		this.position = position;
	}
	public String getSpecializes()
	{
		return specializes;
	}
	public void setSpecializes(String specializes)
	{
		this.specializes = specializes;
	}
	public String getBriefIntroduction()
	{
		return briefIntroduction;
	}
	public void setBriefIntroduction(String briefIntroduction)
	{
		this.briefIntroduction = briefIntroduction;
	}
	public String getIsDelete()
	{
		return isDelete;
	}
	public void setIsDelete(String isDelete)
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

}
