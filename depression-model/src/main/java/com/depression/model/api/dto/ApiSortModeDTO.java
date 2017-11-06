package com.depression.model.api.dto;

public class ApiSortModeDTO
{
	
	public ApiSortModeDTO()
	{
		super();
	}
	
	public ApiSortModeDTO(Integer code, String name)
	{
		super();
		this.code = code;
		this.name = name;
	}
	
	private Integer code;
	private String name;
	public Integer getCode()
	{
		return code;
	}
	public void setCode(Integer code)
	{
		this.code = code;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}	
}
