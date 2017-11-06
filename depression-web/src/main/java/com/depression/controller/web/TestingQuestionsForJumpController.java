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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.controller.web.common.TestingBase;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Testing;
import com.depression.model.TestingOld;
import com.depression.model.TestingQuestions;
import com.depression.model.TestingQuestionsOptions;
import com.depression.service.TestingQuestionsOptionsService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/testingQuestions/jump")
public class TestingQuestionsForJumpController extends TestingBase
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingQuestionsService testingQuestionsService;

	@Autowired
	private TestingQuestionsOptionsService testingQuestionsOptionsService;

	@Autowired
	private TestingService testingService;

	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap modelMap, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (pageIndex == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (pageSize == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}

		// 添加查询条件
		TestingQuestions queryTestingQuestions = new TestingQuestions();
		
		if (pageIndex != null)
		{
			queryTestingQuestions.setPageIndex(pageIndex);
		}
		if (pageSize != null)
		{
			queryTestingQuestions.setPageSize(pageSize);
		}
		
		// 查询集合
		Integer totalCount = testingQuestionsService.getPageCounts(queryTestingQuestions);
		List<TestingQuestions> list = testingQuestionsService.getPageList(queryTestingQuestions);
		result.put("list", list);
		result.put("count", totalCount);

		return result;
	}

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

		// 添加查询条件
		TestingQuestions queryTestingQuestions = new TestingQuestions();
		queryTestingQuestions.setTestingId(testingId);

		// 查询集合
		List<TestingQuestions> list = testingQuestionsService.getTestingQuestionsList(queryTestingQuestions);
		JSONArray jArr = new JSONArray();
		for (TestingQuestions testingQuestions : list)
		{
			JSONObject obj = new JSONObject();
			obj.put("questionsId", testingQuestions.getQuestionsId());
			obj.put("questionsTitle", testingQuestions.getQuestionsTitle());
			obj.put("seqNum", testingQuestions.getSubjectSeqNum());
			obj.put("isEnable", testingQuestions.getIsEnable());
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

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, Long testingId, String tesingTitle, Long typeId, String subtitle, String contentExplain, Integer seqNum,
			String questionsTitle, String jsonArrForOPts)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingId == null || testingId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}
		if (questionsTitle == null || questionsTitle.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试题标题不能为空");
			return result;
		}
		if (seqNum == null || seqNum.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试题序号不能为空");
			return result;
		}
		if (tesingTitle == null || tesingTitle.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷标题不能为空");
			return result;
		}
		if (typeId == null || typeId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷所属类别不能为空");
			return result;
		}
		if (subtitle == null || subtitle.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷副标题不能为空");
			return result;
		}
		if (contentExplain == null || contentExplain.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷描述不能为空");
			return result;
		}
		if (jsonArrForOPts == null || jsonArrForOPts.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷选项json数据不能为空");
			return result;
		}

		// 查询问卷是否存在
		Testing curTesting = testingService.getTestingById(testingId );
		if (curTesting == null)
		{
			result.setCode(-1);
			result.setMsg("问卷不存在");
			return result;
		}

		// 新增试卷
		Long curTestingId = saveTesting(testingId, tesingTitle, typeId, subtitle, contentExplain);

		// 新增题目
		TestingQuestions oldTestingQuestions = new TestingQuestions();
		oldTestingQuestions.setQuestionsTitle(questionsTitle);
		Long curQuestionId = 0l;
		TestingQuestions testingQuestions = new TestingQuestions();
		testingQuestions.setTestingId(curTestingId);
		testingQuestions.setQuestionsTitle(questionsTitle);
		testingQuestions.setSubjectSeqNum(seqNum);
		testingQuestionsService.insertTestingQuestions(testingQuestions);
		curQuestionId = testingQuestions.getQuestionsId();

		// 新增选项
		JSONArray jsonArr = new JSONArray();
		jsonArr = JSON.parseArray(jsonArrForOPts);
		for (int idx = 0; idx < jsonArr.size(); idx++)
		{
			JSONObject obj = (JSONObject) jsonArr.get(idx);
			TestingQuestionsOptions testingQuestionsOptions = new TestingQuestionsOptions();

			Integer optType = obj.getInteger("optType");
			if (optType != null && optType.intValue() == 0)
			{
				Integer jumpToQuestionNo = obj.getInteger("jumpToQuestionNo");
				testingQuestionsOptions.setJumpToQuestionNo(jumpToQuestionNo);
			}
			if (optType != null && optType.intValue() == 1)
			{
				String jumpResultTag = obj.getString("jumpResultTag");
				testingQuestionsOptions.setJumpResultTag(jumpResultTag);
			}

			testingQuestionsOptions.setOptType(optType);

			String title = obj.getString("title");
			testingQuestionsOptions.setTitle(title);

			testingQuestionsOptions.setQuestionsId(curQuestionId);
			if (title != null && !title.equals("") && optType != null)
			{
				testingQuestionsOptionsService.insertTestingQuestionsOptions(testingQuestionsOptions);
			}
			result.setMsg("题目新增成功");
		}

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, Long questionsId, Long testingId, String tesingTitle, Long typeId, String subtitle, String contentExplain,
			Integer seqNum, String questionsTitle, String jsonArrForOPts)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (questionsId == null || questionsId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试题id不能为空");
			return result;
		}
		if (testingId == null || testingId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}
		if (tesingTitle == null || tesingTitle.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷标题不能为空");
			return result;
		}
		if (typeId == null || typeId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷所属类别不能为空");
			return result;
		}
		if (subtitle == null || subtitle.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷副标题不能为空");
			return result;
		}
		if (contentExplain == null || contentExplain.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷描述不能为空");
			return result;
		}
		if (jsonArrForOPts == null || jsonArrForOPts.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷选项json数据不能为空");
			return result;
		}

		// 查询问卷是否存在
		Testing curTesting = testingService.getTestingByPrimaryKey(testingId);
		if (curTesting == null)
		{
			result.setCode(-1);
			result.setMsg("问卷不存在");
			return result;
		}

		// 修改问卷信息
		saveTesting(testingId, tesingTitle, typeId, subtitle, contentExplain);

		// 编辑题目
		Long curQuestionId = questionsId;
		TestingQuestions oldTestingQuestions = new TestingQuestions();
		oldTestingQuestions.setQuestionsId(questionsId);
		oldTestingQuestions.setQuestionsTitle(questionsTitle);
		TestingQuestions testingQuestions = new TestingQuestions();
		testingQuestions.setQuestionsId(questionsId);
		testingQuestions.setTestingId(testingId);
		testingQuestions.setQuestionsTitle(questionsTitle);
		testingQuestions.setSubjectSeqNum(seqNum);
		testingQuestionsService.updateTestingQuestions(testingQuestions);

		// 禁用旧的选项
		testingQuestionsOptionsService.updateTestingQuestionsOptionsDisableByQuestionsId(curQuestionId);

		// 编辑选项
		JSONArray jsonArr = new JSONArray();
		jsonArr = JSON.parseArray(jsonArrForOPts);
		for (int idx = 0; idx < jsonArr.size(); idx++)
		{
			JSONObject obj = (JSONObject) jsonArr.get(idx);
			TestingQuestionsOptions testingQuestionsOptions = new TestingQuestionsOptions();
			Long optionId = obj.getLong("optionId");
			testingQuestionsOptions.setOptionsId(optionId);
			String title = obj.getString("title");
			testingQuestionsOptions.setTitle(title);
			testingQuestionsOptions.setQuestionsId(curQuestionId);
			testingQuestionsOptions.setIsDelete("0");// 将禁用的选项启用

			Integer optType = obj.getInteger("optType");
			if (optType != null && optType.intValue() == 0)
			{
				Integer jumpToQuestionNo = obj.getInteger("jumpToQuestionNo");
				testingQuestionsOptions.setJumpToQuestionNo(jumpToQuestionNo);
			}
			if (optType != null && optType.intValue() == 1)
			{
				String jumpResultTag = obj.getString("jumpResultTag");
				testingQuestionsOptions.setJumpResultTag(jumpResultTag);
			}

			testingQuestionsOptions.setOptType(optType);
			if (title != null && !title.equals("") && optType != null)
			{
				if (optionId != null)
				{
					// 更新已有的选项
					testingQuestionsOptionsService.updateTestingQuestionsOptions(testingQuestionsOptions);
				} else
				{
					// 新增选项
					testingQuestionsOptionsService.insertTestingQuestionsOptions(testingQuestionsOptions);
				}
			}

			result.setMsg("题目更新成功");
		}

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, String questionsId, Byte isDel)
	{
		ResultEntity result = new ResultEntity();

		//检验参数
    	if(PropertyUtils.examineOneNull(
    			questionsId,
    			isDel
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 处理记录id
		questionsId = questionsId.replace("[", "").replace("]", "");
		String[] idArr = questionsId.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (int idx = 0; idx < idArr.length; idx++)
		{
			if (idArr[idx] != null && !idArr[idx].equals(""))
			{
				idList.add(Long.parseLong(idArr[idx]));
			}
		}
		try {
			testingQuestionsService.updateTestingQuestionsStatusByQuestionsIds(idList, isDel);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.setCode(0);
		result.setMsg("状态更新成功");

		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, ModelMap modelMap, Long questionsId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (questionsId == null || questionsId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试题id不能为空");
			return result;
		}

		// 查询记录详情
		TestingQuestions testingQuestions = testingQuestionsService.getTestingQuestionsByQuestionsId(questionsId);

		result.put("obj", testingQuestions);
		result.setCode(0);
		return result;
	}
}
