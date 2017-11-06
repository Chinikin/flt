package com.depression.controller.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdvisoryTag;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberAdvisoryImgs;
import com.depression.model.MemberUpdate;
import com.depression.model.MemberUpdateComment;
import com.depression.model.MemberUpdateDetail;
import com.depression.model.MemberUpdateImgs;
import com.depression.model.UnreadComment;
import com.depression.model.api.dto.ApiAdvisoryCommentDTO;
import com.depression.model.api.dto.ApiAdvisoryDTO;
import com.depression.model.api.dto.ApiAdvisoryImgsDTO;
import com.depression.model.api.dto.ApiAdvisoryTagDTO;
import com.depression.model.api.dto.ApiMemberEssentialDTO;
import com.depression.model.api.dto.ApiUpdateCommentDTO;
import com.depression.model.api.dto.ApiUpdateDTO;
import com.depression.model.api.dto.ApiUpdateImgsDTO;
import com.depression.push.CustomMsgType;
import com.depression.service.EapService;
import com.depression.service.HeartmateService;
import com.depression.service.MemberService;
import com.depression.service.PsychoInfoService;
import com.depression.service.PushService;
import com.depression.service.UnreadCommentService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/Heartmate")
public class HeartmateController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	HeartmateService heartmateService;
	@Autowired
	MemberService memberService;
	@Autowired	
	PsychoInfoService psychoInfoService;
	@Autowired		
	EapService eapService;
	@Autowired			
	UnreadCommentService unreadCommentService;
	@Autowired		
	PushService pushService;
	
	/**
	 * 发布心友圈动态
	 * @param mid
	 * @param content
	 * @param writeLocation
	 * @param imgsJsn
	 * @param isAnony
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/publishUpdate.json")
	@ResponseBody
	public Object publishUpdateV1(Long mid, String content, String writeLocation, String imgsJsn, Byte isAnony,Long releaseFrom)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, content, isAnony))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		Long updateId;
		try{
			// 内容摘要
			String digest = null;
			if(content.length() > 190){
				digest = content.substring(0, 190) + "...";
			}else{
				digest = content;
			}
			
			if(releaseFrom == null){
				releaseFrom = 0L;
			}
			
			MemberUpdate update = new MemberUpdate();
			update.setReleaseFrom(releaseFrom);
			update.setMid(mid);
			update.setContent(digest);
			update.setWriteLocation(writeLocation);
			update.setIsAnony(isAnony);
			heartmateService.createUpdate(update);
			
			updateId = update.getUpdateId();
			
			//保存到详情
			MemberUpdateDetail detail = new MemberUpdateDetail();
			detail.setUpdateId(updateId);
			detail.setDetail(content);
			heartmateService.createDetail(detail);
			
			// 存在图片文件
			if (null != imgsJsn)
			{
				try{
					List<String> imgs = JSON.parseArray(imgsJsn, String.class);
					for(String img : imgs)
					{
						MemberUpdateImgs muimgs = new MemberUpdateImgs();
						muimgs.setImgPath(img);
						muimgs.setImgPreviewPath(img);
						muimgs.setUpdateId(updateId);
						heartmateService.createImg(muimgs);
					}
				}catch(Exception e)
				{
					
				}	
			}
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
			
		result.put("updateId", updateId);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	/**
	 * 计算年龄
	 * @param birthday
	 * @return
	 */
	private Integer calcAge(Date birthday)
	{
		if(birthday == null)
		{//未设置生日，返回 0岁
			return 0;
		}
		Calendar cal =  Calendar.getInstance();
		if(cal.getTime().before(birthday))
		{//生日在未来，返回0岁
			return 0;
		}
        
		int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        
        int age = yearNow - yearBirth;
        
        if(monthNow == monthBirth) 
        {
            if (dayOfMonthNow < dayOfMonthBirth) 
            {//月相等，天小于
                age--;
            } 
        }else if (monthNow < monthBirth)
        {//月小于
        	age--;
        }
        
        return age;
	}
	
	/**
	 * 获取会员必要信息
	 * @param mid  会员id
	 * @param vid  查看者id
	 * @param isAnony 咨询是否匿名 0 匿名 1不匿名
	 * @return
	 */
	private ApiMemberEssentialDTO packMemberEssentialDTO(Long mid, Long vid, Byte isAnony)
	{
		try{
			ApiMemberEssentialDTO meDTO = new ApiMemberEssentialDTO();
			
			Member member = memberService.selectMemberByMid(mid);
			BeanUtils.copyProperties(member, meDTO);
			meDTO.setIsAnony(isAnony);
			meDTO.setAge(calcAge(member.getBirthday()));
			if(member.getUserType() == 2)
			{//咨询师
				//执照名称
				if(member.getLtid()!=null)
		        {
		        	String licenseName =
		        			psychoInfoService.getLicenseTypeByPrimaryKey(member.getLtid()).getLicenseName();
		        	meDTO.setLicenseName(licenseName);
		        }
				//EAP信息
				if(vid != null)
				{
					meDTO.setEapServiceStatus(eapService.isEmployeeServedByPsycho(vid, mid));
				}
			}
			if(isAnony == 0 && !mid.equals(vid))
			{//咨询匿名且非本人
				Integer nickFinal = (mid.intValue() + 1234) % 10000;

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

				meDTO.setNickname(city + "匿名用户" + nickFinal);
				meDTO.setAvatar(null);
			}
			
			return meDTO;
		}catch(Exception e)
		{//用户被删除或者丢失
			ApiMemberEssentialDTO meDTO = new ApiMemberEssentialDTO();
			meDTO.setNickname("匿名用户");
			return meDTO;
		}
	}
	
	private void packUpdateDTO(MemberUpdate update, ApiUpdateDTO updateDTO, Long vid)
	{
		BeanUtils.copyProperties(update, updateDTO);
		
		updateDTO.setCreateTimestamp(update.getCreateTime().getTime());
		//imgs
		List<MemberUpdateImgs> imgs = heartmateService.obtainImgsByUpdateId(update.getUpdateId());
		List<ApiUpdateImgsDTO> imgsDTOs = new ArrayList<ApiUpdateImgsDTO>();
		for(MemberUpdateImgs img : imgs)
		{
			ApiUpdateImgsDTO imgsDTO = new ApiUpdateImgsDTO();
			BeanUtils.copyProperties(img, imgsDTO);
			
			imgsDTOs.add(imgsDTO);
		}
		updateDTO.setImgs(imgsDTOs);
		//author
		ApiMemberEssentialDTO meDTO = packMemberEssentialDTO(update.getMid(), vid, update.getIsAnony());
		updateDTO.setAuthor(meDTO);
		
		//respond count
		updateDTO.setRespondCount(heartmateService.countComments8UpdateId(update.getUpdateId()));
		
		//isEmbrace
		if(vid == null)
		{
			updateDTO.setIsEmbraced((byte) 0);
		}else
		{
			updateDTO.setIsEmbraced((byte) heartmateService.isEmbracedUpdate(update.getUpdateId(), vid));
		}
	}
	
	/**
	 * 封装评论数据
	 * @param comment 评论
	 * @param vid 查看者id
	 * @param isAnony 咨询是否匿名 0 匿名 1不匿名
	 * @return
	 */
	private ApiUpdateCommentDTO packUpdateCommentDTO(MemberUpdateComment comment, Long vid)
	{
		ApiUpdateCommentDTO commentDTO = new ApiUpdateCommentDTO();
		BeanUtils.copyProperties(comment, commentDTO);
		commentDTO.setCommentTimestamp(comment.getCommentTime().getTime());
		//author
		ApiMemberEssentialDTO cmeDTO = packMemberEssentialDTO(comment.getMid(), vid, comment.getIsAnony());
		commentDTO.setAuthor(cmeDTO);
				
		return commentDTO;
	}
	
	/**
	 * 搜索心友圈动态
	 * @param type 1 心友圈 2 我关注的 3匿名动态
	 * @param pageIndex
	 * @param pageSize
	 * @param vid 查看者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/searchUpdateList.json")
	@ResponseBody
	public Object searchUpdateListV1(Byte type, Integer pageIndex, Integer pageSize, Long vid,Long releaseFrom)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(type, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(releaseFrom == null){
			releaseFrom = 0L;
		}
		
		List<ApiUpdateDTO> updateDTOs = new ArrayList<ApiUpdateDTO>();
		Integer count;
		try{
			List<MemberUpdate> updates;
			if(type == 2)
			{
				updates = heartmateService.obtainUpdateConcerned(vid, pageIndex, pageSize,releaseFrom);
				count = heartmateService.countUpdateConcerned(vid,releaseFrom);
			}else if(type == 3)
			{
				updates = heartmateService.obtainUpdate3Page0TmDesc((byte) 0, null, pageIndex, pageSize,releaseFrom);
				count = heartmateService.countUpdate((byte) 0, null,releaseFrom);
			}else
			{
				updates = heartmateService.obtainUpdate3Page0TmDesc(null, null, pageIndex, pageSize,releaseFrom);
				count = heartmateService.countUpdate(null, null,releaseFrom);
			}
			
			for(MemberUpdate update : updates)
			{
				ApiUpdateDTO updateDTO = new ApiUpdateDTO();
				packUpdateDTO(update, updateDTO, vid);
				
				//chosen comment
				MemberUpdateComment comment = heartmateService.obtainCommentNewest8UpdateId(update.getUpdateId());
				if(comment != null)
				{
					ApiUpdateCommentDTO commentDTO = packUpdateCommentDTO(comment, vid);
					
					MemberUpdateComment pcmnt = heartmateService.obtainCommentById(comment.getParentId());
					if(pcmnt != null)
					{
						ApiUpdateCommentDTO pcmntDTO = packUpdateCommentDTO(pcmnt, vid);
						commentDTO.setParent(pcmntDTO);
					}
					updateDTO.setChosenComment(commentDTO);
				}
				
				updateDTOs.add(updateDTO);
			}
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("updateDTOs", updateDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取用户动态列表，查看者是自己时包含匿名动态
	 * @param pageIndex
	 * @param pageSize
	 * @param mid
	 * @param vid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainUpdateListForUser.json")
	@ResponseBody
	public Object obtainUpdateListForUserV1(Integer pageIndex, Integer pageSize, Long mid, Long vid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiUpdateDTO> updateDTOs = new ArrayList<ApiUpdateDTO>();
		Integer count;
		try{
			List<MemberUpdate> updates;
			
			Byte isAnony = mid.equals(vid)? null : (byte)1;

			updates = heartmateService.obtainUpdate3Page0TmDesc(isAnony, mid, pageIndex, pageSize,null);
			count = heartmateService.countUpdate(isAnony, mid,null);

			
			for(MemberUpdate update : updates)
			{
				ApiUpdateDTO updateDTO = new ApiUpdateDTO();
				packUpdateDTO(update, updateDTO, vid);
				
				//chosen comment
				MemberUpdateComment comment = heartmateService.obtainCommentNewest8UpdateId(update.getUpdateId());
				if(comment != null)
				{
					ApiUpdateCommentDTO commentDTO = packUpdateCommentDTO(comment, vid);
					
					MemberUpdateComment pcmnt = heartmateService.obtainCommentById(comment.getParentId());
					if(pcmnt != null)
					{
						ApiUpdateCommentDTO pcmntDTO = packUpdateCommentDTO(pcmnt, vid);
						commentDTO.setParent(pcmntDTO);
					}
					updateDTO.setChosenComment(commentDTO);
				}
				
				updateDTOs.add(updateDTO);
			}
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("updateDTOs", updateDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 获取动态详情
	 * @param updateId
	 * @param vid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainUpdateDetail.json")
	@ResponseBody
	public Object obtainUpdateDetailV1(Long updateId, Long vid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiUpdateDTO updateDTO = new ApiUpdateDTO();
		try{
			//增加阅读数
			heartmateService.transUpdateReadCount(updateId);
			
			MemberUpdate update = heartmateService.obtainUpdateById(updateId);
			packUpdateDTO(update, updateDTO, vid);
			updateDTO.setDetail(heartmateService.obtainDetailByUpdateId(updateId).getDetail());
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("updateDTO", updateDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 拥抱动态
	 * @param updateId 动态id
	 * @param vid 查看者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/embraceUpdate.json")
	@ResponseBody
	public Object embraceUpdateV1(Long updateId, Long vid)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId, vid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			if(heartmateService.isEmbracedUpdate(updateId, vid) == 1)
			{
				result.setCode(ErrorCode.ERROR_HEARTMATE_HAS_EMBRACED.getCode());
				result.setMsg(ErrorCode.ERROR_HEARTMATE_HAS_EMBRACED.getMessage());
				return result;
			}
			heartmateService.embraceUpdate(updateId, vid);
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
	 * 取消拥抱动态
	 * @param updateId 动态id
	 * @param vid 查看者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/unembraceUpdate.json")
	@ResponseBody
	public Object unembraceUpdateV1(Long updateId, Long vid)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId, vid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			heartmateService.unembraceUpdate(updateId, vid);
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
	 * 删除动态
	 * @param updateId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/deleteUpdate.json")
	@ResponseBody
	public Object deleteUpdateV1(Long updateId)
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
	 * 发表动态回应
	 * @param updateId 动态id
	 * @param parentId 父回应id
	 * @param mid 用户id
	 * @param commentContent 评论内容
	 * @param writeLocation 发布地点
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/publishComment.json")
	@ResponseBody
	public Object publishCommentV1(Long updateId, Long parentId, 
			Long mid, String commentContent, String writeLocation, Byte isAnony)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId, parentId, mid, commentContent))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			//创建评论
			MemberUpdateComment comment = new MemberUpdateComment();
			comment.setUpdateId(updateId);
			comment.setParentId(parentId);
			comment.setMid(mid);
			comment.setCommentContent(commentContent);
			comment.setWriteLocation(writeLocation);
			comment.setIsAnony(isAnony);
			heartmateService.createComment(comment);
			
			//消息提醒
			MemberUpdate update = heartmateService.obtainUpdateById(updateId);
			if (!update.getMid().equals(mid))
			{
				// 添加未读列表
				// 给咨询作者添加未读信息
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(comment.getCommentId());
				unreadComment.setMid(update.getMid());
				unreadCommentService.addUpdateCircleUnreadComment(unreadComment);
				
				//推送				
				pushService.pushSingleDevice(CustomMsgType.WARDMATE_COMMENTS_MSG, update.getMid(), null);
			}
			
			MemberUpdateComment pcmnt = heartmateService.obtainCommentById(parentId);
			if(pcmnt != null)
			{
				// 给父回应作者添加未读信息
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(comment.getCommentId());
				unreadComment.setMid(pcmnt.getMid());
				unreadCommentService.addUpdateCircleUnreadComment(unreadComment);
				
				//推送				
				pushService.pushSingleDevice(CustomMsgType.WARDMATE_COMMENTS_TO_AT, pcmnt.getMid(), null);
			}
		}catch (Exception e)
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
	
	/**
	 * 获取回应列表
	 * @param updateId 动态id
	 * @param vid 查看这id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainCommentList.json")
	@ResponseBody
	public Object obtainCommentListV1(Long updateId, Long vid, Integer pageIndex, Integer pageSize)
	{  
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(updateId, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiUpdateCommentDTO> commentDTOs = new ArrayList<ApiUpdateCommentDTO>();
		Integer count;
		try{
			List<MemberUpdateComment> comments =
					heartmateService.obtainComments8UpdateId3Page(updateId, pageIndex, pageSize);
			count = heartmateService.countComments8UpdateId(updateId);
			for(MemberUpdateComment comment : comments)
			{
				ApiUpdateCommentDTO commentDTO = packUpdateCommentDTO(comment, vid);
				
				MemberUpdateComment pcmnt = heartmateService.obtainCommentById(comment.getParentId());
				if(pcmnt != null)
				{
					ApiUpdateCommentDTO pcmntDTO = packUpdateCommentDTO(pcmnt, vid);
					commentDTO.setParent(pcmntDTO);
				}
				commentDTOs.add(commentDTO);				
			}
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
	 * 删除回应
	 * @param commentId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/deleteComment.json")
	@ResponseBody
	public Object deleteCommentV1(Long commentId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(commentId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
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
