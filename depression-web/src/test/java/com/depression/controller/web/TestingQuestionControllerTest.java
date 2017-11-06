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
import com.depression.service.MessagePushService;
import com.depression.service.QiniuService;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class TestingQuestionControllerTest
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
    public void saveTesting() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestions/save.json")
    			.param("calcMethod", "0")
    			.param("contentExplain", "测试是是是是是是是是是是是是是是是是是是是是是")
    			.param("subtitle", "测试副标题的得得得得")
    			.param("thumbnail", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("thumbnailMobile", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("thumbnailSlide", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("title", "这是一个标题")
    			.param("typeId", "2")
    			.param("questionsTitle", "测sdsdsgfsdsdf得得得")
    			.param("seqNum", "9")
    			.param("tesingTitle", "测试标题踢踢踢踢踢踢踢踢")
    			.param("jsonArrForOPts", "[{\"optionsId\":2,\"score\":12,\"title\":\"测试标题0\"},{\"optionsId\":2,\"score\":12,\"title\":\"测试标题1\"}]")
    			
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
    public void listTestingQuestions() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestions/list.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "15")
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
    public void listTestingQuestionsByTestingId() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestions/listByTestingId.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "15")
    			.param("testingId", "1")
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
    public void updateTesting() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestions/update.json")
    			.param("calcMethod", "0")
    			.param("contentExplain", "测试是是是是是是是是是是是是是是是是是是是是是")
    			.param("subtitle", "更新测试副标题的得得得得")
    			.param("thumbnail", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("thumbnailMobile", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("thumbnailSlide", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("title", "这是一个标题")
    			.param("typeId", "2")
    			.param("questionsTitle", "更新测试百题得得得得得")
    			.param("seqNum", "9")
    			.param("tesingTitle", "更新标题踢踢踢踢踢踢踢踢")
    			.param("jsonArrForOPts", "[{\"optionId\":2935,\"score\":12,\"title\":\"更新标题0\"},{\"score\":12,\"title\":\"更新标题0\"}]")
    			.param("questionsId", "799")
    			.param("testingId", "76")
    			
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
    public void changeStatus() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestions/changeStatus.json")
    			.param("questionsId", "[796,797]")
    			.param("isDel", "1")
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
    public void view() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestions/view.json")
    			.param("questionsId", "795")
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    	JSONObject jsonObject = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
    }
    
    
}
