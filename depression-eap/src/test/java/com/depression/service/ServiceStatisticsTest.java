package com.depression.service;


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
public class ServiceStatisticsTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ServiceStatisticsService serviceStatisticsService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void getOrCreatePsychoStat() throws Exception{
    	try{
	    	System.out.println(serviceStatisticsService.getOrCreatePsychoStat(7022L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void getOrCreateCustomerStat() throws Exception{
    	try{
	    	System.out.println(serviceStatisticsService.getOrCreateCustomerStat(8443L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void updatePsychoDuration() throws Exception{
    	try{
	    	System.out.println(serviceStatisticsService.updatePsychoDuration(7022L, 60L, 1));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void updatePsychoScore() throws Exception{
    	try{
	    	System.out.println(serviceStatisticsService.updatePsychoScore(7022L, 5, 1));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void updateCutomerDuration() throws Exception{
    	try{
	    	System.out.println(serviceStatisticsService.updateCutomerDuration(8443L, 60L, 1, 1L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void updateEnterpriseDuration() throws Exception{
    	try{
	    	System.out.println(serviceStatisticsService.updateEnterpriseDuration(60L, 1, 1L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void updateDuration() throws Exception{
    	try{
	    	System.out.println(serviceStatisticsService.updateDuration(8443L, 7022L, 60L, 1, 1L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
