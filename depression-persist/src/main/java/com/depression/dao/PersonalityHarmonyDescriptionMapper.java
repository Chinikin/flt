package com.depression.dao;

import com.depression.model.PersonalityHarmonyDescription;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PersonalityHarmonyDescriptionMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int insert(PersonalityHarmonyDescription record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int insertSelective(PersonalityHarmonyDescription record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	PersonalityHarmonyDescription selectByPrimaryKey(Long harmonyId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	PersonalityHarmonyDescription selectByPrimaryKeyLock(Long harmonyId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	List<PersonalityHarmonyDescription> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int deleteByPrimaryKey(Long harmonyId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int updateByPrimaryKey(PersonalityHarmonyDescription record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(PersonalityHarmonyDescription record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int updateByPrimaryKeySelective(PersonalityHarmonyDescription record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	List<PersonalityHarmonyDescription> selectSelective(PersonalityHarmonyDescription record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	List<PersonalityHarmonyDescription> selectSelectiveWithPage(PersonalityHarmonyDescription record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table personality_harmony_description
	 * @mbggenerated  Fri Aug 12 19:52:27 CST 2016
	 */
	int countSelective(PersonalityHarmonyDescription record);
}
