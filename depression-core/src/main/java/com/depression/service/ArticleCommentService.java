package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ArticleCommentDAO;
import com.depression.model.ArticleComment;

/**
 * 文章评论
 * 
 * @author hongqian_li
 * @date 2016/04/29
 */
@Service
public class ArticleCommentService
{
	@Autowired
	ArticleCommentDAO dao;

	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int insert(ArticleComment entity)
	{
		entity.setCommentTime(new Date());
		return dao.insert(entity);
	}

	/**
	 * 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleComment> selectBy(ArticleComment entity)
	{
		return dao.selectBy(entity);
	}

	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleComment> selectByPage(ArticleComment entity)
	{
		return dao.selectByPage(entity);
	}

	public long selectCount(ArticleComment entity)
	{
		return dao.selectCount(entity);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(ArticleComment entity)
	{
		return dao.update(entity);
	}
}
