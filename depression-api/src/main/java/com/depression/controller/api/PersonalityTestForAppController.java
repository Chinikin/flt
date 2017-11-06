package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.depression.entity.ErrorCode;
import com.depression.entity.QuestionResult;
import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.MemberWechat;
import com.depression.model.PersonalityCharactersSimilarity;
import com.depression.model.PersonalityHarmonyDescription;
import com.depression.model.PersonalityTestMemberMapping;
import com.depression.model.PersonalityTestShareGroup;
import com.depression.model.PersonalityTestStatistics;
import com.depression.model.web.dto.WebPersonalityCharactersSimilarityDTO;
import com.depression.model.web.dto.WebPersonalityTestMemberMappingDTO;
import com.depression.service.MemberService;
import com.depression.service.MemberWechatService;
import com.depression.service.PersonalityCharactersSimilarityService;
import com.depression.service.PersonalityHarmonyDescriptionService;
import com.depression.service.PersonalityTestMemberMappingService;
import com.depression.service.PersonalityTestResultDescService;
import com.depression.service.PersonalityTestShareGroupService;
import com.depression.service.PersonalityTestStatisticsService;
import com.depression.utils.DiscUtil;
import com.depression.utils.PropertyUtils;

/**
 * 人格测试(APP上使用)
 * 
 * @author ax
 * 
 */
@Controller
@RequestMapping("/personalityTest/app")
public class PersonalityTestForAppController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private PersonalityCharactersSimilarityService personalityCharactersSimilarityService;

	@Autowired
	private PersonalityTestShareGroupService personalityTestShareGroupService;

	@Autowired
	private PersonalityTestMemberMappingService personalityTestMemberMappingService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private PersonalityTestResultDescService personalityTestResultDescService;

	@Autowired
	private PersonalityTestStatisticsService personalityTestStatisticsService;

	@Autowired
	private PersonalityHarmonyDescriptionService personalityHarmonyDescriptionService;
	@Autowired
	MemberWechatService memberWechatService;
	/**
	 * 查询相似度排行
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSimilarList.json")
	@ResponseBody
	public Object getSimilarList(HttpSession session, HttpServletRequest request, @RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "shareId", required = false) Long shareId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (openid == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 获取微信用户详细信息
		Member queryMember = new Member();
		queryMember.setOpenid(openid);
		Member curMem = memberService.getMember(queryMember);

		// 生成自己的分享分组
		PersonalityTestShareGroup queryGroup = new PersonalityTestShareGroup();
		queryGroup.setMid(Long.parseLong(curMem.getMid().toString()));
		List<PersonalityTestShareGroup> existGroupList = personalityTestShareGroupService.selectSelective(queryGroup);
		Long gid = 0L;
		if (existGroupList == null || existGroupList.size() == 0)
		{
			PersonalityTestShareGroup group = new PersonalityTestShareGroup();
			group.setMid(Long.parseLong(curMem.getMid().toString()));
			personalityTestShareGroupService.insertSelective(group);
			gid = group.getgId();
		} else
		{
			gid = existGroupList.get(0).getgId();
		}
		log.info("生成自己的分享分组成功！");

		if (shareId != null)
		{

			// 获取分享人的会员id
			PersonalityTestShareGroup group = personalityTestShareGroupService.selectByPrimaryKey(shareId);
			Long midOther = 0L;
			if (group != null)
			{
				midOther = group.getMid();
			}
			log.info("获取分享人的会员id成功！分享人id：" + midOther);

			// 排除用户自己点击自己分享的链接
			if (curMem.getMid().longValue() != midOther.longValue())
			{
				// 将自己加入分享人的分组中
				log.info("shareId : " + shareId);
				PersonalityCharactersSimilarity querySimilaritySelf = new PersonalityCharactersSimilarity();
				querySimilaritySelf.setgId(Long.parseLong(shareId.toString()));
				querySimilaritySelf.setMid(Long.parseLong(curMem.getMid().toString()));
				List<PersonalityCharactersSimilarity> existSimList = personalityCharactersSimilarityService.selectSelective(querySimilaritySelf);
				if (existSimList == null || existSimList.size() == 0)
				{
					PersonalityCharactersSimilarity similaritySelf = new PersonalityCharactersSimilarity();
					similaritySelf.setgId(Long.parseLong(shareId.toString()));
					similaritySelf.setMid(Long.parseLong(curMem.getMid().toString()));
					personalityCharactersSimilarityService.insertSelective(similaritySelf);
				}
				log.info("将自己加入分享人的分组中成功！");

				// 将分享人加入自己分组中
				querySimilaritySelf = new PersonalityCharactersSimilarity();
				querySimilaritySelf.setgId(gid);
				querySimilaritySelf.setMid(midOther);
				existSimList = personalityCharactersSimilarityService.selectSelective(querySimilaritySelf);
				if (existSimList == null || existSimList.size() == 0)
				{
					PersonalityCharactersSimilarity similarityOther = new PersonalityCharactersSimilarity();
					log.info("自己的分组id：" + gid);
					similarityOther.setgId(gid);
					similarityOther.setMid(midOther);
					personalityCharactersSimilarityService.insertSelective(similarityOther);
				}
				log.info("将分享人加入自己分组中成功！");

				// 检查该用户是否已经做过测试
				if (curMem != null)
				{
					PersonalityTestMemberMapping record = new PersonalityTestMemberMapping();
					record.setMid(Long.parseLong(curMem.getMid().toString()));
					List<PersonalityTestMemberMapping> mappingList = personalityTestMemberMappingService.selectSelective(record);

					// 用户已经做过测试，更新特征数据
					if (mappingList != null && mappingList.size() > 0)
					{
						log.info("更新特征数据：自己id-" + curMem.getMid() + ",分享人id-" + midOther);
						personalityTestShareGroupService.insertSimilarityAndHarmony(curMem.getMid(), midOther);
					}
				}
			}

		}

		if (curMem != null)
		{
			// 查询组信息
			PersonalityTestShareGroup queryGroup2 = new PersonalityTestShareGroup();
			queryGroup2.setMid(Long.parseLong(curMem.getMid().toString()));
			List<PersonalityTestShareGroup> existGroupList2 = personalityTestShareGroupService.selectSelective(queryGroup2);
			if (existGroupList2 == null || existGroupList2.size() == 0)
			{
				result.setCode(ErrorCode.ERROR_NOT_SHARE.getCode());
				result.setError(ErrorCode.ERROR_NOT_SHARE.getMessage());
				log.error(ErrorCode.ERROR_NOT_SHARE.getMessage());
				return result;
			}

			// 查询相似度排行
			PersonalityCharactersSimilarity querySimilarity = new PersonalityCharactersSimilarity();
			querySimilarity.setgId(existGroupList2.get(0).getgId());
			List<WebPersonalityCharactersSimilarityDTO> simList = personalityCharactersSimilarityService.selectSelectiveOrderBySimDesc(querySimilarity);
			result.setCode(ErrorCode.SUCCESS.getCode());
			result.setMsg(ErrorCode.SUCCESS.getMessage());
			result.put("list", simList);
		}

		return result;
	}

	/**
	 * 查询相似度详情
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSimilarDetail.json")
	@ResponseBody
	public Object getSimilarDetail(HttpSession session, HttpServletRequest request, @RequestParam(value = "pcsId", required = false) Long pcsId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (pcsId == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 根据主键id查找相似度详情
		PersonalityCharactersSimilarity personalityCharactersSimilarity = personalityCharactersSimilarityService.selectByPrimaryKey(pcsId);
		if (personalityCharactersSimilarity != null)
		{

			// 设置相似度信息
			WebPersonalityCharactersSimilarityDTO similarityDTO = new WebPersonalityCharactersSimilarityDTO();
			BeanUtils.copyProperties(personalityCharactersSimilarity, similarityDTO);

			PersonalityHarmonyDescription personalityHarmonyDescription = personalityHarmonyDescriptionService.selectByPrimaryKey(personalityCharactersSimilarity.getHarmonyId());
			BeanUtils.copyProperties(personalityHarmonyDescription, similarityDTO);

			// 设置微信用户详情			
			MemberWechat memberWechat = memberWechatService.obtainWechatByMid(personalityCharactersSimilarity.getMid());
			if (memberWechat != null)
			{
				BeanUtils.copyProperties(memberWechat, similarityDTO);
				similarityDTO.setSex(String.valueOf(memberWechat.getSex()));
				similarityDTO.setOpenid(memberWechat.getPublicOpenid());
			}

			// 设置对方的测试结果
			PersonalityTestMemberMapping queryMapping = new PersonalityTestMemberMapping();
			queryMapping.setMid(personalityCharactersSimilarity.getMid());
			List<WebPersonalityTestMemberMappingDTO> detailList = personalityTestMemberMappingService.selectTestDetail(queryMapping);
			if (detailList != null)
			{
				if (detailList.size() > 0)
				{
					WebPersonalityTestMemberMappingDTO mappingDTO = detailList.get(0);
					similarityDTO.setOtherSideTestType(mappingDTO.getType());
					similarityDTO.setOtherSideTestDesc(mappingDTO.getDescp());
				}
			}

			// 设置自己的测试结果
			Long gid = personalityCharactersSimilarity.getgId();
			PersonalityTestShareGroup shearGroup = personalityTestShareGroupService.selectByPrimaryKey(gid);
			if (shearGroup != null)
			{
				Long selfMid = shearGroup.getMid();
				PersonalityTestMemberMapping selfQueryMapping = new PersonalityTestMemberMapping();
				selfQueryMapping.setMid(selfMid);
				List<WebPersonalityTestMemberMappingDTO> selfDetailList = personalityTestMemberMappingService.selectTestDetail(selfQueryMapping);
				if (selfDetailList != null)
				{
					if (selfDetailList.size() > 0)
					{
						WebPersonalityTestMemberMappingDTO mappingDTO = selfDetailList.get(0);
						similarityDTO.setSelfTestType(mappingDTO.getType());
						similarityDTO.setSelfTestDesc(mappingDTO.getDescp());
					}
				}
			}

			result.put("obj", similarityDTO);
		}

		return result;
	}

	/**
	 * 查询测试详情
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectTestDetail.json")
	@ResponseBody
	public Object selectTestDetail(HttpSession session, HttpServletRequest request, @RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "shareId", required = false) Long shareId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (openid == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 获取微信用户详细信息
		Member queryMember = new Member();
		queryMember.setOpenid(openid);
		Member curMem = memberService.getMember(queryMember);

		if (curMem != null)
		{
			// 查询测试详情
			PersonalityTestMemberMapping queryMapping = new PersonalityTestMemberMapping();
			queryMapping.setMid(Long.parseLong(curMem.getMid().toString()));
			List<WebPersonalityTestMemberMappingDTO> detailList = personalityTestMemberMappingService.selectTestDetail(queryMapping);
			if (detailList != null)
			{
				if (detailList.size() > 0)
				{
					PersonalityTestStatistics queryPersonalityTestStatistics = new PersonalityTestStatistics();
					queryPersonalityTestStatistics.setMid(curMem.getMid());
					List<PersonalityTestStatistics> statisticsList = personalityTestStatisticsService.selectSelective(queryPersonalityTestStatistics);
					if (statisticsList != null && statisticsList.size() > 0)
					{
						PersonalityTestStatistics personalityTestStatistics = statisticsList.get(0);
						if (personalityTestStatistics != null)
						{
							WebPersonalityTestMemberMappingDTO mappingDTO = detailList.get(0);
							BeanUtils.copyProperties(personalityTestStatistics, mappingDTO);
							result.put("obj", mappingDTO);
						}

					}
				} else
				{
					result.put("obj", null);
				}
			}

		}

		// 生成自己的分享分组
		PersonalityTestShareGroup queryGroup = new PersonalityTestShareGroup();
		queryGroup.setMid(Long.parseLong(curMem.getMid().toString()));
		List<PersonalityTestShareGroup> existGroupList = personalityTestShareGroupService.selectSelective(queryGroup);
		Long gid = 0L;
		if (existGroupList == null || existGroupList.size() == 0)
		{
			PersonalityTestShareGroup group = new PersonalityTestShareGroup();
			group.setMid(Long.parseLong(curMem.getMid().toString()));
			personalityTestShareGroupService.insertSelective(group);
			gid = group.getgId();
		} else
		{
			gid = existGroupList.get(0).getgId();
		}
		log.info("生成自己的分享分组成功！");

		if (shareId != null)
		{

			// 获取分享人的会员id
			PersonalityTestShareGroup group = personalityTestShareGroupService.selectByPrimaryKey(shareId);
			Long midOther = 0L;
			if (group != null)
			{
				midOther = group.getMid();
			}
			log.info("获取分享人的会员id成功！分享人id：" + midOther);

			// 排除用户自己点击自己分享的链接
			if (curMem.getMid().longValue() != midOther.longValue())
			{
				// 将自己加入分享人的分组中
				log.info("shareId : " + shareId);
				PersonalityCharactersSimilarity querySimilaritySelf = new PersonalityCharactersSimilarity();
				querySimilaritySelf.setgId(Long.parseLong(shareId.toString()));
				querySimilaritySelf.setMid(Long.parseLong(curMem.getMid().toString()));
				List<PersonalityCharactersSimilarity> existSimList = personalityCharactersSimilarityService.selectSelective(querySimilaritySelf);
				if (existSimList == null || existSimList.size() == 0)
				{
					PersonalityCharactersSimilarity similaritySelf = new PersonalityCharactersSimilarity();
					similaritySelf.setgId(Long.parseLong(shareId.toString()));
					similaritySelf.setMid(Long.parseLong(curMem.getMid().toString()));
					personalityCharactersSimilarityService.insertSelective(similaritySelf);
				}
				log.info("将自己加入分享人的分组中成功！");

				// 将分享人加入自己分组中
				querySimilaritySelf = new PersonalityCharactersSimilarity();
				querySimilaritySelf.setgId(gid);
				querySimilaritySelf.setMid(midOther);
				existSimList = personalityCharactersSimilarityService.selectSelective(querySimilaritySelf);
				if (existSimList == null || existSimList.size() == 0)
				{
					PersonalityCharactersSimilarity similarityOther = new PersonalityCharactersSimilarity();
					log.info("自己的分组id：" + gid);
					similarityOther.setgId(gid);
					similarityOther.setMid(midOther);
					personalityCharactersSimilarityService.insertSelective(similarityOther);
				}
				log.info("将分享人加入自己分组中成功！");

				// 检查该用户是否已经做过测试
				if (curMem != null)
				{
					PersonalityTestMemberMapping record = new PersonalityTestMemberMapping();
					record.setMid(Long.parseLong(curMem.getMid().toString()));
					List<PersonalityTestMemberMapping> mappingList = personalityTestMemberMappingService.selectSelective(record);

					// 用户已经做过测试，更新特征数据
					if (mappingList != null && mappingList.size() > 0)
					{
						log.info("更新特征数据：自己id-" + curMem.getMid() + ",分享人id-" + midOther);
						personalityTestShareGroupService.insertSimilarityAndHarmony(curMem.getMid(), midOther);
					}
				}
			}

		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	@RequestMapping(value = "/saveDiscResult.json")
	@ResponseBody
	public Object saveDiscResult(HttpSession session, HttpServletRequest request, String resultJson, @RequestParam(value = "openid", required = false) String openid)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(resultJson, openid))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		Integer[] disc = null;
		try
		{
			List<QuestionResult> questionResults = JSON.parseObject(resultJson, new TypeReference<ArrayList<QuestionResult>>()
			{
			});
			disc = DiscUtil.countDisc(questionResults);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_PARAM_JSON.getMessage(), e);
			result.setCode(ErrorCode.ERROR_PARAM_JSON.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_JSON.getMessage());
			return result;
		}

		// 获取微信用户详细信息
		Member queryMember = new Member();
		queryMember.setOpenid(openid);
		Member curMem = memberService.getMember(queryMember);

		if (curMem != null)
		{
			try
			{
				personalityTestShareGroupService.saveSimilarityAndHarmony((Long) curMem.getMid(), disc);
				personalityTestShareGroupService.saveCharacterAndDisc((Long) curMem.getMid(), disc);
			} catch (Exception e)
			{
				log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
				result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
				return result;
			}
		} else
		{
			log.error(ErrorCode.ERROR_WECHAT_NOT_LOGIN.getMessage());
			result.setCode(ErrorCode.ERROR_WECHAT_NOT_LOGIN.getCode());
			result.setMsg(ErrorCode.ERROR_WECHAT_NOT_LOGIN.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
