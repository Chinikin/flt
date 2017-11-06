package com.depression.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.MemberMoodIndex;
import com.depression.model.MemberMoodRecord;

/**
 * 情绪记录
 * 
 * @author:ziye_huang
 * @date:2016年5月11日
 */

public interface MemberMoodRecordDAO
{

	public Integer insert(MemberMoodRecord memberMoodRecord);

	public List<MemberMoodRecord> getMemberMoodRecord(MemberMoodRecord memberMoodRecord);
	
	/**
	 * 时间区间左右封闭，以天为细粒度
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<MemberMoodRecord> getMmrByTimeSlice(@Param("mid")Long mid, @Param("begin")Date begin, @Param("end")Date end);

}
