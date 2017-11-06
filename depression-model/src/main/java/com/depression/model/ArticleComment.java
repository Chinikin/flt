package com.depression.model;

import java.util.Date;

/**
 * 文章评论
 * 
 * @author hongqian_li
 * @date 2016/04/28
 */
public class ArticleComment extends Page
{
	private Long commentId;
	private Long articleId;
	private Long mid;
	private String commentContent;
	private Long parentCommentId;
	private Date commentTime;
	private int isDelete;

	public ArticleComment()
	{
		super();
	}

	public Long getCommentId()
	{
		return commentId;
	}

	public void setCommentId(Long commentId)
	{
		this.commentId = commentId;
	}

	public Long getArticleId()
	{
		return articleId;
	}

	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getCommentContent()
	{
		return commentContent;
	}

	public void setCommentContent(String commentContent)
	{
		this.commentContent = commentContent;
	}

	public Long getParentCommentId()
	{
		return parentCommentId;
	}

	public void setParentCommentId(Long parentCommentId)
	{
		this.parentCommentId = parentCommentId;
	}

	public Date getCommentTime()
	{
		return commentTime;
	}

	public void setCommentTime(Date commentTime)
	{
		this.commentTime = commentTime;
	}

	public int getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(int isDelete)
	{
		this.isDelete = isDelete;
	}

	@Override
	public String toString()
	{
		return "ArticleComment [commentId=" + commentId + ", articleId=" + articleId + ", mid=" + mid + ", commentContent=" + commentContent + ", parentCommentId=" + parentCommentId
				+ ", commentTime=" + commentTime + ", isDelete=" + isDelete + "]";
	}

}
