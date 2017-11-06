package com.depression.controller.eap;

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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.dao.MemberAdvisoryCommentMapper;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdvisoryTag;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberAdvisoryDetail;
import com.depression.model.MemberAdvisoryImgs;
import com.depression.model.web.dto.WebMemberAdvisoryCommentDTO;
import com.depression.model.web.dto.WebMemberAdvisoryDTO;
import com.depression.model.web.dto.WebMemberAdvisoryDetailDTO;
import com.depression.model.web.dto.WebMemberAdvisoryImgsDTO;
import com.depression.service.AdvisoryTagService;
import com.depression.service.AdvisoryService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberService;
import com.depression.service.Permission;
import com.depression.service.PunishmentService;
import com.depression.utils.PropertyUtils;
import com.sun.tools.javac.comp.Enter;

@Controller
@RequestMapping("/EapUgcMemberAdvisory")
public class EapUgcMemberAdvisoryController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	AdvisoryService advisoryService;
	@Autowired
	MemberService memberService;
	@Autowired
	AdvisoryTagService advisoryTagService;
	@Autowired
	PunishmentService punishmentService;
	@Autowired
	EapEnterpriseService eapEnterpriseService;
	@Autowired
	MemberAdvisoryCommentMapper memberAdvisoryCommentMapper;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	/**
	 * 搜索咨询
	 * @param words 模糊搜索词
	 * @param begin 开始时间
	 * @param end	结束时间
	 * @param mid 用户id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/searchAdvisory.json")
	@ResponseBody
	@Permission("3")
	public Object searchAdvisory(String words,Long mid, Date begin, Date end, 
			Integer pageIndex, Integer pageSize,Long eeId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(eeId,pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		List<WebMemberAdvisoryDTO> memberAdvisoryDTOs = new ArrayList<WebMemberAdvisoryDTO>();
		Integer count;
		try{
			//List<Long> ids=eapEnterpriseService.getRegedEmployee(eeId);
			List<MemberAdvisory> memberAdvisories = 
					advisoryService.searchAdvisoryInEap(words,eeId,mid, begin, end, pageIndex, pageSize);
			for(MemberAdvisory ma : memberAdvisories)
			{
				WebMemberAdvisoryDTO advisoryDTO = new WebMemberAdvisoryDTO();
				BeanUtils.copyProperties(ma, advisoryDTO);
				//查询昵称
				Member member = memberService.selectMemberByMid(ma.getMid());
				advisoryDTO.setAdvNickname(member.getNickname());
				advisoryDTO.setAdvThumbnail(member.getAvatar());
				//查询tag
				List<AdvisoryTag> advisoryTags = advisoryTagService.selectAdvisoryTagByAdvisoryId(ma.getAdvisoryId());
				if(advisoryTags.size() > 0)
				{
					advisoryDTO.setTagId(advisoryTags.get(0).getTagId());
					advisoryDTO.setTagName(advisoryTags.get(0).getPhrase());
				}
				
				//禁言天数
				advisoryDTO.setDisableMessageDays(punishmentService.obtainDisableMessageDays(ma.getMid()));
				
				//查询详情
				MemberAdvisoryDetail detail = advisoryService.obtainDetailByAdvisoryId(ma.getAdvisoryId());
				advisoryDTO.setDetail(detail.getDetail());
				
				//查询图片
				List<WebMemberAdvisoryImgsDTO> imgsDTOs = new ArrayList<WebMemberAdvisoryImgsDTO>();
				List<MemberAdvisoryImgs> advisoryImgs = advisoryService.obtainImgsByAdvisoryId(ma.getAdvisoryId());;;
				for(MemberAdvisoryImgs img : advisoryImgs)
				{
					WebMemberAdvisoryImgsDTO imgsDTO = new WebMemberAdvisoryImgsDTO();
					BeanUtils.copyProperties(img, imgsDTO);
					imgsDTO.setImgPathAbs(img.getImgPath());
					imgsDTO.setImgPreviewPathAbs(img.getImgPreviewPath());
					imgsDTOs.add(imgsDTO);
				}
				advisoryDTO.setImgsDTOs(imgsDTOs);
				
				// 获取评论条数
				Long commentCount = advisoryService.countComments(ma.getAdvisoryId());
				advisoryDTO.setCommentCount(commentCount.intValue());
				
				memberAdvisoryDTOs.add(advisoryDTO);
			}
			
			count = advisoryService.countSearchAdvisory(words,eeId,mid, begin, end);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("memberAdvisoryDTOs", memberAdvisoryDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取咨询详情
	 * @param advisoryId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainAdvisoryDetail.json")
	@ResponseBody
	@Permission("3")
	public Object obtainAdvisoryDetail(Long advisoryId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		WebMemberAdvisoryDetailDTO advisoryDetailDTO = new WebMemberAdvisoryDetailDTO();
		try{
			MemberAdvisory advisory = advisoryService.obtainAdvisoryById(advisoryId);
			if(advisory == null)
			{
				result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
				return result;
			}
			BeanUtils.copyProperties(advisory, advisoryDetailDTO);
			//查询详情
			MemberAdvisoryDetail detail = advisoryService.obtainDetailByAdvisoryId(advisoryId);			
			advisoryDetailDTO.setDetail(detail.getDetail());
			//查询昵称
			Member member = memberService.selectMemberByMid(advisory.getMid());
			advisoryDetailDTO.setAdvNickname(member.getNickname());
			advisoryDetailDTO.setAdvThumbnail(member.getAvatar());
			//禁言天数
			advisoryDetailDTO.setDisableMessageDays(punishmentService.obtainDisableMessageDays(advisory.getMid()));
			//查询tag
			List<AdvisoryTag> advisoryTags = advisoryTagService.selectAdvisoryTagByAdvisoryId(advisory.getAdvisoryId());
			if(advisoryTags.size() > 0)
			{
				advisoryDetailDTO.setTagId(advisoryTags.get(0).getTagId());
				advisoryDetailDTO.setTagName(advisoryTags.get(0).getPhrase());
			}
			//查询图片
			List<WebMemberAdvisoryImgsDTO> imgsDTOs = new ArrayList<WebMemberAdvisoryImgsDTO>();
			List<MemberAdvisoryImgs> advisoryImgs = advisoryService.obtainImgsByAdvisoryId(advisoryId);;
			for(MemberAdvisoryImgs img : advisoryImgs)
			{
				WebMemberAdvisoryImgsDTO imgsDTO = new WebMemberAdvisoryImgsDTO();
				BeanUtils.copyProperties(img, imgsDTO);
				imgsDTO.setImgPathAbs(img.getImgPath());
				imgsDTO.setImgPreviewPathAbs(img.getImgPreviewPath());
				imgsDTOs.add(imgsDTO);
			}
			advisoryDetailDTO.setImgsDTOs(imgsDTOs);
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("advisoryDetailDTO", advisoryDetailDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取评论列表
	 * @param advisoryId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainAdvisoryComments.json")
	@ResponseBody
	@Permission("3")
	public Object obtainAdvisoryComments(Long advisoryId, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryId, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<WebMemberAdvisoryCommentDTO> commentDTOs = 
				new ArrayList<WebMemberAdvisoryCommentDTO>();
		Long count;
		try{
			List<MemberAdvisoryComment> comments = advisoryService.obtainCommentsWithPage(advisoryId, pageIndex, pageSize);
			for(MemberAdvisoryComment comment : comments)
			{
				WebMemberAdvisoryCommentDTO commentDTO = new WebMemberAdvisoryCommentDTO();
				BeanUtils.copyProperties(comment, commentDTO);
				//查询昵称
				Member member = memberService.selectMemberByMid(comment.getMid());
				commentDTO.setNickname(member.getNickname());
				commentDTO.setThumbnail(member.getAvatar());
				//查询禁言天数
				commentDTO.setDisableMessageDays(punishmentService.obtainDisableMessageDays(comment.getMid()));
				commentDTOs.add(commentDTO);
			}
			
			count = advisoryService.countComments(advisoryId);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("commentDTOs", commentDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 修改咨询被推荐状态
	 * @param advisoryId
	 * @param isRecommended
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/changeAdvisoryRecommendStatus.json")
	@ResponseBody
	@Permission("3")
	public Object changeAdvisoryRecommendStatus(Long advisoryId, Byte isRecommended)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			advisoryService.changeAdvisoryRecommendStatus(advisoryId, isRecommended);
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
	
	/**
	 * 删除咨询
	 * @param advisoryId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberAdvisory.json")
	@ResponseBody
	@Permission("3")
	public Object deleteMemberAdvisory(Long advisoryId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			advisoryService.deleteAdvisory(advisoryId);
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
	
	/**
	 * 删除咨询中图片
	 * @param advisoryImgId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberAdvisoryImg.json")
	@ResponseBody
	@Permission("3")
	public Object deleteMemberAdvisoryImg(Long advisoryImgId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryImgId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			advisoryService.deleteImgs(advisoryImgId);
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
	
	/**
	 * 删除咨询评论
	 * @param commentId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberAdvisoryComment.json")
	@ResponseBody
	@Permission("3")
	public Object deleteMemberAdvisoryComment(Long commentId ,Long eeId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(commentId,eeId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		
		
		try{
			List<Long> ids=eapEnterpriseService.getRegedEmployee(eeId);
			MemberAdvisoryComment mac=memberAdvisoryCommentMapper.selectByPrimaryKey(commentId);
			Member psy = memberService.selectMemberByMid(mac.getMid());
			if(!ids.contains(mac.getMid()) && !eeId.equals(psy.getpSource())){
				//不是该企业用户的评论不能删除
				result.setCode(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getCode());
				result.setMsg(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getMessage());
				return result;
			}
			
			advisoryService.deleteComment(commentId);
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
