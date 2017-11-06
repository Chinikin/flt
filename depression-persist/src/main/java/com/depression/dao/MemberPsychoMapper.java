package com.depression.dao;

import com.depression.model.MemberPsycho;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberPsychoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int insert(MemberPsycho record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int insertSelective(MemberPsycho record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    MemberPsycho selectByPrimaryKey(Long mpId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    MemberPsycho selectByPrimaryKeyLock(Long mpId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    List<MemberPsycho> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int deleteByPrimaryKey(Long mpId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int updateByPrimaryKey(MemberPsycho record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(MemberPsycho record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int updateByPrimaryKeySelective(MemberPsycho record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    List<MemberPsycho> selectSelective(MemberPsycho record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    List<MemberPsycho> selectSelectiveWithPage(MemberPsycho record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_psycho
     *
     * @mbggenerated Fri Dec 30 14:27:18 CST 2016
     */
    int countSelective(MemberPsycho record);
    
    List<Long> selectPsychoIds();
    
    List<Long> selectPsychoIds8Audited(@Param("isAudited")Byte isAudited);
}