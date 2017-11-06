package com.depression.entity;

/**
 * 会员类型
 * 
 * @author fanxinhui
 * 
 */
public enum MemberType
{
	TYPE_ORDINARY(1, "ordinary"), 
	TYPE_CONSULTANT(2, "consultant"), ;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private MemberType(Integer code, String message)
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
