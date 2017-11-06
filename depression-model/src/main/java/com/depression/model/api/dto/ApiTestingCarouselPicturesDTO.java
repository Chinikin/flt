package com.depression.model.api.dto;

/**
 * 问卷轮播图片
 * 
 * @author fanxinhui
 * @date 2016/7/11
 */
public class ApiTestingCarouselPicturesDTO
{

	/* 轮播图片属性 */
	private Long cpid;
	private Long type;
	private String descp;
	private String imgPath;
	private Long testingId;
	private Byte isEnable;
	/* 问卷相关属性 */
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
	private int tested;// 是否已经测试
	private int questionAmount;// 总数量
	private String filePath;// 图片网络路径
	private String filePathMobile;
	private String filePathSlide;

	public Long getCpid()
	{
		return cpid;
	}

	public void setCpid(Long cpid)
	{
		this.cpid = cpid;
	}

	public Long getType()
	{
		return type;
	}

	public void setType(Long type)
	{
		this.type = type;
	}

	public String getDescp()
	{
		return descp;
	}

	public void setDescp(String descp)
	{
		this.descp = descp;
	}

	public String getImgPath()
	{
		return imgPath;
	}

	public void setImgPath(String imgPath)
	{
		this.imgPath = imgPath;
	}

	public Long getTestingId()
	{
		return testingId;
	}

	public void setTestingId(Long testingId)
	{
		this.testingId = testingId;
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

	public String getThumbnailSlide()
	{
		return thumbnailSlide;
	}

	public void setThumbnailSlide(String thumbnailSlide)
	{
		this.thumbnailSlide = thumbnailSlide;
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

	public Byte getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Byte isEnable) {
		this.isEnable = isEnable;
	}

}
