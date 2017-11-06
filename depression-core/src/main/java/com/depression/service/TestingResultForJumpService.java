package com.depression.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingResultForJumpDAO;
import com.depression.dao.TestingResultForJumpMapper;
import com.depression.model.TestingResultForJump;
import com.depression.model.TestingResultForJumpOld;
import com.depression.model.eap.dto.TestingResultCustom;

/**
 * 跳转方式问卷测试结论
 * 
 * @author fanxinhui
 * @date 2016/6/27
 */
@Service
public class TestingResultForJumpService
{
	@Autowired
	private TestingResultForJumpDAO testingResultForJumpDAO;
	
	@Autowired
	private TestingResultForJumpMapper testingResultForJumpMapper;
	
	public void insertTestingResultForJump(TestingResultForJump testingResultForJump)
	{
		this.testingResultForJumpMapper.insertSelective(testingResultForJump);
	}
	
	public void updateTestingResultForJump(TestingResultForJump testingResultForJump)
	{	
		this.testingResultForJumpMapper.updateByPrimaryKeySelective(testingResultForJump);
	}
	
	
	public TestingResultForJump getTestingResultForJumpByResultId(Long resultId)
	{
		return this.testingResultForJumpMapper.selectByPrimaryKey(resultId);
	}
	
	
	public List<TestingResultForJump> getTestingResultForJumpByQueryTestingResult(TestingResultForJump testingResultForJump)
	{
		return this.testingResultForJumpMapper.selectSelective(testingResultForJump);
	}

	public List<TestingResultCustom> getTestingResultForJumpByQuery(
			Map<String, Object> query) {
		return testingResultForJumpMapper.getTestingResultForJumpByQuery(query);
	}
	
	
}
