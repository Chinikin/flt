package com.depression.controller.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdvisoryTag;
import com.depression.model.web.dto.WebAdvisoryTagDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.service.AdvisoryTagService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/AdvisoryTag")
public class AdvisoryTagController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	AdvisoryTagService advisoryTagService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/newAdvisoryTag.json")
	@ResponseBody
	public Object newAdvisoryTag(String phrase, Integer orderNo)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(phrase, orderNo))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			advisoryTagService.newAdvisoryTag(phrase, orderNo);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/updateAdvisoryTag.json")
	@ResponseBody
	public Object updateAdvisoryTag(Long tagId, String phrase, Integer orderNo)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(tagId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Integer ret = advisoryTagService.updateAdvisoryTag(tagId, phrase, orderNo);
			if(ret == 0)
			{
				result.setCode(ErrorCode.ERROR_RECORD_INUNIQUE.getCode());
				result.setMsg(ErrorCode.ERROR_RECORD_INUNIQUE.getMessage());
				return result;
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/enableAdvisoryTag.json")
	@ResponseBody
	public Object enableAdvisoryTag(WebIdsVO Tagids, Integer isEnable)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(Tagids, isEnable))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			advisoryTagService.enable(Tagids.getIds(), isEnable);
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainAdvisoryTagList.json")
	@ResponseBody
	public Object obtainAdvisoryTagList(Integer pageIndex, Integer pageSize)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		List<WebAdvisoryTagDTO> advisoryTagDTOs = new ArrayList<WebAdvisoryTagDTO>();
		Integer count=0;
		try{
			List<AdvisoryTag> tags = advisoryTagService.selectWithPageOrder(pageIndex, pageSize, null);
			count = advisoryTagService.countSelect(null);
			
			for(AdvisoryTag tag : tags)
			{
				WebAdvisoryTagDTO tagDTO = new WebAdvisoryTagDTO();
				BeanUtils.copyProperties(tag, tagDTO);
				advisoryTagDTOs.add(tagDTO);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		result.put("advisoryTags", advisoryTagDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/obtainAdvisoryTag.json")
	@ResponseBody
	public Object obtainAdvisoryTag(Long tagId)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(tagId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		WebAdvisoryTagDTO advisoryTagDTO = new WebAdvisoryTagDTO();
		try{
			AdvisoryTag tag = advisoryTagService.selectById(tagId);
			
			BeanUtils.copyProperties(tag, advisoryTagDTO);

		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		result.put("advisoryTag", advisoryTagDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
