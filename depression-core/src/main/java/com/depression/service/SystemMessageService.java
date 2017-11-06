package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MessageConcornMapper;
import com.depression.dao.SystemMessageDAO;
import com.depression.model.MessageConcorn;
import com.depression.model.SystemMessage;

/**
 * @author:ziye_huang
 * @date:2016年5月10日
 */

@Service
public class SystemMessageService
{
	@Autowired
	SystemMessageDAO systemMessageDAO;
	@Autowired
	MessageConcornMapper messageConcornMapper;
	@Autowired
	MemberService memberService;

	public Integer insert(SystemMessage systemMessage)
	{
		systemMessage.setIsDelete(0);
		systemMessage.setSendTime(new Date());
		systemMessage.setReadStatus(0);
		return systemMessageDAO.insert(systemMessage);
	}

	public Integer delete(SystemMessage systemMessage)
	{
		systemMessage.setIsDelete(1);
		return systemMessageDAO.delete(systemMessage);
	}

	public Integer update(SystemMessage systemMessage)
	{
		return systemMessageDAO.update(systemMessage);
	}

	public List<SystemMessage> getSystemMessage(SystemMessage systemMessage)
	{
		List<SystemMessage> smList = systemMessageDAO.getSystemMessage(systemMessage);
		for(SystemMessage sm : smList)
		{
			if(sm.getType()==SystemMessage.TYPE_CONCORN||sm.getType()==SystemMessage.TYPE_UNCONCORN)
			{
				MessageConcorn mc = messageConcornMapper.selectByPrimaryKey(sm.getRefId());
				memberService.fillMemberBasic(mc.getMid(), mc);
				sm.setRefObj(mc);
			}
		}
		return smList;
	}
	
	public Long getSystemMessageCount(SystemMessage systemMessage)
	{
		return systemMessageDAO.getSystemMessageCount(systemMessage);
	}
	
	public int setSystemMessageRead(Long messageId)
	{
		SystemMessage systemMessage = new SystemMessage();
		systemMessage.setMessageId(messageId);
		systemMessage.setReadStatus(1);
		return systemMessageDAO.update(systemMessage);
	}
	
	private int addSystemMessage(Integer type, Long refId, Long mid)
	{
		SystemMessage sm = new SystemMessage();
		sm.setIsDelete(0);
		sm.setMid(mid);
		sm.setSendTime(new Date());
		sm.setReadStatus(0);
		sm.setRefId(refId);
		sm.setType(type);
		return systemMessageDAO.insert(sm);
	}
	
	public int  addMessageConcorn(Long fromId, Long toId)
	{
		//创建关注消息记录
		MessageConcorn mc = new MessageConcorn();
		mc.setMid(fromId);
		mc.setIsDelete("0");
		messageConcornMapper.insertSelective(mc);
		
		//创建系统消息记录
		return addSystemMessage(SystemMessage.TYPE_CONCORN, mc.getMessageId(), toId);
	}

	public int  addMessageUnconcorn(Long fromId, Long toId)
	{
		//创建关注消息记录
		MessageConcorn mc = new MessageConcorn();
		mc.setMid(fromId);
		mc.setIsDelete("0");
		messageConcornMapper.insertSelective(mc);
		
		//创建系统消息记录
		return addSystemMessage(SystemMessage.TYPE_UNCONCORN, mc.getMessageId(), toId);
	}
	
}
