package com.depression.controller.api;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.depression.entity.Constant;
import com.depression.entity.ResultEntity;

@Controller
@RequestMapping("/main")
public class TestController
{
    Logger log = Logger.getLogger(this.getClass());
    
    @RequestMapping(method = RequestMethod.GET, value = "/serverStatus.json")
    @ResponseBody
    public Object serverStatus(HttpSession session, HttpServletRequest request)
    {
    	ResultEntity result = new ResultEntity();
    	log.info("Server is running!");
    	result.put("apiServerStatus", "running");
    	result.put("serverTime", new Date().toLocaleString());
        return result;
    }
    
    @RequestMapping(value = "/weixinTest.json")
    @ResponseBody
    public Object weixinTest(HttpSession session, HttpServletRequest request)
    {
    	ResultEntity result = new ResultEntity();
    	log.info("Weixin reponse!");
    	Object loginId = session.getAttribute(Constant.LOGINED);
    	if (loginId!=null)
    	{
    		log.info("Weixin user login success!");
    	}
        return result;
    }
}
