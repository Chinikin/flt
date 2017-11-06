package com.depression.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.TestingResultDAO;
import com.depression.dao.TestingResultMapper;
import com.depression.model.Testing;
import com.depression.model.TestingResult;
import com.depression.model.TestingResultForJump;
import com.depression.model.TestingResultForMember;
import com.depression.model.TestingScoreAmount;
import com.depression.model.TestingSection;
import com.depression.model.eap.dto.TestingResultCustom;

/**
 * 测试结果Service
 * @author caizj
 *
 */
@Service
public class TestingResultService
{
	@Autowired
	private TestingResultDAO testingResultDAO;
	
	@Autowired
	private TestingResultMapper testingResultMapper;
	
	@Autowired
	private TestingScoreAmountService testingScoreAmountService;
	
	@Autowired
	private TestingService testingService;
	
	@Autowired 
	private TestingQuestionsService testingQuestionsService;
	
	@Autowired
	private TestingSectionService testingSectionService;
	
	@Autowired
	private TestingResultForJumpService testingResultForJumpService;
	
	@Autowired
	private TestingResultForMemberService testingResultForMemberService;
	
	public void insertTestingResult(TestingResult testingResult)
	{
		this.testingResultMapper.insertSelective(testingResult);
	}
	
	public void updateTestingResult(TestingResult testingResult){
		testingResultMapper.updateByPrimaryKeySelective(testingResult);
	}
	
	public void updateTestingResultDisableByIds(List<Long> resIds)
	{
		this.testingResultMapper.updateTestingResultDisableByIds(resIds);
	}
	
	/*public void deleteTestingResult(String resultId)
	{
		this.testingResultDAO.deleteTestingResult(resultId);
	}
	
	public Long getCounts()
	{
		return this.testingResultDAO.getCounts();
	}
	
	public List<TestingResultOld> getTestingResultList()
	{
		return this.testingResultDAO.getTestingResultList();
	}*/
	
	public TestingResult getTestingResultByResultId(Long resultId)
	{
		return testingResultMapper.selectByPrimaryKey(resultId);
	}
	
	/*public List<TestingResultOld> getTestingResultByResultIds(List<String> idsList)
	{
		return this.testingResultDAO.getTestingResultByResultIds(idsList);
	}
	
	public List<TestingResultOld> getTestingResultByQuery(Map<String, Object> paramMap)
	{
		return this.testingResultDAO.getTestingResultByQuery(paramMap);
	}*/
	
	public List<TestingResult> getTestingResultByQueryTestingResult(TestingResult testingResult)
	{
		return testingResultMapper.selectSelective(testingResult);
	}
	
	public List<TestingResult> getValidTestingResultByQueryTestingResult(TestingResult testingResult)
	{	
		testingResult.setIsEnable((byte)0);
		return this.testingResultMapper.selectSelective(testingResult);
	}
	
	/*public List<TestingResultOld> getPageList(TestingResultOld testingResult)
	{
		return this.testingResultDAO.getPageList(testingResult);
	}
	
	public Long getPageCounts(TestingResultOld testingResult)
	{
		return this.testingResultDAO.getPageCounts(testingResult);
	}*/
	
	
	
	
	
	/**
	 * 根据用户id 和测试id,跳转类型 返回唯一测试结果
	 * @param testingId 测试id
	 * @param mid 用户id
	 * @param calc 跳转类型
	 * @return
	 */
	public TestingResultCustom getTestingResultByMidAndTestingId(Long testingId,Long mid,Integer calc){
		TestingResultCustom trc=new TestingResultCustom();
		// 返回问卷标题
		Testing testing = testingService.getTestingById(testingId);
		if (testing != null ){
			trc.setTitle(testing.getTitle());
		}
		trc.setCalc(calc);
		//计分型结果
		if(calc == 0){
			// 返回得分
			TestingScoreAmount testingScoreAmount = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(mid, testingId);
			BeanUtils.copyProperties(testingScoreAmount, trc);
			// 根据得分查询问卷测试结果描述
			if (testingScoreAmount != null){
				List<TestingSection> testingSectionList = testingSectionService.getTestingSectionByTestingIdAndScore(testingId, testingScoreAmount.getScore());
				if (testingSectionList != null && testingSectionList.size() > 0){
					trc.setLevel(testingSectionList.get(0).getLevel());
					trc.setDetail(testingSectionList.get(0).getDetail());
				}

			}
			
		}
		//跳转型结果
		if(calc == 1){
			//联合查询跳转类型结果
			Map<String,Object> query=new HashMap<String,Object>();
			query.put("mid", mid);
			query.put("testingId", testingId);
			query.put("isEnable", (byte)0);
			List<TestingResultCustom> reTrcs = testingResultForJumpService.getTestingResultForJumpByQuery(query);
			if (reTrcs != null && reTrcs.size() > 0){
					BeanUtils.copyProperties(reTrcs.get(0), trc);
			}			
		}
		return trc;
	}
	
}
