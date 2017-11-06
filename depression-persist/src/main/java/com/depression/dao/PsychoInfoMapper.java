package com.depression.dao;

import com.depression.model.Member;
import com.depression.model.PsychoInfo;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PsychoInfoMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int insert(PsychoInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int insertSelective(PsychoInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	PsychoInfo selectByPrimaryKey(Long piid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	PsychoInfo selectByPrimaryKeyLock(Long piid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	List<PsychoInfo> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int deleteByPrimaryKey(Long piid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int updateByPrimaryKey(PsychoInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(PsychoInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int updateByPrimaryKeySelective(PsychoInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	List<PsychoInfo> selectSelective(PsychoInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	List<PsychoInfo> selectSelectiveWithPage(PsychoInfo record);
	
	List<PsychoInfo> selectSelectiveWithPageOrderBy(PsychoInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table psycho_info
	 * @mbggenerated  Thu Apr 13 13:06:42 CST 2017
	 */
	int countSelective(PsychoInfo record);

	public List<PsychoInfo> search(@Param("words")String words, @Param("auditStatus")Byte auditStatus,
			@Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize, @Param("createTimeDirection")Byte createTimeDirection,
			@Param("begin")Date begin, @Param("end")Date end);
	
	public Integer countSearch(@Param("words")String words, @Param("auditStatus")Byte auditStatus, 
			@Param("begin")Date begin, @Param("end")Date end);
}