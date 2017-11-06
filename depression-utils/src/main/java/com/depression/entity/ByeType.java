package com.depression.entity;

/**
 * 挂机类型
 * 
 * @author fanxinhui
 *
 */
public enum ByeType
{
	TYPE_NORMAL_1(1, "通话中取消回拨、直拨和外呼的正常结束通话"),
	TYPE_NORMAL_2(2, "账户欠费或者设置的通话时间到"),
	TYPE_NORMAL_3(3, "回拨通话中主叫挂断，正常结束通话"),
	TYPE_NORMAL_4(4, "回拨通话中被叫挂断，正常结束通话"),
	
	;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private ByeType(Integer code, String message)
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
