package com.depression.model.api.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.depression.model.Page;

public class ApiPersonalityCharactersSimilarityDTO extends Page
{

	/* @Comment(主键) */
	private Long pcsId;

	/* @Comment() */
	private Long gId;

	/* @Comment() */
	private Long mid;

	/* @Comment(相似度) */
	private BigDecimal similarity;

	/* @Comment(创建时间) */
	private Date createTime;

	/* @Comment(修改时间) */
	private Date modifyTime;

	/* @Comment(0启用，1禁用) */
	private Byte isEnable;

	/* @Comment(0正常，1删除) */
	private Byte isDelete;

	/* 用户id */
	private String openid;

	/* 昵称 */
	private String nickname;

	/* 性别 */
	private String sex;

	/* 市 */
	private String city;

	/* 国家 */
	private String country;

	/* 省 */
	private String province;

	/* 头像地址 */
	private String headimgurl;

	/* 合拍（未使用） */
	private String inStep;

	/* 不合拍（未使用） */
	private String outOfStep;

	/* 解药（不合拍双方才存在解药） */
	private String antidote;
	
	/* 是否合拍（1合拍，0不合拍） */
	private Byte whetherInTune;
	
	/* 自己测试结论 */
	private String selfTestDesc;

	/* 对方测试结论 */
	private String otherSideTestDesc;
	
	/* 自己测试类型 */
	private String selfTestType;

	/* 对方测试类型 */
	private String otherSideTestType;

	public Long getPcsId()
	{
		return pcsId;
	}

	public void setPcsId(Long pcsId)
	{
		this.pcsId = pcsId;
	}

	public Long getgId()
	{
		return gId;
	}

	public void setgId(Long gId)
	{
		this.gId = gId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public BigDecimal getSimilarity()
	{
		return similarity;
	}

	public void setSimilarity(BigDecimal similarity)
	{
		this.similarity = similarity;
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

	public String getInStep()
	{
		return inStep;
	}

	public void setInStep(String inStep)
	{
		this.inStep = inStep;
	}

	public String getOutOfStep()
	{
		return outOfStep;
	}

	public void setOutOfStep(String outOfStep)
	{
		this.outOfStep = outOfStep;
	}

	public String getAntidote()
	{
		return antidote;
	}

	public void setAntidote(String antidote)
	{
		this.antidote = antidote;
	}

	public Byte getWhetherInTune()
	{
		return whetherInTune;
	}

	public void setWhetherInTune(Byte whetherInTune)
	{
		this.whetherInTune = whetherInTune;
	}

	public String getSelfTestDesc()
	{
		return selfTestDesc;
	}

	public void setSelfTestDesc(String selfTestDesc)
	{
		this.selfTestDesc = selfTestDesc;
	}

	public String getOtherSideTestDesc()
	{
		return otherSideTestDesc;
	}

	public void setOtherSideTestDesc(String otherSideTestDesc)
	{
		this.otherSideTestDesc = otherSideTestDesc;
	}

	public String getSelfTestType()
	{
		return selfTestType;
	}

	public void setSelfTestType(String selfTestType)
	{
		this.selfTestType = selfTestType;
	}

	public String getOtherSideTestType()
	{
		return otherSideTestType;
	}

	public void setOtherSideTestType(String otherSideTestType)
	{
		this.otherSideTestType = otherSideTestType;
	}

}
