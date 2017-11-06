/******************************************************************
 ** 类    名：FeedbackService
 ** 描    述：意见反馈service接口
 ** 创 建 者：fanxinhui
 ** 创建时间：2016-07-05
 ******************************************************************/
package com.depression.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.FeedbackDAO;
import com.depression.model.Feedback;

/**
 * (FeedbackService)
 * 
 * @author fanxinhui
 * @version 1.0.0 2016-07-05
 */
@Service
public class FeedbackService
{
	@Autowired
	private FeedbackDAO feedbackDAO;

	public void insertFeedback(Feedback feedback)
	{
		feedback.setfTime(new Date());
		feedback.setIsDelete(0);
		this.feedbackDAO.insertFeedback(feedback);
	}

	public void updateFeedback(Feedback feedback)
	{
		this.feedbackDAO.updateFeedback(feedback);
	}

	public void updateFeedbackEnableByFeedbackIds(List<String> feedbackIds)
	{
		this.feedbackDAO.updateFeedbackEnableByFeedbackIds(feedbackIds);
	}

	public void updateFeedbackDisableByFeedbackIds(List<String> feedbackIds)
	{
		this.feedbackDAO.updateFeedbackDisableByFeedbackIds(feedbackIds);
	}

	public void deleteFeedback(String feedbackId)
	{
		this.feedbackDAO.deleteFeedback(feedbackId);
	}

	public List<Feedback> getFeedbackList()
	{
		return this.feedbackDAO.getFeedbackList();
	}

	public Feedback getFeedbackById(Integer feedbackId)
	{
		return (Feedback) this.feedbackDAO.getFeedbackById(feedbackId);
	}

	public Feedback getValidFeedbackById(String feedbackId)
	{
		return (Feedback) this.feedbackDAO.getValidFeedbackById(feedbackId);
	}

	public List<Feedback> getFeedbackListByQuery(Map<String, Object> paramMap)
	{
		return (List<Feedback>) this.feedbackDAO.getFeedbackListByQuery(paramMap);
	}

	public List<Feedback> getFeedbackListByQueryFeedback(Feedback feedback)
	{
		return (List<Feedback>) this.feedbackDAO.getFeedbackListByQueryFeedback(feedback);
	}

	public List<Feedback> getFeedbackByIds(List<String> classList)
	{
		return (List<Feedback>) this.feedbackDAO.getFeedbackByIds(classList);
	}

	// 分页数据
	public List<Feedback> getPageList(Feedback feedback)
	{
		return this.feedbackDAO.getPageList(feedback);
	}

	// 分页总条数
	public Long getPageCounts(Feedback feedback)
	{
		return this.feedbackDAO.getPageCounts(feedback);
	}

}
