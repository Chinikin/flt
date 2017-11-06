package com.depression.dao;

import com.depression.model.CrisisPhone;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CrisisPhoneMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int insert(CrisisPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int insertSelective(CrisisPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    CrisisPhone selectByPrimaryKey(Long cpId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    CrisisPhone selectByPrimaryKeyLock(Long cpId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    List<CrisisPhone> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int deleteByPrimaryKey(Long cpId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int updateByPrimaryKey(CrisisPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(CrisisPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int updateByPrimaryKeySelective(CrisisPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    List<CrisisPhone> selectSelective(CrisisPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    List<CrisisPhone> selectSelectiveWithPage(CrisisPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crisis_phone
     *
     * @mbggenerated Thu Apr 06 08:51:31 CST 2017
     */
    int countSelective(CrisisPhone record);
}