package com.depression.model;

public class MessageConcorn extends MemberBasic{

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column message_concorn.message_id
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	private Long messageId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column message_concorn.mid
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	private Long mid;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column message_concorn.is_delete
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	private String isDelete;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column message_concorn.message_id
	 * @return  the value of message_concorn.message_id
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	public Long getMessageId()
	{
		return messageId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column message_concorn.message_id
	 * @param messageId  the value for message_concorn.message_id
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	public void setMessageId(Long messageId)
	{
		this.messageId = messageId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column message_concorn.mid
	 * @return  the value of message_concorn.mid
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	public Long getMid()
	{
		return mid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column message_concorn.mid
	 * @param mid  the value for message_concorn.mid
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column message_concorn.is_delete
	 * @return  the value of message_concorn.is_delete
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	public String getIsDelete()
	{
		return isDelete;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column message_concorn.is_delete
	 * @param isDelete  the value for message_concorn.is_delete
	 * @mbggenerated  Thu Jun 23 14:04:37 CST 2016
	 */
	public void setIsDelete(String isDelete)
	{
		this.isDelete = isDelete == null ? null : isDelete.trim();
	}
}