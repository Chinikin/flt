package com.depression.model;

/**
 * 跳转方式问卷测试结论
 * 
 * @author fanxinhui
 * @date 2016/6/27
 */
public class TestingResultForJumpOld extends Page
{
	private Integer resId;
	private Integer testingId;
	private String resultTag;
	private String title;
	private String thumbnail;
	private String detail;
	private Integer isDelete;

	// show
	private String testingTitle;
	private String filePath;// 图片网络路径
	private String filePathMobile;
	private String filePathSlide;
	private Integer calcMethod;//问卷类型：0计分；1跳转
	private Long questionAmount;//试题总数

	public Integer getResId()
	{
		return resId;
	}

	public void setResId(Integer resId)
	{
		this.resId = resId;
	}

	public Integer getTestingId()
	{
		return testingId;
	}

	public void setTestingId(Integer testingId)
	{
		this.testingId = testingId;
	}

	public String getResultTag()
	{
		return resultTag;
	}

	public void setResultTag(String resultTag)
	{
		this.resultTag = resultTag;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public Integer getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getTestingTitle()
	{
		return testingTitle;
	}

	public void setTestingTitle(String testingTitle)
	{
		this.testingTitle = testingTitle;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public Integer getCalcMethod()
	{
		return calcMethod;
	}

	public void setCalcMethod(Integer calcMethod)
	{
		this.calcMethod = calcMethod;
	}

	public String getFilePathMobile()
	{
		return filePathMobile;
	}

	public void setFilePathMobile(String filePathMobile)
	{
		this.filePathMobile = filePathMobile;
	}

	public String getFilePathSlide()
	{
		return filePathSlide;
	}

	public void setFilePathSlide(String filePathSlide)
	{
		this.filePathSlide = filePathSlide;
	}

	public Long getQuestionAmount()
	{
		return questionAmount;
	}

	public void setQuestionAmount(Long questionAmount)
	{
		this.questionAmount = questionAmount;
	}

}
