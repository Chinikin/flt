package com.depression.dao;

import com.depression.model.EvaluationLabel;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EvaluationLabelMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int insert(EvaluationLabel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int insertSelective(EvaluationLabel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	EvaluationLabel selectByPrimaryKey(Long elId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	EvaluationLabel selectByPrimaryKeyLock(Long elId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	List<EvaluationLabel> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int deleteByPrimaryKey(Long elId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int updateByPrimaryKey(EvaluationLabel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int updateByPrimaryKeyWithBLOBs(EvaluationLabel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int updateByPrimaryKeySelective(EvaluationLabel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	List<EvaluationLabel> selectSelective(EvaluationLabel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	List<EvaluationLabel> selectSelectiveWithPage(EvaluationLabel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table evaluation_label
	 * @mbggenerated  Sat Dec 17 16:31:49 CST 2016
	 */
	int countSelective(EvaluationLabel record);
}