package com.depression.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingTypeDAO;
import com.depression.dao.TestingTypeMapper;
import com.depression.model.TestingType;

/**
 * 试卷分类
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
@Service
public class TestingTypeService
{
	@Autowired
	private TestingTypeDAO testingTypeDAO;
	
	@Autowired
	private TestingTypeMapper testingTypeMapper; 

	public int checkTestingTypeExits(TestingType testingType)
	{
		return this.testingTypeMapper.selectSelective(testingType).size();
	}

	public void insertTestingType(TestingType testingType)
	{
		this.testingTypeMapper.insertSelective(testingType);
	}

	public void updateTestingType(TestingType testingType)
	{
		this.testingTypeMapper.updateByPrimaryKeySelective(testingType);
	}
	

	public List<TestingType> getValidTestingTypeList()
	{
		TestingType t=new TestingType();
		t.setIsEnable((byte)0);
		return this.testingTypeMapper.selectSelective(t);
	}
	
	public TestingType getTestingTypeByTypeId(Long typeId)
	{
		return testingTypeMapper.selectByPrimaryKey(typeId);
	}
	

	public List<TestingType> getTestingTypeListByQueryTestingType(TestingType testingType)
	{
		return  testingTypeMapper.selectSelective(testingType);
	}


	// 分页数据
	public List<TestingType> getPageList(TestingType testingType)
	{
		
		return this.testingTypeMapper.getPageList(testingType);
	}

	// 分页总条数
	public Integer getPageCounts(TestingType testingType){
		return this.testingTypeMapper.getPageCounts(testingType);
	}

	public void updateTestingTypeStatusByTypeIds(List<Long> idList, Byte isDel) {
		testingTypeMapper.updateTestingTypeStatusByTypeIds(idList,isDel);
	}

	

}
