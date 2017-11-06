package com.depression.model.web.dto;

import java.util.Date;

import com.depression.model.TestingScoreAmount;

public class WebTestingScoreAmountDTO extends TestingScoreAmount {
	 	private Long scoreId;
	    
	    /* @Comment(会员id，关联会员信息表) */
	    private Long mid;

	    /* @Comment(试卷id) */
	    private Long testingId;

	    
	    /* @Comment(总分，各选项分数总和) */
	    private Integer score;

	    
	    /* @Comment(测试时间) */
	    private Date testTime;

	    /* @Comment() */
	    private Byte isEnable;
	

	public Long getScoreId() {
			return scoreId;
		}
		public void setScoreId(Long scoreId) {
			this.scoreId = scoreId;
		}
		public Long getMid() {
			return mid;
		}
		public void setMid(Long mid) {
			this.mid = mid;
		}
		public Long getTestingId() {
			return testingId;
		}
		public void setTestingId(Long testingId) {
			this.testingId = testingId;
		}
		public Integer getScore() {
			return score;
		}
		public void setScore(Integer score) {
			this.score = score;
		}
		public Date getTestTime() {
			return testTime;
		}
		public void setTestTime(Date testTime) {
			this.testTime = testTime;
		}
		public Byte getIsEnable() {
			return isEnable;
		}
		public void setIsEnable(Byte isEnable) {
			this.isEnable = isEnable;
		}
		// 以下字段用于描述测试结果
		private String  level;			
		private String 	detail; 
		// 问卷标题
		private String 	title;
		private String filePath;// 图片网络路径
		private String filePathMobile;
		private String filePathSlide;
		private Integer calcMethod;//问卷类型：0计分；1跳转
		private Long questionAmount;//试题总数
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
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
}
