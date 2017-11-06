package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalCommissionRateMapper;
import com.depression.model.CapitalCommissionRate;
import com.depression.model.CapitalWithdrawRequestDuration;

@Service
public class CapitalCommissionRateService
{
	@Autowired
	private CapitalCommissionRateMapper capitalCommissionRateMapper;

	public CapitalCommissionRate selectSelective(CapitalCommissionRate record)
	{
		List<CapitalCommissionRate> rateList = capitalCommissionRateMapper.selectSelective(record);
		if (rateList != null && rateList.size() > 0)
		{
			return rateList.get(0);
		} else
		{
			return null;
		}
	}

	public List<CapitalCommissionRate> selectAll(CapitalCommissionRate record)
	{
		return capitalCommissionRateMapper.selectSelective(record);
	}

	/**
	 * 新增一条数据
	 * 
	 * @mbggenerated Sat Aug 27 14:35:49 CST 2016
	 */
	public int insert(CapitalCommissionRate record)
	{
		return capitalCommissionRateMapper.insert(record);
	}

	/**
	 * 根据主键查找
	 * 
	 * @mbggenerated Sat Aug 27 14:35:49 CST 2016
	 */
	public CapitalCommissionRate selectByPrimaryKey(Long crid)
	{
		return capitalCommissionRateMapper.selectByPrimaryKey(crid);
	}

	/**
	 * 更新数据
	 * 
	 * @param record
	 * @return
	 */
	public int update(CapitalCommissionRate record)
	{
		return capitalCommissionRateMapper.updateByPrimaryKey(record);
	}

	/**
	 * 查询条数
	 * 
	 * @param record
	 * @return
	 */
	public int selectCount(CapitalCommissionRate record)
	{
		return capitalCommissionRateMapper.countSelective(record);
	}

}
