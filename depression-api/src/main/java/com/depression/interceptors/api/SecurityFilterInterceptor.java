package com.depression.interceptors.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.depression.entity.Constant;
import com.depression.entity.ResultEntity;

/**
 * 会话安全拦截器，限定特定接口需要登陆
 * 
 * @author fanxinhui
 * 
 */
public class SecurityFilterInterceptor extends HandlerInterceptorAdapter
{
	Logger log = Logger.getLogger(this.getClass());

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链, 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
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

		Object userId = request.getSession().getAttribute(Constant.LOGINED);
		if (userId == null)
		{
			log.error("Interceptor：用户未登陆或会话失效!");
			result.setCode(ResultEntity.SESSION_EXPIRED_ERROR);
			result.setMsg("用户未登陆或会话失效!");
			// 将实体对象转换为JSON Object转换
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
			return true;
	}

}
