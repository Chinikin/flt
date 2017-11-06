package com.depression.entity;

/**
 * 后台管理员用户类型
 * 
 * @author fanxinhui
 * 
 */
public enum AdminUserType
{
	TYPE_ADMIN_ORDINARY(11, "typeAdminOrdinary"), TYPE_ADMIN_SUPER(99, "typeAdminSuper"), ;

	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private AdminUserType(Integer code, String message)
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
