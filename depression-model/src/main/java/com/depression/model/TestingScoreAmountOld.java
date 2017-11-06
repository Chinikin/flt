package com.depression.model;

import java.util.Date;

/**
 * 会员试卷测试总分
 * @author caizj
 *
 */
public class TestingScoreAmountOld extends Page
{
	private Long scoreId; 		//bigint(20) not null auto_increment comment '主键',
	private Long mid;			//bigint(20) comment '会员id，关联会员信息表',
	private Long testingId;	//bigint(20) comment '试卷id',
	private Integer score;		//int comment '总分，各选项分数总和',
	private Date   testTime;   //datetime comment '测试时间',
	private Integer   isDelete; //char(1) default '0' comment '默认0:不删除  1：删除',
	
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
	
	public TestingScoreAmountOld()
	{
		super();
	}

	public TestingScoreAmountOld(Long mid, Long testingId, Integer score, Date testTime)
	{
		super();
		this.mid = mid;
		this.testingId = testingId;
		this.score = score;
		this.testTime = testTime;
	}

	public Long getScoreId()
	{
		return scoreId;
	}

	public void setScoreId(Long scoreId)
	{
		this.scoreId = scoreId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Long getTestingId()
	{
		return testingId;
	}

	public void setTestingId(Long testingId)
	{
		this.testingId = testingId;
	}

	public Integer getScore()
	{
		return score;
	}

	public void setScore(Integer score)
	{
		this.score = score;
	}

	public Date getTestTime()
	{
		return testTime;
	}

	public void setTestTime(Date testTime)
	{
		this.testTime = testTime;
	}

	public Integer getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getLevel()
	{
		return level;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
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
