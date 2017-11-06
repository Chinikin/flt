package com.depression.controller.web;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalPlatformCash;
import com.depression.model.web.dto.CapitalPlatformUnitFundStatisticsDTO;
import com.depression.model.web.dto.WebCapitalPlatformCashDTO;
import com.depression.service.CapitalPlatformCashService;
import com.depression.service.CapitalPlatformUnitFundStatisticsService;
import com.depression.utils.PropertyUtils;

/**
 * 平台收入统计
 * 
 * @author:ziye_huang
 * @date:2016年8月30日
 */
@RequestMapping("/Statistics")
@Controller
public class CapitalPlatformUnitFundStatisticsController
{
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	@Autowired
	CapitalPlatformUnitFundStatisticsService capitalPlatformUnitFundStatisticsService;
	@Autowired
	CapitalPlatformCashService capitalPlatformCashService;

	/**
	 * 查询平台收支记录
	 * 
	 * @param session
	 * @param request
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/selectPlatformIncomeAndExpensesStatistics.json")
	@ResponseBody
	public Object selectPlatformIncomeAndExpensesStatistics(HttpSession session, HttpServletRequest request, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate)
	{
		ResultEntity result = new ResultEntity();

		if (PropertyUtils.examineOneNull(startDate, endDate))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
		}

		try
		{
			capitalPlatformUnitFundStatisticsService.calcPlatformUnitFundStatistics();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<CapitalPlatformUnitFundStatisticsDTO> list = capitalPlatformUnitFundStatisticsService.selectIncomeStatistics(sdf.parse(startDate), sdf.parse(endDate));
			if (null == list)
			{
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			int count = capitalPlatformUnitFundStatisticsService.selectIncomeStatisticsCount(sdf.parse(startDate), sdf.parse(endDate));
			result.setCount(count);
			result.setList(list);
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getPlatformCash.json")
	@ResponseBody
	public Object getPlatformCash()
	{
		ResultEntity result = new ResultEntity();
		
		CapitalPlatformCash capitalPlatformCash;
		try
		{
			capitalPlatformCash = capitalPlatformCashService.selectRecordLimitOne();
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		WebCapitalPlatformCashDTO platformCashDTO = new WebCapitalPlatformCashDTO();
		BeanUtils.copyProperties(capitalPlatformCash, platformCashDTO);
		
		result.put("platformCash", platformCashDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
}
