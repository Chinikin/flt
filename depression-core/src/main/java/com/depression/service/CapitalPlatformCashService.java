package com.depression.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalPlatformCashMapper;
import com.depression.model.CapitalPlatformCash;

@Service
public class CapitalPlatformCashService
{
	@Autowired
	CapitalPlatformCashMapper capitalPlatformCashMapper;
	
	public CapitalPlatformCash selectRecordLimitOne()
	{
		
		CapitalPlatformCash cpc = capitalPlatformCashMapper.selectRecordLimitOne();
		if(cpc == null)
		{
			CapitalPlatformCash capitalPlatformCash = new CapitalPlatformCash();
			capitalPlatformCash.setCashBalance(BigDecimal.valueOf(0));
			capitalPlatformCash.setExpensesAmount(BigDecimal.valueOf(0));
			capitalPlatformCash.setIncomeAmount(BigDecimal.valueOf(0));
			capitalPlatformCash.setTopUpAmount(BigDecimal.valueOf(0));
			capitalPlatformCash.setWithdrawAmount(BigDecimal.valueOf(0));
			capitalPlatformCashMapper.insertSelective(capitalPlatformCash);
		}
		return capitalPlatformCashMapper.selectRecordLimitOne();
	}
	
	/**
	 * 修改平台现金余额， *已加锁
	 * @param pcid
	 * @param delta
	 * @return
	 */
	public int transCashBalance(Long pcid, BigDecimal delta)
	{
		CapitalPlatformCash cpc = capitalPlatformCashMapper.selectByPrimaryKeyLock(pcid);
		
		CapitalPlatformCash platformCash = new CapitalPlatformCash();
		platformCash.setPcid(pcid);
		platformCash.setCashBalance(cpc.getCashBalance().add(delta));
		return capitalPlatformCashMapper.updateByPrimaryKeySelective(platformCash);
	}
	
	/**
	 * 修改平台现金余额， *已加锁
	 * @param delta
	 * @return
	 */
	public int transCashBalance(BigDecimal delta)
	{
		CapitalPlatformCash cpc = selectRecordLimitOne();
		return transCashBalance(cpc.getPcid(), delta);
	}
	
	/**
	 * 修改平台收入总额， *已加锁
	 * @param pcid
	 * @param delta
	 * @return
	 */
	public int transIncomeAmount(Long pcid, BigDecimal delta)
	{
		CapitalPlatformCash cpc = capitalPlatformCashMapper.selectByPrimaryKeyLock(pcid);
		
		CapitalPlatformCash platformCash = new CapitalPlatformCash();
		platformCash.setPcid(pcid);
		platformCash.setIncomeAmount(cpc.getIncomeAmount().add(delta));
		return capitalPlatformCashMapper.updateByPrimaryKeySelective(platformCash);
	}
	
	/**
	 * 修改平台收入总额， *已加锁
	 * @param delta
	 * @return
	 */
	public int transIncomeAmount(BigDecimal delta)
	{
		CapitalPlatformCash cpc = selectRecordLimitOne();
		return transIncomeAmount(cpc.getPcid(), delta);
	}

	/**
	 * 修改平台支出总额， *已加锁
	 * @param pcid
	 * @param delta
	 * @return
	 */
	public int transExpensesAmount(Long pcid, BigDecimal delta)
	{
		CapitalPlatformCash cpc = capitalPlatformCashMapper.selectByPrimaryKeyLock(pcid);
		
		CapitalPlatformCash platformCash = new CapitalPlatformCash();
		platformCash.setPcid(pcid);
		platformCash.setExpensesAmount(cpc.getExpensesAmount().add(delta));
		return capitalPlatformCashMapper.updateByPrimaryKeySelective(platformCash);
	}
	
	/**
	 * 修改平台支出总额， *已加锁
	 * @param delta
	 * @return
	 */
	public int transExpensesAmount(BigDecimal delta)
	{
		CapitalPlatformCash cpc = selectRecordLimitOne();
		return transExpensesAmount(cpc.getPcid(), delta);
	}

	/**
	 * 修改平台用户充值总额， *已加锁
	 * @param pcid
	 * @param delta
	 * @return
	 */
	public int transTopUpAmount(Long pcid, BigDecimal delta)
	{
		CapitalPlatformCash cpc = capitalPlatformCashMapper.selectByPrimaryKeyLock(pcid);
		
		CapitalPlatformCash platformCash = new CapitalPlatformCash();
		platformCash.setPcid(pcid);
		platformCash.setTopUpAmount(cpc.getTopUpAmount().add(delta));
		return capitalPlatformCashMapper.updateByPrimaryKeySelective(platformCash);
	}
	
	/**
	 * 修改平台用户充值总额， *已加锁
	 * @param delta
	 * @return
	 */
	public int transTopUpAmount(BigDecimal delta)
	{
		CapitalPlatformCash cpc = selectRecordLimitOne();
		return transTopUpAmount(cpc.getPcid(), delta);
	}
	
	/**
	 * 修改平台用户提现总额， *已加锁
	 * @param pcid
	 * @param delta
	 * @return
	 */
	public int transWithdrawAmount(Long pcid, BigDecimal delta)
	{
		CapitalPlatformCash cpc = capitalPlatformCashMapper.selectByPrimaryKeyLock(pcid);
		
		CapitalPlatformCash platformCash = new CapitalPlatformCash();
		platformCash.setPcid(pcid);
		platformCash.setWithdrawAmount(cpc.getWithdrawAmount().add(delta));
		return capitalPlatformCashMapper.updateByPrimaryKeySelective(platformCash);
	}	
	
	/**
	 * 修改平台用户提现总额， *已加锁
	 * @param delta
	 * @return
	 */
	public int transWithdrawAmount(BigDecimal delta)
	{
		CapitalPlatformCash cpc = selectRecordLimitOne();
		return transWithdrawAmount(cpc.getPcid(), delta);
	}	
}
