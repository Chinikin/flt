package com.depression.controller.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
import com.depression.model.UserType;
import com.depression.model.web.dto.CapitalPersonalAssetsDTO;
import com.depression.model.web.vo.CapitalPersonalAssetsVO;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.ServiceGoodsService;
import com.depression.service.ServiceOrderService;
import com.depression.utils.PropertyUtils;

/**
 * 个人资产
 * 
 * @author:ziye_huang
 * @date:2016年8月28日
 */
@Controller
@RequestMapping("/Assets")
public class CapitalPersonalAssetsController
{
	Logger log = Logger.getLogger(this.getClass().getSimpleName());
	@Autowired
	CapitalPersonalAssetsService capitalPersonalAssetsService;
	@Autowired
	ServiceGoodsService serviceGoodsService;
	@Autowired
	ServiceOrderService serviceOrderService;

	/**
	 * 添加
	 * 
	 * @param session
	 * @param request
	 * @param cpa
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addCapitalPersonalAssets.json")
	@ResponseBody
	public Object addCapitalPersonalAssets(HttpSession session, HttpServletRequest request, CapitalPersonalAssetsVO cpaVO)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(cpaVO, cpaVO.getMid()))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try
		{
			// 检查该用户的mid是否存在
			CapitalPersonalAssets selectCpa = capitalPersonalAssetsService.selectByMid(cpaVO.getMid());
			if (null == selectCpa)
			{
				capitalPersonalAssetsService.insert(cpaVO.getMid());
			}
		} catch (Exception e)
		{
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_SYSTEM_IO.getCode());
			result.setMsg(ErrorCode.ERROR_SYSTEM_IO.getMessage());
			return result;
		}

		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}

	/**
	 * 启用
	 * 
	 * @param session
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enableCapitalPersonalAssets.json")
	@ResponseBody
	public Object enableCapitalPersonalAssets(HttpSession session, HttpServletRequest request, @RequestParam("ids[]") Long[] ids)
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
			capitalPersonalAssetsService.enableByPK(Arrays.asList(ids), (byte) 0);
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
	 * 禁用
	 * 
	 * @param session
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/disableCapitalPersonalAssets.json")
	@ResponseBody
	public Object disableCapitalPersonalAssets(HttpSession session, HttpServletRequest request, @RequestParam("ids[]") Long[] ids)
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
			capitalPersonalAssetsService.enableByPK(Arrays.asList(ids), (byte) 1);
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
	 * 根据userType获取个人资产列表
	 * 
	 * @param session
	 * @param request
	 * @param userType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/selectWithPageByUserType.json")
	@ResponseBody
	public Object selectWithPageByUserType(HttpSession session, HttpServletRequest request, UserType userType)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(userType, userType.getUserType(), userType.getPageIndex(), userType.getPageSize()))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try
		{
			List<CapitalPersonalAssetsDTO> list = capitalPersonalAssetsService.selectWithPageByType(userType);
			if (null == list)
			{
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}
			int count = capitalPersonalAssetsService.selectWithPageByTypeCount(userType);
			result.setList(list);
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
	 * 更新数据
	 * 
	 * @param session
	 * @param request
	 * @param cpaVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateCapitalPersonalAssets.json")
	@ResponseBody
	public Object updateCapitalPersonalAssets(HttpSession session, HttpServletRequest request, CapitalPersonalAssetsVO cpaVO)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(cpaVO, cpaVO.getPaid()))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try
		{
			CapitalPersonalAssets cpa = capitalPersonalAssetsService.selectByMid(cpaVO.getMid());

			CapitalPersonalAssets capitalPersonalAssets = new CapitalPersonalAssets();
			capitalPersonalAssets.setPaid(cpaVO.getPaid());
			capitalPersonalAssets.setMid(cpa.getMid());
			capitalPersonalAssets.setCashBalance(cpaVO.getCashBalance());
			capitalPersonalAssets.setServiceIncomeAmount(cpaVO.getServiceIncomeAmount());
			capitalPersonalAssets.setPayAmount(cpaVO.getPayAmount());
			capitalPersonalAssets.setExpenseAmount(cpaVO.getExpenseAmount());
			capitalPersonalAssets.setWithdrawAmount(cpaVO.getWithdrawAmount());

			capitalPersonalAssetsService.update(capitalPersonalAssets);
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

	@RequestMapping(method = RequestMethod.POST, value = "/selectCounselorAccountsByCondition.json")
	@ResponseBody
	public Object selectCounselorAccountsByCondition(HttpSession session, HttpServletRequest request, @RequestParam(value = "userType", required = true) Integer userType,
			@RequestParam(value = "mobilePhone", required = false) String mobilePhone, @RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "nickname", required = false) String nickname, @RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "startTime", required = false) String startTime, @RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "startAnswerCount", required = false) Integer startAnswerCount, @RequestParam(value = "endAnswerCount", required = false) Integer endAnswerCount,
			@RequestParam(value = "startServiceIncomeAmount", required = false) BigDecimal startServiceIncomeAmount,
			@RequestParam(value = "endServiceIncomeAmount", required = false) BigDecimal endServiceIncomeAmount, @RequestParam(value = "topNum", required = false) Integer topNum,
			@RequestParam(value = "topPercent", required = false) Integer topPercent, @RequestParam(value = "sortName", required = false) String sortName,
			@RequestParam(value = "sortType", required = false) String sortType, @RequestParam(value = "pageIndex", required = true) Integer pageIndex,
			@RequestParam(value = "pageSize", required = true) Integer pageSize)
	{
		ResultEntity result = new ResultEntity();
		if (PropertyUtils.examineOneNull(userType, pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<CapitalPersonalAssetsDTO> list = capitalPersonalAssetsService.selectCounselorAccounts(userType, mobilePhone, account, nickname, title,
					(null == startTime || "".equals(startTime)) ? null : sdf.parse(startTime), (null == endTime || "".equals(endTime)) ? null : sdf.parse(endTime), startAnswerCount, endAnswerCount,
					startServiceIncomeAmount, endServiceIncomeAmount, topNum, topPercent, sortName, sortType, getPageStartNum(pageIndex, pageSize), pageSize);
			if (null == list)
			{
				result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
				result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
				return result;
			}

			List<CapitalPersonalAssetsDTO> tList = new ArrayList<CapitalPersonalAssetsDTO>();
			if (userType.equals(2))
			{
				// 咨询师咨询数统计
				for (int i = 0; i < list.size(); i++)
				{
					CapitalPersonalAssetsDTO cpa = list.get(i);
					ServiceGoods sg = serviceGoodsService.selectImmVoiceByMid(cpa.getMid());
					cpa.setAnswerCount(sg.getTimes());
					tList.add(i, cpa);
				}
			} else if (userType.equals(1))
			{
				// 求助者咨询数统计
				for (int i = 0; i < list.size(); i++)
				{
					CapitalPersonalAssetsDTO cpa = list.get(i);
					ServiceOrder record = new ServiceOrder();
					record.setMid(cpa.getMid());
					int answerCount = serviceOrderService.selectCount(record);
					cpa.setAnswerCount(answerCount);
					tList.add(cpa);
				}
			}

			Integer count = capitalPersonalAssetsService.selectCounselorAccountsCount(userType, mobilePhone, account, nickname, title,
					(null == startTime || "".equals(startTime)) ? null : sdf.parse(startTime), (null == endTime || "".equals(endTime)) ? null : sdf.parse(endTime), startAnswerCount, endAnswerCount,
					startServiceIncomeAmount, endServiceIncomeAmount, topNum, topPercent, sortName, sortType, pageIndex, pageSize);
			result.setCount(null == count ? 0 : count);
			if (null != topNum)
			{
				if (pageIndex * pageSize > topNum && tList.size() > topNum)
				{
					int size = topNum - (pageIndex - 1) * pageSize;
					List<CapitalPersonalAssetsDTO> tempList = new ArrayList<CapitalPersonalAssetsDTO>();
					for (int i = 0; i < size; i++)
					{
						tempList.add(tList.get(i));
					}
					result.setList(tempList);
					if (tempList.size() < count)
					{
						result.setCount(tempList.size());
					} else
					{
						result.setCount(count);
					}
				} else
				{
					result.setList(tList);
					result.setCount(count);
				}
			} else
			{
				if (null != topPercent)
				{
					int num = count * topPercent / 100;
					if (pageIndex * pageSize > num && tList.size() > num)
					{
						int size = num - (pageIndex - 1) * pageSize;
						List<CapitalPersonalAssetsDTO> tempList = new ArrayList<CapitalPersonalAssetsDTO>();
						for (int i = 0; i < size; i++)
						{
							tempList.add(tList.get(i));
						}
						result.setList(tempList);
						if (tempList.size() < count)
						{
							result.setCount(tempList.size());
						} else
						{
							result.setCount(count);
						}
					} else
					{
						result.setList(tList);
						result.setCount(count);
					}
				} else
				{
					result.setList(tList);
				}
			}
			// result.setCount(null == count ? 0 : count);
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

	public Integer getPageStartNum(Integer pageIndex, Integer pageSize)
	{
		Integer pageStartNum = null;
		if (pageSize == null || pageIndex == null)
		{
			pageStartNum = null;
		} else
		{
			if (pageIndex <= 0)
			{
				pageStartNum = 0;
			} else
			{
				pageStartNum = (pageIndex - 1) * pageSize;
			}
		}
		return pageStartNum;
	}
}
