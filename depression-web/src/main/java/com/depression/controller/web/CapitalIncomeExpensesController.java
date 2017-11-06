package com.depression.controller.web;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.MidModel;
import com.depression.model.web.dto.CapitalIncomeExpensesDTO;
import com.depression.model.web.dto.ExpensesRecordDTO;
import com.depression.model.web.vo.CapitalIncomeExpensesVO;
import com.depression.service.CapitalIncomeExpenseService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.utils.AccountUtil;
import com.depression.utils.PropertyUtils;

/**
 * 现金收支明细
 * 
 * @author:ziye_huang
 * @date:2016年8月29日
 */
@Controller
@RequestMapping("/Income")
public class CapitalIncomeExpensesController
{
	Logger log = Logger.getLogger(this.getClass().getSimpleName());
	@Autowired
	CapitalIncomeExpenseService capitalIncomeExpenseService;
	@Autowired
	CapitalPersonalAssetsService capitalPersonalAssetsService;

	/**
	 * 删除一条现金收支明细
	 * 
	 * @param session
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/deleteByPK.json")
	@ResponseBody
	public Object deleteByPK(HttpSession session, HttpServletRequest request, @RequestParam("ids[]") Long[] ids)
	{
		ResultEntity result = new ResultEntity();

		if (null == ids || 0 == ids.length)
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try
		{
			capitalIncomeExpenseService.deleteByPK(Arrays.asList(ids));
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 查找收支记录（分页）
	 * 
	 * @param session
	 * @param request
	 * @param cieVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/selectWithPageByMid.json")
	@ResponseBody
	public Object selectWithPageByMid(HttpSession session, HttpServletRequest request, CapitalIncomeExpensesVO cieVO)
	{
		ResultEntity result = new ResultEntity();

		if (PropertyUtils.examineOneNull(cieVO, cieVO.getMid(), cieVO.getItems(), cieVO.getPageIndex(), cieVO.getPageSize()))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try
		{
			MidModel swpbm = new MidModel();
			swpbm.setItems(cieVO.getItems());
			swpbm.setMid(cieVO.getMid());
			swpbm.setPageIndex(cieVO.getPageIndex());
			swpbm.setPageSize(cieVO.getPageSize());
			List<CapitalIncomeExpensesDTO> list = capitalIncomeExpenseService.selectWithPageByMid(swpbm);
			if (null == list)
			{
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			int count = capitalIncomeExpenseService.selectCount(swpbm);
			CapitalPersonalAssets cpa = capitalPersonalAssetsService.selectByMid(cieVO.getMid());
			result.setList(list);
			result.put("cpa", cpa);
			result.setCount(count);
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

	/**
	 * 查询消费记录
	 * 
	 * @param session
	 * @param request
	 * @param cieVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/selectWithPageExpenseRecordByMid.json")
	@ResponseBody
	public Object selectWithPageExpenseRecordByMid(HttpSession session, HttpServletRequest request, CapitalIncomeExpensesVO cieVO)
	{
		ResultEntity result = new ResultEntity();

		if (PropertyUtils.examineOneNull(cieVO, cieVO.getMid(), cieVO.getPageIndex(), cieVO.getPageSize()))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try
		{
			MidModel swpbm = new MidModel();
			swpbm.setMid(cieVO.getMid());
			swpbm.setPageIndex(cieVO.getPageIndex());
			swpbm.setPageSize(cieVO.getPageSize());

			List<ExpensesRecordDTO> list = capitalIncomeExpenseService.selectWithPageExpenseRecordByMid(swpbm);
			if (null == list)
			{
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			int count = capitalIncomeExpenseService.selectExpenseRecordCount(swpbm);
			CapitalPersonalAssets cpa = capitalPersonalAssetsService.selectByMid(cieVO.getMid());
			result.setList(list);
			result.put("cpa", cpa);
			result.setCount(count);
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

}
