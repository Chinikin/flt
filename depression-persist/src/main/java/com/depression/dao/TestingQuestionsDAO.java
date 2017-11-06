package com.depression.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.depression.model.TestingQuestions;

/**
 * 试卷分类
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
public interface TestingQuestionsDAO
{

	public int checkTestingQuestionsExits(TestingQuestions testingQuestions);

	public Integer insertTestingQuestions(TestingQuestions testingQuestions);

	public void updateTestingQuestions(TestingQuestions testingQuestions);
	
	public void updateTestingQuestionsEnableByQuestionsIds(List<String> questionsIds);
	
	public void updateTestingQuestionsDisableByQuestionsIds(List<String> questionsIds);

	public void deleteTestingQuestions(String testingQuestionsId);

	public List<TestingQuestions> getTestingQuestionsList();
	
	public List<TestingQuestions> getValidTestingQuestionsListByTestingId(Integer testingId);

	public TestingQuestions getTestingQuestionsByQuestionsId(String QuestionsId);

	public Long getCounts();
	
	public Long getValidQueCountsByTestingId(@Param("testingId")Long testingId);

	public List<TestingQuestions> getTestingQuestionsListByQuery(Map<String, Object> paramMap);

	public List<TestingQuestions> getTestingQuestionsListByQueryTestingQuestions(TestingQuestions testingQuestions);

	public List<TestingQuestions> getTestingQuestionsByQuestionsIds(List<String> classList);

	// 分页数据
	public List<TestingQuestions> getPageList(TestingQuestions testingQuestions);

	// 分页总条数
	public Long getPageCounts(TestingQuestions testingQuestions);

}
