package com.depression.dao;

import com.depression.model.MemberUpdateEmbrace;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberUpdateEmbraceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int insert(MemberUpdateEmbrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int insertSelective(MemberUpdateEmbrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    MemberUpdateEmbrace selectByPrimaryKey(Long emberaceId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    MemberUpdateEmbrace selectByPrimaryKeyLock(Long emberaceId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    List<MemberUpdateEmbrace> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int deleteByPrimaryKey(Long emberaceId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int updateByPrimaryKey(MemberUpdateEmbrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(MemberUpdateEmbrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int updateByPrimaryKeySelective(MemberUpdateEmbrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    List<MemberUpdateEmbrace> selectSelective(MemberUpdateEmbrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    List<MemberUpdateEmbrace> selectSelectiveWithPage(MemberUpdateEmbrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_embrace
     *
     * @mbggenerated Thu Mar 23 10:55:38 CST 2017
     */
    int countSelective(MemberUpdateEmbrace record);
}