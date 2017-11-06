package com.depression.dao;

import java.util.List;

import com.depression.model.TestingOld;
import com.depression.model.TestingComment;

public interface TestingCommentDAO
{

	public int insert(TestingComment testingComment);

	public int update(TestingComment testingComment);

	public int delete(TestingComment testingComment);

	public TestingComment getTestingComment(TestingComment testingComment);

	// 分页数据
	public List<TestingComment> selectByPage(TestingComment testingComment);
	
	// 分页总条数
	public Long getPageCounts(TestingComment testingComment);

}
