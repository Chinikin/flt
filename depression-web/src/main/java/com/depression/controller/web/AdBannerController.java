package com.depression.controller.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdBanner;
import com.depression.model.Article;
import com.depression.model.ArticleCategory;
import com.depression.model.ArticleType;
import com.depression.model.Member;
import com.depression.model.Testing;
import com.depression.model.api.dto.ApiPsychoDTO;
import com.depression.model.web.dto.AdBannerDTO;
import com.depression.model.web.dto.TestingDTO;
import com.depression.model.web.dto.WebArticleDTO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.service.AdBannerService;
import com.depression.service.ArticleCategoryService;
import com.depression.service.ArticleService;
import com.depression.service.ArticleTypeService;
import com.depression.service.MemberService;
import com.depression.service.TestingService;
import com.depression.service.TestingTypeService;
import com.depression.utils.PropertyUtils;
import com.github.pagehelper.Page;

/**
 * banner管理
 * 
 * @author fanxinhui
 * 
 */
@Controller
@RequestMapping("/adBanner")
public class AdBannerController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AdBannerService adBannerService;

	@Autowired
	MemberService memberService;

	@Autowired
	ArticleService articleService;

	@Autowired
	private TestingService testingService;
	@Autowired
	ArticleTypeService articleTypeService;
	@Autowired
	ArticleCategoryService articleCategoryService;
	@Autowired
	TestingTypeService testingTypeService;

	/**
	 * 分页列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap modelMap, AdBanner banner)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (banner == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (banner.getPageIndex() == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (banner.getPageSize() == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}

		// 设置查询条件
		AdBanner queryBanner = new AdBanner();
		queryBanner.setReleaseFrom(0L);
		queryBanner.setPageIndex(banner.getPageIndex());
		queryBanner.setPageSize(banner.getPageSize());
		queryBanner.setShowLocation(banner.getShowLocation());
		if (banner.getBannerTitle() != null && !banner.getBannerTitle().equals(""))
		{
			queryBanner.setBannerTitle(banner.getBannerTitle());
		}

		// 查询集合
		int totalCount = adBannerService.getPageCounts(queryBanner);
		Page<AdBanner> list = adBannerService.getPageList(queryBanner);
		List<AdBannerDTO> dtoList = new ArrayList<AdBannerDTO>();
		for (AdBanner adBanner : list)
		{
			AdBannerDTO adBannerDTO = new AdBannerDTO();
			BeanUtils.copyProperties(adBanner, adBannerDTO);
			// 转换实际文件路径
			if (adBanner.getPicPath() != null && !adBanner.getPicPath().equals(""))
			{
				adBannerDTO.setFilePath(adBanner.getPicPath());
			}
			dtoList.add(adBannerDTO);
		}
		result.put("list", dtoList);
		result.put("count", totalCount);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 新增记录
	 * 
	 * @param request
	 * @param modelMap
	 * @param banner
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, AdBanner banner)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (banner == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (banner.getBannerTitle() == null || banner.getBannerTitle().equals(""))
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}

		// 检查排序编号是否重复
		AdBanner queryAdBanner = new AdBanner();
		queryAdBanner.setSorting(banner.getSorting());
		queryAdBanner.setShowLocation(banner.getShowLocation());
		List<AdBanner> bannerList = adBannerService.selectSelective(queryAdBanner);
		if (bannerList.size() > 0)
		{
			result.setCode(-1);
			result.setMsg("排序序号重复");
			return result;
		}

		// 插入记录
		adBannerService.insert(banner);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 更新记录
	 * 
	 * @param request
	 * @param modelMap
	 * @param banner
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, AdBanner banner)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (banner == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (banner.getBannerId() == null || banner.getBannerId().equals(""))
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}
		if (banner.getBannerTitle() == null || banner.getBannerTitle().equals(""))
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}

		AdBanner bannerRtn = adBannerService.selectByPrimaryKey(banner.getBannerId());
		if (bannerRtn == null)
		{
			result.setCode(-1);
			result.setMsg("记录不存在");
			return result;
		}

		// 检查排序编号是否重复
		AdBanner queryAdBanner = new AdBanner();
		queryAdBanner.setSorting(banner.getSorting());
		queryAdBanner.setShowLocation(banner.getShowLocation());
		List<AdBanner> bannerList = adBannerService.selectSelective(queryAdBanner);
		if (bannerList.size() > 0 && !bannerList.get(0).getBannerId().equals(banner.getBannerId()) )
		{
			result.setCode(-1);
			result.setMsg("排序序号重复");
			return result;
		}

		// 更新
		adBannerService.update(banner);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 根据id列表批量删除记录
	 * 
	 * @param request
	 * @param modelMap
	 * @param ids
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/delete.json")
	@ResponseBody
	public Object delete(HttpServletRequest request, ModelMap modelMap, WebIdsVO ids)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (PropertyUtils.examineOneNull(ids))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 批量删除记录
		adBannerService.deleteByPrimaryKeyBulk(ids.getIds());

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg("状态更新成功");
		return result;
	}

	/**
	 * 详情
	 * 
	 * @param request
	 * @param modelMap
	 * @param objId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, ModelMap modelMap, Long bannerId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (bannerId == null || bannerId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}

		// 查询记录详情
		AdBanner adBanner = adBannerService.selectByPrimaryKey(bannerId);
		AdBannerDTO adBannerDTO = new AdBannerDTO();
		BeanUtils.copyProperties(adBanner, adBannerDTO);

		// 转换实际文件路径
		if (adBanner.getPicPath() != null && !adBanner.getPicPath().equals(""))
		{
			adBannerDTO.setFilePath(adBanner.getPicPath());
		}

		result.put("obj", adBannerDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 查询咨询师列表(根据咨询师名字精确查询)
	 * 
	 * @param request
	 * @param modelMap
	 * @param objId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getPsychoByName.json")
	@ResponseBody
	public Object getPsychoByName(HttpServletRequest request, ModelMap modelMap, String name)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (name == null || name.equals(""))
		{
			result.setCode(-1);
			result.setMsg("咨询师名字不能为空");
			return result;
		}

		// 查询记录详情
		List<Member> members = memberService.searchPsychoByName(name);
		List<ApiPsychoDTO> psychoDTOs = new ArrayList<ApiPsychoDTO>();
		for (Member member : members)
		{
			ApiPsychoDTO psychoDTO = new ApiPsychoDTO();
			BeanUtils.copyProperties(member, psychoDTO);
			psychoDTO.setAvatar(member.getAvatar());
			psychoDTO.setAvatarThumbnail(member.getAvatarThumbnail());
			psychoDTO.setCandidPhoto(member.getCandidPhoto());
			psychoDTOs.add(psychoDTO);
		}

		result.put("list", psychoDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 查询文章列表(根据文章标题精确查询)
	 * 
	 * @param request
	 * @param modelMap
	 * @param objId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getArticleByTitle.json")
	@ResponseBody
	public Object getArticleByTitle(HttpServletRequest request, ModelMap modelMap, String title)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (title == null || title.equals(""))
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}

		// 查询记录详情
		/*Article entity = new Article();
		entity.setTitle(title);*/
		//List<Article> articles = articleService.selectBy(entity);
		List<WebArticleDTO> articleDTOs = new ArrayList<WebArticleDTO>();
		
		List<Article> articles=articleService.getArticleByTitle(title);
		for (Article article : articles)
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

		result.put("list", articleDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 查询问卷列表(根据文章标题精确查询)
	 * 
	 * @param request
	 * @param modelMap
	 * @param objId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getTestingByTitle.json")
	@ResponseBody
	public Object getTestingByTitle(HttpServletRequest request, ModelMap modelMap, String title)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (title == null || title.equals(""))
		{
			result.setCode(-1);
			result.setMsg("测试名不能为空");
			return result;
		}

		// 查询记录详情
		List<Testing> testingList = testingService.getTestingByTitle(title);
		List<TestingDTO> tDTOs=new ArrayList<TestingDTO>();
		for (Testing t : testingList)
		{
			TestingDTO tDTO=new TestingDTO();
			BeanUtils.copyProperties(t, tDTO);
			// 转换实际文件路径
			
			tDTO.setFilePath(t.getThumbnail());
			tDTO.setFilePathMobile(t.getThumbnailMobile());
			tDTO.setFilePathSlide(t.getThumbnailSlide());
			
			//设置测试类别
			tDTO.setTestingType(testingTypeService.getTestingTypeByTypeId(t.getTypeId()));
			tDTOs.add(tDTO);
			
		}

		result.put("list", tDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
