package com.depression.dao;

import java.util.List;

import com.depression.model.SystemMessage;

/**
 * @author:ziye_huang
 * @date:2016年5月10日
 */

public interface SystemMessageDAO
{

	public Integer insert(SystemMessage systemMessage);

	public Integer delete(SystemMessage systemMessage);
	
	public Integer update(SystemMessage systemMessage);

	public List<SystemMessage> getSystemMessage(SystemMessage systemMessage);
	
	public Long getSystemMessageCount(SystemMessage systemMessage);

}
