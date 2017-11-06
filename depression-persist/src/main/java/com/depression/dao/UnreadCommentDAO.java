package com.depression.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.UnreadComment;

public interface UnreadCommentDAO
{
	public int insertUnreadComment(UnreadComment unreadComment);
	
	public int deleteUnreadComment(List<Long> ids);
	
	public List<UnreadComment> selectByPage(UnreadComment unreadComment);
	
	public Long count(Long mid);
	
	public Long countUnread(Long mid);
	
	public Integer readComment(Long unreadId);
	
	public int deleteUnreadArticleComment(@Param("mid")Long mid, @Param("articleId")Long articleId);
	public int deleteUnreadTestingComment(@Param("mid")Long mid, @Param("testingId")Long testingId);
	public int deleteUnreadUpdateComment(@Param("mid")Long mid, @Param("updateId")Long updateId);	
	public int deleteUnreadAdvisoryComment(@Param("mid")Long mid, @Param("advisoryId")Long advisoryId);
}
