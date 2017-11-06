package com.depression.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
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
import com.depression.dao.TestingMapper;
import com.depression.model.Page;
import com.depression.model.TestingType;
import com.depression.service.MessagePushService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class TestingControllerTest
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
    private TestingMapper testingMapper;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void saveTesting() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/save.json")
    			.param("calcMethod", "0")
    			.param("contentExplain", "测试是是是是是是是是是是是是是是是是是是是是是")
    			.param("subtitle", "测试副标题的得得得得")
    			.param("thumbnail", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("thumbnailMobile", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("thumbnailSlide", "images/thumbnail/20160715/2945119615321088.jpg")
    			.param("title", "这是一个标题")
    			.param("typeId", "2")
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
    public void listTesting() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/list.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "15")
    			.param("typeId", "101")
    			.param("mid", "10000074613")
    			.param("tsType", "1")
    			
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
    public void getTestedTestingAl1l() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/getTestedTestingAll.json")
    			.param("calcMethod", "0")
    			
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
    public void getTestedTestingAll() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/getTestedTestingAll.json")
    			.param("mid", "4462")
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
    public void getTestedTesting() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/getTestedTesting.json")
    			.param("mid", "6830")
    			.param("testingId", "33")
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
    	mockMvc.perform(post("/testing/view.json")
    			.param("mid", "6830")
    			.param("testingId", "33")
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
    public void getHotTesting() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/getHotTesting.json")
    			.param("size", "10")
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
    public void getHotTestingForStuTest() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/getHotTestingForStuTest.json")
    			.param("size", "10")
    			.param("tempMid", "6830")
    			
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
    public void getTestingByTsType() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/getTestingByTsType.json")
    			.param("tsType", "1")
    			.param("mid", "6830")
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
    public void getTestingByTypeId() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/testing/getTestingByTypeId.json")
    			.param("typeId", "1")
    			.param("mid", "9506")
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
    
    //数据迁移函数
    
    @Test
    public void getTestingListByTypeId() throws Exception{
    	
    	List<TestingType> list=new ArrayList<TestingType> ();
    	TestingType t=new TestingType();
    	t.setTypeId(6l);
    	
    	list.add(t);
    	TestingType t1=new TestingType();
    	t1.setTypeId(7l);
    	
		List<Long> typeIds=new ArrayList<Long>();
		for(TestingType testingType: list){
			typeIds.add(testingType.getTypeId());
		}
		Page page=new Page();
		page.setPageIndex(1);
		page.setPageSize(10);
		System.out.println(testingMapper.getPageListByTypeIds(typeIds,page.getPageStartNum(),page.getPageSize()).size());
    }
    
}
