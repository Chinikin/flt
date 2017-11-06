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
import com.depression.model.TestingCarouselPictures;
import com.depression.model.web.dto.WebTestingCarouselPicturesDTO;
import com.depression.model.web.vo.TestingCarouselPicturesVO;
import com.depression.service.TestingCarouselPicturesService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/testingCarouselPictures/pro")
public class TestingCarouselPicturesForProController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingCarouselPicturesService testingCarouselPicturesService;

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
	public Object list(HttpServletRequest request, ModelMap modelMap, TestingCarouselPictures testingCarouselPictures)
	{
		ResultEntity result = new ResultEntity();
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			testingCarouselPictures,
    			testingCarouselPictures.getPageIndex(),
    			testingCarouselPictures.getPageSize()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		TestingCarouselPictures queryTestingCarouselPictures = new TestingCarouselPictures();
		
		BeanUtils.copyProperties(testingCarouselPictures, queryTestingCarouselPictures);
		queryTestingCarouselPictures.setType(0);
		Integer totalCount=0;
		List<TestingCarouselPictures> list=new ArrayList<TestingCarouselPictures>();
		try {
			// 查询集合
			totalCount = testingCarouselPicturesService.getPageCounts(queryTestingCarouselPictures);
			list = testingCarouselPicturesService.getPageList(queryTestingCarouselPictures);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
		}
		List<WebTestingCarouselPicturesDTO> tcpDTOs=new ArrayList<WebTestingCarouselPicturesDTO>();
		for (TestingCarouselPictures pic : list){
			WebTestingCarouselPicturesDTO tcpDTO=new WebTestingCarouselPicturesDTO();
			BeanUtils.copyProperties(pic, tcpDTO);
			// 转换实际文件路径
			if (pic.getImgPath() != null && !pic.getImgPath().equals("")){
				tcpDTO.setFilePath(pic.getImgPath());
			}
			tcpDTOs.add(tcpDTO);
		}
		result.put("list", tcpDTOs);
		result.put("count", totalCount);

		result.setCode(ResultEntity.SUCCESS);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, TestingCarouselPicturesVO testingCarouselPicturesVO)
	{
		ResultEntity result = new ResultEntity();
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			testingCarouselPicturesVO.getDescp(),
    			testingCarouselPicturesVO.getImgPath(),
    			testingCarouselPicturesVO.getTestingId()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		TestingCarouselPictures insertTestingCarouselPictures = new TestingCarouselPictures();
		BeanUtils.copyProperties(testingCarouselPicturesVO, insertTestingCarouselPictures);
		try {
			testingCarouselPicturesService.insertTestingCarouselPictures(insertTestingCarouselPictures);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
		}
		
		result.put("cpid", insertTestingCarouselPictures.getCpid());
		result.setMsg("新增成功");

		result.setCode(ResultEntity.SUCCESS);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, TestingCarouselPicturesVO testingCarouselPicturesVO)
	{
		ResultEntity result = new ResultEntity();
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			testingCarouselPicturesVO.getDescp(),
    			testingCarouselPicturesVO.getImgPath(),
    			//testingCarouselPicturesVO.getTestingId(),
    			testingCarouselPicturesVO.getCpid()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
    	TestingCarouselPictures testingCarouselPictures=new TestingCarouselPictures();
    	BeanUtils.copyProperties(testingCarouselPicturesVO, testingCarouselPictures);
    	try {
    		testingCarouselPicturesService.updateTestingCarouselPictures(testingCarouselPictures);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
		}
    	
		result.put("cpId", testingCarouselPictures.getCpid());
		result.setMsg("更新成功");
		result.setCode(ResultEntity.SUCCESS);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, String ids, Byte isDel)
	{
		ResultEntity result = new ResultEntity();
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			ids,
    			isDel
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
    	
    	try {
    		testingCarouselPicturesService.updateTestingCarouselPicturesStatusByPrimaryIds(ids,isDel);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
		}
		
		result.setCode(ResultEntity.SUCCESS);
		result.setMsg("状态更新成功");
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, ModelMap modelMap, Long cpid)
	{
		ResultEntity result = new ResultEntity();

		//检验参数
    	if(PropertyUtils.examineOneNull(
    			cpid
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
    	TestingCarouselPictures testingCarouselPictures=new TestingCarouselPictures();
    	try {
    		// 查询记录详情
    		testingCarouselPictures = testingCarouselPicturesService.getTestingCarouselPicturesByPrimaryId(cpid);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
		}
    	WebTestingCarouselPicturesDTO webTestingCarouselPicturesDTO=new WebTestingCarouselPicturesDTO();
    	// 转换实际文件路径
    	BeanUtils.copyProperties(testingCarouselPictures, webTestingCarouselPicturesDTO);
    	if (testingCarouselPictures.getImgPath() != null && !testingCarouselPictures.getImgPath().equals("")){
			webTestingCarouselPicturesDTO.setFilePath(testingCarouselPictures.getImgPath());
		}

		result.put("obj", webTestingCarouselPicturesDTO);
		result.setCode(ResultEntity.SUCCESS);
		return result;
	}
}
