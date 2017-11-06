package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingCommentDAO;
import com.depression.dao.TestingCommentMapper;
import com.depression.model.TestingComment;

@Service
public class TestingCommentService
{
	@Autowired
	TestingCommentDAO testingCommentDAO;
	
	@Autowired
	TestingCommentMapper testingCommentMapper;

	public int insert(TestingComment testingComment)
	{
		testingComment.setIsDelete((byte)0);
		testingComment.setCommentTime(new Date());
		return testingCommentMapper.insertSelective(testingComment);
	}

	public int update(TestingComment testingComment)
	{
		testingComment.setCommentTime(new Date());
		testingComment.setIsEnable((byte)0);
		return testingCommentMapper.updateByPrimaryKeySelective(testingComment);
	}

	public int delete(TestingComment testingComment)
	{
		testingComment.setCommentTime(new Date());
		testingComment.setIsEnable((byte)1);
		return testingCommentMapper.updateByPrimaryKeySelective(testingComment);
	}


	public TestingComment getTestingCommentByCommentId(Long testingCommentId)
	{
		return testingCommentMapper.selectByPrimaryKey(testingCommentId);
	}
	
	public List<TestingComment> selectByPage(TestingComment testingComment)
	{
		return testingCommentMapper.selectByPageCommentTimeDesc(testingComment);
	}

	public Integer getPageCounts(TestingComment testingComment)
	{
		return testingCommentMapper.countSelective(testingComment);
	}
	
	
}
