package com.depression.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.depression.model.web.vo.MessagePushVO;
import com.depression.service.MessagePushService;
import com.depression.service.QiniuService;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class MessagePushControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MessagePushService messagePushService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    
    @Test
    public void clickMsgPush() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/clickMsgPush.json")
    			.param("mpId", "58")
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    	JSONObject jsonObject = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void testIm() {
    	 JSONObject jo=new JSONObject();
    	 jo.put("img", "images/thumbnail/20170302/3270458257245184.jpg");
	   	 jo.put("innerContent", "常在河边站哪有不湿鞋，经常和女性暧昧怎么办？戳我看心理学家支招");
	   	 jo.put("pushLink", "xinmao://testing?testingId=16&tsType=1&calcMethod=1");
	   //jo.put("pushLink", "xinmao://psycho/10");
	   //jo.put("pushLink", "xinmao://article/5");
	   	 jo.put("linkText", "→李子勋教你应对出轨");
	   	 jo.put("mpId", "58");
	   	 jo.put("xmMessageType", MessagePushService.XM_CUSTOM_MESSAGE);
		 System.out.println(jo.toJSONString());
	   	 System.out.println(IMUtil.neteaseSendBatchMsg(
				"netease3270497340653568",
				"[\"netease3257378790965248\"]", 
				"100",
				jo.toJSONString(),
				null,
				null,
				null));
	}
    
    
    @Test
    public void sendMsg(){
	}
    
}
