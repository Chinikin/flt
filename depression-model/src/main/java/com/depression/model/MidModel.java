package com.depression.model;

/**
 * @author:ziye_huang
 * @date:2016年8月29日
 */

public class MidModel extends Page
{
	private Long mid;
	private Byte items;// 名目：0 充值，1服务收入，2消费、3提现

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Byte getItems()
	{
		return items;
	}

	public void setItems(Byte items)
	{
		this.items = items;
	}

}
