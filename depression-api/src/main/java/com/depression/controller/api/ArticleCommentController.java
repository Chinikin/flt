package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ResultEntity;
import com.depression.model.ArticleComment;
import com.depression.model.Member;
import com.depression.model.UnreadComment;
import com.depression.push.CustomMsgType;
import com.depression.service.ArticleCommentService;
import com.depression.service.MemberService;
import com.depression.service.PushService;
import com.depression.service.UnreadCommentService;
import com.depression.utils.Configuration;

/**
 * 文章评论
 * 
 * @author hongqian_li
 * 
 */
@Controller
@RequestMapping("/ArticleComment")
public class ArticleCommentController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	ArticleCommentService articleCommentService;
	@Autowired
	MemberService memberService;
	@Autowired
	UnreadCommentService unreadCommentService;
	@Autowired
	PushService pushService;

	/**
	 * 获取文章评论（分页）
	 * 
	 * @param session
	 * @param articleId
	 * @param request
	 * @param entity
	 * @return
	 */

	@RequestMapping(method = RequestMethod.POST, value = "/listByPage.json")
	@ResponseBody
	public Object listByPage(HttpSession session, HttpServletRequest request, String articleId, String pageIndex, String pageSize)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		List<Object> resultJSON = new ArrayList<>();
		try
		{
			if (StringUtils.isEmpty(articleId) || StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			// 查找相关评论
			ArticleComment act = new ArticleComment();
			act.setArticleId(Long.valueOf(articleId));
			act.setPageIndex(Integer.valueOf(pageIndex));
			act.setPageSize(Integer.valueOf(pageSize));
			List<ArticleComment> acts = articleCommentService.selectByPage(act);
			// 组建数据
			for (ArticleComment articleComment : acts)
			{
				JSONObject jsb = new JSONObject();
				// 查找相关会员信息
				Member member = new Member();
				Long mid = articleComment.getMid();
				member.setMid(Long.valueOf(mid));
				member = memberService.getMember(member);
				jsb.put("commentId", articleComment.getCommentId());
				jsb.put("articleId", articleComment.getArticleId());
				jsb.put("commentContent", articleComment.getCommentContent());
				jsb.put("commentTime", articleComment.getCommentTime());
				jsb.put("commentTimestamp", articleComment.getCommentTime().getTime());
				if (null != member)
				{
					jsb.put("mid", mid);
					jsb.put("userType", member.getUserType());
					jsb.put("nickname", member.getNickname());
					jsb.put("avatar", member.getAvatar());
					String avatarThumbnail = member.getAvatarThumbnail();
					if (!StringUtils.isEmpty(avatarThumbnail))
					{
						jsb.put("avatarThumbnail", avatarThumbnail);
					} else
					{
						jsb.put("avatarThumbnail", "");
					}
					jsb.put("imAccount", member.getImAccount());
				}
				
				// 查看是否有父级评论
				Long parentCommentId = articleComment.getParentCommentId();
				member = new Member();
				if (0 != parentCommentId)
				{
					ArticleComment actm = new ArticleComment();
					actm.setCommentId(parentCommentId);
					List<ArticleComment> actms = articleCommentService.selectBy(actm);
					if (actms.size() == 0)
					{
						result.setCode(ResultEntity.ERROR);
						result.setError("无改评论");
						return result;
					}
					actm = actms.get(0);
					member.setMid(actm.getMid());
					member = memberService.getMember(member);
					jsb.put("parentCommentContent", actm.getCommentContent());
					jsb.put("parentCommentTime", actm.getCommentTime());
				} else
				{
					jsb.put("parentcommentContent", "");
					jsb.put("parentCommentTime", "");
				}
				if (null != member)
				{
					jsb.put("parentMid", member.getMid());
					jsb.put("parentNickname", member.getNickname());
					jsb.put("parentAvatar", member.getAvatar());
					jsb.put("parentUserType", member.getUserType());
					String avatarThumbnail = member.getAvatarThumbnail();
					if (!StringUtils.isEmpty(avatarThumbnail))
					{
						jsb.put("parentAvatarThumbnail", avatarThumbnail);
					} else
					{
						jsb.put("parentAvatarThumbnail", "");
					}
					jsb.put("parentImAccount", member.getImAccount());
					
				}
				
				resultJSON.add(jsb);
			}
			// 返回数据
			long count = articleCommentService.selectCount(act);
			result.put("totalCount", count);
			result.put("count", count);
			result.put("articleComments", resultJSON);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpSession session, HttpServletRequest request, String articleId, String mid, String commentContent, String currentCommentId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(articleId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章id不能为空");
				return result;
			}
			if (StringUtils.isEmpty(commentContent))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("评论内容不能为空");
				return result;
			}
			ArticleComment artc = new ArticleComment();
			artc.setArticleId(Long.valueOf(articleId));
			artc.setCommentContent(commentContent);
			artc.setMid(Long.valueOf(mid));
			long parentCommentId = StringUtils.isEmpty(currentCommentId) ? 0 : Long.valueOf(currentCommentId);
			ArticleComment parentComment = null;
			if (0 != parentCommentId)
			{
				ArticleComment qartc = new ArticleComment();
				qartc.setCommentId(parentCommentId);
				List<ArticleComment> qarts = articleCommentService.selectBy(qartc);
				if (qarts.size() == 0)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("父级评论为空，评论失败");
					return result;
				}
				parentComment = qarts.get(0);
			}
			artc.setParentCommentId(parentCommentId);
			articleCommentService.insert(artc);

			// 添加未读列表
			if (0 != parentCommentId)
			{
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(artc.getCommentId());
				unreadComment.setMid(parentComment.getMid());
				unreadCommentService.addArticleUnreadComment(unreadComment);
			}
			if (0 != parentCommentId)
			{
				// 给发表动态的人推送被评论的消息
				String tempmid = mid + "";
				String parentCommentMid = parentComment.getMid() + "";
				if (!tempmid.equals(parentCommentMid))
				{
					pushService.pushSingleDevice(CustomMsgType.ARTICLE_COMMENTS_MSG_TO_AT, parentComment.getMid(), null);
				}				
			}
			// 返回数据
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e);
		}
		result.setMsg("评论成功");
		return result;
	}
}
