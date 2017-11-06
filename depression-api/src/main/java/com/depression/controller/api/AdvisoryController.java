package com.depression.controller.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.depression.model.MemberAdvisory4PsychoV1;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberAdvisoryDetail;
import com.depression.model.MemberAdvisoryImgs;
import com.depression.model.UnreadComment;
import com.depression.model.api.dto.ApiAdvisoryCommentDTO;
import com.depression.model.api.dto.ApiAdvisoryDTO;
import com.depression.model.api.dto.ApiAdvisoryImgsDTO;
import com.depression.model.api.dto.ApiAdvisoryTagDTO;
import com.depression.model.api.dto.ApiArticleDTO;
import com.depression.model.api.dto.ApiMemberEssentialDTO;
import com.depression.push.CustomMsgType;
import com.depression.service.AdvisoryService;
import com.depression.service.AdvisoryTagService;
import com.depression.service.EapService;
import com.depression.service.MemberService;
import com.depression.service.PsychoInfoService;
import com.depression.service.PunishmentService;
import com.depression.service.PushService;
import com.depression.service.ServiceStatisticsService;
import com.depression.service.UnreadCommentService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/Advisory")
public class AdvisoryController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	AdvisoryService advisoryService;
	@Autowired
	MemberService memberService;
	@Autowired
	UnreadCommentService unreadCommentService;
	@Autowired
	AdvisoryTagService advisoryTagService;
	@Autowired
	PunishmentService punishmentService;
	@Autowired	
	PsychoInfoService psychoInfoService;
	@Autowired		
	EapService eapService;
	@Autowired		
	PushService pushService;
	@Autowired	
	ServiceStatisticsService serviceStatisticsService; 
	/**
	 * 获取标签列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainTagList.json")
	@ResponseBody
	public Object obtainTagListV1(Integer pageIndex, Integer pageSize)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		List<ApiAdvisoryTagDTO> advisoryTagDTOs = new ArrayList<ApiAdvisoryTagDTO>();
		Integer count=0;
		try{
			List<AdvisoryTag> tags = advisoryTagService.selectWithPageOrder(pageIndex, pageSize, (byte) 0);
			count = advisoryTagService.countSelect((byte) 0);
			
			for(AdvisoryTag tag : tags)
			{
				ApiAdvisoryTagDTO tagDTO = new ApiAdvisoryTagDTO();
				BeanUtils.copyProperties(tag, tagDTO);
				advisoryTagDTOs.add(tagDTO);
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		result.put("advisoryTags", advisoryTagDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	

	/**
	 * 发布咨询
	 * @param mid 会员id
	 * @param content 内容
	 * @param writeLocation 发布地址
	 * @param imgsJsn img文件jsn数组
	 * @param tagId 标签id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/publishAdvisory.json")
	@ResponseBody
	public Object publishAdvisoryV1(Long mid, String title, String content, String writeLocation, String imgsJsn, Long tagId, Byte isAnony,Long releaseFrom)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, title, content, tagId, isAnony))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(punishmentService.obtainDisableMessageDays(mid) > 0)
		{
			result.put("disableMessageDays", punishmentService.obtainDisableMessageDays(mid));
			result.setCode(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getCode());
			result.setMsg(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getMessage());
			return result;
		}
		
		try
		{
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
			MemberAdvisory ma = new MemberAdvisory();
			ma.setTitle(title);
			ma.setContent(digest);
			ma.setMid(mid);
			ma.setWriteLocation(writeLocation);
			ma.setReleaseFrom(releaseFrom);
			ma.setIsAnony(isAnony);
			advisoryService.createAdvisory(ma);
			Long advisoryId = ma.getAdvisoryId();

			// 保存到详情里
			MemberAdvisoryDetail mad = new MemberAdvisoryDetail();
			mad.setDetail(content);
			mad.setAdvisoryId(advisoryId);
			advisoryService.createDetail(mad);

			// 存在图片文件
			if (null != imgsJsn)
			{
				try{
					List<String> imgs = JSON.parseArray(imgsJsn, String.class);
					for(String img : imgs)
					{
						MemberAdvisoryImgs mbimgs = new MemberAdvisoryImgs();
						mbimgs.setImgPath(img);
						mbimgs.setImgPreviewPath(img);
						mbimgs.setAdvisoryId(advisoryId);
						advisoryService.createImgs(mbimgs);
					}
				}catch(Exception e)
				{
					
				}	
			}
			
			// 记录tag信息
			advisoryTagService.newAdvisoryTagMap(advisoryId, tagId);
	
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
			meDTO.setIsAnony((byte) 1);
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
				meDTO.setIsAnony((byte) 0);
			}
			
			return meDTO;
		}catch(Exception e)
		{//用户被删除或者丢失
			ApiMemberEssentialDTO meDTO = new ApiMemberEssentialDTO();
			meDTO.setNickname("匿名用户");
			return meDTO;
		}
	}
	
	private void packAdvisoryDTO(MemberAdvisory ad, ApiAdvisoryDTO advisoryDTO, Long vid)
	{
		BeanUtils.copyProperties(ad, advisoryDTO);
		
		advisoryDTO.setCreateTimestamp(ad.getCreateTime().getTime());
		
		//title
		if(ad.getTitle() == null)
		{
			Integer len = ad.getContent().length();
			len = len < 12 ? len : 12;//最大12
			advisoryDTO.setTitle(ad.getContent().substring(0, len));
		}
		
		//tag
		AdvisoryTag tag = advisoryTagService.selectByAdvisoryId(ad.getAdvisoryId());
		if(tag != null)
		{
			ApiAdvisoryTagDTO tagDTO = new ApiAdvisoryTagDTO();
			BeanUtils.copyProperties(tag, tagDTO);
			advisoryDTO.setTag(tagDTO);
		}
		//imgs
		List<MemberAdvisoryImgs> imgs = advisoryService.obtainImgsByAdvisoryId(ad.getAdvisoryId());
		List<ApiAdvisoryImgsDTO> imgsDTOs = new ArrayList<ApiAdvisoryImgsDTO>();
		for(MemberAdvisoryImgs img : imgs)
		{
			ApiAdvisoryImgsDTO imgsDTO = new ApiAdvisoryImgsDTO();
			BeanUtils.copyProperties(img, imgsDTO);
			
			imgsDTOs.add(imgsDTO);
		}
		advisoryDTO.setImgs(imgsDTOs);
		//author
		ApiMemberEssentialDTO meDTO = packMemberEssentialDTO(ad.getMid(), vid, ad.getIsAnony());
		advisoryDTO.setAuthor(meDTO);
		
		//answer count
		advisoryDTO.setAnswerCount(advisoryService.countComments(ad.getAdvisoryId(), 0L));
	}
	
	/**
	 * 搜索咨询列表
	 * @param pageIndex
	 * @param pageSize
	 * @param tagId 标签id 特别借用值 1000推荐 1001全部
	 * @param sortMode 1 默认 2最多阅读 3最多回复 4最多分享
	 * @param vid 查看者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/searchAdvisoryList.json")
	@ResponseBody
	public Object searchAdvisoryListV1(Integer pageIndex, Integer pageSize, Long tagId,
			Byte sortMode, Long vid , Long releaseFrom)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, tagId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(releaseFrom == null){
			releaseFrom = 0L;
		}
		
		List<ApiAdvisoryDTO> advisoryDTOs = new ArrayList<ApiAdvisoryDTO>();
		Integer count;
		try{
			List<MemberAdvisory> advisories;
			if(tagId == 1000)
			{//推荐
				advisories = advisoryService.searchAdvisoryV1(pageIndex, pageSize, (byte) 1, null, sortMode,releaseFrom);
				count = advisoryService.countSearchAdvisoryV1((byte) 1, null,releaseFrom);
			}else if(tagId == 1001 || tagId == null)
			{//全部
				advisories = advisoryService.searchAdvisoryV1(pageIndex, pageSize, null, null, sortMode,releaseFrom);
				count = advisoryService.countSearchAdvisoryV1(null, null,releaseFrom);
			}else
			{
				advisories = advisoryService.searchAdvisoryV1(pageIndex, pageSize, null, tagId, sortMode,releaseFrom);
				count = advisoryService.countSearchAdvisoryV1(null, tagId,releaseFrom);
				
				//增加tagId点击数
				advisoryTagService.transAdvisoryHit(tagId);
			}
			for(MemberAdvisory ad : advisories)
			{
				ApiAdvisoryDTO advisoryDTO = new ApiAdvisoryDTO();
				packAdvisoryDTO(ad, advisoryDTO, vid);
				
				//praisest comment
				MemberAdvisoryComment comment = advisoryService.obtainCommentPraisestByAdvisoryId(ad.getAdvisoryId());
				if(comment != null)
				{
					ApiAdvisoryCommentDTO commentDTO = packAdvisoryCommentDTO(comment, vid);
					
					advisoryDTO.setChosenComment(commentDTO);
				}
				
				advisoryDTOs.add(advisoryDTO);
			}
			
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("advisoryDTOs", advisoryDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 搜索咨询列表
	 * @param pageIndex
	 * @param pageSize
	 * @param mid 会员mid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainMyAdvisoryList.json")
	@ResponseBody
	public Object obtainMyAdvisoryListV1(Integer pageIndex, Integer pageSize, Long mid)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(pageIndex, pageSize, mid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		Long vid = mid;
		
		List<ApiAdvisoryDTO> advisoryDTOs = new ArrayList<ApiAdvisoryDTO>();
		Integer count;
		try{
			MemberAdvisory adt = new MemberAdvisory();
			adt.setMid(mid);
			adt.setPageIndex(pageIndex);
			adt.setPageSize(pageSize);
			List<MemberAdvisory> advisories = advisoryService.obtainAdvisory3Page0TmDesc(adt);
			count = advisoryService.countAdvisory(adt);
				

			for(MemberAdvisory ad : advisories)
			{
				ApiAdvisoryDTO advisoryDTO = new ApiAdvisoryDTO();
				packAdvisoryDTO(ad, advisoryDTO, vid);
				
				//praisest comment
				MemberAdvisoryComment comment = advisoryService.obtainCommentPraisestByAdvisoryId(ad.getAdvisoryId());
				if(comment != null)
				{
					ApiAdvisoryCommentDTO commentDTO = packAdvisoryCommentDTO(comment, vid);
					
					advisoryDTO.setChosenComment(commentDTO);
				}
				
				advisoryDTOs.add(advisoryDTO);
			}
			
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("advisoryDTOs", advisoryDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}



	/**
	 * 获取咨询师回答过的咨询列表
	 * @param mid 咨询师和会员id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainPsychoAnsweredAdvisoryList.json")
	@ResponseBody
	public Object obtainPsychoAnsweredAdvisoryListV1(Long pid, Integer pageIndex, 
			Integer pageSize, Long vid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(pid, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiAdvisoryDTO> advisoryDTOs = new ArrayList<ApiAdvisoryDTO>();
		Integer count;
		try{
			List<MemberAdvisory4PsychoV1> advisories = 
					advisoryService.obtainAdvisory4PsychosV1(pid, pageIndex, pageSize);
			count = advisoryService.countAdvisory4PsychosV1(pid);
			
			for(MemberAdvisory4PsychoV1 ad : advisories)
			{
				ApiAdvisoryDTO advisoryDTO = new ApiAdvisoryDTO();
				packAdvisoryDTO(ad, advisoryDTO, vid);
				
				//praisest comment
				MemberAdvisoryComment comment = advisoryService.obtainCommentById(ad.getCommentId());
				if(comment != null)
				{
					ApiAdvisoryCommentDTO commentDTO = packAdvisoryCommentDTO(comment, vid);
					
					advisoryDTO.setChosenComment(commentDTO);
				}
				
				advisoryDTOs.add(advisoryDTO);
			}
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("advisoryDTOs", advisoryDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}



	/**
	 * 获取咨询详情
	 * @param advisoryId 咨询id
	 * @param vid 查看者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainAdvisoryDetail.json")
	@ResponseBody
	public Object obtainAdvisoryDetailV1(Long advisoryId, Long vid)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiAdvisoryDTO advisoryDTO = new ApiAdvisoryDTO();
		try{
			//增加阅读数
			advisoryService.transAdvisoryReadCount(advisoryId);
			
			MemberAdvisory ad = advisoryService.obtainAdvisoryById(advisoryId);
			packAdvisoryDTO(ad, advisoryDTO, vid);
			advisoryDTO.setDetail(advisoryService.obtainDetailByAdvisoryId(advisoryId).getDetail());
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("advisoryDTO", advisoryDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 增加咨询被分享数
	 * @param advisoryId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/increAvisoryShareCount.json")
	@ResponseBody
	public Object increAvisoryShareCountV1(Long advisoryId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			advisoryService.transAdvisoryShareCount(advisoryId);
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
	@RequestMapping(method = RequestMethod.POST, value = "/V1/deleteAdvisory.json")
	@ResponseBody
	public Object deleteAdvisoryV1(Long advisoryId)
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
	 * 封装评论数据
	 * @param comment 评论
	 * @param vid 查看者id
	 * @param isAnony 咨询是否匿名 0 匿名 1不匿名
	 * @return
	 */
	private ApiAdvisoryCommentDTO packAdvisoryCommentDTO(MemberAdvisoryComment comment, Long vid)
	{
		ApiAdvisoryCommentDTO commentDTO = new ApiAdvisoryCommentDTO();
		BeanUtils.copyProperties(comment, commentDTO);
		
		//author
		ApiMemberEssentialDTO cmeDTO = packMemberEssentialDTO(comment.getMid(), vid, comment.getIsAnony());
		commentDTO.setAuthor(cmeDTO);
		
		//查询是否已点赞
		commentDTO.setIsPraised((byte) advisoryService.isPraisedComment(vid, comment.getCommentId()));
		
		return commentDTO;
	}
	
	/**
	 * 发表咨询评论
	 * 
	 * @param advisoryId
	 * @param mid
	 * @param commentContent
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/publishComment.json")
	@ResponseBody
	public Object publishCommentV1(Long advisoryId, Long mid, String commentContent, String writeLocation, Long parentId,
			Long ancestorId)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, advisoryId, commentContent, parentId, ancestorId))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(punishmentService.obtainDisableMessageDays(mid) > 0)
		{
			result.put("disableMessageDays", punishmentService.obtainDisableMessageDays(mid));
			result.setCode(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getCode());
			result.setMsg(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getMessage());
			return result;
		}
		
		try
		{
			MemberAdvisory advisory = advisoryService.obtainAdvisoryById(advisoryId);
			Member member = memberService.selectMemberByMid(mid);

			if (member.getUserType() == 1 &&!mid.equals(advisory.getMid()))
			{//只有本人可以回复
				result.setCode(ErrorCode.ERROR_AUTHORITY_CANNOT_COMMENT.getCode());
				result.setMsg(ErrorCode.ERROR_AUTHORITY_CANNOT_COMMENT.getMessage());
				return result;
			}
	
			MemberAdvisoryComment comment = new MemberAdvisoryComment();
			comment.setCommentContent(commentContent);
			comment.setWriteLocation(writeLocation);
			comment.setPraiseNum(0L);
			comment.setMid(mid);
			comment.setAdvisoryId(advisoryId);
			comment.setParentId(parentId);
			comment.setAncestorId(ancestorId);
			
			if(member.getUserType() == 2)
			{//增加咨询师回答数
				comment.setIsAnony((byte) 1);//咨询师不匿名
				memberService.transIncrAnswerCount(mid);
				//eap 企业统计
				serviceStatisticsService.updateEnterpriseAnswerCountByPsycho(mid);
			}else
			{
				//只有用户本人能回复，所以继承咨询的匿名设置
				comment.setIsAnony(advisory.getIsAnony()); 
			}
			
			advisoryService.createComment(comment);
	
			if (!advisory.getMid().equals(mid))
			{
				// 添加未读列表
				// 给咨询作者添加未读信息
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(comment.getCommentId());
				unreadComment.setMid(advisory.getMid());
				unreadCommentService.addAdvisoryUnreadComment(unreadComment);
				
				//推送				
				pushService.pushSingleDevice(CustomMsgType.ADVISORY_COMMENTS_MSG, advisory.getMid(), null);
			}
			//计算需要提醒的用户id
			MemberAdvisoryComment parent = advisoryService.obtainCommentById(parentId);
			MemberAdvisoryComment ancestor = advisoryService.obtainCommentById(ancestorId);
			Set<Long> noticeMid = new HashSet<>();
			if(parent != null)
			{
				noticeMid.add(parent.getMid());
			}
			if(ancestor != null)
			{
				noticeMid.add(ancestor.getMid());
			}
			noticeMid.remove(mid);
			noticeMid.remove(advisory.getMid());
			for(Long nmid : noticeMid)
			{
				// 添加未读列表
				// 给父评论作者或始祖评论添加未读信息
				UnreadComment unreadComment = new UnreadComment();
				unreadComment.setCommentId(comment.getCommentId());
				unreadComment.setMid(nmid);
				unreadCommentService.addAdvisoryUnreadComment(unreadComment);
				
				//推送				
				pushService.pushSingleDevice(CustomMsgType.ADVISORY_COMMENTS_MSG_TO_AT, nmid, null);
			}
			// 返回数据
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



	/**
	 * 获取咨询评论列表
	 * @param pageIndex
	 * @param pageSize
	 * @param advisoryId 咨询id
	 * @param vid 查看者id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainCommentList.json")
	@ResponseBody
	public Object obtainCommentListV1(Integer pageIndex, Integer pageSize, Long advisoryId, Long vid)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(advisoryId, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiAdvisoryCommentDTO> acmntDTOs = new ArrayList<ApiAdvisoryCommentDTO>();
		Integer count;
		try{
			MemberAdvisory advisory = advisoryService.obtainAdvisoryById(advisoryId);
			//ancenstor comments, 始祖评论（咨询师直接回答提问的）
			count = advisoryService.countComments(advisoryId, 0L);
			List<MemberAdvisoryComment> ancenstorComments = 
					advisoryService.obtainComment8AdvisoryId3Page0Praise(pageIndex, pageSize, advisoryId);
			
			for(MemberAdvisoryComment acmnt : ancenstorComments)
			{
				ApiAdvisoryCommentDTO acmntDTO = packAdvisoryCommentDTO(acmnt, vid);
				
				//descendant comments, 子孙评论（在始祖评论下回复的评论）
				List<ApiAdvisoryCommentDTO> dcmntDTOs = new ArrayList<ApiAdvisoryCommentDTO>();
				Integer descendantCount = advisoryService.countComments(advisoryId, acmnt.getCommentId());
				List<MemberAdvisoryComment> descendantComments = 
						advisoryService.obtainCommentsWithPage(advisoryId, acmnt.getCommentId(), 1, descendantCount);
				for(MemberAdvisoryComment dcmnt : descendantComments)
				{
					ApiAdvisoryCommentDTO dcmntDTO = packAdvisoryCommentDTO(dcmnt, vid);
					//parent comment, 父评论（子孙评论回复的对象）
					MemberAdvisoryComment pcmnt = advisoryService.obtainCommentById(dcmnt.getParentId());
					if(pcmnt != null)
					{//父评论可能被删除或者丢失
						ApiAdvisoryCommentDTO pcmntDTO = packAdvisoryCommentDTO(pcmnt, vid);
						dcmntDTO.setParent(pcmntDTO);
					}
					
					dcmntDTOs.add(dcmntDTO);
				}
			
				acmntDTO.setDescendantCount(descendantCount);
				acmntDTO.setDescendants(dcmntDTOs);
				
				acmntDTOs.add(acmntDTO);
			}
				
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("acmntDTOs", acmntDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 删除咨询评论
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
	
	/**
	 * 评论点赞
	 * @param commentId
	 * @param vid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/praiseComment.json")
	@ResponseBody
	public Object praiseCommentV1(Long commentId, Long vid)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(commentId, vid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			advisoryService.praiseComment(vid, commentId);
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
	 * 取消评论点赞
	 * @param commentId
	 * @param vid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/unpraiseComment.json")
	@ResponseBody
	public Object unpraiseCommentV1(Long commentId, Long vid)
	{
		ResultEntity result =  new ResultEntity();
		if (PropertyUtils.examineOneNull(commentId, vid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			advisoryService.unpraiseComment(vid, commentId);
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
	
    @Scheduled(cron="0 0 2 * * ? ")	
    public void dailyUpdateOrderFactor()
    {
    	log.info("Update Advisory Order Factor");
    	Integer pageIndex = 1;
    	Integer pageSize = 1000;
    	while(true)
    	{
    		List<MemberAdvisory> mas = advisoryService.obtainAdvisory3PageV1(pageIndex, pageSize);
    		if(mas.size() == 0)
    		{
    			break;
    		}
    		pageIndex++;
    		for(MemberAdvisory ma : mas)
    		{
    			try{//所有因子区间归一到100分
    				//阅读数
    				Integer readCount = ma.getReadCount();
    				readCount = readCount * 100 / 200; //当前基数200
    				//回复条数
    				Integer answerCount = advisoryService.countComments(ma.getAdvisoryId(), null);
    				answerCount = answerCount * 100 / 20; //当前基数20
    				//回复点赞数
    				List<MemberAdvisoryComment> macs = advisoryService.obtainCommentsByAdvisoryId(ma.getAdvisoryId());
    				Integer praiseNum = 0;
    				for(MemberAdvisoryComment mac : macs)
    				{
    					praiseNum += mac.getPraiseNum().intValue();
    				}
    				praiseNum = praiseNum * 100 / 20; //当前基数20
    				
    				//计算排序因子
    				Random random = new Random();
    				Integer orderFactor = (readCount + answerCount + praiseNum) 
    						+ random.nextInt(200);
    				ma.setOrderFactor(orderFactor);
    				advisoryService.updateAdvisory(ma);
    			}catch(Exception e)
    			{
    				continue;
    			}
    		}
    	}
    }
}
