package com.depression.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ArticleDetailMapper;
import com.depression.model.ArticleDetail;

/**
 * 文章详情
 * 
 * @author hongqian_li
 * @date 201604/23
 */
@Service
public class ArticleDetailService
{
	@Autowired
	ArticleDetailMapper articleDetailMapper;

	/**
	 * 添加一条类型数据
	 * 
	 * @param entity
	 * @return
	 */
	public int insert(ArticleDetail entity)
	{
		return articleDetailMapper.insertSelective(entity);
	}

	/**
	 * 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleDetail> selectBy(ArticleDetail entity)
	{
		return articleDetailMapper.selectSelective(entity);
	}
	
	/**
	 * 根据文章id查询详情
	 * @param articleId
	 * @return
	 */
	public ArticleDetail selectByArticleId(Long articleId)
	{
		ArticleDetail ad = new ArticleDetail();
		ad.setArticleId(articleId);
		
		List<ArticleDetail> ads = articleDetailMapper.selectSelective(ad);
		if(ads.size() > 0)
		{
			return ads.get(0);
		}
		
		return null;
	}

	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<ArticleDetail> selectByPage(ArticleDetail entity)
	{
		return articleDetailMapper.selectSelectiveWithPage(entity);
	}

	public long selectCount(ArticleDetail entity)
	{
		return articleDetailMapper.countSelective(entity);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(ArticleDetail entity)
	{
		return articleDetailMapper.updateByPrimaryKeySelective(entity);
	}
}
