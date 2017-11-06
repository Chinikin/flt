package com.depression.entity;

/**
 * 优惠券状态
 * 
 * @author fanxinhui
 * 
 */
public enum CouponStatus
{
	COUPON_STATUS_USABLE(0, "usable"), 
	COUPON_STATUS_USED(1, "used"), 
	COUPON_STATUS_EXPIRED(2, "expired"), ;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private CouponStatus(Integer code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public Integer getCode()
	{
		return code;
	}

	public void setCode(Integer code)
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
