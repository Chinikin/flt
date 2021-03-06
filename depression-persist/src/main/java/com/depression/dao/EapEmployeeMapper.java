package com.depression.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.depression.model.EapEmployee;
import com.depression.model.EapEmployeeCustom;

public interface EapEmployeeMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int insert(EapEmployee record);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int insertSelective(EapEmployee record);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	EapEmployee selectByPrimaryKey(Long eemId);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	EapEmployee selectByPrimaryKeyLock(Long eemId);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	List<EapEmployee> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int deleteByPrimaryKey(Long eemId);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids,
			@Param("enable") Byte enable);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int updateByPrimaryKey(EapEmployee record);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(EapEmployee record);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int updateByPrimaryKeySelective(EapEmployee record);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	List<EapEmployee> selectSelective(EapEmployee record);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	List<EapEmployee> selectSelectiveWithPage(EapEmployee record);
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table eap_employee
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	int countSelective(EapEmployee record);
	List<EapEmployee> searchWithPage(@Param("eeId")Long eeId, @Param("words")String words, 
			@Param("pageStartNum")Integer pageStartNum, @Param("pageSize")Integer pageSize);
	int countSearch(@Param("eeId")Long eeId, @Param("words")String words);
	
	//查询员工所在的公司id
	List<Long> selectEeIdOfEmployee(Long mid);
	
	//根据企业和会员id查询员工信息
	List<EapEmployee> selectEmployeeByMidAndEeId(@Param("mid")Long mid, @Param("eeId")Long eeId);
	
	int deleteEmployeeByEeId(Long eeId);
	//搜索用户 
	List<EapEmployeeCustom> selectWithQuery(@Param("eeId")Long eeId, @Param("words")String words,@Param("grade")Long grade,
									   @Param("startTime")Date startTime,@Param("endTime")Date endTime, @Param("college")String college,
									   @Param("isEnable")Byte isEnable,@Param("pageStartNum")Integer pageStartNum, @Param("pageSize")Integer pageSize,
									   @Param("sortTag")Integer sortTag);
	
	Integer countEapEmployeeList(@Param("eeId")Long eeId, @Param("words")String words,@Param("grade")Long grade,
								 @Param("startTime")Date startTime,@Param("endTime")Date endTime, @Param("college")String college,
								 @Param("isEnable")Byte isEnable);
	
	List<String> selectCollegesByEeId(Long eeId);
	List<Long> selectGradesByEeId(Long eeId);
	
}