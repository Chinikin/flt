package com.depression.controller.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.MemberSignin;
import com.depression.service.MemberService;
import com.depression.service.MemberSigninService;

@Controller
@RequestMapping("/MemberSignin")
public class MemberSigninController
{
	@Autowired
	MemberSigninService memberSigninService;

	@Autowired
	MemberService memberService;

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	private Integer calcBonusPoint4CurSignin()
	{
		return (int) (Math.random() * 9 + 1);
	}

	/**
	 * 会员今日签到
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @return
	 */
	@RequestMapping(value = "/addMemberSignin.json")
	@ResponseBody
	public Object addMemberSignin(HttpSession session, HttpServletRequest request, Long mid)
	{
		ResultEntity result = new ResultEntity();

		try
		{
			if (mid == null)
			{
				result.setError("会员ID不能为空");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			Member member = new Member();
			member.setMid(mid);

			Member qm = memberService.getMember(member);
			if (qm == null)
			{
				result.setError("没有该用户");
				result.setCode(ResultEntity.ERROR);
				return result;
			}

			MemberSignin ms = memberSigninService.getSigninByDate(mid, new Date());
			if (ms != null)
			{
				result.setError("今天已经签过到");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			ms = new MemberSignin();
			ms.setMid(mid);
			memberSigninService.addSignin(ms);

			Long bonusPoint = qm.getBonusPoint();
			qm.setBonusPoint(null == bonusPoint ? calcBonusPoint4CurSignin() : bonusPoint + calcBonusPoint4CurSignin());
			memberService.update(qm);

			result.setMsg("签到成功");
			result.put("bonusPoint", qm.getBonusPoint());
			result.setCode(ResultEntity.SUCCESS);
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}

		return result;
	}

	/**
	 * 获取某日会员签到
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param date
	 *            为空则默认为当日
	 * @return
	 */
	@RequestMapping(value = "/getMemberSigninByDate.json")
	@ResponseBody
	public Object getMemberSigninByDate(HttpSession session, HttpServletRequest request, Long mid, Date date)
	{
		ResultEntity result = new ResultEntity();

		try
		{
			if (mid == null)
			{
				result.setError("会员ID不能为空");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			if (date == null)
			{
				date = new Date();
			}
			MemberSignin ms = memberSigninService.getSigninByDate(mid, date);
			result.put("signin", ms);
			result.setCode(ResultEntity.SUCCESS);
			if (ms == null)
			{
				result.setMsg("未签到");
			} else
			{
				result.setMsg("已签到");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}

		return result;
	}

	/**
	 * 根据日期片段获取签到记录，开始时间和结束时间为封闭空间 若只有开始时间，则结束时间为今天 若只有结束时间，则开始时间为系统最早的记录时间
	 * 
	 * @param session
	 * @param request
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping(value = "/getMemberSigninByDateSlice.json")
	@ResponseBody
	public Object getMemberSigninByDateSlice(HttpSession session, HttpServletRequest request, Long mid, Date begin, Date end)
	{
		ResultEntity result = new ResultEntity();

		try
		{
			if (mid == null)
			{
				result.setError("会员ID不能为空");
				result.setCode(ResultEntity.ERROR);
				return result;
			}
			if (begin == null && end == null)
			{
				result.setError("开始时间和结束时间至少有一个");
				result.setCode(ResultEntity.ERROR);
				return result;
			}

			List<MemberSignin> msList = memberSigninService.getSigninByDateSlice(mid, begin, end);
			result.put("list", msList);
			result.setCode(ResultEntity.SUCCESS);

		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			return result;
		}
		return result;
	}

}
