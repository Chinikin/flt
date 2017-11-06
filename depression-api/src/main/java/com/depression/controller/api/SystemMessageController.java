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
import com.depression.model.SystemMessage;
import com.depression.service.SystemMessageService;
import com.depression.service.UnreadCommentService;

/**
 * @author:ziye_huang
 * @date:2016年5月10日
 */
@Controller
@RequestMapping("/SystemMessage")
public class SystemMessageController
{
	@Autowired
	SystemMessageService systemMessageService;
	@Autowired
	UnreadCommentService unreadCommentService;

	@RequestMapping(method = RequestMethod.POST, value = "/addSystemMessage.json")
	@ResponseBody
	public Object addSystemMessage(HttpSession session, HttpServletRequest request, SystemMessage systemMessage)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == systemMessage)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			Integer ret = systemMessageService.insert(systemMessage);
			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("插入系统消息失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("插入系统消息成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用插入系统消息接口失败");
			return result;
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteSystemMessage.json")
	@ResponseBody
	public Object deleteSystemMessage(HttpSession session, HttpServletRequest request, SystemMessage systemMessage)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == systemMessage)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			Integer ret = systemMessageService.delete(systemMessage);
			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("删除系统消息失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("删除系统消息成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用删除系统消息接口失败");
			return result;
		}

	}

	@RequestMapping(value = "/readSystemMessage.json")
	@ResponseBody
	public Object readSystemMessage(HttpSession session, HttpServletRequest request, Long  messageId)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == messageId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			Integer ret = systemMessageService.setSystemMessageRead(messageId);
			if (ret != 1)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("更新系统消息失败");
				return result;
			}

			result.setCode(ResultEntity.SUCCESS);
			result.setMsg("更新系统消息成功");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用更新系统消息接口失败");
			return result;
		}

	}

	@RequestMapping(value = "/getSystemMessage.json")
	@ResponseBody
	public Object getSystemMessage(HttpSession session, HttpServletRequest request, SystemMessage systemMessage)
	{
		ResultEntity result = new ResultEntity();
		try
		{
			if (null == systemMessage)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取用户传入的数据为空");
				return result;
			}

			List<SystemMessage> smLists = systemMessageService.getSystemMessage(systemMessage);
//			if (null == smLists || 0 == smLists.size())
//			{
//				result.setCode(ResultEntity.ERROR);
//				result.setError("获取系统消息为空");
//				return result;
//			}

			result.setCode(ResultEntity.SUCCESS);
			result.put("list", smLists);
			result.setCount(systemMessageService.getSystemMessageCount(systemMessage).intValue());
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ResultEntity.ERROR);
			result.setError("调用获取系统消息接口失败");
			return result;
		}

	}
	
	@RequestMapping(value = "/getSystemMessageAndUnreadCommentCount.json")
	@ResponseBody
	public Object getSystemMessageAndUnreadCommentCount(HttpSession session, HttpServletRequest request, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if(mid == null)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("mid不能为空");
			return result;
		}
		
		SystemMessage systemMessage = new SystemMessage();
		systemMessage.setMid(mid);
		systemMessage.setReadStatus(0);
		systemMessage.setType(SystemMessage.TYPE_CONCORN);
		Long smCount = systemMessageService.getSystemMessageCount(systemMessage);
		
		Long ucCount = unreadCommentService.queryUnreadCommentCount(mid);
		
		result.put("systemMessageCount", smCount);
		result.put("unreadCommentCount", ucCount);
		result.setCode(ResultEntity.SUCCESS);
		return result;
		
	}

}
