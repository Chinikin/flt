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
import com.depression.model.TestingType;
import com.depression.service.ImageStorageService;
import com.depression.service.TestingTypeService;

@Controller
@RequestMapping(value={"/testingType", "/superClass/testingType" })
public class TestingTypeController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingTypeService testingTypeService;

	@Autowired
	private ImageStorageService imageStorageService;
	
	//专业测试
	public final static Integer TSTYPE_PRO=0;
	//趣味测试
	public final static Integer TSTYPE_FUNNY=1;
	//学生测试
	public final static Integer TSTYPE_STU=2;
	
	
	//专业测试全部标志
	public final static Integer TSTYPE_PRO_ALL=100;
	//专业测试全部标志
	public final static Integer TSTYPE_FUNNY_ALL=101;
	//专业测试全部标志
	public final static Integer TSTYPE_STU_ALL=102;
	//测试全部标志
	public final static Integer TSTYPE_ALL=null;
		
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap modelMap, Integer tsType)
	{
		ResultEntity result = new ResultEntity();
		TestingType testingType = new TestingType();
		testingType.setTsType(tsType);
		testingType.setIsEnable((byte)0);
		List<TestingType> ttList = testingTypeService.getTestingTypeListByQueryTestingType(testingType);
		TestingType type=new TestingType();
		type.setTestingName("全部");
		if(tsType == TSTYPE_PRO){
			type.setTypeId(TSTYPE_PRO_ALL.longValue());
		}
		if(tsType == TSTYPE_FUNNY){
			type.setTypeId(TSTYPE_FUNNY_ALL.longValue());
		}
		if(tsType == TSTYPE_STU){
			type.setTypeId(TSTYPE_STU_ALL.longValue());
		}
		if(tsType == null){
			type.setTypeId(null);
		}
		ttList.add(0,type);
		result.put("list", ttList);
		result.put("count", ttList.size());
		result.setCode(ResultEntity.SUCCESS);
		return result;
		
		// 查找趣味测试类别
		/*if (funnyOnly != null && funnyOnly == 1)
		{
			TestingType testingType = new TestingType();
			testingType.setTsType(1);
			testingType.setIsEnable((byte)0);
			List<TestingType> ttList = testingTypeService.getTestingTypeListByQueryTestingType(testingType);

			result.put("list", ttList);
			result.put("count", ttList.size());
			result.setCode(ResultEntity.SUCCESS);
			return result;
		} else
		{
			// 查找专业测试类别
			TestingType testingType = new TestingType();
			testingType.setTsType(0);
			testingType.setIsEnable((byte)0);
			List<TestingType> ttList = testingTypeService.getTestingTypeListByQueryTestingType(testingType);
			result.put("list", ttList);
			result.put("count", ttList.size());
			result.setCode(ResultEntity.SUCCESS);
			return result;
		}*/
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getFunnyTestingType.json")
	@ResponseBody
	public Object getFunnyTestingType(HttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			TestingType testingType = new TestingType();
			testingType.setTsType(1);
			testingType.setIsEnable((byte)0);
			List<TestingType> ttList = testingTypeService.getTestingTypeListByQueryTestingType(testingType);

			result.put("list", ttList);
			result.put("count", ttList.size());
			result.setCode(ResultEntity.SUCCESS);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}
	
	/**
	 * 获取学生测试类别列表
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getStuTestType.json")
	@ResponseBody
	public Object getStuTestType(HttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			TestingType testingType = new TestingType();
			testingType.setTsType(2);
			testingType.setIsEnable((byte)0);
			List<TestingType> ttList = testingTypeService.getTestingTypeListByQueryTestingType(testingType);
			TestingType type=new TestingType();
			type.setTypeId(TSTYPE_STU_ALL.longValue());
			type.setTestingName("全部");
			ttList.add(0,type);
			result.put("list", ttList);
			result.put("count", ttList.size());
			result.setCode(ResultEntity.SUCCESS);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
	}
}
