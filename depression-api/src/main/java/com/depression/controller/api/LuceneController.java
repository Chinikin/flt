package com.depression.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.AdvisoryTag;
import com.depression.model.Article;
import com.depression.model.ArticleCategory;
import com.depression.model.ArticleDetail;
import com.depression.model.ArticleType;
import com.depression.model.EapEnterprise;
import com.depression.model.LuceneIdFlag;
import com.depression.model.Member;
import com.depression.model.MemberAdvisory;
import com.depression.model.MemberAdvisoryDetail;
import com.depression.model.Testing;
import com.depression.model.TestingComment;
import com.depression.model.TestingType;
import com.depression.model.api.dto.ApiAdvisoryTagDTO;
import com.depression.model.api.dto.ApiLuceneArticleDTO;
import com.depression.model.api.dto.ApiLuceneMemberAdvisoryDTO;
import com.depression.model.api.dto.ApiLucenePsychoDTO;
import com.depression.model.api.dto.ApiLuceneTestingDTO;
import com.depression.service.AdvisoryService;
import com.depression.service.AdvisoryTagService;
import com.depression.service.ArticleCategoryService;
import com.depression.service.ArticleDetailService;
import com.depression.service.ArticleService;
import com.depression.service.ArticleTypeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.EapService;
import com.depression.service.LuceneService;
import com.depression.service.MemberService;
import com.depression.service.PsychoGroupService;
import com.depression.service.TestingCommentService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingService;
import com.depression.service.TestingTypeService;
import com.depression.service.UbSearchWordsService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/Lucene")
public class LuceneController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	LuceneService luceneService;
	@Autowired
	MemberService memberService;
	@Autowired
	PsychoController psychoController;
	@Autowired
	ArticleService articleService;
	@Autowired
	ArticleCategoryService articleCategoryService;
	@Autowired
	ArticleTypeService articleTypeService;
	@Autowired	
	ArticleDetailService articleDetailService;
	@Autowired
	TestingService testingService;
	@Autowired
	TestingTypeService testingTypeService;
	@Autowired
	TestingQuestionsService testingQuestionsService;
	@Autowired
	TestingCommentService testingCommentService;
	@Autowired
	AdvisoryService advisoryService;
	@Autowired	
	AdvisoryTagService advisoryTagService;
	@Autowired
	UbSearchWordsService ubSearchWordsService;
	@Autowired
	EapEnterpriseService eapEnterpriseService;
	@Autowired
	EapService eapService;
	@Autowired
	PsychoGroupService psychoGroupService;
	
	
	private Integer maxSearchNum = 100;
	private Integer mixedListNum = 3;
	

	
	@RequestMapping(value = "/searchPyschos.json")
	@ResponseBody
	public Object searchPyschos(HttpServletRequest request,String words,Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(words,
				pageIndex,
				pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		System.out.println(request.getRemoteAddr());
		List<LuceneIdFlag> idFlags = luceneService.searchPsychos(words, maxSearchNum);
		List<Long> ids = new ArrayList<Long>();
		Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
		for(LuceneIdFlag lif : idFlags)
		{
			ids.add(lif.getId());
			idFlagMap.put(lif.getId(), lif.getFlag());
		}
		
		List<Member> members;
		Integer count;
		try
		{
			//保存用户搜索记录
			if(mid != null){
			ubSearchWordsService.insertUbSearchWords(mid, words);
			}
			
			members = memberService.getByKeysWithPageEnabled(ids, pageIndex, pageSize, null);
			count = memberService.getCountByPrimaryKeysEnabled(ids, null);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		List<ApiLucenePsychoDTO> psychoDTOs = new ArrayList<ApiLucenePsychoDTO>();
		for (Member m : members)
		{
			// 拷贝属性
			ApiLucenePsychoDTO psychoDTO = new ApiLucenePsychoDTO();
			psychoController.member2PsychoDTO(m, psychoDTO, null);

			psychoDTO.setLuceneFlag(idFlagMap.get(m.getMid()));
			psychoDTOs.add(psychoDTO);
		}
		
//		result.put("ids", ids);
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	@RequestMapping(value = "/V1/searchPyschos.json")
	@ResponseBody
	public Object searchPyschosV1(String words,Long eeId,Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(words,
				pageIndex,
				pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<LuceneIdFlag> idFlags = luceneService.searchPsychos(words, maxSearchNum);
		
		List<Long> ids = new ArrayList<Long>();
		Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
		for(LuceneIdFlag lif : idFlags)
		{
			ids.add(lif.getId());
			idFlagMap.put(lif.getId(), lif.getFlag());
		}
		
		List<Member> members;
		Integer count;
		try
		{
			//保存用户搜索记录
			if(mid != null){
			ubSearchWordsService.insertUbSearchWords(mid, words);
			}
			
			//获取eap全部专家
			EapEnterprise ee = eapEnterpriseService.selectByPrimaryKey(eeId);
			List<Long> ids1 = psychoGroupService.getPsychoIdOfGroup(ee.getPgId());
			
			//过滤搜索专家
			ids.retainAll(ids1);
			
			members = memberService.getByKeysWithPageEnabled(ids, pageIndex, pageSize, null);
			count = memberService.getCountByPrimaryKeysEnabled(ids, null);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		List<ApiLucenePsychoDTO> psychoDTOs = new ArrayList<ApiLucenePsychoDTO>();
		for (Member m : members)
		{
			// 拷贝属性
			ApiLucenePsychoDTO psychoDTO = new ApiLucenePsychoDTO();
			psychoController.member2PsychoDTO(m, psychoDTO, null);

			psychoDTO.setLuceneFlag(idFlagMap.get(m.getMid()));
			psychoDTOs.add(psychoDTO);
		}
		
//		result.put("ids", ids);
		result.put("psychoDTOs", psychoDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	
	private void articleAssemble(Article article, ApiLuceneArticleDTO articleDTO)
	{
		BeanUtils.copyProperties(article, articleDTO);
		
		ArticleCategory articleCategory = new ArticleCategory();
		articleCategory.setCategoryId(article.getCategoryId());
		List<ArticleCategory> articleCategorys = articleCategoryService.selectBy(articleCategory);
		if (articleCategorys.size() > 0)
		{
			articleCategory = articleCategorys.get(0);
			articleDTO.setCategoryName(articleCategory.getCategoryName());
		}
		ArticleType articleType = new ArticleType();
		articleType.setTypeId(article.getTypeId());
		List<ArticleType> artT = articleTypeService.selectBy(articleType);
		if (artT.size() > 0)
		{
			articleType = artT.get(0);
			articleDTO.setTypeName(articleType.getTypeName());
		}
		if (article.getThumbnail() != null && !article.getThumbnail().equals(""))
		{
			articleDTO.setFilePath(article.getThumbnail());
		}
		else 
		{
			articleDTO.setFilePath("");
		}
		
		ArticleDetail ad = new ArticleDetail();
        ad.setArticleId(article.getArticleId());
        List<ArticleDetail> ads = articleDetailService.selectBy(ad);
        if(ads.size() > 0)
        {
        	articleDTO.setDetail(ads.get(0).getContent());
        }
	}

	@RequestMapping(value = "/searchArticles.json")
	@ResponseBody
	public Object searchArticles(String words, Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(words,
				pageIndex,
				pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<LuceneIdFlag> idFlags = luceneService.searchArticles(words, maxSearchNum);
		List<Long> ids = new ArrayList<Long>();
		Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
		for(LuceneIdFlag lif : idFlags)
		{
			ids.add(lif.getId());
			idFlagMap.put(lif.getId(), lif.getFlag());
		}
		
		List<Article> articles;
		Integer count;
		try
		{
			//保存用户搜索记录
			if(mid != null){
			ubSearchWordsService.insertUbSearchWords(mid, words);
			}
			
			articles = articleService.getByKeysWithPageEnabled(ids, pageIndex, pageSize);
			count = articleService.getCountByPrimaryKeysEnabled(ids);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<ApiLuceneArticleDTO> articleDTOs =  new ArrayList<ApiLuceneArticleDTO>();
	
		for (Article article : articles)
		{
			ApiLuceneArticleDTO articleDTO = new ApiLuceneArticleDTO();
			articleAssemble(article, articleDTO);
			
			articleDTO.setLuceneFlag(idFlagMap.get(article.getArticleId()));
			articleDTOs.add(articleDTO);
		}
		
//		result.put("ids", ids);
		result.put("articleDTOs", articleDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	private void testingAssemble(Testing t, ApiLuceneTestingDTO testingDTO)
	{
		BeanUtils.copyProperties(t, testingDTO);
		
		// 查询试题数量
		Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(t.getTestingId());
		testingDTO.setQuestionAmount(questionAmount.intValue());

		// 查询评论数量
		TestingComment testingComment = new TestingComment();
		testingComment.setTestingId(t.getTestingId());
		Integer commentAmount = testingCommentService.getPageCounts(testingComment);
		testingDTO.setTestingCommentPeopleNum(commentAmount.intValue());

		// 转换实际文件路径
		
		testingDTO.setFilePath(t.getThumbnail());
		testingDTO.setFilePathMobile(t.getThumbnailMobile());
		testingDTO.setFilePathSlide(t.getThumbnailSlide());
		

		if (t.getTestingPeopleNum() == null)
		{
			testingDTO.setTestingPeopleNum(0);
		}
		
		TestingType testingType = testingTypeService.getTestingTypeByTypeId(t.getTypeId());
		testingDTO.setTsType(testingType.getTsType());

	}
	
	@RequestMapping(value = "/searchTestings.json")
	@ResponseBody
	public Object searchTestings(String words, Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(words,
				pageIndex,
				pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<LuceneIdFlag> idFlags = luceneService.searchTestings(words, maxSearchNum);
		List<Long> ids = new ArrayList<Long>();
		Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
		for(LuceneIdFlag lif : idFlags)
		{
			ids.add(lif.getId());
			idFlagMap.put(lif.getId(), lif.getFlag());
		}
		
		List<Testing> testings;
		Integer count;
		try
		{
			//保存用户搜索记录
			if(mid != null){
			ubSearchWordsService.insertUbSearchWords(mid, words);
			}
			
			testings = testingService.getByKeysWithPageEnabled(ids, pageIndex, pageSize);
			count = testingService.getCountByPrimaryKeysEnabled(ids);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		List<ApiLuceneTestingDTO> testingDTOs = new ArrayList<ApiLuceneTestingDTO>();
		for (Testing t : testings)
		{
			// 拷贝属性
			ApiLuceneTestingDTO testingDTO = new ApiLuceneTestingDTO();
			testingAssemble(t, testingDTO);

			testingDTO.setLuceneFlag(idFlagMap.get(t.getTestingId().longValue()));
			testingDTOs.add(testingDTO);
		}
		
//		result.put("ids", ids);
		result.put("testingDTOs", testingDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	private void advisoryAssemble(MemberAdvisory a, ApiLuceneMemberAdvisoryDTO advisoryDTO)
	{
		BeanUtils.copyProperties(a, advisoryDTO);
		
		// 获取评论条数
		long commentCount = advisoryService.countComments(a.getAdvisoryId());
		advisoryDTO.setCommentCount(commentCount);
		
		// 获取详情
		MemberAdvisoryDetail mad = advisoryService.obtainDetailByAdvisoryId(a.getAdvisoryId());
	    advisoryDTO.setDetail(mad.getDetail());
		
		// 返回参数
		Member member = new Member();
		member.setMid(a.getMid());
		member = memberService.selectMemberByMid(a.getMid());
		if(member != null)
		{
			advisoryDTO.setNickname(member.getNickname());
			advisoryDTO.setAvatar(member.getAvatar());
			String avatarThumbnail = member.getAvatarThumbnail();
			if (!StringUtils.isEmpty(avatarThumbnail))
			{
				advisoryDTO.setAvatarThumbnail(avatarThumbnail);
			} else
			{
				advisoryDTO.setAvatarThumbnail("");
			}
	
			if(a.getIsAnony() == 0)
			{//咨询匿名
				Integer nickFinal = (member.getMid().intValue() + 1234) % 10000;
				String city = member.getLocation();
				//地址做处理, 去掉市
				if(city == null )
				{
					city = "";
				}
				else if(city.endsWith("市"))
				{
					city = city.substring(0, city.length() -1);
				}

				advisoryDTO.setNickname(city + "匿名用户" + nickFinal);
				advisoryDTO.setAvatar(null);
				advisoryDTO.setAvatarThumbnail(null);
			}
		}else
		{
			advisoryDTO.setNickname("匿名用户");
			advisoryDTO.setAvatar(null);
		}
		//查找tags
		List<AdvisoryTag> tags = advisoryTagService.selectAdvisoryTagByAdvisoryId(a.getAdvisoryId());
		List<ApiAdvisoryTagDTO> advisoryTagDTOs = new ArrayList<ApiAdvisoryTagDTO>();
		for(AdvisoryTag tag : tags)
		{
			ApiAdvisoryTagDTO tagDTO = new ApiAdvisoryTagDTO();
			BeanUtils.copyProperties(tag, tagDTO);
			advisoryTagDTOs.add(tagDTO);
		}
		advisoryDTO.setTags(advisoryTagDTOs);
	}
	
	@RequestMapping(value = "/searchAdvisories.json")
	@ResponseBody
	public Object searchAdvisories(String words, Long mid, Integer pageIndex, Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(words,
				pageIndex,
				pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<LuceneIdFlag> idFlags = luceneService.searchAdvisories(words, maxSearchNum);
		List<Long> ids = new ArrayList<Long>();
		Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
		for(LuceneIdFlag lif : idFlags)
		{
			ids.add(lif.getId());
			idFlagMap.put(lif.getId(), lif.getFlag());
		}
		
		List<MemberAdvisory> advisories;
		Integer count;
		try
		{
			//保存用户搜索记录
			if(mid != null){
			ubSearchWordsService.insertUbSearchWords(mid, words);
			}
			
			advisories = advisoryService.obtainAdvisory8Keys3PageEn(ids, pageIndex, pageSize);
			count = advisoryService.countAdvisory8KeysEn(ids);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		List<ApiLuceneMemberAdvisoryDTO> advisoryDTOs =  new ArrayList<ApiLuceneMemberAdvisoryDTO>();
	
		for (MemberAdvisory a : advisories)
		{
			ApiLuceneMemberAdvisoryDTO advisoryDTO = new ApiLuceneMemberAdvisoryDTO();
			advisoryAssemble(a, advisoryDTO);
			
			advisoryDTO.setLuceneFlag(idFlagMap.get(a.getAdvisoryId()));
			advisoryDTOs.add(advisoryDTO);
		}
		
//		result.put("ids", ids);
		result.put("advisoryDTOs", advisoryDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(value = "/searchAll.json")
	@ResponseBody
	public Object searchAll(String words,Long mid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(words))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		{
			List<LuceneIdFlag> idFlags = luceneService.searchPsychos(words, maxSearchNum);
			List<Long> ids = new ArrayList<Long>();
			Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
			for(LuceneIdFlag lif : idFlags)
			{
				ids.add(lif.getId());
				idFlagMap.put(lif.getId(), lif.getFlag());
			}
			
			List<Member> members;
			Integer count;
			try
			{
				//保存用户搜索记录
				if(mid != null){
				ubSearchWordsService.insertUbSearchWords(mid, words);
				}
				
				members = memberService.getByKeysWithPageEnabled(ids, 1, mixedListNum, null);
				count = memberService.getCountByPrimaryKeysEnabled(ids, null);
			} catch (Exception e)
			{
				log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
	
			List<ApiLucenePsychoDTO> psychoDTOs = new ArrayList<ApiLucenePsychoDTO>();
			for (Member m : members)
			{
				// 拷贝属性
				ApiLucenePsychoDTO psychoDTO = new ApiLucenePsychoDTO();
				psychoController.member2PsychoDTO(m, psychoDTO, null);
	
				psychoDTO.setLuceneFlag(idFlagMap.get(m.getMid()));
				psychoDTOs.add(psychoDTO);
			}
			
	//		result.put("ids", ids);
			result.put("psychoDTOs", psychoDTOs);
			result.put("psychoCount", count);
		}
		{
			List<LuceneIdFlag> idFlags = luceneService.searchArticles(words, maxSearchNum);
			List<Long> ids = new ArrayList<Long>();
			Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
			for(LuceneIdFlag lif : idFlags)
			{
				ids.add(lif.getId());
				idFlagMap.put(lif.getId(), lif.getFlag());
			}
			
			List<Article> articles;
			Integer count;
			try
			{
				articles = articleService.getByKeysWithPageEnabled(ids, 1, mixedListNum);
				count = articleService.getCountByPrimaryKeysEnabled(ids);
			} catch (Exception e)
			{
				log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			
			List<ApiLuceneArticleDTO> articleDTOs =  new ArrayList<ApiLuceneArticleDTO>();
		
			for (Article article : articles)
			{
				ApiLuceneArticleDTO articleDTO = new ApiLuceneArticleDTO();
				articleAssemble(article, articleDTO);
				
				articleDTO.setLuceneFlag(idFlagMap.get(article.getArticleId()));
				articleDTOs.add(articleDTO);
			}
			
//				result.put("ids", ids);
			result.put("articleDTOs", articleDTOs);
			result.put("articleCount", count);
		}
		{
			List<LuceneIdFlag> idFlags = luceneService.searchTestings(words, maxSearchNum);
			List<Long> ids = new ArrayList<Long>();
			Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
			for(LuceneIdFlag lif : idFlags)
			{
				ids.add(lif.getId());
				idFlagMap.put(lif.getId(), lif.getFlag());
			}
			
			List<Testing> testings;
			Integer count;
			try
			{
				testings = testingService.getByKeysWithPageEnabled(ids, 1, mixedListNum);
				count = testingService.getCountByPrimaryKeysEnabled(ids);
			} catch (Exception e)
			{
				log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}

			List<ApiLuceneTestingDTO> testingDTOs = new ArrayList<ApiLuceneTestingDTO>();
			for (Testing t : testings)
			{
				// 拷贝属性
				ApiLuceneTestingDTO testingDTO = new ApiLuceneTestingDTO();
				testingAssemble(t, testingDTO);

				testingDTO.setLuceneFlag(idFlagMap.get(t.getTestingId().longValue()));
				testingDTOs.add(testingDTO);
			}
			
//				result.put("ids", ids);
			result.put("testingDTOs", testingDTOs);
			result.put("testingCount", count);
		}
		{
			List<LuceneIdFlag> idFlags = luceneService.searchAdvisories(words, maxSearchNum);
			List<Long> ids = new ArrayList<Long>();
			Map<Long, Integer> idFlagMap = new HashMap<Long, Integer>(); 
			for(LuceneIdFlag lif : idFlags)
			{
				ids.add(lif.getId());
				idFlagMap.put(lif.getId(), lif.getFlag());
			}
			
			List<MemberAdvisory> advisories;
			Integer count;
			try
			{
				advisories = advisoryService.obtainAdvisory8Keys3PageEn(ids, 1, mixedListNum);
				count = advisoryService.countAdvisory8KeysEn(ids);
			} catch (Exception e)
			{
				log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			
			List<ApiLuceneMemberAdvisoryDTO> advisoryDTOs =  new ArrayList<ApiLuceneMemberAdvisoryDTO>();
		
			for (MemberAdvisory a : advisories)
			{
				ApiLuceneMemberAdvisoryDTO advisoryDTO = new ApiLuceneMemberAdvisoryDTO();
				advisoryAssemble(a, advisoryDTO);
				
				advisoryDTO.setLuceneFlag(idFlagMap.get(a.getAdvisoryId()));
				advisoryDTOs.add(advisoryDTO);
			}
			
//				result.put("ids", ids);
			result.put("advisoryDTOs", advisoryDTOs);
			result.put("advisoryCount", count);
		}
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}	
	
	
	
}
