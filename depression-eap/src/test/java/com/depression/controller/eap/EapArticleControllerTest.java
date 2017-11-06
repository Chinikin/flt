package com.depression.controller.eap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
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
import com.depression.dao.EapEnterpriseMapper;
import com.depression.model.EapEmployee;
import com.depression.model.Member;
import com.depression.model.ServiceCustomerStatistics;
import com.depression.model.eap.dto.EapEmployeeDTO;
import com.depression.model.eap.vo.EapEmployeeVO;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberAdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.MemberUpdateService;
import com.depression.service.QiniuService;
import com.depression.service.ServiceStatisticsService;
import com.depression.service.TestingService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-*.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class EapArticleControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void searchArticle() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapArticle/searchArticle.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "1000")
    			.param("eeId", "32")
    			.param("isEnable", "0")
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
    public void blockXmArticle() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapArticle/blockXmArticle.json")
    			
    			.param("eeId", "8")
    			.param("articleIds", "[3,4,5]")
    			.param("isEnable", "0")
    			//.param("xmArticle", "1")
    			
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
    public void deleteArticle() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapArticle/deleteArticle.json")
    			.param("eeId", "8")
    			.param("articleIds", "[183,182]")
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
    public void getArticleDetail() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapArticle/getArticleDetail.json")
    			.param("articleId", "2")
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
    public void enableArticle() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapArticle/enableArticle.json")
    			.param("eeId", "8")
    			.param("articleIds", "[183,182]")
    			.param("isEnable", "1")
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
