package com.depression.controller.eap;


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
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-*.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class EapAdBannerControllerTest
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
    public void save() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapAdBanner/save.json")
    			.param("bannerTitle", "今天的天气真是好啊啊啊")
    			.param("insideLinkId", "8")
    			.param("insideLinkType", "1")
    			.param("linkType", "0")
    			//.param("outsideLink", "hshsshdddddd")
    			.param("picPath", "Fhn2TCxCBEB3L69K8pem8L87nh_G")
    			.param("showLocation", "1")
    			.param("sorting", "38")
    			.param("releaseFrom", "8")
    			
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
    public void getPsychoByName() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapAdBanner/getPsychoByName.json")
    			.param("name", "郑")
    			.param("eeId", "8")
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
    public void getArticleByTitle() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapAdBanner/getArticleByTitle.json")
    			.param("title", "测")
    			.param("eeId", "8")
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
    public void list() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapAdBanner/list.json")
    			.param("showLocation", "0")
    			.param("releaseFrom", "32")
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
    public void update() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapAdBanner/update.json")
    			.param("bannerId", "128")
    			.param("bannerTitle", "修改標題")
    			.param("releaseFrom", "37")
    			.param("showLocation", "0")
    			.param("sorting", "1")
    			.param("insideLinkId", "1")
    			.param("insideLinkType", "0")
    			.param("linkType", "1")
    			.param("outsideLink", "dffdsfd")
    			.param("picPath", "ss")
    			.param("eeId", "8")
    			
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
    	mockMvc.perform(post("/EapAdBanner/view.json")
    			.param("bannerId", "68")
    			.param("releaseFrom", "8")
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
    public void getTestingByTitle() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapAdBanner/getTestingByTitle.json")
    			.param("title", "心理")
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
