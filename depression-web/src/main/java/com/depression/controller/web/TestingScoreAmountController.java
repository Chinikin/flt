package com.depression.controller.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.TestingScoreAmount;
import com.depression.model.TestingScoreAmountOld;
import com.depression.service.TestingScoreAmountService;

/**
 * 用户测试结果总分
 * 		1. 由会员ID、试卷ID可以确定唯一的测试结果总分
 * @author caizj
 *
 */
@Controller
@RequestMapping("/testingScoreAmount")
public class TestingScoreAmountController
{
	@Autowired
	private TestingScoreAmountService testingScoreAmountService;
	
	/**
	 * 根据会员ID查询试卷测试结果总分列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid		会员Id		必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/listByMid.json")
	@ResponseBody
	public Object listByMid(HttpServletRequest request, ModelMap modelMap, TestingScoreAmount testingScoreAmount)
	{
		ResultEntity result = new ResultEntity();
		if(testingScoreAmount == null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		List<TestingScoreAmount> listTSA = testingScoreAmountService.getTestingScoreAmountByMid(testingScoreAmount);
		
		result.put("list", listTSA);
		result.put("count", listTSA.size());
		result.setCode(0);
		return result;
	}
	
	/**
	 * 根据试卷ID查询试卷测试结果总分列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param testingId		试卷ID		必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/listByTestingId.json")
	@ResponseBody	
	public Object listByTestingId(HttpServletRequest request, ModelMap modelMap, Long testingId)
	{
		ResultEntity result = new ResultEntity();
		if(testingId == null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		List<TestingScoreAmount> listTSA = testingScoreAmountService.getTestingScoreAmountByTestingId(testingId);
		
		result.put("list", listTSA);
		result.put("count", listTSA.size());
		result.setCode(0);
		return result;
	}
	
	/**
	 * 根据试卷ID和会员ID查询试卷测试结果总分
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid			会员ID		必填
	 * @param testingId		试卷ID		必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/selectByMidAndTestingId.json")
	@ResponseBody	
	public Object selectByMidAndTestingId(HttpServletRequest request, ModelMap modelMap, Long mid, Long testingId)
	{
		ResultEntity result = new ResultEntity();
		if(testingId == null || mid == null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		TestingScoreAmount testingScoreAmount = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(
				mid, testingId);
		
		result.put("obj", testingScoreAmount);
		result.setCode(0);
		return result;
	}
	
	/**
	 * 更新试卷测试结果总分
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid			会员ID		必填
	 * @param testingId		试卷ID		必填
	 * @param score			得分			必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody	
	public Object save(HttpServletRequest request, ModelMap modelMap, Long mid, Long testingId, Integer score)
	{
		ResultEntity result = new ResultEntity();
		if(mid==null || testingId==null || score==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		Date testTime = new Date();
		TestingScoreAmount testingScoreAmount = new TestingScoreAmount();
		testingScoreAmount.setMid(mid);
		testingScoreAmount.setTestingId(testingId);
		testingScoreAmount.setScore(score);
		testingScoreAmount.setTestTime(testTime);
		testingScoreAmount.setIsEnable((byte)0);
		TestingScoreAmount oldTSA = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(
				mid, testingId);
		
		if(oldTSA==null)
		{
			testingScoreAmountService.insertTestingScoreAmount(testingScoreAmount);
			result.setMsg("试卷测试结果总分保存成功");
		}else
		{
			testingScoreAmount.setScoreId(oldTSA.getScoreId());
			testingScoreAmountService.updateTestingScoreAmount(testingScoreAmount);
			result.setMsg("结果已存在，试卷测试结果总分更新保存成功");
		}
		
		result.setCode(0);
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param scoreId		总分ID		必填	
	 * @param mid           会员ID		选填
	 * @param testingId     试卷ID		选填
	 * @param score         得分			选填
	 * @param testTime      测试时间		选填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody	
	public Object update(HttpServletRequest request, ModelMap modelMap, Long scoreId, Integer score)
	{
		ResultEntity result = new ResultEntity();
		
		Date testTime = new Date();
		TestingScoreAmount testingScoreAmount = new TestingScoreAmount();
		testingScoreAmount.setScoreId(scoreId);
		testingScoreAmount.setScore(score);
		testingScoreAmount.setTestTime(testTime);
		
		TestingScoreAmount oldTSA = testingScoreAmountService.getTestingScoreAmountByScoreId(scoreId);
		
		if(oldTSA == null)
		{
			result.setCode(-3);
			result.setMsg("不存在该scoreId对应的记录");
			return result;
		}else
		{
			testingScoreAmount.setMid(oldTSA.getMid());
			testingScoreAmount.setTestingId(oldTSA.getTestingId());
			testingScoreAmount.setIsEnable((byte)0);
			testingScoreAmountService.updateTestingScoreAmount(testingScoreAmount);
			result.setMsg("试卷测试结果总分更新保存成功");
		}
		
		result.setCode(0);
		return result;
	}
	
	/**
	 * 更新试卷测试结果总分状态变更
	 * 
	 * @param request
	 * @param modelMap
	 * @param scoreId		总分ID		必填
	 * @param isDelete		状态			必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, Long scoreId, Byte isDelete)
	{
		ResultEntity result = new ResultEntity();
		
		if(scoreId==null || isDelete==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		
		TestingScoreAmount oldTSA = testingScoreAmountService.getTestingScoreAmountByScoreId(scoreId);
		if(oldTSA == null)
		{
			result.setCode(-3);
			result.setMsg("不存在该resultId对应的记录");
			return result;
		}else
		{
			oldTSA.setIsEnable(isDelete);
			testingScoreAmountService.updateTestingScoreAmount(oldTSA);
			result.setMsg("状态更新成功");
		}
		
		result.setCode(0);
		return result;
	}
	
}
