package com.depression.model.web.dto;

import java.util.Date;
import java.util.List;

public class WebMemberAdvisoryCurationDTO{

	private Long askId;
	
	private Long tagId;
	
	private String askContent;
	
	private String answerContent;
	
	private Long praiseNum;
	
	private Long viewNum;
	
	private Date askTime;
	
	private Date createTime;
	
	private Date modifyTime;
	
	private Byte isDelete;
	
	private Byte isEnable;

	private List<WebMemberAdvisoryCurationImgsDTO> imgList;
	
	private String tagName;

	public Long getAskId()
	{
		return askId;
	}

	public void setAskId(Long askId)
	{
		this.askId = askId;
	}

	public Long getTagId()
	{
		return tagId;
	}

	public void setTagId(Long tagId)
	{
		this.tagId = tagId;
	}

	public String getAskContent()
	{
		return askContent;
	}

	public void setAskContent(String askContent)
	{
		this.askContent = askContent;
	}

	public String getAnswerContent()
	{
		return answerContent;
	}

	public void setAnswerContent(String answerContent)
	{
		this.answerContent = answerContent;
	}

	public Long getPraiseNum()
	{
		return praiseNum;
	}

	public void setPraiseNum(Long praiseNum)
	{
		this.praiseNum = praiseNum;
	}

	public Long getViewNum()
	{
		return viewNum;
	}

	public void setViewNum(Long viewNum)
	{
		this.viewNum = viewNum;
	}

	public Date getAskTime()
	{
		return askTime;
	}

	public void setAskTime(Date askTime)
	{
		this.askTime = askTime;
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

	public Byte getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Byte isDelete)
	{
		this.isDelete = isDelete;
	}

	public Byte getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Byte isEnable)
	{
		this.isEnable = isEnable;
	}

	public List<WebMemberAdvisoryCurationImgsDTO> getImgList()
	{
		return imgList;
	}

	public void setImgList(List<WebMemberAdvisoryCurationImgsDTO> imgList)
	{
		this.imgList = imgList;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}
	
}
