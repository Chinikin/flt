package com.depression.controller.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.TestingResultForJump;
import com.depression.model.TestingResultForJumpOld;
import com.depression.model.web.dto.WebTestingResultForJumpDTO;
import com.depression.service.TestingResultForJumpService;

/**
 * 跳转方式问卷测试结论
 * 
 * @author fanxinhui
 * @date 2016/6/27
 */
@Controller
@RequestMapping("/TestingResultForJump")
public class TestingResultForJumpController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingResultForJumpService testingResultForJumpService;

	@RequestMapping(method = RequestMethod.POST, value = "/listByTestingId.json")
	@ResponseBody
	public Object listByTestingId(HttpServletRequest request, ModelMap modelMap, Long testingId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		if (testingId == null)
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}

		TestingResultForJump queryTestingResultForJump = new TestingResultForJump();
		queryTestingResultForJump.setTestingId(testingId);
		List<TestingResultForJump> listTS = testingResultForJumpService.getTestingResultForJumpByQueryTestingResult(queryTestingResultForJump);
		
		List<WebTestingResultForJumpDTO> listDTO=new ArrayList<WebTestingResultForJumpDTO>();
		for (TestingResultForJump testingResultForJump : listTS)
		{
			WebTestingResultForJumpDTO testingResultForJumpDTO=new WebTestingResultForJumpDTO();
			BeanUtils.copyProperties(testingResultForJump, testingResultForJumpDTO);
			// 转换实际文件路径
			testingResultForJumpDTO.setFilePath(testingResultForJump.getThumbnail());
			
			listDTO.add(testingResultForJumpDTO);
		}

		result.put("list", listDTO);
		result.put("count", listTS.size());
		result.setMsg("操作成功");
		return result;

	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, TestingResultForJump testingResultForJump)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		if (testingResultForJump == null)
		{
			result.setCode(-1);
			result.setMsg("参数错误");
			return result;
		}
		if (testingResultForJump.getTestingId() == null)
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}
		if (testingResultForJump.getResultTag() == null)
		{
			result.setCode(-1);
			result.setMsg("结果标签不能为空");
			return result;
		}
		if (testingResultForJump.getResultTag() == null)
		{
			result.setCode(-1);
			result.setMsg("结果标签不能为空");
			return result;
		}
		if (testingResultForJump.getTitle() == null)
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}
		if (testingResultForJump.getDetail() == null)
		{
			result.setCode(-1);
			result.setMsg("详情不能为空");
			return result;
		}

		testingResultForJump.setIsEnable((byte)0);
		testingResultForJumpService.insertTestingResultForJump(testingResultForJump);
		result.setMsg("操作成功");
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, TestingResultForJump testingResultForJump)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		if (testingResultForJump == null)
		{
			result.setCode(-1);
			result.setMsg("参数错误");
			return result;
		}
		if (testingResultForJump.getResId() == null)
		{
			result.setCode(-1);
			result.setMsg("结论id不能为空");
			return result;
		}
		if (testingResultForJump.getTestingId() == null)
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}
		if (testingResultForJump.getResultTag() == null)
		{
			result.setCode(-1);
			result.setMsg("结果标签不能为空");
			return result;
		}
		if (testingResultForJump.getResultTag() == null)
		{
			result.setCode(-1);
			result.setMsg("结果标签不能为空");
			return result;
		}
		if (testingResultForJump.getTitle() == null)
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}
		if (testingResultForJump.getDetail() == null)
		{
			result.setCode(-1);
			result.setMsg("详情不能为空");
			return result;
		}
		testingResultForJump.setIsEnable((byte)0);
		testingResultForJumpService.updateTestingResultForJump(testingResultForJump);
		result.setMsg("操作成功");
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, Long resId, Byte isDel)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		// 参数检查
		if (resId == null)
		{
			result.setCode(-1);
			result.setMsg("结论id不能为空");
			return result;
		}
		if (isDel == null)
		{
			result.setCode(-1);
			result.setMsg("状态值不能为空");
			return result;
		}
		if (isDel != 0 && isDel != 1)
		{
			result.setCode(-1);
			result.setMsg("状态错误：不允许的状态");
			return result;
		}

		TestingResultForJump testingResultForJump = testingResultForJumpService.getTestingResultForJumpByResultId(resId);
		if (testingResultForJump == null)
		{
			result.setCode(-1);
			result.setMsg("记录不存在");
			return result;
		} else
		{
			testingResultForJump.setIsEnable(isDel);
			testingResultForJumpService.updateTestingResultForJump(testingResultForJump);
			result.setMsg("状态更新成功");
		}

		return result;

	}

}
