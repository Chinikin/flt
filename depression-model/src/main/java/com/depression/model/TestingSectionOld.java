package com.depression.model;

/**
 * 测试结果得分区间
 * @author caizj
 *
 */
public class TestingSectionOld extends Page
{
	private Long sectionId; 		//bigint(20) not null auto_increment comment '主键',
	private Long testingId; 		//bigint(20) comment '问卷id，关联测试问卷表',
	private Integer greaterThan; 	//int comment '得分高于',
	private Integer lessThan; 		//int comment '得分低于',
	private String  level;			//varchar(3) comment '结果等级: A,B,C...等等',
	private String 	detail;  		//text comment '结果详细描述',
	private Integer	isDelete; 		//char(1) default '0' comment '默认0:不删除  1：删除',
	
	public TestingSectionOld()
	{
		super();
	}

	public TestingSectionOld(Long testingId, Integer greaterThan, Integer lessThan, String level, String detail)
	{
		super();
		this.testingId = testingId;
		this.greaterThan = greaterThan;
		this.lessThan = lessThan;
		this.level = level;
		this.detail = detail;
	}
	
	

	public Long getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(Long sectionId)
	{
		this.sectionId = sectionId;
	}

	public Long getTestingId()
	{
		return testingId;
	}

	public void setTestingId(Long testingId)
	{
		this.testingId = testingId;
	}

	public Integer getGreaterThan()
	{
		return greaterThan;
	}

	public void setGreaterThan(Integer greaterThan)
	{
		this.greaterThan = greaterThan;
	}

	public Integer getLessThan()
	{
		return lessThan;
	}

	public void setLessThan(Integer lessThan)
	{
		this.lessThan = lessThan;
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

	public Integer getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}
	
	
}
