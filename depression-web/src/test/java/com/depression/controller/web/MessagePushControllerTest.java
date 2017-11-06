package com.depression.controller.web;

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
import com.depression.service.IMMessageService;
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
    @Autowired
    private IMMessageService imMessageService;
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void cancelMsgPush() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/cancelMsgPush.json")
    			.param("mpId", "146")
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
    public void obtainMsgPushList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/obtainMsgPushList.json")
    			.param("title", "")
    			.param("pushStatus", "")
    			.param("pageIndex", "0")
    			.param("pageSize", "3")
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
    public void obtainMsgPush() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/obtainMsgPush.json")
    			.param("mpId", "62")
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
    public void saveMsgPush() throws Exception{
    	
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/saveMsgPush.json")
    			.param("content", "常在河边站哪有不湿鞋，经常和女性暧昧怎么办？戳我看心理学家支招")
    			.param("isAll", MessagePushService.IS_ALL+"")
    			.param("img", "images/preview/20170216/3250239815648256-300x533.jpg")
    			.param("linkText", "链接承接文字")
    			.param("pushLink", "http://www.baidu.com")
    			.param("pushTime", "2017-3-3 17:35:00")
    			.param("pushType", MessagePushService.PUSH_TYPE_ALL+"")
    			.param("title", "哈哈哈哈哈")
    			.param("preImg", "images/preview/20170216/3250239815648256-300x533.jpg")
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    	JSONObject jsonObject = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
    	Thread.sleep(600000);
    }
    
    
    @Test
    public void startMsgPush() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/startMsgPush.json")
    			.param("mpId", "147")
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    	JSONObject jsonObject = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
    	Thread.sleep(6000000);
    }
    
    
    @Test
    public void updateMsgPush() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/updateMsgPush.json")
    			.param("mpId", "146")
    			.param("pushLink", "http://www.120xinmao.com")
    			.param("content", "更新测试信息")
    			.param("pushType", "0")
    			.param("linkText", "更新推送信息")
    			.param("pushTime", "2017-03-16 10:29:00")
    			.param("img", "/images/preview/20170216/3250239815648256-300x533.jpg")
    			.param("isAll", "1")
    			.param("title", "更新测试")
    			.param("preImg", "更新测试")
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
    public void clickMsgPush() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/messagePush/clickMsgPush.json")
    			.param("mpId", "105")
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
	     //jo.put("pushLink", "xinmao://testing?testingId=16&tsType=1&calcMethod=1");
	   	 //jo.put("pushLink", "xinmao://article?articleId=5");
	   	 jo.put("direct", false);
	   	 //jo.put("pushLink", "xinmao://psycho?mid=6684");
	     jo.put("pushLink", "http://tech.qq.com/a/20170308/012260.htm");
	   	 jo.put("person", "");
	   	 jo.put("linkText", "→李子勋教你应对出轨");
	   	 jo.put("mpId",58);
	   	 jo.put("xmMessageType", MessagePushService.XM_CUSTOM_MESSAGE);
		 System.out.println(jo.toJSONString());
		 String str="{\"direct\":true,\"jo:"+jo.toJSONString()+"\"}";
		 System.out.println(str);
	  	 System.out.println(IMUtil.neteaseSendBatchMsg(
				"netease3277095716405248",
				"['netease3257379136225280']",
				//"['netease3257379338371072']",
				//"[\"netease3257378790965248\"]",  //netease3257379338371072 魏 netease3257378790965248 黄
				"100",
				jo.toJSONString(),
				"{push:true}",
				"sssss",
				"{'direct':false,'jo':"+jo.toJSONString()+"}"));
	   	 
	   	 
	   	/*imMessageService.neteaseSendBatchMsg((long) 144,jo.toJSONString(),MessagePushService.IM_CUSTOM_MESSAGE,"hahahaha","{push:true}","{'direct':false,'jo':''}",null);*/
	  	//imMessageService.neteaseSendBatchMsg("netease3277095716405248",jo.toJSONString(),MessagePushService.IM_CUSTOM_MESSAGE,"ssss","{push:true}","{'direct':false,'jo':''}",null);
		
	}
    
    
    @Test
    public void neteaseUserUpdateUinfo(){
    	IMUtil.neteaseUserUpdateUinfo("netease3277095716405248", "心猫团队", "http://api.120xinmao.com:8086/images/thumbnail/20170314/xinmao_logo_2x.png", null);
	}
    
}
