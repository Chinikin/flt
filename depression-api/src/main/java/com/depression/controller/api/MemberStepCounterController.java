package com.depression.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.MemberStepCounter;
import com.depression.service.MemberStepCounterService;

/**
 * @author:ziye_huang
 * @date:2016年5月10日
 */
@Controller
@RequestMapping("/depression/api")
public class MemberStepCounterController
{
	@Autowired
	MemberStepCounterService memberStepCounterService;

	@RequestMapping(method = RequestMethod.POST, value = "/addMemberStepCounter.json")
	@ResponseBody
	public Object addMemberStepCounter(HttpSession session, HttpServletRequest request, MemberStepCounter memberStepCounter)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberStepCounter)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			List<MemberStepCounter> mscLists = memberStepCounterService.getMemberStepByParams(memberStepCounter);
			Integer ret = 0;
			if (null == mscLists || 0 == mscLists.size())
			{
				ret = memberStepCounterService.insert(memberStepCounter);
				if (ret != 1)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("插入失败");
					return result;
				}
			} else
			{
				MemberStepCounter msc = mscLists.get(0);
				msc.setAllStepCounter(memberStepCounter.getAllStepCounter());
				ret = memberStepCounterService.update(msc);
				if (ret != 1)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("更新失败");
					return result;
				}
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("添加成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用添加计步接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberStepCounter.json")
	@ResponseBody
	public Object deleteMemberStepCounter(HttpSession session, HttpServletRequest request, MemberStepCounter memberStepCounter)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberStepCounter)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			Integer ret = memberStepCounterService.delete(memberStepCounter);
			if (1 != ret)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("删除数据失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("删除成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用删除计步接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMemberStepCounter.json")
	@ResponseBody
	public Object getMemberStepCounter(HttpSession session, HttpServletRequest request, MemberStepCounter memberStepCounter)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberStepCounter)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			List<MemberStepCounter> mscLists = memberStepCounterService.getMemberStepByParams(memberStepCounter);
			if (null == mscLists || 0 == mscLists.size())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("查找数据为空");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("list", mscLists);
			result.setCount(mscLists.size());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用查找计步接口失败");
			return result;
		}

	}
}
