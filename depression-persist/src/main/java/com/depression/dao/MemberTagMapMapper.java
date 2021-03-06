package com.depression.dao;

import com.depression.model.MemberTagMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberTagMapMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int insert(MemberTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int insertSelective(MemberTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	MemberTagMap selectByPrimaryKey(Long mapId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	MemberTagMap selectByPrimaryKeyLock(Long mapId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	List<MemberTagMap> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int deleteByPrimaryKey(Long mapId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Integer enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int updateByPrimaryKey(MemberTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(MemberTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int updateByPrimaryKeySelective(MemberTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	List<MemberTagMap> selectSelective(MemberTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	List<MemberTagMap> selectSelectiveWithPage(MemberTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag_map
	 * @mbggenerated  Wed Aug 03 11:18:15 CST 2016
	 */
	int countSelective(MemberTagMap record);
}