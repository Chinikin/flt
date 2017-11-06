package com.depression.utils;


import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class ComuniTest
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
    public void accountRegister() throws Exception{
    	try{
    		System.out.println(IMUtil.ronglianAccountRegister("acdef"));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void ronglianCallService() throws Exception{
    	try{
    		System.out.println(CallCommunicationUtil.ronglianCallService("13867451910", "18957939770", 180).getCallsid());
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void ucpaasCallService() throws Exception{
    	try{
    		System.out.println(CallCommunicationUtil.ucpaasCallService("13867451910", "15669902109", 240).getCallsid());
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
