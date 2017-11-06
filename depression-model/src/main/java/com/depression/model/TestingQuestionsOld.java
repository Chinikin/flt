package com.depression.model;

/**
 * 试卷题目
 * 
 * @author fanxinhui
 * @date 2016/4/22
 */
public class TestingQuestionsOld extends Page
{
	private Integer questionsId;
	private Long testingId;
	private Integer subjectSeqNum;
	private String questionsTitle;
	private String isDelete;
	public Integer getQuestionsId()
	{
		return questionsId;
	}
	public void setQuestionsId(Integer questionsId)
	{
		this.questionsId = questionsId;
	}
	public Long getTestingId()
	{
		return testingId;
	}
	public void setTestingId(Long testingId)
	{
		this.testingId = testingId;
	}
	public Integer getSubjectSeqNum()
	{
		return subjectSeqNum;
	}
	public void setSubjectSeqNum(Integer subjectSeqNum)
	{
		this.subjectSeqNum = subjectSeqNum;
	}
	public String getQuestionsTitle()
	{
		return questionsTitle;
	}
	public void setQuestionsTitle(String questionsTitle)
	{
		this.questionsTitle = questionsTitle;
	}
	public String getIsDelete()
	{
		return isDelete;
	}
	public void setIsDelete(String isDelete)
	{
		this.isDelete = isDelete;
	}

}
