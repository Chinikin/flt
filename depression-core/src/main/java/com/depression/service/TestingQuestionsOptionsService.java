package com.depression.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingQuestionsMapper;
import com.depression.dao.TestingQuestionsOptionsDAO;
import com.depression.dao.TestingQuestionsOptionsMapper;
import com.depression.model.TestingQuestionsOptions;
import com.depression.model.TestingQuestionsOptionsOld;

/**
 * 试题选项
 * 
 * @author fanxinhui
 * @date 2016/4/22
 */
@Service
public class TestingQuestionsOptionsService
{
	@Autowired
	private TestingQuestionsOptionsDAO testingQuestionsOptionsDAO;
	
	@Autowired
	private TestingQuestionsMapper testingQuestionsMapper;
	
	@Autowired
	private TestingQuestionsOptionsMapper testingQuestionsOptionsMapper;

	

	public void insertTestingQuestionsOptions(TestingQuestionsOptions testingQuestionsOptions)
	{
		this.testingQuestionsOptionsMapper.insertSelective(testingQuestionsOptions);
	}

	public void updateTestingQuestionsOptions(TestingQuestionsOptions testingQuestionsOptions){
		testingQuestionsOptions.setIsEnable((byte)0);
		this.testingQuestionsOptionsMapper.updateByPrimaryKeySelective(testingQuestionsOptions);
	}
	
	
	public void updateTestingQuestionsOptionsDisableByQuestionsId(Long questionsId)
	{
		testingQuestionsOptionsMapper.updateTestingQuestionsOptionsStatusByQuestionsId(questionsId, (byte)1);
	}

	
	
	
	public List<TestingQuestionsOptions> getValidTestingQuestionsOptionsListByQuestionsId(Long questionsId){
		TestingQuestionsOptions record=new TestingQuestionsOptions();
		record.setQuestionsId(questionsId);
		record.setIsEnable((byte)0);
		return testingQuestionsOptionsMapper.selectSelective(record);
	}

	public TestingQuestionsOptions getTestingQuestionsOptionsByQuestionsOptionsId(Long testingQuestionsOptionsId)
	{
		return testingQuestionsOptionsMapper.selectByPrimaryKey(testingQuestionsOptionsId);
	}

	

	

	public List<TestingQuestionsOptions> getTestingQuestionsOptionsListByQueryTestingQuestionsOptions(TestingQuestionsOptions testingQuestionsOptions)
	{
		return testingQuestionsOptionsMapper.selectSelective(testingQuestionsOptions);
	}

	

	// 分页数据
	public List<TestingQuestionsOptions> getPageList(TestingQuestionsOptions testingQuestionsOptions)
	{
		return this.testingQuestionsOptionsMapper.selectSelectiveWithPage(testingQuestionsOptions);
	}

	// 分页总条数
	public Integer getPageCounts(TestingQuestionsOptions testingQuestionsOptions)
	{
		return testingQuestionsOptionsMapper.countSelective(testingQuestionsOptions);
	}

	public void updateTestingQuestionsOptionsByOptionsId(List<Long> idList,
			Byte isDel) {
		 testingQuestionsOptionsMapper.updateTestingQuestionsOptionsByOptionsId(idList,isDel);
	}

}
