package com.depression.model.api.dto;

import java.util.Date;

/**
 * 文章
 * 
 * @author hongqian_li
 * 
 */
public class ApiLuceneArticleDTO
{
	private Long articleId;// 主键
	private Long categoryId;// 类别外键
	private Long typeId;// 类型外键
	private String title;// 文章标题
	private String digest;// 文章摘要
	private String detail; //详情
	private String hits;// 点击数量
	private String author;// 作者
	private String source;// 来源
	private Integer collectionNum;// 收藏数量
	private String thumbnail;// 缩略图
	private Date createTime;// 创建时间
	private Date modifyTime;
	private Byte isEnable;
	private int isDelete;

	// show
	private String categoryName;
	private String typeName;
	private String filePath;// 图片网络路径
	
    private Integer luceneFlag;

	public Long getArticleId()
	{
		return articleId;
	}

	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}

	public Long getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(Long categoryId)
	{
		this.categoryId = categoryId;
	}

	public Long getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Long typeId)
	{
		this.typeId = typeId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDigest()
	{
		return digest;
	}

	public void setDigest(String digest)
	{
		this.digest = digest;
	}

	public String getHits()
	{
		return hits;
	}

	public void setHits(String hits)
	{
		this.hits = hits;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public Integer getCollectionNum()
	{
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum)
	{
		this.collectionNum = collectionNum;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public int getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(int isDelete)
	{
		this.isDelete = isDelete;
	}

	public Byte getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Byte isEnable)
	{
		this.isEnable = isEnable;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
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

	public Integer getLuceneFlag()
	{
		return luceneFlag;
	}

	public void setLuceneFlag(Integer luceneFlag)
	{
		this.luceneFlag = luceneFlag;
	}
	
	

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	@Override
	public String toString()
	{
		return "Article [articleId=" + articleId + ", categoryId=" + categoryId + ", typeId=" + typeId + ", title=" + title + ", digest=" + digest + ", hits=" + hits + ", thumbnail=" + thumbnail
				+ ", createTime=" + createTime + ", modifyTime=" + modifyTime + ", isDelete=" + isDelete + ", categoryName=" + categoryName + ", typeName=" + typeName + ", filePath=" + filePath + "]";
	}

}
