package com.depression.model;

import java.util.Date;

/**
 * 会员咨询(member_advisory)
 * 
 * @author hongqian_li
 * @version 1.0.0 2016-05-09
 */
public class MemberAdvisory4Psycho  extends MemberAdvisory{

	/** 有用数量 */
	private Long commentPraiseNum;

	/** 评论内容 */
	private String commentContent;

	/** 创建时间 */
	private Date commentTime;
 
    
    public Long getCommentPraiseNum()
	{
		return commentPraiseNum;
	}

	public void setCommentPraiseNum(Long commentPraiseNum)
	{
		this.commentPraiseNum = commentPraiseNum;
	}

	public String getCommentContent()
	{
		return commentContent;
	}

	public void setCommentContent(String commentContent)
	{
		this.commentContent = commentContent;
	}

	public Date getCommentTime()
	{
		return commentTime;
	}

	public void setCommentTime(Date commentTime)
	{
		this.commentTime = commentTime;
	}
}