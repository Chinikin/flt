package com.depression.model;

/**
 * 针对分页显示
 * 
 * @author xm
 * 
 */
public class Page
{

	/** 每页显示条数 */
	private Integer pageSize = 10;

	/** set:当前页码 ,get:当前分页起始数 */
	private Integer pageIndex = 1;

	/** 分页起始页 */
	private Integer pageStartNum;

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public Integer getPageIndex()
	{
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	public Integer getPageStartNum()
	{
		if (pageSize == null || pageIndex == null)
		{
			pageStartNum = null;
		} else
		{
			if (pageIndex <= 0)
			{
				pageStartNum = 0;
			} else
			{
				pageStartNum = (pageIndex - 1) * pageSize;
			}
		}
		return pageStartNum;
	}

	public void setPageStartNum(Integer pageStartNum)
	{
		this.pageStartNum = pageStartNum;
	}
}
