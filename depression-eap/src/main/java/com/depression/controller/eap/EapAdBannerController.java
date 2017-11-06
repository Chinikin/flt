package com.depression.controller.eap;

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

import com.alibaba.fastjson.JSON;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdBanner;
import com.depression.model.Article;
import com.depression.model.ArticleCategory;
import com.depression.model.ArticleType;
import com.depression.model.Member;
import com.depression.model.Testing;
import com.depression.model.eap.dto.EapArticleDTO;
import com.depression.model.eap.dto.EapPsychoDTO;
import com.depression.model.eap.dto.EapTestingDTO;
import com.depression.model.web.dto.AdBannerDTO;
import com.depression.service.AdBannerService;
import com.depression.service.ArticleCategoryService;
import com.depression.service.ArticleCustomService;
import com.depression.service.ArticleService;
import com.depression.service.ArticleTypeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberService;
import com.depression.service.Permission;
import com.depression.service.TestingService;
import com.depression.service.TestingTypeService;
import com.depression.utils.PropertyUtils;
import com.github.pagehelper.Page;

/**
 * banner管理
 * 
 * @author hs
 * 
 */
@Controller
@RequestMapping("/EapAdBanner")
public class EapAdBannerController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AdBannerService adBannerService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private TestingService testingService;
	@Autowired
	private ArticleTypeService articleTypeService;
	@Autowired
	private ArticleCategoryService articleCategoryService;
	@Autowired
	private TestingTypeService testingTypeService;
	@Autowired
	private ArticleCustomService articleCustomService;
	@Autowired
	private EapEnterpriseService eapEnterpriseService;
	

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
	@Permission("8")
	public Object list(HttpServletRequest request, ModelMap modelMap, AdBanner banner)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (banner == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
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
		queryBanner.setPageIndex(banner.getPageIndex());
		queryBanner.setPageSize(banner.getPageSize());
		queryBanner.setShowLocation(banner.getShowLocation());
		queryBanner.setReleaseFrom(banner.getReleaseFrom());
		if (banner.getBannerTitle() != null && !banner.getBannerTitle().equals(""))
		{
			queryBanner.setBannerTitle(banner.getBannerTitle());
		}

		// 查询集合
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
		result.put("count", list.getTotal());

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
	@Permission("8")
	public Object save(HttpServletRequest request, ModelMap modelMap, AdBanner banner)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (banner == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 检查排序编号是否重复
		AdBanner queryAdBanner = new AdBanner();
		queryAdBanner.setSorting(banner.getSorting());
		queryAdBanner.setShowLocation(banner.getShowLocation());
		queryAdBanner.setReleaseFrom(banner.getReleaseFrom());
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
	@Permission("8")
	public Object update(HttpServletRequest request, ModelMap modelMap, AdBanner banner)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (banner == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (banner.getBannerId() == null || banner.getBannerId().equals(""))
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}
		/*if (banner.getBannerTitle() == null || banner.getBannerTitle().equals(""))
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}*/

		AdBanner bannerRtn = adBannerService.selectByPrimaryKey(banner.getBannerId());
		if (bannerRtn == null)
		{
			result.setCode(-1);
			result.setMsg("记录不存在");
			return result;
		}
		
		// 参数检查
		if (PropertyUtils.examineOneNull(banner.getReleaseFrom()))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		if(bannerRtn.getReleaseFrom() != banner.getReleaseFrom()){
			result.setCode(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getCode());
			result.setMsg(ErrorCode.ERROR_AUTHORITY_NOT_ALLOW.getMessage());
			return result;
		}
		
		// 检查排序编号是否重复
		AdBanner queryAdBanner = new AdBanner();
		queryAdBanner.setSorting(banner.getSorting());
		queryAdBanner.setShowLocation(banner.getShowLocation());
		queryAdBanner.setReleaseFrom(banner.getReleaseFrom());
		List<AdBanner> bannerList = adBannerService.selectSelective(queryAdBanner);
		if (bannerList.size() > 0 && !bannerList.get(0).getBannerId().equals(banner.getBannerId()))
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
	@Permission("8")
	public Object delete(HttpServletRequest request, ModelMap modelMap, String bannerIds,Long eeId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (PropertyUtils.examineOneNull(bannerIds))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		int count = 0;
		try{
			List<Long> ids = JSON.parseArray(bannerIds,Long.class);
			for(Long id : ids ){
				AdBanner banner = adBannerService.selectByPrimaryKey(id);
				if(eeId == banner.getReleaseFrom()){
					count+=adBannerService.deleteByPrimaryKey(id);
				}
			}
			
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
		
		result.put("count", count);
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
	@Permission("8")
	public Object view(HttpServletRequest request, ModelMap modelMap, Long bannerId,Long releaseFrom)
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
		
		if(adBanner.getReleaseFrom() == releaseFrom){
			BeanUtils.copyProperties(adBanner, adBannerDTO);
	
			// 转换实际文件路径
			if (adBanner.getPicPath() != null && !adBanner.getPicPath().equals(""))
			{
				adBannerDTO.setFilePath(adBanner.getPicPath());
			}
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
	@Permission("8")
	public Object getPsychoByName(HttpServletRequest request, ModelMap modelMap, String name,Long eeId)
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
		List<Member> members = eapEnterpriseService.obtainPsyByEeIdAndName(eeId,name);
		List<EapPsychoDTO> psychoDTOs = new ArrayList<EapPsychoDTO>();
		for (Member member : members)
		{
			EapPsychoDTO psychoDTO = new EapPsychoDTO();
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
	@Permission("8")
	public Object getArticleByTitle(HttpServletRequest request, ModelMap modelMap, String title,Long eeId)
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
		List<EapArticleDTO> articleDTOs = new ArrayList<EapArticleDTO>();
		
		List<Article> articles=articleCustomService.searchArticleByTitle(eeId, title);
		for (Article article : articles)
		{
			EapArticleDTO articleDTO = new EapArticleDTO();
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
	@Permission("8")
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
		List<EapTestingDTO> tDTOs=new ArrayList<EapTestingDTO>();
		for (Testing t : testingList)
		{
			EapTestingDTO tDTO=new EapTestingDTO();
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
