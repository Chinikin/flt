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
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.UnreadComment;
import com.depression.push.CustomMsgType;
import com.depression.service.AdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.PunishmentService;
import com.depression.service.PushService;
import com.depression.service.UnreadCommentService;

/**
 * 会员资源评论(普通会员不允许评论)
 * 
 * @author hongqian_li
 * @Date 2016/05/06
 */
@Controller
@RequestMapping("/MemberAdvisoryComment")
public class MemberAdvisoryCommentController
{

	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	MemberService memberService;
	@Autowired
	AdvisoryService advisoryService;
	@Autowired
	UnreadCommentService unreadCommentService;
	@Autowired
	PunishmentService punishmentService;
	@Autowired
	PushService pushService;

	/**
	 * 发表咨询评论
	 * 
	 * @param session
	 * @param request
	 * @param advisoryId
	 * @param mid
	 * @param commentContent
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpSession session, HttpServletRequest request, Long advisoryId, Long mid, String commentContent)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == mid || null == advisoryId || StringUtils.isEmpty(commentContent))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			Member mb = new Member();
			mb.setMid(mid);
			mb = memberService.getMember(mb);
			if (null == mb || mb.getUserType() == 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("用户没有权限评论！！");
				return result;
			}

			MemberAdvisory ma = advisoryService.obtainAdvisoryById(advisoryId);
			if (ma == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("咨询不存在");
				return result;
			}

			MemberAdvisoryComment mbac = new MemberAdvisoryComment();
			mbac.setCommentContent(commentContent);
			mbac.setPraiseNum(0L);
			mbac.setReadStatus((byte) 0);
			mbac.setMid(mid);
			mbac.setAdvisoryId(advisoryId);
			mbac.setParentId(0L);
			advisoryService.createComment(mbac);
			
			if(memberService.isPsychos(mid))
			{//增加咨询师回答数
				memberService.transIncrAnswerCount(mid);
			}

			MemberAdvisory memberAdvisory = advisoryService.obtainAdvisoryById(advisoryId);
			if (!memberAdvisory.getMid().equals(mid))
			{
				// 添加未读列表
				// 给咨询作者添加未读信息
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(mbac.getCommentId());
				unreadComment.setMid(ma.getMid());
				unreadCommentService.addAdvisoryUnreadComment(unreadComment);
			}
			// 给发表动态的人推送被评论的消息

			String midStr = mid + "";
			String maMidStr = ma.getMid() + "";
			if (!midStr.equals(maMidStr))
			{				
				pushService.pushSingleDevice(CustomMsgType.ADVISORY_COMMENTS_MSG, ma.getMid(), null);
			}
			// 返回数据
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}
	
	/**
	 * 发表咨询评论
	 * 
	 * @param advisoryId
	 * @param mid
	 * @param commentContent
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/savex.json")
	@ResponseBody
	public Object savex(Long advisoryId, Long mid, String commentContent, Long parentId)
	{
		ResultEntity result = new ResultEntity();
		
		if(punishmentService.obtainDisableMessageDays(mid) > 0)
		{
			result.put("disableMessageDays", punishmentService.obtainDisableMessageDays(mid));
			result.setCode(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getCode());
			result.setMsg(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getMessage());
			return result;
		}
		
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == mid || null == advisoryId || StringUtils.isEmpty(commentContent))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			MemberAdvisory ma = advisoryService.obtainAdvisoryById(advisoryId);
			if (ma == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("咨询不存在");
				return result;
			}
			
			Member mb = new Member();
			mb.setMid(mid);
			mb = memberService.getMember(mb);
			if (null == mb || (mb.getUserType() == 1 &&!mid.equals(ma.getMid())))
			{//只有本人可以回复
				result.setCode(ResultEntity.ERROR);
				result.setError("用户没有权限评论！！");
				return result;
			}


			MemberAdvisoryComment mbac = new MemberAdvisoryComment();
			mbac.setCommentContent(commentContent);
			mbac.setPraiseNum(0L);
			mbac.setReadStatus((byte) 0);
			mbac.setMid(mid);
			mbac.setAdvisoryId(advisoryId);
			if(parentId==null) parentId = 0L;
			mbac.setParentId(parentId);
			advisoryService.createComment(mbac);
			
			if(memberService.isPsychos(mid))
			{//增加咨询师回答数
				memberService.transIncrAnswerCount(mid);
			}

			if (!ma.getMid().equals(mid))
			{
				// 添加未读列表
				// 给咨询作者添加未读信息
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(mbac.getCommentId());
				unreadComment.setMid(ma.getMid());
				unreadCommentService.addAdvisoryUnreadComment(unreadComment);
				
				//推送				
				pushService.pushSingleDevice(CustomMsgType.ADVISORY_COMMENTS_MSG, ma.getMid(), null);
			}
			MemberAdvisoryComment pMbac = advisoryService.obtainCommentById(parentId);
			if(pMbac!=null && !pMbac.getMid().equals(mid)&&!pMbac.getMid().equals(ma.getMid()))
			{
				// 添加未读列表
				// 给父评论作者添加未读信息
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(mbac.getCommentId());
				unreadComment.setMid(pMbac.getMid());
				unreadCommentService.addAdvisoryUnreadComment(unreadComment);
				
				//推送				
				pushService.pushSingleDevice(CustomMsgType.ADVISORY_COMMENTS_MSG_TO_AT, pMbac.getMid(), null);
			}
			
			// 返回数据
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取咨询评论（分页）
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/listByPage.json")
	@ResponseBody
	public Object listByPage(HttpSession session, HttpServletRequest request, Long mid, Long advisoryId, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		List<Object> resultJSON = new ArrayList<>();
		try
		{
			if (null == pageIndex || null == pageSize || null == advisoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			// 查找评论
			List<MemberAdvisoryComment> mbacs = advisoryService.obtainCommentsWithPage(advisoryId, pageIndex, pageSize);
			for (MemberAdvisoryComment memberAdvisoryComment : mbacs)
			{

				JSONObject jsb = new JSONObject();
				Long midtmp = memberAdvisoryComment.getMid();
				Member member = new Member();
				member.setMid(midtmp);
				member = memberService.getMember(member);
				if (null == member)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("回复会员不存在");
					return result;
				}
				if (null != mid)
				{
					if (advisoryService.isPraisedComment(mid, memberAdvisoryComment.getCommentId()) > 0)
					{
						jsb.put("isEmbrace", 1);// 已赞
					} else
					{
						jsb.put("isEmbrace", 0);// 未赞
					}
				} else
				{
					jsb.put("isEmbrace", 0);// 未赞
				}
				jsb.put("commentId", memberAdvisoryComment.getCommentId());
				jsb.put("mid", midtmp);
				jsb.put("title", member.getTitle());
				jsb.put("nickname", member.getNickname());
				jsb.put("imAccount", member.getImAccount());
				jsb.put("userType", member.getUserType());
				jsb.put("avatar", member.getAvatar());
				String avatarThumbnail = member.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					jsb.put("avatarThumbnail", avatarThumbnail);
				} else
				{
					jsb.put("avatarThumbnail", "");
				}
				jsb.put("commentContent", memberAdvisoryComment.getCommentContent());
				jsb.put("commentTime", memberAdvisoryComment.getCommentTime());
				jsb.put("praiseNum", memberAdvisoryComment.getPraiseNum());
				resultJSON.add(jsb);
			}
			// 返回数据
			long count = advisoryService.countComments(advisoryId);
			result.put("totalCount", count);
			result.put("advisoryComments", resultJSON);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}
	
	/**
	 * 获取咨询评论（分页）
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/listByPagex.json")
	@ResponseBody
	public Object listByPagex(HttpSession session, HttpServletRequest request, Long mid, Long advisoryId, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		List<Object> resultJSON = new ArrayList<>();
		try
		{
			if (null == pageIndex || null == pageSize || null == advisoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			//查找咨询信息
			MemberAdvisory memberAdvisory = advisoryService.obtainAdvisoryById(advisoryId);
			// 查找评论
			List<MemberAdvisoryComment> mbacs = advisoryService.obtainCommentsWithPage(advisoryId, pageIndex, pageSize);
			for (MemberAdvisoryComment memberAdvisoryComment : mbacs)
			{

				JSONObject jsb = new JSONObject();
				Long midtmp = memberAdvisoryComment.getMid();
				Member member = new Member();
				member.setMid(midtmp);
				member = memberService.getMember(member);
				if (null == member)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("回复会员不存在");
					return result;
				}
				if (null != mid)
				{
					if (advisoryService.isPraisedComment(mid, memberAdvisoryComment.getCommentId()) > 0)
					{
						jsb.put("isEmbrace", 1);// 已赞
					} else
					{
						jsb.put("isEmbrace", 0);// 未赞
					}
				} else
				{
					jsb.put("isEmbrace", 0);// 未赞
				}
				jsb.put("commentId", memberAdvisoryComment.getCommentId());
				jsb.put("mid", midtmp);
				jsb.put("title", member.getTitle());
				if(member.getUserType()==1 && !member.getMid().equals(mid))
				{
					String mobilePhone = member.getMobilePhone();
					String phoneSuf = mobilePhone.substring(mobilePhone.length() - 5) ;
					Integer nickFinal = (Integer.parseInt(phoneSuf) + 1234) % 10000;
					jsb.put("nickname", "匿名用户" + nickFinal);
				}else{
					jsb.put("nickname", member.getNickname());
				}
				jsb.put("imAccount", member.getImAccount());
				jsb.put("userType", member.getUserType());
				jsb.put("avatar", member.getAvatar());
				String avatarThumbnail = member.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					jsb.put("avatarThumbnail", avatarThumbnail);
				} else
				{
					jsb.put("avatarThumbnail", "");
				}
				jsb.put("commentContent", memberAdvisoryComment.getCommentContent());
				jsb.put("commentTime", memberAdvisoryComment.getCommentTime());
				jsb.put("praiseNum", memberAdvisoryComment.getPraiseNum());
				jsb.put("advisoryMid", memberAdvisory.getMid());

				// 是否有父级评论
				Long parentCommentId = memberAdvisoryComment.getParentId();
				member = new Member();
				if (0 != parentCommentId)
				{
					List<MemberAdvisoryComment> tempmacs = advisoryService.obtainCommentsByAdvisoryId(advisoryId);

					if (tempmacs.size() == 0)
					{
						jsb.put("parentcommentContent", "");
						jsb.put("parentCommentTime", "");
					}else{
						MemberAdvisoryComment tempmac = tempmacs.get(0);
						member.setMid(tempmac.getMid());
						member = memberService.getMember(member);
						if (null == member)
						{
							jsb.put("parentcommentContent", "");
							jsb.put("parentCommentTime", "");
						}else
						{
							jsb.put("parentCommentContent", tempmac.getCommentContent());
							jsb.put("parentCommentTime", tempmac.getCommentTime());
							jsb.put("parentMid", member.getMid());
							jsb.put("parentUserType", member.getUserType());
							if(member.getUserType()==1 && !member.getMid().equals(mid))
							{
								String mobilePhone = member.getMobilePhone();
								String phoneSuf = mobilePhone.substring(mobilePhone.length() - 5) ;
								Integer nickFinal = (Integer.parseInt(phoneSuf) + 1234) % 10000;
								jsb.put("parentNickname", "匿名用户" + nickFinal);
							}else{
								jsb.put("parentNickname", member.getNickname());
							}
							jsb.put("parentAvatar", member.getAvatar());
							avatarThumbnail = member.getAvatarThumbnail();
							if (!StringUtils.isEmpty(avatarThumbnail))
							{
								jsb.put("parentAvatarThumbnail", avatarThumbnail);
							} else
							{
								jsb.put("parentAvatarThumbnail", "");
							}
						}
					}

				} else
				{
					jsb.put("parentcommentContent", "");
					jsb.put("parentCommentTime", "");
				}

				
				resultJSON.add(jsb);
			}
			// 返回数据
			long count = advisoryService.countComments(advisoryId);
			result.put("totalCount", count);
			result.put("advisoryComments", resultJSON);
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}
}
