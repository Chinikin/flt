package com.depression.model.web.dto;

import java.util.Date;

import com.depression.model.Page;

public class WebSystemOperationLogDTO extends Page {

	private Long olid;
	
	private Long operatorId;
	
	private String no;
	
	private Date operationTime;
	
	private String operationContent;
	
	private Byte isDelete;
	
	private String mobilePhone;
	
	private String showName;
	
	private String depName;

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

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

}