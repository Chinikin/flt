package com.depression.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.UnreadCommentDAO;
import com.depression.model.UnreadComment;

@Service
public class UnreadCommentService
{
	@Autowired
	UnreadCommentDAO dao;
	
	public int addArticleUnreadComment(UnreadComment unreadComment)
	{
		unreadComment.setType(UnreadComment.TYPE_ARTICLE);
		return dao.insertUnreadComment(unreadComment);
	}
	
	public int addTestingUnreadComment(UnreadComment unreadComment)
	{
		unreadComment.setType(UnreadComment.TYPE_TESTING);
		return dao.insertUnreadComment(unreadComment);
	}

	public int addUpdateCircleUnreadComment(UnreadComment unreadComment)
	{
		unreadComment.setType(UnreadComment.TYPE_UPDATE_CIRCLE);
		return dao.insertUnreadComment(unreadComment);
	}
	
	public int addUpdateBottleUnreadComment(UnreadComment unreadComment)
	{
		unreadComment.setType(UnreadComment.TYPE_UPDATE_BOTTLE);
		return dao.insertUnreadComment(unreadComment);
	}
	
	public int addAdvisoryUnreadComment(UnreadComment unreadComment)
	{
		unreadComment.setType(UnreadComment.TYPE_ADVISORY);
		return dao.insertUnreadComment(unreadComment);
	}
	
	public List<UnreadComment> queryUnreadCommentByPage(UnreadComment unreadComment)
	{
		return dao.selectByPage(unreadComment);
	}
	
	public Long queryUnreadCommentCount(Long mid)
	{
		return dao.count(mid);
	}
	
	//获取未读评论数量
	public Long countUnread(Long mid)
	{
		return dao.countUnread(mid);
	}
	
	//设置评论已读
	public Integer readComment(Long unreadId)
	{
		return dao.readComment(unreadId);
	}
	
	public int deleteUnreadArticleComment(Long mid,Long articleId)
	{
		return dao.deleteUnreadArticleComment(mid, articleId);
	}
	public int deleteUnreadTestingComment(Long mid, Long testingId)
	{
		return dao.deleteUnreadTestingComment(mid, testingId);
	}
	public int deleteUnreadUpdateComment(Long mid, Long updateId)
	{
		return dao.deleteUnreadUpdateComment(mid, updateId);
	}
	
	public int deleteUnreadAdvisoryComment(Long mid, Long advisoryId)
	{
		return dao.deleteUnreadAdvisoryComment(mid, advisoryId);
	}
	
	public int deleteUnreadCommentById(Long unreadId)
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(unreadId);
		return dao.deleteUnreadComment(ids);
	}
}
