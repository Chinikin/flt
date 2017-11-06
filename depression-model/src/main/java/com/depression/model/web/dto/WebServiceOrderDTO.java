package com.depression.model.web.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息表
 * 
 * @author hongqian_li
 * 
 */
public class WebServiceOrderDTO
{
	/**
	 * 主键
	 */
	private Long soid;
	/**
	 * 商品ID
	 */
	private Long sgid;
	/**
	 * 消费者id
	 */
	private Long mid;
	/**
	 * 服务者ID（咨询师ID）
	 */
	private Long serviceProviderId;
	/**
	 * 订单号
	 */
	private String no;
	/**
	 * 服务开始时间
	 */
	private Date serviceBeginTime;
	/**
	 * 服务实际开始时间
	 */
	private Date serviceRealityBeginTime;
	/**
	 * 服务商品数量
	 */
	private Integer goodsQuantity;
	/**
	 * 本次服务费用
	 */
	private BigDecimal cost;
	/**
	 * 实际服务时长 秒
	 */
	private Integer practicalDuration;
	/**
	 * 优惠券id
	 */
    private Long dbid;
	/**
	 * 优惠券抵扣金额
	 */
	private BigDecimal discountAmount;
	/**
	 * 佣金比例
	 */
	private Integer commissionRate;
	
	private BigDecimal platformIncome;
	/**
	 * 消费者实付
	 */
	private BigDecimal cashAmount;
	
    /* @Comment(状态, 0 正在扣款，1 已支付， 2 进行中，3已完成，4 异常未处理，5异常已处理，7投诉处理) */
    private Byte status;
    
    /* @Comment(0 为退款 1 现金退款 2 原封退款) */
    private Byte refundStatus;
	/**
	 * 操作人
	 */
	private String operator;
	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 求助者账号
	 */
	private String consumersAccount;
	/**
	 * 求助者电话
	 */
	private String consumersPhone;
	/**
	 * 专家手机号
	 */
	private String specialistPhone;
	/**
	 * 专家姓名
	 */
	private String specialistName;
	
	private Byte pLevel;
	
	private BigDecimal recommandPrice;

	/**
	 * 服务者实际金额
	 */
	private BigDecimal serviceRealityAmount;

	/**
	 * 咨询类型 0  实时语音咨询，1 实时语音倾述
	 */
	private Byte consultType;
	
	public Long getSoid()
	{
		return soid;
	}

	public void setSoid(Long soid)
	{
		this.soid = soid;
	}

	public Long getSgid()
	{
		return sgid;
	}

	public void setSgid(Long sgid)
	{
		this.sgid = sgid;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Long getServiceProviderId()
	{
		return serviceProviderId;
	}

	public void setServiceProviderId(Long serviceProviderId)
	{
		this.serviceProviderId = serviceProviderId;
	}

	public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	}

	public Date getServiceBeginTime()
	{
		return serviceBeginTime;
	}

	public void setServiceBeginTime(Date serviceBeginTime)
	{
		this.serviceBeginTime = serviceBeginTime;
	}

	public Date getServiceRealityBeginTime()
	{
		return serviceRealityBeginTime;
	}

	public void setServiceRealityBeginTime(Date serviceRealityBeginTime)
	{
		this.serviceRealityBeginTime = serviceRealityBeginTime;
	}

	public Integer getGoodsQuantity()
	{
		return goodsQuantity;
	}

	public void setGoodsQuantity(Integer goodsQuantity)
	{
		this.goodsQuantity = goodsQuantity;
	}

	public BigDecimal getCost()
	{
		return cost;
	}

	public void setCost(BigDecimal cost)
	{
		this.cost = cost;
	}

	public Integer getPracticalDuration()
	{
		return practicalDuration;
	}

	public void setPracticalDuration(Integer practicalDuration)
	{
		this.practicalDuration = practicalDuration;
	}

	
	
	public BigDecimal getPlatformIncome()
	{
		return platformIncome;
	}

	public void setPlatformIncome(BigDecimal platformIncome)
	{
		this.platformIncome = platformIncome;
	}

	public BigDecimal getDiscountAmount()
	{
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount)
	{
		this.discountAmount = discountAmount;
	}

	public Integer getCommissionRate()
	{
		return commissionRate;
	}

	public void setCommissionRate(Integer commissionRate)
	{
		this.commissionRate = commissionRate;
	}

	public BigDecimal getCashAmount()
	{
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount)
	{
		this.cashAmount = cashAmount;
	}
	
	public Long getDbid()
	{
		return dbid;
	}

	public void setDbid(Long dbid)
	{
		this.dbid = dbid;
	}

	public Byte getStatus()
	{
		return status;
	}

	public void setStatus(Byte status)
	{
		this.status = status;
	}

	public Byte getRefundStatus()
	{
		return refundStatus;
	}

	public void setRefundStatus(Byte refundStatus)
	{
		this.refundStatus = refundStatus;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public String getConsumersAccount()
	{
		return consumersAccount;
	}

	public void setConsumersAccount(String consumersAccount)
	{
		this.consumersAccount = consumersAccount;
	}

	public String getConsumersPhone()
	{
		return consumersPhone;
	}

	public void setConsumersPhone(String consumersPhone)
	{
		this.consumersPhone = consumersPhone;
	}

	public String getSpecialistPhone()
	{
		return specialistPhone;
	}

	public void setSpecialistPhone(String specialistPhone)
	{
		this.specialistPhone = specialistPhone;
	}

	public String getSpecialistName()
	{
		return specialistName;
	}

	public void setSpecialistName(String specialistName)
	{
		this.specialistName = specialistName;
	}

	public BigDecimal getServiceRealityAmount()
	{
		return serviceRealityAmount;
	}

	public void setServiceRealityAmount(BigDecimal serviceRealityAmount)
	{
		this.serviceRealityAmount = serviceRealityAmount;
	}

	public Byte getConsultType()
	{
		return consultType;
	}

	public void setConsultType(Byte consultType)
	{
		this.consultType = consultType;
	}

	public Byte getpLevel() {
		return pLevel;
	}

	public void setpLevel(Byte pLevel) {
		this.pLevel = pLevel;
	}


	public BigDecimal getRecommandPrice() {
		return recommandPrice;
	}

	public void setRecommandPrice(BigDecimal recommandPrice) {
		this.recommandPrice = recommandPrice;
	}

}
