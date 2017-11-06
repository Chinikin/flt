package com.depression.model.eap.dto;

import java.math.BigDecimal;
import java.util.Date;

public class EapServiceOrderDTO {
	
	 /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.soid
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private Long soid;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.sgid
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(服务商品id) */
   private Long sgid;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.mid
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(消费者id) */
   private Long mid;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.service_provider_id
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(服务提供者id) */
   private Long serviceProviderId;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.no
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(编号) */
   private String no;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.create_time
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(创建时间) */
   private Date createTime;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.service_begin_time
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(服务开始时间) */
   private Date serviceBeginTime;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.service_reality_begin_time
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(服务实际开始时间) */
   private Date serviceRealityBeginTime;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.goods_quantity
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(服务商品数量) */
   private Integer goodsQuantity;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.cost
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private BigDecimal cost;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.practical_duration
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(实际服务时长) */
   private Integer practicalDuration;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.dbid
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private Long dbid;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.discount_amount
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private BigDecimal discountAmount;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.commission_rate
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(佣金比例0-100) */
   private Integer commissionRate;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.cash_amount
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private BigDecimal cashAmount;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.status
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(状态, 0 正在扣款，1 已支付， 2 进行中，3已完成，4 异常未处理，5异常已处理，7投诉未处理， 8投诉已处理) */
   private Byte status;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.refund_status
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(0 为退款 1 现金退款 2 原封退款) */
   private Byte refundStatus;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.refund_platform_amount
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(平台退款金额) */
   private BigDecimal refundPlatformAmount;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.refund_server_amount
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(咨询师退款金额) */
   private BigDecimal refundServerAmount;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.order_type
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(订单类型: 0 普通订单，1 eap订单) */
   private Byte orderType;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.ee_id
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(企业id) */
   private Long eeId;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.operator
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private String operator;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.operation_log
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(操作记录) */
   private String operationLog;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.remarks
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private String remarks;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.invisible_user
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(用户不可见 0可见 1不可见) */
   private Byte invisibleUser;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.invisible_psycho
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment(咨询师不可见 0可见 1不可见) */
   private Byte invisiblePsycho;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column service_order.is_enable
    *
    * @mbggenerated Sat Mar 25 11:26:31 CST 2017
    */
   /* @Comment() */
   private Byte isEnable;
   
   //求助者姓名
   private String name;
   
   //求助者手机号
   private String phoneNum;
   
   //专家姓名
   private String psyName;
   
   //咨询形式
   private String apptForm;
   
   //专家手机号
   private String psyPhoneNum;
   

public Long getSoid() {
	return soid;
}


public void setSoid(Long soid) {
	this.soid = soid;
}


public Long getSgid() {
	return sgid;
}


public void setSgid(Long sgid) {
	this.sgid = sgid;
}


public Long getMid() {
	return mid;
}


public void setMid(Long mid) {
	this.mid = mid;
}


public Long getServiceProviderId() {
	return serviceProviderId;
}


public void setServiceProviderId(Long serviceProviderId) {
	this.serviceProviderId = serviceProviderId;
}


public String getNo() {
	return no;
}


public void setNo(String no) {
	this.no = no;
}


public Date getCreateTime() {
	return createTime;
}


public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}


public Date getServiceBeginTime() {
	return serviceBeginTime;
}


public void setServiceBeginTime(Date serviceBeginTime) {
	this.serviceBeginTime = serviceBeginTime;
}


public Date getServiceRealityBeginTime() {
	return serviceRealityBeginTime;
}


public void setServiceRealityBeginTime(Date serviceRealityBeginTime) {
	this.serviceRealityBeginTime = serviceRealityBeginTime;
}


public Integer getGoodsQuantity() {
	return goodsQuantity;
}


public void setGoodsQuantity(Integer goodsQuantity) {
	this.goodsQuantity = goodsQuantity;
}


public BigDecimal getCost() {
	return cost;
}


public void setCost(BigDecimal cost) {
	this.cost = cost;
}


public Integer getPracticalDuration() {
	return practicalDuration;
}


public void setPracticalDuration(Integer practicalDuration) {
	this.practicalDuration = practicalDuration;
}


public Long getDbid() {
	return dbid;
}


public void setDbid(Long dbid) {
	this.dbid = dbid;
}


public BigDecimal getDiscountAmount() {
	return discountAmount;
}


public void setDiscountAmount(BigDecimal discountAmount) {
	this.discountAmount = discountAmount;
}


public Integer getCommissionRate() {
	return commissionRate;
}


public void setCommissionRate(Integer commissionRate) {
	this.commissionRate = commissionRate;
}


public BigDecimal getCashAmount() {
	return cashAmount;
}


public void setCashAmount(BigDecimal cashAmount) {
	this.cashAmount = cashAmount;
}


public Byte getStatus() {
	return status;
}


public void setStatus(Byte status) {
	this.status = status;
}


public Byte getRefundStatus() {
	return refundStatus;
}


public void setRefundStatus(Byte refundStatus) {
	this.refundStatus = refundStatus;
}


public BigDecimal getRefundPlatformAmount() {
	return refundPlatformAmount;
}


public void setRefundPlatformAmount(BigDecimal refundPlatformAmount) {
	this.refundPlatformAmount = refundPlatformAmount;
}


public BigDecimal getRefundServerAmount() {
	return refundServerAmount;
}


public void setRefundServerAmount(BigDecimal refundServerAmount) {
	this.refundServerAmount = refundServerAmount;
}


public Byte getOrderType() {
	return orderType;
}


public void setOrderType(Byte orderType) {
	this.orderType = orderType;
}


public Long getEeId() {
	return eeId;
}


public void setEeId(Long eeId) {
	this.eeId = eeId;
}


public String getOperator() {
	return operator;
}


public void setOperator(String operator) {
	this.operator = operator;
}


public String getOperationLog() {
	return operationLog;
}


public void setOperationLog(String operationLog) {
	this.operationLog = operationLog;
}


public String getRemarks() {
	return remarks;
}


public void setRemarks(String remarks) {
	this.remarks = remarks;
}


public Byte getInvisibleUser() {
	return invisibleUser;
}


public void setInvisibleUser(Byte invisibleUser) {
	this.invisibleUser = invisibleUser;
}


public Byte getInvisiblePsycho() {
	return invisiblePsycho;
}


public void setInvisiblePsycho(Byte invisiblePsycho) {
	this.invisiblePsycho = invisiblePsycho;
}


public Byte getIsEnable() {
	return isEnable;
}


public void setIsEnable(Byte isEnable) {
	this.isEnable = isEnable;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public String getPhoneNum() {
	return phoneNum;
}


public void setPhoneNum(String phoneNum) {
	this.phoneNum = phoneNum;
}


public String getPsyName() {
	return psyName;
}


public void setPsyName(String psyName) {
	this.psyName = psyName;
}


public String getApptForm() {
	return apptForm;
}


public void setApptForm(String apptForm) {
	this.apptForm = apptForm;
}


public String getPsyPhoneNum() {
	return psyPhoneNum;
}


public void setPsyPhoneNum(String psyPhoneNum) {
	this.psyPhoneNum = psyPhoneNum;
}


  
   

}
