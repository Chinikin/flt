package com.depression.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.TestingSectionOld;

/**
 * 测试结果得分区间DAO
 * @author caizj
 *
 */
public interface TestingSectionDAO
{
	public void insertTestingSection(TestingSectionOld testingSection);
	
	public void updateTestingSection(TestingSectionOld testingSection);
	
	public void deleteTestingSection(String sectionId);
	
	public TestingSectionOld checkTestingSectionExist(TestingSectionOld testingSection);
	
	public List<TestingSectionOld> getTestingSectionList();
	
	public Long getCounts();
	
	public TestingSectionOld getTestingSectionBySectionId(String sectionId);
	
	public List<TestingSectionOld> getTestingSectionByTestingId(String testingId);
	
	public List<TestingSectionOld> getTestingSectionByTestingIdAndScore(@Param("testingId")String testingId, 
			@Param("score")String score);
}
