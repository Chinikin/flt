package com.depression.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.controller.web.common.TestingBase;
import com.depression.entity.ResultEntity;
import com.depression.model.TestingSection;
import com.depression.model.TestingSectionOld;
import com.depression.service.TestingSectionService;

/**
 * 试卷得分区间信息
 * 		1.（试卷ID，得分上限， 得分下限）确定唯一得分区间，不可重复
 * @author caizj
 *
 */
@Controller
@RequestMapping("/testingSection")
public class TestingSectionController extends TestingBase
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private TestingSectionService testingSectionService;
	
	/**
	 * 根据testingId获取某试卷的得分区间list
	 * 
	 * @param request
	 * @param modelMap
	 * @param testingId		试卷ID	必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/listByTestingId.json")
	@ResponseBody
	public Object listByTestingId(HttpServletRequest request, ModelMap modelMap, Long testingId)
	{
		ResultEntity result = new ResultEntity();
		
		if(testingId == null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		List<TestingSection> listTS = testingSectionService.getTestingSectionByTestingId(testingId);
		
		result.put("list", listTS);
		result.put("count", listTS.size());
		result.setCode(0);
		return result;
	}
	
	/**
	 * 根据testingId和score获取试卷测试结果的得分区间
	 * 
	 * @param request
	 * @param modelMap
	 * @param testingId		试卷ID	必填
	 * @param score			试卷得分	必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/selectByTestingIdAndScore.json")
	@ResponseBody
	public Object selectByTestingIdAndScore(HttpServletRequest request, ModelMap modelMap, Long testingId, Integer score)
	{
		ResultEntity result = new ResultEntity();
		
		if(testingId==null || score==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		List<TestingSection> testingSectionList = testingSectionService.getTestingSectionByTestingIdAndScore(
				testingId, score);
		
		if (testingSectionList != null && testingSectionList.size() > 0)
		{
			result.put("obj", testingSectionList.get(0));
		}
		
		result.setCode(0);
		return result;
	}
	
	/**
	 * 试卷得分区间信息保存
	 * 		1.如果信息保存时，已经存在记录，则执行更新原记录
	 * @param request
	 * @param modelMap
	 * @param testingId		试卷ID		必填
	 * @param greaterThan	得分下限		必填
	 * @param lessThan		得分上限		必填
	 * @param level			级别描述		必填
	 * @param detail		详细描述		必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, 
			Long testingId, Integer greaterThan, Integer lessThan, String level, String detail,
			String tesingTitle, Long typeId, String subtitle, String contentExplain)
	{
		ResultEntity result = new ResultEntity();
		
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

		// 新增试卷
		testingId = saveTesting(testingId, tesingTitle, typeId, subtitle, contentExplain).longValue();
		
		if(testingId==null || greaterThan==null || lessThan==null || level==null || detail==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		TestingSection testingSection = new TestingSection();
		//testingId, greaterThan, lessThan, level, detail
		testingSection.setTestingId(testingId);
		testingSection.setGreaterThan(greaterThan);
		testingSection.setLessThan(lessThan);
		testingSection.setLevel(level);
		testingSection.setDetail(detail);
		testingSection.setIsDelete((byte)0);
		List<TestingSection> list = testingSectionService.checkTestingSectionExist(testingSection);
		if(list.size()==0)
		{
			testingSectionService.insertTestingSection(testingSection);
			result.setMsg("试卷得分区间信息保存成功");
		}else
		{
			testingSection.setSectionId(list.get(0).getSectionId());
			testingSectionService.updateTestingSection(testingSection);
			result.setMsg("原记录已存在，试卷得分区间信息更新成功");
		}
		
		result.setCode(0);
		return result;
	}
	
	/**
	 * 试卷得分区间信息更新
	 * 
	 * @param request
	 * @param modelMap
	 * @param sectionId		   区间ID		必填	
	 * @param greaterThan    得分下限		选填
	 * @param lessThan       得分上限		选填
	 * @param level          级别描述		选填
	 * @param detail         详细描述		选填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, Long sectionId,
	Integer greaterThan, Integer lessThan, String level, String detail, Long testingId,
	String tesingTitle, Long typeId, String subtitle, String contentExplain)
	{
		ResultEntity result = new ResultEntity();
		
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

		// 新增试卷
		testingId = saveTesting(testingId, tesingTitle, typeId, subtitle, contentExplain).longValue();
		
		TestingSection testingSection = new TestingSection();
		testingSection.setGreaterThan(greaterThan);
		testingSection.setLessThan(lessThan);
		testingSection.setLevel(level);
		testingSection.setDetail(detail);
		testingSection.setSectionId(sectionId);
		testingSection.setTestingId(testingId);
		testingSection.setIsEnable((byte)0);
		TestingSection oldTS = testingSectionService.getTestingSectionBySectionId(sectionId);
		if(oldTS == null)
		{
			result.setCode(-3);
			result.setMsg("不存在该sectionId对应的记录");
			return result;
		}else
		{
			testingSectionService.updateTestingSection(testingSection);
			result.setMsg("试卷得分区间信息更新成功");
		}
		
		result.setCode(0);
		return result;
	}
	
	/**
	 * 试卷得分区间信息状态变更
	 * 
	 * @param request
	 * @param modelMap
	 * @param sectionId		区间ID	必填
	 * @param isDelete		删除状态	必填
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, Long sectionId, Byte isDelete)
	{
		ResultEntity result = new ResultEntity();
		
		if(sectionId==null || isDelete==null)
		{
			result.setCode(-3);
			result.setMsg("所有参数不能为null");
			return result;
		}
		
		
		TestingSection testingSection = testingSectionService.getTestingSectionBySectionId(sectionId);
		if(testingSection == null)
		{
			result.setCode(-3);
			result.setMsg("不存在该resultId对应的记录");
			return result;
		}else
		{
			testingSection.setIsEnable(isDelete);
			testingSectionService.updateTestingSection(testingSection);
			result.setMsg("状态更新成功");
		}
		
		result.setCode(0);
		return result;
	}
	
}
