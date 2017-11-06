package com.depression.controller.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdvisoryTag;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberAdvisoryCuration;
import com.depression.model.MemberAdvisoryDetail;
import com.depression.model.api.dto.ApiMemberAdvisoryCurationDTO;
import com.depression.model.web.dto.WebMemberAdvisoryCommentDTO;
import com.depression.model.web.dto.WebMemberAdvisoryCurationDTO;
import com.depression.model.web.dto.WebMemberAdvisoryDTO;
import com.depression.model.web.vo.WebMemberAdvisoryVO;
import com.depression.service.AdvisoryTagService;
import com.depression.service.MemberAdvisoryCurationService;
import com.depression.service.AdvisoryService;
import com.depression.service.MemberService;

/**
 * 精选问答
 * 
 * @author xinhui_fan
 * 
 */
@Controller
@RequestMapping("/advisoryCuration")
public class MemberAdvisoryCurationController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private MemberAdvisoryCurationService advisoryCurationService;

	@Autowired
	private AdvisoryService advisoryService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AdvisoryTagService advisoryTagService;

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	/**
	 * 添加精彩问答
	 * 
	 * @param session
	 * @param request
	 * @param memberAdvisoryCuration
	 * @param tagId
	 * @param jsonForImgs
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addAdvisoryCuration.json")
	@ResponseBody
	public Object addAdvisoryCuration(HttpSession session, HttpServletRequest request, MemberAdvisoryCuration memberAdvisoryCuration, String jsonForImgs)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 参数检查
			if (null == memberAdvisoryCuration)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}
			if (memberAdvisoryCuration.getAskContent() == null || memberAdvisoryCuration.getAskContent().equals(""))
			{
				result.setCode(-1);
				result.setMsg("题目不能为空");
				return result;
			}
			if (memberAdvisoryCuration.getAnswerContent() == null || memberAdvisoryCuration.getAnswerContent().equals(""))
			{
				result.setCode(-1);
				result.setMsg("回答不能为空");
				return result;
			}
			if (memberAdvisoryCuration.getAskTime() == null)
			{
				result.setCode(-1);
				result.setMsg("提问时间不能为空");
				return result;
			}
			if (null == memberAdvisoryCuration.getTagId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标签id不能为空");
				return result;
			}
			if (null == jsonForImgs || jsonForImgs.equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("图片不能为空");
				return result;
			}

			// 检查标签是否存在
			boolean isExist = advisoryCurationService.checkTagExist(memberAdvisoryCuration.getTagId());
			if (isExist == false)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标签不存在");
				return result;
			}

			// 添加精彩问答
			advisoryCurationService.insertMemberAdvisoryCuration(memberAdvisoryCuration, jsonForImgs);

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("操作成功");
			return result;
		} catch (Exception e)
		{
			log.error("系统错误: " + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}

	}

	/**
	 * 更新精彩问答
	 * 
	 * @param session
	 * @param request
	 * @param memberAdvisoryCuration
	 * @param tagId
	 * @param jsonForImgs
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateAdvisoryCuration.json")
	@ResponseBody
	public Object updateAdvisoryCuration(HttpSession session, HttpServletRequest request, MemberAdvisoryCuration memberAdvisoryCuration, String jsonForImgs)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			// 参数检查
			if (null == memberAdvisoryCuration)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}
			if (memberAdvisoryCuration.getAskId() == null)
			{
				result.setCode(-1);
				result.setMsg("记录id不能为空");
				return result;
			}
			if (memberAdvisoryCuration.getAskContent() == null || memberAdvisoryCuration.getAskContent().equals(""))
			{
				result.setCode(-1);
				result.setMsg("题目不能为空");
				return result;
			}
			if (memberAdvisoryCuration.getAnswerContent() == null || memberAdvisoryCuration.getAnswerContent().equals(""))
			{
				result.setCode(-1);
				result.setMsg("回答不能为空");
				return result;
			}
			if (memberAdvisoryCuration.getAskTime() == null)
			{
				result.setCode(-1);
				result.setMsg("提问时间不能为空");
				return result;
			}
			if (null == memberAdvisoryCuration.getTagId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标签id不能为空");
				return result;
			}
			if (null == jsonForImgs || jsonForImgs.equals(""))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("图片不能为空");
				return result;
			}

			// 检查标签是否存在
			boolean isExist = advisoryCurationService.checkTagExist(memberAdvisoryCuration.getTagId());
			if (isExist == false)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标签不存在");
				return result;
			}

			// 更新精彩问答
			int rtn = advisoryCurationService.updateMemberAdvisoryCuration(memberAdvisoryCuration, jsonForImgs);
			if (rtn == 0)
			{
				result.setCode(ResultEntity.SUCCESS);
				result.setMsg("操作成功");
			} else
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("更新失败");
			}

			return result;
		} catch (Exception e)
		{
			log.error("系统错误: " + e);
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}

	}

	/**
	 * 精彩问答列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/listAdvisoryCuration.json")
	@ResponseBody
	public Object listAdvisoryCuration(HttpServletRequest request, ModelMap modelMap, MemberAdvisoryCuration memberAdvisoryCuration, @RequestParam(value = "begin", required = false)Date begin, @RequestParam(value = "end", required = false)Date end)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (memberAdvisoryCuration == null)
		{
			result.setCode(-1);
			result.setMsg("传入数据为空");
			return result;
		}
		if (memberAdvisoryCuration.getPageIndex() == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (memberAdvisoryCuration.getPageSize() == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}

		if (memberAdvisoryCuration.getTagId() != null)
		{
			// 检查标签是否存在
			boolean isExist = advisoryCurationService.checkTagExist(memberAdvisoryCuration.getTagId());
			if (isExist == false)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标签不存在");
				return result;
			}
		}

		MemberAdvisoryCuration queryMemberAdvisoryCuration = new MemberAdvisoryCuration();
		if (memberAdvisoryCuration.getTagId() != null)
		{
			queryMemberAdvisoryCuration.setTagId(memberAdvisoryCuration.getTagId());
		}
		if (memberAdvisoryCuration.getPageIndex() != null)
		{
			queryMemberAdvisoryCuration.setPageIndex(memberAdvisoryCuration.getPageIndex());
		}
		if (memberAdvisoryCuration.getPageSize() != null)
		{
			queryMemberAdvisoryCuration.setPageSize(memberAdvisoryCuration.getPageSize());
		}

		// 查询集合
		try
		{
			Integer totalCount = advisoryCurationService.countCurationByTimeSlice(queryMemberAdvisoryCuration.getTagId(), begin, end);
			List<MemberAdvisoryCuration> list = advisoryCurationService.selectCurationByTimeSliceWithPage(queryMemberAdvisoryCuration.getTagId(), queryMemberAdvisoryCuration.getPageStartNum(),
					queryMemberAdvisoryCuration.getPageSize(), begin, end);
			List<WebMemberAdvisoryCurationDTO> advisoryCurationDtoList = new ArrayList<WebMemberAdvisoryCurationDTO>();
			for(MemberAdvisoryCuration advisoryCuration : list)
			{
				WebMemberAdvisoryCurationDTO webMemberAdvisoryCurationDTO = new WebMemberAdvisoryCurationDTO();
				BeanUtils.copyProperties(advisoryCuration, webMemberAdvisoryCurationDTO);
				AdvisoryTag advisoryTag = advisoryTagService.selectById(advisoryCuration.getTagId());
				if (advisoryTag != null)
				{
					webMemberAdvisoryCurationDTO.setTagName(advisoryTag.getPhrase());
				}
				advisoryCurationDtoList.add(webMemberAdvisoryCurationDTO);
			}
			result.put("list", advisoryCurationDtoList);
			result.put("count", totalCount);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 精彩问答详情
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getAdvisoryCurationDetail.json")
	@ResponseBody
	public Object getAdvisoryCurationDetail(HttpServletRequest request, ModelMap modelMap, Long askId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (askId == null)
		{
			result.setCode(-1);
			result.setMsg("问答id不能为空");
			return result;
		}

		// 查询详情
		try
		{
			ApiMemberAdvisoryCurationDTO memberAdvisoryCurationDTO = advisoryCurationService.getMemberAdvisoryCurationDetail(askId);
			result.put("obj", memberAdvisoryCurationDTO);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 问答列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/listAdvisory.json")
	@ResponseBody
	public Object listAdvisory(HttpServletRequest request, ModelMap modelMap, WebMemberAdvisoryVO webMemberAdvisoryVO, @RequestParam(value = "begin", required = false)Date begin, @RequestParam(value = "end", required = false)Date end)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (webMemberAdvisoryVO == null)
		{
			result.setCode(-1);
			result.setMsg("传入数据为空");
			return result;
		}
		if (webMemberAdvisoryVO.getPageIndex() == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (webMemberAdvisoryVO.getPageSize() == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}

		if (webMemberAdvisoryVO.getTagId() != null)
		{
			// 检查标签是否存在
			boolean isExist = advisoryCurationService.checkTagExist(webMemberAdvisoryVO.getTagId());
			if (isExist == false)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标签不存在");
				return result;
			}
		}

		MemberAdvisory queryMemberAdvisory = new MemberAdvisory();
		if (webMemberAdvisoryVO.getPageIndex() != null)
		{
			queryMemberAdvisory.setPageIndex(webMemberAdvisoryVO.getPageIndex());
		}
		if (webMemberAdvisoryVO.getPageSize() != null)
		{
			queryMemberAdvisory.setPageSize(webMemberAdvisoryVO.getPageSize());
		}

		// 查询集合
		Integer totalCount;
		List<WebMemberAdvisoryDTO> advisoryDTOs = new ArrayList<WebMemberAdvisoryDTO>();
		try
		{
			totalCount = advisoryService.countAdvisoryByTimeSlice(webMemberAdvisoryVO.getTagId(), begin, end);
			List<MemberAdvisory> advisories = advisoryService.obtainAdvisoryByTimeSliceWithPageOrderBy(webMemberAdvisoryVO.getTagId(), queryMemberAdvisory.getPageStartNum(),
					queryMemberAdvisory.getPageSize(), begin, end);
			for (MemberAdvisory advisory : advisories)
			{
				WebMemberAdvisoryDTO advisoryDTO = new WebMemberAdvisoryDTO();
				BeanUtils.copyProperties(advisory, advisoryDTO);
				
				//设置回答数
				long commentCoont = advisoryService.countComments(advisory.getAdvisoryId());
				advisoryDTO.setAnswerNum(commentCoont);
				
				//查询昵称
				Member member = memberService.selectMemberByMid(advisory.getMid());
				if(member != null){
				advisoryDTO.setAdvNickname(member.getNickname());
				advisoryDTO.setAdvThumbnail(member.getAvatarThumbnail());
				}
				//查询tag
				List<AdvisoryTag> advisoryTags = advisoryTagService.selectAdvisoryTagByAdvisoryId(advisory.getAdvisoryId());
				if(advisoryTags.size() > 0)
				{
					advisoryDTO.setTagId(advisoryTags.get(0).getTagId());
					advisoryDTO.setTagName(advisoryTags.get(0).getPhrase());
				}
				//查询详情
				MemberAdvisoryDetail detail = advisoryService.obtainDetailByAdvisoryId(advisory.getAdvisoryId());
				advisoryDTO.setDetail(detail.getDetail());
				
				// 获取评论条数
				Long commentCount = advisoryService.countComments(advisory.getAdvisoryId());
				advisoryDTO.setCommentCount(commentCount.intValue());
				
				advisoryDTOs.add(advisoryDTO);				
			}

		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.put("list", advisoryDTOs);
		result.put("count", totalCount);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 问答详情
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getAdvisoryDetail.json")
	@ResponseBody
	public Object getAdvisoryDetail(HttpServletRequest request, ModelMap modelMap, Long advisoryId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (advisoryId == null)
		{
			result.setCode(-1);
			result.setMsg("问答id不能为空");
			return result;
		}

		// 查询详情
		try
		{
			// 查询咨询
			MemberAdvisory memberAdvisory = advisoryService.obtainAdvisoryById(advisoryId);
			WebMemberAdvisoryDTO webMemberAdvisoryDTO = new WebMemberAdvisoryDTO();
			BeanUtils.copyProperties(memberAdvisory, webMemberAdvisoryDTO);

			// 查询会员
			Member member = memberService.selectMemberByMid(memberAdvisory.getMid());
			webMemberAdvisoryDTO.setAdvNickname(member.getNickname());
			webMemberAdvisoryDTO.setAdvThumbnail(member.getAvatarThumbnail());

			// 查询标签
			List<AdvisoryTag> tags = advisoryTagService.selectAdvisoryTagByAdvisoryId(memberAdvisory.getAdvisoryId());
			if (tags != null && tags.size() > 0)
			{
				webMemberAdvisoryDTO.setTagName(tags.get(0).getPhrase());
			}
			
			// 查询咨询回复列表
			List<MemberAdvisoryComment> commentList = advisoryService.obtainCommentsByAdvisoryId(advisoryId);
			List<WebMemberAdvisoryCommentDTO> comDtoList = new ArrayList<WebMemberAdvisoryCommentDTO>();
			for (MemberAdvisoryComment comment : commentList)
			{
				WebMemberAdvisoryCommentDTO webMemberAdvisoryCommentDTO = new WebMemberAdvisoryCommentDTO();
				BeanUtils.copyProperties(comment, webMemberAdvisoryCommentDTO);
				Member curMember = memberService.selectMemberByMid(comment.getMid());
				webMemberAdvisoryCommentDTO.setNickname(curMember.getNickname());
				webMemberAdvisoryCommentDTO.setTitle(curMember.getTitle());
				webMemberAdvisoryCommentDTO.setThumbnail(curMember.getAvatarThumbnail());
				
				comDtoList.add(webMemberAdvisoryCommentDTO);
			}

			result.put("obj", webMemberAdvisoryDTO);
			result.put("commentList", comDtoList);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 批量启用or禁用： 0启用，1禁用
	 * 
	 * @param session
	 * @param request
	 * @param ids
	 * @param isDel
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpSession session, HttpServletRequest request, @RequestParam("ids[]") Long[] ids, String isDel)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (ids == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (!isDel.equals("0") && !isDel.equals("1"))
		{
			result.setCode(-1);
			result.setMsg("状态错误：不允许的状态");
			return result;
		}

		try
		{
			if (isDel.equals("0"))
			{
				advisoryCurationService.enableAdvisoryCurationBulk(Arrays.asList(ids));
			} else if (isDel.equals("1"))
			{
				advisoryCurationService.disableAdvisoryCurationBulk(Arrays.asList(ids));
			}
		} catch (Exception e)
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
	
	/**
	 * 单个启用or禁用： 0启用，1禁用
	 * 
	 * @param session
	 * @param request
	 * @param id
	 * @param isDel
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changeStatusSingle.json")
	@ResponseBody
	public Object changeStatusSingle(HttpSession session, HttpServletRequest request, Long id, String isDel)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (id == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg("id不能为空");
			return result;
		}
		if (!isDel.equals("0") && !isDel.equals("1"))
		{
			result.setCode(-1);
			result.setMsg("状态错误：不允许的状态");
			return result;
		}

		try
		{
			List<Long> ids = new ArrayList<Long>();
			ids.add(id);
			if (isDel.equals("0"))
			{
				advisoryCurationService.enableAdvisoryCurationBulk(ids);
			} else if (isDel.equals("1"))
			{
				advisoryCurationService.disableAdvisoryCurationBulk(ids);
			}
		} catch (Exception e)
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
	
	/**
	 * 获取全部标签列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getTagList.json")
	@ResponseBody
	public Object getTagList(HttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();

		try
		{
			// 查询所有标签列表
			List<AdvisoryTag> tags = advisoryTagService.selectAllAdvisoryTagList();

			result.put("list", tags);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

}
