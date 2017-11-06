package com.depression.dao;

import com.depression.model.MemberUpdateComment;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberUpdateCommentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int insert(MemberUpdateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int insertSelective(MemberUpdateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    MemberUpdateComment selectByPrimaryKey(Long commentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    MemberUpdateComment selectByPrimaryKeyLock(Long commentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    List<MemberUpdateComment> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int deleteByPrimaryKey(Long commentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int updateByPrimaryKey(MemberUpdateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(MemberUpdateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int updateByPrimaryKeySelective(MemberUpdateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    List<MemberUpdateComment> selectSelective(MemberUpdateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    List<MemberUpdateComment> selectSelectiveWithPage(MemberUpdateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_update_comment
     *
     * @mbggenerated Thu Mar 23 10:55:20 CST 2017
     */
    int countSelective(MemberUpdateComment record);
    
    List<MemberUpdateComment> select3Page0TmDesc(MemberUpdateComment record);
    
    //增加时间倒序
    List<MemberUpdateComment> selectSelectiveWithPageOrderBy(MemberUpdateComment record);
}