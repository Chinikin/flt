package com.depression.model;

import java.util.Date;

public class MemberWechat extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.mw_id
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment() */
    private Long mwId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.mid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment() */
    private Long mid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.unionid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment() */
    private String unionid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.public_openid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(公众平台身份标识) */
    private String publicOpenid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.open_openid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment() */
    private String openOpenid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.nickname
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(昵称) */
    private String nickname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.sex
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(性别) */
    private Byte sex;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.city
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(市) */
    private String city;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.country
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(国家) */
    private String country;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.province
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(省) */
    private String province;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.headimgurl
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(头像地址) */
    private String headimgurl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.create_time
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment() */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.modify_time
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment() */
    private Date modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.is_enable
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment() */
    private Byte isEnable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_wechat.is_delete
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    /* @Comment(是否删除) */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.mw_id
     *
     * @return the value of member_wechat.mw_id
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public Long getMwId() {
        return mwId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.mw_id
     *
     * @param mwId the value for member_wechat.mw_id
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setMwId(Long mwId) {
        this.mwId = mwId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.mid
     *
     * @return the value of member_wechat.mid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public Long getMid() {
        return mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.mid
     *
     * @param mid the value for member_wechat.mid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.unionid
     *
     * @return the value of member_wechat.unionid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.unionid
     *
     * @param unionid the value for member_wechat.unionid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.public_openid
     *
     * @return the value of member_wechat.public_openid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getPublicOpenid() {
        return publicOpenid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.public_openid
     *
     * @param publicOpenid the value for member_wechat.public_openid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setPublicOpenid(String publicOpenid) {
        this.publicOpenid = publicOpenid == null ? null : publicOpenid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.open_openid
     *
     * @return the value of member_wechat.open_openid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getOpenOpenid() {
        return openOpenid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.open_openid
     *
     * @param openOpenid the value for member_wechat.open_openid
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setOpenOpenid(String openOpenid) {
        this.openOpenid = openOpenid == null ? null : openOpenid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.nickname
     *
     * @return the value of member_wechat.nickname
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.nickname
     *
     * @param nickname the value for member_wechat.nickname
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.sex
     *
     * @return the value of member_wechat.sex
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.sex
     *
     * @param sex the value for member_wechat.sex
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.city
     *
     * @return the value of member_wechat.city
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.city
     *
     * @param city the value for member_wechat.city
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.country
     *
     * @return the value of member_wechat.country
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getCountry() {
        return country;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.country
     *
     * @param country the value for member_wechat.country
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.province
     *
     * @return the value of member_wechat.province
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.province
     *
     * @param province the value for member_wechat.province
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.headimgurl
     *
     * @return the value of member_wechat.headimgurl
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public String getHeadimgurl() {
        return headimgurl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.headimgurl
     *
     * @param headimgurl the value for member_wechat.headimgurl
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.create_time
     *
     * @return the value of member_wechat.create_time
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.create_time
     *
     * @param createTime the value for member_wechat.create_time
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.modify_time
     *
     * @return the value of member_wechat.modify_time
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.modify_time
     *
     * @param modifyTime the value for member_wechat.modify_time
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.is_enable
     *
     * @return the value of member_wechat.is_enable
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.is_enable
     *
     * @param isEnable the value for member_wechat.is_enable
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_wechat.is_delete
     *
     * @return the value of member_wechat.is_delete
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_wechat.is_delete
     *
     * @param isDelete the value for member_wechat.is_delete
     *
     * @mbggenerated Sat Dec 31 15:56:32 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}