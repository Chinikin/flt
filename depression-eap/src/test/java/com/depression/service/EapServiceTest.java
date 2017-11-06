package com.depression.service;


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
public class EapServiceTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private EapService eapService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void isValidEapEmployee() throws Exception{
    	try{
	    	System.out.println(eapService.isValidEapEmployee(8443L));
	    	System.out.println(eapService.isValidEapEmployee(8444L));
	    	System.out.println(eapService.isValidEapEmployee(8460L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void isValidEapPsycho() throws Exception{
    	try{
	    	System.out.println(eapService.isValidEapPsycho(7022L));
	    	System.out.println(eapService.isValidEapPsycho(7023L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void isEmployeeServedByPsycho() throws Exception{
    	try{
	    	System.out.println(eapService.isEmployeeServedByPsycho(8443L, 7022L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void obtainServiceStatusAndEeId() throws Exception{
    	try{
	    	System.out.println(eapService.obtainServiceStatusAndEeId(8443L, 7022L));
	    	System.out.println(eapService.obtainServiceStatusAndEeId(8443L, 7023L));
	    	System.out.println(eapService.obtainServiceStatusAndEeId(8460L, 7023L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void obtainInvalidEeIds4EapEmployee() throws Exception{
    	try{
	    	System.out.println(eapService.obtainInvalidEeIds4EapEmployee(8443L));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

}
