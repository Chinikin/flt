package com.depression.dao;

import com.depression.model.MemberTag;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberTagMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int insert(MemberTag record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int insertSelective(MemberTag record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	MemberTag selectByPrimaryKey(Long tagId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	MemberTag selectByPrimaryKeyLock(Long tagId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	List<MemberTag> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int deleteByPrimaryKey(Long tagId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Integer enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int updateByPrimaryKey(MemberTag record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(MemberTag record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int updateByPrimaryKeySelective(MemberTag record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	List<MemberTag> selectSelective(MemberTag record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	List<MemberTag> selectSelectiveWithPage(MemberTag record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_tag
	 * @mbggenerated  Wed Aug 03 19:25:20 CST 2016
	 */
	int countSelective(MemberTag record);

	List<MemberTag> selectByTypeOrderHitCount(MemberTag record);
	
	int countByType(MemberTag record);
}