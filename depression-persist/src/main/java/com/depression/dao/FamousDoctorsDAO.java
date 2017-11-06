package com.depression.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.FamousDoctors;

/**
 * 名医介绍
 * 
 * @author fanxinhui
 * @date 2016/6/20
 */
public interface FamousDoctorsDAO
{

	public void insert(FamousDoctors famousDoctors);

	public void update(FamousDoctors famousDoctors);

	public List<FamousDoctors> getValidFamousDoctorsList();
	
	// 分页数据
	public List<FamousDoctors> getPageList(FamousDoctors famousDoctors);

	// 分页总条数
	public Long getPageCounts(FamousDoctors famousDoctors);
	
	public void updateFamousDoctorsEnableByDoctIds(List<String> doctIds);
	
	public void updateFamousDoctorsDisableByDoctIds(List<String> doctIds);
	
	public FamousDoctors getFamousDoctorsByDoctId(@Param("doctId")Integer doctId);

}
