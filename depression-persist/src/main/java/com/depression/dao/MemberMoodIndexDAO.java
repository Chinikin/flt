package com.depression.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.MemberMoodIndex;

/**
 * 心情指数
 * 
 * @author:ziye_huang
 * @date:2016年5月11日
 */

public interface MemberMoodIndexDAO
{

	public Integer insert(MemberMoodIndex memberMoodIndex);

	public List<MemberMoodIndex> getMemberMoodIndex(MemberMoodIndex memberMoodIndex);

	/**
	 * 时间区间左右封闭，以天为细粒度
	 * @param mid
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<MemberMoodIndex> getMmiByTimeSlice(@Param("mid")Long mid, @Param("begin")Date begin, @Param("end")Date end);
}
