package com.depression.model;

import java.util.Date;

public class EapEmployee extends Page {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.eem_id
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Long eemId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.ee_id
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Long eeId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.name
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private String name;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.phone_num
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private String phoneNum;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.number
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private String number;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.remark1
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private String remark1;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.remark2
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private String remark2;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.grade
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Long grade;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.college
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private String college;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.statistical_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Date statisticalTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.eap_advisory_duration
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Long eapAdvisoryDuration;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.eap_advisory_times
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Integer eapAdvisoryTimes;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.total_advisory_duration
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Long totalAdvisoryDuration;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.total_advisory_times
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Integer totalAdvisoryTimes;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.tag
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private String tag;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.create_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Date createTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.update_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Date updateTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.delete_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Date deleteTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.is_enable
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Byte isEnable;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column eap_employee.is_delete
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	private Byte isDelete;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.eem_id
	 * @return  the value of eap_employee.eem_id
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Long getEemId() {
		return eemId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.eem_id
	 * @param eemId  the value for eap_employee.eem_id
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setEemId(Long eemId) {
		this.eemId = eemId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.ee_id
	 * @return  the value of eap_employee.ee_id
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Long getEeId() {
		return eeId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.ee_id
	 * @param eeId  the value for eap_employee.ee_id
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setEeId(Long eeId) {
		this.eeId = eeId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.name
	 * @return  the value of eap_employee.name
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.name
	 * @param name  the value for eap_employee.name
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.phone_num
	 * @return  the value of eap_employee.phone_num
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public String getPhoneNum() {
		return phoneNum;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.phone_num
	 * @param phoneNum  the value for eap_employee.phone_num
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum == null ? null : phoneNum.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.number
	 * @return  the value of eap_employee.number
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.number
	 * @param number  the value for eap_employee.number
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setNumber(String number) {
		this.number = number == null ? null : number.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.remark1
	 * @return  the value of eap_employee.remark1
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public String getRemark1() {
		return remark1;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.remark1
	 * @param remark1  the value for eap_employee.remark1
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1 == null ? null : remark1.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.remark2
	 * @return  the value of eap_employee.remark2
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public String getRemark2() {
		return remark2;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.remark2
	 * @param remark2  the value for eap_employee.remark2
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2 == null ? null : remark2.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.grade
	 * @return  the value of eap_employee.grade
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Long getGrade() {
		return grade;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.grade
	 * @param grade  the value for eap_employee.grade
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setGrade(Long grade) {
		this.grade = grade;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.college
	 * @return  the value of eap_employee.college
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public String getCollege() {
		return college;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.college
	 * @param college  the value for eap_employee.college
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setCollege(String college) {
		this.college = college == null ? null : college.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.statistical_time
	 * @return  the value of eap_employee.statistical_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Date getStatisticalTime() {
		return statisticalTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.statistical_time
	 * @param statisticalTime  the value for eap_employee.statistical_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setStatisticalTime(Date statisticalTime) {
		this.statisticalTime = statisticalTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.eap_advisory_duration
	 * @return  the value of eap_employee.eap_advisory_duration
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Long getEapAdvisoryDuration() {
		return eapAdvisoryDuration;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.eap_advisory_duration
	 * @param eapAdvisoryDuration  the value for eap_employee.eap_advisory_duration
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setEapAdvisoryDuration(Long eapAdvisoryDuration) {
		this.eapAdvisoryDuration = eapAdvisoryDuration;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.eap_advisory_times
	 * @return  the value of eap_employee.eap_advisory_times
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Integer getEapAdvisoryTimes() {
		return eapAdvisoryTimes;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.eap_advisory_times
	 * @param eapAdvisoryTimes  the value for eap_employee.eap_advisory_times
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setEapAdvisoryTimes(Integer eapAdvisoryTimes) {
		this.eapAdvisoryTimes = eapAdvisoryTimes;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.total_advisory_duration
	 * @return  the value of eap_employee.total_advisory_duration
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Long getTotalAdvisoryDuration() {
		return totalAdvisoryDuration;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.total_advisory_duration
	 * @param totalAdvisoryDuration  the value for eap_employee.total_advisory_duration
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setTotalAdvisoryDuration(Long totalAdvisoryDuration) {
		this.totalAdvisoryDuration = totalAdvisoryDuration;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.total_advisory_times
	 * @return  the value of eap_employee.total_advisory_times
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Integer getTotalAdvisoryTimes() {
		return totalAdvisoryTimes;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.total_advisory_times
	 * @param totalAdvisoryTimes  the value for eap_employee.total_advisory_times
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setTotalAdvisoryTimes(Integer totalAdvisoryTimes) {
		this.totalAdvisoryTimes = totalAdvisoryTimes;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.tag
	 * @return  the value of eap_employee.tag
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.tag
	 * @param tag  the value for eap_employee.tag
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setTag(String tag) {
		this.tag = tag == null ? null : tag.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.create_time
	 * @return  the value of eap_employee.create_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.create_time
	 * @param createTime  the value for eap_employee.create_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.update_time
	 * @return  the value of eap_employee.update_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.update_time
	 * @param updateTime  the value for eap_employee.update_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.delete_time
	 * @return  the value of eap_employee.delete_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Date getDeleteTime() {
		return deleteTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.delete_time
	 * @param deleteTime  the value for eap_employee.delete_time
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.is_enable
	 * @return  the value of eap_employee.is_enable
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Byte getIsEnable() {
		return isEnable;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.is_enable
	 * @param isEnable  the value for eap_employee.is_enable
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setIsEnable(Byte isEnable) {
		this.isEnable = isEnable;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column eap_employee.is_delete
	 * @return  the value of eap_employee.is_delete
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public Byte getIsDelete() {
		return isDelete;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column eap_employee.is_delete
	 * @param isDelete  the value for eap_employee.is_delete
	 * @mbggenerated  Wed Apr 05 17:28:28 CST 2017
	 */
	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}
}