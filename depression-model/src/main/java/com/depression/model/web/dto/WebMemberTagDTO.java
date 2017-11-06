package com.depression.model.web.dto;

public class WebMemberTagDTO{

    /* @Comment(主键) */
    private Long tagId;

    /* @Comment() */
    private Integer hitCount;

    /* @Comment() */
    private Integer refCount;

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

	public Integer getHitCount()
	{
		return hitCount;
	}

	public void setHitCount(Integer hitCount)
	{
		this.hitCount = hitCount;
	}

	public Integer getRefCount()
	{
		return refCount;
	}

	public void setRefCount(Integer refCount)
	{
		this.refCount = refCount;
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