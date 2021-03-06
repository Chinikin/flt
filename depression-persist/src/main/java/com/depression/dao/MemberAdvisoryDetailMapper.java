package com.depression.dao;

import com.depression.model.MemberAdvisoryDetail;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberAdvisoryDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int insert(MemberAdvisoryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int insertSelective(MemberAdvisoryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    MemberAdvisoryDetail selectByPrimaryKey(Long advisoryDetailId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    MemberAdvisoryDetail selectByPrimaryKeyLock(Long advisoryDetailId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    List<MemberAdvisoryDetail> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int deleteByPrimaryKey(Long advisoryDetailId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int updateByPrimaryKey(MemberAdvisoryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(MemberAdvisoryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int updateByPrimaryKeySelective(MemberAdvisoryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    List<MemberAdvisoryDetail> selectSelective(MemberAdvisoryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    List<MemberAdvisoryDetail> selectSelectiveWithPage(MemberAdvisoryDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_advisory_detail
     *
     * @mbggenerated Sat Mar 18 16:31:01 CST 2017
     */
    int countSelective(MemberAdvisoryDetail record);
}