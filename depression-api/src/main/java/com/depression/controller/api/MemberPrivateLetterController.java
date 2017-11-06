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
import com.depression.model.MemberPrivateLetter;
import com.depression.service.MemberPrivateLetterService;

@Controller
@RequestMapping("/depression/api")
public class MemberPrivateLetterController
{

	@Autowired
	MemberPrivateLetterService memberPrivateLetterService;

	@RequestMapping(method = RequestMethod.GET, value = "/addMemberPrivateLetter.json")
	@ResponseBody
	public Object addMemberPrivateLetter(HttpSession session, HttpServletRequest request, MemberPrivateLetter memberPrivateLetter)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberPrivateLetter)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端 传入的数据为空");
				return result;
			}

			memberPrivateLetter.setIsDelete(0);
			int ret = memberPrivateLetterService.insert(memberPrivateLetter);
			if (ret <= 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("私信插入失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("插入成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用私信插入接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getMemberPrivateLetter.json")
	@ResponseBody
	public Object getMemberPrivateLetter(HttpSession session, HttpServletRequest request, MemberPrivateLetter memberPrivateLetter)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == memberPrivateLetter)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取客户端 传入的数据为空");
				return result;
			}

			long count = memberPrivateLetterService.selectCount(memberPrivateLetter);
			List<MemberPrivateLetter> mplLists = memberPrivateLetterService.selectByPage(memberPrivateLetter);
			if (null == mplLists || 0 == mplLists.size())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("查找私信数据为空");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("list", mplLists);
			result.setCount(Integer.parseInt(count + ""));
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用私信插入接口失败");
			return result;
		}

	}

}
