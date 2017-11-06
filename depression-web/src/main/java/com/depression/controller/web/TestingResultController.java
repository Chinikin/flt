package com.depression.controller.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.TestingQuestions;
import com.depression.model.TestingResult;
import com.depression.model.TestingResultOld;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingResultService;

/**
 * 用户测试结果接口
 * 		1.对于某会员某试卷中的某题只保存一份测试结果，即（会员ID，试卷ID，试题ID）的组合只存在一个测试结果
 * @author caizj
 *
 */
@Controller
@RequestMapping("/testingResult")
public class TestingResultController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingResultService testingResultService;
	
	@Autowired
	private TestingQuestionsService testingQuestionsService;

	@RequestMapping(method = RequestMethod.POST, value = "/listByMidAndTestingId.json")
	@ResponseBody
	public Object listByMidAndTestingId(HttpServletRequest request, ModelMap modelMap, Long mid, Long testingId)
	{
		ResultEntity result = new ResultEntity();
		if(mid==null || testingId==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		/*-----------查询试卷的所有试题------------*/
		// 添加查询条件
		TestingQuestions queryTestingQuestions = new TestingQuestions();
		queryTestingQuestions.setTestingId(testingId);

		// 查询集合
		List<TestingQuestions> listTQ = testingQuestionsService.getTestingQuestionsListByQueryTestingQuestions(queryTestingQuestions);
		
		if(listTQ.size() == 0)
		{
			result.setCode(-3);
			result.setMsg("该testingID试卷下没有试题");
			return result;
		}
		
		/*--------通过试题查询会用的测试结果----------*/
		List<TestingResult> listTR = new ArrayList<TestingResult>();
		for(TestingQuestions tq : listTQ)
		{
			TestingResult tr = new TestingResult();
			tr.setMid(mid);
			tr.setTestingId(testingId);
			tr.setQuestionsId(tq.getQuestionsId());
			List<TestingResult> ltr = testingResultService.getTestingResultByQueryTestingResult(tr);
			if(ltr.size() != 0)
			{
				listTR.add(ltr.get(0));
			}
		}
		result.put("list", listTR);
		result.put("count", listTR.size());

		result.setCode(0);
		return result;
	}
	
	
	/**
	 * 保存用户测试结果（试题的选项）
	 *		1.在保存时，如已存在上次测试结果，则执行更新操作, 并把isDelete置为0
	 * @param request
	 * @param modelMap
	 * @param mid				会员ID		必填 
	 * @param testingId			试卷ID		必填
	 * @param questionsId		试题ID		必填
	 * @param optionsId			选择ID		必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, Long mid, Long testingId, Long questionsId, Long optionsId)
	{
		ResultEntity result = new ResultEntity();
		
		if(mid==null || testingId==null || questionsId==null || optionsId==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		TestingResult testingResult = new TestingResult();
		testingResult.setMid(mid);
		testingResult.setTestingId(testingId);
		testingResult.setQuestionsId(questionsId);
		testingResult.setOptionsId(optionsId);
		testingResult.setIsEnable((byte)0);
		List<TestingResult> listTR = testingResultService.getTestingResultByQueryTestingResult(testingResult);
		if (listTR.size() == 0){
			testingResultService.insertTestingResult(testingResult);
			result.setMsg("测试结果插入成功");
		} else{
			testingResult.setResultId(listTR.get(0).getResultId());
			testingResultService.updateTestingResult(testingResult);
			result.setMsg("测试结果已存在，更新成功");
		}

		result.setCode(0);
		return result;
	}
	
	/**
	 * 更新用户测试结果
	 * 		
	 * @param request
	 * @param modelMap
	 * @param resultId		测试结果ID		必填			
	 * @param optionsId		选项ID		必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, Long resultId, Long optionsId)
	{
		ResultEntity result = new ResultEntity();
		
		if(resultId==null || optionsId==null){
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		TestingResult testingResult = testingResultService.getTestingResultByResultId(resultId);
		if(testingResult == null){
			result.setCode(-3);
			result.setMsg("不存在该resultId对应的记录");
			return result;
		}else{
			testingResult.setOptionsId(optionsId);
			testingResultService.updateTestingResult(testingResult);
			result.setMsg("用户测试结果更新成功");
		}
		result.setCode(0);
		return result;
	}
	
	/**
	 * 更新用户测试结果的状态
	 * 
	 * @param request
	 * @param modelMap
	 * @param resultId		测试结果ID		必填
	 * @param isDelete		删除状态		必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, Long resultId, Byte isDelete)
	{
		ResultEntity result = new ResultEntity();
		
		if(resultId==null || isDelete==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		if(isDelete!=0 && isDelete!=1)
		{
			result.setCode(-3);
			result.setMsg("状态错误：不允许的状态");
			return result;			
		}
		
		TestingResult testingResult = testingResultService.getTestingResultByResultId(resultId);
		if(testingResult == null)
		{
			result.setCode(-3);
			result.setMsg("不存在该resultId对应的记录");
			return result;
		}else
		{
			testingResult.setIsDelete(isDelete);
			testingResultService.updateTestingResult(testingResult);
			result.setMsg("状态更新成功");
		}
		
		result.setCode(0);
		return result;
		
	}

}
