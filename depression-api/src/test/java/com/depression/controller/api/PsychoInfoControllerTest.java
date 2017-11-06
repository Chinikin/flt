package com.depression.controller.api;


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
public class PsychoInfoControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private QiniuService pictureManagerService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void getLicenseTypeList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/getLicenseTypeList.json")
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
    public void newPsychoInfo() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/newPsychoInfo.json")
    			.param("name", "name")
    			.param("sex", "0")
    			.param("mobilePhone", "13812341234")
    			.param("wxAccount", "13812341234")
    			.param("location", "location")
    			.param("title", "title")
    			.param("ltid", "1")
    			.param("photoCertificationRel", "/photoCertificationRel")
    			.param("photoCertificationPreviewRel", "/photoCertificationPreviewRel")
    			.param("photoIdentityCardRel", "/photoIdentityCardRel")
    			.param("photoIdentityCardPreviewRel", "/photoIdentityCardPreviewRel")
    			.param("photoCandidRel", "/photoCandidRel")
    			.param("photoCandidPreviewRel", "/photoCandidPreviewRel")
    			.param("brief", "brief")
    			.param("primaryDomains", "['aa', 'bb']")
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