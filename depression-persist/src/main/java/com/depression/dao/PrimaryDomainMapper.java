package com.depression.dao;

import com.depression.model.PrimaryDomain;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PrimaryDomainMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int insert(PrimaryDomain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int insertSelective(PrimaryDomain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    PrimaryDomain selectByPrimaryKey(Long pdid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    PrimaryDomain selectByPrimaryKeyLock(Long pdid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    List<PrimaryDomain> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int deleteByPrimaryKey(Long pdid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int updateByPrimaryKey(PrimaryDomain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(PrimaryDomain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int updateByPrimaryKeySelective(PrimaryDomain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    List<PrimaryDomain> selectSelective(PrimaryDomain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    List<PrimaryDomain> selectSelectiveWithPage(PrimaryDomain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table primary_domain
     *
     * @mbggenerated Mon Sep 12 14:07:21 CST 2016
     */
    int countSelective(PrimaryDomain record);
}