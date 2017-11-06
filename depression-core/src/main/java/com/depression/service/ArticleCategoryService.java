package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ArticleCategoryMapper;
import com.depression.model.ArticleCategory;

/**
 * 科普类别
 * 
 * @author hongqian_li
 * 
 */
@Service
public class ArticleCategoryService
{
	@Autowired
	ArticleCategoryMapper articleCategoryMapper;

	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int insert(ArticleCategory entity)
	{
		entity.setCreateTime(new Date());
		entity.setModifyTime(new Date());
		return articleCategoryMapper.insertSelective(entity);
	}
	
	/**
	 * 根据主键查询
	 * @param categoryId
	 * @return
	 */
	public ArticleCategory selectById(Long categoryId)
	{
		return articleCategoryMapper.selectByPrimaryKey(categoryId);
	}

	/**
	 * 根据条件查找相关内容 主要有：1：根据文章类型获取文章一级分类:2：根据文章类型和parentID查找该父级菜单下载的所有内容等
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleCategory> selectBy(ArticleCategory entity)
	{
		return articleCategoryMapper.selectSelective(entity);
	}

	/**
	 * 分页查询 根据条件查找相关内容 主要有：1：根据文章类型获取文章一级分类:2：根据文章类型和parentID查找该父级菜单下载的所有内容等
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleCategory> selectByPage(ArticleCategory entity)
	{
		return articleCategoryMapper.selectSelectiveWithPage(entity);
	}

	/**
	 * @param entity
	 * @return
	 */
	public long selectCount(ArticleCategory entity)
	{
		return articleCategoryMapper.countSelective(entity);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(ArticleCategory entity)
	{
		entity.setModifyTime(new Date());
		return articleCategoryMapper.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public int deleteBulk(List<Long> ids)
	{
		return articleCategoryMapper.deleteByPrimaryKeyBulk(ids);
	}
	
}
