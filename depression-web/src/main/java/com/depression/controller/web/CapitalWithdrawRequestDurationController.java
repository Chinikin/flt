package com.depression.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalWithdrawRequestDuration;
import com.depression.service.CapitalWithdrawRequestDurationService;

/**
 * 提现日期配置
 * 
 * @author hongqian_li
 * 
 */
@Controller
@RequestMapping("/CapitalWithdrawRequestDuration")
public class CapitalWithdrawRequestDurationController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	CapitalWithdrawRequestDurationService mCapitalWithdrawRequestDurationService;

	@RequestMapping(method = RequestMethod.POST, value = "/updateCapitalWithdrawRequestsDuration.json")
	@ResponseBody
	public Object updateCapitalWithdrawRequestsDuration(HttpSession session, HttpServletRequest request, Long wrdid, Integer beginDate, Integer endDate)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			if (null == wrdid || null == beginDate || null == endDate)
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}

			CapitalWithdrawRequestDuration wrd = mCapitalWithdrawRequestDurationService.selectByPrimaryKey(wrdid);
			if (null == wrd)
			{
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			wrd.setBeginDate(beginDate);
			wrd.setEndDate(endDate);
			mCapitalWithdrawRequestDurationService.update(wrd);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.debug(e.toString());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getAllCapitalWithdrawRequestsDuration.json")
	@ResponseBody
	public Object getAllCapitalWithdrawRequestsDuration(HttpSession session, HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{

			List<CapitalWithdrawRequestDuration> all = mCapitalWithdrawRequestDurationService.selectAll(new CapitalWithdrawRequestDuration());
			result.setList(all);
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.debug(e.toString());
		}
		return result;
	}
}
