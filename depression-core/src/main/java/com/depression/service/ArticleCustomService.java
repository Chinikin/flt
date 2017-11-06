package com.depression.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.ArticleMapper;
import com.depression.dao.EapEnterpriseMapper;
import com.depression.model.Article;
import com.depression.model.ArticleCustom;
import com.depression.model.EapEnterprise;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 文章
 * 
 * @author hongqian_li
 * @date 201604/23
 */
@Service
public class ArticleCustomService
{
	@Autowired
	ArticleMapper articleMapper;
	@Autowired
	EapEnterpriseMapper eapEnterpriseMapper;

	
	/**
	 * 根据标题模糊搜索文件
	 * @param title 文章标题
	 * @return
	 */
	public List<Article> getArticleByTitle(String title) {
		return articleMapper.searchArticleByTitle(title);
	}

	/**
	 * 查询文章  先根据企业信息的 xmArticle字段  如果为0 则全部查询   包含心猫文章
	 * 如果为1 则只查询自己EAP的文章
	 * 
	 * @param eeId
	 * @param begin
	 * @param end
	 * @param words
	 * @param sortTag
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	
	
	
	public Page<Article> getValidArticlesByPage(
			Long releaseFrom,Integer pageIndex,Integer pageSize) {
		
		EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKey(releaseFrom);
		
		
		if(ee != null && ee.getXmArticle() == 0){
			PageHelper.startPage(pageIndex, pageSize);
			return articleMapper.searchEapAndXmArticle(releaseFrom);
		}else{
			PageHelper.startPage(pageIndex, pageSize);
			return articleMapper.searchEapArticle(releaseFrom);
		}
		
	}
/*	public Page<Article> getValidArticlesByPage1(
			Long releaseFrom,Integer pageIndex,Integer pageSize) {
		
		EapEnterprise ee = eapEnterpriseMapper.selectByPrimaryKey(releaseFrom);
		PageHelper.startPage(pageIndex, pageSize);
		if(ee != null && ee.getXmArticle() == 0){
			return articleMapper.searchEapAndXmArticle(releaseFrom);
		}
		return articleMapper.searchEapArticle(releaseFrom);
	}
	*/


	public List<Article> searchArticleByTitle(Long eeId, String title) {
		return articleMapper.searchArticleByTitleInEap(eeId,title);
	}
	
	
	public Page<ArticleCustom> getArticlesByPageInEap(Long eeId, Date begin, Date end, String words, Integer sortTag,
			Integer pageIndex, Integer pageSize,Byte isEnable){
		PageHelper.startPage(pageIndex, pageSize);
		
		return articleMapper.getArticlesByPageInEap(eeId, begin, end, words, sortTag,isEnable);
		
	}

}
