/******************************************************************
 ** 类    名：Feedback
 ** 描    述：意见反馈
 ** 创 建 者：fanxinhui
 ** 创建时间：2016-07-05
 ******************************************************************/
package com.depression.model;

import java.util.Date;

/**
 * (Feedback)
 * 
 * @author fanxinhui
 * @version 1.0.0 2016-07-05
 */
public class Feedback extends Page implements java.io.Serializable
{

	private static final long serialVersionUID = 8714487775873544576L;

	private Integer fid;

	private Long mid;

	private String fContent;

	private Date fTime;

	private Integer isDelete;
	
	// show
	private String nickname;//会员昵称

	public Integer getFid()
	{
		return fid;
	}

	public void setFid(Integer fid)
	{
		this.fid = fid;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getfContent()
	{
		return fContent;
	}

	public void setfContent(String fContent)
	{
		this.fContent = fContent;
	}

	public Date getfTime()
	{
		return fTime;
	}

	public void setfTime(Date fTime)
	{
		this.fTime = fTime;
	}

	public Integer getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

}
