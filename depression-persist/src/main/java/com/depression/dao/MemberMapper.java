package com.depression.dao;

import com.depression.model.Member;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MemberMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int insert(Member record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int insertSelective(Member record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	Member selectByPrimaryKey(Long mid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	Member selectByPrimaryKeyLock(Long mid);
	
	Member selectByMobileLock(String mobile);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	List<Member> selectByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int deleteByPrimaryKey(Long mid);
	
	int deleteByMid(Long mid);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int deleteByPrimaryKeyBulk(@Param("ids") List<Long> ids);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int enableByPrimaryKeyBulk(@Param("ids") List<Long> ids, @Param("enable") Byte enable);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int updateByPrimaryKey(Member record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int updateByPrimaryKeyWithBLOBs(Member record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int updateByPrimaryKeySelective(Member record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	List<Member> selectSelective(Member record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	List<Member> selectSelectiveWithPage(Member record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table member
	 * @mbggenerated  Tue Apr 11 14:39:03 CST 2017
	 */
	int countSelective(Member record);

	public Integer updatePassword(Member member);
	
	public List<Member> selectByImAccounts(List<String> imAccounts);
	
	public List<Member> selectByTypeOrderAnswerCountWithPage(Member record);
	
	int countByTypeOrderAnswerCount(@Param("userType")Byte userType);
	
	public List<Member> selectByTypeOrderAnswerCountWithPageEnabled(Member record);
	
	public List<Member> selectByTypeSortableWithPageEnabled(@Param("userType")Byte userType, @Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize, @Param("pLevel")Byte pLevel, @Param("sortMode")Byte sortMode);
	
	int countByTypeOrderAnswerCountEnabled(@Param("userType")Byte userType, @Param("pLevel")Byte pLevel);
	
	public List<Member> selectByTypeOrderRegTimeWithPageDesc(Member record);
	
	public List<Member> selectByTypeOrderRegTimeWithPageAsc(Member record);
	
	public List<Member> selectByPrimaryKeysOrderAnswerCountWithPage(
			@Param("ids")List<Long> ids, @Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize);
	
	public List<Member> selectByPrimaryKeysOrderAnswerCountWithPageEnabled(
			@Param("ids")List<Long> ids, @Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize, @Param("pLevel")Byte pLevel);

	public List<Member> selectByPrimaryKeysSortableWithPageEnabled(
			@Param("ids")List<Long> ids, @Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize, @Param("pLevel")Byte pLevel, @Param("sortMode")Byte sortMode);
	
	public List<Member> selectByPrimaryKeysWithPageEnabled(
			@Param("ids")List<Long> ids, @Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize, @Param("pLevel")Byte pLevel);
	
	public int countByPrimaryKeysEnabled(@Param("ids")List<Long> ids, @Param("pLevel")Byte pLevel);
	
	public List<Member> searchMember(@Param("words")String words, @Param("userType")Byte userType,
			@Param("pageStartNum")Integer pageStartNum,
			@Param("pageSize")Integer pageSize , @Param("regTimeDirection")Integer regTimeDirection, 
			@Param("begin")Date begin, @Param("end")Date end, @Param("hasMobile")Byte hasMobile,
			@Param("isAudited")Byte isAudited);
	
	public Integer countSearchMember(@Param("words")String words, @Param("userType")Byte userType, 
			@Param("begin")Date begin, @Param("end")Date end, @Param("hasMobile")Byte hasMobile,
			@Param("isAudited")Byte isAudited);
	
	Member selectByPrimaryKeyWithAnswerCount(Long mid);
	
	public List<Member> selectByTimeSliceWithPage(@Param("userType")Byte userType,
			@Param("pageStartNum")Integer pageStartNum,@Param("pageSize")Integer pageSize,
			@Param("begin")Date begin, @Param("end")Date end);
	
	public Integer countByTimeSlice(@Param("userType")Byte userType,
			@Param("begin")Date begin, @Param("end")Date end);
	
	public List<Member> selectForLucene(@Param("userType")Byte userType,
			@Param("lastIndexTime")Date lastIndexTime, @Param("indexTime")Date indexTime);
	
	public List<Member> searchPsychoByName(@Param("name")String name, @Param("userType")Byte userType);
	
	public Member selectByMidOrMobile(Member record);
	
	/**
	 * 搜索咨询师
	 * @param pageStartNum
	 * @param pageSize
	 * @param pLevel 0咨询师  1倾听师
	 * @param sortMode 1默认 2在线 3最长从业年限 4价格从高到低 5价格从低到高 6最多经验 7最高效率
	 * @param city 城市
	 * @param tagIds 标签id
	 * @param degreeIds 1博士 2硕士 3本科及以下
	 * @param priceFloor 价格下限
	 * @param priceCeil 价格上限
	 * @return
	 */
	public List<Member> searchPsychoV1(@Param("pageStartNum")Integer pageStartNum, @Param("pageSize")Integer pageSize, 
			@Param("pLevel")Byte pLevel, 
			@Param("sortMode")Byte sortMode, 
			@Param("city")String city, @Param("tagIds")List<Long> tagIds,
			@Param("licenseIds")List<Long> licenseIds,@Param("degreeIds")List<Long> degreeIds,
			@Param("priceFloor")Integer priceFloor, @Param("priceCeil")Integer priceCeil,
			@Param("sex")Byte sex,
			@Param("pIds")List<Long> pIds);
	
	public Integer countSearchPsychoV1(
			@Param("pLevel")Byte pLevel, 
			@Param("city")String city, @Param("tagIds")List<Long> tagIds,
			@Param("licenseIds")List<Long> licenseIds,@Param("degreeIds")List<Long> degreeIds,
			@Param("priceFloor")Integer priceFloor, @Param("priceCeil")Integer priceCeil,
			@Param("sex")Byte sex,
			@Param("pIds")List<Long> pIds);

	List<Member> obtainPsyByEeIdAndName(@Param("eeId")Long eeId,@Param("name")String name);
	
}
