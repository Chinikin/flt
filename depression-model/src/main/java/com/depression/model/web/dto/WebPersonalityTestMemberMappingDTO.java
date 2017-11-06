package com.depression.model.web.dto;

import java.util.Date;

import com.depression.model.Page;

public class WebPersonalityTestMemberMappingDTO extends Page
{

	/* @Comment(主键) */
	private Long mappingId;

	/* @Comment() */
	private Long mid;

	/* @Comment() */
	private Long ptrdId;

	/* @Comment() */
	private Date createTime;

	/* @Comment() */
	private Date modifyTime;

	/* @Comment(0启用，1禁用) */
	private Byte isEnable;

	/* @Comment(0正常，1删除) */
	private Byte isDelete;

	/* @Comment(16种人格) */
	private String type;

	/* @Comment(描述) */
	private String descp;
	
	/* @Comment(M中D选项总数-L中D选项总数) */
    private Integer dVal;

    /* @Comment(M中I选项总数-L中I选项总数) */
    private Integer iVal;

    /* @Comment(M中S选项总数-L中S选项总数) */
    private Integer sVal;

    /* @Comment(M中C选项总数-L中C选项总数) */
    private Integer cVal;

	public Long getMappingId()
	{
		return mappingId;
	}

	public void setMappingId(Long mappingId)
	{
		this.mappingId = mappingId;
	}

	public Long getMid()
	{
		return mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public Long getPtrdId()
	{
		return ptrdId;
	}

	public void setPtrdId(Long ptrdId)
	{
		this.ptrdId = ptrdId;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public Byte getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Byte isEnable)
	{
		this.isEnable = isEnable;
	}

	public Byte getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Byte isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDescp()
	{
		return descp;
	}

	public void setDescp(String descp)
	{
		this.descp = descp;
	}

	public Integer getdVal()
	{
		return dVal;
	}

	public void setdVal(Integer dVal)
	{
		this.dVal = dVal;
	}

	public Integer getiVal()
	{
		return iVal;
	}

	public void setiVal(Integer iVal)
	{
		this.iVal = iVal;
	}

	public Integer getsVal()
	{
		return sVal;
	}

	public void setsVal(Integer sVal)
	{
		this.sVal = sVal;
	}

	public Integer getcVal()
	{
		return cVal;
	}

	public void setcVal(Integer cVal)
	{
		this.cVal = cVal;
	}

}
