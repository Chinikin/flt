package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ServiceCallRecordMapper;
import com.depression.model.ServiceCallRecord;

/**
 * 订单通话记录
 * 
 * @author 范新辉
 * 
 */
@Service
public class ServiceCallRecordService
{
	@Autowired
	private ServiceCallRecordMapper serviceCallRecordMapper;

	public int insertSelective(ServiceCallRecord record)
	{
		record.setCreateTime(new Date());
		return serviceCallRecordMapper.insertSelective(record);
	}

	public int updateByPrimaryKey(ServiceCallRecord record)
	{
		record.setModifyTime(new Date());
		return serviceCallRecordMapper.updateByPrimaryKey(record);
	}

	public List<ServiceCallRecord> selectSelective(ServiceCallRecord record)
	{
		return serviceCallRecordMapper.selectSelective(record);
	}

	public List<ServiceCallRecord> selectByDate(ServiceCallRecord record)
	{
		return serviceCallRecordMapper.selectByDate(record);
	}
}
