package com.depression.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberMoodRecordDAO;
import com.depression.model.MemberMoodRecord;

/**
 * @author:ziye_huang
 * @date:2016年5月11日
 */

@Service
public class MemberMoodRecordService
{
	@Autowired
	MemberMoodRecordDAO memberMoodRecordDAO;

	public Integer insert(MemberMoodRecord memberMoodRecord)
	{
		memberMoodRecord.setIsDelete(0);
		return memberMoodRecordDAO.insert(memberMoodRecord);
	}

	public List<MemberMoodRecord> getMemberMoodRecord(MemberMoodRecord memberMoodRecord)
	{
		Date recordDate = memberMoodRecord.getRecordDate();
		if (null == recordDate)
			memberMoodRecord.setRecordDate(new Date());
		return memberMoodRecordDAO.getMemberMoodRecord(memberMoodRecord);
	}
	
	public List<MemberMoodRecord> getMmrByTimeSlice(Long mid, Date begin, Date end)
	{
		return memberMoodRecordDAO.getMmrByTimeSlice(mid, begin, end);
	}


}
