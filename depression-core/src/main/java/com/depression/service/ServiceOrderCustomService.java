package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalDiscountBestowalMapper;
import com.depression.dao.CapitalIncomeExpensesMapper;
import com.depression.dao.CapitalPersonalAssetsMapper;
import com.depression.dao.CapitalPlatformCashMapper;
import com.depression.dao.ServiceGoodsMapper;
import com.depression.dao.ServiceOrderMapper;
import com.depression.model.ServiceOrder;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class ServiceOrderCustomService
{
	@Autowired
	private ServiceOrderMapper serviceOrderMapper;

	@Autowired
	CapitalDiscountBestowalMapper capitalCouponBestowalMapper;

	@Autowired
	CapitalPersonalAssetsMapper capitalPersonalAssetsMapper;

	@Autowired
	CapitalPlatformCashMapper capitalPlatformCashMapper;

	@Autowired
	ServiceGoodsMapper serviceGoodsMapper;

	@Autowired
	CapitalIncomeExpensesMapper capitalIncomeExpensesMapper;

	public Page<ServiceOrder> searchRecommendOrder(String nickname, 
			Integer pageIndex, Integer pageSize, Byte createTimeDirection,
			Date begin, Date end){
		
				if(pageIndex != null &&pageSize != null){
				PageHelper.startPage(pageIndex, pageSize);
				}
		return serviceOrderMapper.searchRecommendOrder(nickname, createTimeDirection, begin, end);
	}
	
	
	public List<ServiceOrder> getAllRecommendOrder(){
		ServiceOrder so = new ServiceOrder();
		so.setOrderType((byte)2);
		return serviceOrderMapper.selectSelective(so);
	}
		
	
	public List<ServiceOrder> getEapServiceOrder(ServiceOrder so){
		return serviceOrderMapper.selectSelective(so);
	}
}
