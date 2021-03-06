package com.depression.dao;

import com.depression.model.ServiceGoods;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceGoodsMapper
{

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int insert(ServiceGoods record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int insertSelective(ServiceGoods record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	ServiceGoods selectByPrimaryKey(Long sgid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	ServiceGoods selectByPrimaryKeyLock(Long sgid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	List<ServiceGoods> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int deleteByPrimaryKey(Long sgid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int updateByPrimaryKey(ServiceGoods record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(ServiceGoods record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int updateByPrimaryKeySelective(ServiceGoods record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	List<ServiceGoods> selectSelective(ServiceGoods record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	List<ServiceGoods> selectSelectiveWithPage(ServiceGoods record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table service_goods
	 * 
	 * @mbggenerated Sat Sep 10 15:13:24 CST 2016
	 */
	int countSelective(ServiceGoods record);

	ServiceGoods selectByTimes(Integer startAnswerCount, Integer endAnswerCount);
}