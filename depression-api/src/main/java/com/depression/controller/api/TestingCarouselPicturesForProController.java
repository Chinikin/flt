package com.depression.controller.api;

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

import com.depression.entity.ResultEntity;
import com.depression.model.Testing;
import com.depression.model.TestingCarouselPictures;
import com.depression.model.TestingCarouselPicturesOld;
import com.depression.model.TestingComment;
import com.depression.model.api.dto.ApiTestingCarouselPicturesDTO;
import com.depression.model.api.dto.ApiTestingDTO;
import com.depression.service.TestingCarouselPicturesService;
import com.depression.service.TestingCommentService;
import com.depression.service.TestingQuestionsService;
import com.depression.service.TestingScoreAmountService;
import com.depression.service.TestingService;

@Controller
@RequestMapping("/testingCarouselPictures/pro")
public class TestingCarouselPicturesForProController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingCarouselPicturesService testingCarouselPicturesService;
	
	@Autowired
	private TestingService testingService;
	
	@Autowired
	private TestingScoreAmountService testingScoreAmountService;
	
	@Autowired
	TestingCommentService testingCommentService;
	
	@Autowired
	private TestingQuestionsService testingQuestionsService;

	/**
	 * 获取专业测试轮播图片有效列表
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		TestingCarouselPictures queryTestingCarouselPictures = new TestingCarouselPictures();
		queryTestingCarouselPictures.setType(0);// 只查询专业测试类别的图片
		queryTestingCarouselPictures.setIsEnable((byte)0);// 只查询有效的记录

		// 查询集合
		List<TestingCarouselPictures> list = testingCarouselPicturesService.getTestingCarouselPicturesListByQueryTestingCarouselPictures(queryTestingCarouselPictures);
		List<ApiTestingCarouselPicturesDTO> picListDTO = new ArrayList<ApiTestingCarouselPicturesDTO>();
		for (TestingCarouselPictures pic : list)
		{
			// 转换实际文件路径
			/*if (pic.getImgPath() != null && !pic.getImgPath().equals(""))
			{
				pic.setFilePath(pic.getImgPath());
			}*/
			
			Testing testing = testingService.getTestingById(pic.getTestingId());
			// 查询试题数量
			Integer questionAmount = testingQuestionsService.getValidQueCountsByTestingId(testing.getTestingId());
			
			ApiTestingDTO testingDTO=new ApiTestingDTO();
			BeanUtils.copyProperties(questionAmount, testingDTO);
			testingDTO.setQuestionAmount(questionAmount.intValue());

			// 查询评论数量
			TestingComment testingComment = new TestingComment();
			testingComment.setTestingId(testing.getTestingId());
			Integer commentAmount = testingCommentService.getPageCounts(testingComment);
			testingDTO.setTestingCommentPeopleNum(commentAmount.longValue());

			
			if (testing.getThumbnailMobile() != null && !testing.getThumbnailMobile().equals(""))
			{
				testingDTO.setFilePathMobile(testing.getThumbnailMobile());
			}
			if (testing.getThumbnailSlide() != null && !testing.getThumbnailSlide().equals(""))
			{
				testingDTO.setFilePathSlide(testing.getThumbnailSlide());
			}

			if (testing.getTestingPeopleNum() == null)
			{
				testingDTO.setTestingPeopleNum(0l);
			}
			
			ApiTestingCarouselPicturesDTO testingCarouselPicturesDTO = new ApiTestingCarouselPicturesDTO();
			BeanUtils.copyProperties(testingDTO, testingCarouselPicturesDTO);
			// 转换实际文件路径
			if (testing.getThumbnail() != null && !testing.getThumbnail().equals(""))
			{
				testingCarouselPicturesDTO.setFilePath(testing.getThumbnail());
			}
			
			BeanUtils.copyProperties(pic, testingCarouselPicturesDTO);
			picListDTO.add(testingCarouselPicturesDTO);
		}
		result.put("list", picListDTO);
		result.put("count", list.size());

		result.setCode(ResultEntity.SUCCESS);
		return result;
	}

}
