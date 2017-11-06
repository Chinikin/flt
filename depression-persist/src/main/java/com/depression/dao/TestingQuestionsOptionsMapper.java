package com.depression.dao;

import com.depression.model.TestingQuestionsOptions;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TestingQuestionsOptionsMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int insert(TestingQuestionsOptions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int insertSelective(TestingQuestionsOptions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	TestingQuestionsOptions selectByPrimaryKey(Long optionsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	TestingQuestionsOptions selectByPrimaryKeyLock(Long optionsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	List<TestingQuestionsOptions> selectByPrimaryKeyBulk(
			@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int deleteByPrimaryKey(Long optionsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids,
			@Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int updateByPrimaryKey(TestingQuestionsOptions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(TestingQuestionsOptions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int updateByPrimaryKeySelective(TestingQuestionsOptions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	List<TestingQuestionsOptions> selectSelective(TestingQuestionsOptions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	List<TestingQuestionsOptions> selectSelectiveWithPage(
			TestingQuestionsOptions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions_options
	 * @mbggenerated  Tue Mar 21 11:20:59 CST 2017
	 */
	int countSelective(TestingQuestionsOptions record);

	void updateTestingQuestionsOptionsStatusByQuestionsId(@Param("questionsId")Long questionsId,@Param("isDel")byte isDel);

	void updateTestingQuestionsOptionsByOptionsId(@Param("idList")List<Long> idList,@Param("isDel")byte isDel);
}