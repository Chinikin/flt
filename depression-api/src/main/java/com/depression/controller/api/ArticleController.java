package com.depression.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Article;
import com.depression.model.ArticleCategory;
import com.depression.model.ArticleDetail;
import com.depression.model.ArticleType;
import com.depression.model.api.dto.ApiArticleDTO;
import com.depression.service.ArticleCategoryService;
import com.depression.service.ArticleCollectionService;
import com.depression.service.ArticleCommentService;
import com.depression.service.ArticleCustomService;
import com.depression.service.ArticleDetailService;
import com.depression.service.ArticleService;
import com.depression.service.ArticleTypeService;
import com.depression.service.MemberService;
import com.depression.service.UnreadCommentService;
import com.depression.utils.PropertyUtils;
import com.github.pagehelper.Page;

/**
 * 文章
 * 
 * @author hongqian_li
 * 
 */
@Controller
@RequestMapping("/Article")
public class ArticleController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	ArticleCommentService articleCommentService;
	@Autowired
	MemberService memberService;
	@Autowired
	ArticleTypeService articleTypeService;
	@Autowired
	ArticleCategoryService articleCategoryService;
	@Autowired
	ArticleService articleService;
	@Autowired
	ArticleDetailService articleDetailService;
	@Autowired
	UnreadCommentService unreadCommentService;
	@Autowired	
	ArticleCollectionService articleCollectionService;
	@Autowired
	ArticleCustomService articleCustomService;

	/**
	 * 获取认识抑郁类型下类别菜单
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getScienceNatureMenu.json")
	@ResponseBody
	public Object getScienceNatureMenu(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		String typeName = "认识抑郁";
		try
		{
			// 数据库初始化时应该固化文章类型：猫闻 认识抑郁
			// 获取类型是认识抑郁类型的所有文章
			ArticleType at = new ArticleType();
			at.setTypeName(typeName);
			List<ArticleType> ats = articleTypeService.selectBy(at);
			if (ats.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("查看数据是否初始化！");
				return result;
			}
			at = ats.get(0);
			// 获取该类型下面所有1级类别
			ArticleCategory oneLevelAc = new ArticleCategory();
			oneLevelAc.setTypeId(at.getTypeId());
			oneLevelAc.setParentCategoryId(0L);
			List<ArticleCategory> oneLevelAcs = articleCategoryService.selectBy(oneLevelAc);
			List<Object> oneLevelJSONs = new ArrayList<>();
			List<Object> subLevelJSONs = new ArrayList<>();
			for (ArticleCategory articleCategory : oneLevelAcs)
			{
				subLevelJSONs = new ArrayList<>();
				// 1级类别
				JSONObject oneLevelJsObj = new JSONObject();
				Long categoryId = articleCategory.getCategoryId();
				oneLevelJsObj.put("categoryId", categoryId);
				oneLevelJsObj.put("typeId", articleCategory.getTypeId());
				oneLevelJsObj.put("typeName", typeName);
				oneLevelJsObj.put("categoryName", articleCategory.getCategoryName());
				// oneLevelJSONs.add(oneLevelJsObj);
				// 根据1级类别获取子类别
				ArticleCategory tempSub = new ArticleCategory();
				tempSub.setParentCategoryId(categoryId);
				List<ArticleCategory> subAcs = articleCategoryService.selectBy(tempSub);
				for (ArticleCategory sub : subAcs)
				{
					JSONObject subLevelJsObj = new JSONObject();
					subLevelJsObj.put("categoryId", sub.getCategoryId());
					// subLevelJsObj.put("typeId", sub.getTypeId());
					// subLevelJsObj.put("typeName", typeName);
					subLevelJsObj.put("categoryName", sub.getCategoryName());
					subLevelJsObj.put("parentCategoryId", sub.getParentCategoryId());
					subLevelJSONs.add(subLevelJsObj);
				}
				oneLevelJsObj.put("subCategory", subLevelJSONs);
				oneLevelJSONs.add(oneLevelJsObj);
			}
			result.put("oneLevelMenu", oneLevelJSONs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取心闻天下类型下类别菜单
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getHeartCatMenu.json")
	@ResponseBody
	public Object getHeartCatMenu(HttpSession session, HttpServletRequest request, Article entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == entity || null == entity.getPageIndex() || null == entity.getPageSize())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}

			ArticleCategory querAarticleCategory = new ArticleCategory();
			querAarticleCategory.setCategoryName("心闻天下");
			List<ArticleCategory> artCtyList = articleCategoryService.selectBy(querAarticleCategory);
			if (artCtyList == null || artCtyList.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("无该类文章");
				return result;
			}
			
			List<ApiArticleDTO> articleDTOs = new ArrayList<ApiArticleDTO>();
			
			ArticleCategory curCategory = artCtyList.get(0);
			entity.setCategoryId(curCategory.getCategoryId());
			entity.setIsEnable((byte) 0);
			List<Article> arts = articleService.selectByPage(entity);
			for (Article article : arts)
			{
				ApiArticleDTO articleDTO = new ApiArticleDTO();
				BeanUtils.copyProperties(article, articleDTO);
				
				//设置图片路径
				articleDTO.setFilePath(article.getThumbnail());
				//查询类型名
				ArticleType articleType = articleTypeService.selectById(article.getTypeId());
				articleDTO.setTypeName(articleType.getTypeName());
				//查询类别名
				ArticleCategory articleCategory = articleCategoryService.selectById(article.getCategoryId());
				articleDTO.setCategoryName(articleCategory.getCategoryName());
			
				articleDTOs.add(articleDTO);
			}
			long totalCount = articleService.selectCount(entity);
			result.put("totalCount", totalCount);
			result.put("Articles", articleDTOs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 查询文章 分页
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getArticleByPage.json")
	@ResponseBody
	public Object getArticleByPage(HttpSession session, HttpServletRequest request, Article entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == entity || null == entity.getPageIndex() || null == entity.getPageSize())
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			List<ApiArticleDTO> articleDTOs = new ArrayList<ApiArticleDTO>();
			
			entity.setReleaseFrom(0L);
			entity.setIsEnable((byte) 0);
			List<Article> arts = articleService.selectByPage(entity);
			for (Article article : arts)
			{
				ApiArticleDTO articleDTO = new ApiArticleDTO();
				BeanUtils.copyProperties(article, articleDTO);
				
				//设置图片路径
				articleDTO.setFilePath(article.getThumbnail());
				//查询类型名
				ArticleType articleType = articleTypeService.selectById(article.getTypeId());
				articleDTO.setTypeName(articleType.getTypeName());
				//查询类别名
				ArticleCategory articleCategory = articleCategoryService.selectById(article.getCategoryId());
				articleDTO.setCategoryName(articleCategory.getCategoryName());
			
				articleDTOs.add(articleDTO);
				
				result.put("author", article.getAuthor());
				result.put("source", article.getSource());
				result.put("digest", article.getDigest());
			}
			long totalCount = articleService.selectCount(entity);
			result.put("totalCount", totalCount);
			result.put("Articles", articleDTOs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 查看文章详情
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/getArticleDetail.json")
	@ResponseBody
	public Object getArticleDetail(HttpSession session, HttpServletRequest request, String articleId, Long mid)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(articleId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			
			Long artId = Long.valueOf(articleId);
			Article article = articleService.selectById(artId);
			if (article == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章不存在");
				return result;
			}
			ArticleDetail actd = new ArticleDetail();
			actd.setArticleId(artId);
			List<ArticleDetail> actds = articleDetailService.selectBy(actd);
			if (actds.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章不存在");
				return result;
			}
			ArticleDetail articleDetail = actds.get(0);
			result.put("articleId", articleDetail.getArticleId());
			result.put("content", articleDetail.getContent());
			result.put("detailId", articleDetail.getDetailId());
			result.put("author", article.getAuthor());
			result.put("source", article.getSource());
			result.put("digest", article.getDigest());

			// TODO 点击量更新，后期考虑优化
			if (article != null && (article.getHits() == null || article.getHits().equals("")))
			{
				article.setHits(1L);
			} else
			{
				article.setHits(article.getHits() + 1);
			}
			articleService.update(article);

			result.put("title", article.getTitle());
			if (article.getHits() == null)
			{
				result.put("hits", 0);
			} else
			{
				result.put("hits", article.getHits());
			}
			if (article.getCollectionNum() == null)
			{
				result.put("collectionNum", 0);
			} else
			{
				result.put("collectionNum", article.getCollectionNum());
			}
			result.put("createTime", article.getCreateTime());
			result.put("createTimestamp", article.getCreateTime().getTime());
			result.put("modifyTime", article.getModifyTime());
			if (article.getThumbnail() != null && !article.getThumbnail().equals(""))
			{
				result.put("filePath", article.getThumbnail());
			}
			else 
			{
				result.put("filePath", "");
			}
			Long typeId = article.getTypeId();
			ArticleType artp = new ArticleType();
			artp.setTypeId(typeId);
			List<ArticleType> artps = articleTypeService.selectBy(artp);
			ArticleType articleType = artps.get(0);
			result.put("typeId", typeId);
			result.put("typeName", articleType.getTypeName());
			ArticleCategory subArticleCategory = new ArticleCategory();
			subArticleCategory.setCategoryId(article.getCategoryId());
			List<ArticleCategory> subArticleCategorys = articleCategoryService.selectBy(subArticleCategory);
			subArticleCategory = subArticleCategorys.get(0);
			if (subArticleCategory.getParentCategoryId() != 0)
			{
				// 说明有父类别
				result.put("subCategoryId", subArticleCategory.getCategoryId());
				result.put("subCategoryName", subArticleCategory.getCategoryName());
				ArticleCategory articleCategory = new ArticleCategory();
				articleCategory.setCategoryId(subArticleCategory.getParentCategoryId());
				List<ArticleCategory> articleCategorys = articleCategoryService.selectBy(articleCategory);
				articleCategory = articleCategorys.get(0);
				result.put("categoryId", articleCategory.getCategoryId());
				result.put("categoryName", articleCategory.getCategoryName());
			} else
			{
				// 无之类，之类就是父类
				result.put("categoryId", subArticleCategory.getCategoryId());
				result.put("categoryName", subArticleCategory.getCategoryName());
				result.put("subCategoryId", "");
				result.put("subCategoryName", "");
			}
			
			//删除未读评论
//			if(mid != null)
//			{
//				unreadCommentService.deleteUnreadArticleComment(mid, Long.valueOf(articleId));
//			}
			
			return result;
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 首页查询最新资讯文章
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getArticleByHomePage.json")
	@ResponseBody
	public Object getArticleByHomePage(HttpSession session, HttpServletRequest request, Integer size)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			// 参数检查
			if (null == size)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取条数不能为空");
				return result;
			}
			


			// 认识抑郁
			List<ApiArticleDTO> articleDTOs = new ArrayList<ApiArticleDTO>();
			
			Map<String, List<Article>> obj = new HashMap<String, List<Article>>();
			String typeName1 = "认识抑郁";
			List<Article> list1 = articleService.getLatestByTypeName(typeName1, size, (byte) 0);
			for (Article article : list1)
			{
				ApiArticleDTO articleDTO = new ApiArticleDTO();
				BeanUtils.copyProperties(article, articleDTO);
				
				//设置图片路径
				articleDTO.setFilePath(article.getThumbnail());
				//查询类型名
				ArticleType articleType = articleTypeService.selectById(article.getTypeId());
				articleDTO.setTypeName(articleType.getTypeName());
				//查询类别名
				ArticleCategory articleCategory = articleCategoryService.selectById(article.getCategoryId());
				articleDTO.setCategoryName(articleCategory.getCategoryName());
			
				articleDTOs.add(articleDTO);
				
				result.put("author", article.getAuthor());
				result.put("source", article.getSource());
				result.put("digest", article.getDigest());
			}
			result.put("rsyy", articleDTOs);

			// 心闻天下
			articleDTOs = new ArrayList<ApiArticleDTO>();
			
			String typeName2 = "心闻天下";
			List<Article> list2 = articleService.getLatestByTypeName(typeName2, size, (byte) 0);
			
			for (Article article : list2)
			{
				ApiArticleDTO articleDTO = new ApiArticleDTO();
				BeanUtils.copyProperties(article, articleDTO);
				
				//设置图片路径
				articleDTO.setFilePath(article.getThumbnail());
				//查询类型名
				ArticleType articleType = articleTypeService.selectById(article.getTypeId());
				articleDTO.setTypeName(articleType.getTypeName());
				//查询类别名
				ArticleCategory articleCategory = articleCategoryService.selectById(article.getCategoryId());
				articleDTO.setCategoryName(articleCategory.getCategoryName());
			
				articleDTOs.add(articleDTO);
				
				result.put("author", article.getAuthor());
				result.put("source", article.getSource());
				result.put("digest", article.getDigest());
			}
			result.put("xwtx", articleDTOs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}
	
	/**
	 * 获取"认识抑郁", "常见问题"下的点击量最多文章列表
	 * @param session
	 * @param request
	 * @param size   要求获取的文章数量
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getScienceFaqList.json")
	@ResponseBody
	public Object getScienceFaqList(HttpSession session, HttpServletRequest request, Integer size)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			// 参数检查
			if (null == size)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("获取条数不能为空");
				return result;
			}

			// 认识抑郁, 常见问题
			List<Article> faqList = articleService.getLatestByTypeNameAndCategoryName(
					"认识抑郁", "常见问题", size, (byte) 0);
			result.put("list", faqList);
			result.put("count", faqList.size());

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}
	
	private void packArticleDTO(Article article, ApiArticleDTO articleDTO)
	{
		BeanUtils.copyProperties(article, articleDTO);
		
		articleDTO.setCreateTimestamp(article.getCreateTime().getTime());
		//设置图片路径
		articleDTO.setFilePath(article.getThumbnail());
		//查询类型名
		ArticleType articleType = articleTypeService.selectById(article.getTypeId());
		if(articleType != null)
		{
			articleDTO.setTypeName(articleType.getTypeName());
		}
		//查询类别名
		ArticleCategory articleCategory = articleCategoryService.selectById(article.getCategoryId());
		if(articleCategory != null)
		{
			articleDTO.setCategoryName(articleCategory.getCategoryName());
		}
	}
	
	/**
	 * 分页获取最新的文章
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainArticlesNew.json")
	@ResponseBody
	public Object obtainArticlesNewV1(Integer pageIndex, Integer pageSize,Long releaseFrom)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				pageIndex,
				pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiArticleDTO> articleDTOs = new ArrayList<ApiArticleDTO>();
		Page<Article> articles = new Page<Article>() ;
		
		if(releaseFrom == null){
			releaseFrom = 0L;
		}
		try{
			
			articles = articleCustomService.getValidArticlesByPage(releaseFrom, pageIndex, pageSize);
			for(Article art : articles)
			{
				ApiArticleDTO articleDTO = new ApiArticleDTO();
				packArticleDTO(art, articleDTO);
				
				articleDTOs.add(articleDTO);
			}
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", articles.getTotal());
		result.put("articleDTOs", articleDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 获取文章详情
	 * @param articleId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/V1/obtainArticleDetail.json")
	@ResponseBody
	public Object obtainArticleDetailV1(Long articleId, Long mid)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				articleId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		ApiArticleDTO articleDTO = new ApiArticleDTO();
		try{
			Article article = articleService.selectById(articleId);
			if(article == null)
			{
				result.setCode(ErrorCode.ERROR_ID_INEXISTENT.getCode());
				result.setMsg(ErrorCode.ERROR_ID_INEXISTENT.getMessage());
				return result;
			}
			//更新点击量
			article.setHits(article.getHits() + 1);
			articleService.update(article);
			//分装dto
			packArticleDTO(article, articleDTO);
			//查询详情
			ArticleDetail articleDetail = articleDetailService.selectByArticleId(article.getArticleId());
			if(articleDetail != null)
			{
				articleDTO.setDetail(articleDetail.getContent());
			}
			
			//是否收藏了		
			if(mid != null)
			{
				articleDTO.setIsCollected(articleCollectionService.isCollected(articleId, mid));
			}else
			{
				articleDTO.setIsCollected((byte) 0);
			}
			
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("articleDTO", articleDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
		
	}

	
	/**
	 * 文章分享
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/shareArticle.json")
	@ResponseBody
	public Object shareArticle(Long articleId)
	{
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(
				articleId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			Article article = articleService.selectById(articleId);
			article.setShare(article.getShare()+1);
			articleService.update(article);
		}catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
}
