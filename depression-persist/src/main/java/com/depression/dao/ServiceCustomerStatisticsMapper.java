package com.depression.dao;

import com.depression.model.ServiceCustomerStatistics;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceCustomerStatisticsMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int insert(ServiceCustomerStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int insertSelective(ServiceCustomerStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	ServiceCustomerStatistics selectByPrimaryKey(Long scsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	ServiceCustomerStatistics selectByPrimaryKeyLock(Long scsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	List<ServiceCustomerStatistics> selectByPrimaryKeyBulk(
			@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int deleteByPrimaryKey(Long scsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids,
			@Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int updateByPrimaryKey(ServiceCustomerStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(ServiceCustomerStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int updateByPrimaryKeySelective(ServiceCustomerStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	List<ServiceCustomerStatistics> selectSelective(
			ServiceCustomerStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	List<ServiceCustomerStatistics> selectSelectiveWithPage(
			ServiceCustomerStatistics record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_customer_statistics
	 * @mbggenerated  Wed Jun 14 15:19:15 CST 2017
	 */
	int countSelective(ServiceCustomerStatistics record);
}