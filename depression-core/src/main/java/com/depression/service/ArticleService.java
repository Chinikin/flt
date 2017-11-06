package com.depression.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ArticleMapper;
import com.depression.model.Article;
import com.depression.model.ArticleCustom;
import com.depression.model.EapEnterprise;
import com.depression.model.Page;
import com.github.pagehelper.PageHelper;

/**
 * 文章
 * 
 * @author hongqian_li
 * @date 201604/23
 */
@Service
public class ArticleService
{
	@Autowired
	ArticleMapper articleMapper;
	@Autowired
	EapEnterpriseService eapEnterpriseService;

	/**
	 * 新建一篇文章
	 * 
	 * @param article
	 * @return
	 */
	public int insert(Article article)
	{
		article.setCreateTime(new Date());
		article.setModifyTime(new Date());
		return articleMapper.insertSelective(article);
	}
	
	/**
	 * 根据主键查询
	 * @param articleId
	 * @return
	 */
	public Article selectById(Long articleId)
	{
		return articleMapper.selectByPrimaryKey(articleId);
	}

	/**
	 * 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<Article> selectBy(Article article)
	{
		return articleMapper.selectSelective(article);
	}

	/**
	 * 分页查询 根据条件查找相关内容
	 * 
	 * @param entity
	 * @return
	 */
	public List<Article> selectByPage(Article article)
	{
		return articleMapper.selectSelectiveWithPage(article);
	}
	
	
	public List<Article> selectByPageOrderBy(Article article)
	{
		return articleMapper.selectSelectiveWithPageOrderBy(article);
	}
	
	public Integer selectCount(Article article)
	{
		return articleMapper.countSelective(article);
	}
	
	/**
	 * 分页查询，按时间倒序
	 * @param article
	 * @return
	 */
	public List<Article> selectNew(Integer pageIndex, Integer pageSize)
	{
		Article article = new Article();
		article.setPageIndex(pageIndex);
		article.setPageSize(pageSize);
		article.setIsEnable((byte) 0);
		return articleMapper.selectSelectiveWithPageNew(article);
	}
	
	public Integer countNew()
	{
		Article article = new Article();
		article.setIsEnable((byte) 0);
		return articleMapper.countSelective(article);
	}
	

	
	/**
	 * 根据类别名称获取最新文章
	 * @param typeName
	 * @param size
	 * @return
	 */
	public List<Article> getLatestByTypeName(String typeName, Integer size, Byte isEnable)
	{
		return articleMapper.getLatestByTypeName(typeName, size, isEnable);
	}
	
	/**
	 * 根据类型和类别名称获取最新文章
	 * @param typeName
	 * @param categoryName
	 * @param size
	 * @return
	 */
	public List<Article> getLatestByTypeNameAndCategoryName(String typeName,String categoryName, Integer size, Byte isEnable)
	{
		return articleMapper.getLatestByTypeNameAndCategoryName(typeName, categoryName, size, isEnable);
	}



	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(Article article)
	{
		article.setModifyTime(new Date());
		return articleMapper.updateByPrimaryKeySelective(article);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public int deleteBulk(List<Long> ids)
	{
		return articleMapper.deleteByPrimaryKeyBulk(ids);
	}
	
	/**
	 * 根据id列表获取文章列表
	 * @param ids
	 * @return
	 */
	public List<Article> getArticleByIds(List<String> ids, Integer pageIndex, Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		return articleMapper.getArticleByIds(ids, page.getPageStartNum(), pageSize);
	}
	
	public Integer countArticleByIds(List<String> ids)
	{
		return articleMapper.countArticleByIds(ids);
	}
	
	/**
	 * 根据id启用或者禁用文章
	 * @param id
	 * @param isEnable
	 * @return
	 */
	public int enable(Long id, Byte isEnable)
	{
		List<Long> ids =  new ArrayList<Long>();
		ids.add(id);
		return articleMapper.enableByPrimaryKeyBulk(ids, isEnable);
	}
	
	/**
	 * 查询id列表中启用的文章列表
	 * @param ids
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<Article> getByKeysWithPageEnabled(List<Long> ids, Integer pageIndex,
			Integer pageSize)
	{
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		if(ids.size()==0)
		{
			return new ArrayList<Article>();
		}
		return articleMapper.selectByPrimaryKeysWithPageEnabled(ids, page.getPageStartNum(), pageSize);
	}
	
	/**
	 * 获取id列表中启用的文章数量
	 * @param ids
	 * @return
	 */
	public Integer getCountByPrimaryKeysEnabled(List<Long> ids)
	{
		if(ids.size()==0)
		{
			return 0;
		}
		return articleMapper.countByPrimaryKeysEnabled(ids);
	}

	/**
	 * 根据标题模糊搜索文件
	 * @param title 文章标题
	 * @return
	 */
	public List<Article> getArticleByTitle(String title) {
		return articleMapper.searchArticleByTitle(title);
	}

	public Integer deleteByArticleId(Long articleId) {
		return articleMapper.deleteByPrimaryKey(articleId);
	}
	

}
