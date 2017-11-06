package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ArticleTypeMapper;
import com.depression.model.ArticleType;

/**
 * 文章类型
 * 
 * @author hongqian_li
 * @date 2016/04/21
 */
@Service
public class ArticleTypeService
{
	@Autowired
	ArticleTypeMapper articleTypeMapper;

	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int insert(ArticleType entity)
	{
		entity.setCreateTime(new Date());
		entity.setModifyTime(new Date());
		return articleTypeMapper.insertSelective(entity);
	}
	
	/**
	 * 根据主键查询
	 * @param typeId
	 * @return
	 */
	public ArticleType selectById(Long typeId)
	{
		return articleTypeMapper.selectByPrimaryKey(typeId);
	}

	/**
	 * 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleType> selectBy(ArticleType entity)
	{
		return articleTypeMapper.selectSelective(entity);
	}

	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleType> selectByPage(ArticleType entity)
	{
		return articleTypeMapper.selectSelectiveWithPage(entity);
	}

	public long selectCount(ArticleType entity)
	{
		return articleTypeMapper.countSelective(entity);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(ArticleType entity)
	{
		entity.setModifyTime(new Date());
		return articleTypeMapper.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public int deleteBulk(List<Long> ids)
	{
		return articleTypeMapper.deleteByPrimaryKeyBulk(ids);
	}
}
