package com.depression.controller.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.web.dto.WebSystemOperationLogDTO;
import com.depression.model.web.vo.WebSystemOperationLogVO;
import com.depression.service.SystemOperationLogService;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/operationLog")
public class SystemOperationLogController
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private SystemOperationLogService systemOperationLogService;

	@RequestMapping(value = "/listLogs.json")
	@ResponseBody
	public Object listLogs(HttpServletRequest request, @RequestParam(value = "pageIndex", required = false)Integer pageIndex, @RequestParam(value = "pageSize", required = false)Integer pageSize, @RequestParam(value = "dptId", required = false)Long dptId, @RequestParam(value = "mobilePhone", required = false)String mobilePhone, @RequestParam(value = "showName", required = false)String showName, @RequestParam(value = "begin", required = false)Date begin, @RequestParam(value = "end", required = false)Date end)
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());

		// 参数检查
		if (PropertyUtils.examineOneNull(pageIndex, pageSize))
		{
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}

		// 设置查询条件
		WebSystemOperationLogVO webSystemOperationLogVO = new WebSystemOperationLogVO();
		if (pageIndex != null)
		{
			webSystemOperationLogVO.setPageIndex(pageIndex);
		}
		if (pageSize != null)
		{
			webSystemOperationLogVO.setPageSize(pageSize);
		}
		if (dptId != null)
		{
			webSystemOperationLogVO.setDptId(dptId);
		}
		if (mobilePhone != null)
		{
			webSystemOperationLogVO.setMobilePhone(mobilePhone);
		}
		if (mobilePhone != null)
		{
			webSystemOperationLogVO.setMobilePhone(mobilePhone);
		}

		// 查询列表和总数
		List<WebSystemOperationLogDTO> operationLogList = systemOperationLogService.selectOperationLogAndUserInfoWithPage(webSystemOperationLogVO);
		int count = systemOperationLogService.countOperationLogAndUserInfo(webSystemOperationLogVO);

		// 返回列表
		result.put("list", operationLogList);
		result.put("count", count);

		return result;
	}
}
