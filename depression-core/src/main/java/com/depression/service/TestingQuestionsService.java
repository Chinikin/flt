package com.depression.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingQuestionsDAO;
import com.depression.dao.TestingQuestionsMapper;
import com.depression.model.TestingQuestions;

/**
 * 试卷分类
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
@Service
public class TestingQuestionsService
{
	@Autowired
	private TestingQuestionsDAO testingQuestionsDAO;
	
	@Autowired
	private TestingQuestionsMapper testingQuestionsMapper;

	

	public Integer insertTestingQuestions(TestingQuestions testingQuestions)
	{
		return this.testingQuestionsMapper.insertSelective(testingQuestions);
	}

	public void updateTestingQuestions(TestingQuestions testingQuestions)
	{
		testingQuestions.setIsEnable(testingQuestionsMapper.selectByPrimaryKey(testingQuestions.getQuestionsId()).getIsEnable());
		testingQuestionsMapper.updateByPrimaryKeySelective(testingQuestions);
	}

	

	public TestingQuestions getTestingQuestionsByQuestionsId(Long testingQuestionsId){
		return testingQuestionsMapper.selectByPrimaryKey(testingQuestionsId);
	}
	
	public List<TestingQuestions> getValidTestingQuestionsListByTestingId(Long testingId)
	{
		TestingQuestions questions=new TestingQuestions();
		questions.setTestingId(testingId);
		questions.setIsEnable((byte)0);
		return (List<TestingQuestions>) this.testingQuestionsMapper.selectSelective(questions);
	}

	
	public Integer getValidQueCountsByTestingId(Long testingId)
	{
		TestingQuestions testingQuestions=new TestingQuestions();
		testingQuestions.setTestingId(testingId);
		testingQuestions.setIsEnable((byte)0);
		return this.testingQuestionsMapper.selectSelective(testingQuestions).size();
	}


	public List<TestingQuestions> getTestingQuestionsListByQueryTestingQuestions(TestingQuestions testingQuestions){
		return testingQuestionsMapper.selectSelective(testingQuestions);
	}

	public List<TestingQuestions> getTestingQuestionsByQuestionsIds(List<String> classList)
	{
		return (List<TestingQuestions>) this.testingQuestionsDAO.getTestingQuestionsByQuestionsIds(classList);
	}

	// 分页数据
	public List<TestingQuestions> getPageList(TestingQuestions testingQuestions)
	{
		return testingQuestionsMapper.selectSelectiveWithPage(testingQuestions);
	}
	
	//查询所有
	public List<TestingQuestions> getTestingQuestionsList(TestingQuestions testingQuestions)
	{
		return testingQuestionsMapper.selectSelective(testingQuestions);
	}
	
	
	// 分页总条数
	public Integer getPageCounts(TestingQuestions testingQuestions){
		return testingQuestionsMapper.countSelective(testingQuestions);
	}

	public void updateTestingQuestionsStatusByQuestionsIds(List<Long> idList,
			Byte isDel) {
		testingQuestionsMapper.updateTestingQuestionsStatusByQuestionsIds(idList,isDel);
	}

	public List<TestingQuestions> getTestingQuestionsByTestingId(Long testingId) {
		TestingQuestions q=new TestingQuestions();
		q.setTestingId(testingId);
		return testingQuestionsMapper.selectSelective(q);
	}

}
