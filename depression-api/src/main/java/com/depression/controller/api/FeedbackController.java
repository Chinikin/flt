package com.depression.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.Feedback;
import com.depression.service.FeedbackService;

/**
 * 意见反馈
 * 
 * @author hongqian_li
 * 
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	FeedbackService feedbackService;

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpSession session, HttpServletRequest request, Long mid, String content)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(content))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("请输入您的建议或意见");
				return result;
			}
			Feedback fb = new Feedback();
			if (null != mid)
			{
				fb.setMid(mid);
			}
			fb.setfContent(content);
			feedbackService.insertFeedback(fb);
			// 返回数据
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e);
		}
		result.setMsg("添加成功");
		return result;
	}

}
