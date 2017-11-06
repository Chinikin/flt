package com.depression.controller.api;

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
import com.depression.service.AdStartupPageService;
import com.depression.utils.Configuration;

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
	public Object list(HttpServletRequest request, ModelMap modelMap)
	{
		ResultEntity result = new ResultEntity();

		// 查询集合
		List<AdStartupPage> list = adStartupPageService.selectInProgressStartPage(new Date());
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
			dtoList.add(adBannerDTO);
		}
		result.put("list", dtoList);
		result.put("count", dtoList.size());

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

}
