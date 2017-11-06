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
import com.depression.model.Testing;
import com.depression.model.TestingType;
import com.depression.model.web.dto.TestingDTO;
import com.depression.model.web.vo.TestingVO;
import com.depression.service.TestingService;
import com.depression.service.TestingTypeService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/testing")
public class TestingController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TestingService testingService;
	@Autowired
	private TestingTypeService testingTypeService;

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
	public Object list(HttpServletRequest request, ModelMap modelMap, Integer pageIndex, Integer pageSize, String title)
	{
		ResultEntity result = new ResultEntity();
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			pageIndex,
    			pageSize
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		// 查询集合
    	Testing queryTesting =new Testing();
    	queryTesting.setTitle(title);
    	queryTesting.setPageIndex(pageIndex);
    	queryTesting.setPageSize(pageSize);
    	Long totalCount=0l;
    	List<TestingDTO> testingDTOs=new ArrayList<TestingDTO>();
    	try {
    		totalCount= testingService.getPageCounts(queryTesting);
    		
    		if (totalCount != 0){
    			List<Testing> list = testingService.getPageList(queryTesting);
    			
    			for (Testing testing : list){
    				TestingType type=testingTypeService.getTestingTypeByTypeId(testing.getTypeId());
    				TestingDTO testingDTO=new TestingDTO();
    				testingDTO.setTestingName(type.getTestingName());
    				BeanUtils.copyProperties(testing, testingDTO);
    				
    				// 转换实际文件路径
    				if (testingDTO.getThumbnail() != null && !testingDTO.getThumbnail().equals("")){
    					testingDTO.setFilePath(testingDTO.getThumbnail());
    				}
    				if (testingDTO.getThumbnailMobile() != null && !testingDTO.getThumbnailMobile().equals("")){
    					testingDTO.setFilePathMobile(testingDTO.getThumbnailMobile());
    				}
    				if (testingDTO.getThumbnailSlide() != null && !testingDTO.getThumbnailSlide().equals("")){
    					testingDTO.setFilePathSlide(testingDTO.getThumbnailSlide());
    				}
    				testingDTOs.add(testingDTO);
    			}
    			
    		} 
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
		}
		result.put("list", testingDTOs);
		result.put("count", totalCount);
		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, TestingVO tVO)
	{
		ResultEntity result = new ResultEntity();
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			tVO.getTypeId(),
    			tVO.getTitle(),
    			tVO.getSubtitle(),
    			tVO.getContentExplain()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
    	Testing testing=new Testing();
		BeanUtils.copyProperties(tVO, testing);
		try {
			testingService.save(testing);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
			result.put("testingId", testing.getTestingId());
			result.setMsg("新增成功");
			result.setCode(0);
			return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, 
			TestingVO tVO){
		ResultEntity result = new ResultEntity();
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			tVO.getTestingId(),
    			tVO.getTypeId(),
    			tVO.getTitle(),
    			tVO.getSubtitle(),
    			tVO.getContentExplain()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
    	Testing testing=new Testing();
    	BeanUtils.copyProperties(tVO, testing);
    	try {
    		testingService.updateTesting(testing);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
    	
    	result.put("testingId", testing.getTestingId());
		result.setCode(0);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changeStatus.json")
	@ResponseBody
	public Object changeStatus(HttpServletRequest request, ModelMap modelMap, String testingId, byte isDel)
	{
		ResultEntity result = new ResultEntity();

		//检验参数
    	if(PropertyUtils.examineOneNull(
    			testingId,
    			isDel
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

    	try {
    		testingService.updateTestingStatus(testingId,isDel);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
    	
		
		result.setCode(0);
		result.setMsg("状态更新成功");

		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/view.json")
	@ResponseBody
	public Object view(HttpServletRequest request, ModelMap modelMap, Long testingId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		//检验参数
    	if(PropertyUtils.examineOneNull(
    			testingId
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
    	Testing testing=new Testing();
    	try {
    		testing=testingService.getTestingByPrimaryKey(testingId);
		} catch (Exception e) {
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		TestingDTO testingDTO=new TestingDTO();
		BeanUtils.copyProperties(testing, testingDTO);
		// 转换实际文件路径
		
		testingDTO.setFilePath(testingDTO.getThumbnail());
		testingDTO.setFilePathMobile(testingDTO.getThumbnailMobile());
		testingDTO.setFilePathSlide(testingDTO.getThumbnailSlide());
		

		result.put("obj", testingDTO);
		result.setCode(0);
		return result;
	}
}
