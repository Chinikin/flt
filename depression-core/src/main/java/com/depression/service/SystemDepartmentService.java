package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.SystemDepartmentMapper;
import com.depression.model.SystemDepartment;

@Service
public class SystemDepartmentService
{
	@Autowired
	SystemDepartmentMapper systemDepartmentMapper;

	public int insert(SystemDepartment record)
	{
		return systemDepartmentMapper.insert(record);
	}
	
	public SystemDepartment selectByPrimaryKey(Long dptId)
	{
		return systemDepartmentMapper.selectByPrimaryKey(dptId);
	}
	
	public int updateByPrimaryKey(SystemDepartment record)
	{
		return systemDepartmentMapper.updateByPrimaryKey(record);
	}
	
	public List<SystemDepartment> selectSelective(SystemDepartment record)
	{
		return systemDepartmentMapper.selectSelective(record);
	}
}
