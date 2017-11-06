package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.SystemOperationLogMapper;
import com.depression.model.SystemOperationLog;
import com.depression.model.web.dto.WebSystemOperationLogDTO;
import com.depression.model.web.vo.WebSystemOperationLogVO;

/**
 * 操作日志
 */
@Service
public class SystemOperationLogService
{
	@Autowired
	SystemOperationLogMapper systemOperationLogMapper;

	public int insertSelective(SystemOperationLog record)
	{
		return systemOperationLogMapper.insertSelective(record);
	}
	
	public List<WebSystemOperationLogDTO> selectOperationLogAndUserInfoWithPage(WebSystemOperationLogVO record)
	{
		return systemOperationLogMapper.selectOperationLogAndUserInfoWithPage(record);
	}

	public int countOperationLogAndUserInfo(WebSystemOperationLogVO record)
	{
		return systemOperationLogMapper.countOperationLogAndUserInfo(record);
	}
}
