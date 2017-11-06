package com.depression.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingMapper;
import com.depression.dao.TestingResultForMemberMapper;
import com.depression.dao.TestingResultMapper;
import com.depression.model.Page;
import com.depression.model.Testing;
import com.depression.model.TestingCustom;
import com.depression.model.TestingResultForMember;
import com.depression.model.TestingType;

/**
 * 试卷分类
 * 
 * @author fanxinhui
 * @date 2016/4/20
 */
@Service
public class TestingService
{	
	@Autowired
	private TestingMapper testingMapper;
	
	@Autowired
	private TestingResultMapper testingResultMapper;
	
	@Autowired
	private TestingResultForMemberMapper testingResultForMemberMapper;


	public void insertTesting(Testing testing){
		this.testingMapper.insertSelective(testing);
	}

	public void updateTesting(Testing testing){
			Testing test=testingMapper.selectByPrimaryKey(testing.getTestingId());
			testing.setIsEnable(test.getIsEnable());
			testingMapper.updateByPrimaryKeySelective(testing);
	}

	

	public Testing getTestingById(Long testingId)
	{
		return testingMapper.selectByPrimaryKey(testingId);
	}

	public Testing getValidTestingById(Long testingId){
		
		return  this.testingMapper.selectByPrimaryKey(testingId);
	}


	// 分页数据
	public List<Testing> getPageList(Testing testing){
		
		return testingMapper.getPageList(testing);
	}
	
	// 分页数据
		public List<Testing> getPageListOrderBy(Testing testing){
			
			return testingMapper.getPageListOrderBy(testing);
		}

	// 分页总条数
	public Long getPageCounts(Testing testing){
		return testingMapper.getPageCounts(testing);
	}

	// 获取最热门的测试问卷列表
	public List<Testing> getTopTesting(Integer size)
	{
		return this.testingMapper.getTopTesting(size);
	}

	// 查询热门的测试问卷列表（学生测试）
	public List<Testing> getHotTestingForStuTest(Integer size)
	{
		return this.testingMapper.getHotTestingForStuTest(size);
	}
	
	public List<Testing> getByKeysWithPageEnabled(List<Long> ids, Integer pageIndex,
			Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		if(ids.size()==0)
		{
			return new ArrayList<Testing>();
		}
		return testingMapper.selectByPrimaryKeysWithPageEnabled(ids, page.getPageStartNum(), pageSize);
	}
	
	public Integer getCountByPrimaryKeysEnabled(List<Long> ids)
	{
		if(ids.size()==0)
		{
			return 0;
		}
		return testingMapper.countByPrimaryKeysEnabled(ids);
	}

       /**
	 * 根据标题精确查找问卷
	 * 
	 * @param title
	 * @return
	 */
	public List<Testing> getTestingByTitle(String title)
	{
		return this.testingMapper.getTestingByTitle(title);
	}
	
	
	/**
	 * 
	 */
	public void save(Testing record){
		
			
			
			if (record.getCalcMethod() != null)
			{
				record.setCalcMethod(record.getCalcMethod());
			}
			else
			{
				// 若传入参数calcMethod为空，默认问卷为计分方式
				record.setCalcMethod(0);
			}
			testingMapper.insertSelective(record);
	}

	public void updateTestingStatus(String testingIds, byte isDel) {
		// 处理记录id
		testingIds = testingIds.replace("[", "").replace("]", "");
		String[] idArr = testingIds.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (int idx = 0; idx < idArr.length; idx++)
		{
			if (idArr[idx] != null && !idArr[idx].equals(""))
			{
				idList.add(Long.parseLong(idArr[idx]));
			}
		}

		// 修改记录状态：0启用，1禁用
		/*if (isDel != null && isDel != "")
		{
			if (isDel.equals("0"))
			{
				testingMapper.updateTestingEnableByTestingIds(idList);
			} else if (isDel.equals("1"))
			{
				testingMapper.updateTestingDisableByTestingIds(idList);
			}
		}*/
		//更新状态
			testingMapper.updateTestingStatusTestingIds(idList,isDel);
		
	}

	/**
	 * 主键获取测试信息
	 * @param testingId
	 * @return
	 */
	public Testing getTestingByPrimaryKey(Long testingId) {
		return testingMapper.selectByPrimaryKey(testingId);
	}

	/**
	 * 根据测试类型查询测试信息
	 * @param list TestingType主键集合 typeId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<Testing> getTestingListByTestingTypes(List<TestingType> list,Integer pageIndex,Integer pageSize) {
		List<Long> typeIds=new ArrayList<Long>();
		for(TestingType testingType: list){
			typeIds.add(testingType.getTypeId());
		}
		Page page=new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return testingMapper.getPageListByTypeIds(typeIds,page.getPageStartNum(),page.getPageSize());
	}

	/**
	 * 根据测试类型统计测试
	 * @param list
	 * @return
	 */
	public Integer countByTestingTypes(List<TestingType> list){
		List<Long> typeIds=new ArrayList<Long>();
		for(TestingType testingType: list){
			typeIds.add(testingType.getTypeId());
		}
		return testingMapper.countByTypeIds(typeIds);
	}
	
	/**
	 * 根据typeId 获取测试列表
	 * @param typeId
	 * @return
	 */
	public List<Testing> getTestingListByTypeId(Long typeId) {
		Testing testing=new Testing();
		testing.setTypeId(typeId);
		return testingMapper.selectSelective(testing);
	}

	
	/**
	 * 根据mid统计测试数量
	 * @param mid
	 * @return
	 */
	public Integer countTestingByMid(Long mid) {
		//统计积分型数量
		Integer count=0;
		count+=testingResultMapper.countTestingByMid(mid);
		//统计跳转型数量
		TestingResultForMember trfm=new TestingResultForMember();
		trfm.setMid(mid);
		count+=testingResultForMemberMapper.countSelective(trfm);
		return count;
	}
	
}
