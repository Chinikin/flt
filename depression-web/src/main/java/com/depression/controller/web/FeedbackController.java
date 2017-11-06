/******************************************************************
 ** 类    名：FeedbackController
 ** 描    述：意见反馈
 ** 创 建 者：fanxinhui
 ** 创建时间：2016-07-05
 ******************************************************************/
package com.depression.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.Feedback;
import com.depression.model.Member;
import com.depression.service.FeedbackService;
import com.depression.service.MemberService;

/**
 * (FeedbackController)
 * 
 * @author fanxinhui
 * @version 1.0.0 2016-07-05
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private MemberService memberService;

	/**
	 * 分页
	 * 
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, Feedback entity)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (entity == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("参数错误");
			return result;
		}
		if (entity.getPageIndex() == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (entity.getPageSize() == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}

		Feedback feedback = new Feedback();
		if (entity.getfContent() != null)
		{
			feedback.setfContent(entity.getfContent());
		}
		if (entity.getPageIndex() != null)
		{
			feedback.setPageIndex(entity.getPageIndex());
		}
		if (entity.getPageSize() != null)
		{
			feedback.setPageSize(entity.getPageSize());
		}

		// 查询集合
		Long totalCount = feedbackService.getPageCounts(feedback);
		List<Feedback> list = feedbackService.getPageList(feedback);
		for (Feedback fdk : list)
		{
			Long curMid = fdk.getMid();
			if (curMid != null)
			{
				Member member = new Member();
				member.setMid(curMid);
				Member curMember = memberService.getMember(member);
				if (curMember != null)
				{
					fdk.setNickname(curMember.getNickname());
				}
			}
		}
		result.put("list", list);
		result.put("count", totalCount);

		result.setCode(ResultEntity.SUCCESS);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, Feedback entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);

		// 参数检查
		if (entity == null)
		{
			result.setCode(-1);
			result.setMsg("参数错误");
			return result;
		}
		if (entity.getFid() == null)
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}

		// 查询记录详情
		Feedback feedback = feedbackService.getFeedbackById(entity.getFid());
		Long curMid = feedback.getMid();
		if (curMid != null)
		{
			Member member = new Member();
			member.setMid(curMid);
			Member curMember = memberService.getMember(member);
			if (curMember != null)
			{
				feedback.setNickname(curMember.getNickname());
			}
		}

		result.put("obj", feedback);
		return result;
	}
}
