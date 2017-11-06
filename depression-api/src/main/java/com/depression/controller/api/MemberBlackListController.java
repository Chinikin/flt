package com.depression.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.MemberBlackList;
import com.depression.service.MemberBlackListService;

/**
 * @author:ziye_huang
 * @date:2016年5月9日
 */

@Controller
@RequestMapping("/depression/api")
public class MemberBlackListController
{
	@Autowired
	MemberBlackListService memberBlackListService;

	@RequestMapping(method = RequestMethod.POST, value = "/addMemberBlackList.json")
	@ResponseBody
	public Object addMemberBlackList(HttpSession session, HttpServletRequest request, MemberBlackList memberBlackList)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			if (null == memberBlackList)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			if (StringUtils.isEmpty(memberBlackList.getMid()) || StringUtils.isEmpty(memberBlackList.getBlackMid()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传来的会员id为空");
				return result;
			}

			MemberBlackList mb = memberBlackListService.getMemberBlackList(memberBlackList);
			if (null != mb)
			{
				if (1 == mb.getIsDelete())
				{
					Integer ret = memberBlackListService.update(memberBlackList);
					if (ret != 1)
					{
						result.setCode(ResultEntity.ERROR);
						result.setError("加入黑名单失败");
						return result;
					}
					result.setCode(ResultEntity.SUCCESS);
					result.setMsg("加入黑名单成功");
					return result;
				}
				result.setCode(ResultEntity.ERROR);
				result.setError("该账号已在黑名单");
				return result;
			}

			Integer ret = memberBlackListService.insert(memberBlackList);
			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("加入黑名单失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("加入黑名单成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用加入黑名单接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteMemberBlackList.json")
	@ResponseBody
	public Object deleteMemberBlackList(HttpSession session, HttpServletRequest request, MemberBlackList memberBlackList)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			if (null == memberBlackList)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			if (StringUtils.isEmpty(memberBlackList.getMid()) || StringUtils.isEmpty(memberBlackList.getBlackMid()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传来的会员id为空");
				return result;
			}

			MemberBlackList mb = memberBlackListService.getMemberBlackList(memberBlackList);
			if (null == mb)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("该会员信息不存在");
				return result;
			}

			Integer ret = memberBlackListService.delete(memberBlackList);
			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("删除黑名单失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("删除黑名单成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用删除黑名单接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllMemberBlackList.json")
	@ResponseBody
	public Object getAllMemberBlackList(HttpSession session, HttpServletRequest request, MemberBlackList memberBlackList)
	{
		ResultEntity result = new ResultEntity();
		try
		{

			if (null == memberBlackList)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			if (StringUtils.isEmpty(memberBlackList.getMid()) || StringUtils.isEmpty(memberBlackList.getBlackMid()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端传来的会员id为空");
				return result;
			}

			List<MemberBlackList> mbLists = memberBlackListService.findAllBlackListByMid(memberBlackList);
			if (null == mbLists || 0 == mbLists.size())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("查找该会员黑名单为空");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("list", mbLists);
			result.setCount(mbLists.size());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用查找所有黑名单接口失败");
			return result;
		}

	}

}
