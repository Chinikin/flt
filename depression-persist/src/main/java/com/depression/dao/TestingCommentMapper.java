package com.depression.dao;

import com.depression.model.TestingComment;
import com.depression.model.TestingOld;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TestingCommentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int insert(TestingComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int insertSelective(TestingComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    TestingComment selectByPrimaryKey(Long commentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    TestingComment selectByPrimaryKeyLock(Long commentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    List<TestingComment> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int deleteByPrimaryKey(Long commentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int updateByPrimaryKey(TestingComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(TestingComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int updateByPrimaryKeySelective(TestingComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    List<TestingComment> selectSelective(TestingComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    List<TestingComment> selectSelectiveWithPage(TestingComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table testing_comment
     *
     * @mbggenerated Wed Mar 22 15:59:26 CST 2017
     */
    int countSelective(TestingComment record);
    
    
 // 分页总条数
 	public List<TestingComment> selectByPageCommentTimeDesc(TestingComment testingComment);
    
    
}