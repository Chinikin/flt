package com.depression.model.web.dto;

import java.math.BigDecimal;
import java.util.Date;

public class WebPGServiceOrderDTO{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.soid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment() */
    private Long soid;
    
    /*@Comment(订单类型)*/
	private Byte orderType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.sgid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(服务商品id) */
    private Long sgid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.mid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(消费者id) */
    private Long mid;
    
    private String customerPhone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.service_provider_id
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(服务提供者id) */
    private Long serviceProviderId;
    
    private String providerPhone;
    
    private String providerName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.no
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(编号) */
    private String no;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.service_begin_time
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(服务开始时间) */
    private Date serviceBeginTime;

    /**
	 *
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column service_order.practical_duration
	 *
	 * @mbggenerated Wed Sep 14 14:21:36 CST 2016
	 */
	/* @Comment(实际服务时长) */
	private Integer practicalDuration;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.cost
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment() */
    private BigDecimal cost;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.discount_amount
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment() */
    private BigDecimal discountAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.commission_rate
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(佣金比例0-100) */
    private Integer commissionRate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.cash_amount
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment() */
    private BigDecimal cashAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.status
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(状态, 0 正在扣款，1 已支付， 2 进行中，3已完成，4 异常未处理，5异常已处理，7投诉未处理， 8投诉已处理) */
    private Byte status;
    
    private Integer evaluationScore;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_order.remarks
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    /* @Comment(操作记录) */
    private String remarks;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.soid
     *
     * @return the value of service_order.soid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Long getSoid() {
        return soid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.soid
     *
     * @param soid the value for service_order.soid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setSoid(Long soid) {
        this.soid = soid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.sgid
     *
     * @return the value of service_order.sgid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Long getSgid() {
        return sgid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.sgid
     *
     * @param sgid the value for service_order.sgid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.mid
     *
     * @return the value of service_order.mid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.mid
     *
     * @param mid the value for service_order.mid
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.service_provider_id
     *
     * @return the value of service_order.service_provider_id
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.service_provider_id
     *
     * @param serviceProviderId the value for service_order.service_provider_id
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.no
     *
     * @return the value of service_order.no
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public String getNo() {
        return no;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.no
     *
     * @param no the value for service_order.no
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.service_begin_time
     *
     * @return the value of service_order.service_begin_time
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Date getServiceBeginTime() {
        return serviceBeginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.service_begin_time
     *
     * @param serviceBeginTime the value for service_order.service_begin_time
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setServiceBeginTime(Date serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.cost
     *
     * @return the value of service_order.cost
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.cost
     *
     * @param cost the value for service_order.cost
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.practical_duration
     *
     * @return the value of service_order.practical_duration
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Integer getPracticalDuration() {
        return practicalDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.practical_duration
     *
     * @param practicalDuration the value for service_order.practical_duration
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setPracticalDuration(Integer practicalDuration) {
        this.practicalDuration = practicalDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.discount_amount
     *
     * @return the value of service_order.discount_amount
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.discount_amount
     *
     * @param discountAmount the value for service_order.discount_amount
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.commission_rate
     *
     * @return the value of service_order.commission_rate
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Integer getCommissionRate() {
        return commissionRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.commission_rate
     *
     * @param commissionRate the value for service_order.commission_rate
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setCommissionRate(Integer commissionRate) {
        this.commissionRate = commissionRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.cash_amount
     *
     * @return the value of service_order.cash_amount
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.cash_amount
     *
     * @param cashAmount the value for service_order.cash_amount
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.status
     *
     * @return the value of service_order.status
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.status
     *
     * @param status the value for service_order.status
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_order.remarks
     *
     * @return the value of service_order.remarks
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_order.remarks
     *
     * @param remarks the value for service_order.remarks
     *
     * @mbggenerated Wed Sep 14 14:21:36 CST 2016
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

	public Byte getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Byte orderType)
	{
		this.orderType = orderType;
	}

	public String getCustomerPhone()
	{
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone)
	{
		this.customerPhone = customerPhone;
	}

	public String getProviderPhone()
	{
		return providerPhone;
	}

	public void setProviderPhone(String providerPhone)
	{
		this.providerPhone = providerPhone;
	}

	public Integer getEvaluationScore()
	{
		return evaluationScore;
	}

	public void setEvaluationScore(Integer evaluationScore)
	{
		this.evaluationScore = evaluationScore;
	}

	public String getProviderName()
	{
		return providerName;
	}

	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}
}