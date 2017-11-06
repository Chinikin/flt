package com.depression.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.UbAccessRecordMapper;
import com.depression.model.UbAccessRecord;

@Service
public class UserBehaviorService
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	UbAccessRecordMapper accessRecordMapper;
	
	/**
	 * 创建用户访问记录
	 * @param accessRecord
	 * @return
	 */
	public Integer createAccessRecord(UbAccessRecord accessRecord)
	{
		accessRecord.setCreateTime(new Date());
		
		return accessRecordMapper.insertSelective(accessRecord);
	}
}
