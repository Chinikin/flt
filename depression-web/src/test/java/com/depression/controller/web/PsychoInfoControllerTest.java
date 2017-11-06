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
    			.param("photoCertificationRel", "/photoCertificationRel")
    			.param("photoCertificationPreviewRel", "/photoCertificationPreviewRel")
    			.param("photoIdentityCardRel", "/photoIdentityCardRel")
    			.param("photoIdentityCardPreviewRel", "/photoIdentityCardPreviewRel")
    			.param("photoCandidRel", "/photoCandidRel")
    			.param("photoCandidPreviewRel", "/photoCandidPreviewRel")
    			.param("brief", "brief")
    			.param("primaryDomains", "Domain1")
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
    public void getPsychoInfoList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/getPsychoInfoList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("auditStatus", "0")
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
    public void searchPsychoInfoList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/searchPsychoInfoList.json")
    			.param("words", "na")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("auditStatus", "0")
    			.param("createTimeDirection", "1")
    			.param("begin", "2016-9-12 00:00:00")
    			.param("end", "2016-9-14 00:00:00")
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
    public void getPsychoInfoByKey() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/getPsychoInfoByKey.json")
    			.param("piid", "1")
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
    public void modifyPsychoInfo() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/modifyPsychoInfo.json")
    			.param("piid", "1")
    			.param("name", "name")
    			.param("sex", "0")
    			.param("mobilePhone", "13812341234")
    			.param("wxAccount", "13812341234")
    			.param("location", "location")
    			.param("title", "title")
    			.param("photoCertificationRel", "/photoCertificationRel")
    			.param("photoCertificationPreviewRel", "/photoCertificationPreviewRel")
    			.param("photoCertificationDealtRel", "/photoCertificationDealtRel")
    			.param("photoCertificationDealtPreviewRel", "/photoCertificationDealtPreviewRel")
    			.param("photoIdentityCardRel", "/photoIdentityCardRel")
    			.param("photoIdentityCardPreviewRel", "/photoIdentityCardPreviewRel")
    			.param("photoIdentityCardDealtRel", "/photoIdentityCardDealtRel")
    			.param("photoIdentityCardDealtPreviewRel", "/photoIdentityCardDealtPreviewRel")
    			.param("photoCandidRel", "/photoCandidRel")
    			.param("photoCandidPreviewRel", "/photoCandidPreviewRel")
    			.param("brief", "brief")
    			.param("primaryDomains", "Domain1")
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
    public void deletePsychoInfoByKey() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/deletePsychoInfoByKey.json")
    			.param("piid", "1")
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
    public void auditPsychoInfo() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/PsychoInfo/auditPsychoInfo.json")
    			.param("piid", "1")
    			.param("auditStatus", "1")
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
    public void exportPsychoInfo() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/exportPsychoOrderList.json")
    			.param("pid", "22")
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
