package com.depression.base.page;

/**
 * 分页工具类
 * 
 * @author fanxinhui
 * @version 1.0
 * @date 2016-4-19
 */
public class PageUtil
{

	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static final String PAGE_DESC = "↓";
	public static final String PAGE_ASC = "↑";
	public static final String PAGE_NULL = "&nbsp;&nbsp;";
	public static final String REQ_PAGE_KEY = "page";

	/**
	 * 初始化分页类
	 * 
	 * @param initPageSql
	 *            未分页的查询SQL
	 * @param totalCount
	 *            总行数
	 * @param index
	 *            分页状态
	 * @param value
	 *            只有在设置每页显示多少条时,值不会NULL,其它为NULL
	 */
	public static Page initPage(Long totalCount, Integer index, String value, Page reqPage)
	{
		Page page = null;
		if (index < 0)
		{
			page = new Page(totalCount);
		} else
		{
			/** 每页显示多少条 */
			Long everPage = null == value ? 10 : Long.parseLong(value);
			/** 获取req中的分页类,方便保存页面分页状态 */
			page = reqPage;
			page.setEveryPage(everPage);
			page.setTotalCount(totalCount);
		}
		return page;
	}

	/**
	 * 当页点击：首页,前一页,后一页,末页,排序,到第多少页时进行分页操作
	 * 
	 * @param index
	 *            分页状态
	 * @param value
	 *            排序字段名或者到第多少页
	 */
	public static Page execPage(Long totalCount, int index, String value, Page reqPage)
	{
		Page page = reqPage;
		if (index < 0)
		{
			page = new Page(totalCount);
		} else
		{
			page.pageState(index,value);
			/** 每页显示多少条 */
			Long everPage = null == value ? 10 : Long.parseLong(value);
			page.setEveryPage(everPage);
			page.setTotalCount(totalCount);
		}
		return page;
	}

}
