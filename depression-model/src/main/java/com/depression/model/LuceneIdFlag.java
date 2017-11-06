package com.depression.model;

public class LuceneIdFlag
{
	Long id;
	
	/*0 搜索展示可见 1不可见*/
	Integer flag;
	
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Integer getFlag()
	{
		return flag;
	}
	public void setFlag(Integer flag)
	{
		this.flag = flag;
	}
	
}
