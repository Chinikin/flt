package com.depression.entity;

/**
 * 呼叫类型
 * 
 * @author fanxinhui
 *
 */
public enum CallType
{
	TYPE_ADVISORY(0, "咨询"),
	TYPE_POUR(1, "倾述"),
	;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private CallType(Integer code, String message)
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
