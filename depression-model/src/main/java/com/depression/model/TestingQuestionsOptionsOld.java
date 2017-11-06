package com.depression.model;

/**
 * 试题选项
 * 
 * @author fanxinhui
 * @date 2016/4/22
 */
public class TestingQuestionsOptionsOld extends Page
{
	private Integer optionsId;
	private Integer questionsId;
	private Integer sequence;
	private String title;
	private Integer score;
	private Integer optType;
	private Integer jumpToQuestionNo;
	private String jumpResultTag;
	private String isDelete;

	public Integer getOptionsId()
	{
		return optionsId;
	}

	public void setOptionsId(Integer optionsId)
	{
		this.optionsId = optionsId;
	}

	public Integer getQuestionsId()
	{
		return questionsId;
	}

	public void setQuestionsId(Integer questionsId)
	{
		this.questionsId = questionsId;
	}

	public Integer getSequence()
	{
		return sequence;
	}

	public void setSequence(Integer sequence)
	{
		this.sequence = sequence;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Integer getScore()
	{
		return score;
	}

	public void setScore(Integer score)
	{
		this.score = score;
	}

	public Integer getOptType()
	{
		return optType;
	}

	public void setOptType(Integer optType)
	{
		this.optType = optType;
	}

	public Integer getJumpToQuestionNo()
	{
		return jumpToQuestionNo;
	}

	public void setJumpToQuestionNo(Integer jumpToQuestionNo)
	{
		this.jumpToQuestionNo = jumpToQuestionNo;
	}

	public String getJumpResultTag()
	{
		return jumpResultTag;
	}

	public void setJumpResultTag(String jumpResultTag)
	{
		this.jumpResultTag = jumpResultTag;
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
