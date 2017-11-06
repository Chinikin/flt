package com.depression.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.depression.model.CapitalCouponEntity;
import com.depression.model.CapitalDiscountBestowal;

public interface CapitalDiscountBestowalMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int insert(CapitalDiscountBestowal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int insertSelective(CapitalDiscountBestowal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    CapitalDiscountBestowal selectByPrimaryKey(Long dbid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    CapitalDiscountBestowal selectByPrimaryKeyLock(Long dbid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    List<CapitalDiscountBestowal> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int deleteByPrimaryKey(Long dbid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int updateByPrimaryKey(CapitalDiscountBestowal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(CapitalDiscountBestowal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int updateByPrimaryKeySelective(CapitalDiscountBestowal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    List<CapitalDiscountBestowal> selectSelective(CapitalDiscountBestowal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    List<CapitalDiscountBestowal> selectSelectiveWithPage(CapitalDiscountBestowal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table capital_discount_bestowal
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    int countSelective(CapitalDiscountBestowal record);
    
    int insertBulk(List<CapitalDiscountBestowal> records);
    
    List<CapitalCouponEntity> selectCouponEntity(@Param("mid")Long mid, @Param("status")Byte status,
    		@Param("pageStartNum")Integer pageStartNum, @Param("pageSize")Integer pageSize);
    
    CapitalCouponEntity selectValuabestUsableCouponEntity(@Param("mid")Long mid, @Param("type")byte type,
    		@Param("current")Date current);
}