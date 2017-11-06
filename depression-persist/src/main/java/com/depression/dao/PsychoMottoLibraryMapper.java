package com.depression.dao;

import com.depression.model.PsychoMottoLibrary;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PsychoMottoLibraryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int insert(PsychoMottoLibrary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int insertSelective(PsychoMottoLibrary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    PsychoMottoLibrary selectByPrimaryKey(Long pmlId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    PsychoMottoLibrary selectByPrimaryKeyLock(Long pmlId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    List<PsychoMottoLibrary> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int deleteByPrimaryKey(Long pmlId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int updateByPrimaryKey(PsychoMottoLibrary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(PsychoMottoLibrary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int updateByPrimaryKeySelective(PsychoMottoLibrary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    List<PsychoMottoLibrary> selectSelective(PsychoMottoLibrary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    List<PsychoMottoLibrary> selectSelectiveWithPage(PsychoMottoLibrary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table psycho_motto_library
     *
     * @mbggenerated Tue Jan 10 19:20:23 CST 2017
     */
    int countSelective(PsychoMottoLibrary record);
}