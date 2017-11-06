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

import com.depression.entity.ResultEntity;
import com.depression.model.TestingQuestionsOptions;
import com.depression.service.TestingQuestionsOptionsService;

@Controller
@RequestMapping("/testingQuestionsOptions")
public class TestingQuestionsOptionsController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingQuestionsOptionsService testingQuestionsOptionsService;

	@RequestMapping(method = RequestMethod.POST, value = "/listByQuestionsId.json")
	@ResponseBody
	public Object listByQuestionsId(HttpServletRequest request, ModelMap modelMap, Long questionsId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (questionsId == null || questionsId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试题id不能为空");
			return result;
		}

		// 查询集合
		List<TestingQuestionsOptions> list = testingQuestionsOptionsService.getValidTestingQuestionsOptionsListByQuestionsId(questionsId);
		result.put("list", list);
		result.put("count", list.size());

		result.setCode(0);
		return result;
	}

}
