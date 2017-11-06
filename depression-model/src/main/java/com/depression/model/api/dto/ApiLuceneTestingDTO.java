package com.depression.model.api.dto;

/**
 * 试卷
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
public class ApiLuceneTestingDTO
{
	private Long testingId;
	private Integer typeId;
	private Integer tsType;
	private Integer calcMethod;
	private String title;
	private String subtitle;
	private String contentExplain;
	private Integer questionsNum;
	private Integer testingPeopleNum;
	private Integer testingCommentPeopleNum;
	private String thumbnail;
	private String thumbnailMobile;
	private String thumbnailSlide;
	private String isDelete;

	// 问卷类别表字段
	private String testingName;
	// 是否已经测试
	private int tested;
	// 总数量
	private int questionAmount;
	// 图片网络路径
	private String filePath;
	private String filePathMobile;
	private String filePathSlide;
	
    private Integer luceneFlag;

	public Long getTestingId()
	{
		return testingId;
	}

	public void setTestingId(Long testingId)
	{
		this.testingId = testingId;
	}

	public Integer getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public Integer getTsType()
	{
		return tsType;
	}

	public void setTsType(Integer tsType)
	{
		this.tsType = tsType;
	}

	public Integer getCalcMethod()
	{
		return calcMethod;
	}

	public void setCalcMethod(Integer calcMethod)
	{
		this.calcMethod = calcMethod;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSubtitle()
	{
		return subtitle;
	}

	public void setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
	}

	public String getContentExplain()
	{
		return contentExplain;
	}

	public void setContentExplain(String contentExplain)
	{
		this.contentExplain = contentExplain;
	}

	public Integer getQuestionsNum()
	{
		return questionsNum;
	}

	public void setQuestionsNum(Integer questionsNum)
	{
		this.questionsNum = questionsNum;
	}

	public Integer getTestingPeopleNum()
	{
		return testingPeopleNum;
	}

	public void setTestingPeopleNum(Integer testingPeopleNum)
	{
		this.testingPeopleNum = testingPeopleNum;
	}

	public Integer getTestingCommentPeopleNum()
	{
		return testingCommentPeopleNum;
	}

	public void setTestingCommentPeopleNum(Integer testingCommentPeopleNum)
	{
		this.testingCommentPeopleNum = testingCommentPeopleNum;
	}

	public String getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(String isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getTestingName()
	{
		return testingName;
	}

	public void setTestingName(String testingName)
	{
		this.testingName = testingName;
	}

	public int getTested()
	{
		return tested;
	}

	public void setTested(int tested)
	{
		this.tested = tested;
	}

	public int getQuestionAmount()
	{
		return questionAmount;
	}

	public void setQuestionAmount(int questionAmount)
	{
		this.questionAmount = questionAmount;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public String getThumbnailMobile()
	{
		return thumbnailMobile;
	}

	public void setThumbnailMobile(String thumbnailMobile)
	{
		this.thumbnailMobile = thumbnailMobile;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getFilePathMobile()
	{
		return filePathMobile;
	}

	public void setFilePathMobile(String filePathMobile)
	{
		this.filePathMobile = filePathMobile;
	}

	public String getThumbnailSlide()
	{
		return thumbnailSlide;
	}

	public void setThumbnailSlide(String thumbnailSlide)
	{
		this.thumbnailSlide = thumbnailSlide;
	}

	public String getFilePathSlide()
	{
		return filePathSlide;
	}

	public void setFilePathSlide(String filePathSlide)
	{
		this.filePathSlide = filePathSlide;
	}

	public Integer getLuceneFlag()
	{
		return luceneFlag;
	}

	public void setLuceneFlag(Integer luceneFlag)
	{
		this.luceneFlag = luceneFlag;
	}
}
