package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.TestingComment;
import com.depression.model.UnreadComment;
import com.depression.model.api.dto.ApiTestingCommentDTO;
import com.depression.push.CustomMsgType;
import com.depression.service.MemberService;
import com.depression.service.PushService;
import com.depression.service.TestingCommentService;
import com.depression.service.UnreadCommentService;

@Controller
@RequestMapping("/testingComment")
public class TestingCommentController
{

	@Autowired
	MemberService memberService;

	@Autowired
	TestingCommentService testingCommentService;

	@Autowired
	UnreadCommentService unreadCommentService;
	@Autowired
	PushService pushService;

	/**
	 * 发表评论
	 * 
	 * @param session
	 * @param request
	 * @param testingComment
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addTestingComment.json")
	@ResponseBody
	public Object addTestingComment(HttpSession session, HttpServletRequest request, TestingComment testingComment)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			// 参数检查
			if (null == testingComment)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}
			if (null == testingComment.getTestingId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("问卷id不能为空");
				return result;
			}
			if (null == testingComment.getMid())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("会员id不能为空");
				return result;
			}
			if (null == testingComment.getCommentContent() || testingComment.getCommentContent().equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("评论内容不能为空");
				return result;
			}

			// 插入数据
			int ret = testingCommentService.insert(testingComment);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("插入用户评论数据失败");
				return result;
			}

			if (testingComment.getTestCommentId() != null && testingComment.getTestCommentId() != 0)
			{
				TestingComment qtc = testingCommentService.getTestingCommentByCommentId(testingComment.getTestCommentId());

				UnreadComment uc = new UnreadComment();
				uc.setCommentId(testingComment.getCommentId().longValue());
				uc.setMid(qtc.getMid());
				unreadCommentService.addTestingUnreadComment(uc);
				// 推送给父级评论
				if (!qtc.getMid().equals(testingComment.getMid()))
				{
					pushService.pushSingleDevice(CustomMsgType.TEST_COMMENTS_MSG_TO_AT, qtc.getMid(), null);
				}
			}

			// 返回结果
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("评论成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户评论接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateTestingComment.json")
	@ResponseBody
	public Object updateTestingComment(HttpSession session, HttpServletRequest request, TestingComment testingComment)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			if (null == testingComment)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			int ret = testingCommentService.update(testingComment);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("更新用户评论数据失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("更新成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户更新接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteTestingComment.json")
	@ResponseBody
	public Object deleteTestingComment(HttpSession session, HttpServletRequest request, Long commentId)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			if (null == commentId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("评论id不能为空");
				return result;
			}

			TestingComment testingComment = new TestingComment();
			testingComment.setCommentId(commentId);
			int ret = testingCommentService.delete(testingComment);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("删除用户评论数据失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("删除成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户删除接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getTestingCommentByTestingId.json")
	@ResponseBody
	public Object getTestingCommentByTestingId(HttpSession session, HttpServletRequest request, Integer pageIndex, Integer pageSize, Long testingId)
	{
		ResultEntity result = new ResultEntity();
		try
		{
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
			if (testingId == null)
			{
				result.setCode(-1);
				result.setMsg("问卷id不能为空");
				return result;
			}

			// 构造分页查询条件
			TestingComment queryTestingComment = new TestingComment();
			queryTestingComment.setPageIndex(pageIndex);
			queryTestingComment.setPageSize(pageSize);
			queryTestingComment.setTestingId(testingId);

			// 分页查询列表
			Integer count = testingCommentService.getPageCounts(queryTestingComment);
			List<TestingComment> testingCommentLists = testingCommentService.selectByPage(queryTestingComment);
			List<ApiTestingCommentDTO> testingCommentDTOLists=new ArrayList<ApiTestingCommentDTO>();
			
			for (TestingComment comment : testingCommentLists)
			{
				ApiTestingCommentDTO commentDTO=new ApiTestingCommentDTO();
				BeanUtils.copyProperties(comment, commentDTO);
				// 查询会员信息
				Member queryMember = new Member();
				queryMember.setMid(comment.getMid());
				queryMember.setIsEnable((byte) 0);
				Member curMember = memberService.getMember(queryMember);
				if (curMember != null)
				{
					commentDTO.setNickname(curMember.getNickname());
					commentDTO.setAvatar(curMember.getAvatar());
					commentDTO.setUserType(curMember.getUserType());
					String avatarThumbnail = curMember.getAvatarThumbnail();
					if (!StringUtils.isEmpty(avatarThumbnail))
					{
						commentDTO.setAvatarThumbnail(avatarThumbnail);
					} else
					{
						commentDTO.setAvatarThumbnail("");
					}
					commentDTO.setImAccount(curMember.getImAccount());
				}
				
				// 查询被艾特人信息
				if (comment.getTestCommentId() != null)
				{
					/*TestingComment queryTestingComment3 = new TestingComment();
					queryTestingComment3.setCommentId(comment.getTestCommentId());
					queryTestingComment3.setIsEnable((byte)0);*/
					TestingComment curTestingComment2 = testingCommentService.getTestingCommentByCommentId(comment.getTestCommentId());
					if(curTestingComment2 != null)
					{
						Member queryMember2 = new Member();
						queryMember2.setMid(curTestingComment2.getMid());
						queryMember2.setIsEnable((byte) 0);
						Member curMember2 = memberService.getMember(queryMember2);
						if (curMember2 != null)
						{
							commentDTO.setEitMid(curTestingComment2.getMid());
							commentDTO.setEitUserType(curMember2.getUserType());
							commentDTO.setEitNickname(curMember2.getNickname());
							commentDTO.setEitAvatar(curMember2.getAvatar());
							String avatarThumbnail2 = curMember2.getAvatarThumbnail();
							if (!StringUtils.isEmpty(avatarThumbnail2))
							{
								commentDTO.setEitAvatarThumbnail(avatarThumbnail2);
							} else
							{
								commentDTO.setEitAvatarThumbnail("");
							}
							commentDTO.setEitImAccount(curMember2.getImAccount());
						}
					}
				}
				
				testingCommentDTOLists.add(commentDTO);
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("list", testingCommentDTOLists);
			if (count == null)
			{
				result.put("count", 0);
			} else
			{
				result.put("count", count);
			}

			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取问卷评论接口失败");
			return result;
		}

	}
}
