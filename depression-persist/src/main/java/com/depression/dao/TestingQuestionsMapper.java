package com.depression.dao;

import com.depression.model.TestingQuestions;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TestingQuestionsMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int insert(TestingQuestions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int insertSelective(TestingQuestions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	TestingQuestions selectByPrimaryKey(Long questionsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	TestingQuestions selectByPrimaryKeyLock(Long questionsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	List<TestingQuestions> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int deleteByPrimaryKey(Long questionsId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids,
			@Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int updateByPrimaryKey(TestingQuestions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(TestingQuestions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int updateByPrimaryKeySelective(TestingQuestions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	List<TestingQuestions> selectSelective(TestingQuestions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	List<TestingQuestions> selectSelectiveWithPage(TestingQuestions record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table testing_questions
	 * @mbggenerated  Tue Mar 21 11:09:02 CST 2017
	 */
	int countSelective(TestingQuestions record);

	void updateTestingQuestionsStatusByQuestionsIds(@Param("idList")List<Long> idList,@Param("isDel")byte isDel);
}