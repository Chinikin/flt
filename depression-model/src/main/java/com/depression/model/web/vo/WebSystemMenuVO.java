package com.depression.model.web.vo;

import java.util.Date;

public class WebSystemMenuVO {

	private Long menuId;
	
	private Long userId;
	
	private Byte menuType;
	
	private String menuName;
	
	private String menuUrl;
	
	private Long parentMenuId;
	
	private Date createTime;
	
	private Date modifyTime;
	
	private Byte isEnable;
	
	private Byte isDelete;

	public Long getMenuId()
	{
		return menuId;
	}

	public void setMenuId(Long menuId)
	{
		this.menuId = menuId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Byte getMenuType()
	{
		return menuType;
	}

	public void setMenuType(Byte menuType)
	{
		this.menuType = menuType;
	}

	public String getMenuName()
	{
		return menuName;
	}

	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}

	public String getMenuUrl()
	{
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl)
	{
		this.menuUrl = menuUrl;
	}

	public Long getParentMenuId()
	{
		return parentMenuId;
	}

	public void setParentMenuId(Long parentMenuId)
	{
		this.parentMenuId = parentMenuId;
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

}