package com.depression.model.eap.vo;

import java.util.Date;

public class EapEmployeeVO {
    
    private String words;

    private String name;
    
    private Long grade;

    private String number;
   
    private String college;

    private String phoneNum;
    
    private Date startTime;
    
    private Date endTime;
    
    private Long eeId;
    
    private Byte isEnable;
    
    private String tag;
    
    private Long eemId;
    
    private String remark1;
    
    private String remark2;
    

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public Long getGrade() {
		return grade;
	}

	public void setGrade(Long grade) {
		this.grade = grade;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getEeId() {
		return eeId;
	}

	public void setEeId(Long eeId) {
		this.eeId = eeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Byte getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Byte isEnable) {
		this.isEnable = isEnable;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getEemId() {
		return eemId;
	}

	public void setEemId(Long eemId) {
		this.eemId = eemId;
	}

    
}