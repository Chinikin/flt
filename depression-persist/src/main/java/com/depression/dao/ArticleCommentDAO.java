package com.depression.dao;

import java.util.List;

import com.depression.model.ArticleComment;

public interface ArticleCommentDAO
{
	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int insert(ArticleComment entity);

	/**
	 * 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleComment> selectBy(ArticleComment entity);

	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleComment> selectByPage(ArticleComment entity);

	/**
	 * 分页查询count
	 * 
	 * @param entity
	 * @return
	 */
	public long selectCount(ArticleComment entity);

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(ArticleComment entity);
}
