package com.depression.dao;

import com.depression.model.CapitalPlatformCash;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CapitalPlatformCashMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int insert(CapitalPlatformCash record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int insertSelective(CapitalPlatformCash record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	CapitalPlatformCash selectByPrimaryKey(Long pcid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	CapitalPlatformCash selectByPrimaryKeyLock(Long pcid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	List<CapitalPlatformCash> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int deleteByPrimaryKey(Long pcid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int updateByPrimaryKey(CapitalPlatformCash record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(CapitalPlatformCash record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int updateByPrimaryKeySelective(CapitalPlatformCash record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	List<CapitalPlatformCash> selectSelective(CapitalPlatformCash record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	List<CapitalPlatformCash> selectSelectiveWithPage(CapitalPlatformCash record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table capital_platform_cash
	 * @mbggenerated  Tue Aug 30 13:47:42 CST 2016
	 */
	int countSelective(CapitalPlatformCash record);

	/**
	 * 获取表中唯一一条记录，根据业务要求，平台账户表中只允许存在一条有效记录
	 * 
	 * @return
	 */
	CapitalPlatformCash selectRecordLimitOne();
}
