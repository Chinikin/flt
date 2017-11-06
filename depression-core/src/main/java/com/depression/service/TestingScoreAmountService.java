package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingScoreAmountDAO;
import com.depression.dao.TestingScoreAmountMapper;
import com.depression.model.TestingScoreAmount;
import com.depression.model.TestingScoreAmountOld;

/**
 * 会员试卷测试总分Service
 * @author caizj
 *
 */
@Service
public class TestingScoreAmountService
{
	@Autowired
	private TestingScoreAmountDAO testingScoreAmountDAO;
	
	@Autowired
	private TestingScoreAmountMapper testingScoreAmountMapper;
	
	public void insertTestingScoreAmount(TestingScoreAmount testingScoreAmount)
	{
		this.testingScoreAmountMapper.insertSelective(testingScoreAmount);
	}
	
	public void updateTestingScoreAmount(TestingScoreAmount testingScoreAmount)
	{
		this.testingScoreAmountMapper.updateByPrimaryKeySelective(testingScoreAmount);
	}
	
	/*public void deleteTestingScoreAmount(String scoreId)
	{
		this.testingScoreAmountDAO.deleteTestingScoreAmount(scoreId);
	}
	
	public Long getCounts()
	{
		return this.testingScoreAmountDAO.getCounts();
	}
	
	public List<TestingScoreAmountOld> getTestingScoreAmountList()
	{
		return this.testingScoreAmountDAO.getTestingScoreAmountList();
	}
	*/
	public TestingScoreAmount getTestingScoreAmountByScoreId(Long scoreId)
	{
		return this.testingScoreAmountMapper.selectByPrimaryKey(scoreId);
	}
	
	public List<TestingScoreAmount> getTestingScoreAmountByMid(TestingScoreAmount testingScoreAmount)
	{
		return testingScoreAmountMapper.selectSelective(testingScoreAmount);
	}
	
	public List<TestingScoreAmount> getTestingScoreAmountByTestingId(Long testingId){
		TestingScoreAmount tsa=new TestingScoreAmount();
		tsa.setTestingId(testingId);
		return this.testingScoreAmountMapper.selectSelective(tsa);
	}
	
	public TestingScoreAmount getTestingScoreAmountByMidAndTestingId(Long mid, Long testingId)
	{	TestingScoreAmount tsa=new TestingScoreAmount();
		tsa.setMid(mid);
		tsa.setTestingId(testingId);
		List<TestingScoreAmount> list=testingScoreAmountMapper.selectSelective(tsa);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
}
