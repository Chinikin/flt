package com.depression.dao;

import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisory4Psycho;
import com.depression.model.MemberAdvisory4PsychoV1;
import com.depression.model.web.dto.WebMemberAdvisoryDTO;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MemberAdvisoryMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int insert(MemberAdvisory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int insertSelective(MemberAdvisory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	MemberAdvisory selectByPrimaryKey(Long advisoryId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	MemberAdvisory selectByPrimaryKeyLock(Long advisoryId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	List<MemberAdvisory> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int deleteByPrimaryKey(Long advisoryId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int updateByPrimaryKey(MemberAdvisory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(MemberAdvisory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int updateByPrimaryKeySelective(MemberAdvisory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	List<MemberAdvisory> selectSelective(MemberAdvisory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	List<MemberAdvisory> selectSelectiveWithPage(MemberAdvisory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member_advisory
	 * @mbggenerated  Mon May 08 16:02:34 CST 2017
	 */
	int countSelective(MemberAdvisory record);

	List<MemberAdvisory> selectSelective3Page0TmDesc(MemberAdvisory record);
    
	public List<MemberAdvisory> select8Tag3Page0TmDesc(@Param("pageStartNum")Integer pageStartNum, @Param("pageSize")Integer pageSize, @Param("tagId")Long tagId);
	
	public Integer count8Tag(@Param("tagId")Long tagId);
	
	/**
	 * 获取咨询师详情页的回答过的咨询列表
	 * @param mid
	 * @param pageStartNum
	 * @param pageSize
	 * @return
	 */
	public List<MemberAdvisory4Psycho> selectAdvisory4Psychos(@Param("mid")Long mid, @Param("pageStartNum")Integer pageStartNum, 
			@Param("pageSize")Integer pageSize);

	public Integer countAdvisory4Psychos(@Param("mid")Long mid);
	
	public List<MemberAdvisory4PsychoV1> selectAdvisory4PsychosV1(@Param("pid")Long pid, @Param("pageStartNum")Integer pageStartNum, 
			@Param("pageSize")Integer pageSize);

	public Integer countAdvisory4PsychosV1(@Param("pid")Long pid);
	
	public List<MemberAdvisory> selectForLucene(
			@Param("lastIndexTime")Date lastIndexTime, @Param("indexTime")Date indexTime);
	
	public List<MemberAdvisory> select8Keys3PageEn(
			@Param("ids")List<Long> ids, @Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize);
	
	public int count8KeysEn(@Param("ids")List<Long> ids);
	
	/**
	 * 咨询分页查询列表
	 * 
	 * @param tagId
	 * @param pageStartNum
	 * @param pageSize
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<MemberAdvisory> selectAdvisoryByTimeSliceWithPage(@Param("tagId")Long tagId,
			@Param("pageStartNum")Integer pageStartNum,@Param("pageSize")Integer pageSize,
			@Param("begin")Date begin, @Param("end")Date end);
	
	public List<MemberAdvisory> selectAdvisoryByTimeSliceWithPageOrderBy(@Param("tagId")Long tagId,
			@Param("pageStartNum")Integer pageStartNum,@Param("pageSize")Integer pageSize,
			@Param("begin")Date begin, @Param("end")Date end);
	
	/**
	 * 咨询分页查询条数
	 * 
	 * @param tagId
	 * @param begin
	 * @param end
	 * @return
	 */
	public Integer countAdvisoryByTimeSlice(@Param("tagId")Long tagId,
			@Param("begin")Date begin, @Param("end")Date end);
	
	/**
	 * 搜索咨询
	 * @param begin
	 * @param end
	 * @param words
	 * @return
	 */
	public List<MemberAdvisory> searchAdvisory(
			@Param("pageStartNum")Integer pageStartNum,@Param("pageSize")Integer pageSize,
			@Param("begin")Date begin, @Param("end")Date end,
			@Param("words")String words);
	public Integer countSearchAdvisory(@Param("begin")Date begin, @Param("end")Date end,
			@Param("words")String words);
	
	/**
	 * 搜索咨询，返回列表
	 * @param pageStartNum
	 * @param pageSize
	 * @param isRecommended 是否被推荐
	 * @param tagId 标签id
	 * @param sortMode 1 默认 2最多阅读 3最多回复 4最多分享
	 * @return
	 */
	public List<MemberAdvisory> searchAdvisoryV1(
			@Param("pageStartNum")Integer pageStartNum,@Param("pageSize")Integer pageSize,
			@Param("isRecommended")Byte isRecommended, @Param("tagId")Long tagId,
			@Param("sortMode")Byte sortMode,@Param("releaseFrom")Long releaseFrom
			);
	public Integer countSearchAdvisoryV1(
			@Param("isRecommended")Byte isRecommended, @Param("tagId")Long tagId,@Param("releaseFrom")Long releaseFrom
			);

	List<MemberAdvisory> searchAdvisoryInEap(@Param("pageStartNum")Integer pageStartNum,@Param("pageSize")Integer pageSize,
			@Param("begin")Date begin, @Param("end")Date end,
			@Param("words")String words,@Param("eeId")Long eeId,
			@Param("mid")Long mid);
	
	public Integer countSearchAdvisoryInEap(
			@Param("words")String words,@Param("eeId")Long eeId,@Param("mid")Long mid,@Param("begin")Date begin, @Param("end")Date end
			);
	
}