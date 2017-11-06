package com.depression.model.web.dto;

import java.util.Date;
import java.util.List;

public class WebSystemUserInfoDTO{

	private Long userId;
	
	private Byte userType;
	
	private Long dptId;
	
	private String dptName;
	
	private String showName;
	
	private String mobilePhone;
	
	private String username;
	
	private String userPsw;
	
	private Date createTime;
	
	private Date modifyTime;
	
	private Byte isEnable;
	
	private Byte isDelete;
	
	private List<String> privilegesMenuList;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Byte getUserType()
	{
		return userType;
	}

	public void setUserType(Byte userType)
	{
		this.userType = userType;
	}

	public Long getDptId()
	{
		return dptId;
	}

	public void setDptId(Long dptId)
	{
		this.dptId = dptId;
	}

	public String getDptName()
	{
		return dptName;
	}

	public void setDptName(String dptName)
	{
		this.dptName = dptName;
	}

	public String getShowName()
	{
		return showName;
	}

	public void setShowName(String showName)
	{
		this.showName = showName;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUserPsw()
	{
		return userPsw;
	}

	public void setUserPsw(String userPsw)
	{
		this.userPsw = userPsw;
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

	public List<String> getPrivilegesMenuList()
	{
		return privilegesMenuList;
	}

	public void setPrivilegesMenuList(List<String> privilegesMenuList)
	{
		this.privilegesMenuList = privilegesMenuList;
	}

}
