package com.depression.entity;

/**
 * 账户明细类型
 * 
 * @author fanxinhui
 * 
 */
public enum AccountDetailsType
{
	TYPE_DEPOSIT(0, "充值"), 
	TYPE_SERVICE_INCOME(1, "服务收入"), 
	TYPE_CONSUME(2, "消费"), 
	TYPE_WITHDRAW_DEPOSIT(3, "提现"), 
	TYPE_REFUND(4, "退款"), 
	;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private AccountDetailsType(Integer code, String message)
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
