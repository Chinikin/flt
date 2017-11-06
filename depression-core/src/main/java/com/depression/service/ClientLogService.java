package com.depression.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ClientLogMapper;
import com.depression.model.ClientLog;

@Service
public class ClientLogService
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	ClientLogMapper clientLogMapper;
	
	/**
	 * 新建客户端日志
	 * @param type
	 * @param marking
	 * @param log
	 * @return
	 */
	public Integer newClientLog(Byte type, String marking, String log)
	{
		ClientLog clientLog = new ClientLog();
		clientLog.setType(type);
		clientLog.setMarking(marking);
		clientLog.setLog(log);
		clientLog.setCreateTime(new Date());
		
		return clientLogMapper.insertSelective(clientLog);
	}
}
