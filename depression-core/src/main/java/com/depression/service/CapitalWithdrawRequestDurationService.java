package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalWithdrawRequestDurationMapper;
import com.depression.model.CapitalWithdrawRequestDuration;

/**
 * 提现申请日期服务层
 * 
 * @author hongqian_li
 * @date 2016/08/28
 */
@Service
public class CapitalWithdrawRequestDurationService
{
	@Autowired
	CapitalWithdrawRequestDurationMapper mCapitalWithdrawRequestDurationMapper;

	/**
	 * 
	 * 
	 * @param entity
	 * @return
	 */
	public List<CapitalWithdrawRequestDuration> selectAll(CapitalWithdrawRequestDuration record)
	{
		List<CapitalWithdrawRequestDuration> cwrds = mCapitalWithdrawRequestDurationMapper.selectSelective(record);
		if(cwrds.size()==0)
		{
			CapitalWithdrawRequestDuration entity = new CapitalWithdrawRequestDuration();
			entity.setBeginDate(1);
			entity.setEndDate(5);
			mCapitalWithdrawRequestDurationMapper.insertSelective(entity);
			cwrds = mCapitalWithdrawRequestDurationMapper.selectSelective(record);
		}
		return cwrds;
	}
	
	/**
	 * 根据主键查找
	 * 
	 * @mbggenerated Sat Aug 27 14:35:49 CST 2016
	 */
	public CapitalWithdrawRequestDuration selectByPrimaryKey(Long wrdid)
	{
		return mCapitalWithdrawRequestDurationMapper.selectByPrimaryKey(wrdid);
	}

	/**
	 * 更新数据
	 * 
	 * @param record
	 * @return
	 */
	public int update(CapitalWithdrawRequestDuration record)
	{
		return mCapitalWithdrawRequestDurationMapper.updateByPrimaryKey(record);
	}

	/**
	 * 设计只有一条
	 * @return
	 */
	public CapitalWithdrawRequestDuration getTheOnlyOne()
	{
		CapitalWithdrawRequestDuration record = new CapitalWithdrawRequestDuration();
		List<CapitalWithdrawRequestDuration> cwrds = mCapitalWithdrawRequestDurationMapper.selectSelective(record);
		if(cwrds.size()==0)
		{
			CapitalWithdrawRequestDuration entity = new CapitalWithdrawRequestDuration();
			entity.setBeginDate(1);
			entity.setEndDate(5);
			mCapitalWithdrawRequestDurationMapper.insertSelective(entity);
			cwrds = mCapitalWithdrawRequestDurationMapper.selectSelective(record);
		}
		
		return cwrds.get(0);
	}
}
