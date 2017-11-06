/******************************************************************
 ** 类    名：FeedbackDAO
 ** 描    述：意见反馈DAO接口
 ** 创 建 者：fanxinhui
 ** 创建时间：2016-07-05
 ******************************************************************/
package com.depression.dao;

import java.util.List;
import java.util.Map;

import com.depression.model.Feedback;

/**
 * (FeedbackDAO)
 * 
 * @author fanxinhui
 * @version 1.0.0 2016-07-05
 */
public interface FeedbackDAO
{

	public void insertFeedback(Feedback feedback);

	public void updateFeedback(Feedback feedback);
	
	public void updateFeedbackEnableByFeedbackIds(List<String> feedbackIds);
	
	public void updateFeedbackDisableByFeedbackIds(List<String> feedbackIds);

	public void deleteFeedback(String feedbackId);

	public List<Feedback> getFeedbackList();

	public Feedback getFeedbackById(Integer feedbackId);
	
	public Feedback getValidFeedbackById(String feedbackId);

	public List<Feedback> getFeedbackListByQuery(Map<String, Object> paramMap);

	public List<Feedback> getFeedbackListByQueryFeedback(Feedback feedback);

	public List<Feedback> getFeedbackByIds(List<String> classList);

	// 分页数据
	public List<Feedback> getPageList(Feedback feedback);

	// 分页总条数
	public Long getPageCounts(Feedback feedback);

}
