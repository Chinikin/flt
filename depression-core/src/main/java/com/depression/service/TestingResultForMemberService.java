package com.depression.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingResultForMemberDAO;
import com.depression.dao.TestingResultForMemberMapper;
import com.depression.model.TestingResultForMember;

/**
 * 跳转方式问卷测试结论
 * 
 * @author fanxinhui
 * @date 2016/6/27
 */
@Service
public class TestingResultForMemberService
{
	@Autowired
	private TestingResultForMemberDAO testingResultForMemberDAO;
	
	@Autowired
	private TestingResultForMemberMapper testingResultForMemberMapper;
	
	public void insertTestingResultForMember(TestingResultForMember testingResultForMember)
	{
		this.testingResultForMemberMapper.insertSelective(testingResultForMember);
	}
	
	public void updateTestingResultForMember(TestingResultForMember testingResultForMember)
	{
		this.testingResultForMemberMapper.updateByPrimaryKeySelective(testingResultForMember);
		
	}
	
	/*public void deleteTestingResultForMember(String resultId)
	{
		this.testingResultForMemberDAO.deleteTestingResultForMember(resultId);
	}*/
	
	/*public Long getCounts()
	{
		return this.testingResultForMemberDAO.getCounts();
	}
	
	public List<TestingResultForMember> getTestingResultForMemberList()
	{
		return this.testingResultForMemberDAO.getTestingResultForMemberList();
	}
	
	public TestingResultForMember getTestingResultForMemberByResultId(String resultId)
	{
		return this.testingResultForMemberDAO.getTestingResultForMemberByResultId(resultId);
	}
	
	public List<TestingResultForMember> getTestingResultForMemberByResultIds(List<String> idsList)
	{
		return this.testingResultForMemberDAO.getTestingResultForMemberByResultIds(idsList);
	}
	
	public List<TestingResultForMember> getTestingResultForMemberByQuery(Map<String, Object> paramMap)
	{
		return this.testingResultForMemberDAO.getTestingResultForMemberByQuery(paramMap);
	}*/
	
	
	public List<TestingResultForMember> getTestingResultForMemberByQueryTestingResult(TestingResultForMember testingResultForMember)
	{
		return testingResultForMemberMapper.selectSelective(testingResultForMember);
	}
	
	/*public List<TestingResultForMember> getPageList(TestingResultForMember testingResultForMember)
	{
		return this.testingResultForMemberDAO.getPageList(testingResultForMember);
	}
	
	public Long getPageCounts(TestingResultForMember testingResultForMember)
	{
		return this.testingResultForMemberDAO.getPageCounts(testingResultForMember);
	}*/
	
}
