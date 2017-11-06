package com.depression.dao;

import java.util.List;
import java.util.Map;

import com.depression.model.TestingQuestionsOptionsOld;

/**
 * 试题选项
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
public interface TestingQuestionsOptionsDAO
{

	public int checkTestingQuestionsOptionsExits(TestingQuestionsOptionsOld testingQuestionsOptions);

	public void insertTestingQuestionsOptions(TestingQuestionsOptionsOld testingQuestionsOptions);

	public void updateTestingQuestionsOptions(TestingQuestionsOptionsOld testingQuestionsOptions);
	
	public void updateTestingQuestionsOptionsEnableByOptionsIds(List<String> optionsIds);
	
	public void updateTestingQuestionsOptionsDisableByOptionsIds(List<String> optionsIds);
	
	public void updateTestingQuestionsOptionsDisableByQuestionsId(String questionsId);

	public void deleteTestingQuestionsOptions(String testingQuestionsOptionsId);

	public List<TestingQuestionsOptionsOld> getTestingQuestionsOptionsList();
	
	public List<TestingQuestionsOptionsOld> getValidTestingQuestionsOptionsListByQuestionsId(Integer questionsId);

	public TestingQuestionsOptionsOld getTestingQuestionsOptionsByQuestionsOptionsId(String QuestionsOptionsId);

	public Long getCounts();

	public List<TestingQuestionsOptionsOld> getTestingQuestionsOptionsListByQuery(Map<String, Object> paramMap);

	public List<TestingQuestionsOptionsOld> getTestingQuestionsOptionsListByQueryTestingQuestionsOptions(TestingQuestionsOptionsOld testingQuestionsOptions);

	public List<TestingQuestionsOptionsOld> getTestingQuestionsOptionsByQuestionsOptionsIds(List<String> classList);

	// 分页数据
	public List<TestingQuestionsOptionsOld> getPageList(TestingQuestionsOptionsOld testingQuestionsOptions);

	// 分页总条数
	public Long getPageCounts(TestingQuestionsOptionsOld testingQuestionsOptions);

}
