package com.depression.service;


import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.depression.entity.ErrorCode;
import com.depression.model.Member;
import com.depression.utils.IMUtil;
import com.depression.utils.SmsUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class SmsTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CapitalCouponService capitalCouponService;
    @Autowired    
    MemberService memberService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
	public void smsToAll() throws Exception{

		Integer mc = 0;
		Integer pageIndex = 1;
        while(true)
        {
        	Member qm = new Member();
			qm.setUserType((byte) 1);
			qm.setPageIndex(pageIndex);
			pageIndex++;
			qm.setPageSize(1000);
			List<Member> members = memberService.selectByPage(qm);
			if(members.size() == 0) break;
				        	
        	for(Member m : members)
        	{//合法手机号	
        		if(m != null && m.getUserType()==1&&
        				m.getMobilePhone()!=null&&m.getMobilePhone().length()==11)
        		{//普通用户
        			mc ++;
        			SmsUtil.sendSms(m.getMobilePhone(), "158267");
        		}
        	}
        }
        System.out.println(mc);

	}
}
