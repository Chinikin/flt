package com.depression.controller.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.MemberConcern;
import com.depression.model.api.dto.ApiMemberConcernDTO;
import com.depression.push.CustomMsgType;
import com.depression.service.IMMessageService;
import com.depression.service.MemberConcernService;
import com.depression.service.MemberService;
import com.depression.service.PsychoInfoService;
import com.depression.service.PushService;
import com.depression.service.SystemMessageService;
import com.depression.utils.Configuration;
import com.depression.utils.PropertyUtils;

/**
 * @author:ziye_huang
 * @date:2016年5月5日
 */

@Controller
@RequestMapping("/MemberConcern")
public class MemberConcernController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	MemberConcernService memberConcernService;
	@Autowired
	MemberService memberService;
	@Autowired
	SystemMessageService systemMessageService;
	@Autowired
	PsychoInfoService psychoInfoService;
	@Autowired
	PushService pushService;
	@Autowired
	IMMessageService imMessageService;

	/**
	 * 确认是否已经关注
	 * 
	 * @param session
	 * @param request
	 * @param memberConcern
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/checkMemberConcern.json")
	@ResponseBody
	public Object checkMemberConcern(HttpSession session, HttpServletRequest request, MemberConcern memberConcern)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				memberConcern, 
				memberConcern.getConcernFrom(),
				memberConcern.getConcernTo()
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try
		{

			if (memberConcernService.checkConcern(memberConcern.getConcernFrom(), memberConcern.getConcernTo()))
			{
				result.put("existed", 1);
			} else
			{
				result.put("existed", 0);
			}
			result.setCode(ResultEntity.SUCCESS);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用关注接口失败");
			return result;
		}
	}

	/**
	 * 添加关注
	 * 
	 * @param session
	 * @param request
	 * @param memberConcern
	 * @return
	 */
	@RequestMapping(value = "/concernMemberConcern.json")
	@ResponseBody
	public Object concernMemberConcern(HttpSession session, HttpServletRequest request, MemberConcern memberConcern)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				memberConcern, 
				memberConcern.getConcernFrom(),
				memberConcern.getConcernTo()
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		try
		{

			if(memberConcernService.checkConcern(memberConcern.getConcernFrom(), memberConcern.getConcernTo()))
			{
				result.setCode(ResultEntity.SUCCESS);
				result.setMsg("之前已关注");
				return result;
			}else
			{
				memberConcernService.newConcern(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
			}
			//添加到未读列表
//			systemMessageService.addMessageConcorn(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
//			// 关注推送
			// 给发表动态的人推送被评论的消息
			Integer unreadCount = memberConcernService.countUnreadFansNum(memberConcern.getConcernTo());
			JSONObject jo = new JSONObject();
			jo.put("unreadCount", unreadCount);
			pushService.pushSingleDevice(CustomMsgType.MEMBER_CONCERN, memberConcern.getConcernTo(), jo.toJSONString());
			
			//IM消息
			Member mcf = memberService.selectMemberByMid(memberConcern.getConcernFrom());
			Member mct = memberService.selectMemberByMid(memberConcern.getConcernTo());
			if(mct != null && mcf != null)
			{
				imMessageService.sendConcernMsg(mcf.getNickname(), mcf.getMid(), mcf.getUserType(), mct.getImAccount());
			}
			
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("关注成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用关注接口失败");
			return result;
		}

	}

	/**
	 * 取消会员关注
	 * 
	 * @param session
	 * @param request
	 * @param memberConcern
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/cancelMemberConcern.json")
	@ResponseBody
	public Object cancelMemberConcern(HttpSession session, HttpServletRequest request, MemberConcern memberConcern)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				memberConcern, 
				memberConcern.getConcernFrom(),
				memberConcern.getConcernTo()
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		try
		{

			Integer ret = memberConcernService.removeConcern(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
//			if(ret > 0)
//			{
//				//添加取消关注提醒
//				systemMessageService.addMessageUnconcorn(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
//			}
			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("取消关注成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用取消关注接口失败");
			return result;
		}

	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/V1/concernMemberConcern.json")
	@ResponseBody
	public Object concernMemberConcernV1(HttpSession session, HttpServletRequest request, MemberConcern memberConcern,int isConcern)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				memberConcern, 
				memberConcern.getConcernFrom(),
				memberConcern.getConcernTo(),
				isConcern
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		
		
		
		
		try
		{
			//会员关注逻辑
			if(isConcern == 0){
				if(memberConcernService.checkConcern(memberConcern.getConcernFrom(), memberConcern.getConcernTo()))
				{
					result.setCode(ResultEntity.SUCCESS);
					result.setMsg("之前已关注");
					return result;
				}else
				{
					memberConcernService.newConcern(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
				}
				//添加到未读列表
//				systemMessageService.addMessageConcorn(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
//				// 关注推送
				// 给发表动态的人推送被评论的消息
				Integer unreadCount = memberConcernService.countUnreadFansNum(memberConcern.getConcernTo());
				JSONObject jo = new JSONObject();
				jo.put("unreadCount", unreadCount);
				pushService.pushSingleDevice(CustomMsgType.MEMBER_CONCERN, memberConcern.getConcernTo(), jo.toJSONString());
				
				//IM消息
				Member mcf = memberService.selectMemberByMid(memberConcern.getConcernFrom());
				Member mct = memberService.selectMemberByMid(memberConcern.getConcernTo());
				if(mct != null && mcf != null)
				{
	//				imMessageService.sendConcernMsg(mcf.getNickname(), mcf.getMid(), mcf.getUserType(), mct.getImAccount());
				}
				
				result.setCode(ResultEntity.SUCCESS);
				result.setMsg("关注成功");
				return result;
			}
			
			//取消关注逻辑
			if(isConcern == 1){
				Integer ret = memberConcernService.removeConcern(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
//				if(ret > 0)
//				{
//					//添加取消关注提醒
//					systemMessageService.addMessageUnconcorn(memberConcern.getConcernFrom(), memberConcern.getConcernTo());
//				}
				result.setCode(ResultEntity.SUCCESS);
				result.setMsg("取消关注成功");
				return result;
			}
			
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用取消关注接口失败");
			return result;
		}

	}
	

	/**
	 * 获取所有的会员关注
	 * !旧接口，以后会废弃
	 * @param session
	 * @param request
	 * @param memberConcern
	 * @return
	 */
	@RequestMapping(value = "/getAllMemberConcern.json")
	@ResponseBody
	public Object getAllMemberConcern(HttpSession session, HttpServletRequest request, MemberConcern memberConcern)
	{
		ResultEntity result = new ResultEntity();
		
		try
		{
			if (null == memberConcern)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传入的数据为空");
				return result;
			}

			if (memberConcern.getConcernFrom() == null && memberConcern.getConcernTo() == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传入的关注和被关注人数据为空");
				return result;
			}

			Integer count = memberConcernService.selectCount(memberConcern);
			List<MemberConcern> memberConcernLists = memberConcernService.selectByPage(memberConcern);
			result.setCode(ResultEntity.SUCCESS);
			result.setCount(count);
			result.put("list", memberConcernLists);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取关注接口失败");
			return result;
		}

	}

	/**
	 * 获取所有的会员关注
	 * !旧接口，以后会废弃
	 * @param session
	 * @param request
	 * @param type
	 *            0==我关注的人 1==关注我的人
	 * @param mid
	 * @return
	 */
	@RequestMapping(value = "/getAllMemberConcernByPage.json")
	@ResponseBody
	public Object getAllMemberConcernByPage(HttpSession session, HttpServletRequest request, Integer type, String mid, Integer pageSize, Integer pageIndex)
	{
		ResultEntity result = new ResultEntity();
		List<Object> resultJSON = new ArrayList<>();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == pageIndex || null == pageSize || null == mid)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("传入参数缺少");
				return result;
			}
			MemberConcern mc = new MemberConcern();
			mc.setPageIndex(pageIndex);
			mc.setPageSize(pageSize);
			// 我关注的人
			if (type == 0)
			{
				mc.setConcernFrom(Long.parseLong(mid));
			} else if (type == 1)
			{
				mc.setConcernTo(Long.parseLong(mid));
			}

			Integer count = memberConcernService.selectCount(mc);
			List<MemberConcern> memberConcernLists = memberConcernService.selectByPage(mc);

			for (MemberConcern memberConcern : memberConcernLists)
			{
				Member mb = new Member();
				// 我关注的人
				if (type == 0)
				{
					Long concernTo = memberConcern.getConcernTo();
					mb.setMid(concernTo);
				} else if (type == 1)// 关注我的人
				{
					Long concernFrom = memberConcern.getConcernFrom();
					mb.setMid(concernFrom);
				}
				mb = memberService.getMember(mb);
				if (null != mb)
				{
					JSONObject jsb = new JSONObject();
					jsb.put("mid", mb.getMid());
					jsb.put("imAccount", mb.getImAccount());
					jsb.put("avatar", mb.getAvatar());
					String avatarThumbnail = mb.getAvatarThumbnail();
					if (!StringUtils.isEmpty(avatarThumbnail))
					{
						jsb.put("avatarThumbnail", avatarThumbnail);
					} else
					{
						jsb.put("avatarThumbnail", "");
					}
					jsb.put("username", mb.getUserName());
					jsb.put("email", mb.getEmail());
					jsb.put("nickname", mb.getNickname());
					jsb.put("hobby", mb.getHobby());
					jsb.put("mLevel", mb.getmLevel());
					jsb.put("userType", mb.getUserType());
					resultJSON.add(jsb);
				}
			}

			result.put("totalCount", count);
			result.put("list", resultJSON);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取关注接口失败");
			return result;
		}

	}
	
	/**
	 * 计算年龄
	 * @param birthday
	 * @return
	 */
	Integer calcAge(Date birthday)
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
	 * 从member填充数据到ApiMemberConcernDTO
	 * @param member
	 * @param concernDTO
	 */
	void member2ConcernDTO(Member member, ApiMemberConcernDTO concernDTO)
	{
		BeanUtils.copyProperties(member, concernDTO);
		
		concernDTO.setAvatarAbs(member.getAvatar());
		concernDTO.setAvatarThumbnailAbs(member.getAvatarThumbnail());
		
		concernDTO.setAge(calcAge(member.getBirthday()));
		
        if(member.getLtid()!=null)
        {
        	String licenseName =
        			psychoInfoService.getLicenseTypeByPrimaryKey(member.getLtid()).getLicenseName();
        	concernDTO.setLicenseName(licenseName);
        }
	}
	
	/**
	 * 获取会员关注
	 * @param mid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/obtainConcernList.json")
	@ResponseBody
	public Object obtainConcernList(Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				mid, 
				pageIndex,
				pageSize
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		
		
		List<ApiMemberConcernDTO> concernDTOs = new ArrayList<ApiMemberConcernDTO>();
		Integer count = 0;
		try{
			List<MemberConcern> concerns = memberConcernService.obtainConcern(mid, pageIndex, pageSize);
			for(MemberConcern mc : concerns)
			{
				ApiMemberConcernDTO concernDTO = new ApiMemberConcernDTO();
				Member member = memberService.selectMemberByMid(mc.getConcernTo());
				if(member == null)
				{//会员资料被删除，小概率事件
					memberConcernService.deleteConcern(mc.getMemberConcernId());
					continue;
				}
				member2ConcernDTO(member, concernDTO);
				
				concernDTO.setIsConcern(0);
				
				concernDTOs.add(concernDTO);
			}
			
			count = memberConcernService.countConcernNum(mid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.put("concernDTOs", concernDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	
	/**
	 * 获取会员关注
	 * @param mid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/obtainConcernFromViewList.json")
	@ResponseBody
	public Object obtainConcernFromViewList(Long fromMid,Long toMid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				fromMid,
				toMid,
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		
		
		List<ApiMemberConcernDTO> concernDTOs = new ArrayList<ApiMemberConcernDTO>();
		Integer count = 0;
		try{
			List<MemberConcern> toConcerns = memberConcernService.obtainConcern(toMid, pageIndex, pageSize);
			List<MemberConcern> fromConcerns = memberConcernService.obtainConcern(fromMid, pageIndex, pageSize);
			for(MemberConcern mc : toConcerns){
				//用户是否关注标志
				boolean flag=true;
				
				ApiMemberConcernDTO concernDTO = new ApiMemberConcernDTO();
				Member member = memberService.selectMemberByMid(mc.getConcernTo());
				if(member == null){//会员资料被删除，小概率事件
					memberConcernService.deleteConcern(mc.getMemberConcernId());
					continue;
				}else{
					for(MemberConcern fmc: fromConcerns){
						if(fmc.getConcernTo().equals(mc.getConcernTo())){
							//用户已关注该用户
							concernDTO.setIsConcern(0);
							flag=false;
							break;
						}
					}
					//说明用户没有关注该用户
					if(flag){
						concernDTO.setIsConcern(1);
					}
				}
				
				member2ConcernDTO(member, concernDTO);
				
				concernDTOs.add(concernDTO);
			}
			
			count = memberConcernService.countConcernNum(toMid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.put("concernDTOs", concernDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	

	/**
	 * 获取用户的粉丝
	 * @param mid 会员id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/obtainFansList.json")
	@ResponseBody
	public Object obtainFansList(Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				mid, 
				pageIndex,
				pageSize
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		
		
		List<ApiMemberConcernDTO> fansDTOs = new ArrayList<ApiMemberConcernDTO>();
		Integer count = 0;
		try{
			List<MemberConcern> fans = memberConcernService.obtainFans(mid, pageIndex, pageSize);
			for(MemberConcern mc : fans)
			{
				if(mc.getIsRead()==0)
				{//设置已读
					memberConcernService.setReaded(mc.getMemberConcernId());
				}
				
				ApiMemberConcernDTO fansDTO = new ApiMemberConcernDTO();
				Member member = memberService.selectMemberByMid(mc.getConcernFrom());
				if(member == null)
				{//会员资料被删除，小概率事件
					memberConcernService.deleteConcern(mc.getMemberConcernId());
					continue;
				}
				member2ConcernDTO(member, fansDTO);
				
				fansDTO.setIsConcern(memberConcernService.checkConcern(mid, mc.getConcernFrom())?0:1);
				
				fansDTO.setIsRead(mc.getIsRead());
				
				fansDTOs.add(fansDTO);
			}
			
			count = memberConcernService.countFansNum(mid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.put("fansDTOs", fansDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	/**
	 * 获取用户的粉丝
	 * @param mid 会员id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/obtainFansFromViewList.json")
	@ResponseBody
	public Object obtainFansFromViewList(Long fromMid,Long toMid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				fromMid, 
				toMid,
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		
		
		List<ApiMemberConcernDTO> fansDTOs = new ArrayList<ApiMemberConcernDTO>();
		Integer count = 0;
		try{
			List<MemberConcern> toFans = memberConcernService.obtainFans(toMid, pageIndex, pageSize);
			List<MemberConcern> fromConcerns = memberConcernService.obtainAllConcern(fromMid);
			
			for(MemberConcern tf : toFans){
				//用户是否关注标志
				boolean flag=true;
				
				ApiMemberConcernDTO fansDTO = new ApiMemberConcernDTO();
				Member member = memberService.selectMemberByMid(tf.getConcernFrom());
				if(member == null){//会员资料被删除，小概率事件
					memberConcernService.deleteConcern(tf.getMemberConcernId());
					continue;
				}else{
					for(MemberConcern ff: fromConcerns){
						if(ff.getConcernTo().equals(tf.getConcernFrom())){
							//用户已关注该用户
							fansDTO.setIsConcern(0);
							flag=false;
							break;
						}
					}
					//说明用户没有关注该用户
					if(flag){
						fansDTO.setIsConcern(1);
					}
				}
				
				member2ConcernDTO(member, fansDTO);
				
				fansDTOs.add(fansDTO);
			}
			
			count = memberConcernService.countFansNum(toMid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.put("fansDTOs", fansDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	
	
	
	/**
	 * 获取未读粉丝数量
	 * @param mid
	 * @return
	 */
	@RequestMapping(value = "/obtainUnreadFansCount.json")
	@ResponseBody
	public Object obtainUnreadFansCount(Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(
				mid
				))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}
		
		Integer count;
		try{
			count = memberConcernService.countUnreadFansNum(mid);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
