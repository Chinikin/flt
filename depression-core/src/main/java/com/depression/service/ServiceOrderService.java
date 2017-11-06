package com.depression.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.utils.DateUtil;
import com.depression.dao.CapitalDiscountBestowalMapper;
import com.depression.dao.CapitalIncomeExpensesMapper;
import com.depression.dao.CapitalPersonalAssetsMapper;
import com.depression.dao.CapitalPlatformCashMapper;
import com.depression.dao.ServiceGoodsMapper;
import com.depression.dao.ServiceOrderMapper;
import com.depression.entity.AccountDetailsDirectionType;
import com.depression.entity.AccountDetailsType;
import com.depression.entity.CouponStatus;
import com.depression.entity.CouponType;
import com.depression.entity.OrderState;
import com.depression.model.CapitalCommissionRate;
import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalDiscountBestowal;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.CapitalPlatformCash;
import com.depression.model.Page;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.utils.BigDecimalUtil;

@Service
public class ServiceOrderService
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

	public static byte REFUND_STATUS_NO = 0; // 未退款
	public static byte REFUND_STATUS_CASH = 1; // 现金退款
	public static byte REFUND_STATUS_RESTORE = 2; // 原封退款（含优惠券）

	public static byte STATUS_PAYING = 0; // 0 正在扣款
	public static byte STATUS_PAYED = 1; // 1 已支付
	public static byte STATUS_UNDERWAY = 2; // 2 进行中
	public static byte STATUS_FINISH = 3; // 3已完成
	public static byte STATUS_EXCEPTION_UNHANDLED = 4; // 4 异常未处理
	public static byte STATUS_EXCEPTION_HANDLED = 5;// 5异常已处理
	public static byte STATUS_COMPLAINT_UNHANDLED = 7;// 7投诉未处理
	public static byte STATUS_COMPLAINT_HANDLED = 8; // 8投诉已处理

	/**
	 * 插入订单记录
	 * 
	 * @param record
	 * @return
	 */
	public int insert(ServiceOrder record)
	{
		return serviceOrderMapper.insertSelective(record);
	}

	/**
	 * 更新订单记录
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(ServiceOrder record)
	{
		return serviceOrderMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 条件查询
	 * 
	 * @param record
	 * @return
	 */
	public List<ServiceOrder> selectSelective(ServiceOrder record)
	{
		return serviceOrderMapper.selectSelective(record);
	}

	/**
	 * 根据编号查询
	 * 
	 * @param no
	 * @return
	 */
	public ServiceOrder selectByNo(String no)
	{
		ServiceOrder so = new ServiceOrder();
		so.setNo(no);
		List<ServiceOrder> sos = serviceOrderMapper.selectSelective(so);

		if (sos.size() > 0)
		{
			return sos.get(0);
		} else
		{
			return null;
		}
	}

	/**
	 * 条件查询分页
	 * 
	 * @param record
	 * @return
	 */
	public List<ServiceOrder> selectSelectiveByPage(ServiceOrder record)
	{
		return serviceOrderMapper.selectSelectiveWithPage(record);
	}
	
	public List<ServiceOrder> selectSelective3Page0TmDesc(ServiceOrder record)
	{
		return serviceOrderMapper.selectSelective3Page0TmDesc(record);
	}
	
	public List<ServiceOrder> selectSelective3Page0TmDescByzj(ServiceOrder record)
	{
		return serviceOrderMapper.selectSelective3Page0TmDescByzj(record);
	}
	/**
	 * 根据咨询师id获取订单列表
	 * @param pid
	 * @return
	 */
	public List<ServiceOrder> obtainServiceOrderByPid(Long pid)
	{
		ServiceOrder so = new ServiceOrder();
		so.setServiceProviderId(pid);
		
		return serviceOrderMapper.selectSelective(so);
	}

	/**
	 * 根据主键查询订单
	 * 
	 * @param soid
	 * @return
	 */
	public ServiceOrder selectOrderByPrimaryKey(Long soid)
	{
		return serviceOrderMapper.selectByPrimaryKey(soid);
	}

	/**
	 * 根据主键查询商品
	 * 
	 * @param soid
	 * @return
	 */
	public ServiceGoods selectGoodsByPrimaryKey(Long sgid)
	{
		return serviceGoodsMapper.selectByPrimaryKey(sgid);
	}

	/**
	 * 处理通话订单交易信息
	 * 
	 * @param serviceOrder
	 *            订单记录
	 * @param serviceGoods
	 *            商品信息
	 * @param capitalCoupon
	 *            优惠券信息
	 * @param callerAssets
	 *            主叫人账户
	 * @param calledAssets
	 *            被叫人账户
	 * @param curCommissionRate
	 *            平台佣金比例
	 * @return
	 * @throws Exception
	 */
	public int updateNewTransactions(ServiceOrder serviceOrder, ServiceGoods serviceGoods, CapitalCouponEntity capitalCoupon, CapitalPersonalAssets callerAssets, CapitalPersonalAssets calledAssets, CapitalCommissionRate curCommissionRate) throws Exception
	{
		// 使用优惠券
		if(capitalCoupon != null && serviceOrder.getCost().compareTo(capitalCoupon.getDiscount()) < 1)
		{//只有订单总额大于咨询券才能使用
			capitalCoupon = null;
		}
		CapitalDiscountBestowal cdb = new CapitalDiscountBestowal();
		if (capitalCoupon != null)
		{
			cdb.setDbid(capitalCoupon.getDbid());
			cdb.setStatus(Byte.parseByte(CouponStatus.COUPON_STATUS_USED.getCode().toString()));
			capitalCouponBestowalMapper.updateByPrimaryKeySelective(cdb);
		}
		
		// 扣除主叫人账户金额
		BigDecimal goodsAmount = serviceGoods.getPrice().multiply(new BigDecimal(serviceOrder.getGoodsQuantity())); // 总额
		BigDecimal goodsFinalPrice = new BigDecimal(0.00); // 商品折扣后金额
		BigDecimal goodsDiscountAmount = new BigDecimal(0.00); // 商品被优惠金额
		if (capitalCoupon != null)//有可用优惠券
		{
			Byte couponType = capitalCoupon.getType();
			if(couponType.intValue() == CouponType.TYPE_CASH.getCode().intValue()) // 现金类型
			{
				goodsFinalPrice = BigDecimal.valueOf(BigDecimalUtil.sub(goodsAmount.doubleValue(), capitalCoupon.getDiscount().doubleValue()));//付款金额 = 商品总额-优惠券金额
				goodsDiscountAmount = capitalCoupon.getDiscount();// 优惠金额=优惠券金额
			} else if(couponType.intValue() == CouponType.TYPE_DISCOUNT.getCode().intValue()) // 折扣类型
			{
				goodsFinalPrice = BigDecimal.valueOf(BigDecimalUtil.mul(goodsAmount.doubleValue(), capitalCoupon.getDiscount().doubleValue()) / 100);//付款金额 = 商品总额*优惠券折扣
				goodsDiscountAmount = goodsAmount.subtract(goodsFinalPrice);// 优惠金额=商品总额-商品折扣后金额
			}
		} else {//无可用优惠券
			goodsFinalPrice = goodsAmount;
		}
		BigDecimal callerCashBalance = callerAssets.getCashBalance();
		if (callerCashBalance.intValue() < goodsFinalPrice.intValue())
		{
			throw new Exception("主叫人个人账户余额不足");
		}
		callerCashBalance = callerCashBalance.subtract(goodsFinalPrice);
		callerAssets.setCashBalance(callerCashBalance);
		callerAssets.setExpenseAmount(callerAssets.getExpenseAmount().add(goodsFinalPrice));
		capitalPersonalAssetsMapper.updateByPrimaryKey(callerAssets);
		
		// 被叫人账户入账
		BigDecimal calledCashBalance = calledAssets.getCashBalance();
		Integer rate = curCommissionRate.getRate();
		if (rate < 0 || rate > 100)
		{
			throw new Exception("佣金比例异常，正常值：0至100");
		}
		BigDecimal calledCollection = BigDecimal.valueOf(BigDecimalUtil.sub(goodsAmount.doubleValue(), BigDecimalUtil.mul(goodsAmount.doubleValue(), rate/100.0))).setScale(2);
		calledCashBalance = calledCashBalance.add(calledCollection);
		calledAssets.setCashBalance(calledCashBalance);
		calledAssets.setServiceIncomeAmount(calledAssets.getServiceIncomeAmount().add(calledCollection));
		capitalPersonalAssetsMapper.updateByPrimaryKey(calledAssets);
		
		// 平台入账
		CapitalPlatformCash capitalPlatformCash = capitalPlatformCashMapper.selectRecordLimitOne();
		BigDecimal platformCashBalance = capitalPlatformCash.getCashBalance();
		BigDecimal platformCollection = (goodsAmount.multiply(new BigDecimal(rate))).divide(new BigDecimal(100.00), 2, RoundingMode.UP);
		platformCollection = platformCollection.subtract(goodsDiscountAmount);
		platformCashBalance = platformCashBalance.add(platformCollection);
		capitalPlatformCash.setCashBalance(platformCashBalance);
		capitalPlatformCashMapper.updateByPrimaryKey(capitalPlatformCash);
		
		// 更新订单信息
		if (capitalCoupon != null)
		{
			// 优惠券抵扣
			serviceOrder.setDbid(capitalCoupon.getDbid());
			serviceOrder.setDiscountAmount(goodsDiscountAmount);
		}
		serviceOrder.setCommissionRate(curCommissionRate.getRate());
		serviceOrder.setCashAmount(goodsFinalPrice);
		serviceOrderMapper.updateByPrimaryKeySelective(serviceOrder);
		
		// 新增主叫人账户明细
		CapitalIncomeExpenses capitalIncomeExpenses4Caller = new CapitalIncomeExpenses();
		capitalIncomeExpenses4Caller.setMid(callerAssets.getMid());
		capitalIncomeExpenses4Caller.setOrderNo(serviceOrder.getNo());
		capitalIncomeExpenses4Caller.setAmount(goodsFinalPrice);
		capitalIncomeExpenses4Caller.setCashBalance(callerAssets.getCashBalance());
		capitalIncomeExpenses4Caller.setItems(Byte.parseByte(AccountDetailsType.TYPE_CONSUME.getCode().toString()));
		capitalIncomeExpenses4Caller.setDirection(Byte.parseByte(AccountDetailsDirectionType.TYPE_EXPEND.getCode().toString()));
		capitalIncomeExpenses4Caller.setStatus(STATUS_PAYED);
		if (serviceGoods!= null && serviceGoods.getType() != null)
		{
			if (serviceGoods.getType().byteValue() == new Byte("0").byteValue())
			{
				capitalIncomeExpenses4Caller.setRemark("咨询费支出");
			} else if (serviceGoods.getType().byteValue() == new Byte("1").byteValue())
			{
				capitalIncomeExpenses4Caller.setRemark("倾述费支出");
			}
		}
		
		capitalIncomeExpenses4Caller.setCreateTime(new Date());
		capitalIncomeExpenses4Caller.setFinishTime(new Date());
		capitalIncomeExpensesMapper.insertSelective(capitalIncomeExpenses4Caller);
		
		// 新增被叫人账户明细
		CapitalIncomeExpenses capitalIncomeExpenses4Called = new CapitalIncomeExpenses();
		capitalIncomeExpenses4Called.setMid(calledAssets.getMid());
		capitalIncomeExpenses4Called.setOrderNo(serviceOrder.getNo());
		capitalIncomeExpenses4Called.setAmount(calledCollection);
		capitalIncomeExpenses4Called.setCashBalance(calledAssets.getCashBalance());
		capitalIncomeExpenses4Called.setItems(Byte.parseByte(AccountDetailsType.TYPE_SERVICE_INCOME.getCode().toString()));
		capitalIncomeExpenses4Called.setDirection(Byte.parseByte(AccountDetailsDirectionType.TYPE_INCOME.getCode().toString()));
		capitalIncomeExpenses4Called.setStatus(STATUS_PAYED);
		capitalIncomeExpenses4Called.setRemark("收入");
		capitalIncomeExpenses4Called.setCreateTime(new Date());
		capitalIncomeExpenses4Called.setFinishTime(new Date());
		capitalIncomeExpensesMapper.insertSelective(capitalIncomeExpenses4Called);
		
		return 0;
	}

	/**
	 * 查询条数
	 * 
	 * @param record
	 * @return
	 */
	public int selectCount(ServiceOrder record)
	{
		return serviceOrderMapper.countSelective(record);
	}
	
	/**
	 * 根据服务提供者id查询未完成订单
	 * 
	 * @param record
	 * @return
	 */
	public ServiceOrder selectUnCompleteOrderByServiceProviderId(ServiceOrder record)
	{
		return serviceOrderMapper.selectUnCompleteOrderByServiceProviderId(record);
	}
	
	/**
	 * 根据服务提供者id查询未完成订单（推选订单）
	 * 
	 * @param record
	 * @return
	 */
	public ServiceOrder selectSelectUnCompleteOrderByServiceProviderId(ServiceOrder record)
	{
		return serviceOrderMapper.selectSelectUnCompleteOrderByServiceProviderId(record);
	}
	
	/**
	 * 根据服务提供者id查询未完成订单列表
	 * 
	 * @param record
	 * @return
	 */
	public List<ServiceOrder> selectUnCompleteOrderListByServiceProviderId(ServiceOrder record)
	{
		return serviceOrderMapper.selectUnCompleteOrderListByServiceProviderId(record);
	}
	
	public List<ServiceOrder> search(String words, Byte status,
			Integer pageIndex, Integer pageSize, Byte createTimeDirection,
			Date begin, Date end)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return serviceOrderMapper.search(words, status, page.getPageStartNum(), pageSize, createTimeDirection, begin, end);
	}
	
	
	public List<ServiceOrder> searchAll(String words, Byte status,
			 Byte createTimeDirection,
			Date begin, Date end)
	{
		return serviceOrderMapper.searchAll(words, status,  createTimeDirection, begin, end);
	}
	
	public Integer countSearch(String words, Byte status, 
			Date begin, Date end)
	{
		return serviceOrderMapper.countSearch(words, status, begin, end);
	}
	
	/**
	 * 检查订单是否用尽
	 * @param soid 订单id
	 * @return
	 */
	public boolean isOrderUseUp(Long soid)
	{
		ServiceOrder serviceOrder = serviceOrderMapper.selectByPrimaryKey(soid);
		ServiceGoods serviceGoods =  serviceGoodsMapper.selectByPrimaryKey(serviceOrder.getSgid());
		
		Long maxDuration = Long.valueOf(serviceGoods.getDuration() * 60);
		Long curDur = serviceOrder.getPracticalDuration().longValue();
		if (curDur >= maxDuration.longValue()) // 超过订单最大时长，订单结束
		{
			return true;
		}
		return false;
	}
	
	public boolean isOrderUseUp(ServiceOrder serviceOrder)
	{
		ServiceGoods serviceGoods =  serviceGoodsMapper.selectByPrimaryKey(serviceOrder.getSgid());
		
		Long maxDuration = Long.valueOf(serviceGoods.getDuration() * 60);
		Long curDur = serviceOrder.getPracticalDuration().longValue();
		if (curDur >= maxDuration.longValue()) // 超过订单最大时长，订单结束
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查订单是否过期
	 * @param soid 订单id
	 * @return
	 */
	public boolean isOrderExpired(Long soid)
	{
		ServiceOrder serviceOrder = serviceOrderMapper.selectByPrimaryKey(soid);
		
		long curInterval = (new Date().getTime() - serviceOrder.getServiceRealityBeginTime().getTime()) / 1000;
		if (curInterval >= 3 * 24 * 60 * 60)// 超过3天，订单结束
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isOrderExpired(ServiceOrder serviceOrder)
	{
		long curInterval = (new Date().getTime() - serviceOrder.getServiceRealityBeginTime().getTime()) / 1000;
		if (curInterval >= 3 * 24 * 60 * 60)// 超过3天，订单结束
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 是否是未完成状态
	 * @param status 状态
	 * @return true 订单未完成
	 */
	public boolean isUncompletedStatus(Byte status)
	{
		return status.equals(OrderState.STATE_CALL_IN_PROGRESS.getCode()) ||
				status.equals(OrderState.STATE_CALL_NOT_CONNECTED.getCode()) ||
				status.equals(OrderState.STATE_CALL_NOT_CHARGEBECKS.getCode());
	}
	
	/**
	 * 刷新
	 * @param soid
	 * @return
	 */
	public Integer refreshOrderStatus(Long soid)
	{
		ServiceOrder serviceOrder = serviceOrderMapper.selectByPrimaryKey(soid);
		boolean needUpdate = false;
		if(isOrderExpired(serviceOrder))
		{//过期
			if(serviceOrder.getStatus().equals(OrderState.STATE_CALL_IN_PROGRESS.getCode()))
			{//已支付订单，设置为未评价
				serviceOrder.setStatus(OrderState.STATE_CALL_NOT_EVALUATION.getCode());
				needUpdate = true;
			}else if(serviceOrder.getStatus().equals(OrderState.STATE_CALL_NOT_CONNECTED.getCode())||
					serviceOrder.getStatus().equals(OrderState.STATE_CALL_NOT_CHARGEBECKS.getCode()))
			{//未支付订单，设置为过期
				serviceOrder.setStatus(OrderState.STATE_CALL_EXPIRED.getCode());
				needUpdate = true;
			}
		}
		
		if(isOrderUseUp(serviceOrder))
		{//时长用尽
			if(serviceOrder.getStatus().equals(OrderState.STATE_CALL_IN_PROGRESS.getCode()))
			{//已支付订单，设置为未评价
				serviceOrder.setStatus(OrderState.STATE_CALL_NOT_EVALUATION.getCode());
				needUpdate = true;
			}
		}
		
		if(needUpdate)
		{
			return serviceOrderMapper.updateByPrimaryKeySelective(serviceOrder);
		}
		
		return 0;
		
	}

	public List<ServiceOrder> getServiceOrderList(Long eeId,String words,Date begin,Date end,Integer pageIndex,Integer pageSize) {
		Page page=new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return  serviceOrderMapper.selectServiceOrderList(eeId,words,begin,end,page.getPageStartNum(),page.getPageSize());
	}

	public Integer countServiceOrder(Long eeId, String words, Date begin,
			Date end) {
		return serviceOrderMapper.countServiceOrder(eeId,words,begin,end);
	}

	/**
	 * 获取eap用户的订单信息（mid 不为空时 查询个人的eap订单信息）
	 * @param eeId
	 * @param mid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ServiceOrder> getServiceOrderListInEap(Long eeId,Long mid, Integer pageIndex, Integer pageSize) {
		Page page=new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return serviceOrderMapper.getServiceOrderListInEap(eeId,mid,page.getPageStartNum(),page.getPageSize());
	}
	
	
	public  Integer countServiceOrder(ServiceOrder serviceOrder){
		return serviceOrderMapper.countSelective(serviceOrder);
	}

	public ServiceOrder countServiceOrderElect(ServiceOrder serviceOrder) throws ParseException{
		//计算时间（当前日期的凌晨0点1分）
		serviceOrder.setCreateTime(getCompareTime());
		List<ServiceOrder> list =  serviceOrderMapper.countServiceOrderElect(serviceOrder);
		if(list.size()>0){
			return list.get(0);
		}
		else{
			return null;
		}
	}
	/**
	 * 查询是否存在订单（mid 不为空时 查询个人的eap订单信息）
	 * @param eeId
	 * @param mid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ServiceOrder> getServiceOrderByTime(Long eeId,Long mid, Integer pageIndex, Integer pageSize) {
		Page page=new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return serviceOrderMapper.getServiceOrderListInEap(eeId,mid,page.getPageStartNum(),page.getPageSize());
	}
	//返回当前时间的零点
	public Date getCompareTime() throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//24小时制
		String time = df.format(new Date())+" 00:05:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
		Date date = simpleDateFormat.parse(time); 
		return date;
	}
	
	
	public List<ServiceOrder> selectByPrimaryKeyBulk(List<Long> ids){
		return serviceOrderMapper.selectByPrimaryKeyBulk(ids);
	}
}
