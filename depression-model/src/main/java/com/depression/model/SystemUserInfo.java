package com.depression.model;

import java.util.Date;

public class SystemUserInfo extends Page {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.user_id
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private Long userId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.user_type
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private Byte userType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.dpt_id
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private Long dptId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.show_name
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private String showName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.mobile_phone
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private String mobilePhone;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.username
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private String username;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.user_psw
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private String userPsw;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.create_time
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private Date createTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.modify_time
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private Date modifyTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.is_enable
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private Byte isEnable;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column system_user_info.is_delete
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	private Byte isDelete;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.user_id
	 * @return  the value of system_user_info.user_id
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public Long getUserId()
	{
		return userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.user_id
	 * @param userId  the value for system_user_info.user_id
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.user_type
	 * @return  the value of system_user_info.user_type
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public Byte getUserType()
	{
		return userType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.user_type
	 * @param userType  the value for system_user_info.user_type
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setUserType(Byte userType)
	{
		this.userType = userType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.dpt_id
	 * @return  the value of system_user_info.dpt_id
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public Long getDptId()
	{
		return dptId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.dpt_id
	 * @param dptId  the value for system_user_info.dpt_id
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setDptId(Long dptId)
	{
		this.dptId = dptId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.show_name
	 * @return  the value of system_user_info.show_name
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public String getShowName()
	{
		return showName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.show_name
	 * @param showName  the value for system_user_info.show_name
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setShowName(String showName)
	{
		this.showName = showName == null ? null : showName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.mobile_phone
	 * @return  the value of system_user_info.mobile_phone
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public String getMobilePhone()
	{
		return mobilePhone;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.mobile_phone
	 * @param mobilePhone  the value for system_user_info.mobile_phone
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.username
	 * @return  the value of system_user_info.username
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.username
	 * @param username  the value for system_user_info.username
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setUsername(String username)
	{
		this.username = username == null ? null : username.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.user_psw
	 * @return  the value of system_user_info.user_psw
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public String getUserPsw()
	{
		return userPsw;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.user_psw
	 * @param userPsw  the value for system_user_info.user_psw
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setUserPsw(String userPsw)
	{
		this.userPsw = userPsw == null ? null : userPsw.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.create_time
	 * @return  the value of system_user_info.create_time
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public Date getCreateTime()
	{
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.create_time
	 * @param createTime  the value for system_user_info.create_time
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.modify_time
	 * @return  the value of system_user_info.modify_time
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public Date getModifyTime()
	{
		return modifyTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.modify_time
	 * @param modifyTime  the value for system_user_info.modify_time
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.is_enable
	 * @return  the value of system_user_info.is_enable
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public Byte getIsEnable()
	{
		return isEnable;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.is_enable
	 * @param isEnable  the value for system_user_info.is_enable
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setIsEnable(Byte isEnable)
	{
		this.isEnable = isEnable;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column system_user_info.is_delete
	 * @return  the value of system_user_info.is_delete
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public Byte getIsDelete()
	{
		return isDelete;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column system_user_info.is_delete
	 * @param isDelete  the value for system_user_info.is_delete
	 * @mbggenerated  Mon Sep 12 11:24:07 CST 2016
	 */
	public void setIsDelete(Byte isDelete)
	{
		this.isDelete = isDelete;
	}
}
