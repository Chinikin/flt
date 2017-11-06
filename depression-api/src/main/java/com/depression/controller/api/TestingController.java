package com.depression.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.tsp.TSTInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Testing;
import com.depression.model.TestingComment;
import com.depression.model.TestingOld;
import com.depression.model.TestingResultForJump;
import com.depression.model.TestingResultForJumpOld;
import com.depression.model.TestingResultForMember;
import com.depression.model.TestingScoreAmount;
import com.depression.model.TestingScoreAmountOld;
import com.depression.model.TestingSection;
import com.depression.model.TestingSectionOld;
import com.depression.model.TestingType;
import com.depression.model.api.dto.ApiTestingDTO;
import com.depression.model.api.dto.ApiTestingScoreAmountDTO;
import com.depression.model.web.dto.WebMemberTestingRecDTO;
import com.depression.service.TestingCommentService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingResultForJumpService;
import com.depression.service.TestingResultForMemberService;
import com.depression.service.TestingScoreAmountService;
import com.depression.service.TestingSectionService;
import com.depression.service.TestingService;
import com.depression.service.TestingTypeService;
import com.depression.service.UnreadCommentService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping(value = { "/testing", "/superClass" })
public class TestingController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingService testingService;

	@Autowired
	private TestingScoreAmountService testingScoreAmountService;

	@Autowired
	private TestingQuestionsService testingQuestionsService;

	@Autowired
	UnreadCommentService unreadCommentService;

	@Autowired
	TestingSectionService testingSectionService;

	@Autowired
	TestingCommentService testingCommentService;

	@Autowired
	private TestingTypeService testingTypeService;

	@Autowired
	private TestingResultForMemberService testingResultForMemberService;

	@Autowired
	private TestingResultForJumpService testingResultForJumpService;
	

	/**
	 * 异步分页
	 * 
	 * @param request
	 * @param modelMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap modelMap, 
			Integer pageIndex, Integer pageSize, Long typeId, 
			@RequestParam(value = "title", required = false) String title, 
			@RequestParam(value = "mid", required = false) Long mid,
			@RequestParam(value = "tempMid", required = false) Long tempMid)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (pageIndex == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (pageSize == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}
		if ((mid == null && tempMid == null) || (mid != null && tempMid != null))
		{
			result.setCode(-1);
			result.setMsg("会员id和游客id二选一必填");
			return result;
		}
		if (mid == null)
		{
			mid = tempMid;
		}

		Testing queryTesting = new Testing();
		if (title != null)
		{
			queryTesting.setTitle(title);
		}
		if (pageIndex != null)
		{
			queryTesting.setPageIndex(pageIndex);
		}
		if (pageSize != null)
		{
			queryTesting.setPageSize(pageSize);
		}
		// 只返回有效的记录
		queryTesting.setIsEnable((byte)0);
		queryTesting.setTypeId(typeId);

		// 查询集合
		Long totalCount = testingService.getPageCounts(queryTesting);
		List<Testing> list = testingService.getPageList(queryTesting);
		List<ApiTestingDTO> listDTOs=new ArrayList<ApiTestingDTO>();
		
		for (Testing testing : list)
		{
			ApiTestingDTO testingDTO=new ApiTestingDTO();
			BeanUtils.copyProperties(testing, testingDTO);
			Integer calcMethod = testing.getCalcMethod();
			if (calcMethod.intValue() == 0)// 查询计分型测试是否已经测试
			{
				// 查询是否已经测试
				TestingScoreAmount testingScoreAmount = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(mid, testing.getTestingId());
				if (testingScoreAmount != null)
				{
					testingDTO.setTested((byte)0); // 已测试
				} else
				{
					testingDTO.setTested(-1); // 未测试
				}
			} else if (calcMethod.intValue() == 1)// 查询跳转型测试是否已经测试
			{
				// 查询问卷对应的结论列表
				TestingResultForJump queryTestingResultForJump = new TestingResultForJump();
				queryTestingResultForJump.setTestingId(testing.getTestingId());
				List<TestingResultForJump> resList4Update = testingResultForJumpService.getTestingResultForJumpByQueryTestingResult(queryTestingResultForJump);
				if (resList4Update != null && resList4Update.size() > 0)
				{
					for (TestingResultForJump resJump : resList4Update)
					{
						TestingResultForMember queryResultForMember = new TestingResultForMember();
						queryResultForMember.setMid(mid);
						queryResultForMember.setResId(resJump.getResId());
						queryResultForMember.setIsEnable((byte)0);
						List<TestingResultForMember> resForMemList = testingResultForMemberService.getTestingResultForMemberByQueryTestingResult(queryResultForMember);

						// 查询是否已经测试
						if (resForMemList != null && resForMemList.size() > 0)
						{
							testingDTO.setTested(0); // 已测试
							break; // 退出当前循环（重要！！！）
						} else
						{
							testingDTO.setTested(-1); // 未测试
						}
					}
				}

			}

			// 查询试题数量
			Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(testing.getTestingId());
			testingDTO.setQuestionAmount(questionAmount.intValue());

			// 查询评论数量
			TestingComment testingComment = new TestingComment();
			testingComment.setTestingId(testing.getTestingId());
			Integer commentAmount = testingCommentService.getPageCounts(testingComment);
			testingDTO.setTestingCommentPeopleNum(commentAmount.longValue());

			// 转换实际文件路径
			
				testingDTO.setFilePath(testing.getThumbnail());
				testingDTO.setFilePathMobile(testing.getThumbnailMobile());
				testingDTO.setFilePathSlide(testing.getThumbnailSlide());
			

			if (testing.getTestingPeopleNum() == null)
			{
				testingDTO.setTestingPeopleNum(0l);
			}
			
			listDTOs.add(testingDTO);
			
		}
		result.put("list", listDTOs);
		result.put("count", totalCount);

		result.setCode(0);
		return result;
	}
	

	/**
	 * 获取用户测试过的试卷(获取趣味测试和专业测试，新版本接口)
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid
	 *            用户ID
	 * @return
	 */
	@RequestMapping(value = "/getTestedTestingAll.json")
	@ResponseBody
	public Object getTestedTestingAll(HttpServletRequest request, ModelMap modelMap, TestingScoreAmount testingScoreAmount)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingScoreAmount == null)
		{
			result.setCode(-1);
			result.setMsg("会员ID不能为空");
			return result;
		}

		// 1. 计算类型：计分型
		testingScoreAmount.setIsEnable((byte)0);
		List<TestingScoreAmount> tsaList = testingScoreAmountService.getTestingScoreAmountByMid(testingScoreAmount);
		
		List<WebMemberTestingRecDTO> mtrList = new ArrayList<WebMemberTestingRecDTO>();
		List<WebMemberTestingRecDTO> mtrListForFunny = new ArrayList<WebMemberTestingRecDTO>();
		List<WebMemberTestingRecDTO> mtrListForStu = new ArrayList<WebMemberTestingRecDTO>();
		for (TestingScoreAmount tsa : tsaList)
		{
			ApiTestingScoreAmountDTO tsaDTO=new ApiTestingScoreAmountDTO();
			BeanUtils.copyProperties(tsa, tsaDTO);
			Testing testing = testingService.getTestingById(tsa.getTestingId());

			// 转换实际文件路径
			
			tsaDTO.setFilePath(testing.getThumbnail());
			tsaDTO.setFilePathMobile(testing.getThumbnailMobile());
			tsaDTO.setFilePathSlide(testing.getThumbnailSlide());
			

			if (testing != null)
			{
				tsaDTO.setTitle(testing.getTitle());
				tsaDTO.setCalcMethod(testing.getCalcMethod());
			}

			List<TestingSection> testingSectionList = testingSectionService.getTestingSectionByTestingIdAndScore(tsa.getTestingId(), tsa.getScore());
			if (testingSectionList != null && testingSectionList.size() > 0)
			{
				tsaDTO.setLevel(testingSectionList.get(0).getLevel());
				tsaDTO.setDetail(testingSectionList.get(0).getDetail());
			}

			// 检查问卷是专业测试或趣味测试
			Long typeId = testing.getTypeId();
			TestingType testingType = testingTypeService.getTestingTypeByTypeId(typeId);
			Integer tsType = testingType.getTsType();
			WebMemberTestingRecDTO memberTestingRecDTO = new WebMemberTestingRecDTO();
			BeanUtils.copyProperties(tsaDTO, memberTestingRecDTO);
			if (tsType.intValue() == 0)// 专业测试
			{
				mtrList.add(memberTestingRecDTO);
			} else if (tsType.intValue() == 1)// 趣味测试
			{
				mtrListForFunny.add(memberTestingRecDTO);
			}else if(tsType.intValue() == 2){
				mtrListForStu.add(memberTestingRecDTO);
			}
		}

		// 2. 计算方式：跳转型
		TestingResultForMember queryResultForMember = new TestingResultForMember();
		queryResultForMember.setMid(testingScoreAmount.getMid());
		queryResultForMember.setIsEnable((byte)0);
		List<TestingResultForMember> resForMemList = testingResultForMemberService.getTestingResultForMemberByQueryTestingResult(queryResultForMember);

		// 遍历会员测试管理表
		if (resForMemList != null && resForMemList.size() > 0)
		{
			for (TestingResultForMember resultForMember : resForMemList)
			{

				// 跳转方式问卷测试结论
				TestingResultForJump testingResultForJump = testingResultForJumpService.getTestingResultForJumpByResultId(resultForMember.getResId());

				// 返回问卷标题
				Testing testing = testingService.getTestingById(testingResultForJump.getTestingId());
				ApiTestingDTO testingDTO=new ApiTestingDTO();
				BeanUtils.copyProperties(testing, testingDTO);
				// 转换实际文件路径
				testingDTO.setFilePath(testing.getThumbnail());
				testingDTO.setFilePathMobile(testing.getThumbnailMobile());
				testingDTO.setFilePathSlide(testing.getThumbnailSlide());
				

				// 检查问卷是专业测试或趣味测试
				Long typeId = testing.getTypeId();
				TestingType testingType = testingTypeService.getTestingTypeByTypeId(typeId);
				Integer tsType = testingType.getTsType();
				WebMemberTestingRecDTO memberTestingRecDTO = new WebMemberTestingRecDTO();
				BeanUtils.copyProperties(testingDTO, memberTestingRecDTO);
				memberTestingRecDTO.setLevel(testingResultForJump.getTitle());
				memberTestingRecDTO.setDetail(testingResultForJump.getTitle());
				memberTestingRecDTO.setMid(Long.parseLong(resultForMember.getMid().toString()));
				memberTestingRecDTO.setTestingId(Long.parseLong(testing.getTestingId().toString()));
				memberTestingRecDTO.setTestTime(resultForMember.getTestTime());
				if (tsType.intValue() == 0)// 专业测试
				{
					mtrList.add(memberTestingRecDTO);
				} else if (tsType.intValue() == 1)// 趣味测试
				{
					mtrListForFunny.add(memberTestingRecDTO);
				}else if(tsType.intValue() == 2)
				{
					mtrListForStu.add(memberTestingRecDTO);
				}

			}

		}

		result.put("tsaList", mtrList);
		result.put("mtrListForFunny", mtrListForFunny);
		result.put("mtrListForStu", mtrListForStu);

		result.setCode(0);
		return result;
	}

	/**
	 * 获取用户测试过的试卷(老版本接口，暂不变)
	 * 
	 * @param request
	 * @param modelMap
	 * @param mid
	 *            用户ID
	 * @return
	 */
	@RequestMapping(value = "/getTestedTesting.json")
	@ResponseBody
	public Object getTestedTesting(HttpServletRequest request, ModelMap modelMap, TestingScoreAmount testingScoreAmount)
	{
		ResultEntity result = new ResultEntity();
		// 参数检查
		if (testingScoreAmount == null)
		{
			result.setCode(-1);
			result.setMsg("会员ID不能为空");
			return result;
		}

		List<TestingScoreAmount> tsaList = testingScoreAmountService.getTestingScoreAmountByMid(testingScoreAmount);
		List<ApiTestingScoreAmountDTO> tsaListDTO=new ArrayList<ApiTestingScoreAmountDTO>();
		
		List<Testing> testingList = new ArrayList<Testing>();
		List<ApiTestingDTO> testingListDTO = new ArrayList<ApiTestingDTO>();
		
		for (TestingScoreAmount tsa : tsaList)
		{
			ApiTestingScoreAmountDTO testingScoreAmountDTO =new ApiTestingScoreAmountDTO();
			BeanUtils.copyProperties(tsa, testingScoreAmountDTO);
			Testing testing = testingService.getTestingById(tsa.getTestingId());
			ApiTestingDTO testingDTO=new ApiTestingDTO();
			BeanUtils.copyProperties(testing, testingDTO);
			
			if (testing != null)
			{
				testingScoreAmountDTO.setTitle(testing.getTitle());
				testingListDTO.add(testingDTO);
			}
			List<TestingSection> testingSectionList = testingSectionService.getTestingSectionByTestingIdAndScore(tsa.getTestingId(), tsa.getScore());
			if (testingSectionList != null && testingSectionList.size() > 0)
			{
				testingScoreAmountDTO.setLevel(testingSectionList.get(0).getLevel());
				testingScoreAmountDTO.setDetail(testingSectionList.get(0).getDetail());
			}

			if (testing.getTestingPeopleNum() == null)
			{
				testingDTO.setTestingPeopleNum(0l);
			}

			// 转换实际文件路径
			
			testingDTO.setFilePath(testing.getThumbnail());
			testingDTO.setFilePathMobile(testing.getThumbnailMobile());
			testingDTO.setFilePathSlide(testing.getThumbnailSlide());
			
			testingListDTO.add(testingDTO);
			tsaListDTO.add(testingScoreAmountDTO);
		}

		result.put("list", testingList);
		result.put("tsaList", tsaListDTO);
		result.put("count", testingList.size());

		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, ModelMap modelMap, Long testingId, Long mid)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (testingId == null || testingId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("问卷id不能为空");
			return result;
		}

		// 查询记录详情
		Testing testing = testingService.getValidTestingById(testingId);
		ApiTestingDTO testingDTO=new ApiTestingDTO();
		BeanUtils.copyProperties(testing, testingDTO);
		if(testing != null){
			// 转换实际文件路径
			
			testingDTO.setFilePath(testing.getThumbnail());
			testingDTO.setFilePathMobile(testing.getThumbnailMobile());
			testingDTO.setFilePathSlide(testing.getThumbnailSlide());
			
	
			// 查询试题数量
			Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(testing.getTestingId());
			testingDTO.setQuestionAmount(questionAmount);
	
			// 查询评论数量
			TestingComment testingComment = new TestingComment();
			testingComment.setTestingId(testingId);
			Integer commentAmount = testingCommentService.getPageCounts(testingComment);
			testingDTO.setTestingCommentPeopleNum(commentAmount.longValue());
	
			// 删除未读评论
	//		if (mid != null)
	//		{
	//			unreadCommentService.deleteUnreadTestingComment(mid, Long.valueOf(testingId));
	//		}
	
			if (testing.getTestingPeopleNum() == null)
			{
				testingDTO.setTestingPeopleNum(0l);
			}
		}
		result.put("obj", testingDTO);
		result.setCode(0);
		return result;
	}

	/**
	 * 查询最热门的测试问卷列表
	 * 
	 * @param session
	 * @param request
	 * @param size
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getHotTesting.json")
	@ResponseBody
	public Object getHotTesting(HttpSession session, HttpServletRequest request, Integer size)
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

			// 获取列表
			List<Testing> list = testingService.getTopTesting(size);
			List<ApiTestingDTO> listDTO=new ArrayList<ApiTestingDTO>();
			for (Testing testing : list)
			{
				ApiTestingDTO tDTO=new ApiTestingDTO();
				BeanUtils.copyProperties(testing, tDTO);
				// 转换实际文件路径
				
				tDTO.setFilePath(testing.getThumbnail());
				tDTO.setFilePathMobile(testing.getThumbnailMobile());
				tDTO.setFilePathSlide(testing.getThumbnailSlide());
				

				if (testing.getTestingPeopleNum() == null)
				{
					tDTO.setTestingPeopleNum(0l);
				}
				listDTO.add(tDTO);
			}
			result.put("list", listDTO);

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}

	/**
	 * 查询热门的测试问卷列表（学生测试）
	 * 
	 * @param session
	 * @param request
	 * @param size
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getHotTestingForStuTest.json")
	@ResponseBody
	public Object getHotTestingForStuTest(HttpSession session, HttpServletRequest request, Integer size, @RequestParam(value = "mid", required = false) Long mid,
			@RequestParam(value = "tempMid", required = false) Long tempMid)
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
			if (mid == null)
			{
				mid = tempMid;
			}

			// 获取列表
			List<Testing> list = testingService.getHotTestingForStuTest(size);
			List<ApiTestingDTO> listDTO = new ArrayList<ApiTestingDTO>();
			
			for (Testing testing : list)
			{
				ApiTestingDTO testingDTO=new ApiTestingDTO();
				BeanUtils.copyProperties(testing, testingDTO);
				
				if(mid != null)
				{
					Integer calcMethod = testing.getCalcMethod();
					if (calcMethod.intValue() == 0)// 查询计分型测试是否已经测试
					{
						// 查询是否已经测试
						TestingScoreAmount testingScoreAmount = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(mid, testing.getTestingId());
						if (testingScoreAmount != null)
						{
							testingDTO.setTested(0); // 已测试
						} else
						{
							testingDTO.setTested(-1); // 未测试
						}
					} else if (calcMethod.intValue() == 1)// 查询跳转型测试是否已经测试
					{
						// 查询问卷对应的结论列表
						TestingResultForJump queryTestingResultForJump = new TestingResultForJump();
						queryTestingResultForJump.setTestingId(testing.getTestingId());
						List<TestingResultForJump> resList4Update = testingResultForJumpService.getTestingResultForJumpByQueryTestingResult(queryTestingResultForJump);
						if (resList4Update != null && resList4Update.size() > 0)
						{
							for (TestingResultForJump resJump : resList4Update)
							{
								TestingResultForMember queryResultForMember = new TestingResultForMember();
								queryResultForMember.setMid(mid);
								queryResultForMember.setResId(resJump.getResId());
								queryResultForMember.setIsEnable((byte)0);
								List<TestingResultForMember> resForMemList = testingResultForMemberService.getTestingResultForMemberByQueryTestingResult(queryResultForMember);

								// 查询是否已经测试
								if (resForMemList != null && resForMemList.size() > 0)
								{
									testingDTO.setTested(0); // 已测试
									break; // 退出当前循环（重要！！！）
								} else
								{
									testingDTO.setTested(-1); // 未测试
								}
							}
						}

					}
				} else
				{
					testingDTO.setTested(-1); // 未测试
				}

				// 查询试题数量
				Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(testing.getTestingId());
				testingDTO.setQuestionAmount(questionAmount.intValue());

				// 查询评论数量
				TestingComment testingComment = new TestingComment();
				testingComment.setTestingId(testing.getTestingId());
				Integer commentAmount = testingCommentService.getPageCounts(testingComment);
				testingDTO.setTestingCommentPeopleNum(commentAmount.longValue());

				// 转换实际文件路径
				
				testingDTO.setFilePath(testing.getThumbnail());
				testingDTO.setFilePathMobile(testing.getThumbnailMobile());
				testingDTO.setFilePathSlide(testing.getThumbnailSlide());
				

				if (testing.getTestingPeopleNum() == null)
				{
					testingDTO.setTestingPeopleNum(0l);
				}
				
				listDTO.add(testingDTO);
			}
			result.put("list", listDTO);

		} catch (Exception e)
		{
			result.setCode(ResultEntity.ERROR);
			result.setError("系统错误");
			log.error(e);
		}
		return result;
	}
	
	
	/**
	 * 获取测试类别的所有测试
	 * @param pageIndex
	 * @param pageSize
	 * @param tsType 测试类别
	 * @param mid  用户主键
	 * @param tempMid 游客主键
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getTestingByTypeId.json")
	@ResponseBody
	public Object getTestingByTypeId(Integer pageIndex, Integer pageSize,
			Long typeId,
			@RequestParam(value = "mid", required = false) Long mid
			){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(pageIndex,pageSize,typeId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		List<ApiTestingDTO> listDTOs=new ArrayList<ApiTestingDTO>();
		List<TestingType> typeList=new ArrayList<TestingType>();
		
		Integer count=0;
		
		
		try{
			//传的是tsType
					if(typeId == TestingTypeController.TSTYPE_FUNNY_ALL.longValue() 
					|| typeId == TestingTypeController.TSTYPE_PRO_ALL.longValue() 
					|| typeId == TestingTypeController.TSTYPE_STU_ALL.longValue()
					|| typeId == null
					){
				typeList=getTypeListByTypeId(typeId);
			}else{
			//传的是typeId
				typeList.add(testingTypeService.getTestingTypeByTypeId(typeId));
			}
			//根据TestingTypeList 查询所有的testing
			List<Testing> testingList=testingService.getTestingListByTestingTypes(typeList,pageIndex,pageSize);
			count=testingService.countByTestingTypes(typeList);
			if(testingList.size()>0){
				for(Testing testing : testingList){
					
					
					ApiTestingDTO testingDTO=new ApiTestingDTO();
					BeanUtils.copyProperties(testing, testingDTO);
					Integer calcMethod = testing.getCalcMethod();
					if (calcMethod == 0){// 查询计分型测试是否已经测试
						// 查询是否已经测试
						TestingScoreAmount testingScoreAmount = testingScoreAmountService.getTestingScoreAmountByMidAndTestingId(mid, testing.getTestingId());
						if (testingScoreAmount != null){
							testingDTO.setTested((byte)0); // 已测试
						} else{
							testingDTO.setTested(-1); // 未测试
						}
					} else if (calcMethod == 1){// 查询跳转型测试是否已经测试
						// 查询问卷对应的结论列表
						TestingResultForJump queryTestingResultForJump = new TestingResultForJump();
						queryTestingResultForJump.setTestingId(testing.getTestingId());
						List<TestingResultForJump> resList4Update = testingResultForJumpService.getTestingResultForJumpByQueryTestingResult(queryTestingResultForJump);
						if (resList4Update != null && resList4Update.size() > 0){
							for (TestingResultForJump resJump : resList4Update){
								TestingResultForMember queryResultForMember = new TestingResultForMember();
								queryResultForMember.setMid(mid);
								queryResultForMember.setResId(resJump.getResId());
								queryResultForMember.setIsEnable((byte)0);
								List<TestingResultForMember> resForMemList = testingResultForMemberService.getTestingResultForMemberByQueryTestingResult(queryResultForMember);

								// 查询是否已经测试
								if (resForMemList != null && resForMemList.size() > 0){
									testingDTO.setTested(0); // 已测试
									break; // 退出当前循环（重要！！！）
								} else{
									testingDTO.setTested(-1); // 未测试
								}
							}
						}

					}

					// 查询试题数量
					Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(testing.getTestingId());
					testingDTO.setQuestionAmount(questionAmount.intValue());

					// 查询评论数量
					TestingComment testingComment = new TestingComment();
					testingComment.setTestingId(testing.getTestingId());
					Integer commentAmount = testingCommentService.getPageCounts(testingComment);
					testingDTO.setTestingCommentPeopleNum(commentAmount.longValue());

					// 转换实际文件路径
					testingDTO.setFilePath(testing.getThumbnail());
					testingDTO.setFilePathMobile(testing.getThumbnailMobile());
					testingDTO.setFilePathSlide(testing.getThumbnailSlide());
					if (testing.getTestingPeopleNum() == null){
						testingDTO.setTestingPeopleNum(0l);
					}
					
					listDTOs.add(testingDTO);
				}
			}
			
			} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		result.put("list", listDTOs);
		result.put("count", count);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	/**
	 * 根据typeId 如果是100 101 102则是tsType 查询全部  99是所有
	 * @param typeId
	 * @return
	 */
	public List<TestingType> getTypeListByTypeId(Long typeId){
		Integer tsType=0;
		if(typeId==TestingTypeController.TSTYPE_FUNNY_ALL.longValue()){
			tsType=TestingTypeController.TSTYPE_FUNNY;
		}
		if(typeId==TestingTypeController.TSTYPE_PRO_ALL.longValue()){
			tsType=TestingTypeController.TSTYPE_PRO;
		}
		if(typeId==TestingTypeController.TSTYPE_STU_ALL.longValue()){
			tsType=TestingTypeController.TSTYPE_STU;
		}
		//null 为测试全部标志
		if(typeId==null){
			tsType=null;
		}
		TestingType testingType=new TestingType();
		testingType.setTsType(tsType);
		testingType.setIsEnable((byte)0);
		return testingTypeService.getTestingTypeListByQueryTestingType(testingType);
		
		
	}
	
	
	
}
