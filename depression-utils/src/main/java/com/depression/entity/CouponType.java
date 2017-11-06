package com.depression.entity;

/**
 * 优惠券类型
 * @author fanxinhui
 *
 */
public enum CouponType
{
	TYPE_CASH(0, "cash"),
	TYPE_DISCOUNT(1, "discount"),
	;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private CouponType(Integer code, String message)
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
