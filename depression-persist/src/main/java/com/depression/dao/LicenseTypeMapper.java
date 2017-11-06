package com.depression.dao;

import com.depression.model.LicenseType;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LicenseTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int insert(LicenseType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int insertSelective(LicenseType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    LicenseType selectByPrimaryKey(Long ltid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    LicenseType selectByPrimaryKeyLock(Long ltid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    List<LicenseType> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int deleteByPrimaryKey(Long ltid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int updateByPrimaryKey(LicenseType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(LicenseType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int updateByPrimaryKeySelective(LicenseType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    List<LicenseType> selectSelective(LicenseType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    List<LicenseType> selectSelectiveWithPage(LicenseType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table license_type
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int countSelective(LicenseType record);
}