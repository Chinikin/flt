package com.depression.model.eap.dto;

import java.util.Date;

public class EapTestingResultCustom {
	private Long mid;
	private Date testTime;
	private Integer calc;
	private Long testingId;
	
	
	public Long getTestingId() {
		return testingId;
	}
	public void setTestingId(Long testingId) {
		this.testingId = testingId;
	}
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public Date getTestTime() {
		return testTime;
	}
	public void setTestTime(Date testTime) {
		this.testTime = testTime;
	}
	public Integer getCalc() {
		return calc;
	}
	public void setCalc(Integer calc) {
		this.calc = calc;
	}

}
