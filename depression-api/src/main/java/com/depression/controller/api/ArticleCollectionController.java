package com.depression.controller.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Article;
import com.depression.model.ArticleCategory;
import com.depression.model.ArticleCollection;
import com.depression.model.ArticleType;
import com.depression.model.api.dto.ApiArticleDTO;
import com.depression.service.ArticleCategoryService;
import com.depression.service.ArticleCollectionService;
import com.depression.service.ArticleService;
import com.depression.service.ArticleTypeService;
import com.depression.service.MemberService;
import com.depression.utils.Configuration;
import com.depression.utils.PropertyUtils;

/**
 * 文章收藏
 * 
 * @author fanxinhui
 * @date 2016/5/27
 */
@Controller
@RequestMapping("/ArticleCollection")
public class ArticleCollectionController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	ArticleCollectionService articleCollectionService;

	@Autowired
	MemberService memberService;
	@Autowired
	ArticleService articleService;
	@Autowired	
	ArticleTypeService articleTypeService;
	@Autowired		
	ArticleCategoryService articleCategoryService;

	/**
	 * 检查是否已经收藏
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/checkArticleCollection.json")
	@ResponseBody
	public Object checkArticleCollection(HttpSession session, HttpServletRequest request, String articleId, @RequestParam(value = "mid", required = false) Long mid)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			// 参数检查
			if (StringUtils.isEmpty(articleId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章id不能为空");
				return result;
			}
			if ( mid == null )
			{
				result.setCode(-1);
				result.setMsg("会员id不能为空");
				return result;
			}

			// 检查文章是否存在
			Long artId = Long.valueOf(articleId);
			Article art = articleService.selectById(artId);
			if (art == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章不存在");
				return result;
			}
			
			Byte isCollected =  articleCollectionService.isCollected(Long.valueOf(articleId), mid);
			if (isCollected == 1)
			{
				result.put("isCollection", "1");
				result.setMsg("已收藏");
				return result;
			}
			else
			{
				result.put("isCollection", "0");
				result.setMsg("未收藏");
				return result;
			}

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}
	
	/**
	 * 文章收藏
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/postArticleCollection.json")
	@ResponseBody
	public Object postArticleCollection(HttpSession session, HttpServletRequest request, String articleId, @RequestParam(value = "mid", required = false) Long mid, Integer cancel)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			// 参数检查
			if (StringUtils.isEmpty(articleId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章id不能为空");
				return result;
			}
			if ( mid == null )
			{
				result.setCode(-1);
				result.setMsg("会员id不能为空");
				return result;
			}

			// 检查文章是否存在
			Long artId = Long.valueOf(articleId);
			Article article = articleService.selectById(artId);
			if (article == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章不存在");
				return result;
			}
			
			Byte isCollected = articleCollectionService.isCollected(Long.valueOf(articleId), mid);
			if(cancel != null && cancel == 1)
			{//取消收藏
				
				if (isCollected == 1)
				{
					articleCollectionService.deleteCollection(Long.valueOf(articleId), mid);
					// 更新文章收藏数量
					if(article.getCollectionNum() > 0)
					{
						article.setCollectionNum(article.getCollectionNum() - 1);
					}
					articleService.update(article);
				}
				
				result.setMsg("取消收藏成功");
				return result;
			}else
			{
				if (isCollected == 1)
				{
					result.setMsg("收藏成功");
					return result;
				}
	
				// 更新文章收藏数量
				if (article.getCollectionNum() == null)
				{
					article.setCollectionNum(1L);
				} else
				{
					article.setCollectionNum(article.getCollectionNum() + 1);
				}
				articleService.update(article);
	
				// TODO 收藏更新，后期考虑优化
				articleCollectionService.createCollection(Long.valueOf(articleId), mid);
	
				result.setMsg("收藏成功");
			}
			return result;
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}
	
	/**
	 * 获取用户收藏列表以及文章列表
	 * @param session
	 * @param request
	 * @param mid 	会员id
	 * @return
	 */
	@RequestMapping(value = "/getArticleListAndCollection.json")
	@ResponseBody
	public Object getArticleListAndCollection(Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				mid,
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		result.setCode(ResultEntity.SUCCESS);
		try{
			List<ArticleCollection> acList = articleCollectionService.obtainArticleCollectionsByMid(mid);
					
			List<ApiArticleDTO> articleDTOs = new ArrayList<ApiArticleDTO>();
			if(acList.size()==0)
			{
				result.put("articleList", articleDTOs);
				result.put("count", 0);
			}
			else
			{
				List<String> idsList = new ArrayList<String>();
				for(ArticleCollection ac : acList)
				{
					idsList.add(String.valueOf(ac.getArticleId()));
				}
				List<Article> articleList = articleService.getArticleByIds(idsList, pageIndex, pageSize);
				Integer count = articleService.countArticleByIds(idsList);
				for(Article article: articleList)
				{
					ApiArticleDTO articleDTO = new ApiArticleDTO();
					BeanUtils.copyProperties(article, articleDTO);
					articleDTO.setCreateTimestamp(article.getCreateTime().getTime());
					//设置图片路径
					articleDTO.setFilePath(article.getThumbnail());
					//查询类型名
					ArticleType articleType = articleTypeService.selectById(article.getTypeId());
					
					if(articleType != null){
						articleDTO.setTypeName(articleType.getTypeName());
					}
					
					//查询类别名
					ArticleCategory articleCategory = articleCategoryService.selectById(article.getCategoryId());
					
					if(articleCategory != null){
						articleDTO.setCategoryName(articleCategory.getCategoryName());
					}
					articleDTOs.add(articleDTO);
				}
				result.put("articleList", articleDTOs);
				result.put("count", count);
				result.put("acList", acList);
			}
		}catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}
	
	/**
	 * 修改文章收藏的状态
	 * @param session
	 * @param request
	 * @param colId
	 * @return
	 */
	@RequestMapping(value = "/changeArticleCollectionStatus.json")
	@ResponseBody
	public Object changeArticleCollectionStatus(HttpSession session, HttpServletRequest request, Long colId)
	{
		ResultEntity result = new ResultEntity();
		try{
			if(colId == null)
			{
				result.setCode(-1);
				result.setMsg("收藏ID不能为空");
				return result;
			}
			articleCollectionService.deleteCollection(colId);
			result.setMsg("修改收藏状态成功");
			result.setCode(ResultEntity.SUCCESS);
		}catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}

}
