package com.depression.controller.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
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
import com.depression.service.QiniuService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class EapEnterpriseControllerTest
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
    public void newEnterprise() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapEnterprise/newEnterprise.json")
    			.param("name", "心猫")
    			.param("contacts", "小白")
    			.param("contactsPhoneNum", "13712341234")
    			.param("logoRel", "/pic/logo.jpg")
    			.param("logoPreviewRel", "/pic/logo_preview.jpg")
    			.param("purchasedCashAmount", "500000")
    			.param("purchasedOrderAmount", "20000")
    			.param("orderSingleLimit", "6")
    			.param("serviceStartDate", "2016-12-16 00:00:00")
    			.param("serviceEndDate", "2017-12-16 00:00:00")
    			.param("pgId", "1")
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
    public void obtainEnterpriseList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapEnterprise/obtainEnterpriseList.json")
    			//.param("words", "猫")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
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
    public void obtainEnterprise() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapEnterprise/obtainEnterprise.json")
    			.param("eeId", "1")
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
    public void updateEnterprise() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapEnterprise/updateEnterprise.json")
    			.param("eeId", "1")
    			.param("name", "心猫")
    			.param("contacts", "小白猫")
    			.param("contactsPhoneNum", "13712341234")
    			.param("logoRel", "/pic/logo.jpg")
    			.param("logoPreviewRel", "/pic/logo_preview.jpg")
    			.param("purchasedCashAmount", "500000")
    			.param("purchasedOrderAmount", "20000")
    			.param("orderSingleLimit", "6")
    			.param("serviceStartDate", "2016-12-16 00:00:00")
    			.param("serviceEndDate", "2017-12-16 00:00:00")
    			.param("pgId", "1")
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
    public void enableEnterprise() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapEnterprise/enableEnterprise.json")
    			.param("ids", "1")
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
