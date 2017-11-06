package com.depression.entity;

public enum ErrorCode
{
	// 此处添加error code定义
	SUCCESS(0, "Suceess"),
	ERROR_DATABASE_QUERY(1, "database query failed"),
	ERROR_DATABASE_INSERT(2, "database insert failed"),
	ERROR_DATABASE_UPDATE(3, "database update failed"),
	ERROR_USERINFO_NOT_FOUND(4, "user information not found"),
	ERROR_PROPERTY_COPY(100, "property copy failed"),
	ERROR_PARAM_INCOMPLETE(101, "request param incomplete"),
	ERROR_PARAM_INVALID(102, "request param invalid"),
	ERROR_PARAM_JSON(103, "request param json error"),
	ERROR_SYSTEM_ERROR(104, "system error"),
	ERROR_PASSWORD_INCORRECT(200, "password incorrect"),
	ERROR_USERNAME_INEXISTENT(201, "username inexistent"),
	ERROR_USERNAME_EXISTED(202, "username existed"),
	ERROR_LOGIN_FAILED(203, "username or password incorrect"),	
	ERROR_PAGEINDEX_IS_NULL_ERROR(204, "pageIndex is null error"),
	ERROR_PAGESIZE_IS_NULL_ERROR(205, "pageSize is null error"),
	ERROR_STATUS_NOT_ALLOWED_ERROR(206, "status not allowed error"),
	ERROR_MOBILEPHONE_EXISTED(207, "mobile phone existed"),
	ERROR_USER_INEXISTENT(208, "user inexistent"),
	ERROR_ID_INEXISTENT(209, "id inexistent"),
	ERROR_RECORD_INUNIQUE(210, "record inunique"),
	ERROR_SYSTEM_IO(300, "request system io failed"),
	ERROR_REG_VOIP(301, "register voip failed"),
	ERROR_NOT_LOGIN(302, "not login."),
	ERROR_NOT_SHARE(303, "not share."),
	ERROR_WECHAT_LOGIN_FAILED(304, "login failed."),
	ERROR_WECHAT_NOT_LOGIN(305, "wechat not login."),
	ERROR_PSYCHO_GROUP_IN_USE(306, "psycho group in use."),
	ERROR_EMPLOYEE_NOT_BELONGS_ENTERPRISE(307, "employee not belongs to enterprise."),
	/*----------------pingxx 相关错误码---------------*/
	ERROR_PINGXX_CHARGE(401, "pingxx charge failed"),
	ERROR_PINGXX_CHANNEL(402, "pingxx channel invalid"),
	ERROR_PINGXX_TRANSFER(403, "pingxx transfer failed"),
	/*------------------订单相关错误码-----------------*/
	ERROR_ORDER_NO_INEXISTENT(501, "order no inexistent"),
	ERROR_ORDER_UNPAID(502, "order unpaid"),
	ERROR_ORDER_REFUNDED(503, "order has refunded"),
	
	/*----------------提现相关----------------------*/
	ERROR_WITHDRAW_NO_INEXISTENT(601, "withdraw no inexistent"),
	/*--------------------------------------------------------*/
	/*----------------资金账户相关----------------------*/
	ERROR_CAPITAL_PERSONAL_ASSETS_INEXISTENT(701, "capital personal assets inexistent"),
	ERROR_CAPITAL_BALANCE_INSUFFICIENT(702, "capital balance insufficient"),
	ERROR_CAPITAL_NOT_WITHDRAW_DURATION(703, "capital not withdraw duration"),
	
	/*------------------通话相关错误码-----------------*/
	ERROR_CALLER_INFO_INEXISTENT(901, "主叫方不存在"),
	ERROR_CALLED_INFO_INEXISTENT(902, "被叫方不存在"),
	ERROR_SERVICE_GOODS_INFO_INEXISTENT(903, "服务商品不存在"),
	ERROR_COMMISSION_RATE_ERROR(904, "佣金比例异常"),
	ERROR_CALL_SERVICE_CONNECTION_ERROR(905, "回拨服务连接异常"),
	ERROR_CALL_SERVICE_ELECT_ERROR(910,"今日只能产生一单"),
	ERROR_CALL_SERVICE_OUT_ERROR(911,"您的活动订单数已用完"),
	ERROR_CALL_SERVICE_OUT_MAX(912,"该咨询师今日活动订单已达到上限"),
	ERROR_CALL_TIMEOUT_ERROR(906, "订单已超时"),
	ERROR_CALL_SERVICE_IS_OVER(907, "通话已结束"),
	ERROR_SERVICE_PROVIDER_BUSY_ERROR(908, "被叫方正忙，请稍后再拨"),
	ERROR_SERVICE_PROVIDER_OFFLINE_ERROR(909, "被叫方不在线，请稍后再拨"),
	
	/*------------------文件上传相关-----------------*/	
	ERROR_UPLOAD_FILE_NOT_FOUND(1001, "upload file not found"),
	ERROR_UPLOAD_FILE_DEAL_FAILED(1002, "upload file deal failed"),
	ERROR_UPLOAD_FILE_INCORRECT(1003, "upload file incorrect"),
	
	/*------------------文件生成相关-----------------*/	
	ERROR_GENERATE_FILE_FAILED(1101, "generate file failed"),
	
	/*------------------微信用户相关-----------------*/	
	ERROR_WECHAT_HAS_BEEN_BOUND(1201, "wechat has been bound"),	
	
	/*------------------用户处罚相关-----------------*/	
	ERROR_PUNISHMENT_DISABLE_MESSAGE(1301, "punishment disable message"),
	
	/*------------------音乐相关-----------------*/	
	ERROR_MUSIC_CLASS_HAS_SONGS(1401, "music class has songs"),
	/*------------------音乐相关-----------------*/
	ERROR_AUTHORITY_CANNOT_COMMENT(1401, "no authority to comment "),
	/*------------------心友圈相关-----------------*/
	ERROR_HEARTMATE_HAS_EMBRACED(1501, "update has embraced"),
	/*------------------权限相关-----------------*/
	ERROR_AUTHORITY_NOT_ALLOW(1601, "authority not allow"),
	/*------------------登录相关-----------------*/
	ERROR_LOGIN_CODE_ERROR(2002, "验证码错误"),
	ERROR_LOGIN_CODE_EXPIRE(2003, "验证码已失效"),	
	ERROR_LOGIN_NO_USER(2004, "获取用户登录数据为空"),	
	ERROR_LOGIN_ERROR(2005, "用户名或密码错误"),	
	ERROR_LOGIN_LOSE_POWER(2006, "该用户已被禁用"),	
	ERROR_LOGIN_REPEAT(2007, "您已在别的地方登录"),	
	ERROR_LOGIN_NO_EAPID(2008, "该用户无企业信息"),
	ERROR_LOGIN_SUCCESS(2009, "登录成功"),	
	ERROR_LOGIN_PHONE_ERROR(2010, "手机号码不正确"),	
	ERROR_LOGIN_SEND_CODE_ERROR(2011, "发送验证码失败"),	
	ERROR_LOGIN_PHONE_EXIST(2012, "手机号码已存在"),	
	ERROR_LOGIN_PASSWORD_NULL(2013, "密码不能为空"),
	ERROR_LOGIN_PASSWORD_LENGTH(2014, "密码超过要求长度"),	
	ERROR_LOGIN_USERNAME_LENGTH(2015, "用户名超过要求长度"),	
	ERROR_LOGIN_EAP_NULL(2016, "eap企业不存在，请核实"),
	ERROR_LOGIN_PHONE_NOEXIST(2017, "手机号码未注册"),	
	ERROR_LOGIN_PHONE_NULL(2018, "手机号码不能为空"),	
	ERROR_LOGIN_USER_NULL(2019, "用户不存在"),
	ERROR_LOGIN_ID_NULL(2020, "ID不能为空"),	
	ERROR_LOGIN_STATUS_ERROR(2021, "状态错误：不允许的状态"),
	ERROR_LOGIN_PARAM_ERROR(2022, "参数错误"),
	ERROR_LOGIN_USERNAME_NULL(2023, "用户不能为空"),
	/*------------------权限相关-----------------*/
	ERROR_NO_LOGIN(3001, "用户未登陆或会话失效!"),
	ERROR_LOGIN_AGIAN(3002, "您已在别的地方登录，被迫退出!"),
	ERROR_NO_PERMISSION(3003, "暂无权限，请从超级管理员处获得权限!"),
	;
	/*--------------------------------------------------------*/
	private Integer code;
	private String message;

	private ErrorCode(Integer code, String message)
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
