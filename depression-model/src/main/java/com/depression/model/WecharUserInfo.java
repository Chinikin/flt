package com.depression.model;

import java.util.Date;

/**
 * 微信用户信息
 * 
 * @author fanxinhui
 * 
 */
public class WecharUserInfo extends Page
{
	private String openid;
	private String nickname;
	private String sex;
	private String city;
	private String country;
	private String province;
	private String headimgurl;
	private Date createTime;
	private Date modifyTime;

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getHeadimgurl()
	{
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl)
	{
		this.headimgurl = headimgurl;
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

}
