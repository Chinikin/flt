package com.depression.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberMoodIndexDAO;
import com.depression.model.MemberMoodIndex;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */

@Service
public class MemberMoodIndexService
{
	@Autowired
	MemberMoodIndexDAO memberMoodIndexDAO;

	public Integer insert(MemberMoodIndex memberMoodIndex)
	{
		memberMoodIndex.setIsDelete(0);
		return memberMoodIndexDAO.insert(memberMoodIndex);
	}

	public List<MemberMoodIndex> getMemberMoodIndex(MemberMoodIndex memberMoodIndex)
	{
		Date recordDate = memberMoodIndex.getRecordDate();
		if (null == recordDate)
			memberMoodIndex.setRecordDate(new Date());
		return memberMoodIndexDAO.getMemberMoodIndex(memberMoodIndex);
	}
	
	public List<MemberMoodIndex> getMmiByTimeSlice(Long mid, Date begin, Date end)
	{
		return memberMoodIndexDAO.getMmiByTimeSlice(mid, begin, end);
	}
	
	public Integer getMmiDayAverage(Long mid, Date day)
	{
		List<MemberMoodIndex> mmiList = memberMoodIndexDAO.getMmiByTimeSlice(mid, day, day);
		
		Integer index = 0;
		for(MemberMoodIndex mmi:mmiList)
		{
			index += mmi.getMoodIndex();
		}
		
		if (mmiList.size() > 0)
		{
			index /= mmiList.size();
		} else
		{
			index = -1;
		}
		
		return index;
	}
}
