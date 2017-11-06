package com.depression.interceptors.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.depression.entity.Constant;
import com.depression.entity.ResultEntity;
import com.depression.model.SystemOperationLog;
import com.depression.service.SystemOperationLogService;

public class SecurityFilterInterceptor extends HandlerInterceptorAdapter
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private SystemOperationLogService systemOperationLogService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		log.info("==============preHandle================");
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());

		log.info("requestUri:" + requestUri);
		log.info("contextPath:" + contextPath);
		log.info("url:" + url);

		Object userId = request.getSession().getAttribute(Constant.ADMIN_LOGINED);
		if (userId == null)
		{
			log.error("Interceptor：用户未登陆或会话失效!");
			result.setCode(ResultEntity.SESSION_EXPIRED_ERROR);
			result.setMsg("用户未登陆或会话失效!");
			JSONObject responseJSONObject = JSONObject.fromObject(result);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = null;
			try
			{
				out = response.getWriter();
				out.append(responseJSONObject.toString());
				log.error(responseJSONObject.toString());
			} catch (IOException e)
			{
				log.error(e);
			} finally
			{
				if (out != null)
				{
					out.close();
				}
			}
			return false;
		} else
		{
			SystemOperationLog systemOperationLog = new SystemOperationLog();
			systemOperationLog.setOperatorId(Long.parseLong(userId.toString()));
			if (url.contains("/depression/system/addUser.json"))
			{
				systemOperationLog.setOperationContent("用户管理-新增");
			} else if (url.contains("/depression/system/modifyUser.json"))
			{
				systemOperationLog.setOperationContent("用户管理-修改");
			} else if (url.contains("/depression/system/changeStatus.json"))
			{
				systemOperationLog.setOperationContent("用户管理-用户启用或禁用");
			} else if (url.contains("/privileges/core/updMenuList.json"))
			{
				systemOperationLog.setOperationContent("用户管理-修改权限");
			} else if (url.contains("/depression/system/resetPass.json"))
			{
				systemOperationLog.setOperationContent("用户管理-重置密码");
			} else if (url.contains("/CapitalCommissionRate/updateCapitalCommissionRate.json"))
			{
				systemOperationLog.setOperationContent("底层操作-更新平台佣金");
			} else if (url.contains("/CapitalWithdrawRequestDuration/updateCapitalWithdrawRequestsDuration.json"))
			{
				systemOperationLog.setOperationContent("底层操作-更新提现日期");
			} else if (url.contains("/CapitalWithdrawRequest/capitalWithdrawOperationByNo.json"))
			{
				systemOperationLog.setOperationContent("订单管理-结算");
			}
			systemOperationLog.setOperationTime(new Date());
			systemOperationLogService.insertSelective(systemOperationLog);
			return true;
		}
		 
	}
 
}
