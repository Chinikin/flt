package com.depression.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

import com.depression.model.CapitalPlatformUnitFundStatistics;
import com.depression.model.web.dto.CapitalPlatformUnitFundStatisticsDTO;

public interface CapitalPlatformUnitFundStatisticsMapper
{

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int insert(CapitalPlatformUnitFundStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int insertSelective(CapitalPlatformUnitFundStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	CapitalPlatformUnitFundStatistics selectByPrimaryKey(Long fsid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	CapitalPlatformUnitFundStatistics selectByPrimaryKeyLock(Long fsid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	List<CapitalPlatformUnitFundStatistics> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int deleteByPrimaryKey(Long fsid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int updateByPrimaryKey(CapitalPlatformUnitFundStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(CapitalPlatformUnitFundStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int updateByPrimaryKeySelective(CapitalPlatformUnitFundStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	List<CapitalPlatformUnitFundStatistics> selectSelective(CapitalPlatformUnitFundStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	List<CapitalPlatformUnitFundStatistics> selectSelectiveWithPage(CapitalPlatformUnitFundStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_unit_fund_statistics
	 * 
	 * @mbggenerated Tue Aug 30 14:19:50 CST 2016
	 */
	int countSelective(CapitalPlatformUnitFundStatistics record);

	/**
	 * 查询平台收支记录
	 * 
	 * @return
	 */
	List<CapitalPlatformUnitFundStatisticsDTO> selectIncomeStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	int selectIncomeStatisticsCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	List<String> selectDateSet();
}