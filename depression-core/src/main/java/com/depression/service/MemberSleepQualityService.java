package com.depression.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberSleepQualityDAO;
import com.depression.model.MemberSleepQuality;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */

@Service
public class MemberSleepQualityService
{
	@Autowired
	MemberSleepQualityDAO memberSleepQualityDAO;

	public Integer insert(MemberSleepQuality memberSleepQuality)
	{
		memberSleepQuality.setIsDelete(0);
		return memberSleepQualityDAO.insert(memberSleepQuality);
	}

	public List<MemberSleepQuality> getMemberSleepQuality(MemberSleepQuality memberSleepQuality)
	{
		if (null == memberSleepQuality.getRecordDate())
			memberSleepQuality.setRecordDate(new Date());
		return memberSleepQualityDAO.getMemberSleepQuality(memberSleepQuality);
	}
	
	public List<MemberSleepQuality> getMsqByTimeSlice(Long mid, Date begin, Date end)
	{
		return memberSleepQualityDAO.getMsqByTimeSlice(mid, begin, end);
	}
	
	public Integer getMsqDayAverage(Long mid, Date day)
	{
		List<MemberSleepQuality> msqList = memberSleepQualityDAO.getMsqByTimeSlice(mid,day,day);
		
		Integer sleepQuality = 0;
		for(MemberSleepQuality msq:msqList)
		{
			sleepQuality += msq.getSleepQuality();
		}
		
		if (msqList.size() > 0)
		{
			sleepQuality /= msqList.size();
		} else
		{
			sleepQuality = -1;
		}
		
		return sleepQuality;
	}

}
