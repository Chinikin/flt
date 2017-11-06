package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingSectionDAO;
import com.depression.dao.TestingSectionMapper;
import com.depression.model.TestingSection;
import com.depression.model.TestingSectionOld;

/**
 * 测试结果得分区间
 * @author caizj
 *
 */
@Service
public class TestingSectionService
{
	@Autowired
	private TestingSectionDAO testingSectionDAO;
	
	@Autowired
	private TestingSectionMapper testingSectionMapper;
	
	
	public void insertTestingSection(TestingSection testingSection)
	{
		this.testingSectionMapper.insertSelective(testingSection);
	}
	
	public void updateTestingSection(TestingSection testingSection)
	{
		this.testingSectionMapper.updateByPrimaryKeySelective(testingSection);
	}
	
	/*public void deleteTestingSection(String sectionId)
	{
		this.testingSectionDAO.deleteTestingSection(sectionId);
	}*/
	
	public List<TestingSection> checkTestingSectionExist(TestingSection testingSection){
		return this.testingSectionMapper.selectSelective(testingSection);
	}
	
	/*public List<TestingSectionOld> getTestingSectionList()
	{
		return this.testingSectionDAO.getTestingSectionList();
	}
	
	public Long getCounts()
	{
		return this.testingSectionDAO.getCounts();
	}
	*/
	public TestingSection getTestingSectionBySectionId(Long sectionId)
	{
		return this.testingSectionMapper.selectByPrimaryKey(sectionId);
	}
	
	public List<TestingSection> getTestingSectionByTestingId(Long testingId){
		TestingSection testingSection =new TestingSection();
		testingSection.setTestingId(testingId);
		return this.testingSectionMapper.selectSelective(testingSection);
	}
	
	public List<TestingSection> getTestingSectionByTestingIdAndScore(Long testingId, Integer score){
		return this.testingSectionMapper.getTestingSectionByTestingIdAndScore(testingId, score);
	}
}
