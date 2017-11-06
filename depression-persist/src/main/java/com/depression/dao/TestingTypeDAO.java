package com.depression.dao;

import java.util.List;
import java.util.Map;

import com.depression.model.TestingType;

/**
 * 试卷分类
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
public interface TestingTypeDAO
{

	public int checkTestingTypeExits(TestingType testingType);

	public void insertTestingType(TestingType testingType);

	public void updateTestingType(TestingType testingType);
	
	public void updateTestingTypeEnableByTypeIds(List<String> typeIds);
	
	public void updateTestingTypeDisableByTypeIds(List<String> typeIds);

	public void deleteTestingType(String testingTypeId);

	public List<TestingType> getTestingTypeList();
	
	public List<TestingType> getValidTestingTypeList();

	public TestingType getTestingTypeByTypeId(String typeId);

	public Long getCounts();

	public List<TestingType> getTestingTypeListByQuery(Map<String, Object> paramMap);

	public List<TestingType> getTestingTypeListByQueryTestingType(TestingType testingType);

	public List<TestingType> getTestingTypeByTypeIds(List<String> classList);

	// 分页数据
	public List<TestingType> getPageList(TestingType testingType);

	// 分页总条数
	public Long getPageCounts(TestingType testingType);

}
