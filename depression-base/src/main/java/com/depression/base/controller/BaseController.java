package com.depression.base.controller;

import javax.servlet.http.HttpServletRequest;

import com.depression.base.page.Page;
import com.depression.base.page.PageState;
import com.depression.base.page.PageUtil;

/**
 * 控制器基础类
 * 
 * @author fanxinhui
 * @version 1.0
 * @date 2016-4-19
 * 
 */
public class BaseController
{

	/**
	 * 三层分页语句 子类在展现数据前,进行分页计算!
	 * 
	 * @param querySql
	 *            查询的SQL语句,未进行分页
	 * @param totalCount
	 *            根据查询SQL获取的总条数
	 * @param columnNameDescOrAsc
	 *            列名+排序方式 : ID DESC or ASC
	 */
	protected Page executePage(HttpServletRequest request, Long totalCount)
	{
		if (null == totalCount)
		{
			totalCount = 0L;
		}
		/** 页面状态,这个状态是分页自带的,与业务无关 */
		String pageAction = request.getParameter("pageAction");
		String value = request.getParameter("pageKey");

		/** 获取下标判断分页状态 */
		int index = PageState.getOrdinal(pageAction);

		Page page = null;
		/**
		 * index < 1 只有二种状态 1 当首次调用时,分页状态类中没有值为 NULL 返回 -1 2 当页面设置每页显示多少条: index=0,当每页显示多少条时,分页类要重新计算
		 * */
		Page reqPage = getPage(request);

		if (index < 1)
		{
			page = PageUtil.initPage(totalCount, index, value, reqPage);
		} else
		{
			page = PageUtil.execPage(totalCount, index, value, reqPage);
		}
		if(page.getCurrentPage()==0){
			page.setCurrentPage(1L);
		}
		setSession(request, page);
		return page;
	}

	private Page getPage(HttpServletRequest request)
	{
		Page page = (Page) request.getSession().getAttribute(PageUtil.REQ_PAGE_KEY);
		if (page == null)
		{
			page = new Page();
		}
		return page;
	}

	private void setSession(HttpServletRequest request, Page page)
	{
		request.getSession().setAttribute(PageUtil.REQ_PAGE_KEY, page);
	}

}
