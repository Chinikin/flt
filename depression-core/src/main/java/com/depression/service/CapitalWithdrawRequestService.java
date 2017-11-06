package com.depression.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalWithdrawRequestMapper;
import com.depression.model.CapitalWithdrawRequest;

/**
 * 提现申请服务层
 * 
 * @author hongqian_li
 * @date 2016/08/28
 */
@Service
public class CapitalWithdrawRequestService
{
	@Autowired
	CapitalWithdrawRequestMapper mCapitalWithdrawRequestMapper;

	public final static byte CHANNEL_WX = 1;
	public final static byte CHANNEL_UNIONPAY = 4;
	public final static byte CHANNEL_WX_PUB = 5;
	
	public final static byte STATUS_INAUDITED = 0;
	public final static byte STATUS_AUDITED = 1;
	public final static byte STATUS_FAILED = 2;
	
	/**
	 * 获取渠道代码
	 * @param channel
	 * @return
	 */
	public static byte getChannelCode(String channel)
	{
		if(channel.equals("wx")) return CHANNEL_WX;
		if(channel.equals("wx_pub")) return CHANNEL_WX_PUB;
		if(channel.equals("unionpay")) return CHANNEL_UNIONPAY;
		return -1;
	}
	
	/**
	 * 获取渠道名称
	 * @param code
	 * @return
	 */
	public static String getChannelStr(byte code)
	{
		if (code == CHANNEL_WX)	return "wx";
		if(code == CHANNEL_WX_PUB) return "wx_pub";
		if(code == CHANNEL_UNIONPAY) return "unionpay";	
		return null;
	}
	
	/**
	 * 分页查询
	 * 
	 * @param entity
	 * @return
	 */
	public List<CapitalWithdrawRequest> selectAllByPage(CapitalWithdrawRequest entity)
	{
		return mCapitalWithdrawRequestMapper.selectSelectiveWithPage(entity);
	}

	/**
	 * 新增一条数据
	 * 
	 * @mbggenerated Sat Aug 27 14:35:49 CST 2016
	 */
	public int insert(CapitalWithdrawRequest record)
	{
		return mCapitalWithdrawRequestMapper.insertSelective(record);
	}

	/**
	 * 根据主键查找
	 * 
	 * @mbggenerated Sat Aug 27 14:35:49 CST 2016
	 */
	public CapitalWithdrawRequest selectByPrimaryKey(Long wrid)
	{
		return mCapitalWithdrawRequestMapper.selectByPrimaryKey(wrid);
	}

	/**
	 * 根据主键查找
	 * 
	 * @mbggenerated Sat Aug 27 14:35:49 CST 2016
	 */
	public CapitalWithdrawRequest selectByNo(String no)
	{
		CapitalWithdrawRequest cwq = new CapitalWithdrawRequest();
		cwq.setNo(no);
		List<CapitalWithdrawRequest> cwqs = mCapitalWithdrawRequestMapper.selectSelective(cwq);
		return cwqs.size() > 0 ? cwqs.get(0) : null;
	}

	/**
	 * 更新数据
	 * 
	 * @param record
	 * @return
	 */
	public int update(CapitalWithdrawRequest record)
	{
		return mCapitalWithdrawRequestMapper.updateByPrimaryKey(record);
	}

	/**
	 * 查询条数
	 * 
	 * @param record
	 * @return
	 */
	public int selectCount(CapitalWithdrawRequest record)
	{
		return mCapitalWithdrawRequestMapper.countSelective(record);
	}

	/**
	 * 获取用户在审核中的提现申请
	 * @param mid
	 * @return
	 */
	public List<CapitalWithdrawRequest> selectMemberInaudited(Long mid)
	{
		CapitalWithdrawRequest record = new CapitalWithdrawRequest();
		record.setMid(mid);
		record.setStatus(STATUS_INAUDITED);
		return mCapitalWithdrawRequestMapper.selectSelective(record);
	}
}
