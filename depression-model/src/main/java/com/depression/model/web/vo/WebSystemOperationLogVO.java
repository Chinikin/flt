package com.depression.model.web.vo;

import java.util.Date;

import com.depression.model.Page;

public class WebSystemOperationLogVO extends Page {

	private Long olid;
	
	private Long operatorId;
	
	private String no;
	
	private Date operationTime;
	
	private String operationContent;
	
	private Byte isDelete;
	
	private Long dptId;
	
	private String mobilePhone;
	
	private String showName;
	
	private Date begin;
	
	private Date end;

	public Long getOlid()
	{
		return olid;
	}

	public void setOlid(Long olid)
	{
		this.olid = olid;
	}

	public Long getOperatorId()
	{
		return operatorId;
	}

	public void setOperatorId(Long operatorId)
	{
		this.operatorId = operatorId;
	}

	public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	}

	public Date getOperationTime()
	{
		return operationTime;
	}

	public void setOperationTime(Date operationTime)
	{
		this.operationTime = operationTime;
	}

	public String getOperationContent()
	{
		return operationContent;
	}

	public void setOperationContent(String operationContent)
	{
		this.operationContent = operationContent;
	}

	public Byte getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Byte isDelete)
	{
		this.isDelete = isDelete;
	}

	public Long getDptId()
	{
		return dptId;
	}

	public void setDptId(Long dptId)
	{
		this.dptId = dptId;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public String getShowName()
	{
		return showName;
	}

	public void setShowName(String showName)
	{
		this.showName = showName;
	}

	public Date getBegin()
	{
		return begin;
	}

	public void setBegin(Date begin)
	{
		this.begin = begin;
	}

	public Date getEnd()
	{
		return end;
	}

	public void setEnd(Date end)
	{
		this.end = end;
	}

}