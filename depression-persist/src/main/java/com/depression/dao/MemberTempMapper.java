package com.depression.dao;

import com.depression.model.MemberTemp;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberTempMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int insert(MemberTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int insertSelective(MemberTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    MemberTemp selectByPrimaryKey(Long mid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    MemberTemp selectByPrimaryKeyLock(Long mid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    List<MemberTemp> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int deleteByPrimaryKey(Long mid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int updateByPrimaryKey(MemberTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(MemberTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int updateByPrimaryKeySelective(MemberTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    List<MemberTemp> selectSelective(MemberTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    List<MemberTemp> selectSelectiveWithPage(MemberTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_temp
     *
     * @mbggenerated Fri Dec 30 10:34:18 CST 2016
     */
    int countSelective(MemberTemp record);
}