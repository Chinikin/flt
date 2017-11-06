package com.depression.model.web.vo;

import com.depression.model.CapitalWithdrawRequest;

/**
 * 
 * @author hongqian_li
 */
public class WebCapitalWithdrawRequestVO extends CapitalWithdrawRequest
{
	private String operationName;

	public String getOperationName()
	{
		return operationName;
	}

	public void setOperationName(String operationName)
	{
		this.operationName = operationName;
	}

}
