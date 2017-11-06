package com.depression.model.web.dto;

import java.util.Date;
import java.util.List;

import com.depression.model.ArticleCategory;

public class WebArticleCategoryDTO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.category_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment(����) */
    private Long categoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.parent_category_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment() */
    private Long parentCategoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.type_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment(��������) */
    private Long typeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.category_name
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment(�������) */
    private String categoryName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.thumbnail
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment(����ͼ) */
    private String thumbnail;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.create_time
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment(����ʱ��) */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.modify_time
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment(�޸�ʱ��) */
    private Date modifyTime;
    
	private String typeName;
	private String filePath;// 图片网络路径

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article_category.is_enable
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    /* @Comment() */
    private Byte isEnable;
    
    private List<ArticleCategory> subArticleCategory;
    

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.category_id
     *
     * @return the value of article_category.category_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.category_id
     *
     * @param categoryId the value for article_category.category_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.parent_category_id
     *
     * @return the value of article_category.parent_category_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.parent_category_id
     *
     * @param parentCategoryId the value for article_category.parent_category_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.type_id
     *
     * @return the value of article_category.type_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.type_id
     *
     * @param typeId the value for article_category.type_id
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.category_name
     *
     * @return the value of article_category.category_name
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.category_name
     *
     * @param categoryName the value for article_category.category_name
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.thumbnail
     *
     * @return the value of article_category.thumbnail
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.thumbnail
     *
     * @param thumbnail the value for article_category.thumbnail
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.create_time
     *
     * @return the value of article_category.create_time
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.create_time
     *
     * @param createTime the value for article_category.create_time
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.modify_time
     *
     * @return the value of article_category.modify_time
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.modify_time
     *
     * @param modifyTime the value for article_category.modify_time
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article_category.is_enable
     *
     * @return the value of article_category.is_enable
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public Byte getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article_category.is_enable
     *
     * @param isEnable the value for article_category.is_enable
     *
     * @mbggenerated Wed Mar 15 14:09:37 CST 2017
     */
    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
    }

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public List<ArticleCategory> getSubArticleCategory() {
		return subArticleCategory;
	}

	public void setSubArticleCategory(List<ArticleCategory> subArticleCategory) {
		this.subArticleCategory = subArticleCategory;
	}
    
    
}