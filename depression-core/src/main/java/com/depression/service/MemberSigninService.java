package com.depression.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberSigninDAO;
import com.depression.model.MemberSignin;

/**
 * 会员签到
 * @author caizj
 *
 */
@Service
public class MemberSigninService
{
	@Autowired
	MemberSigninDAO dao;
	/**
	 * 添加一条签到
	 * @param signin
	 * @return
	 */
	public Integer addSignin(MemberSignin memberSignin)
	{
		memberSignin.setIsDelete(0);
		memberSignin.setCreateTime(new Date());
		return dao.addSignin(memberSignin);
	}
	
	/**
	 * 获取会员某天的签到信息
	 * @param mid
	 * @param date
	 * @return
	 */
	public MemberSignin getSigninByDate(Long mid, Date date)
	{
		return dao.getSigninByDate(mid, date);
	}
	
	/**
	 * 获取会员某时间段的签到信息，时间区间左右封闭，以天为细粒度
	 * @param mid
	 * @param begin	
	 * @param end
	 * @return
	 */
	public List<MemberSignin> getSigninByDateSlice(Long mid, Date begin, Date end)
	{
		return dao.getSigninByDateSlice(mid, begin, end);
	}
	
	/**
	 * 修改签到状态
	 * @param signinID
	 * @param isDelete
	 * @return
	 */
	public Integer changeSigninStatus(Long signinId, Integer isDelete)
	{
		return dao.changeSigninStatus(signinId, isDelete);
	}

}
