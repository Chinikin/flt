package com.depression.controller.web;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ResultEntity;
import com.depression.model.Article;
import com.depression.model.ArticleCategory;
import com.depression.model.ArticleDetail;
import com.depression.model.ArticleType;
import com.depression.model.web.dto.WebArticleCategoryDTO;
import com.depression.model.web.dto.WebArticleDTO;
import com.depression.service.ArticleCategoryService;
import com.depression.service.ArticleDetailService;
import com.depression.service.ArticleService;
import com.depression.service.ArticleTypeService;

/**
 * 文章管理
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
	ArticleTypeService articleTypeService;
	@Autowired
	ArticleCategoryService articleCategoryService;
	@Autowired
	ArticleService articleService;
	@Autowired
	ArticleDetailService articleDetailService;

	// 文章类型管理
	/**
	 * 添加类别
	 * 
	 * @param session
	 * 
	 * @param request
	 * @param typeName
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/type/addType.json")
	@ResponseBody
	public Object addType(HttpSession session, HttpServletRequest request, String typeName)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(typeName))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			// 查询是否有重名
			ArticleType articleType = new ArticleType();
			articleType.setTypeName(typeName);
			List<ArticleType> articleTypes = articleTypeService.selectBy(articleType);
			if (articleTypes.size() > 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类型已存在！");
				return result;
			}
			articleTypeService.insert(articleType);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取全部类型
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/type/getAllType.json")
	@ResponseBody
	public Object getAllType(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			List<ArticleType> ats = articleTypeService.selectBy(new ArticleType());

			if (ats.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("暂无类别，需初始化数据库");
				return result;
			}
			result.put("Alltype", ats);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取类型 分页
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/type/getTypeByPage.json")
	@ResponseBody
	public Object getTypeByPage(HttpSession session, HttpServletRequest request, ArticleType entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == (entity.getPageIndex()) || null == (entity.getPageSize()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			List<ArticleType> ats = articleTypeService.selectByPage(entity);
			// if (ats.size() == 0)
			// {
			// result.setCode(ResultEntity.ERROR);
			// result.setError("暂无类别，需初始化数据库");
			// return result;
			// }
			long totalCount = articleTypeService.selectCount(entity);
			result.put("Alltype", ats);
			result.put("totalCount", totalCount);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 删除文章类型
	 * 
	 * @param session
	 * @param request
	 * @param typeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/type/deleteArticleType.json")
	@ResponseBody
	public Object deleteArticleType(HttpSession session, HttpServletRequest request, String typeId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{

			if (StringUtils.isEmpty(typeId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			String temp = typeId.replaceAll("\\[([^\\]]*)\\]", "$1");
			String[] typeIds = temp.split(",");
			List<Long> ids = new ArrayList<Long>();
			for (String id : typeIds)
			{
				// 查找该类型是否存在				
				ids.add(Long.parseLong(id));
			}
			if(ids.size() > 0)
			{
				articleTypeService.deleteBulk(ids);
			}
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 修改类型名称
	 * 
	 * @param session
	 * @param request
	 * @param typeId
	 * @param typeNewName
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/type/reTypeName.json")
	@ResponseBody
	public Object reTypeName(HttpSession session, HttpServletRequest request, String typeId, String typeNewName)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(typeId) || StringUtils.isEmpty(typeNewName))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}

			// 查找该类型是否存在
			ArticleType act = new ArticleType();
			act.setTypeName(typeNewName);
			List<ArticleType> acts = articleTypeService.selectBy(act);
			if (acts.size() != 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("已经存在该类型");
				return result;
			}

			// 查找该类型是否存在
			ArticleType actQuery = new ArticleType();
			actQuery.setTypeId(Long.parseLong(typeId));
			List<ArticleType> actExist = articleTypeService.selectBy(actQuery);
			if (actExist.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("不存在该类型");
				return result;
			}
			// 更新
			ArticleType articleType = actExist.get(0);
			articleType.setTypeName(typeNewName);
			articleTypeService.update(articleType);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	// 文章类别管理

	/**
	 * 获取一级类别 parentID==0 根据 文章类型typeId
	 * 
	 * @param session
	 * @param request
	 * @param type
	 *            类别
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/category/getCategoryLevel1ByType.json")
	@ResponseBody
	public Object getCategoryLevel1ByType(HttpSession session, HttpServletRequest request, String typeId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(typeId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			
			List<WebArticleCategoryDTO> articleCategoryDTOs = new ArrayList<WebArticleCategoryDTO>();
			
			ArticleCategory actc = new ArticleCategory();
			actc.setParentCategoryId(0L);
			actc.setTypeId(Long.valueOf(typeId));
			List<ArticleCategory> scs = articleCategoryService.selectBy(actc);
			for (ArticleCategory articleCategory : scs)
			{
				WebArticleCategoryDTO articleCategoryDTO = new WebArticleCategoryDTO();
				BeanUtils.copyProperties(articleCategory, articleCategoryDTO);
				
				ArticleType articleType = articleTypeService.selectById(articleCategory.getTypeId());

				articleCategoryDTO.setTypeName(articleType.getTypeName());

				articleCategoryDTO.setFilePath(articleCategory.getThumbnail());

				articleCategoryDTOs.add(articleCategoryDTO);
			}
			
			result.put("Category", articleCategoryDTOs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取子级类别
	 * 
	 * @param session
	 * @param request
	 * @param typeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/category/getSubCategory.json")
	@ResponseBody
	public Object getSubCategory(HttpSession session, HttpServletRequest request, String parentCategoryId, String typeId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(typeId) || StringUtils.isEmpty(parentCategoryId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
			}
			List<WebArticleCategoryDTO> articleCategoryDTOs = new ArrayList<WebArticleCategoryDTO>();
			
			ArticleCategory actc = new ArticleCategory();
			actc.setParentCategoryId(Long.valueOf(parentCategoryId));
			actc.setTypeId(Long.valueOf(typeId));
			List<ArticleCategory> scs = articleCategoryService.selectBy(actc);
			for (ArticleCategory articleCategory : scs)
			{
				WebArticleCategoryDTO articleCategoryDTO = new WebArticleCategoryDTO();
				BeanUtils.copyProperties(articleCategory, articleCategoryDTO);
				
				ArticleType articleType = articleTypeService.selectById(articleCategory.getTypeId());

				articleCategoryDTO.setTypeName(articleType.getTypeName());

				articleCategoryDTO.setFilePath(articleCategory.getThumbnail());

				articleCategoryDTOs.add(articleCategoryDTO);
			}
			result.put("Category", articleCategoryDTOs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 获取类别 分页
	 * 
	 * @param session
	 * @param request
	 * @param typeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/category/getCategoryByPage.json")
	@ResponseBody
	public Object getCategoryByPage(HttpSession session, HttpServletRequest request, ArticleCategory entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (null == (entity.getPageSize()) || null == (entity.getPageIndex()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			
			List<WebArticleCategoryDTO> articleCategoryDTOs = new ArrayList<WebArticleCategoryDTO>();

			List<ArticleCategory> scs = articleCategoryService.selectByPage(entity);
			long totalCount = articleCategoryService.selectCount(entity);
			for (ArticleCategory articleCategory : scs)
			{
				WebArticleCategoryDTO articleCategoryDTO = new WebArticleCategoryDTO();
				BeanUtils.copyProperties(articleCategory, articleCategoryDTO);
				
				ArticleType articleType = articleTypeService.selectById(articleCategory.getTypeId());

				articleCategoryDTO.setTypeName(articleType.getTypeName());

				articleCategoryDTO.setFilePath(articleCategory.getThumbnail());

				articleCategoryDTOs.add(articleCategoryDTO);
			}
			result.put("totalCount", totalCount);
			result.put("Category", articleCategoryDTOs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 查询类别 TODO 废弃
	 * 
	 * @param session
	 * @param request
	 * @param typeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/category/selectCategory.json")
	@ResponseBody
	public Object selectCategory(HttpSession session, HttpServletRequest request, String categoryName, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(categoryName))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			
			List<WebArticleCategoryDTO> articleCategoryDTOs = new ArrayList<WebArticleCategoryDTO>();

			ArticleCategory entity = new ArticleCategory();
			entity.setPageIndex(pageIndex);
			entity.setPageSize(pageSize);
			List<ArticleCategory> scs = articleCategoryService.selectByPage(entity);
			long totalCount = articleCategoryService.selectCount(entity);
			for (ArticleCategory articleCategory : scs)
			{
				WebArticleCategoryDTO articleCategoryDTO = new WebArticleCategoryDTO();
				BeanUtils.copyProperties(articleCategory, articleCategoryDTO);
				
				ArticleType articleType = articleTypeService.selectById(articleCategory.getTypeId());

				articleCategoryDTO.setTypeName(articleType.getTypeName());

				articleCategoryDTO.setFilePath(articleCategory.getThumbnail());

				articleCategoryDTOs.add(articleCategoryDTO);
			}
			result.put("totalCount", totalCount);
			result.put("Category", articleCategoryDTOs);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 删除子级类别
	 * 
	 * @param session
	 * @param request
	 * @param typeId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/category/deleteSubCategory.json")
	@ResponseBody
	public Object deleteSubCategory(HttpSession session, HttpServletRequest request, String categoryId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(categoryId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			String temp = categoryId.replaceAll("\\[([^\\]]*)\\]", "$1");
			String[] categoryIds = temp.split(",");
			for (String id : categoryIds)
			{
				ArticleCategory actc = new ArticleCategory();
				actc.setCategoryId(Long.valueOf(id));
				List<ArticleCategory> scs = articleCategoryService.selectBy(actc);
				if (scs.size() == 0)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("类别不存在");
					return result;
				}
				ArticleCategory articleCategory = scs.get(0);
				if (articleCategory.getParentCategoryId().equals(0))
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("无权限修改该类别");
					return result;
				}
				List<Long> ids = new ArrayList<Long>();
				ids.add(Long.parseLong(id));
				articleCategoryService.deleteBulk(ids);
			}
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 添加子类别
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/category/addSubCategory.json")
	@ResponseBody
	public Object addSubCategory(HttpSession session, HttpServletRequest request, ArticleCategory entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			Long typeId = entity.getTypeId();
			Long parentCategoryId = entity.getParentCategoryId();
			String categoryName = entity.getCategoryName();
			if (null == entity || null == typeId || null == parentCategoryId || StringUtils.isEmpty(categoryName))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}
			// 检查类型是否存在
			ArticleType at = new ArticleType();
			at.setTypeId(typeId);
			List<ArticleType> ats = articleTypeService.selectBy(at);
			if (ats.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类型不存在");
				return result;
			}
			// 检查父级类别是否存在
			ArticleCategory selectAct = new ArticleCategory();
			selectAct.setTypeId(typeId);
			selectAct.setCategoryId(parentCategoryId);
			List<ArticleCategory> selectActs = articleCategoryService.selectBy(selectAct);
			if (selectActs.size() == 0 && 0 != parentCategoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("父级类别不存在");
				return result;
			}
			// 检查类别是否重名
			selectAct = new ArticleCategory();
			selectAct.setTypeId(typeId);
			selectAct.setParentCategoryId(parentCategoryId);
			selectAct.setCategoryName(categoryName);
			selectAct.setThumbnail(entity.getThumbnail());
			List<ArticleCategory> scs = articleCategoryService.selectBy(selectAct);
			if (scs.size() != 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类别已存在");
				return result;
			}

			articleCategoryService.insert(selectAct);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 更新子类别
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/category/updateSubCategory.json")
	@ResponseBody
	public Object updateSubCategory(HttpSession session, HttpServletRequest request, ArticleCategory entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			Long typeId = entity.getTypeId();
			Long parentCategoryId = entity.getParentCategoryId();
			String categoryName = entity.getCategoryName();
			Long cId = entity.getCategoryId();
			if (null == entity || null == cId || null == typeId || null == parentCategoryId || StringUtils.isEmpty(categoryName))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("参数错误");
				return result;
			}

			// 检查是否是一级
			ArticleCategory act = new ArticleCategory();
			act.setCategoryId(cId);
			List<ArticleCategory> acts = articleCategoryService.selectBy(act);
			if (acts.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("不存在该类别");
				return result;
			}
			act = acts.get(0);
			Long pId = act.getParentCategoryId();
			if (pId.equals(0))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("无权限修改该类别");
				return result;
			}
			// 检查类型是否存在
			ArticleType at = new ArticleType();
			at.setTypeId(typeId);
			List<ArticleType> ats = articleTypeService.selectBy(at);
			if (ats.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类型不存在");
				return result;
			}
			// 检查父级类别是否存在
			ArticleCategory selectAct = new ArticleCategory();
			selectAct.setTypeId(typeId);
			selectAct.setCategoryId(parentCategoryId);
			List<ArticleCategory> selectActs = articleCategoryService.selectBy(selectAct);
			if (selectActs.size() == 0 && 0 != parentCategoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("父级类别不存在");
				return result;
			}
			// 检查类别是否重名
			selectAct = new ArticleCategory();
			selectAct.setTypeId(typeId);
			selectAct.setParentCategoryId(parentCategoryId);
			selectAct.setCategoryName(categoryName);
			List<ArticleCategory> scs = articleCategoryService.selectBy(selectAct);
			if (scs.size() != 0)
			{
				// 修改时名称允许和记录本身相同
				if (scs.get(0).getCategoryId().longValue() != entity.getCategoryId().longValue())
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("类别已存在");
					return result;
				}
			}

			articleCategoryService.update(entity);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	// 文章管理

	/**
	 * 添加文章
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addArticle.json")
	@ResponseBody
	public Object addArticle(HttpSession session, HttpServletRequest request, Article entity, String oneLevelCategoryId, String content)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			Long categoryId = entity.getCategoryId();
			if (null == categoryId || 0 == categoryId && !StringUtils.isEmpty(oneLevelCategoryId))
			{
				categoryId = Long.valueOf(oneLevelCategoryId);
				entity.setCategoryId(categoryId);
			}
			Long typeId = entity.getTypeId();
			if (null == categoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类别id不能为空");
				return result;
			}
			if (null == typeId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类型id不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getTitle()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标题不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getAuthor()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("作者不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getSource()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("来源不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getDigest()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("摘要不能为空");
				return result;
			}
			if (StringUtils.isEmpty(content))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("内容不能为空");
				return result;
			}
			// 查找类别是否存在
			ArticleCategory act = new ArticleCategory();
			act.setCategoryId(categoryId);
			act.setTypeId(typeId);
			List<ArticleCategory> acts = articleCategoryService.selectBy(act);
			if (acts.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类别不存在");
				return result;
			}
			// 检查文章标题是否存在
			Article art = new Article();
			art.setTitle(entity.getTitle());
			art.setCategoryId(categoryId);
			art.setTypeId(typeId);
			List<Article> arts = articleService.selectBy(art);
			if (arts.size() > 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章已存在");
				return result;
			}

			// 插入主表
			articleService.insert(entity);

			// 插入详情表
			ArticleDetail acd = new ArticleDetail();
			acd.setArticleId(entity.getArticleId());
			acd.setContent(content);
			articleDetailService.insert(acd);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}

	/**
	 * 查询文章
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getArticle.json")
	@ResponseBody
	public Object getArticle(HttpSession session, HttpServletRequest request, Article entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			List<WebArticleDTO> articleDTOs = new ArrayList<WebArticleDTO>();
			
			entity.setReleaseFrom(0L);
			List<Article> arts = articleService.selectBy(entity);
			if (arts.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章不存在");
				return result;
			}
			for (Article article : arts)
			{
				WebArticleDTO articleDTO = new WebArticleDTO();
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
			List<WebArticleDTO> articleDTOs = new ArrayList<WebArticleDTO>();
			
			entity.setReleaseFrom(0L);
			List<Article> arts = articleService.selectByPageOrderBy(entity);
			if (arts.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("文章不存在");
				return result;
			}
			for (Article article : arts)
			{
				WebArticleDTO articleDTO = new WebArticleDTO();
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
	 * 查看文章详情
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getArticleDetail.json")
	@ResponseBody
	public Object getArticleDetail(HttpSession session, HttpServletRequest request, String articleId)
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

			result.put("title", article.getTitle());
			result.put("author", article.getAuthor());
			result.put("source", article.getSource());
			result.put("digest", article.getDigest());
			result.put("thumbnail", article.getThumbnail());
			// 返回前端显示的图片路径
			result.put("filePath", article.getThumbnail());
			Long typeId = article.getTypeId();
			ArticleType artp = new ArticleType();
			artp.setTypeId(typeId);
			List<ArticleType> artps = articleTypeService.selectBy(artp);
			ArticleType articleType = artps.get(0);
			result.put("typeId", typeId);
			result.put("typeName", articleType.getTypeName());
			
			//类别处理
			ArticleCategory subArticleCategory = new ArticleCategory();
			subArticleCategory.setCategoryId(article.getCategoryId());
			
			//当前文章类别
			ArticleCategory ac = articleCategoryService.selectById(article.getCategoryId());
			if(ac.getParentCategoryId() != 0 ){
				ArticleCategory pac=articleCategoryService.selectById(ac.getParentCategoryId());
				result.put("categoryName", pac.getCategoryName());
				result.put("categoryId", pac.getCategoryId());
				result.put("subCategoryName", ac.getCategoryName());
				result.put("subCategoryId", ac.getCategoryId());
			} else
			{
				// 无之类，之类就是父类
				result.put("categoryId", subArticleCategory.getCategoryId());
				result.put("categoryName", subArticleCategory.getCategoryName());
				result.put("subCategoryId", "");
				result.put("subCategoryName", "");
			}
			
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 禁用文章
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enableArticle.json")
	@ResponseBody
	public Object enableArticle(String articleId, Byte isEnable)
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
			String temp = articleId.replaceAll("\\[([^\\]]*)\\]", "$1");
			String[] articleIds = temp.split(",");
			for (String id : articleIds)
			{
				Article article = articleService.selectById(Long.valueOf(id));
				if (article == null)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("文章不存在");
					return result;
				}
				articleService.enable(Long.valueOf(id), isEnable);
			}
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}
	
	/**
	 * 删除文章
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteArticle.json")
	@ResponseBody
	public Object deleteArticle(HttpSession session, HttpServletRequest request, String articleId)
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
			String temp = articleId.replaceAll("\\[([^\\]]*)\\]", "$1");
			String[] articleIds = temp.split(",");
			for (String id : articleIds)
			{
				Article article = articleService.selectById(Long.valueOf(id));
				if (article == null)
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("文章不存在");
					return result;
				}
				List<Long> ids = new ArrayList<Long>();
				ids.add(Long.parseLong(id));
				articleService.deleteBulk(ids);
			}
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	/**
	 * 更新文章
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateArticle.json")
	@ResponseBody
	public Object updateArticle(HttpSession session, HttpServletRequest request, Article entity, String oneLevelCategoryId, String content)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			Long categoryId = entity.getCategoryId();
			/*if ((null == categoryId || 0 == categoryId) && !StringUtils.isEmpty(oneLevelCategoryId))
			{
				categoryId = Long.valueOf(oneLevelCategoryId);
				entity.setCategoryId(categoryId);
			}*/
			Long typeId = entity.getTypeId();
			//Long articleId = entity.getArticleId();
			if (null == categoryId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类别id不能为空");
				return result;
			}
			if (null == typeId)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类型id不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getTitle()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("标题不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getAuthor()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("作者不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getSource()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("来源不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getDigest()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("摘要不能为空");
				return result;
			}
			if (StringUtils.isEmpty(content))
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("内容不能为空");
				return result;
			}
			// 查找类别是否存在
			ArticleCategory act = new ArticleCategory();
			act.setCategoryId(categoryId);
			act.setTypeId(typeId);
			//List<ArticleCategory> acts = articleCategoryService.selectBy(act);
			ArticleCategory ac=articleCategoryService.selectById(categoryId);
			/*if (acts.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setError("类别不存在");
				return result;
			}*/
			
			if(ac == null){
				result.setCode(ResultEntity.ERROR);
				result.setError("类别不存在");
				return result;
			}
			
			// 检查文章标题是否存在
			Article art = new Article();
			art.setTitle(entity.getTitle());
			art.setCategoryId(categoryId);
			art.setTypeId(typeId);
			//List<Article> arts = articleService.selectBy(art);
			/*if (arts.size() > 0)
			{
				// 修改时名称允许和记录本身相同
				if (arts.get(0).getArticleId().longValue() != entity.getArticleId().longValue())
				{
					result.setCode(ResultEntity.ERROR);
					result.setError("文章已存在");
					return result;
				}
			}*/

			// 更新摘要表
			articleService.update(entity);
			// 更新详情表
			ArticleDetail acd = new ArticleDetail();
			acd.setArticleId(entity.getArticleId());
			List<ArticleDetail> acds = articleDetailService.selectBy(acd);
			if (acds.size() == 0)
			{
				// 插入详情表
				ArticleDetail acd1 = new ArticleDetail();
				acd1.setArticleId(entity.getArticleId());
				acd1.setContent(content);
				articleDetailService.insert(acd1);
			}else{
			acd = acds.get(0);
			acd.setContent(content);
			articleDetailService.update(acd);
			}
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.debug(e);
		}
		return result;
	}
	
	
}
