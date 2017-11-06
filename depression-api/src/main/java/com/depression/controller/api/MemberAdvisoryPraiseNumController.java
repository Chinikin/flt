package com.depression.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberAdvisoryPraiseNum;
import com.depression.service.AdvisoryService;
import com.depression.service.MemberService;

/**
 * 会员咨询点赞
 * 
 * @author hongqian_li
 * @date 2016/05/10
 */
@Controller
@RequestMapping("/MemberAdvisoryPraiseNum")
public class MemberAdvisoryPraiseNumController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	MemberService memberService;
	@Autowired
	AdvisoryService advisoryService;

	/**
	 * 添加点赞
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 *            用户ID
	 * @param commentId
	 *            咨询评论师ID
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add.json")
	@ResponseBody
	public Object add(HttpSession session, HttpServletRequest request, Long mid, Long commentId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == mid || null == commentId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			List<MemberAdvisoryComment> mbacs = advisoryService.obtainCommentsByAdvisoryId(commentId);
			if (mbacs.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("该评论不存在！！");
				return result;
			}

			// 查找是否已经点赞
			if (advisoryService.isPraisedComment(mid, commentId) != 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("已经点赞，无需再次点击");
				return result;
			}
			MemberAdvisoryComment mbac = mbacs.get(0);
			//咨询师被赞次数
			memberService.transIncrAppreciatedCount(mbac.getMid());
			//修改评论被赞次数
			Long embraceNum = mbac.getPraiseNum();
			mbac.setCommentTime(null);
			mbac.setPraiseNum(null == embraceNum ? 1 : embraceNum + 1);
			advisoryService.modifyComment(mbac);
			//添加点赞记录
			MemberAdvisoryPraiseNum mbap = new MemberAdvisoryPraiseNum();
			mbap.setCommentId(commentId);
			mbap.setMid(mid);
			advisoryService.createCommentPraise(mbap);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}
}
