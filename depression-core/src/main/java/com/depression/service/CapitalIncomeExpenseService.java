package com.depression.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.CapitalIncomeExpensesMapper;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.MidModel;
import com.depression.model.web.dto.CapitalIncomeExpensesDTO;
import com.depression.model.web.dto.ExpensesRecordDTO;
import com.depression.utils.AccountUtil;

/**
 * 现金收支明细
 * 
 * @author:ziye_huang
 * @date:2016年8月29日
 */
@Service
public class CapitalIncomeExpenseService
{
	@Autowired
	CapitalIncomeExpensesMapper capitalIncomeExpensesMapper;
	@Autowired
	CapitalPersonalAssetsService capitalPersonalAssetsService;

	public final static byte DIRECTION_INCOME = 0; // 收入
	public final static byte DIRECTION_EXPENSES = 1; // 支出

	public final static byte ITEMS_TOPUP = 0; // 充值
	public final static byte ITEMS_SERVICE_INCOME = 1; // 服务收入
	public final static byte ITEMS_EXPENSES = 2; // 消费
	public final static byte ITEMS_WITHDRAW = 3; // 提现
	public final static byte ITEMS_REFUND = 4; // 退款
	public final static byte ITEMS_SERVICE_RETURN = 5; // 退还服务费

	public final static byte CHANNEL_ALIPAY = 0;
	public final static byte CHANNEL_WX = 1;
	public final static byte CHANNEL_UPACP = 2;
	public final static byte CHANNEL_XINMAO = 3;
	public final static byte CHANNEL_UNIONPAY = 4;
	public final static byte CHANNEL_WX_PUB = 5;

	public final static byte STATUS_PAYING = 0;
	public final static byte STATUS_PAID = 1;
	public final static byte STATUS_FAILED = 2;

	/**
	 * 获取渠道代码
	 * 
	 * @param channel
	 * @return
	 */
	public static byte getChannelCode(String channel)
	{
		if (channel.equals("alipay"))
			return CHANNEL_ALIPAY;
		if (channel.equals("wx"))
			return CHANNEL_WX;
		if (channel.equals("upacp"))
			return CHANNEL_UPACP;
		if (channel.equals("xinmao"))
			return CHANNEL_XINMAO;
		if (channel.equals("unionpay"))
			return CHANNEL_UNIONPAY;
		if(channel.equals("wx_pub")) 
			return CHANNEL_WX_PUB;
		return -1;
	}

	/**
	 * 获取渠道名称
	 * 
	 * @param code
	 * @return
	 */
	public static String getChannelStr(byte code)
	{
		if (code == CHANNEL_ALIPAY)
			return "alipay";
		if (code == CHANNEL_WX)
			return "wx";
		if (code == CHANNEL_UPACP)
			return "upacp";
		if (code == CHANNEL_XINMAO)
			return "xinmao";
		if (code == CHANNEL_UNIONPAY)
			return "unionpay";
		if(code == CHANNEL_WX_PUB) 
			return "wx_pub";
		return null;
	}

	/**
	 * 根据编号获取支付明细
	 * 
	 * @param no
	 * @return
	 */
	public CapitalIncomeExpenses getIncomeExpenses(String no)
	{
		CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
		cie.setNo(no);
		List<CapitalIncomeExpenses> cies = capitalIncomeExpensesMapper.selectSelective(cie);
		return cies.size() > 0 ? cies.get(0) : null;
	}
	
	/**
	 * 根据订单编号获取支付明细
	 * 
	 * @param no
	 * @return
	 */
	public CapitalIncomeExpenses getIncomeExpensesByOrderNo(String orderNo)
	{
		CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
		cie.setOrderNo(orderNo);
		List<CapitalIncomeExpenses> cies = capitalIncomeExpensesMapper.selectSelective(cie);
		return cies.size() > 0 ? cies.get(0) : null;
	}


	/**
	 * 获取用户收支明细，分页
	 * 
	 * @param mid
	 * @param items
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<CapitalIncomeExpenses> getIncomeExpensesWithPage(Long mid, Byte items, Integer pageIndex, Integer pageSize)
	{
		CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
		cie.setMid(mid);
		cie.setItems(items);
		cie.setStatus(STATUS_PAID);
		cie.setPageIndex(pageIndex);
		cie.setPageSize(pageSize);
		return capitalIncomeExpensesMapper.selectSelectiveOrderByTimeWithPage(cie);
	}

	/**
	 * 获取用户收支明细数量
	 * 
	 * @param mid
	 * @param items
	 * @return
	 */
	public Integer countIncomeExpenses(Long mid, Byte items)
	{
		CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
		cie.setMid(mid);
		cie.setItems(items);
		cie.setStatus(STATUS_PAID);
		return capitalIncomeExpensesMapper.countSelective(cie);
	}

	/**
	 * 插入一条现金收支明细
	 * 
	 * @param cie
	 * @return
	 */
	public int insert(CapitalIncomeExpenses cie)
	{
		return capitalIncomeExpensesMapper.insertSelective(cie);
	}

	/**
	 * 删除一条记录
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteByPK(List<Long> ids)
	{
		return capitalIncomeExpensesMapper.deleteByPrimaryKeyBulk(ids);
	}

	/**
	 * 更新现金收支明细
	 * 
	 * @param cie
	 * @return
	 */
	public int update(CapitalIncomeExpenses cie)
	{
		cie.setFinishTime(new Date());
		return capitalIncomeExpensesMapper.updateByPrimaryKeySelective(cie);
	}

	/**
	 * 查询现金收支明细（分页）
	 * 
	 * @param cieVO
	 * @return
	 */
	public List<CapitalIncomeExpensesDTO> selectWithPageByMid(MidModel swpbm)
	{
		return capitalIncomeExpensesMapper.selectWithPageByMid(swpbm);
	}

	/**
	 * 
	 * @param swpbm
	 * @return
	 */
	public int selectCount(MidModel swpbm)
	{
		return capitalIncomeExpensesMapper.selectCount(swpbm);
	}

	/**
	 * 查询消费记录
	 * 
	 * @param swpbm
	 * @return
	 */
	public List<ExpensesRecordDTO> selectWithPageExpenseRecordByMid(MidModel swpbm)
	{
		return capitalIncomeExpensesMapper.selectWithPageExpenseRecordByMid(swpbm);
	}

	/**
	 * 查询消费记录数量
	 * 
	 * @param swpbm
	 * @return
	 */
	public int selectExpenseRecordCount(MidModel swpbm)
	{
		return capitalIncomeExpensesMapper.selectExpenseRecordCount(swpbm);
	}

	public int insertRefund(Long mid, BigDecimal amount)
	{
		// 构建信息
		CapitalIncomeExpenses incomeExpenses = new CapitalIncomeExpenses();
		incomeExpenses.setNo(AccountUtil.generateNo());
		incomeExpenses.setMid(mid);
		incomeExpenses.setAmount(amount);
		incomeExpenses.setItems(ITEMS_REFUND);
		incomeExpenses.setDirection(DIRECTION_INCOME);
		incomeExpenses.setStatus(STATUS_PAID);
		incomeExpenses.setRemark("退款");
		incomeExpenses.setCashBalance(capitalPersonalAssetsService.selectCashBalanceByMid(mid));
		incomeExpenses.setCreateTime(new Date());
		incomeExpenses.setFinishTime(new Date());

		return capitalIncomeExpensesMapper.insertSelective(incomeExpenses);
	}

	public int insertServiceReturn(Long mid, BigDecimal amount)
	{
		// 构建信息
		CapitalIncomeExpenses incomeExpenses = new CapitalIncomeExpenses();
		incomeExpenses.setNo(AccountUtil.generateNo());
		incomeExpenses.setMid(mid);
		incomeExpenses.setAmount(amount);
		incomeExpenses.setItems(ITEMS_SERVICE_RETURN);
		incomeExpenses.setDirection(DIRECTION_EXPENSES);
		incomeExpenses.setStatus(STATUS_PAID);
		incomeExpenses.setRemark("退还服务费");
		incomeExpenses.setCashBalance(capitalPersonalAssetsService.selectCashBalanceByMid(mid));
		incomeExpenses.setCreateTime(new Date());
		incomeExpenses.setFinishTime(new Date());

		return capitalIncomeExpensesMapper.insertSelective(incomeExpenses);
	}
	
}
