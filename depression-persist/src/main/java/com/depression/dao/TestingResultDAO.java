package com.depression.dao;

import java.util.List;
import java.util.Map;

import com.depression.model.TestingResultOld;

/**
 * 测试结果DAO
 * @author caizj
 *
 */
public interface TestingResultDAO
{
	public void insertTestingResult(TestingResultOld testingResult);
	
	public void updateTestingResult(TestingResultOld testingResult);
	
	public void updateTestingResultDisableByIds(List<String> resIds);
	
	public void deleteTestingResult(String resultId);
	
	public Long getCounts();
	
	public List<TestingResultOld> getTestingResultList();
	
	public TestingResultOld getTestingResultByResultId(String resultId);
	
	public List<TestingResultOld> getTestingResultByResultIds(List<String> idsList);
	
	public List<TestingResultOld> getTestingResultByQuery(Map<String, Object> paramMap);
	
	public List<TestingResultOld> getTestingResultByQueryTestingResult(TestingResultOld testingResult);
	
	public List<TestingResultOld> getValidTestingResultByQueryTestingResult(TestingResultOld testingResult);
	
	public List<TestingResultOld> getPageList(TestingResultOld testingResult);
	
	public Long getPageCounts(TestingResultOld testingResult);
}
