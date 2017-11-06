package com.depression.interceptors.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.depression.entity.Constant;
import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.SystemOperationLog;
import com.depression.model.eap.vo.EapUserVO;
import com.depression.service.EapUserService;
import com.depression.service.Permission;
import com.depression.service.SystemOperationLogService;
import com.depression.utils.WebUtil;

public class SecurityFilterInterceptor extends HandlerInterceptorAdapter
{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private SystemOperationLogService systemOperationLogService;
	@Autowired
	private EapUserService eapUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		ResultEntity result = new ResultEntity();
		result.setCode(ResultEntity.SUCCESS);
		log.info("==============preHandle================");
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
        boolean flag = false;
		//不拦截的请求
		if(requestUri.contains("eapUserLogin.json")||requestUri.contains("universalAccountLogin.json")||
				requestUri.contains("getSmsAuthCode.json")){
			return true;
		}
		log.info("requestUri:" + requestUri);
		log.info("contextPath:" + contextPath);
		log.info("url:" + url);  
		System.out.println(request.getSession().getId());
		Object userId = request.getSession().getAttribute(Constant.ADMIN_LOGINED);
		if (userId == null)
		{
			log.error("Interceptor：用户未登陆或会话失效!");
			result.setCode(ErrorCode.ERROR_NO_LOGIN.getCode());
			result.setMsg(ErrorCode.ERROR_NO_LOGIN.getMessage());
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
		} 
		Object repeatLogin = request.getSession().getAttribute(Constant.ADMIN_LEVEL);
		if(repeatLogin!=null&&repeatLogin.toString().equals("000000")){
			log.error("Interceptor：您已在别的地方登录，被迫退出");
			result.setCode(ErrorCode.ERROR_LOGIN_AGIAN.getCode());
			result.setMsg(ErrorCode.ERROR_LOGIN_AGIAN.getMessage());
			request.getSession().invalidate();
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
		}
		if(userId!=null)
		{		
			EapUserVO dto = eapUserService.selectByPrimaryKey(Long.valueOf(userId.toString()));
			String level = String.valueOf(dto.getUserType());
			if(level.equals("99")){}else{
				String authorityId = dto.getSpareChar();				
				//处理Permission Annotation，实现方法级权限控制  
		        HandlerMethod method = (HandlerMethod)handler;  
		        Permission permission = method.getMethodAnnotation(Permission.class);  		
		        
		        //如果为空在表示该方法不需要进行权限验证  
		        if (permission == null) {  
		            return true;  
		        }  
		        //验证是否具有权限  
		        if (!WebUtil.isPermission(authorityId, permission.value())) {  
		        	log.error("Interceptor：用户没有权限!");
					result.setCode(ErrorCode.ERROR_NO_PERMISSION.getCode());
					result.setMsg(ErrorCode.ERROR_NO_PERMISSION.getMessage());
				//	result.setRedirect(request.getContextPath()+"/index.jsp");
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
		        }  
		        return true;  
			}				
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
			flag = true;
		} 
		return  flag;
	}

}
