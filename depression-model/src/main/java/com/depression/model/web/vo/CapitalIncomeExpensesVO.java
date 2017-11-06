package com.depression.model.web.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.depression.model.Page;

/**
 * @author:ziye_huang
 * @date:2016年8月29日
 */

public class CapitalIncomeExpensesVO extends Page
{
	private Long ieid;
	private Long mid;
	private String no;// 订单编号
	private BigDecimal amount;// 金额
	private String pingxxPayId;// pingxx对象id
	private Byte items;// 名目：0 充值，1服务收入，2消费、3提现
	private Byte direction;// 0 收入，1支出
	private Byte channel;// 0 支付宝，1微信，2银联，3心猫
	private Byte status;// 0 正在支付，1 已完成，2正在审核
	private String remark;// 备注
	private Date createTime;
	private Date finishTime;

	public Long getIeid()
	{
		return ieid;
	}

	public void setIeid(Long ieid)
	{
		this.ieid = ieid;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public String getNo()
	{
		return no;
	}

	public void setNo(String no)
	{
		this.no = no;
	}

	public BigDecimal getAmount()
	{
		return amount;
	}

	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}

	public String getPingxxPayId()
	{
		return pingxxPayId;
	}

	public void setPingxxPayId(String pingxxPayId)
	{
		this.pingxxPayId = pingxxPayId;
	}

	public Byte getItems()
	{
		return items;
	}

	public void setItems(Byte items)
	{
		this.items = items;
	}

	public Byte getDirection()
	{
		return direction;
	}

	public void setDirection(Byte direction)
	{
		this.direction = direction;
	}

	public Byte getChannel()
	{
		return channel;
	}

	public void setChannel(Byte channel)
	{
		this.channel = channel;
	}

	public Byte getStatus()
	{
		return status;
	}

	public void setStatus(Byte status)
	{
		this.status = status;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getFinishTime()
	{
		return finishTime;
	}

	public void setFinishTime(Date finishTime)
	{
		this.finishTime = finishTime;
	}

}
