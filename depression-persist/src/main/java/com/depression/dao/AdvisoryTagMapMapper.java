package com.depression.dao;

import com.depression.model.AdvisoryTagMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdvisoryTagMapMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int insert(AdvisoryTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int insertSelective(AdvisoryTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	AdvisoryTagMap selectByPrimaryKey(Long mapId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	AdvisoryTagMap selectByPrimaryKeyLock(Long mapId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	List<AdvisoryTagMap> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int deleteByPrimaryKey(Long mapId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Integer enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int updateByPrimaryKey(AdvisoryTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(AdvisoryTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int updateByPrimaryKeySelective(AdvisoryTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	List<AdvisoryTagMap> selectSelective(AdvisoryTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	List<AdvisoryTagMap> selectSelectiveWithPage(AdvisoryTagMap record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table advisory_tag_map
	 * @mbggenerated  Wed Sep 28 14:22:18 CST 2016
	 */
	int countSelective(AdvisoryTagMap record);
}