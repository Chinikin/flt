package com.depression.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.MemberMoodIndex;
import com.depression.model.MemberSleepQuality;

/**
 * 睡眠质量
 * 
 * @author:ziye_huang
 * @date:2016年5月11日
 */

public interface MemberSleepQualityDAO
{

	public Integer insert(MemberSleepQuality memberSleepQuality);

	public List<MemberSleepQuality> getMemberSleepQuality(MemberSleepQuality memberSleepQuality);
	
	/**
	 * 时间区间左右封闭，以天为细粒度
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<MemberSleepQuality> getMsqByTimeSlice(@Param("mid")Long mid, @Param("begin")Date begin, @Param("end")Date end);

}
