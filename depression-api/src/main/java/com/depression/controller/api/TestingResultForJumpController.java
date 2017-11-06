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
import com.depression.model.TestingResultForJump;
import com.depression.model.TestingResultForJumpOld;
import com.depression.model.TestingResultForMember;
import com.depression.model.api.dto.ApiTestingResultForJumpDTO;
import com.depression.service.TestingQuestionsOptionsService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingResultForJumpService;
import com.depression.service.TestingResultForMemberService;
import com.depression.service.TestingResultService;
import com.depression.service.TestingSectionService;
import com.depression.service.TestingService;

/**
 * 用户测试结果（跳转问卷类型）
 * 
 * @author fanxinhui
 * 
 */
@Controller
@RequestMapping(value = { "/testingResult/jump", "/superClass/jump" })
public class TestingResultForJumpController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingResultService testingResultService;

	@Autowired
	private TestingQuestionsService testingQuestionsService;

	@Autowired
	private TestingResultForJumpService testingResultForJumpService;

	@Autowired
	private TestingResultForMemberService testingResultForMemberService;

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

		// 返回测试结果
		TestingResultForJump testingResultForJump = new TestingResultForJump();
		testingResultForJump.setTestingId(testingId);
		testingResultForJump.setIsEnable((byte)0);
		List<TestingResultForJump> resList = testingResultForJumpService.getTestingResultForJumpByQueryTestingResult(testingResultForJump);
		if (resList != null && resList.size() > 0)
		{
			for (int idx = 0; idx < resList.size(); idx++)
			{
				TestingResultForJump curTestingResultForJump = resList.get(idx);
				
				ApiTestingResultForJumpDTO tefjDTO=new ApiTestingResultForJumpDTO();
				BeanUtils.copyProperties(curTestingResultForJump, tefjDTO);
				
				TestingResultForMember resultForMember = new TestingResultForMember();
				resultForMember.setMid(mid);
				resultForMember.setResId(curTestingResultForJump.getResId());
				resultForMember.setIsEnable((byte)0);
				List<TestingResultForMember> resForMemList = testingResultForMemberService.getTestingResultForMemberByQueryTestingResult(resultForMember);

				// 当在会员测试管理表中查询到时，返回结果
				if (resForMemList != null && resForMemList.size() > 0)
				{

					// 返回问卷标题
					Testing testing = testingService.getTestingById(testingId);
					if (testing != null)
					{
						tefjDTO.setTestingTitle(testing.getTitle());
						tefjDTO.setCalcMethod(testing.getCalcMethod());
					}

					// 转换实际文件路径
					if (curTestingResultForJump.getThumbnail() != null && !curTestingResultForJump.getThumbnail().equals(""))
					{
						tefjDTO.setFilePath(curTestingResultForJump.getThumbnail());
					}
					if (testing.getThumbnailMobile() != null && !testing.getThumbnailMobile().equals(""))
					{
						tefjDTO.setFilePathMobile(testing.getThumbnailMobile());
					}
					if (testing.getThumbnailSlide() != null && !testing.getThumbnailSlide().equals(""))
					{
						tefjDTO.setFilePathSlide(testing.getThumbnailSlide());
					}
					
					// 查询试题数量
					Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(testingId);
					tefjDTO.setQuestionAmount(questionAmount.longValue());

					result.put("obj", tefjDTO);
					result.setCode(0);
					return result;
				}
			}

			result.setCode(-1);
			result.setMsg("未找到测试结果");
			return result;
		} else
		{
			result.setCode(-1);
			result.setMsg("未找到测试结果");
			return result;
		}
	}

	/**
	 * 保存整份试卷的测试结果
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid
	 * @param testingId
	 * @param selJson
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

		// 查询用户选择的选项
		String resTag = "";
		TestingResult testingResult = new TestingResult();
		testingResult.setMid(mid);
		testingResult.setTestingId(testingId);
		testingResult.setIsEnable((byte)0);
		List<TestingResult> listTR = testingResultService.getValidTestingResultByQueryTestingResult(testingResult);
		for (int idx = 0; idx < listTR.size(); idx++)
		{
			TestingResult tResult = listTR.get(idx);
			Long optionId = tResult.getOptionsId();
			TestingQuestionsOptions testingQuestionsOptions = testingQuestionsOptionsService.getTestingQuestionsOptionsByQuestionsOptionsId(optionId);
			Integer optType = testingQuestionsOptions.getOptType();
			if (optType != null && optType.intValue() == 1)
			{
				resTag = testingQuestionsOptions.getJumpResultTag();
				break;
			}
		}

		// 查找测试结论，并和用户id进行关联
		TestingResultForJump testingResultForJump = new TestingResultForJump();
		testingResultForJump.setTestingId(testingId);
		testingResultForJump.setResultTag(resTag);
		List<TestingResultForJump> resList = testingResultForJumpService.getTestingResultForJumpByQueryTestingResult(testingResultForJump);
		if (resList != null && resList.size() > 0)
		{
			// 查询问卷对应的结论列表
			TestingResultForJump queryTestingResultForJump = new TestingResultForJump();
			queryTestingResultForJump.setTestingId(testingId);
			List<TestingResultForJump> resList4Update = testingResultForJumpService.getTestingResultForJumpByQueryTestingResult(queryTestingResultForJump);
			if (resList4Update != null && resList4Update.size() > 0)
			{
				for (TestingResultForJump resJump : resList4Update)
				{
					// 禁用原来的记录
					TestingResultForMember queryResultForMember = new TestingResultForMember();
					queryResultForMember.setMid(mid);
					queryResultForMember.setResId(resJump.getResId());
					List<TestingResultForMember> trList = testingResultForMemberService.getTestingResultForMemberByQueryTestingResult(queryResultForMember);
					for (TestingResultForMember trm : trList)
					{
						trm.setIsEnable((byte)1);
						testingResultForMemberService.updateTestingResultForMember(trm);
					}

				}
			}

			// 插入最新结果
			TestingResultForJump resultForJump = resList.get(0);
			TestingResultForMember resultForMember = new TestingResultForMember();
			resultForMember.setMid(mid);
			resultForMember.setResId(resultForJump.getResId());
			resultForMember.setTestTime(new Date());
			testingResultForMemberService.insertTestingResultForMember(resultForMember);

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
			result.setCode(-1);
			result.setMsg("未找到对应的测试结论");
			return result;
		}

		result.setCode(0);
		return result;
	}
}
