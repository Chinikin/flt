package com.depression.controller.eap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Article;
import com.depression.model.ArticleCategory;
import com.depression.model.ArticleCustom;
import com.depression.model.ArticleDetail;
import com.depression.model.ArticleType;
import com.depression.model.EapArticleBlock;
import com.depression.model.EapEnterprise;
import com.depression.model.eap.dto.EapArticleDTO;
import com.depression.service.ArticleCategoryService;
import com.depression.service.ArticleCustomService;
import com.depression.service.ArticleDetailService;
import com.depression.service.ArticleService;
import com.depression.service.ArticleTypeService;
import com.depression.service.EapArticleBlockService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.Permission;
import com.depression.service.UbSearchWordsService;
import com.depression.utils.PropertyUtils;
import com.github.pagehelper.Page;

@Controller
@RequestMapping("/EapArticle")
public class EapArticleController
{
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	UbSearchWordsService ubSearchWordsService;
	
	@Autowired
	ArticleCustomService articleCustomService;
	@Autowired
	ArticleService articleService;
	@Autowired
	ArticleTypeService articleTypeService;
	@Autowired
	ArticleCategoryService articleCategoryService;
	@Autowired
	EapEnterpriseService enterpriseService;
	@Autowired
	ArticleDetailService articleDetailService;
	@Autowired
	EapArticleBlockService eapArticleBlockService;

	
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * @param eeId 企业id
	 * @param begin 开始
	 * @param end 结束
	 * @param words 关键字
	 * @param sortTag （1 时间倒序 2 时间顺序 3 点击（阅读）倒序 4 点击（阅读）顺序 5 分享倒序 6 分享顺序 7 评论倒序 8 评论顺序）
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/searchArticle.json")
	@ResponseBody
	@Permission("7")
	Object searchArticle(Long eeId,Date begin,Date end,String words,Integer sortTag,Integer pageIndex,Integer pageSize,Byte isEnable){
		ResultEntity result = new ResultEntity();
		

		if (PropertyUtils.examineOneNull(eeId,pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		Byte xmArticle;
		List<EapArticleDTO> articleDTOs = new ArrayList<EapArticleDTO>();
		Page<ArticleCustom> articles =new Page<ArticleCustom>();
		try{
			
			articles=articleCustomService.getArticlesByPageInEap(eeId, begin, end, words, sortTag, pageIndex, pageSize,isEnable);
			EapEnterprise ee= enterpriseService.selectByPrimaryKey(eeId);
			xmArticle = ee.getXmArticle();
			for(ArticleCustom art : articles)
			{
				EapArticleDTO articleDTO = new EapArticleDTO();
				packArticleDTO(art, articleDTO);
					if( eeId != 0){
						EapArticleBlock eab = eapArticleBlockService.getArticleBlockByArticleIdAndEeId(art.getArticleId(),eeId);
						if(eab != null){
							//此时禁用数据 使用 在 eap文章禁用表中的字段
							articleDTO.setIsEnable(eab.getIsEnable());
						}
				}
				articleDTOs.add(articleDTO);
			}
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		result.put("xmArticle", xmArticle);
		result.put("count", articles.getTotal());
		result.put("articleDTOs", articleDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	
	
	
	/**
	 * 屏蔽心猫文章（全部禁用 部分禁用）
	 * @param eeId 企业id
	 * @param xmArticle  全部禁用标识
	 * @param articleIds 部分禁用文章id
	 * @param isEnable 部分禁用标识
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/blockXmArticle.json")
	@ResponseBody
	@Permission("7")
	Object blockXmArticle(Long eeId,Byte xmArticle, String articleIds,Byte isEnable){
		ResultEntity result = new ResultEntity();
		
		if (PropertyUtils.examineOneNull(eeId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		Integer count = 0;
		
		try{
			//部分禁用
			if(articleIds != null ){
				count = eapArticleBlockService.insertEapArticleBlockByArticleIds(eeId, articleIds, isEnable);
			}else{
			//全部禁用
				EapEnterprise ee= new EapEnterprise();
				ee.setEeId(eeId);
				ee.setXmArticle(xmArticle);
				count+=enterpriseService.updateEnterprise(ee);
			}
			
			
			
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	private void packArticleDTO(ArticleCustom article, EapArticleDTO articleDTO)
	{
		BeanUtils.copyProperties(article, articleDTO);
		
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
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteArticle.json")
	@ResponseBody
	@Permission("7")
	Object deleteArticle(Long eeId,String articleIds){
		ResultEntity result = new ResultEntity();
		Integer count=0;
		try{
			List<Long> ids = JSON.parseArray(articleIds,Long.class);
			for(Long id : ids ){
				Article article = articleService.selectById(id);
				//eap只能删除自己的文章
				if(article.getReleaseFrom() == eeId){
					count+=articleService.deleteByArticleId(article.getArticleId());
				}
				
			}
			
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		result.put("count",count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	/*@RequestMapping(method = RequestMethod.POST, value = "/enableArticle.json")
	@ResponseBody
	@Permission("7")
	Object enableArticle(Long eeId,String articleIds,Byte isEnable){
		ResultEntity result = new ResultEntity();
		Integer count=0;
		try{
			List<Long> ids = JSON.parseArray(articleIds,Long.class);
			for(Long id : ids ){
				Article article = articleService.selectById(id);
				//eap只能禁用自己的文章
				if(article.getReleaseFrom() == eeId){
					article.setIsEnable(isEnable);
					count+=articleService.update(article);
				}
			}
			
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		result.put("count",count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	*/
	
	
	
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
	@Permission("7")
	public Object addArticle(HttpSession session, HttpServletRequest request, Article entity,  String content,String videoLink,String audioPath)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			/*Long categoryId = entity.getCategoryId();
			if (null == categoryId || 0 == categoryId && !StringUtils.isEmpty(oneLevelCategoryId))
			{
				categoryId = Long.valueOf(oneLevelCategoryId);
				entity.setCategoryId(categoryId);
			}*/
			Long typeId = entity.getTypeId();
			
			
			
			if (StringUtils.isEmpty(entity.getAuthor()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("作者不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getDigest()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("摘要不能为空");
				return result;
			}
			if (StringUtils.isEmpty(content))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("内容不能为空");
				return result;
			}
			if (PropertyUtils.examineOneNull(entity.getReleaseFrom()))
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}
			// 查找类别是否存在
			/*ArticleCategory act = new ArticleCategory();
			act.setCategoryId(categoryId);
			act.setTypeId(typeId);
			List<ArticleCategory> acts = articleCategoryService.selectBy(act);
			if (acts.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("类别不存在");
				return result;
			}*/
			// 检查文章标题是否存在
			Article art = new Article();
			art.setTitle(entity.getTitle());
			//art.setCategoryId(categoryId);
			/*art.setTypeId(typeId);
			List<Article> arts = articleService.selectBy(art);
			if (arts.size() > 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("文章已存在");
				return result;
			}*/

			// 插入主表
			articleService.insert(entity);

			// 插入详情表
			ArticleDetail acd = new ArticleDetail();
			acd.setArticleId(entity.getArticleId());
			acd.setContent(content);
			acd.setVideoLink(videoLink);
			acd.setAudioPath(audioPath);
			articleDetailService.insert(acd);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("系统错误");
			log.error(e);
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
	@Permission("7")
	public Object updateArticle(HttpSession session, HttpServletRequest request, Article entity,  String content,String videoLink,String audioPath )
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			/*Long categoryId = entity.getCategoryId();
			
			Long typeId = entity.getTypeId();*/
			//Long articleId = entity.getArticleId();
			if (null == entity.getArticleId())
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("文章id不能为空");
				return result;
			}
			
			if(null == entity.getReleaseFrom())
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("企业id不能为空");
				return result;
			}
			if (StringUtils.isEmpty(entity.getTitle()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("标题不能为空");
				return result;
			}

			if (StringUtils.isEmpty(entity.getAuthor()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("作者不能为空");
				return result;
			}
			/*if (StringUtils.isEmpty(entity.getSource()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("来源不能为空");
				return result;
			}*/
			if (StringUtils.isEmpty(entity.getDigest()))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("摘要不能为空");
				return result;
			}
			if (StringUtils.isEmpty(content))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("内容不能为空");
				return result;
			}
			// 查找类别是否存在
			/*ArticleCategory act = new ArticleCategory();
			act.setCategoryId(categoryId);
			act.setTypeId(typeId);*/
			//List<ArticleCategory> acts = articleCategoryService.selectBy(act);
			//ArticleCategory ac=articleCategoryService.selectById(categoryId);
			/*if (acts.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("类别不存在");
				return result;
			}*/
			
			/*if(ac == null){
				result.setCode(ResultEntity.ERROR);
				result.setMsg("类别不存在");
				return result;
			}*/
			
			// 检查文章标题是否存在
			Article art = new Article();
			art.setTitle(entity.getTitle());
			/*art.setCategoryId(categoryId);
			art.setTypeId(typeId);*/
			/*List<Article> arts = articleService.selectBy(art);
			if (arts.size() > 0)
			{
				// 修改时名称允许和记录本身相同
				if (arts.get(0).getArticleId().longValue() != entity.getArticleId().longValue())
				{
					result.setCode(ResultEntity.ERROR);
					result.setMsg("文章已存在");
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
				result.setCode(ResultEntity.ERROR);
				result.setMsg("文章不存在");
				return result;
			}
			acd = acds.get(0);
			acd.setContent(content);
			acd.setAudioPath(audioPath);
			acd.setVideoLink(videoLink);
			articleDetailService.update(acd);
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("系统错误");
			log.debug(e);
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
	@Permission("7")
	public Object getArticleDetail(HttpSession session, HttpServletRequest request, String articleId)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		try
		{
			if (StringUtils.isEmpty(articleId))
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("参数错误");
				return result;
			}

			Long artId = Long.valueOf(articleId);
			Article article = articleService.selectById(artId);
			if (article == null)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("文章不存在");
				return result;
			}
			
			ArticleDetail actd = new ArticleDetail();
			actd.setArticleId(artId);
			List<ArticleDetail> actds = articleDetailService.selectBy(actd);
			if (actds.size() == 0)
			{
				result.setCode(ResultEntity.ERROR);
				result.setMsg("文章不存在");
				return result;
			}
			ArticleDetail articleDetail = actds.get(0);
			result.put("articleId", articleDetail.getArticleId());
			result.put("content", articleDetail.getContent());
			result.put("detailId", articleDetail.getDetailId());
			result.put("audioPath", articleDetail.getAudioPath());
			result.put("videoLink", articleDetail.getVideoLink());
			
			
			result.put("title", article.getTitle());
			result.put("author", article.getAuthor());
			//result.put("source", article.getSource());
			result.put("digest", article.getDigest());
			result.put("thumbnail", article.getThumbnail());
			// 返回前端显示的图片路径
			result.put("filePath", article.getThumbnail());
			
			
			
			/*Long typeId = article.getTypeId();
			ArticleType artp = new ArticleType();
			artp.setTypeId(typeId);
			List<ArticleType> artps = articleTypeService.selectBy(artp);
			ArticleType articleType = artps.get(0);
			result.put("typeId", typeId);
			result.put("typeName", articleType.getTypeName());*/
			
			/*//类别处理
			ArticleCategory subArticleCategory = new ArticleCategory();
			subArticleCategory.setCategoryId(article.getCategoryId());*/
			
			/*//当前文章类别
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
			}*/
			
		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setMsg("系统错误");
			log.debug(e.toString());
		}
		return result;
	}

	
	
}
