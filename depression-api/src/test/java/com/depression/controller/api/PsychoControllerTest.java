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
public class PsychoControllerTest
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
    public void getMember() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/getMember.json")
    			.param("id", "8462")
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
    public void getAllMember() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/getAllMember.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("pLevel", "0")
    			.param("sortMode", "0")
    			.param("mid", "8460")
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
    public void getMemberListByTag() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/getMemberListByTag.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("tagId", "1")
    			.param("pLevel", "0")
    			.param("sortMode", "0")
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
    public void getSortMode() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/getSortMode.json")
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
    public void getEapPsychos() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/getEapPsychos.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("sortMode", "5")
    			.param("mid", "8460")
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
    public void obtainPsychoData() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/obtainPsychoData.json")
    			.param("mid", "7475")
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
    public void modifyPsychoData() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/modifyPsychoData.json")
    			.param("mid", "7475")
    			.param("avatar", "images/thumbnail/20160820/2995676749841408.png")
    			.param("avatarThumbnail", "images/preview/20160820/2995676749841408-300x388.png")
    			.param("candidPhoto", "images/thumbnail/20160820/2995676560393216.png")
    			.param("caseHours", "123")
    			.param("degree", "博士")
    			.param("email", "feyeye@foxmail.com")
    			.param("hobby", "游泳")
    			.param("location", "心猫")
    			.param("motto", "独来独往")
    			.param("primaryDomain", "{primaryDomain:['抑郁','心情']}")
    			.param("profile", "介绍")
    			.param("school", "杭州大学")
    			.param("title", "心理打卡")
    			.param("trainingExperience", "心理打卡")
    			.param("workYears", "8")
    			.param("wxAccount", "feyeye")
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
    public void obtainEvaluations4Psycho() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/obtainEvaluations4Psycho.json")
    			.param("pid", "8489")
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
    public void obtainEvaluationStatis4Psycho() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/obtainEvaluationStatis4Psycho.json")
    			.param("pid", "5904")
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
    public void obtainPsychoTagListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/obtainPsychoTagList.json")
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
	public void obtainPsychoTagListAndLicenseListV1() throws Exception{
		MvcResult mvcResult = 
		mockMvc.perform(post("/Psychor/V1/obtainPsychoTagListAndLicenseList.json")
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
	public void searchPsychoV1() throws Exception{
		MvcResult mvcResult = 
		mockMvc.perform(post("/Psychor/V1/searchPsycho.json")
				.param("pageIndex", "1")
				.param("pageSize", "100")
				.param("pLevel", "0")
				.param("sortMode", "1")
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
