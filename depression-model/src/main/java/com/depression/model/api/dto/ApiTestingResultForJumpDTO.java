package com.depression.model.api.dto;


public class ApiTestingResultForJumpDTO {

	
	private Long resId;
	
	private Long testingId;
	
	private String resultTag;
	
	private String title;
	
	private String thumbnail;
	
	private Byte isEnable;
	
	private String detail;
	
	private String filePath;// 图片网络路径
	private String filePathMobile;
	private String filePathSlide;
	private Integer calcMethod;//问卷类型：0计分；1跳转
	private Long questionAmount;//试题总数
		// show
		private String testingTitle;
		public String getTestingTitle() {
			return testingTitle;
		}
		public void setTestingTitle(String testingTitle) {
			this.testingTitle = testingTitle;
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
		public Integer getCalcMethod() {
			return calcMethod;
		}
		public void setCalcMethod(Integer calcMethod) {
			this.calcMethod = calcMethod;
		}
		public Long getQuestionAmount() {
			return questionAmount;
		}
		public void setQuestionAmount(Long questionAmount) {
			this.questionAmount = questionAmount;
		}
		public Long getResId() {
			return resId;
		}
		public void setResId(Long resId) {
			this.resId = resId;
		}
		public Long getTestingId() {
			return testingId;
		}
		public void setTestingId(Long testingId) {
			this.testingId = testingId;
		}
		public String getResultTag() {
			return resultTag;
		}
		public void setResultTag(String resultTag) {
			this.resultTag = resultTag;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getThumbnail() {
			return thumbnail;
		}
		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
		public Byte getIsEnable() {
			return isEnable;
		}
		public void setIsEnable(Byte isEnable) {
			this.isEnable = isEnable;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		
	
}
