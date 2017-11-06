package com.depression.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class MemberMoodRecordControllerTest
{
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void getDayMoodAndSportTest() throws Exception{
    	MvcResult mvcResult = 
    	    	mockMvc.perform(post("/MemberMoodRecord/getDayMoodAndSport.json").param("mid", "4020")
    	    			.param("day", "2016-07-07 00:00:00"))
    	    	.andReturn();
    	
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	JSONObject jsonObject = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
    }

}
