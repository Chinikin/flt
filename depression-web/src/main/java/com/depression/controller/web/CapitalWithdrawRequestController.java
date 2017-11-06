package com.depression.controller.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.CapitalIncomeExpenses;
import com.depression.model.CapitalPersonalAssets;
import com.depression.model.CapitalWithdrawRequest;
import com.depression.model.Member;
import com.depression.model.SystemDepartment;
import com.depression.model.SystemUserInfo;
import com.depression.model.web.dto.WebCapitalWithdrawRequestDTO;
import com.depression.model.web.dto.WebSystemUserInfoDTO;
import com.depression.model.web.vo.WebCapitalWithdrawRequestVO;
import com.depression.service.CapitalIncomeExpenseService;
import com.depression.service.CapitalPersonalAssetsService;
import com.depression.service.CapitalPlatformCashService;
import com.depression.service.CapitalWithdrawRequestService;
import com.depression.service.MemberService;
import com.depression.service.PingxxService;
import com.depression.service.SystemDepartmentService;
import com.depression.service.SystemUserInfoService;
import com.depression.utils.AccountUtil;
import com.pingplusplus.model.Transfer;

/**
 * 提现申请
 * 
 * @author hongqian_li
 * @date 2016/08/28
 */
@Controller
@RequestMapping("/CapitalWithdrawRequest")
public class CapitalWithdrawRequestController
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	CapitalWithdrawRequestService mCapitalWithdrawRequestService;
	@Autowired
	MemberService mMemberService;
	@Autowired
	CapitalPersonalAssetsService mMCapitalPersonalAssetsService;
	@Autowired
	CapitalPlatformCashService mCapitalPlatformCashService;
	@Autowired
	CapitalIncomeExpenseService mCapitalIncomeExpenseService;
	@Autowired
	PingxxService pingxxService;
	@Autowired
	SystemUserInfoService systemUserInfoService;
	@Autowired
	SystemDepartmentService systemDepartmentService;

	/**
	 * 分页条件查询
	 * 
	 * @param session
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getCapitalWithdrawRequestsByPage.json")
	@ResponseBody
	public Object getCapitalWithdrawRequestsByPage(HttpSession session, HttpServletRequest request, WebCapitalWithdrawRequestVO entity)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			String operationName = entity.getOperationName();
			if (!StringUtils.isEmpty(operationName))
			{
				entity.setAuditor(operationName);
			}
			// 根据条件查询
			List<CapitalWithdrawRequest> cwrs = mCapitalWithdrawRequestService.selectAllByPage(entity);
			// 查询总条数
			int counts = mCapitalWithdrawRequestService.selectCount(entity);
			List<WebCapitalWithdrawRequestDTO> cwrdtos = new ArrayList<>();
			for (CapitalWithdrawRequest capitalWithdrawRequest : cwrs)
			{
				WebCapitalWithdrawRequestDTO cwrDto = new WebCapitalWithdrawRequestDTO();
				BeanUtils.copyProperties(capitalWithdrawRequest, cwrDto);
				
				// 查找咨询师姓名，以及手机号
				Member member = mMemberService.selectMemberByMid(capitalWithdrawRequest.getMid());
				if (member != null)
				{
					cwrDto.setPsychologicalName(member.getNickname());
					cwrDto.setPsycholoPhone(member.getMobilePhone());
				}
				
				// 查找余额
				CapitalPersonalAssets mpa = mMCapitalPersonalAssetsService.selectByMid(capitalWithdrawRequest.getMid());
				if (mpa != null)
				{
					cwrDto.setCashBalance(mpa.getCashBalance());
				}
				if (member != null)
				{
					cwrdtos.add(cwrDto);
				}
				
			}
			result.setCount(counts);
			result.setList(cwrdtos);
		} catch (Exception e)
		{
			e.printStackTrace();
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.error(e);
		}
		return result;
	}

	/**
	 * 提现操作应该在pingxx返回后进行资金操作
	 * 
	 * @param session
	 * @param request
	 * @param no
	 * @param operationName
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/capitalWithdrawOperationByNo.json")
	@ResponseBody
	public Object capitalWithdrawOperationByNo(HttpSession session, HttpServletRequest request, String no, String operationName)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		try
		{
			// 参数不完整
			if (StringUtils.isEmpty(no))
			{
				result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
				result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
				return result;
			}
			// 根据申请提现编号查询记录
			CapitalWithdrawRequest cwq = mCapitalWithdrawRequestService.selectByNo(no);
			// 不存在该提现申请||或者已经审核通过（已经提现）
			if (null == cwq || 1 == cwq.getStatus())
			{
				result.setCode(ErrorCode.ERROR_WITHDRAW_NO_INEXISTENT.getCode());
				result.setError(ErrorCode.ERROR_WITHDRAW_NO_INEXISTENT.getMessage());
				return result;
			}
			// 进行提现操作
			// 得到提现人的资金账户
			CapitalPersonalAssets mpa = mMCapitalPersonalAssetsService.selectByMid(cwq.getMid());
			// 该账户不存在
			if (null == mpa || 1 == mpa.getIsEnable())
			{
				result.setCode(ErrorCode.ERROR_CAPITAL_PERSONAL_ASSETS_INEXISTENT.getCode());
				result.setError(ErrorCode.ERROR_CAPITAL_PERSONAL_ASSETS_INEXISTENT.getMessage());
				return result;
			}

			if (mpa.getCashBalance().compareTo(cwq.getAmount()) == -1)
			{// 余额不足
				result.setCode(ErrorCode.ERROR_CAPITAL_BALANCE_INSUFFICIENT.getCode());
				result.setError(ErrorCode.ERROR_CAPITAL_BALANCE_INSUFFICIENT.getMessage());
				return result;
			}

			cwq.setAuditTime(new Date());
			cwq.setStatus((byte) 1);
			cwq.setAuditor(operationName);

			
			// TODO 生成支付明细
			CapitalIncomeExpenses incomeExpenses = new CapitalIncomeExpenses();
			incomeExpenses.setAmount(cwq.getAmount());
			incomeExpenses.setCashBalance(mMCapitalPersonalAssetsService.selectByMid(cwq.getMid()).getCashBalance());
			incomeExpenses.setChannel(cwq.getChannel());
			incomeExpenses.setCreateTime(new Date());
			incomeExpenses.setDirection(CapitalIncomeExpenseService.DIRECTION_EXPENSES);
			incomeExpenses.setItems(CapitalIncomeExpenseService.ITEMS_WITHDRAW);
			incomeExpenses.setMid(cwq.getMid());
			incomeExpenses.setNo(AccountUtil.generateNo());
			incomeExpenses.setRemark("提现");
			incomeExpenses.setStatus(CapitalIncomeExpenseService.STATUS_PAYING);
			mCapitalIncomeExpenseService.insert(incomeExpenses);

			// pingxx 付款
			Transfer transfer = null;
			if (cwq.getChannel() == CapitalWithdrawRequestService.CHANNEL_WX_PUB ||
					cwq.getChannel() == CapitalWithdrawRequestService.CHANNEL_WX)
			{
				Member member = mMemberService.selectMemberByMid(incomeExpenses.getMid());
				transfer = pingxxService.wxTransfer(incomeExpenses.getNo(), (int) (incomeExpenses.getAmount().doubleValue() * 100), member.getOpenid(), "用户余额提现", member.getNickname());
			}
			if(transfer==null || transfer.getStatus().equals("failed"))
			{
				//提现失败，修改明细状态
				CapitalIncomeExpenses cie = new CapitalIncomeExpenses();
    			cie.setIeid(incomeExpenses.getIeid());
    			incomeExpenses.setCashBalance(mMCapitalPersonalAssetsService.selectByMid(cwq.getMid()).getCashBalance());
    			cie.setStatus(CapitalIncomeExpenseService.STATUS_FAILED);
       			cie.setFinishTime(new Date());
    			mCapitalIncomeExpenseService.update(cie);
    			
				result.setCode(ErrorCode.ERROR_PINGXX_TRANSFER.getCode());
				result.setError(transfer.getFailureMsg());
				return result;
			}else{
				// 减掉余额
				mMCapitalPersonalAssetsService.transCashBalance(mpa.getPaid(), cwq.getAmount().negate());
				// 加提现总金额
				mMCapitalPersonalAssetsService.transWithdrawAmount(mpa.getPaid(), cwq.getAmount());
				// 更新平台用户提现总额
				mCapitalPlatformCashService.transWithdrawAmount(cwq.getAmount());
				//更新申请状态
				mCapitalWithdrawRequestService.update(cwq);
			}
		} catch (Exception e)
		{
			result.setCode(ErrorCode.ERROR_SYSTEM_ERROR.getCode());
			result.setError(ErrorCode.ERROR_SYSTEM_ERROR.getMessage());
			log.debug(e.toString());
		}
		return result;
	}
	
	/**
	 * 操作人列表
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listOperator.json")
	@ResponseBody
	public Object listOperator(HttpServletRequest request)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		
		// 设置查询条件
		SystemUserInfo querySystemUserInfo = new SystemUserInfo();
		querySystemUserInfo.setIsEnable(new Byte("0"));

		// 查询操作人列表
		List<SystemUserInfo> sysUserList = systemUserInfoService.selectSelective(querySystemUserInfo);
		List<WebSystemUserInfoDTO> sysUserDTOList = new ArrayList<WebSystemUserInfoDTO>();
		for (SystemUserInfo systemUserInfo : sysUserList)
		{
			WebSystemUserInfoDTO webSystemUserInfoDTO = new WebSystemUserInfoDTO();
			BeanUtils.copyProperties(systemUserInfo, webSystemUserInfoDTO);
			List<String> privilegesMenuList = new ArrayList<String>();
			if (systemUserInfo != null && systemUserInfo.getDptId() != null)
			{
				// 查询部门信息
				SystemDepartment systemDepartment = systemDepartmentService.selectByPrimaryKey(systemUserInfo.getDptId());
				webSystemUserInfoDTO.setDptName(systemDepartment.getDepName());
				

				webSystemUserInfoDTO.setPrivilegesMenuList(privilegesMenuList);
			}
			
			sysUserDTOList.add(webSystemUserInfoDTO);
		}

		// 返回列表
		result.put("list", sysUserDTOList);

		return result;
	}


	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
}
