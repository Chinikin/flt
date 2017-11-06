package com.depression.dao;

import java.util.List;
import java.util.Map;

import com.depression.model.TestingResultForJumpOld;

/**
 * 跳转方式问卷测试结论
 * 
 * @author fanxinhui
 * @date 2016/6/27
 */
public interface TestingResultForJumpDAO
{
	public void insertTestingResultForJump(TestingResultForJumpOld testingResultForJump);

	public void updateTestingResultForJump(TestingResultForJumpOld testingResultForJump);

	public void deleteTestingResultForJump(String resultForJumpId);

	public Long getCounts();

	public List<TestingResultForJumpOld> getTestingResultForJumpList();

	public TestingResultForJumpOld getTestingResultForJumpByResultId(String resultId);

	public List<TestingResultForJumpOld> getTestingResultForJumpByResultIds(List<String> idsList);

	public List<TestingResultForJumpOld> getTestingResultForJumpByQuery(Map<String, Object> paramMap);

	public List<TestingResultForJumpOld> getTestingResultForJumpByQueryTestingResult(TestingResultForJumpOld testingResultForJump);

	public List<TestingResultForJumpOld> getPageList(TestingResultForJumpOld testingResultForJump);

	public Long getPageCounts(TestingResultForJumpOld testingResultForJump);
}
