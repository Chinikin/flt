package com.depression.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalCouponMapper;
import com.depression.dao.CapitalDiscountBestowalMapper;
import com.depression.model.CapitalCoupon;
import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalDiscountBestowal;
import com.depression.model.Page;

@Service
public class CapitalCouponService
{
	@Autowired
	CapitalCouponMapper capitalCouponMapper;
	@Autowired
	CapitalDiscountBestowalMapper capitalCouponBestowalMapper;
	
	final static byte COUPON_TYPE_CASH = 0;
	final static byte COUPON_TYPE_DISCOUNT = 1;
	
	final static byte COUPON_STATUS_USABLE = 0;
	final static byte COUPON_STATUS_USED = 1;
	final static byte COUPON_STATUS_EXPIRED = 2;
	
	/**
	 * 添加优惠券
	 * @param name
	 * @param discount
	 * @param state
	 * @return
	 */
	public Integer createCoupon(String name, BigDecimal discount, String state, byte type)
	{
		CapitalCoupon capitalCoupon = new CapitalCoupon();
		capitalCoupon.setName(name);
		capitalCoupon.setDiscount(discount);
		capitalCoupon.setState(state);
		capitalCoupon.setType(type);
		return capitalCouponMapper.insertSelective(capitalCoupon);
	}
	
	/**
	 * 添加现金优惠券
	 * @param name
	 * @param discount
	 * @param state
	 * @return
	 */
	public Integer createCashCoupon(String name, BigDecimal discount, String state)
	{
		return createCoupon(name, discount, state, COUPON_TYPE_CASH);
	}
	
	/**
	 * 启用优惠券
	 * @param ids
	 * @return
	 */
	public Integer enable(List<Long> ids)
	{
		return capitalCouponMapper.enableByPrimaryKeyBulk(ids, (byte) 0);
	}
	
	/**
	 * 禁用优惠券
	 * @param ids id列表
	 * @return
	 */
	public Integer disable(List<Long> ids)
	{
		return capitalCouponMapper.enableByPrimaryKeyBulk(ids, (byte) 1);
	}
	
	/**
	 * 构建发放的优惠券
	 * @param mid  	会员id
	 * @param cid	优惠券id	
	 * @param validityBeginTime		有效期开始时间
	 * @param validityEndTime		有效期结束时间
	 * @return
	 */
	CapitalDiscountBestowal newCouponBestowal(Long mid, Long cid, Date validityBeginTime,
			Date validityEndTime)
	{
		CapitalDiscountBestowal cdb = new CapitalDiscountBestowal();
		cdb.setMid(mid);
		cdb.setCid(cid);
		cdb.setValidityBeginTime(validityBeginTime);
		cdb.setValidityEndTime(validityEndTime);
		cdb.setBestowalTime(new Date());
		cdb.setStatus(COUPON_STATUS_USABLE);
		return cdb;
	}
	
	/**
	 * 单个发放优惠券
	 * @param mid  	会员id
	 * @param cid	优惠券id	
	 * @param validityBeginTime		有效期开始时间
	 * @param validityEndTime		有效期结束时间
	 * @return
	 */
	public Integer bestowalCoupon(Long mid, Long cid, Date validityBeginTime,
			Date validityEndTime)
	{
		CapitalDiscountBestowal cdb = newCouponBestowal(mid, cid, validityBeginTime, validityEndTime);
		
		return capitalCouponBestowalMapper.insertSelective(cdb);
	}
	
	/**
	 * 新用户发放优惠券
	 * @param mid
	 * @return
	 */
	public Integer bestowalNewMemberCoupon(Long mid)
	{
		CapitalCoupon cc = new CapitalCoupon();
		String name = "新用户优惠券";
		String state = "可用于折扣咨询费用";
		cc.setName(name);
		List<CapitalCoupon> capitalCoupons = capitalCouponMapper.selectSelective(cc);
		if(capitalCoupons.size()==0)
		{
			createCashCoupon(name, BigDecimal.valueOf(30), state);
			capitalCoupons = capitalCouponMapper.selectSelective(cc);
		}
		Date current = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(current);
		calendar.add(Calendar.YEAR, 1);
		return bestowalCoupon(mid, capitalCoupons.get(0).getCid(), current, calendar.getTime());
	}
	
	/**
	 * 批量发放优惠券，1000条每次分批
	 * @param mid  	会员id
	 * @param cid	优惠券id	
	 * @param validityBeginTime		有效期开始时间
	 * @param validityEndTime		有效期结束时间
	 * @return
	 */
	public Integer bestowalCoupon(List<Long> mids, Long cid, Date validityBeginTime,
			Date validityEndTime)
	{
		int eachTime = 1000;
		Integer count = 0;
		while(count < mids.size())
		{
			int thisTime = mids.size() - count;
			thisTime = thisTime < eachTime ? thisTime : eachTime;
			List<Long> subMids = mids.subList(count, count + thisTime);
			count += thisTime;
			
			List<CapitalDiscountBestowal> cdbs = new ArrayList<CapitalDiscountBestowal>();
			for(Long mid : subMids)
			{
				cdbs.add(newCouponBestowal(mid, cid, validityBeginTime, validityEndTime));
			}
			capitalCouponBestowalMapper.insertBulk(cdbs);
		}
		return count;
	}
	/**
	 * 根据mid获取优惠券发放信息
	 * @param mid	会员id
	 * @param status	优惠券状态
	 * @return
	 */
	public List<CapitalDiscountBestowal> getCouponBestowal(Long mid, byte status)
	{
		CapitalDiscountBestowal cdb = new CapitalDiscountBestowal();
		cdb.setMid(mid);
		cdb.setStatus(status);
		return capitalCouponBestowalMapper.selectSelective(cdb);
	}
	

	/**
	 * 根据mid获取未使用的优惠券，根据发放时间倒序
	 * @param mid
	 * @return
	 */
	public List<CapitalDiscountBestowal> getUsableBestowal(Long mid)
	{
		CapitalDiscountBestowal bestowal = new CapitalDiscountBestowal();
		bestowal.setMid(mid);
		bestowal.setStatus(COUPON_STATUS_USABLE);
		return capitalCouponBestowalMapper.selectSelective(bestowal);
	}
	
	/**
	 * 根据mid获取已使用的优惠券，根据发放时间倒序
	 * @param mid
	 * @return
	 */
	public List<CapitalCouponEntity> getAllCouponEntity(Long mid, Integer pageIndex, Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return capitalCouponBestowalMapper.selectCouponEntity(mid, null, page.getPageStartNum(), pageSize);
	}	
	
	/**
	 * 根据mid获取最有价值的现金优惠券
	 * @param mid
	 * @return
	 */
	public CapitalCouponEntity getValuabestUsableCashCouponEntity(Long mid)
	{
		return capitalCouponBestowalMapper.selectValuabestUsableCouponEntity(
				mid, COUPON_TYPE_CASH, new Date());
	}
	
	/**
	 * 根据mid获取最有价值的折扣优惠券,过滤过期的优惠券
	 * @param mid
	 * @return
	 */
	public CapitalCouponEntity getValuabestUsableDiscountCouponEntity(Long mid)
	{
		return capitalCouponBestowalMapper.selectValuabestUsableCouponEntity(
				mid, COUPON_TYPE_DISCOUNT, new Date());
	}
	
	/**
	 * 过期优惠券
	 * @param dbid
	 * @return
	 */
	public Integer expireCoupon(Long dbid)
	{
		CapitalDiscountBestowal cdb = new CapitalDiscountBestowal();
		cdb.setDbid(dbid);
		cdb.setStatus(COUPON_STATUS_EXPIRED);
		
		return capitalCouponBestowalMapper.updateByPrimaryKeySelective(cdb);
	}
	
	/**
	 * 使用优惠券
	 * @param dbid 优惠券id
	 * @return
	 */
	public Integer useCoupon(Long dbid)
	{
		CapitalDiscountBestowal cdb = new CapitalDiscountBestowal();
		cdb.setDbid(dbid);
		cdb.setStatus(COUPON_STATUS_USED);
		
		return capitalCouponBestowalMapper.updateByPrimaryKeySelective(cdb);
	}
	
	/**
	 * 还原优惠券
	 * @param dbid 优惠券id
	 * @return
	 */
	public Integer restoreCoupon(Long dbid)
	{
		CapitalDiscountBestowal cdb = new CapitalDiscountBestowal();
		cdb.setDbid(dbid);
		cdb.setStatus(COUPON_STATUS_USABLE);
		
		return capitalCouponBestowalMapper.updateByPrimaryKeySelective(cdb);
	}	
	
	/**
	 * 根据主键查询对象
	 * 
	 * @param cid
	 * @return
	 */
	public CapitalCoupon selectByPrimaryKey(Long cid)
	{
		return capitalCouponMapper.selectByPrimaryKey(cid);
	}
}
