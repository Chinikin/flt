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
public class MemberControllerTest
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
    public void searchMemberTest() throws Exception{
    	MvcResult mvcResult =
    	mockMvc.perform(post("/depression/member/searchMember.json")

    			.param("words", "蔡")
    			.param("pageIndex", "0")
    			.param("pageSize", "10")
    			.param("hasMobile", "0")
    			.param("begin", "2016-12-12 00:00:00")
    			.param("end", "2017-03-12 00:00:00")
    			.param("regTimeDirection", "0")
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
    public void getMemberListByTimeSliceTest() throws Exception{
    	MvcResult mvcResult =
    	mockMvc.perform(post("/depression/member/getMemberListByTimeSlice.json")
    			.param("begin", "2016-01-01 00:00:00")
    			.param("end", "2016-09-01 00:00:00")
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
    public void getAllMemberTest() throws Exception{
    	MvcResult mvcResult =
    	mockMvc.perform(post("/depression/member/getAllMember.json")
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
    public void addMember() throws Exception{
    	MvcResult mvcResult =
    	mockMvc.perform(post("/depression/member/addMember.json")
    			.param("mobilePhone", "13325647895")
    			.param("nickname", "hhh")
    			.param("userPassword", "123456")
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
    public void updateMember() throws Exception{
    	MvcResult mvcResult =
    	mockMvc.perform(post("/depression/member/addMember.json")
    			.param("mobilePhone", "13325647895")
    			.param("nickname", "zhangsan33")
    			.param("userPassword", "123456")
    			.param("mid", "9503")
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
    	mockMvc.perform(post("/depression/member/getAllMember.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "5")
    			.param("byRegTime", "1")
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
    public void getMember() throws Exception{
    	MvcResult mvcResult =
    	mockMvc.perform(post("/depression/member/getMember.json")
    			.param("mobilePhone", "18371026631")
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
