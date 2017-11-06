package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ArticleCollectionMapper;
import com.depression.model.ArticleCollection;

/**
 * 文章收藏
 * 
 * @author fanxinhui
 * @date 2016/5/27
 */
@Service
public class ArticleCollectionService
{
	@Autowired
	ArticleCollectionMapper collectionMapper;

	/**
	 * 新建收藏
	 * @param articleId
	 * @return
	 */
	public int createCollection(Long articleId, Long mid)
	{
		ArticleCollection ac = new ArticleCollection();
		ac.setArticleId(articleId);
		ac.setMid(mid);
		ac.setCreateTime(new Date());
		return collectionMapper.insertSelective(ac);
	}
	
	/**
	 * 是否已经关注
	 * @param colId
	 * @param mid
	 * @return
	 */
	public Byte isCollected(Long articleId, Long mid)
	{
		ArticleCollection ac = new ArticleCollection();
		ac.setArticleId(articleId);
		ac.setMid(mid);
		List<ArticleCollection> acs = collectionMapper.selectSelective(ac);
		
		return (byte) (acs.size()==0?0:1);
	}
	
	/**
	 * 取消收藏
	 * @param articleId
	 * @param mid
	 * @return
	 */
	public int deleteCollection(Long articleId, Long mid)
	{
		ArticleCollection ac = new ArticleCollection();
		ac.setArticleId(articleId);
		ac.setMid(mid);
		List<ArticleCollection> acs = collectionMapper.selectSelective(ac);
		
		if(acs.size() > 0)
		{
			return collectionMapper.deleteByPrimaryKey(acs.get(0).getColId());
		}
		return 0;
	}
	
	public int deleteCollection(Long colId)
	{
		return collectionMapper.deleteByPrimaryKey(colId);
	}
	
	/**
	 * 获取用户的收藏列表
	 * @param mid
	 * @return
	 */
	public List<ArticleCollection> obtainArticleCollectionsByMid(Long mid)
	{
		ArticleCollection ac = new ArticleCollection();
		ac.setMid(mid);
		return collectionMapper.selectSelective(ac);
	}

}
