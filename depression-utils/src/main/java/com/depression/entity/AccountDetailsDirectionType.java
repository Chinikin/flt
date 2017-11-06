package com.depression.entity;

/**
 * 账户消费方向类型
 * 
 * @author fanxinhui
 *
 */
public enum AccountDetailsDirectionType
{
	TYPE_INCOME(0, "收入"),
	TYPE_EXPEND(1, "支出"),
	;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private AccountDetailsDirectionType(Integer code, String message)
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
