package com.depression.controller.web;

import java.util.ArrayList;
import java.util.Date;
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
import com.depression.model.AdStartupPage;
import com.depression.model.web.dto.AdStartupPageDTO;
import com.depression.model.web.vo.AdStartupPageVO;
import com.depression.model.web.vo.WebIdsVO;
import com.depression.service.AdStartupPageService;
import com.depression.utils.Configuration;
import com.depression.utils.PropertyUtils;

/**
 * 启动页管理
 * 
 * @author fanxinhui
 * 
 */
@Controller
@RequestMapping("/adStartupPage")
public class AdStartupPageController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AdStartupPageService adStartupPageService;

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
	public Object list(HttpServletRequest request, ModelMap modelMap, AdStartupPageVO adStartupPage)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (adStartupPage == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (adStartupPage.getPageIndex() == null)
		{
			result.setCode(-1);
			result.setMsg("分页页码不能为空");
			return result;
		}
		if (adStartupPage.getPageSize() == null)
		{
			result.setCode(-1);
			result.setMsg("每页条数不能为空");
			return result;
		}

		// 查询集合
		int totalCount = adStartupPageService.countAdStartupPageList(
				adStartupPage.getPageTitle(), adStartupPage.getPageStartNum(), 
				adStartupPage.getPageSize(), adStartupPage.getType(), new Date());
		List<AdStartupPage> list = adStartupPageService.searchAdStartupPageList(
				adStartupPage.getPageTitle(), adStartupPage.getPageStartNum(), 
				adStartupPage.getPageSize(), adStartupPage.getType(), new Date());
		List<AdStartupPageDTO> dtoList = new ArrayList<AdStartupPageDTO>();
		for (AdStartupPage startupPage : list)
		{
			AdStartupPageDTO adBannerDTO = new AdStartupPageDTO();
			BeanUtils.copyProperties(startupPage, adBannerDTO);
			// 转换实际文件路径
			if (startupPage.getPicPath() != null && !startupPage.getPicPath().equals(""))
			{
				adBannerDTO.setFilePath(startupPage.getPicPath());
			}
			
			calcStartPageStatus(adBannerDTO);
			dtoList.add(adBannerDTO);
		}
		result.put("list", dtoList);
		result.put("count", totalCount);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	/**
	 * 计算启动页状态
	 * 
	 * @param adBannerDTO
	 */
	private void calcStartPageStatus(AdStartupPageDTO adBannerDTO)
	{
		Date curDate = new Date();
		if (adBannerDTO.getStartTime() != null && curDate.before(adBannerDTO.getStartTime()))
		{
			adBannerDTO.setStartPageStatus("未开始");
		} else if (adBannerDTO.getStartTime() != null && 
			         adBannerDTO.getEndTime() != null &&
				      curDate.after(adBannerDTO.getStartTime()) &&
				        curDate.before(adBannerDTO.getEndTime()))
		{
			adBannerDTO.setStartPageStatus("进行中");
		} else if (adBannerDTO.getEndTime() != null &&
			        curDate.after(adBannerDTO.getEndTime()))
		{
			adBannerDTO.setStartPageStatus("已结束");
		}
	}

	/**
	 * 新增记录
	 * @param request
	 * @param modelMap
	 * @param startupPage
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, ModelMap modelMap, AdStartupPage startupPage)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (startupPage == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (startupPage.getPageTitle() == null || startupPage.getPageTitle().equals(""))
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}

		// 插入记录
		adStartupPageService.insert(startupPage);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 更新记录
	 * @param request
	 * @param modelMap
	 * @param startupPage
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update.json")
	@ResponseBody
	public Object update(HttpServletRequest request, ModelMap modelMap, AdStartupPage startupPage)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (startupPage == null)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setError(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			log.error(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		if (startupPage.getStPageId() == null || startupPage.getStPageId().equals(""))
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}
		if (startupPage.getPageTitle() == null || startupPage.getPageTitle().equals(""))
		{
			result.setCode(-1);
			result.setMsg("标题不能为空");
			return result;
		}

		AdStartupPage startupPageRtn = adStartupPageService.selectByPrimaryKey(startupPage.getStPageId());
		if (startupPageRtn == null)
		{
			result.setCode(-1);
			result.setMsg("记录不存在");
			return result;
		}

		// 更新
		adStartupPageService.update(startupPage);

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 根据id列表批量删除记录
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
		adStartupPageService.deleteByPrimaryKeyBulk(ids.getIds());
		result.setCode(0);
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
	public Object view(HttpServletRequest request, ModelMap modelMap, Long stPageId)
	{
		ResultEntity result = new ResultEntity();

		// 参数检查
		if (stPageId == null || stPageId.equals(""))
		{
			result.setCode(-1);
			result.setMsg("id不能为空");
			return result;
		}

		// 查询记录详情
		AdStartupPage startupPage = adStartupPageService.selectByPrimaryKey(stPageId);
		AdStartupPageDTO adStartupPageDTO = new AdStartupPageDTO();
		BeanUtils.copyProperties(startupPage, adStartupPageDTO);

		// 转换实际文件路径
		if (startupPage.getPicPath() != null && !startupPage.getPicPath().equals(""))
		{
			adStartupPageDTO.setFilePath(startupPage.getPicPath());
		}

		result.put("obj", adStartupPageDTO);
		result.setCode(0);
		return result;
	}
}
