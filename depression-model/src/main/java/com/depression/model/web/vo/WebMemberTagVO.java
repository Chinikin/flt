package com.depression.model.web.vo;

public class WebMemberTagVO  {

    /* @Comment(主键) */
    private Long tagId;

    /* @Comment(短语) */
    private String phrase;

    /* @Comment() */
    private Integer isEnable;

	public Long getTagId()
	{
		return tagId;
	}

	public void setTagId(Long tagId)
	{
		this.tagId = tagId;
	}

	public String getPhrase()
	{
		return phrase;
	}

	public void setPhrase(String phrase)
	{
		this.phrase = phrase;
	}

	public Integer getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Integer isEnable)
	{
		this.isEnable = isEnable;
	}

 
}