package com.depression.model.web.dto;

import com.depression.model.TestingCarouselPictures;

public class WebTestingCarouselPicturesDTO {

	
	private Long cpid;
	
	private Integer type;
	
	private String descp;
	
	private String imgPath;
	
	private Long testingId;
	
	private Byte isEnable;
	
	
	public Long getCpid() {
		return cpid;
	}

	public void setCpid(Long cpid) {
		this.cpid = cpid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Long getTestingId() {
		return testingId;
	}

	public void setTestingId(Long testingId) {
		this.testingId = testingId;
	}

	public Byte getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Byte isEnable) {
		this.isEnable = isEnable;
	}

	// show
	private String filePath;//图片路径（显示）

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
