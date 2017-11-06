package com.depression.controller.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.MemberTag;
import com.depression.model.web.dto.WebMemberTagDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.model.web.vo.WebMemberTagVO;
import com.depression.service.MemberTagService;
import com.depression.utils.PropertyUtils;

@RequestMapping("/MemberTag")
@Controller
public class MemberTagController
{
    Logger log = Logger.getLogger(this.getClass());
	@Autowired
	MemberTagService memberTagService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPsychoTagList.json")
	@ResponseBody
	public Object obtainPsychoTagList(HttpSession session, HttpServletRequest request,
			Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<MemberTag> psychoTags = null;
		Integer count = null;
		try{
			psychoTags = memberTagService.getTagList4Psycho(pageIndex, pageSize, null);
			count = memberTagService.getTagCount4Psycho(null);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		List<WebMemberTagDTO> psychoTagDTOs = new ArrayList<WebMemberTagDTO>();
		for(MemberTag mt : psychoTags)
		{
			WebMemberTagDTO tagDTO = new WebMemberTagDTO();
			BeanUtils.copyProperties(mt, tagDTO);
			psychoTagDTOs.add(tagDTO);
		}
		
		result.put("psychoTags", psychoTagDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainPsychoTag.json")
	@ResponseBody
	public Object obtainPsychoTag(HttpSession session, HttpServletRequest request,
			Long tagId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				tagId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		MemberTag psychoTag = null;
		try{
			psychoTag = memberTagService.selectByPrimaryKey(tagId);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		WebMemberTagDTO tagDTO = new WebMemberTagDTO();
		BeanUtils.copyProperties(psychoTag, tagDTO);

		result.put("psychoTag", psychoTag);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/newPsychoTag.json")
	@ResponseBody
	public Object newPsychoTag(HttpSession session, HttpServletRequest request,
			WebMemberTagVO psychoTagVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				psychoTagVO,
				psychoTagVO.getPhrase()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			memberTagService.insertNewTag4Psycho(psychoTagVO.getPhrase());
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/updatePsychoTag.json")
	@ResponseBody
	public Object updatePsychoTag(HttpSession session, HttpServletRequest request,
			WebMemberTagVO psychoTagVO)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				psychoTagVO,
				psychoTagVO.getTagId()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		MemberTag memberTag = new MemberTag();
		BeanUtils.copyProperties(psychoTagVO, memberTag);
		
		try{
			memberTagService.updateTag(memberTag);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/deletePsychoTag.json")
	@ResponseBody
	public Object deletePsychoTagBulk(HttpSession session, HttpServletRequest request,
			WebIdsVO tagIds)
	{
		ResultEntity result = new ResultEntity();
		if(tagIds==null){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			memberTagService.deleteTagBulk(tagIds.getIds());
		}catch(Exception e){
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
