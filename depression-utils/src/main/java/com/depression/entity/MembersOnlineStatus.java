package com.depression.entity;

/**
 * 会员状态
 * 
 * @author fanxinhui
 * 
 */
public enum MembersOnlineStatus
{
	STATUS_NOT_ONLINE(0, "不在线"), 
	STATUS_ONLINE(1, "在线"), 
	STATUS_IN_THE_CALL(2, "通话中"), ;

	/*--------------------------------------------------------*/
	private Byte code;
	private String message;

	private MembersOnlineStatus(Integer code, String message)
	{
		this.code = code.byteValue();
		this.message = message;
	}


	private MembersOnlineStatus(Byte code, String message)
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
