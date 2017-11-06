package com.depression.model.api.dto;

import java.util.Date;
import java.util.List;

public class ApiMemberAdvisoryCurationDTO{

	private Long askId;
	
	private Long tagId;
	
	private String askContent;
	
	private String answerContent;
	
	private Long praiseNum;
	
	private Long viewNum;
	
	private Long sorting;
	
	private Date askTime;
	
	private Date createTime;
	
	private Date modifyTime;
	
	private String isDelete;
	
	private String isEnable;

	private List<ApiMemberAdvisoryCurationImgsDTO> imgList;
	
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

	public Long getSorting()
	{
		return sorting;
	}

	public void setSorting(Long sorting)
	{
		this.sorting = sorting;
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

	public String getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(String isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(String isEnable)
	{
		this.isEnable = isEnable;
	}

	public List<ApiMemberAdvisoryCurationImgsDTO> getImgList()
	{
		return imgList;
	}

	public void setImgList(List<ApiMemberAdvisoryCurationImgsDTO> imgList)
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
