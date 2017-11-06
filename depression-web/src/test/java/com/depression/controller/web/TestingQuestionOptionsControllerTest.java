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
public class TestingQuestionOptionsControllerTest
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
    	mockMvc.perform(post("/testingQuestionsOptions/save.json")
    			.param("jumpToQuestionNo", "2")
    			.param("contentExplain", "测试是是是是是是是是是是是是是是是是是是是是是")
    			.param("subtitle", "测试副标题的得得得得")
    			.param("optType", "0")
    			.param("sequence", "2")
    			.param("score", "22")
    			.param("title", "option标题")
    			.param("typeId", "2")
    			.param("questionsId", "797")
    			.param("QuestionNo", "9")
    			.param("tesingTitle", "测试标题踢踢踢踢踢踢踢踢")
    			.param("jumpResultTag", "A")
    			
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
    public void listtestingQuestionsOptions() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestionsOptions/list.json")
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
    public void listtestingQuestionsOptionsByTestingId() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testingQuestionsOptions/listByQuestionsId.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "15")
    			.param("questionsId", "797")
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
    	mockMvc.perform(post("/testingQuestionsOptions/update.json")
    			.param("jumpToQuestionNo", "2")
    			.param("contentExplain", "测试是是是是是是是是是是是是是是是是是是是是是")
    			.param("subtitle", "测试副标题的得得得得")
    			.param("optType", "0")
    			.param("sequence", "2")
    			.param("score", "82")
    			.param("title", "sdsffgfd")
    			.param("typeId", "2")
    			.param("questionsId", "797")
    			.param("QuestionNo", "9")
    			.param("tesingTitle", "aaaaaaaaaa")
    			.param("jumpResultTag", "B")
    			.param("optionsId", "2949")
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
    	mockMvc.perform(post("/testingQuestionsOptions/changeStatus.json")
    			.param("optionsId", "[2950,2951]")
    			.param("isDel", "0")
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
    	mockMvc.perform(post("/testingQuestionsOptions/view.json")
    			.param("optionsId", "2942")
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
