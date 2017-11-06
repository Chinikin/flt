package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdvisoryTag;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisory4Psycho;
import com.depression.model.MemberAdvisoryComment;
import com.depression.model.MemberAdvisoryDetail;
import com.depression.model.MemberAdvisoryImgs;
import com.depression.model.api.dto.ApiAdvisoryTagDTO;
import com.depression.model.api.vo.ApiIdsVO;
import com.depression.service.AdvisoryTagService;
import com.depression.service.AdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.PunishmentService;
import com.depression.service.UnreadCommentService;
import com.depression.utils.PropertyUtils;

/**
 * 会员咨询
 * 
 * @author hongqian_li
 * @date 2016/05/11
 */
@Controller
@RequestMapping("/MemberAdvisory")
public class MemberAdvisoryController
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

	/**
	 * 获取标签列表
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
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
	
	/**
	 * 发表咨询
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param content
	 * @param writeLocation
	 * @param fileRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add.json")
	@ResponseBody
	public Object add(HttpSession session, HttpServletRequest request, Long mid, String content, String writeLocation, String jsonForImgs)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == mid || StringUtils.isEmpty(content) || StringUtils.isEmpty(writeLocation))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			// 判断内容长度
			int length = content.length();
			String des = null;
			if(length > 190){
				des = content.substring(0, 190) + "...";
			}else{
				des = content;
			}
			MemberAdvisory ma = new MemberAdvisory();
			ma.setContent(des);
			ma.setMid(mid);
			ma.setWriteLocation(writeLocation);
			advisoryService.createAdvisory(ma);
			Long advisoryId = ma.getAdvisoryId();
			// if (length > 200)
			{
				// 保存到详情里
				MemberAdvisoryDetail mad = new MemberAdvisoryDetail();
				mad.setDetail(content);
				mad.setAdvisoryId(advisoryId);
				advisoryService.createDetail(mad);
			}
			// 存在图片文件
			if (null != jsonForImgs && jsonForImgs.length() > 0)
			{
				JSONArray jsonArr = new JSONArray();
				jsonArr = JSON.parseArray(jsonForImgs);
				for (int idx = 0; idx < jsonArr.size(); idx++)
				{
					JSONObject obj = (JSONObject) jsonArr.get(idx);
					String imgPath = obj.getString("fileRelPath");
					String imgPreviewPath = obj.getString("filePreviewRelPath");
					MemberAdvisoryImgs mbimgs = new MemberAdvisoryImgs();
					mbimgs.setImgPath(imgPath);
					mbimgs.setImgPreviewPath(imgPreviewPath);
					mbimgs.setAdvisoryId(advisoryId);
					advisoryService.createImgs(mbimgs);
				}

			}

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}
	
	/**
	 * 发表咨询
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param content
	 * @param writeLocation
	 * @param fileRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addx.json")
	@ResponseBody
	public Object addx(HttpSession session, HttpServletRequest request, Long mid, String content, String writeLocation, String jsonForImgs, ApiIdsVO tagIds)
	{
		ResultEntity result = new ResultEntity();
		
		if(punishmentService.obtainDisableMessageDays(mid) > 0)
		{
			result.put("disableMessageDays", punishmentService.obtainDisableMessageDays(mid));
			result.setCode(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getCode());
			result.setMsg(ErrorCode.ERROR_PUNISHMENT_DISABLE_MESSAGE.getMessage());
			return result;
		}
		
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == mid || StringUtils.isEmpty(content) || StringUtils.isEmpty(writeLocation))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			// 判断内容长度
			int length = content.length();
			String des = null;
			if(length > 190){
				des = content.substring(0, 190) + "...";
			}else{
				des = content;
			}
			MemberAdvisory ma = new MemberAdvisory();
			ma.setContent(des);
			ma.setMid(mid);
			ma.setWriteLocation(writeLocation);
			advisoryService.createAdvisory(ma);
			Long advisoryId = ma.getAdvisoryId();
			// if (length > 200)
			{
				// 保存到详情里
				MemberAdvisoryDetail mad = new MemberAdvisoryDetail();
				mad.setDetail(content);
				mad.setAdvisoryId(advisoryId);
				advisoryService.createDetail(mad);
			}
			// 存在图片文件
			if (null != jsonForImgs && jsonForImgs.length() > 0)
			{
				JSONArray jsonArr = new JSONArray();
				jsonArr = JSON.parseArray(jsonForImgs);
				for (int idx = 0; idx < jsonArr.size(); idx++)
				{
					JSONObject obj = (JSONObject) jsonArr.get(idx);
					String imgPath = obj.getString("fileRelPath");
					String imgPreviewPath = obj.getString("filePreviewRelPath");
					MemberAdvisoryImgs mbimgs = new MemberAdvisoryImgs();
					mbimgs.setImgPath(imgPath);
					mbimgs.setImgPreviewPath(imgPreviewPath);
					mbimgs.setAdvisoryId(advisoryId);
					advisoryService.createImgs(mbimgs);
				}

			}
			// 记录tag信息
			if(tagIds != null&&null!=tagIds.getIds())
			{
				advisoryTagService.newAdvisoryTagMaps(advisoryId, tagIds.getIds());
			}

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/delete.json")
	@ResponseBody
	public Object delete(HttpSession session, HttpServletRequest request, Long advisoryId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == advisoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("请输入要删除的咨询ID");
				return result;
			}
			advisoryService.deleteAdvisory(advisoryId);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取咨询列表
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param content
	 * @param writeLocation
	 * @param fileRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/allListByPage.json")
	@ResponseBody
	public Object allListByPage(HttpSession session, HttpServletRequest request, Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		List<Object> resultJSON = new ArrayList<>();
		try
		{
			if (null == pageIndex || null == pageSize)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			MemberAdvisory mba = new MemberAdvisory();
			mba.setPageIndex(pageIndex);
			mba.setPageSize(pageSize);
			if (null != mid)
			{
				mba.setMid(mid);
			}
			List<MemberAdvisory> mbas = advisoryService.obtainAdvisory3Page0TmDesc(mba);
			for (MemberAdvisory memberAdvisory : mbas)
			{
				// 获取评论条数
				long commentCount = advisoryService.countComments(memberAdvisory.getAdvisoryId());
				JSONObject jsobj = new JSONObject();
				// 返回参数
				Member member = new Member();
				member.setMid(memberAdvisory.getMid());
				member = memberService.selectMemberByMid(memberAdvisory.getMid());
				jsobj.put("mid", member.getMid());
				jsobj.put("imAccount", member.getImAccount());
				jsobj.put("avatar", member.getAvatar());
				String avatarThumbnail = member.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					jsobj.put("avatarThumbnail", avatarThumbnail);
				} else
				{
					jsobj.put("avatarThumbnail", "");
				}
				jsobj.put("username", member.getUserName());
				String nickname = member.getNickname();
				if (null == mid)
				{
					if (StringUtils.isEmpty(nickname))
					{
						jsobj.put("nickname", "");
					} else
					{
						String mobilePhone = member.getMobilePhone();
						if (mobilePhone != null && mobilePhone.length() >5)
						{
							String phoneSuf = mobilePhone.substring(mobilePhone.length() - 5) ;
							Integer nickFinal = (Integer.parseInt(phoneSuf) + 1234) % 10000;
							jsobj.put("nickname", "匿名用户" + nickFinal);
						}
						else
						{
							jsobj.put("nickname", "匿名用户");
						}
					}
				} else
				{
					jsobj.put("nickname", nickname);
				}
				jsobj.put("mLevel", member.getmLevel());
				jsobj.put("userType", member.getUserType());
				jsobj.put("advisoryId", memberAdvisory.getAdvisoryId());
				jsobj.put("content", memberAdvisory.getContent());
				jsobj.put("writeLocation", memberAdvisory.getWriteLocation());
				jsobj.put("createTime", memberAdvisory.getCreateTime());
				jsobj.put("commentCount", commentCount);
				// 查找图片
				List<MemberAdvisoryImgs> imgs = advisoryService.obtainImgsByAdvisoryId(memberAdvisory.getAdvisoryId());
				for (MemberAdvisoryImgs imgObj : imgs)
				{
					imgObj.setImgPreviewPath(imgObj.getImgPreviewPath());
					imgObj.setImgPath(imgObj.getImgPath());
				}
				
				//添加感谢最多的回复
				MemberAdvisoryComment mac = advisoryService.obtainCommentPraisestByAdvisoryId(memberAdvisory.getAdvisoryId());
				if(mac!=null){
					jsobj.put("praisedCommentContent", mac.getCommentContent());
					jsobj.put("praisedCommentId", mac.getCommentId());
					Member commentMem = memberService.selectMemberByMid(mac.getMid());
					if(commentMem.getUserType()==1&&!commentMem.getMid().equals(mid))
					{
						String mobilePhone = commentMem.getMobilePhone();
						String phoneSuf = mobilePhone.substring(mobilePhone.length() - 5) ;
						Integer nickFinal = (Integer.parseInt(phoneSuf) + 1234) % 10000;
						jsobj.put("praisedNickname", "匿名用户" + nickFinal);
					}else{
						jsobj.put("praisedNickname", commentMem.getNickname());
					}
					jsobj.put("praisedTitle", commentMem.getTitle());
					jsobj.put("praisedMid", commentMem.getMid());
				}else
				{
					jsobj.put("praisedCommentContent", null);
					jsobj.put("praisedCommentId", null);
					jsobj.put("praisedNickname", null);
					jsobj.put("praisedTitle", null);
					jsobj.put("praisedMid", null);
				}
				
				jsobj.put("imgs", imgs);
				resultJSON.add(jsobj);
			}
			long totalCount = advisoryService.countAdvisory(mba);
			result.put("totalCount", totalCount);
			result.put("Comments", resultJSON);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取咨询列表
	 * 
	 * @param session
	 * @param request
	 * @param mid 获取自己的列表时传入自己mid
	 * @param content
	 * @param writeLocation
	 * @param fileRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/allListByPagex.json")
	@ResponseBody
	public Object allListByPagex(HttpSession session, HttpServletRequest request, Long mid, Integer pageIndex, Integer pageSize, Long tagId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		List<Object> resultJSON = new ArrayList<>();
		try
		{
			if (null == pageIndex || null == pageSize)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			MemberAdvisory mba = new MemberAdvisory();
			mba.setPageIndex(pageIndex);
			mba.setPageSize(pageSize);
			if (null != mid)
			{
				mba.setMid(mid);
			}
			List<MemberAdvisory> mbas ;
			long totalCount;
			if(tagId == null)
			{
				mbas = advisoryService.obtainAdvisory3Page0TmDesc(mba);
				totalCount = advisoryService.countAdvisory(mba);
			}else{
				advisoryTagService.transAdvisoryHit(tagId);
				mbas = advisoryService.obtainAdvisoryByTag3Page0TmDesc(pageIndex, pageSize, tagId);
				totalCount = advisoryService.countAdvisoryByTag(tagId);
			}
			for (MemberAdvisory memberAdvisory : mbas)
			{
				// 获取评论条数
				long commentCount = advisoryService.countComments(memberAdvisory.getAdvisoryId());
				JSONObject jsobj = new JSONObject();
				// 返回参数
				Member member = new Member();
				member.setMid(memberAdvisory.getMid());
				member = memberService.selectMemberByMid(memberAdvisory.getMid());
				if(member==null)
				{
					continue;
				}
				jsobj.put("mid", member.getMid());
				jsobj.put("imAccount", member.getImAccount());
				jsobj.put("avatar", member.getAvatar());
				String avatarThumbnail = member.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					jsobj.put("avatarThumbnail", avatarThumbnail);
				} else
				{
					jsobj.put("avatarThumbnail", "");
				}
				jsobj.put("username", member.getUserName());
				String nickname = member.getNickname();
				if (null == mid)
				{
					String mobilePhone = member.getMobilePhone();
					if (mobilePhone != null && mobilePhone.length() >5)
					{
						String phoneSuf = mobilePhone.substring(mobilePhone.length() - 5) ;
						Integer nickFinal = (Integer.parseInt(phoneSuf) + 1234) % 10000;
						jsobj.put("nickname", "匿名用户" + nickFinal);
					}
					else
					{
						jsobj.put("nickname", "匿名用户");
					}

				} else
				{
					jsobj.put("nickname", nickname);
				}
				jsobj.put("mLevel", member.getmLevel());
				jsobj.put("userType", member.getUserType());
				jsobj.put("advisoryId", memberAdvisory.getAdvisoryId());
				jsobj.put("content", memberAdvisory.getContent());
				jsobj.put("writeLocation", memberAdvisory.getWriteLocation());
				jsobj.put("createTime", memberAdvisory.getCreateTime());
				jsobj.put("commentCount", commentCount);
				// 查找图片
				List<MemberAdvisoryImgs> imgs = advisoryService.obtainImgsByAdvisoryId(memberAdvisory.getAdvisoryId());
				for (MemberAdvisoryImgs imgObj : imgs)
				{
					imgObj.setImgPreviewPath(imgObj.getImgPreviewPath());
					imgObj.setImgPath(imgObj.getImgPath());
				}
				
				//查找tags
				List<AdvisoryTag> tags = advisoryTagService.selectAdvisoryTagByAdvisoryId(memberAdvisory.getAdvisoryId());
				List<ApiAdvisoryTagDTO> advisoryTagDTOs = new ArrayList<ApiAdvisoryTagDTO>();
				for(AdvisoryTag tag : tags)
				{
					ApiAdvisoryTagDTO tagDTO = new ApiAdvisoryTagDTO();
					BeanUtils.copyProperties(tag, tagDTO);
					advisoryTagDTOs.add(tagDTO);
				}
				jsobj.put("tags", advisoryTagDTOs);
				//添加感谢最多的回复
				MemberAdvisoryComment mac = advisoryService.obtainCommentPraisestByAdvisoryId(memberAdvisory.getAdvisoryId());
				if(mac!=null){
					jsobj.put("praisedCommentContent", mac.getCommentContent());
					jsobj.put("praisedCommentId", mac.getCommentId());
					Member commentMem = memberService.selectMemberByMid(mac.getMid());
					if(commentMem.getUserType()==1&&!commentMem.getMid().equals(mid))
					{
						String mobilePhone = commentMem.getMobilePhone();
						String phoneSuf = mobilePhone.substring(mobilePhone.length() - 5) ;
						Integer nickFinal = (Integer.parseInt(phoneSuf) + 1234) % 10000;
						jsobj.put("praisedNickname", "匿名用户" + nickFinal);
					}else{
						jsobj.put("praisedNickname", commentMem.getNickname());
					}
					jsobj.put("praisedTitle", commentMem.getTitle());
					jsobj.put("praisedMid", commentMem.getMid());
					jsobj.put("praisedAvatar", commentMem.getAvatar());
				}else
				{
					jsobj.put("praisedCommentContent", null);
					jsobj.put("praisedCommentId", null);
					jsobj.put("praisedNickname", null);
					jsobj.put("praisedTitle", null);
					jsobj.put("praisedMid", null);
					jsobj.put("praisedAvatar",null);
				}
				
				jsobj.put("imgs", imgs);
				resultJSON.add(jsobj);
			}

			result.put("totalCount", totalCount);
			result.put("Comments", resultJSON);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	
	/**
	 * 获取咨询列表(自己) 废弃
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param content
	 * @param writeLocation
	 * @param fileRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/myAllListByPage.json")
	@ResponseBody
	public Object myAllListByPage(HttpSession session, HttpServletRequest request, Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		List<Object> resultJSON = new ArrayList<>();
		try
		{
			if (null == pageIndex || null == pageSize || null == mid)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("请求参数请输入完成");
				return result;
			}
			MemberAdvisory mba = new MemberAdvisory();
			mba.setPageIndex(pageIndex);
			mba.setPageSize(pageSize);
			mba.setMid(mid);
			List<MemberAdvisory> mbas = advisoryService.obtainAdvisory3Page0TmDesc(mba);
			for (MemberAdvisory memberAdvisory : mbas)
			{
				// 获取评论条数
				long commentCount = advisoryService.countComments(memberAdvisory.getAdvisoryId());
				JSONObject jsobj = new JSONObject();
				// 返回参数
				Member member = new Member();
				member.setMid(memberAdvisory.getMid());
				member = memberService.getMember(member);
				jsobj.put("mid", member.getMid());
				jsobj.put("imAccount", member.getImAccount());
				jsobj.put("avatar", member.getAvatar());
				String avatarThumbnail = member.getAvatarThumbnail();
				if (!StringUtils.isEmpty(avatarThumbnail))
				{
					jsobj.put("avatarThumbnail", avatarThumbnail);
				} else
				{
					jsobj.put("avatarThumbnail", "");
				}
				jsobj.put("username", member.getUserName());
				jsobj.put("nickname", member.getNickname());

				jsobj.put("mLevel", member.getmLevel());
				jsobj.put("userType", member.getUserType());
				jsobj.put("advisoryId", memberAdvisory.getAdvisoryId());
				jsobj.put("content", memberAdvisory.getContent());
				jsobj.put("writeLocation", memberAdvisory.getWriteLocation());
				jsobj.put("createTime", memberAdvisory.getCreateTime());
				jsobj.put("commentCount", commentCount);
				// 查找图片
				List<MemberAdvisoryImgs> imgs = advisoryService.obtainImgsByAdvisoryId(memberAdvisory.getAdvisoryId());;
				for (MemberAdvisoryImgs imgObj : imgs)
				{
					imgObj.setImgPreviewPath(imgObj.getImgPreviewPath());
					imgObj.setImgPath(imgObj.getImgPath());
				}
				jsobj.put("imgs", imgs);
				resultJSON.add(jsobj);
			}
			long totalCount = advisoryService.countAdvisory(mba);
			result.put("totalCount", totalCount);
			result.put("Comments", resultJSON);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 咨询详情
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param updateId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getAdvisoryDetail.json")
	@ResponseBody
	public Object getAdvisoryDetail(HttpSession session, HttpServletRequest request, Long mid, Long advisoryId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == advisoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("咨询ID为空");
				return result;
			}
			MemberAdvisory ma = advisoryService.obtainAdvisoryById(advisoryId);
			if (ma == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("咨询内容无法获取");
				return result;
			}
			MemberAdvisoryDetail mad = advisoryService.obtainDetailByAdvisoryId(advisoryId);			
			if (mad == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("不存在该咨询");
				return result;
			}

			result.put("advisoryId", ma.getAdvisoryId());
			result.put("createTime", ma.getCreateTime());
			result.put("writeLocation", ma.getWriteLocation());
			Member mb = new Member();
			mb.setMid(ma.getMid());
			mb = memberService.getMember(mb);
			result.put("mid", ma.getMid());
			result.put("imAccount", mb.getImAccount());
			result.put("avatar", mb.getAvatar());
			String avatarThumbnail = mb.getAvatarThumbnail();
			if (!StringUtils.isEmpty(avatarThumbnail))
			{
				result.put("avatarThumbnail", avatarThumbnail);
			} else
			{
				result.put("avatarThumbnail", "");
			}
			String nickname = mb.getNickname();
			if (null == mid)
			{
				if (StringUtils.isEmpty(nickname))
				{
					result.put("nickname", "");
				} else
				{
					String mobilePhone = mb.getMobilePhone();
					if (mobilePhone != null && mobilePhone.length() >5)
					{
						String phoneSuf = mobilePhone.substring(mobilePhone.length() - 5) ;
						Integer nickFinal = (Integer.parseInt(phoneSuf) + 1234) % 10000;
						result.put("nickname", "匿名用户" + nickFinal);
					}
					else
					{
						result.put("nickname", "匿名用户");
					}
				}
			} else
			{
				result.put("nickname", nickname);
			}
			result.put("username", mb.getUserName());
			result.put("userType", mb.getUserType());
			result.put("mLevel", mb.getmLevel());
			result.put("content", mad.getDetail());
			// 查找图片
			List<MemberAdvisoryImgs> imgs = advisoryService.obtainImgsByAdvisoryId(mad.getAdvisoryId());
			for (MemberAdvisoryImgs imgObj : imgs)
			{
				imgObj.setImgPreviewPath(imgObj.getImgPreviewPath());
				imgObj.setImgPath(imgObj.getImgPath());
			}
			result.put("imgs", imgs);

			//查找tags
			List<AdvisoryTag> tags = advisoryTagService.selectAdvisoryTagByAdvisoryId(mad.getAdvisoryId());
			List<ApiAdvisoryTagDTO> advisoryTagDTOs = new ArrayList<ApiAdvisoryTagDTO>();
			for(AdvisoryTag tag : tags)
			{
				ApiAdvisoryTagDTO tagDTO = new ApiAdvisoryTagDTO();
				BeanUtils.copyProperties(tag, tagDTO);
				advisoryTagDTOs.add(tagDTO);
			}
			result.put("tags", advisoryTagDTOs);
			
			// 删除未读评论记录
//			if (mid != null)
//			{
//				unreadCommentService.deleteUnreadAdvisoryComment(mid, advisoryId);
//			}

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}
	
	/**
	 * 获取咨询师回答过的咨询列表
	 * @param mid 咨询师和会员id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getAnsweredList.json")
	@ResponseBody
	public Object getAnsweredList(Long mid, Integer pageIndex, 
			Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(mid, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<MemberAdvisory4Psycho> advisory4Psychos;
		Integer count;
		try{
			advisory4Psychos = advisoryService.obtainAdvisory4Psychos(mid, pageIndex, pageSize);
			count = advisoryService.countAdvisory4Psychos(mid);
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("answeredAdvisorys", advisory4Psychos);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
