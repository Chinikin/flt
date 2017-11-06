package com.depression.entity;

/**
 * 订单状态
 * @author fanxinhui
 *
 */
public enum OrderState
{
	STATE_CHARGEBECKS(0, "正在扣款"),
	STATE_HAS_BEEN_PAID(1, "已支付"),
	STATE_IN_PROGRESS(2, "进行中"),
	STATE_COMPLETED(3, "已完成"),
	STATE_EXCEPTION_NOT_HANDLED(4, "异常未处理"),
	STATE_EXCEPTION_HANDLED(5, "异常已处理"),
	STATE_COMPLAINTS(7, "投诉处理"),
	STATE_CALL_NOT_CONNECTED(11, "电话未接听"),
	STATE_CALL_NOT_CHARGEBECKS(12, "电话未扣款"),
	STATE_CALL_IN_PROGRESS(13, "电话进行中"),
	STATE_CALL_NOT_EVALUATION(14, "电话未评价"),
	STATE_CALL_EVALUATION_FINISH(15, "电话已评价"),
	STATE_CALL_EXPIRED(16, "过期无效订单"),
	;

	/*--------------------------------------------------------*/
	private Byte code;
	private String message;

	private OrderState(Integer code, String message)
	{
		this.code = code.byteValue();
		this.message = message;
	}	
	
	private OrderState(Byte code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public Byte getCode()
	{
		return code;
	}

	public void setCode(Byte code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
