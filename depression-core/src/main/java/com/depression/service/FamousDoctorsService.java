package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.FamousDoctorsDAO;
import com.depression.model.FamousDoctors;

/**
 * 名医介绍
 * 
 * @author fanxinhui
 * @date 2016/6/20
 */
@Service
public class FamousDoctorsService
{
	@Autowired
	FamousDoctorsDAO dao;

	public void insert(FamousDoctors entity)
	{
		dao.insert(entity);
	}

	public void update(FamousDoctors entity)
	{
		dao.update(entity);
	}
	
	public List<FamousDoctors> getValidFamousDoctorsList()
	{
		return dao.getValidFamousDoctorsList();
	}
	
	public List<FamousDoctors> getPageList(FamousDoctors famousDoctors)
	{
		return dao.getPageList(famousDoctors);
	}
	
	public Long getPageCounts(FamousDoctors famousDoctors)
	{
		return dao.getPageCounts(famousDoctors);
	}
	
	public void updateFamousDoctorsEnableByDoctIds(List<String> doctIds)
	{
		dao.updateFamousDoctorsEnableByDoctIds(doctIds);
	}
	
	public void updateFamousDoctorsDisableByDoctIds(List<String> doctIds)
	{
		dao.updateFamousDoctorsDisableByDoctIds(doctIds);
	}
	
	public FamousDoctors getFamousDoctorsByDoctId(Integer doctId)
	{
		return dao.getFamousDoctorsByDoctId(doctId);
	}
}
