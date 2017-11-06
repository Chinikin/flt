package com.depression.model;

/**
 * 测试结果
 * @author caizj
 * @date 2016/4/28
 */
public class TestingResultOld extends Page
{
	private Long resultId; // COMMENT '主键',
	private Long mid; // COMMENT '会员id，关联会员信息表',
	private Long testingId;
	private Long questionsId;
	private Long optionsId;// COMMENT '试题选择id，关联试题选项表',
	private Integer isDelete; // COMMENT '默认0:不删除 1：删除',
	

	public TestingResultOld()
	{
		super();
	}

	
	
	public TestingResultOld(Long mid, Long testingId, Long questionsId)
	{
		super();
		this.mid = mid;
		this.testingId = testingId;
		this.questionsId = questionsId;
	}



	public TestingResultOld(Long mid, Long testingId, Long questionsId, Long optionsId)
	{
		super();
		this.mid = mid;
		this.testingId = testingId;
		this.questionsId = questionsId;
		this.optionsId = optionsId;
	}

	public TestingResultOld(Long mid, Long testingId, Long questionsId, Long optionsId, Integer isDelete)
	{
		super();
		this.mid = mid;
		this.testingId = testingId;
		this.questionsId = questionsId;
		this.optionsId = optionsId;
		this.isDelete = isDelete;
	}



	public Long getResultId()
	{
		return resultId;
	}


	public void setResultId(Long resultId)
	{
		this.resultId = resultId;
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


	public Long getQuestionsId()
	{
		return questionsId;
	}


	public void setQuestionsId(Long questionsId)
	{
		this.questionsId = questionsId;
	}


	public Long getOptionsId()
	{
		return optionsId;
	}


	public void setOptionsId(Long optionsId)
	{
		this.optionsId = optionsId;
	}


	public Integer getIsDelete()
	{
		return isDelete;
	}


	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}

}
