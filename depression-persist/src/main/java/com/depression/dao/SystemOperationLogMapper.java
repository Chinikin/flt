package com.depression.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.SystemOperationLog;
import com.depression.model.web.dto.WebSystemOperationLogDTO;
import com.depression.model.web.vo.WebSystemOperationLogVO;

public interface SystemOperationLogMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int insert(SystemOperationLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int insertSelective(SystemOperationLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	SystemOperationLog selectByPrimaryKey(Long olid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	SystemOperationLog selectByPrimaryKeyLock(Long olid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	List<SystemOperationLog> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int deleteByPrimaryKey(Long olid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int updateByPrimaryKey(SystemOperationLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(SystemOperationLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int updateByPrimaryKeySelective(SystemOperationLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	List<SystemOperationLog> selectSelective(SystemOperationLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	List<SystemOperationLog> selectSelectiveWithPage(SystemOperationLog record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_operation_log
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int countSelective(SystemOperationLog record);
	
	/**
	 * 管理员操作日志关联查询列表
	 * @param record
	 * @return
	 */
	List<WebSystemOperationLogDTO> selectOperationLogAndUserInfoWithPage(WebSystemOperationLogVO record);

	/**
	 * 管理员操作日志关联查询条数
	 * @param record
	 * @return
	 */
	int countOperationLogAndUserInfo(WebSystemOperationLogVO record);
}
