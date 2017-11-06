package com.depression.controller.web.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.depression.model.Testing;
import com.depression.model.TestingOld;
import com.depression.service.TestingService;

public class TestingBase
{
	@Autowired
	private TestingService testingService;
	
	protected Long saveTesting(Long testingId, String tesingTitle, Long typeId, String subtitle, String contentExplain)
	{
		Long curTestingId = 0L;
		if (testingId == null || testingId.equals(""))
		{
			// 新增问卷信息
			Testing testing = new Testing();
			testing.setTypeId(typeId);
			testing.setTitle(tesingTitle);
			testing.setSubtitle(subtitle);
			testing.setContentExplain(contentExplain);
			testingService.insertTesting(testing);
			curTestingId = testing.getTestingId();
		} else
		{
			// 修改问卷信息
			Testing testing = new Testing();
			testing.setTypeId(typeId);
			testing.setTitle(tesingTitle);
			testing.setSubtitle(subtitle);
			testing.setContentExplain(contentExplain);
			testing.setTestingId(testingId);
			//testingService.updateTesting(testing);
			curTestingId = testingId;
		}
		return curTestingId;
	}
}
