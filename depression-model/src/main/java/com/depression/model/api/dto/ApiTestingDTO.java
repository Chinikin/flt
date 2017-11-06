package com.depression.model.api.dto;


public class ApiTestingDTO {
	
	private Long testingId;
	
	private Long typeId;
	
	private Integer calcMethod;
	
	private String title;
	
	private String subtitle;
	
	private String thumbnail;
	
	private String thumbnailMobile;
	
	private String thumbnailSlide;
	
	private Integer questionsNum;
	
	private Long testingPeopleNum;
	
	private Long testingCommentPeopleNum;
	
	private Byte isEnable;
	
	private String contentExplain;
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

	public Long getTestingId() {
		return testingId;
	}
	public void setTestingId(Long testingId) {
		this.testingId = testingId;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Integer getCalcMethod() {
		return calcMethod;
	}
	public void setCalcMethod(Integer calcMethod) {
		this.calcMethod = calcMethod;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getThumbnailMobile() {
		return thumbnailMobile;
	}
	public void setThumbnailMobile(String thumbnailMobile) {
		this.thumbnailMobile = thumbnailMobile;
	}
	public String getThumbnailSlide() {
		return thumbnailSlide;
	}
	public void setThumbnailSlide(String thumbnailSlide) {
		this.thumbnailSlide = thumbnailSlide;
	}
	public Integer getQuestionsNum() {
		return questionsNum;
	}
	public void setQuestionsNum(Integer questionsNum) {
		this.questionsNum = questionsNum;
	}
	public Long getTestingPeopleNum() {
		return testingPeopleNum;
	}
	public void setTestingPeopleNum(Long testingPeopleNum) {
		this.testingPeopleNum = testingPeopleNum;
	}
	public Long getTestingCommentPeopleNum() {
		return testingCommentPeopleNum;
	}
	public void setTestingCommentPeopleNum(Long testingCommentPeopleNum) {
		this.testingCommentPeopleNum = testingCommentPeopleNum;
	}
	public Byte getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Byte isEnable) {
		this.isEnable = isEnable;
	}
	public String getContentExplain() {
		return contentExplain;
	}
	public void setContentExplain(String contentExplain) {
		this.contentExplain = contentExplain;
	}
	
	public String getTestingName() {
		return testingName;
	}
	public void setTestingName(String testingName) {
		this.testingName = testingName;
	}
	public int getTested() {
		return tested;
	}
	public void setTested(int tested) {
		this.tested = tested;
	}
	public int getQuestionAmount() {
		return questionAmount;
	}
	public void setQuestionAmount(int questionAmount) {
		this.questionAmount = questionAmount;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePathMobile() {
		return filePathMobile;
	}
	public void setFilePathMobile(String filePathMobile) {
		this.filePathMobile = filePathMobile;
	}
	public String getFilePathSlide() {
		return filePathSlide;
	}
	public void setFilePathSlide(String filePathSlide) {
		this.filePathSlide = filePathSlide;
	}
	
}
