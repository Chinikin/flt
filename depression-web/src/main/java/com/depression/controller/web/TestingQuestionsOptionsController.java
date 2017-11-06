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

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.TestingQuestionsOptions;
import com.depression.model.TestingQuestionsOptionsOld;
import com.depression.service.TestingQuestionsOptionsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/testingQuestionsOptions")
public class TestingQuestionsOptionsController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingQuestionsOptionsService testingQuestionsOptionsService;

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
		TestingQuestionsOptions queryTestingQuestionsOptions = new TestingQuestionsOptions();
//		queryTestingQuestionsOptions.setTitle("选项");
		if (pageIndex != null)
		{
			queryTestingQuestionsOptions.setPageIndex(pageIndex);
		}
		if (pageSize != null)
		{
			queryTestingQuestionsOptions.setPageSize(pageSize);
		}

		// 查询集合
		Integer totalCount = testingQuestionsOptionsService.getPageCounts(queryTestingQuestionsOptions);
		if (totalCount != 0)
		{
			List<TestingQuestionsOptions> list = testingQuestionsOptionsService.getPageList(queryTestingQuestionsOptions);
			result.put("list", list);
			result.put("count", totalCount);
		} else
		{
			result.put("list", null);
			result.put("count", totalCount);
		}

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/listByQuestionsId.json")
	@ResponseBody
	public Object listByQuestionsId(HttpServletRequest request, ModelMap modelMap, Long questionsId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (questionsId == null || questionsId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("问题id不能为空");
			return result;
		}

		// 添加查询条件
		TestingQuestionsOptions queryTestingQuestionsOptions = new TestingQuestionsOptions();
		queryTestingQuestionsOptions.setQuestionsId(questionsId);

		// 查询集合
		List<TestingQuestionsOptions> list = testingQuestionsOptionsService.getTestingQuestionsOptionsListByQueryTestingQuestionsOptions(queryTestingQuestionsOptions);
		result.put("list", list);
		result.put("count", list.size());

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, TestingQuestionsOptions testingQuestionsOptions)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingQuestionsOptions == null)
		{
			result.setCode(-1);
			result.setMsg("参数错误");
			return result;
		}
		if (testingQuestionsOptions.getQuestionsId() == null || testingQuestionsOptions.getQuestionsId().equals(""))
		{
			result.setCode(-1);
			result.setMsg("试题id不能为空");
			return result;
		}
		if (testingQuestionsOptions.getTitle() == null || testingQuestionsOptions.getTitle().equals(""))
		{
			result.setCode(-1);
			result.setMsg("试题标题不能为空");
			return result;
		}

		TestingQuestionsOptions questionsOptions = new TestingQuestionsOptions();
		questionsOptions.setQuestionsId(testingQuestionsOptions.getQuestionsId());
		questionsOptions.setTitle(testingQuestionsOptions.getTitle());
		questionsOptions.setSequence(testingQuestionsOptions.getSequence());
		questionsOptions.setScore(testingQuestionsOptions.getScore());
		questionsOptions.setOptType(testingQuestionsOptions.getOptType());
		questionsOptions.setJumpToQuestionNo(testingQuestionsOptions.getJumpToQuestionNo());
		questionsOptions.setJumpResultTag(testingQuestionsOptions.getJumpResultTag());
		testingQuestionsOptionsService.insertTestingQuestionsOptions(questionsOptions);
		result.setMsg("新增成功");

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, TestingQuestionsOptions testingQuestionsOptions)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingQuestionsOptions == null)
		{
			result.setCode(-1);
			result.setMsg("参数错误");
			return result;
		}
		if (testingQuestionsOptions.getOptionsId() == null || testingQuestionsOptions.getOptionsId().equals(""))
		{
			result.setCode(-1);
			result.setMsg("选项id不能为空");
			return result;
		}

		TestingQuestionsOptions questionsOptions = new TestingQuestionsOptions();
		questionsOptions.setOptionsId(testingQuestionsOptions.getOptionsId());
		questionsOptions.setQuestionsId(testingQuestionsOptions.getQuestionsId());
		questionsOptions.setTitle(testingQuestionsOptions.getTitle());
		questionsOptions.setSequence(testingQuestionsOptions.getSequence());
		questionsOptions.setScore(testingQuestionsOptions.getScore());
		questionsOptions.setOptType(testingQuestionsOptions.getOptType());
		questionsOptions.setJumpToQuestionNo(testingQuestionsOptions.getJumpToQuestionNo());
		questionsOptions.setJumpResultTag(testingQuestionsOptions.getJumpResultTag());
		testingQuestionsOptionsService.updateTestingQuestionsOptions(questionsOptions);
		result.setMsg("更新成功");

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, String optionsId, Byte isDel)
	{
		ResultEntity result = new ResultEntity();

		//检验参数
    	if(PropertyUtils.examineOneNull(
    			optionsId,
    			isDel
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 处理记录id
		optionsId = optionsId.replace("[", "").replace("]", "");
		String[] idArr = optionsId.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (int idx = 0; idx < idArr.length; idx++)
		{
			if (idArr[idx] != null && !idArr[idx].equals(""))
			{
				idList.add(Long.parseLong(idArr[idx]));
			}
		}

		testingQuestionsOptionsService.updateTestingQuestionsOptionsByOptionsId(idList,isDel);
		
		result.setCode(0);
		result.setMsg("状态更新成功");

		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, ModelMap modelMap, Long optionsId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (optionsId == null || optionsId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("选项id不能为空");
			return result;
		}
		
		// 查询记录详情
		TestingQuestionsOptions testingQuestionsOptions = testingQuestionsOptionsService.getTestingQuestionsOptionsByQuestionsOptionsId(optionsId);

		result.put("obj", testingQuestionsOptions);
		result.setCode(0);
		return result;
	}
}
