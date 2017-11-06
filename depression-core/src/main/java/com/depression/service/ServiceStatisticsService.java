package com.depression.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.EapEmployeeMapper;
import com.depression.dao.EapEnterpriseMapper;
import com.depression.dao.PsychoGroupMemberMapper;
import com.depression.dao.ServiceCustomerStatisticsMapper;
import com.depression.dao.ServicePsychoStatisticsMapper;
import com.depression.model.EapEmployee;
import com.depression.model.EapEnterprise;
import com.depression.model.ServiceCustomerStatistics;
import com.depression.model.ServicePsychoStatistics;

@Service
public class ServiceStatisticsService
{
	@Autowired
	ServicePsychoStatisticsMapper psychoStatMapper;
	@Autowired
	ServiceCustomerStatisticsMapper customerStatMapper;
	@Autowired
	EapEmployeeMapper eapEmployeeMapper;
	@Autowired
	EapEnterpriseMapper eapEnterpriseMapper;
	
	public static int ORDER_TYPE_COMMON = 0;
	public static int ORDER_TYPE_EAP = 1;
	public static int ORDER_TYPE_ACTIVE = 2;
	
	/**
	 * 获取咨询师服务统计，没有则创建一个
	 * @param mid 咨询师会员id
	 * @return
	 */
	public ServicePsychoStatistics getOrCreatePsychoStat(Long mid)
	{
		ServicePsychoStatistics sps0 = new ServicePsychoStatistics();
		sps0.setMid(mid);
		
		List<ServicePsychoStatistics> spss = psychoStatMapper.selectSelective(sps0);
		if(spss.size()==0)
		{
			sps0.setCreateTime(new Date());
			psychoStatMapper.insertSelective(sps0);
			
			ServicePsychoStatistics sps1 = new ServicePsychoStatistics();
			sps1.setMid(mid);
			spss = psychoStatMapper.selectSelective(sps1);
		}
		
		return spss.get(0);
	}
	
	/**
	 * 获取用户服务统计，没有则创建一个
	 * @param mid 用户会员id
	 * @return
	 */
	public ServiceCustomerStatistics getOrCreateCustomerStat(Long mid)
	{
		ServiceCustomerStatistics scs0 = new ServiceCustomerStatistics();
		scs0.setMid(mid);
		
		List<ServiceCustomerStatistics> scss = customerStatMapper.selectSelective(scs0);
		if(scss.size()==0)
		{
			scs0.setCreateTime(new Date());
			customerStatMapper.insertSelective(scs0);
			
			ServiceCustomerStatistics scs1 = new ServiceCustomerStatistics();
			scs1.setMid(mid);
			scss = customerStatMapper.selectSelective(scs1);
		}
		
		return scss.get(0);
	}
	
	
	/**
	 * 获取用户服务统计，没有则创建一个
	 * @param mid 用户会员id
	 * @return
	 */
	public ServiceCustomerStatistics getOrCreateCustomerStatV1(Long mid,Long eeId)
	{
		ServiceCustomerStatistics scs0 = new ServiceCustomerStatistics();
		scs0.setMid(mid);
		scs0.setEeId(eeId);
		List<ServiceCustomerStatistics> scss = customerStatMapper.selectSelective(scs0);
		if(scss.size()==0)
		{
			scs0.setCreateTime(new Date());
			customerStatMapper.insertSelective(scs0);
			
			ServiceCustomerStatistics scs1 = new ServiceCustomerStatistics();
			scs1.setMid(mid);
			scss = customerStatMapper.selectSelective(scs1);
		}
		
		return scss.get(0);
	}
	
	/**
	 * 更新咨询师咨询时长统计
	 * @param mid	咨询师会员id
	 * @param duration	本次咨询时长
	 * @param type	本次咨询类型
	 */
	public Integer updatePsychoDuration(Long mid, Long duration, Integer type)
	{
		ServicePsychoStatistics sps = getOrCreatePsychoStat(mid);
		if(type.equals(ORDER_TYPE_EAP))
		{
			sps.setEapAdvisoryDuration(sps.getEapAdvisoryDuration() + duration);
		}
		if(type.equals(ORDER_TYPE_ACTIVE)){
			sps.setTotalActivityDuration(sps.getTotalActivityDuration()+duration);
		}
		if(type.equals(ORDER_TYPE_COMMON)){
		sps.setTotalAdvisoryDuration(sps.getTotalAdvisoryDuration() + duration);
		}
		return psychoStatMapper.updateByPrimaryKeySelective(sps);
	}
	
	/**
	 * 更新咨询师咨询次数统计
	 * @param mid	咨询师会员id
	 * @param type	本次咨询类型
	 */
	public Integer updatePsychoTimes(Long mid, Integer type)
	{
		ServicePsychoStatistics sps = getOrCreatePsychoStat(mid);
		if(type.equals(ORDER_TYPE_EAP))
		{
			sps.setEapAdvisoryTimes(sps.getEapAdvisoryTimes() + 1);
		}
		
		sps.setTotalAdvisoryTimes(sps.getTotalAdvisoryTimes() + 1);
		
		return psychoStatMapper.updateByPrimaryKeySelective(sps);
	}
	
	/**
	 * 更新咨询师无效咨询次数，来自订单评价
	 * @param mid
	 * @return
	 */
	public Integer updatePsychoInvalidTimes(Long mid)
	{
		ServicePsychoStatistics sps = getOrCreatePsychoStat(mid);
		
		sps.setInvalidAdvisoryTimes(sps.getInvalidAdvisoryTimes() + 1);
		
		return psychoStatMapper.updateByPrimaryKeySelective(sps);
	}
	
	/**
	 * 更新咨询师服务评分
	 * @param mid 咨询师会员id
	 * @param score  本次打分
	 * @param type  本次咨询类型
	 * @return
	 */
	public Integer updatePsychoScore(Long mid, Integer score, Integer type)
	{
		ServicePsychoStatistics sps = getOrCreatePsychoStat(mid);
		if(type.equals(ORDER_TYPE_EAP))
		{
			Double oldAvgScore = sps.getEapScore().doubleValue();
			Double newAvgScore = (oldAvgScore*sps.getEapScoreTimes() + score) /
					(sps.getEapScoreTimes() + 1);
			sps.setEapScore(BigDecimal.valueOf(newAvgScore));
			sps.setEapScoreTimes(sps.getEapScoreTimes() + 1);
		}else
		{		
			Double oldAvgScore = sps.getCommonScore().doubleValue();
			Double newAvgScore = (oldAvgScore*sps.getCommonScoreTimes() + score) /
					(sps.getCommonScoreTimes() + 1);
			sps.setCommonScore(BigDecimal.valueOf(newAvgScore));
			sps.setCommonScoreTimes(sps.getCommonScoreTimes() + 1);
		}
		
		return psychoStatMapper.updateByPrimaryKeySelective(sps);
	}
	
	/**
	 * 更新咨询师在线时长
	 * @param mid
	 * @param sec
	 * @return
	 */
	public Integer updatePsychoOnlineDuration(Long mid, Long sec)
	{
		ServicePsychoStatistics sps = getOrCreatePsychoStat(mid);
		sps.setOnlineDuration(sps.getOnlineDuration() + sec);
		
		return psychoStatMapper.updateByPrimaryKeySelective(sps);
	}
	
	/**
	 * 更新用户咨询时长
	 * @param mid	用户会员id
	 * @param duration	时长
	 * @param type	本次咨询时长
	 * @param eeId	企业id
	 * @return
	 */
	public Integer updateCutomerDuration(Long mid, Long duration, Integer type, Long eeId)
	{
		Integer ret = 0;
		ServiceCustomerStatistics scs = getOrCreateCustomerStat(mid);
		
		if(type.equals(ORDER_TYPE_EAP))
		{
			scs.setEapAdvisoryDuration(scs.getEapAdvisoryDuration()+duration);
			
			List<EapEmployee> eems = eapEmployeeMapper.selectEmployeeByMidAndEeId(mid, eeId);
			if(eems.size() > 0)
			{
				EapEmployee eem = eems.get(0);
				eem.setEapAdvisoryDuration(eem.getEapAdvisoryDuration()+duration);
				ret += eapEmployeeMapper.updateByPrimaryKeySelective(eem);
			}
		}
		if(type.equals(ORDER_TYPE_ACTIVE)){
			scs.setTotalActivityDuration(scs.getTotalActivityDuration()+duration);
		}
		if(type.equals(ORDER_TYPE_COMMON)){
		scs.setTotalAdvisoryDuration(scs.getTotalAdvisoryDuration()+duration);
		}
		ret += customerStatMapper.updateByPrimaryKeySelective(scs);
		
		return ret;
	}
	
	/**
	 * 更新用户咨询次数
	 * @param mid	用户会员id
	 * @param type	本次咨询时长
	 * @param eeId	企业id
	 * @return
	 */
	public Integer updateCutomerTimes(Long mid, Integer type, Long eeId)
	{
		Integer ret = 0;
		ServiceCustomerStatistics scs = getOrCreateCustomerStat(mid);
		
		if(type.equals(ORDER_TYPE_EAP))
		{
			scs.setEapAdvisoryTimes(scs.getEapAdvisoryTimes()+1);
			
			List<EapEmployee> eems = eapEmployeeMapper.selectEmployeeByMidAndEeId(mid, eeId);
			if(eems.size() > 0)
			{
				EapEmployee eem = eems.get(0);
				eem.setEapAdvisoryTimes(eem.getEapAdvisoryTimes()+1);
				ret += eapEmployeeMapper.updateByPrimaryKeySelective(eem);
			}
		}
		
		scs.setTotalAdvisoryTimes(scs.getTotalAdvisoryTimes()+1);
		
		ret += customerStatMapper.updateByPrimaryKeySelective(scs);
		
		return ret;
	}	
	
	/**
	 * 更新企业通话时长
	 * @param duration	时长
	 * @param type	本次通话类型
	 * @param eeId	企业id
	 * @return
	 */
	public Integer updateEnterpriseDuration(Long duration, Integer type, Long eeId)
	{
		Integer ret = 0;
		if(type.equals(ORDER_TYPE_EAP))
		{
			EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKeyLock(eeId);
			
			ee.setConsumedDuration(ee.getConsumedDuration()+duration);
			
			ret += eapEnterpriseMapper.updateByPrimaryKeySelective(ee);
		}
		
		return ret;
	}
	
	/**
	 * 更新企业通话次数
	 * @param type	本次通话类型
	 * @param eeId	企业id
	 * @return
	 */
	public Integer updateEnterpriseTimes(Integer type, Long eeId)
	{
		Integer ret = 0;
		if(type.equals(ORDER_TYPE_EAP))
		{
			EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKeyLock(eeId);
			
			ee.setConsumedOrderAmount(ee.getConsumedOrderAmount()+1);
			
			ret += eapEnterpriseMapper.updateByPrimaryKeySelective(ee);
		}
		
		return ret;
	}
	
	/**
	 * 增加企业提问回答数
	 * @param eeId
	 * @return
	 */
	public Integer updateEnterpriseAnswerCount(Long eeId)
	{
		EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKeyLock(eeId);
		ee.setAnswerCount(ee.getAnswerCount() + 1);
		
		return eapEnterpriseMapper.updateByPrimaryKeySelective(ee);
	}
	
	/**
	 * 增加企业提问回答数
	 * @param pid
	 * @return
	 */
	public Integer updateEnterpriseAnswerCountByPsycho(Long pid)
	{
		List<Long> eeIds = eapEnterpriseMapper.selectEeId4Psycho(pid);
		for(Long eeId : eeIds)
		{
			updateEnterpriseAnswerCount(eeId);
		}
		
		return eeIds.size();
	}
	
	/**
	 * 增加企业咨询师在线时长
	 * @param eeId
	 * @param sec
	 * @return
	 */
	public Integer updateEnterpriseOnlineDuration(Long eeId, Long sec)
	{
		EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKeyLock(eeId);
		ee.setOnlineDuration(ee.getOnlineDuration() + sec);
		
		return eapEnterpriseMapper.updateByPrimaryKeySelective(ee);
	}
	
	/**
	 * 增加企业咨询师在线时长
	 * @param pid
	 * @param sec
	 * @return
	 */
	public Integer updateEnterpriseOnlineDurationByPsycho(Long pid, Long sec)
	{
		List<Long> eeIds = eapEnterpriseMapper.selectEeId4Psycho(pid);
		for(Long eeId : eeIds)
		{
			updateEnterpriseOnlineDuration(eeId, sec);
		}
		
		return eeIds.size();
	}
	
	/**
	 * 更新咨询师和用户通话时长
	 * @param mid	用户会员id	
	 * @param pid	咨询师会员id
	 * @param duration	通话时长
	 * @param type	本次通话类型
	 * @param eeId	企业id
	 * @return
	 */
	public Integer updateDuration(Long mid, Long pid, Long duration, Integer type, Long eeId)
	{
		Integer ret = 0;
		ret += updatePsychoDuration(pid, duration, type);
		ret += updateCutomerDuration(mid, duration, type, eeId);
		ret += updateEnterpriseDuration(duration, type, eeId);
		return ret;
	}
	
	/**
	 * 更新咨询师和用户通话次数
	 * @param mid	用户会员id	
	 * @param pid	咨询师会员id
	 * @param type	本次通话类型
	 * @param eeId	企业id
	 * @return
	 */
	public Integer updateTimes(Long mid, Long pid, Integer type, Long eeId)
	{
		Integer ret = 0;
		ret += updatePsychoTimes(pid, type);
		ret += updateCutomerTimes(mid, type, eeId);
		ret += updateEnterpriseTimes(type, eeId);
		return ret;
	}
	
	
}
