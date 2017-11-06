package com.depression.controller.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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

import com.alibaba.fastjson.JSON;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.entity.VoipUser;
import com.depression.model.Member;
import com.depression.model.MemberTag;
import com.depression.model.NeteaseUinfoEx;
import com.depression.model.web.dto.WebMemberDTO;
import com.depression.model.web.dto.WebMemberTagDTO;
import com.depression.model.web.dto.WebPsychoDTO;
import com.depression.service.MemberService;
import com.depression.service.PunishmentService;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;
import com.depression.utils.MD5Util;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/depression/member")
public class MemberController
{
    Logger log = Logger.getLogger(this.getClass());
	@Autowired
	MemberService memberService;
	@Autowired
	PunishmentService punishmentService;
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addMember.json")
	@ResponseBody
	public Object addMember(HttpSession session, HttpServletRequest request, Member member)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			if (null == member)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			Member m = new Member();
			m.setMobilePhone(member.getMobilePhone());

			Integer preemption = memberService.insertPreemption(member.getMobilePhone());
			if (0 == preemption)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("该手机号码已存在");
				return result;
			}

			Member queryMember = memberService.selectMemberByMobilePhone(member.getMobilePhone());

			// 设置用户为正式会员
			member.setIsTemp((byte) 0);
			member.setMid(queryMember.getMid());
			member.setUserPassword(MD5Util.getMD5String(member.getUserPassword()));

			// 注册网易im账号
			String icon = member.getAvatar()==null?"":member.getAvatar();
			VoipUser voipUser = IMUtil.neteaseUserCreate(member.getNickname(), icon, null);
			if (null == voipUser)
			{
				member.setImAccount("");
				member.setImPsw("");
			}else
			{
				member.setImAccount(voipUser.getVoipAccount());
				member.setImPsw(voipUser.getVoipPwd());
			}

			memberService.update(member);
			
			//网易名片
			NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
			uinfoEx.setMid(member.getMid());
			uinfoEx.setUserType(member.getUserType()==null?1:member.getUserType());
			uinfoEx.setTitle(member.getTitle()==null?"":member.getTitle());
			IMUtil.neteaseUserUpdateUinfo(member.getImAccount(), null, null, JSON.toJSONString(uinfoEx));

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("注册成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户注册接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateMember.json")
	@ResponseBody
	public Object updateMember(HttpSession session, HttpServletRequest request, Member member)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == member || null == member.getMid())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			if (!StringUtils.isEmpty(member.getMobilePhone()))
			{
				Member tempMember = new Member();
				tempMember.setMobilePhone(member.getMobilePhone());
				Member tempQueryMember = memberService.selectMemberByMobilePhone(member.getMobilePhone());
				if (null != tempQueryMember && !member.getMid().equals(tempQueryMember.getMid()))
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("您修改的用户信息已存在");
					return result;
				}
			}

			Member queryMember = memberService.selectMemberByMid(member.getMid());
			Integer ret = -1;
			// 修改密码
			if (null != queryMember && !member.getUserPassword().equals(queryMember.getUserPassword()))
			{
				member.setUserPassword(MD5Util.getMD5String(member.getUserPassword()));
			}
			// 修改其他用户信息
			ret = memberService.update(member);
			
			//修改网易im信息
			if(member.getAvatar()!=null || member.getNickname()!=null)
			{
				Member m = memberService.selectMemberByMid(member.getMid());
				String icon = m.getAvatar()==null?"":m.getAvatar();
				IMUtil.neteaseUserUpdateUinfo(m.getImAccount(), m.getNickname(), icon, null);
			}

			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("更新失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("更新成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用用户更新接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllMember.json")
	@ResponseBody
	public Object getAllMember(HttpSession session, HttpServletRequest request,Integer pageIndex, Integer pageSize, Integer byRegTime)
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
		
		try
		{
			int count = memberService.getUserCount();
			List<Member> memberLists ;
			if(byRegTime!=null && byRegTime==1)
			{
				memberLists = memberService.getUsersOrderRegTimeWithPageDesc(pageIndex, pageSize);
			}else if(byRegTime!=null && byRegTime==0){
				memberLists = memberService.getUsersOrderRegTimeWithPageAsc(pageIndex, pageSize);
			}else{			
				memberLists = memberService.getUsersOrderAnswerCountWithPage(pageIndex, pageSize);
			}
			for (Member curMember : memberLists)
			{
				// 用户密码不返回
				curMember.setUserPassword("");
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setCount(count);
			result.setList(memberLists);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取所有用户接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMember.json")
	@ResponseBody
	public Object getMember(HttpSession session, HttpServletRequest request, String mobilePhone)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			Integer pageIndex = request.getParameter("pageIndex") == null ? null : Integer.parseInt(request.getParameter("pageIndex"));
			Integer pageSize = request.getParameter("pageSize") == null ? null : Integer.parseInt(request.getParameter("pageSize"));
			Member member = new Member();
			member.setPageIndex(pageIndex);
			member.setPageSize(pageSize);

			Integer count = 0;
			List<Member> memberLists;

			if (!StringUtils.isEmpty(mobilePhone))
			{
				member.setMobilePhone(mobilePhone);

				count = memberService.selectByMemberCount(member);
				memberLists = memberService.selectByMember(member);
				if (null == memberLists || 0 == memberLists.size())
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("查找数据为空");
					return result;
				}
			} else
			{
				count = memberService.selectCount();
				memberLists = memberService.selectByPage(member);
				if (null == memberLists || 0 == memberLists.size())
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("查找数据为空");
					return result;
				}
			}

			for (Member curMember : memberLists)
			{
				// 用户密码不返回
				curMember.setUserPassword("");
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setCount(count);
			result.put("list", memberLists);
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取用户接口失败");
			return result;
		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/searchMember.json")
	@ResponseBody
	public Object searchMember(HttpSession session, HttpServletRequest request, 
			Integer pageIndex, Integer pageSize, String words, Integer regTimeDirection, 
			Date begin, Date end,Byte hasMobile)
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
		
		List<WebMemberDTO> memberDTOs = new ArrayList<WebMemberDTO>();
		Integer count;
		try{
			//模糊搜索
			List<Member> members = memberService.searchUsers(words, pageIndex, pageSize, regTimeDirection, begin, end, hasMobile);
			for (Member m : members)
			{
				WebMemberDTO memberDTO = new WebMemberDTO(); 
				BeanUtils.copyProperties(m, memberDTO);
				memberDTO.setDisableMessageDays(punishmentService.obtainDisableMessageDays(m.getMid()));
			
				memberDTOs.add(memberDTO);
			}
			
			count = memberService.countSearchUsers(words, begin, end, hasMobile);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
				
		
		result.put("members", memberDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getMemberListByTimeSlice.json")
	@ResponseBody
	public Object getMemberListByTimeSlice(HttpSession session, HttpServletRequest request, 
			Integer pageIndex, Integer pageSize, Date begin, Date end)
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
		
		List<Member> members;
		Integer count;
		try{
			members = memberService.selectUsersByTimeSliceWithPage(pageIndex, pageSize, begin, end);
			count = memberService.countUsersByTimeSlice(begin, end);
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		for (Member curMember : members)
		{
			// 用户密码不返回
			curMember.setUserPassword("");
		}
		
		result.put("members", members);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

}
