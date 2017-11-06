package com.depression.controller.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.TestingType;
import com.depression.service.ImageStorageService;
import com.depression.service.TestingTypeService;

@Controller
@RequestMapping("/testingType")
public class TestingTypeController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingTypeService testingTypeService;

	@Autowired
	private ImageStorageService imageStorageService;

	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap modelMap, String typeName, Integer pageIndex, Integer pageSize)
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
		TestingType queryTestingType = new TestingType();
		if (typeName != null)
		{
			queryTestingType.setTestingName(typeName);
		}
		if (pageIndex != null)
		{
			queryTestingType.setPageIndex(pageIndex);
		}
		if (pageSize != null)
		{
			queryTestingType.setPageSize(pageSize);
		}

		// 查询集合
		Integer totalCount = testingTypeService.getPageCounts(queryTestingType);
		if (totalCount != 0)
		{
			List<TestingType> list = testingTypeService.getPageList(queryTestingType);
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

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, String typeName, @RequestParam(value = "tsType", required = false)Integer tsType)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (typeName == null || typeName.equals(""))
		{
			result.setCode(-1);
			result.setMsg("试卷类别名称不能为空");
			return result;
		}
		if (tsType == null)
		{
			result.setCode(-1);
			result.setMsg("问卷类型不能为空");
			return result;
		}
		if (tsType != 0 && tsType != 1 && tsType != 2)
		{
			result.setCode(-1);
			result.setMsg("问卷类型错误：不允许的类型");
			return result;
		}

		// 保存记录
		TestingType oldTestingType = new TestingType();
		oldTestingType.setTestingName(typeName);
		int isExists = testingTypeService.checkTestingTypeExits(oldTestingType);
		if (isExists <= 0)
		{
			TestingType testingType = new TestingType();
			testingType.setTsType(tsType);
			testingType.setTestingName(typeName);
			testingType.setCreateTime(new Date());
			testingTypeService.insertTestingType(testingType);
			result.setCode(0);
			result.setMsg("新增成功");
		} else
		{

			log.error("类别已存在：" + typeName);
			result.setMsg("类别已存在");
			result.setCode(-1);
			return result;
		}

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, Long typeId, String typeName, @RequestParam(value = "tsType", required = false)Integer tsType)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (typeId == null || typeId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("类别id不能为空");
			return result;
		}
		if (tsType != null && tsType != 0 && tsType != 1 && tsType != 2)
		{
			result.setCode(-1);
			result.setMsg("问卷类型错误：不允许的类型");
			return result;
		}

		// 更新记录
		TestingType oldTestingType = new TestingType();
		oldTestingType.setTypeId(typeId);
		oldTestingType.setTestingName(typeName);
		int isExists = testingTypeService.checkTestingTypeExits(oldTestingType);
		if (isExists <= 0)
		{
			TestingType testingType = new TestingType();
			testingType.setTypeId(typeId);
			if (tsType != null)
			{
				testingType.setTsType(tsType);
			}
			if (typeName != null && typeName != "")
			{
				testingType.setTestingName(typeName);
			}

			testingType.setModifyTime(new Date());
			testingTypeService.updateTestingType(testingType);
			result.setCode(0);
			result.setMsg("更新成功");
		} else
		{
			result.setCode(-1);
			log.error("类别已存在：" + typeName);
			result.setMsg("类别已存在");
			return result;
		}

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, String typeId, Byte isDel)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (typeId == null || typeId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("类别id不能为空");
			return result;
		}
		if (isDel == null || isDel.equals(""))
		{
			result.setCode(-1);
			result.setMsg("状态不能为空");
			return result;
		}
	

		// 处理记录id
		typeId = typeId.replace("[", "").replace("]", "");
		String[] idArr = typeId.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (int idx = 0; idx < idArr.length; idx++)
		{
			if (idArr[idx] != null && !idArr[idx].equals(""))
			{
				idList.add(Long.parseLong(idArr[idx]));
			}
		}
		testingTypeService.updateTestingTypeStatusByTypeIds(idList,isDel);
		/*// 修改记录状态：0启用，1禁用
		if (isDel != null && isDel != "")
		{
			if (isDel.equals("0"))
			{
				testingTypeService.updateTestingTypeEnableByTypeIds(idList);
			} else if (isDel.equals("1"))
			{
				testingTypeService.updateTestingTypeDisableByTypeIds(idList);
			}
		}*/
		result.setCode(0);
		result.setMsg("状态更新成功");

		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, ModelMap modelMap, Long typeId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (typeId == null || typeId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("类别id不能为空");
			return result;
		}

		// 查询记录详情
		TestingType testingType = testingTypeService.getTestingTypeByTypeId(typeId);

		result.put("obj", testingType);
		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getTestingType.json")
	@ResponseBody
	public Object getAllType(HttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();

		// 查询记录详情
		List<TestingType> testingTypeList = testingTypeService.getValidTestingTypeList();

		result.put("list", testingTypeList);
		result.setCode(0);
		return result;
	}
}
