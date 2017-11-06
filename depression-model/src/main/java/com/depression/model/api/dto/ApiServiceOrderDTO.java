package com.depression.model.api.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息表
 * 
 * @author hongqian_li
 * 
 */
public class ApiServiceOrderDTO
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
	 * 剩余服务时长 秒
	 */
	private Integer remainingDuration;
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
	/**
	 * 消费者实付
	 */
	private BigDecimal cashAmount;
	/**
	 * 状态, 0 正在扣款，1 已支付， 2 进行中，3已完成，4 异常未处理，5异常已处理，7投诉处理
	 */
	private Byte status;
	/**
	 * 操作人
	 */
	private String operator;
	/**
	 * 操作日志
	 */
	private String operationLog;
	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 求助者账号
	 */
	private String consumersName;
	/**
	 * 求助者电话
	 */
	private String consumersPhone;
	/**
	 * 求助者头像
	 */
	private String consumersAvatar;
	/**
	 * 求助者账户
	 */
	private String consumersImAccount;
	/**
	 * 求助者用户类型
	 */
	private Byte consumersType;
	/**
	 * 专家手机号
	 */
	private String specialistPhone;
	/**
	 * 专家姓名
	 */
	private String specialistName;
	/**
	 * 专家头像
	 */
	private String specialistAvatar;
	/**
	 * 专家头衔
	 */
	private String specialistTitle;
	/**
	 * 专家账户
	 */
	private String specialistImAccount;
	/**
	 * 专家用户类型
	 */
	private Byte specialistType;

	/**
	 * 专家实际收入
	 */
	private BigDecimal specialistRealityAmount;
	
	/**
	 * 服务者实际金额
	 */
	private BigDecimal serviceRealityAmount;
	
	/**
	 * 平台佣金总数
	 */
	private BigDecimal platformCommissionAmount;
	
	/**
	 * 产品类型
	 */
	private Byte goodsType;
	
	/**
	 * 产品价格
	 */
	private BigDecimal goodsPrice;
	
	/**
	 * 订单类型
	 */
	private Byte orderType;
	
	/**
	 * 评价id
	 */
	private Long soeId;
	
	private Long serviceRealityBeginTimestamp;
	
	/**
	 * 评分
	 */
	private Integer score;
	

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

	public Integer getRemainingDuration()
	{
		return remainingDuration;
	}

	public void setRemainingDuration(Integer remainingDuration)
	{
		this.remainingDuration = remainingDuration;
	}

	public Long getDbid()
	{
		return dbid;
	}

	public void setDbid(Long dbid)
	{
		this.dbid = dbid;
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

	public Byte getStatus()
	{
		return status;
	}

	public void setStatus(Byte status)
	{
		this.status = status;
	}

	public String getOperationLog()
	{
		return operationLog;
	}

	public void setOperationLog(String operationLog)
	{
		this.operationLog = operationLog;
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

	public String getConsumersName()
	{
		return consumersName;
	}

	public void setConsumersName(String consumersName)
	{
		this.consumersName = consumersName;
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

	public BigDecimal getPlatformCommissionAmount()
	{
		return platformCommissionAmount;
	}

	public void setPlatformCommissionAmount(BigDecimal platformCommissionAmount)
	{
		this.platformCommissionAmount = platformCommissionAmount;
	}

	public Byte getGoodsType()
	{
		return goodsType;
	}

	public void setGoodsType(Byte goodsType)
	{
		this.goodsType = goodsType;
	}

	public BigDecimal getGoodsPrice()
	{
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice)
	{
		this.goodsPrice = goodsPrice;
	}

	public String getConsumersAvatar()
	{
		return consumersAvatar;
	}

	public void setConsumersAvatar(String consumersAvatar)
	{
		this.consumersAvatar = consumersAvatar;
	}

	public String getSpecialistAvatar()
	{
		return specialistAvatar;
	}

	public void setSpecialistAvatar(String specialistAvatar)
	{
		this.specialistAvatar = specialistAvatar;
	}

	public String getSpecialistTitle()
	{
		return specialistTitle;
	}

	public void setSpecialistTitle(String specialistTitle)
	{
		this.specialistTitle = specialistTitle;
	}

	public String getConsumersImAccount()
	{
		return consumersImAccount;
	}

	public void setConsumersImAccount(String consumersImAccount)
	{
		this.consumersImAccount = consumersImAccount;
	}

	public String getSpecialistImAccount()
	{
		return specialistImAccount;
	}

	public void setSpecialistImAccount(String specialistImAccount)
	{
		this.specialistImAccount = specialistImAccount;
	}

	public Byte getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Byte orderType)
	{
		this.orderType = orderType;
	}

	public Long getSoeId()
	{
		return soeId;
	}

	public void setSoeId(Long soeId)
	{
		this.soeId = soeId;
	}

	public Integer getScore()
	{
		return score;
	}

	public void setScore(Integer score)
	{
		this.score = score;
	}

	public Byte getConsumersType()
	{
		return consumersType;
	}

	public void setConsumersType(Byte consumersType)
	{
		this.consumersType = consumersType;
	}

	public Byte getSpecialistType()
	{
		return specialistType;
	}

	public void setSpecialistType(Byte specialistType)
	{
		this.specialistType = specialistType;
	}

	public BigDecimal getSpecialistRealityAmount()
	{
		return specialistRealityAmount;
	}

	public void setSpecialistRealityAmount(BigDecimal specialistRealityAmount)
	{
		this.specialistRealityAmount = specialistRealityAmount;
	}

	public Long getServiceRealityBeginTimestamp() {
		return serviceRealityBeginTimestamp;
	}

	public void setServiceRealityBeginTimestamp(
			Long serviceRealityBeginTimestamp) {
		this.serviceRealityBeginTimestamp = serviceRealityBeginTimestamp;
	}

}
