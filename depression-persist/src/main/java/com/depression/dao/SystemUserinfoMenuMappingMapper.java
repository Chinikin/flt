package com.depression.dao;

import com.depression.model.SystemUserinfoMenuMapping;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemUserinfoMenuMappingMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int insert(SystemUserinfoMenuMapping record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int insertSelective(SystemUserinfoMenuMapping record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	SystemUserinfoMenuMapping selectByPrimaryKey(Long mappingId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	SystemUserinfoMenuMapping selectByPrimaryKeyLock(Long mappingId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	List<SystemUserinfoMenuMapping> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int deleteByPrimaryKey(Long mappingId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int updateByPrimaryKey(SystemUserinfoMenuMapping record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(SystemUserinfoMenuMapping record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int updateByPrimaryKeySelective(SystemUserinfoMenuMapping record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	List<SystemUserinfoMenuMapping> selectSelective(SystemUserinfoMenuMapping record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	List<SystemUserinfoMenuMapping> selectSelectiveWithPage(SystemUserinfoMenuMapping record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table system_userinfo_menu_mapping
	 * @mbggenerated  Sat Sep 10 15:24:00 CST 2016
	 */
	int countSelective(SystemUserinfoMenuMapping record);
	
	/**
	 * 根据用户id修改记录状态
	 * 
	 * @param ids
	 * @param enable
	 * @return
	 */
	int modifyStatusByUserId(@Param("userId") Long userId, @Param("enable") Byte enable);
}