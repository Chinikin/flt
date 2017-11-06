package com.depression.model;

public class MemberAdvisoryCurationImgs extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory_curation_imgs.advisory_img_id
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    /* @Comment(会员动态中的图片id) */
    private Long advisoryImgId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory_curation_imgs.ask_id
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    /* @Comment(会员咨询id) */
    private Long askId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory_curation_imgs.img_path
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    /* @Comment(图片路径) */
    private String imgPath;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory_curation_imgs.img_preview_path
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    /* @Comment(预览图) */
    private String imgPreviewPath;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory_curation_imgs.is_delete
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    /* @Comment(默认0:不删除  1：删除) */
    private Byte isDelete;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_advisory_curation_imgs.is_enable
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    /* @Comment(是否使能：0启用，1禁用) */
    private Byte isEnable;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory_curation_imgs.advisory_img_id
     *
     * @return the value of member_advisory_curation_imgs.advisory_img_id
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public Long getAdvisoryImgId() {
        return advisoryImgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory_curation_imgs.advisory_img_id
     *
     * @param advisoryImgId the value for member_advisory_curation_imgs.advisory_img_id
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public void setAdvisoryImgId(Long advisoryImgId) {
        this.advisoryImgId = advisoryImgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory_curation_imgs.ask_id
     *
     * @return the value of member_advisory_curation_imgs.ask_id
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public Long getAskId() {
        return askId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory_curation_imgs.ask_id
     *
     * @param askId the value for member_advisory_curation_imgs.ask_id
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public void setAskId(Long askId) {
        this.askId = askId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory_curation_imgs.img_path
     *
     * @return the value of member_advisory_curation_imgs.img_path
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory_curation_imgs.img_path
     *
     * @param imgPath the value for member_advisory_curation_imgs.img_path
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath == null ? null : imgPath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory_curation_imgs.img_preview_path
     *
     * @return the value of member_advisory_curation_imgs.img_preview_path
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public String getImgPreviewPath() {
        return imgPreviewPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory_curation_imgs.img_preview_path
     *
     * @param imgPreviewPath the value for member_advisory_curation_imgs.img_preview_path
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public void setImgPreviewPath(String imgPreviewPath) {
        this.imgPreviewPath = imgPreviewPath == null ? null : imgPreviewPath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory_curation_imgs.is_delete
     *
     * @return the value of member_advisory_curation_imgs.is_delete
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory_curation_imgs.is_delete
     *
     * @param isDelete the value for member_advisory_curation_imgs.is_delete
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_advisory_curation_imgs.is_enable
     *
     * @return the value of member_advisory_curation_imgs.is_enable
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_advisory_curation_imgs.is_enable
     *
     * @param isEnable the value for member_advisory_curation_imgs.is_enable
     *
     * @mbggenerated Sat Mar 18 16:33:50 CST 2017
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }
}