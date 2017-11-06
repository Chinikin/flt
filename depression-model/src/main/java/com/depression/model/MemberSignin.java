package com.depression.model;

import java.util.Date;

/**
 * 会员签到
 * @author caizj
 *
 */
public class MemberSignin extends Page implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2291972125585512236L;

	private Long signinId;
	private Long mid;
	private Date createTime;
	private Integer isDelete;

	public Long getSigninId()
	{
		return signinId;
	}

	public void setSigninId(Long signinId)
	{
		this.signinId = signinId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
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
