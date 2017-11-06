package com.depression.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ResultEntity;
import com.depression.model.Testing;
import com.depression.model.TestingQuestions;
import com.depression.model.TestingQuestionsOptions;
import com.depression.service.TestingQuestionsOptionsService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingService;

@Controller
@RequestMapping(value={"/testingQuestions", "/superClass" })
public class TestingQuestionsController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingQuestionsService testingQuestionsService;
	
	@Autowired
	private TestingQuestionsOptionsService testingQuestionsOptionsService;
	
	@Autowired
	private TestingService testingService;

	@RequestMapping(method = RequestMethod.POST, value = "/listByTestingId.json")
	@ResponseBody
	public Object listByTestingId(HttpServletRequest request, ModelMap modelMap, Long testingId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingId == null || testingId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷id不能为空");
			return result;
		}

		// 查询集合
		List<TestingQuestions> list = testingQuestionsService.getValidTestingQuestionsListByTestingId(testingId);
		result.put("list", list);
		result.put("count", list.size());

		result.setCode(0);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/listAllQuestionByTestingId.json")
	@ResponseBody
	public Object listAllQuestionByTestingId(HttpServletRequest request, ModelMap modelMap, Long testingId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingId == null || testingId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷id不能为空");
			return result;
		}

		// 查询集合
		List<TestingQuestions> list = testingQuestionsService.getValidTestingQuestionsListByTestingId(testingId);
		Testing testing = testingService.getTestingById(testingId);
		if (testing != null)
		{
			result.put("title", testing.getTitle());
			result.put("calcMethod", testing.getCalcMethod());
		}
		JSONArray jArr = new JSONArray();
		for (TestingQuestions testingQuestions : list)
		{
			JSONObject obj = new JSONObject();
			obj.put("questionsId", testingQuestions.getQuestionsId());
			obj.put("questionsTitle", testingQuestions.getQuestionsTitle());
			obj.put("seqNum", testingQuestions.getSubjectSeqNum());
			obj.put("isDelete", testingQuestions.getIsEnable());
			Long questionsId = testingQuestions.getQuestionsId();
			List<TestingQuestionsOptions> optlist = testingQuestionsOptionsService.getValidTestingQuestionsOptionsListByQuestionsId(questionsId);
			obj.put("optList", optlist);
			jArr.add(obj);
		}
		result.put("list", jArr);
		result.put("count", list.size());

		result.setCode(0);
		return result;
	}

}
