package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.MemberTag;
import com.depression.model.api.dto.ApiMemberTagDTO;
import com.depression.model.api.dto.ApiSortModeDTO;
import com.depression.service.MemberTagService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/MemberTag")
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
			psychoTags = memberTagService.getTagList4Psycho(pageIndex, pageSize, 0);
			count = memberTagService.getTagCount4Psycho(0);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		List<ApiMemberTagDTO> psychoTagDTOs = new ArrayList<ApiMemberTagDTO>();
		for(MemberTag mt : psychoTags)
		{
			ApiMemberTagDTO tagDTO = new ApiMemberTagDTO();
			BeanUtils.copyProperties(mt, tagDTO);
			psychoTagDTOs.add(tagDTO);
		}
		
		List<ApiSortModeDTO> modeDTOs = new ArrayList<ApiSortModeDTO>();
		modeDTOs.add(new ApiSortModeDTO(1, "最多咨询"));		
		modeDTOs.add(new ApiSortModeDTO(2, "最多感谢 "));		
		modeDTOs.add(new ApiSortModeDTO(3, "在线状态 "));		
		modeDTOs.add(new ApiSortModeDTO(4, "价格从低到高 "));	
		modeDTOs.add(new ApiSortModeDTO(5, "价格从高到低 "));	
		
		result.put("modeDTOs", modeDTOs);
		result.put("psychoTags", psychoTagDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

}
