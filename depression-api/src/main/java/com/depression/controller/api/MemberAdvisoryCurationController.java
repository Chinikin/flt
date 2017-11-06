package com.depression.controller.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdvisoryTag;
import com.depression.model.MemberAdvisoryCuration;
import com.depression.model.MemberAdvisoryCurationImgs;
import com.depression.model.api.dto.ApiMemberAdvisoryCurationDTO;
import com.depression.model.api.dto.ApiMemberAdvisoryCurationImgsDTO;
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
	private MemberAdvisoryCurationService memberAdvisoryCurationService;

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
	public Object listAdvisoryCuration(HttpServletRequest request, ModelMap modelMap, MemberAdvisoryCuration memberAdvisoryCuration)
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
			boolean isExist = memberAdvisoryCurationService.checkTagExist(memberAdvisoryCuration.getTagId());
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
			Integer totalCount = memberAdvisoryCurationService.countCurationByTagId(queryMemberAdvisoryCuration.getTagId());
			List<MemberAdvisoryCuration> list = memberAdvisoryCurationService.selectCurationByTagIdWithPage(queryMemberAdvisoryCuration.getTagId(), queryMemberAdvisoryCuration.getPageStartNum(),
					queryMemberAdvisoryCuration.getPageSize());
			List<ApiMemberAdvisoryCurationDTO> AdvisoryCurationDtoList = new ArrayList<ApiMemberAdvisoryCurationDTO>();
			for (MemberAdvisoryCuration advisoryCuration : list)
			{
				ApiMemberAdvisoryCurationDTO apiMemberAdvisoryCurationDTO = new ApiMemberAdvisoryCurationDTO();
				BeanUtils.copyProperties(advisoryCuration, apiMemberAdvisoryCurationDTO);
				AdvisoryTag advisoryTag = advisoryTagService.selectById(advisoryCuration.getTagId());
				if (advisoryTag != null)
				{
					apiMemberAdvisoryCurationDTO.setTagName(advisoryTag.getPhrase());
				}
				
				List<MemberAdvisoryCurationImgs> imgList = memberAdvisoryCurationService.getAdvisoryCurationImgList(advisoryCuration.getAskId());
				List<ApiMemberAdvisoryCurationImgsDTO> imgListDTO = new ArrayList<ApiMemberAdvisoryCurationImgsDTO>();
				for (MemberAdvisoryCurationImgs memberAdvisoryCurationImgs : imgList)
				{
					ApiMemberAdvisoryCurationImgsDTO apiMemberAdvisoryCurationImgsDTO = new ApiMemberAdvisoryCurationImgsDTO();
					BeanUtils.copyProperties(memberAdvisoryCurationImgs, apiMemberAdvisoryCurationImgsDTO);
					imgListDTO.add(apiMemberAdvisoryCurationImgsDTO);
				}
				apiMemberAdvisoryCurationDTO.setImgList(imgListDTO);
				
				AdvisoryCurationDtoList.add(apiMemberAdvisoryCurationDTO);
			}
			result.put("list", AdvisoryCurationDtoList);
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
			ApiMemberAdvisoryCurationDTO memberAdvisoryCurationDTO = memberAdvisoryCurationService.getMemberAdvisoryCurationDetail(askId);
			List<MemberAdvisoryCurationImgs> imgList = memberAdvisoryCurationService.getAdvisoryCurationImgList(memberAdvisoryCurationDTO.getAskId());
			List<ApiMemberAdvisoryCurationImgsDTO> imgListDTO = new ArrayList<ApiMemberAdvisoryCurationImgsDTO>();
			for (MemberAdvisoryCurationImgs memberAdvisoryCurationImgs : imgList)
			{
				ApiMemberAdvisoryCurationImgsDTO apiMemberAdvisoryCurationImgsDTO = new ApiMemberAdvisoryCurationImgsDTO();
				BeanUtils.copyProperties(memberAdvisoryCurationImgs, apiMemberAdvisoryCurationImgsDTO);
				imgListDTO.add(apiMemberAdvisoryCurationImgsDTO);
			}
			memberAdvisoryCurationDTO.setImgList(imgListDTO);
			result.put("obj", memberAdvisoryCurationDTO);
			
			// 更新查看次数
			MemberAdvisoryCuration memberAdvisoryCuration = memberAdvisoryCurationService.selectByPrimaryKey(askId);
			if (memberAdvisoryCuration != null)
			{
				if (memberAdvisoryCuration.getViewNum() == null || memberAdvisoryCuration.getViewNum().intValue() == 0)
				{
					memberAdvisoryCuration.setViewNum(1L);
				} else 
				{
					memberAdvisoryCuration.setViewNum(memberAdvisoryCuration.getViewNum() + 1);
				}
			}
			memberAdvisoryCurationService.updateByPrimaryKeySelective(memberAdvisoryCuration);
			
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
	
	/**
	 * 更新问答详情
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updAdvisoryCurationPraise.json")
	@ResponseBody
	public Object updAdvisoryCurationPraise(HttpServletRequest request, ModelMap modelMap, Long askId)
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
		Long praiseNum = 0L;
		try
		{
			// 更新查看次数
			MemberAdvisoryCuration memberAdvisoryCuration = memberAdvisoryCurationService.selectByPrimaryKey(askId);
			if (memberAdvisoryCuration != null)
			{
				if (memberAdvisoryCuration.getPraiseNum() == null || memberAdvisoryCuration.getPraiseNum().intValue() == 0)
				{
					memberAdvisoryCuration.setPraiseNum(1L);
				} else 
				{
					memberAdvisoryCuration.setPraiseNum(memberAdvisoryCuration.getPraiseNum() + 1);
				}
				praiseNum = memberAdvisoryCuration.getPraiseNum();
			}
			memberAdvisoryCurationService.updateByPrimaryKeySelective(memberAdvisoryCuration);
			
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("praiseNum", praiseNum);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

}
