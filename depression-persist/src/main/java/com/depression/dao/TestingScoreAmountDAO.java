package com.depression.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.TestingScoreAmountOld;

/**
 * 会员试卷测试总分DAO
 * @author caizj
 *
 */
public interface TestingScoreAmountDAO
{
	public void insertTestingScoreAmount(TestingScoreAmountOld testingScoreAmount);
	
	public void updateTestingScoreAmount(TestingScoreAmountOld testingScoreAmount);
	
	public void deleteTestingScoreAmount(String scoreId);
	
	public Long getCounts();
	
	public List<TestingScoreAmountOld> getTestingScoreAmountList();
	
	public TestingScoreAmountOld getTestingScoreAmountByScoreId(String scoreId);
	
	public List<TestingScoreAmountOld> getTestingScoreAmountByMid(TestingScoreAmountOld testingScoreAmount);
	
	public List<TestingScoreAmountOld> getTestingScoreAmountByTestingId(@Param("testingId")String testingId);
	
	public TestingScoreAmountOld getTestingScoreAmountByMidAndTestingId(
			@Param("mid")String mid, @Param("testingId")String testingId);
	
}
