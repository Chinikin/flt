package com.depression.dao;

import com.depression.model.MessageConcorn;

public interface MessageConcornMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table message_concorn
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	int deleteByPrimaryKey(Long messageId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table message_concorn
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	int insert(MessageConcorn record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table message_concorn
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	int insertSelective(MessageConcorn record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table message_concorn
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	MessageConcorn selectByPrimaryKey(Long messageId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table message_concorn
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	int updateByPrimaryKeySelective(MessageConcorn record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table message_concorn
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	int updateByPrimaryKey(MessageConcorn record);
}