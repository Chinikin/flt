package com.depression.dao;

import com.depression.model.ClientLog;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClientLogMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int insert(ClientLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int insertSelective(ClientLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	ClientLog selectByPrimaryKey(Long clId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	ClientLog selectByPrimaryKeyLock(Long clId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	List<ClientLog> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int deleteByPrimaryKey(Long clId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int updateByPrimaryKey(ClientLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(ClientLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int updateByPrimaryKeySelective(ClientLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	List<ClientLog> selectSelective(ClientLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	List<ClientLog> selectSelectiveWithPage(ClientLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table client_log
	 * @mbggenerated  Tue Feb 28 11:54:31 CST 2017
	 */
	int countSelective(ClientLog record);
}