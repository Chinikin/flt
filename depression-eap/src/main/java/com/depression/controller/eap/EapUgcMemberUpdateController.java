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

import com.depression.dao.MemberUpdateCommentMapper;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberUpdate;
import com.depression.model.MemberUpdateComment;
import com.depression.model.MemberUpdateDetail;
import com.depression.model.MemberUpdateImgs;
import com.depression.model.web.dto.WebMemberUpdateCommentDTO;
import com.depression.model.web.dto.WebMemberUpdateDTO;
import com.depression.model.web.dto.WebMemberUpdateDetailDTO;
import com.depression.model.web.dto.WebMemberUpdateImgsDTO;
import com.depression.service.EapEnterpriseService;
import com.depression.service.HeartmateService;
import com.depression.service.MemberService;
import com.depression.service.MemberUpdateService;
import com.depression.service.Permission;
import com.depression.service.PunishmentService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/EapUgcMemberUpdate")
public class EapUgcMemberUpdateController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	HeartmateService heartmateService;
	@Autowired
	MemberService memberService;
	@Autowired
	PunishmentService punishmentService;
	@Autowired
	EapEnterpriseService eapEnterpriseService;
	@Autowired
	MemberUpdateCommentMapper memberUpdateCommentMapper;
	
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
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/searchUpdate.json")
	@ResponseBody
	@Permission("3")
	public Object searchUpdate(String words,Long eeId,Long mid, Date begin, Date end, 
			Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(eeId,pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		List<WebMemberUpdateDTO> memberUpdateDTOs = new ArrayList<WebMemberUpdateDTO>();
		Integer count;
		try{
			
			List<MemberUpdate> memberUpdates = heartmateService.searchUpdateInEap(words,eeId,mid, begin, end, pageIndex, pageSize);
			for(MemberUpdate mu : memberUpdates)
			{
				WebMemberUpdateDTO updateDTO = new WebMemberUpdateDTO();
				BeanUtils.copyProperties(mu, updateDTO);
				//查询昵称
				Member member = memberService.selectMemberByMid(mu.getMid());
				updateDTO.setNickname(member.getNickname());
				updateDTO.setThumbnail(member.getAvatar());
				
				//查询详情
				MemberUpdateDetail detail = heartmateService.obtainDetailByUpdateId(mu.getUpdateId());
				updateDTO.setDetail(detail.getDetail());
				
				//查询禁言天数
				updateDTO.setDisableMessageDays(punishmentService.obtainDisableMessageDays(mu.getMid()));
				
				//查询图片
				List<WebMemberUpdateImgsDTO> imgsDTOs = new ArrayList<WebMemberUpdateImgsDTO>();
				List<MemberUpdateImgs> updateImgs = heartmateService.obtainImgsByUpdateId(mu.getUpdateId());
				for(MemberUpdateImgs img : updateImgs)
				{
					WebMemberUpdateImgsDTO imgsDTO = new WebMemberUpdateImgsDTO();
					BeanUtils.copyProperties(img, imgsDTO);
					imgsDTO.setImgPathAbs(img.getImgPath());
					imgsDTO.setImgPreviewPathAbs(img.getImgPreviewPath());
					imgsDTOs.add(imgsDTO);
				}
				updateDTO.setImgsDTOs(imgsDTOs);
				
				// 获取评论条数
				Integer commentCount = heartmateService.countComments8UpdateId(mu.getUpdateId());
				updateDTO.setCommentCount(commentCount);				
				
				memberUpdateDTOs.add(updateDTO);
			}
			
			count = heartmateService.countSearchUpdateInEap(words,eeId,mid, begin, end);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("memberUpdateDTOs", memberUpdateDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取咨询详情
	 * @param updateId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainUpdateDetail.json")
	@ResponseBody
	@Permission("3")
	public Object obtainUpdateDetail(Long updateId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		WebMemberUpdateDetailDTO updateDetailDTO = new WebMemberUpdateDetailDTO();
		try{
			MemberUpdate update = heartmateService.obtainUpdateById(updateId);
			if(update == null)
			{
				result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
				return result;
			}
			
			BeanUtils.copyProperties(update, updateDetailDTO);
			//查询详情
			MemberUpdateDetail mad = new MemberUpdateDetail();
			mad.setUpdateId(updateId);
			MemberUpdateDetail detail = heartmateService.obtainDetailByUpdateId(updateId);
			updateDetailDTO.setDetail(detail.getDetail());
			//查询昵称
			Member member = memberService.selectMemberByMid(update.getMid());
			updateDetailDTO.setNickname(member.getNickname());
			updateDetailDTO.setThumbnail(member.getAvatar());
			//查询禁言天数
			updateDetailDTO.setDisableMessageDays(punishmentService.obtainDisableMessageDays(update.getMid()));
			//查询图片
			List<WebMemberUpdateImgsDTO> imgsDTOs = new ArrayList<WebMemberUpdateImgsDTO>();
			List<MemberUpdateImgs> updateImgs = heartmateService.obtainImgsByUpdateId(updateId);
			for(MemberUpdateImgs img : updateImgs)
			{
				WebMemberUpdateImgsDTO imgsDTO = new WebMemberUpdateImgsDTO();
				BeanUtils.copyProperties(img, imgsDTO);
				imgsDTO.setImgPathAbs(img.getImgPath());
				imgsDTO.setImgPreviewPathAbs(img.getImgPreviewPath());
				imgsDTOs.add(imgsDTO);
			}
			updateDetailDTO.setImgsDTOs(imgsDTOs);
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("updateDetailDTO", updateDetailDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取评论列表
	 * @param updateId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainUpdateComments.json")
	@ResponseBody
	@Permission("3")
	public Object obtainUpdateComments(Long updateId, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<WebMemberUpdateCommentDTO> commentDTOs = 
				new ArrayList<WebMemberUpdateCommentDTO>();
		Integer count;
		try{
			List<MemberUpdateComment> comments = 
					heartmateService.obtainComments8UpdateId3Page(updateId, pageIndex, pageSize);
			for(MemberUpdateComment comment : comments)
			{
				WebMemberUpdateCommentDTO commentDTO = new WebMemberUpdateCommentDTO();
				BeanUtils.copyProperties(comment, commentDTO);
				//查询昵称
				Member member = memberService.selectMemberByMid(comment.getMid());
				commentDTO.setNickname(member.getNickname());
				commentDTO.setThumbnail(member.getAvatar());
				
				//查询禁言天数
				commentDTO.setDisableMessageDays(punishmentService.obtainDisableMessageDays(comment.getMid()));
				commentDTOs.add(commentDTO);
			}
			
			count = heartmateService.countComments8UpdateId(updateId);
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
	 * 删除咨询
	 * @param updateId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberUpdate.json")
	@ResponseBody
	@Permission("3")
	public Object deleteMemberUpdate(Long updateId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			heartmateService.deleteUpdate(updateId);
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
	 * @param updateImgId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberUpdateImg.json")
	@ResponseBody
	@Permission("3")
	public Object deleteMemberUpdateImg(Long updateImgId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(updateImgId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			heartmateService.deleteImg(updateImgId);
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
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberUpdateComment.json")
	@ResponseBody
	@Permission("3")
	public Object deleteMemberUpdateComment(Long commentId,Long eeId)
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
			MemberUpdateComment mc=memberUpdateCommentMapper.selectByPrimaryKey(commentId);
			Member psy = memberService.selectMemberByMid(mc.getMid());
			if(!ids.contains(mc.getMid()) && !eeId.equals(psy.getpSource())){
				//不是该企业用户的评论不能删除
				result.setCode(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getCode());
				result.setMsg(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getMessage());
			}
			heartmateService.deleteComment(commentId);
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
