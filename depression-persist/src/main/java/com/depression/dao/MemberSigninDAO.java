package com.depression.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.MemberSignin;

public interface MemberSigninDAO
{
	/**
	 * 添加一条签到
	 * @param signin
	 * @return
	 */
	public Integer addSignin(MemberSignin memberSignin);
	
	/**
	 * 获取会员某天的签到信息
	 * @param mid
	 * @param date
	 * @return
	 */
	public MemberSignin getSigninByDate(@Param("mid")Long mid, @Param("date")Date date);
	
	/**
	 * 获取会员某时间段的签到信息，时间区间左右封闭，以天为细粒度
	 * @param mid
	 * @param begin	
	 * @param end
	 * @return
	 */
	public List<MemberSignin> getSigninByDateSlice(@Param("mid")Long mid, @Param("begin")Date begin, @Param("end")Date end);
	
	/**
	 * 修改签到状态
	 * @param signinID
	 * @param isDelete
	 * @return
	 */
	public Integer changeSigninStatus(@Param("signinId")Long signinId, @Param("isDelete")Integer isDelete);

	
}
