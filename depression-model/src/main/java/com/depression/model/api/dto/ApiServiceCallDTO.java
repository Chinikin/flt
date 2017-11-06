package com.depression.model.api.dto;

import java.util.Date;

public class ApiServiceCallDTO{

	private Long serviceOrderId;
	
	private String orderId;
	
	private String callsid;
	
	private String caller;
	
	private String called;
	
	private Long callerDuration;
	
	private Long calledDuration;
	
	private Date beginTime;
	
	private Date endTime;
	
	private Long byetype;
	
	private String recordUrl;

	public Long getServiceOrderId()
	{
		return serviceOrderId;
	}

	public void setServiceOrderId(Long serviceOrderId)
	{
		this.serviceOrderId = serviceOrderId;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getCallsid()
	{
		return callsid;
	}

	public void setCallsid(String callsid)
	{
		this.callsid = callsid;
	}

	public String getCaller()
	{
		return caller;
	}

	public void setCaller(String caller)
	{
		this.caller = caller;
	}

	public String getCalled()
	{
		return called;
	}

	public void setCalled(String called)
	{
		this.called = called;
	}

	public Long getCallerDuration()
	{
		return callerDuration;
	}

	public void setCallerDuration(Long callerDuration)
	{
		this.callerDuration = callerDuration;
	}

	public Long getCalledDuration()
	{
		return calledDuration;
	}

	public void setCalledDuration(Long calledDuration)
	{
		this.calledDuration = calledDuration;
	}

	public Date getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(Date beginTime)
	{
		this.beginTime = beginTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Long getByetype()
	{
		return byetype;
	}

	public void setByetype(Long byetype)
	{
		this.byetype = byetype;
	}

	public String getRecordUrl()
	{
		return recordUrl;
	}

	public void setRecordUrl(String recordUrl)
	{
		this.recordUrl = recordUrl;
	}

}