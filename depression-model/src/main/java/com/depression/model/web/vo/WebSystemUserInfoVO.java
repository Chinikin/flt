package com.depression.model.web.vo;

import java.util.Date;

public class WebSystemUserInfoVO {
	
    /* @Comment(主键) */
    private Long userId;
    
    /* @Comment(用户类型) */
    private Byte userType;

    /* @Comment(部门id) */
    private Long dptId;

    /* @Comment(显示名) */
    private String showName;

    /* @Comment(手机号) */
    private String mobilePhone;

    /* @Comment(用户名) */
    private String username;

    /* @Comment(密码) */
    private String userPsw;

    /* @Comment(创建时间) */
    private Date createTime;

    /* @Comment(修改时间) */
    private Date modifyTime;

    /* @Comment(0启用，1禁用) */
    private Byte isEnable;

    /* @Comment(0正常，1删除) */
    private Byte isDelete;

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

}
