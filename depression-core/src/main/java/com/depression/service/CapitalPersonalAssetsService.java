package com.depression.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalPersonalAssetsMapper;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.UserType;
import com.depression.model.web.dto.CapitalPersonalAssetsDTO;
import com.depression.utils.AccountUtil;

/**
 * 个人资产
 * 
 * @author:ziye_huang
 * @date:2016年8月28日
 */
@Service
public class CapitalPersonalAssetsService
{
	@Autowired
	CapitalPersonalAssetsMapper capitalPersonalAssetsMapper;
	@Autowired
	CapitalCouponService capitalCouponService;
	@Autowired
	MemberService memberService;

	/**
	 * 插入一条个人资产
	 * 
	 * @param cpa
	 * @return
	 */
	public int insert(Long mid)
	{
		CapitalPersonalAssets personalAssets = new CapitalPersonalAssets();
		personalAssets.setMid(mid);
		personalAssets.setAccount(AccountUtil.generateAccount());
		return capitalPersonalAssetsMapper.insertSelective(personalAssets);
	}

	/**
	 * 禁用或启用该条记录：0 启用，1禁用
	 * 
	 * @param ids
	 * @param enable
	 * @return
	 */
	public int enableByPK(List<Long> ids, Byte enable)
	{
		return capitalPersonalAssetsMapper.enableByPrimaryKeyBulk(ids, enable);
	}

	/**
	 * 更新个人资产
	 * 
	 * @param cpa
	 * @return
	 */
	public int update(CapitalPersonalAssets cpa)
	{
		return capitalPersonalAssetsMapper.updateByPrimaryKeySelective(cpa);
	}

	/**
	 * 修改个人资产现金余额， *已加锁
	 * 
	 * @param paid
	 * @param delta
	 * @return
	 */
	public int transCashBalance(Long paid, BigDecimal delta)
	{
		CapitalPersonalAssets cpa = capitalPersonalAssetsMapper.selectByPrimaryKeyLock(paid);

		CapitalPersonalAssets personalAssets = new CapitalPersonalAssets();
		personalAssets.setPaid(paid);
		personalAssets.setCashBalance(cpa.getCashBalance().add(delta));
		return capitalPersonalAssetsMapper.updateByPrimaryKeySelective(personalAssets);
	}

	/**
	 * 修改个人资产现金余额， *已加锁
	 * 
	 * @param delta
	 * @param mid
	 * @return
	 */
	public int transCashBalance(BigDecimal delta, Long mid)
	{
		CapitalPersonalAssets cpa = selectByMid(mid);
		if (cpa == null)
			return 0;

		return transCashBalance(cpa.getPaid(), delta);
	}

	/**
	 * 修改个人资产服务收入总额， *已加锁
	 * 
	 * @param paid
	 * @param delta
	 * @return
	 */
	public int transServiceIncomeAmount(Long paid, BigDecimal delta)
	{
		CapitalPersonalAssets cpa = capitalPersonalAssetsMapper.selectByPrimaryKeyLock(paid);

		CapitalPersonalAssets personalAssets = new CapitalPersonalAssets();
		personalAssets.setPaid(paid);
		personalAssets.setServiceIncomeAmount(cpa.getServiceIncomeAmount().add(delta));
		return capitalPersonalAssetsMapper.updateByPrimaryKeySelective(personalAssets);
	}

	/**
	 * 修改个人资产服务收入总额， *已加锁
	 * 
	 * @param delta
	 * @param mid
	 * @return
	 */
	public int transServiceIncomeAmount(BigDecimal delta, Long mid)
	{
		CapitalPersonalAssets cpa = selectByMid(mid);
		if (cpa == null)
			return 0;

		return transServiceIncomeAmount(cpa.getPaid(), delta);
	}

	/**
	 * 修改个人资产充值总额， *已加锁
	 * 
	 * @param paid
	 * @param delta
	 * @return
	 */
	public int transPayAmount(Long paid, BigDecimal delta)
	{
		CapitalPersonalAssets cpa = capitalPersonalAssetsMapper.selectByPrimaryKeyLock(paid);

		CapitalPersonalAssets personalAssets = new CapitalPersonalAssets();
		personalAssets.setPaid(paid);
		personalAssets.setPayAmount(cpa.getPayAmount().add(delta));
		return capitalPersonalAssetsMapper.updateByPrimaryKeySelective(personalAssets);
	}

	/**
	 * 修改个人资产充值总额， *已加锁
	 * 
	 * @param delta
	 * @param mid
	 * @return
	 */
	public int transPayAmount(BigDecimal delta, Long mid)
	{
		CapitalPersonalAssets cpa = selectByMid(mid);
		if (cpa == null)
			return 0;

		return transPayAmount(cpa.getPaid(), delta);
	}

	/**
	 * 修改个人资产支出总额， *已加锁
	 * 
	 * @param paid
	 * @param delta
	 * @return
	 */
	public int transExpenseAmount(Long paid, BigDecimal delta)
	{
		CapitalPersonalAssets cpa = capitalPersonalAssetsMapper.selectByPrimaryKeyLock(paid);

		CapitalPersonalAssets personalAssets = new CapitalPersonalAssets();
		personalAssets.setPaid(paid);
		personalAssets.setExpenseAmount(cpa.getExpenseAmount().add(delta));
		return capitalPersonalAssetsMapper.updateByPrimaryKeySelective(personalAssets);
	}

	/**
	 * 修改个人资产支出总额， *已加锁
	 * 
	 * @param delta
	 * @param mid
	 * @return
	 */
	public int transExpenseAmount(BigDecimal delta, Long mid)
	{
		CapitalPersonalAssets cpa = selectByMid(mid);
		if (cpa == null)
			return 0;

		return transExpenseAmount(cpa.getPaid(), delta);
	}

	/**
	 * 修改个人资产提现总额， *已加锁
	 * 
	 * @param paid
	 * @param delta
	 * @return
	 */
	public int transWithdrawAmount(Long paid, BigDecimal delta)
	{
		CapitalPersonalAssets cpa = capitalPersonalAssetsMapper.selectByPrimaryKeyLock(paid);

		CapitalPersonalAssets personalAssets = new CapitalPersonalAssets();
		personalAssets.setPaid(paid);
		personalAssets.setWithdrawAmount(cpa.getWithdrawAmount().add(delta));
		return capitalPersonalAssetsMapper.updateByPrimaryKeySelective(personalAssets);
	}

	/**
	 * 修改个人资产提现总额， *已加锁
	 * 
	 * @param delta
	 * @param mid
	 * @return
	 */
	public int transWithdrawAmount(BigDecimal delta, Long mid)
	{
		CapitalPersonalAssets cpa = selectByMid(mid);
		if (cpa == null)
			return 0;

		return transWithdrawAmount(cpa.getPaid(), delta);
	}

	/**
	 * 根据mid查询个人资产
	 * 
	 * @param mid
	 * @return
	 */
	public CapitalPersonalAssets selectByMid(Long mid)
	{
		CapitalPersonalAssets cpa = new CapitalPersonalAssets();
		cpa.setMid(mid);
		List<CapitalPersonalAssets> cpaList = capitalPersonalAssetsMapper.selectSelective(cpa);
		if (cpaList.size() == 0)
		{
			insert(mid);
			cpaList = capitalPersonalAssetsMapper.selectSelective(cpa);

			// 发放新普通用户优惠券;
			if (!memberService.isPsychos(mid))
				capitalCouponService.bestowalNewMemberCoupon(mid);
		}
		return cpaList.get(0);
	}

	/**
	 * 根据mid获取现金余额
	 * 
	 * @param mid
	 * @return
	 */
	public BigDecimal selectCashBalanceByMid(Long mid)
	{
		CapitalPersonalAssets cpa = selectByMid(mid);
		if (cpa == null)
			return null;

		return cpa.getCashBalance();
	}

	/**
	 * 分页查找
	 * 
	 * @param cpa
	 * @return
	 */
	public List<CapitalPersonalAssetsDTO> selectWithPageByType(UserType userType)
	{
		return capitalPersonalAssetsMapper.selectWithPageByType(userType);
	}

	public Integer selectWithPageByTypeCount(UserType userType)
	{
		return capitalPersonalAssetsMapper.selectWithPageByTypeCount(userType);
	}

	/**
	 * 查询咨询师资金账户列表
	 * 
	 * @param counselorCondition
	 * @return
	 */
	public List<CapitalPersonalAssetsDTO> selectCounselorAccounts(Integer userType, String mobilePhone, String account, String nickname, String title, Date startTime, Date endTime,
			Integer startAnswerCount, Integer endAnswerCount, BigDecimal startServiceIncomeAmount, BigDecimal endServiceIncomeAmount, Integer topNum, Integer topPercent, String sortName,
			String sortType, Integer pageIndex, Integer pageSize)
	{
		return capitalPersonalAssetsMapper.selectCounselorAccounts(userType, mobilePhone, account, nickname, title, startTime, endTime, startAnswerCount, endAnswerCount, startServiceIncomeAmount,
				endServiceIncomeAmount, topNum, topPercent, sortName, sortType, pageIndex, pageSize);
	}

	public Integer selectCounselorAccountsCount(Integer userType, String mobilePhone, String account, String nickname, String title, Date startTime, Date endTime, Integer startAnswerCount,
			Integer endAnswerCount, BigDecimal startServiceIncomeAmount, BigDecimal endServiceIncomeAmount, Integer topNum, Integer topPercent, String sortName, String sortType, Integer pageIndex,
			Integer pageSize)
	{
		return capitalPersonalAssetsMapper.selectCounselorAccountsCount(userType, mobilePhone, account, nickname, title, startTime, endTime, startAnswerCount, endAnswerCount,
				startServiceIncomeAmount, endServiceIncomeAmount, topNum, topPercent, sortName, sortType, pageIndex, pageSize);
	}
}
