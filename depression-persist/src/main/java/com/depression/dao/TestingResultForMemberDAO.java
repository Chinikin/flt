package com.depression.dao;

import java.util.List;
import java.util.Map;

import com.depression.model.TestingResultForMember;

/**
 * 跳转方式问卷测试结论
 * 
 * @author fanxinhui
 * @date 2016/6/27
 */
public interface TestingResultForMemberDAO
{
	public void insertTestingResultForMember(TestingResultForMember testingResultForMember);

	public void updateTestingResultForMember(TestingResultForMember testingResultForMember);

	public void deleteTestingResultForMember(String resultForJumpId);

	public Long getCounts();

	public List<TestingResultForMember> getTestingResultForMemberList();

	public TestingResultForMember getTestingResultForMemberByResultId(String resultId);

	public List<TestingResultForMember> getTestingResultForMemberByResultIds(List<String> idsList);

	public List<TestingResultForMember> getTestingResultForMemberByQuery(Map<String, Object> paramMap);

	public List<TestingResultForMember> getTestingResultForMemberByQueryTestingResult(TestingResultForMember testingResultForMember);

	public List<TestingResultForMember> getPageList(TestingResultForMember testingResultForMember);

	public Long getPageCounts(TestingResultForMember testingResultForMember);
}
