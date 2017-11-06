package com.depression.model.eap.vo;

import java.util.Date;

import com.depression.model.Page;

public class EapUserVO extends Page{
    private Long eapUserId;

    private Byte userType;

    private Long eeId;

    private String showName;

    private String mobilePhone;

    private String username;

    private String eapPassword;

    private Date createTime;

    private Date modifyTime;

    private Byte isEnable;

    private Byte isDelete;

    private String remark;

    private int spareInt;

    private String spareChar;

    public Long getEapUserId() {
        return eapUserId;
    }

    public void setEapUserId(Long eapUserId) {
        this.eapUserId = eapUserId;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Long getEeId() {
        return eeId;
    }

    public void setEeId(Long eeId) {
        this.eeId = eeId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName == null ? null : showName.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getEapPassword() {
        return eapPassword;
    }

    public void setEapPassword(String eapPassword) {
        this.eapPassword = eapPassword == null ? null : eapPassword.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Byte getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    public Byte getIsDetele() {
        return isDelete;
    }

    public void setIsDetele(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public int getSpareInt() {
        return spareInt;
    }

    public void setSpareInt(int i) {
        this.spareInt = i;
    }

    public String getSpareChar() {
        return spareChar;
    }

    public void setSpareChar(String spareChar) {
        this.spareChar = spareChar == null ? null : spareChar.trim();
    }
}