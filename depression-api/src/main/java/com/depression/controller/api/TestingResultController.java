package com.depression.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ResultEntity;
import com.depression.model.Testing;
import com.depression.model.TestingOld;
import com.depression.model.TestingQuestionsOptions;
import com.depression.model.TestingResult;
import com.depression.model.TestingScoreAmount;
import com.depression.model.TestingScoreAmountOld;
import com.depression.model.TestingSection;
import com.depression.model.api.dto.ApiTestingScoreAmountDTO;
import com.depression.service.TestingQuestionsOptionsService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingResultService;
import com.depression.service.TestingScoreAmountService;
import com.depression.service.TestingSectionService;
import com.depression.service.TestingService;

/**
 * 用户测试结果
 * 
 * @author fanxinhui
 * 
 */
@Controller
@RequestMapping(value = { "/testingResult", "/superClass" })
public class TestingResultController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingResultService testingResultService;

	@Autowired
	private TestingQuestionsService testingQuestionsService;

	@Autowired
	private TestingScoreAmountService testingScoreAmountService;

	@Autowired
	private TestingQuestionsOptionsService testingQuestionsOptionsService;

	@Autowired
	private TestingService testingService;

	@Autowired
	private TestingSectionService testingSectionService;

	/**
	 * 根据试卷ID和会员ID查询试卷测试结果总分
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid
	 *            会员ID
	 * @param testingId
	 *            试卷ID
	 * @return
	 */
	@RequestMapping(value = "/getTestingResult.json")
	@ResponseBody
	public Object getTestingResult(HttpServletRequest request, ModelMap modelMap, Long testingId, @RequestParam(value = "mid", required = false) Long mid,
			@RequestParam(value = "tempMid", required = false) Long tempMid)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingId == null)
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}
		if ((mid == null && tempMid == null) || (mid != null && tempMid != null))
		{
			result.setCode(-1);
			result.setMsg("会员id和游客id二选一必填");
			return result;
		}
		if (mid == null)
		{
			mid = tempMid;
		}

		// 返回得分
		TestingScoreAmount testingScoreAmount = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(mid, testingId);
		ApiTestingScoreAmountDTO testingScoreAmountDTO=new ApiTestingScoreAmountDTO();
		BeanUtils.copyProperties(testingScoreAmount, testingScoreAmountDTO);
		// 返回问卷标题
		Testing testing = testingService.getTestingById(testingId);
		if (testing != null && testingScoreAmount != null)
		{
			testingScoreAmountDTO.setTitle(testing.getTitle());
			testingScoreAmountDTO.setCalcMethod(testing.getCalcMethod());

			// 转换实际文件路径
			if (testing.getThumbnail() != null && !testing.getThumbnail().equals(""))
			{
				testingScoreAmountDTO.setFilePath(testing.getThumbnail());
			}
			if (testing.getThumbnailMobile() != null && !testing.getThumbnailMobile().equals(""))
			{
				testingScoreAmountDTO.setFilePathMobile(testing.getThumbnailMobile());
			}
			if (testing.getThumbnailSlide() != null && !testing.getThumbnailSlide().equals(""))
			{
				testingScoreAmountDTO.setFilePathSlide(testing.getThumbnailSlide());
			}
		}

		// 查询试题数量
		Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(testing.getTestingId());
		testingScoreAmountDTO.setQuestionAmount(questionAmount.longValue());

		// 根据得分查询问卷测试结果描述
		if (testingScoreAmount != null)
		{
			List<TestingSection> testingSectionList = testingSectionService.getTestingSectionByTestingIdAndScore(testingId, testingScoreAmount.getScore());
			if (testingSectionList != null && testingSectionList.size() > 0)
			{
				testingScoreAmountDTO.setLevel(testingSectionList.get(0).getLevel());
				testingScoreAmountDTO.setDetail(testingSectionList.get(0).getDetail());
			}

		}

		List<TestingSection> tsList = testingSectionService.getTestingSectionByTestingId(testingId);

		if (testingScoreAmount != null)
		{
			result.put("obj", testingScoreAmountDTO);
		} else
		{
			result.put("obj", "");
		}

		result.put("standard", tsList);
		result.setCode(0);
		return result;
	}

	/**
	 * 保存整份试卷的测试结果
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid
	 * @param testingId
	 * @param questionsId
	 * @param optionsId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveEntireTestingResult.json")
	@ResponseBody
	public Object saveEntireTestingResult(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "mid", required = false) Long mid,
			@RequestParam(value = "tempMid", required = false) Long tempMid, Long testingId, String selJson)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingId == null)
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}
		if (selJson == null)
		{
			result.setCode(-1);
			result.setMsg("测试结果不能为空");
			return result;
		}
		if ((mid == null && tempMid == null) || (mid != null && tempMid != null))
		{
			result.setCode(-1);
			result.setMsg("会员id和游客id二选一必填");
			return result;
		}
		if (mid == null)
		{
			mid = tempMid;
		}

		try
		{
			// 禁用原来的选项
			TestingResult queryTestingResult = new TestingResult();
			queryTestingResult.setMid(mid);
			queryTestingResult.setTestingId(testingId);
			queryTestingResult.setIsEnable((byte)0);
			List<TestingResult> curTesReList = testingResultService.getTestingResultByQueryTestingResult(queryTestingResult);
			List<Long> resIds = new ArrayList<Long>();
			for (TestingResult testingResult : curTesReList)
			{
				resIds.add(testingResult.getResultId());
			}
			if (resIds.size() > 0)
			{
				testingResultService.updateTestingResultDisableByIds(resIds);
			}

			// 问卷测试选项结果入库
			JSONArray jsonArray = JSONArray.parseArray(selJson);
			for (int i = 0; i < jsonArray.size(); i++)
			{
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				Long questionId = Long.parseLong(jsonObj.get("questionId").toString());
				Long optionId = Long.parseLong(jsonObj.get("optionId").toString());

				TestingResult testingResult = new TestingResult();
				testingResult.setMid(mid);
				testingResult.setTestingId(testingId);
				testingResult.setQuestionsId(questionId);
				testingResult.setIsEnable((byte)0);
				testingResult.setOptionsId(optionId);
				testingResultService.insertTestingResult(testingResult);
				result.setMsg("试题测试结果插入成功");
			}

			// 计算总得分
			TestingResult testingResult = new TestingResult();
			testingResult.setMid(mid);
			testingResult.setTestingId(testingId);
			testingResult.setIsEnable((byte)0);
			List<TestingResult> listTR = testingResultService.getValidTestingResultByQueryTestingResult(testingResult);
			Integer allScore = 0;
			for (int idx = 0; idx < listTR.size(); idx++)
			{
				TestingResult tResult = listTR.get(idx);
				Long optionId = tResult.getOptionsId();
				TestingQuestionsOptions testingQuestionsOptions = testingQuestionsOptionsService.getTestingQuestionsOptionsByQuestionsOptionsId(optionId);
				Integer score = testingQuestionsOptions.getScore();
				if (score == null)
				{
					score = 0;
				}
				allScore += score;
			}

			// 问卷总分入库
			Date testTime = new Date();
			TestingScoreAmount testingScoreAmount = new TestingScoreAmount();
			testingScoreAmount.setMid(mid);
			testingScoreAmount.setTestingId(testingId);
			testingScoreAmount.setScore(allScore);
			testingScoreAmount.setTestTime(testTime);
			testingScoreAmount.setIsEnable((byte)0);
			TestingScoreAmount oldTSA = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(mid,testingId);
			if (oldTSA == null)
			{
				testingScoreAmountService.insertTestingScoreAmount(testingScoreAmount);
				result.setMsg("问卷测试结果总分保存成功");

				// 保存测试人数统计信息
				Testing testing = testingService.getTestingById(testingId);

				if (testing.getTestingPeopleNum() == null)
				{
					testing.setTestingPeopleNum(1l);
				} else
				{
					testing.setTestingPeopleNum(testing.getTestingPeopleNum() + 1);
				}
				testingService.updateTesting(testing);
			} else
			{
				testingScoreAmount.setScoreId(oldTSA.getScoreId());
				testingScoreAmountService.updateTestingScoreAmount(testingScoreAmount);
				result.setMsg("结果已存在，问卷测试结果总分更新保存成功");
			}
		} catch (Exception e)
		{
			log.error("Save testing result error : " + e);
		}

		result.setCode(0);
		return result;
	}
}
