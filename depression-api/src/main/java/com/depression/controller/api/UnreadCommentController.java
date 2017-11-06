package com.depression.controller.api;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.ArticleComment;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberUpdate;
import com.depression.model.MemberUpdateComment;
import com.depression.model.TestingComment;
import com.depression.model.UnreadComment;
import com.depression.service.AdvisoryService;
import com.depression.service.ArticleCommentService;
import com.depression.service.HeartmateService;
import com.depression.service.MemberService;
import com.depression.service.TestingCommentService;
import com.depression.service.UnreadCommentService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/UnreadCommentController")
public class UnreadCommentController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	UnreadCommentService unreadCommentService;
	@Autowired
	ArticleCommentService articleCommentService;
	@Autowired
	MemberService memberService;

	@Autowired
	TestingCommentService testingCommentService;
	@Autowired
	HeartmateService heartmateService;
	@Autowired
	AdvisoryService advisoryService;

	@RequestMapping(value = "/getUnreadCommentList.json")
	@ResponseBody
	public Object getUnreadCommentList(HttpSession session, HttpServletRequest request, Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (mid == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("会员id不能为空");
				return result;
			}
			UnreadComment unreadComment = new UnreadComment();
			unreadComment.setMid(mid);
			if (pageIndex != null)
			{
				unreadComment.setPageIndex(pageIndex);
			}
			if (pageSize != null)
			{
				unreadComment.setPageSize(pageSize);
			}

			List<UnreadComment> qucList = unreadCommentService.queryUnreadCommentByPage(unreadComment);
			Iterator<UnreadComment> iter = qucList.iterator();
			while (iter.hasNext())
			{
				UnreadComment uc = iter.next();
				try{
					if(uc.getIsRead() == 0)
					{//设置已读
						unreadCommentService.readComment(uc.getUnreadId());
					}
				}catch (Exception e)
				{
					log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
					result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
					result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
					return result;
				}
				
				try
				{
					if (uc.getType() == UnreadComment.TYPE_ARTICLE)
					{
						// 获取评论的信息
						ArticleComment ac = new ArticleComment();
						ac.setCommentId(uc.getCommentId());
						ArticleComment articleComment = articleCommentService.selectBy(ac).get(0);
						uc.setCommentContent(articleComment.getCommentContent());
						uc.setChapterId(articleComment.getArticleId());
						uc.setCommentTime(articleComment.getCommentTime());
						// 获取会员信息
						Member m = new Member();
						m.setMid(articleComment.getMid());
						Member member = memberService.getMember(m);
						uc.setAvatar(member.getAvatar());
						String avatarThumbnail = member.getAvatarThumbnail();
						if (!StringUtils.isEmpty(avatarThumbnail))
						{
							uc.setAvatarThumbnail(avatarThumbnail);
						} else
						{
							uc.setAvatarThumbnail("");
						}
						uc.setCmid(member.getMid());
						uc.setNickname(member.getNickname());
						uc.setUserType(member.getUserType());
						uc.setTitle(member.getTitle());
						uc.setIsAnony((byte) 1);
						// 获取父级评论的内容
						ac.setCommentId(articleComment.getParentCommentId());
						articleComment = articleCommentService.selectBy(ac).get(0);
						uc.setParentContent(articleComment.getCommentContent());
					} else if (uc.getType() == UnreadComment.TYPE_TESTING)
					{
						// 获取评论的信息
						TestingComment tc = new TestingComment();
						tc.setCommentId(uc.getCommentId());
						TestingComment testingComment = testingCommentService.getTestingCommentByCommentId(uc.getCommentId());
						uc.setCommentContent(testingComment.getCommentContent());
						uc.setChapterId(testingComment.getTestingId().longValue());
						uc.setCommentTime(testingComment.getCommentTime());
						// 获取会员信息
						Member m = new Member();
						m.setMid(testingComment.getMid());
						Member member = memberService.getMember(m);
						uc.setAvatar( member.getAvatar());
						String avatarThumbnail = member.getAvatarThumbnail();
						if (!StringUtils.isEmpty(avatarThumbnail))
						{
							uc.setAvatarThumbnail(avatarThumbnail);
						} else
						{
							uc.setAvatarThumbnail("");
						}
						uc.setCmid(member.getMid());
						uc.setNickname(member.getNickname());
						uc.setUserType(member.getUserType());
						uc.setTitle(member.getTitle());
						uc.setIsAnony((byte) 1);
						// 获取父级评论的内容
						tc.setCommentId(testingComment.getTestCommentId());
						testingComment = testingCommentService.getTestingCommentByCommentId(tc.getCommentId());
						uc.setParentContent(testingComment.getCommentContent());
					} else if (uc.getType() == UnreadComment.TYPE_UPDATE_BOTTLE || uc.getType() == UnreadComment.TYPE_UPDATE_CIRCLE)
					{
						MemberUpdateComment memberUpdateComment = heartmateService.obtainCommentById(uc.getCommentId());
						// 确认心友圈是否被删除，此处如心友圈被删除会触发异常，然后删除该评论
						MemberUpdate memberUpdate = heartmateService.obtainUpdateById(memberUpdateComment.getUpdateId());
						
						// 获取评论的信息
						uc.setCommentContent(memberUpdateComment.getCommentContent());
						uc.setChapterId(memberUpdate.getUpdateId());
						uc.setCommentTime(memberUpdateComment.getCommentTime());
						// 获取会员信息
						Member m = new Member();
						m.setMid(memberUpdateComment.getMid());
						Member member = memberService.getMember(m);
						uc.setAvatar(member.getAvatar());
						String avatarThumbnail = member.getAvatarThumbnail();
						if (!StringUtils.isEmpty(avatarThumbnail))
						{
							uc.setAvatarThumbnail(avatarThumbnail);
						} else
						{
							uc.setAvatarThumbnail("");
						}
						uc.setNickname(member.getNickname());
						uc.setCmid(member.getMid());
						uc.setUserType(member.getUserType());
						uc.setTitle(member.getTitle());
						
						uc.setIsAnony(memberUpdateComment.getIsAnony());
						
						if(memberUpdateComment.getIsAnony() == 0 && !mid.equals(member.getMid()))
						{//咨询匿名且非本人
							Integer nickFinal = (member.getMid().intValue() + 1234) % 10000;

							String city = member.getLocation();
							//地址做处理, 去掉市
							if(city == null )
							{
								city = "";
							}
							else if(city.endsWith("市"))
							{
								city = city.substring(0, city.length() -1);
							}

							uc.setNickname(city + "匿名用户" + nickFinal);
							uc.setAvatar(null);
							uc.setAvatarThumbnail(null);
						}

						if (memberUpdateComment.getParentId() != null && memberUpdateComment.getParentId() != 0)
						{
							MemberUpdateComment pmuc = heartmateService.obtainCommentById(memberUpdateComment.getParentId());
							uc.setParentContent(pmuc.getCommentContent());
						} else
						{
							uc.setParentContent(memberUpdate.getContent());
						}
					} else if (uc.getType() == UnreadComment.TYPE_ADVISORY)
					{
						MemberAdvisoryComment advisoryComment = advisoryService.obtainCommentById(uc.getCommentId());
						// 获取评论的信息
						uc.setCommentContent(advisoryComment.getCommentContent());
						uc.setChapterId(advisoryComment.getAdvisoryId());
						uc.setCommentTime(advisoryComment.getCommentTime());
						// 获取会员信息
						Member m = new Member();
						m.setMid(advisoryComment.getMid());
						Member member = memberService.getMember(m);
						uc.setAvatar(member.getAvatar());
						String avatarThumbnail = member.getAvatarThumbnail();

						if (!StringUtils.isEmpty(avatarThumbnail))
						{
							uc.setAvatarThumbnail(avatarThumbnail);
						} else
						{
							uc.setAvatarThumbnail("");
						}
						uc.setCmid(member.getMid());
						uc.setNickname(member.getNickname());
						uc.setUserType(member.getUserType());
						uc.setTitle(member.getTitle());
						
						uc.setIsAnony(advisoryComment.getIsAnony());
						
						if(advisoryComment.getIsAnony() == 0 && !mid.equals(member.getMid()))
						{//咨询匿名且非本人
							Integer nickFinal = (member.getMid().intValue() + 1234) % 10000;

							String city = member.getLocation();
							//地址做处理, 去掉市
							if(city == null )
							{
								city = "";
							}
							else if(city.endsWith("市"))
							{
								city = city.substring(0, city.length() -1);
							}

							uc.setNickname(city + "匿名用户" + nickFinal);
							uc.setAvatar(null);
							uc.setAvatarThumbnail(null);
						}

						MemberAdvisory memberAdvisory = advisoryService.obtainAdvisoryById(advisoryComment.getAdvisoryId());
						uc.setParentContent(memberAdvisory.getContent());
					}
				} catch (Exception e)
				{
					unreadCommentService.deleteUnreadCommentById(uc.getUnreadId());
					iter.remove();
				}
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("totalCount", unreadCommentService.queryUnreadCommentCount(mid));
			result.put("list", qucList);
			result.put("curPageCount", qucList.size());

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
			log.error(e.getStackTrace());
			return result;
		}
		return result;
	}
	
	/**
	 * 获取未读的评论数量
	 * @param mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainUnreadCount.json")
	@ResponseBody
	public Object obtainUnreadCount(Long mid)
	{

		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		Long count;
		try{
			count = unreadCommentService.countUnread(mid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 删除未读评论
	 * @param unreadId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteUnreadComment.json")
	@ResponseBody
	public Object deleteUnreadComment(Long unreadId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(unreadId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			unreadCommentService.deleteUnreadCommentById(unreadId);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

}
