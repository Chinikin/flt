package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.MemberConcernMapper;
import com.depression.model.MemberConcern;

/**
 * @author:ziye_huang
 * @date:2016年5月5日
 */

@Service
public class MemberConcernService
{
	@Autowired
	MemberConcernMapper memberConcernMapper;
	
	/**
	 * 查询会员关注的列表
	 * @param mid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberConcern> obtainConcern(Long mid, Integer pageIndex, Integer pageSize)
	{
		MemberConcern mc = new MemberConcern();
		mc.setConcernFrom(mid);
		mc.setPageIndex(pageIndex);
		mc.setPageSize(pageSize);
		
		return memberConcernMapper.selectSelectiveWithPageDesc(mc);
	}
	
	/**
	 * 查询会员关注的数量
	 * @param mid 会员id
	 * @return
	 */
	public Integer countConcernNum(Long mid)
	{
		MemberConcern mc = new MemberConcern();
		mc.setConcernFrom(mid);
		
		return memberConcernMapper.countSelective(mc);
	}
	
	/**
	 * 获取会员的粉丝列表
	 * @param mid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MemberConcern> obtainFans(Long mid, Integer pageIndex, Integer pageSize)
	{
		MemberConcern mc = new MemberConcern();
		mc.setConcernTo(mid);
		mc.setPageIndex(pageIndex);
		mc.setPageSize(pageSize);
		
		return memberConcernMapper.selectSelectiveWithPageDesc(mc);
	}
	
	/**
	 * 查询会员粉丝的数量
	 * @param mid 会员id
	 * @return
	 */
	public Integer countFansNum(Long mid)
	{
		MemberConcern mc = new MemberConcern();
		mc.setConcernTo(mid);
		
		return memberConcernMapper.countSelective(mc);
	}
	
	/**
	 * 设置关注已读
	 * @param memberConcernId
	 * @return
	 */
	public Integer setReaded(Long memberConcernId)
	{
		MemberConcern mc = new MemberConcern();
		mc.setMemberConcernId(memberConcernId);
		mc.setIsRead((byte) 1);
		
		return memberConcernMapper.updateByPrimaryKeySelective(mc);
	}
	
	/**
	 * 查询未读的粉丝数量
	 * @param mid
	 * @return
	 */
	public Integer countUnreadFansNum(Long mid)
	{
		MemberConcern mc = new MemberConcern();
		mc.setConcernTo(mid);
		mc.setIsRead((byte) 0);
		
		return memberConcernMapper.countSelective(mc);
	}
	

	/**
	 * 新建关注
	 * @param concernFrom 关注人
	 * @param concernTo	被关注人
	 * @return
	 */
	public Integer newConcern(Long concernFrom, Long concernTo)
	{
		MemberConcern memberConcern = new MemberConcern();
		memberConcern.setConcernFrom(concernFrom);
		memberConcern.setConcernTo(concernTo);
		memberConcern.setConcernTime(new Date());
		return memberConcernMapper.insertSelective(memberConcern);
	}

	
	/**
	 * 取消关注
	 * @param concernFrom	关注人
	 * @param concernTo	被关注人
	 * @return
	 */
	public Integer removeConcern(Long concernFrom, Long concernTo)
	{
		MemberConcern memberConcern = new MemberConcern();
		memberConcern.setConcernFrom(concernFrom);
		memberConcern.setConcernTo(concernTo);
		
		List<MemberConcern> mcs = memberConcernMapper.selectSelective(memberConcern);
		if(mcs.size() > 0)
		{
			memberConcernMapper.deleteByPrimaryKey(mcs.get(0).getMemberConcernId());
		}
		
		return mcs.size();
	}

	/**
	 * 确认是否已经关注
	 * @param concernFrom 关注人
	 * @param concernTo	被关注
	 * @return
	 */
	public boolean checkConcern(Long concernFrom, Long concernTo)
	{
		MemberConcern memberConcern = new MemberConcern();
		memberConcern.setConcernFrom(concernFrom);
		memberConcern.setConcernTo(concernTo);
		List<MemberConcern> mcs = memberConcernMapper.selectSelective(memberConcern);
		return mcs.size() > 0;
	}



	/**
	 * 获取关注列表
	 * @param memberConcern
	 * @return
	 */
	public List<MemberConcern> selectByPage(MemberConcern memberConcern)
	{
		return memberConcernMapper.selectSelective(memberConcern);
	}
	
	/**
	 * 获取关注列表数量
	 * @param memberConcern
	 * @return
	 */
	public Integer selectCount(MemberConcern memberConcern)
	{
		return memberConcernMapper.countSelective(memberConcern);
	}
	
	/**
	 * 删除关注
	 * @param memberConcernId
	 * @return
	 */
	public Integer deleteConcern(Long memberConcernId)
	{
		return memberConcernMapper.deleteByPrimaryKey(memberConcernId);
	}
	
	
	
	public List<MemberConcern> obtainAllConcern(Long mid){
		MemberConcern mc=new MemberConcern();
		mc.setConcernFrom(mid);
		return memberConcernMapper.selectSelective(mc);
	}
	
	

}
