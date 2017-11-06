package com.depression.service;


import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.alibaba.fastjson.JSON;
import com.depression.dao.UbSearchWordsMapper;
import com.depression.model.Member;
import com.depression.model.UbSearchWords;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class MemberServiceTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MemberService memberService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void genNickname() throws Exception{
    	try{
	    	System.out.println(memberService.genNickname(1));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }

    @Test
    public void obtainIndustryList() throws Exception{
    	try{
	    	System.out.println(memberService.obtainIndustryList());
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void genPyschoMotto() throws Exception{
    	try{
	    	System.out.println(memberService.genPyschoMotto(1));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void genUserProfile() throws Exception{
    	try{
	    	System.out.println(memberService.genUserSignature(1));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void searchPsychsooV1() throws Exception{
    	try{
    		List<Long> tagIds = new ArrayList<Long>();
    		tagIds.add(28L);
    		tagIds.add(29L);
    		tagIds.add(31L);
    		tagIds.add(34L);
    		List<Long> licenseIds = new ArrayList<Long>();
    		licenseIds.add(1L);
    		licenseIds.add(2L);
    		licenseIds.add(3L);
    		licenseIds.add(4L);
    		List<Long> degreeIds = new ArrayList<Long>();
    		degreeIds.add(1L);
    		degreeIds.add(2L);
    		degreeIds.add(3L);
    		List<Member> members = memberService.searchPsychoV1(1, 3, //分页
    											(byte)0,//pLevel
    											(byte)7,//sortMode
    											"杭州",//city
    											tagIds,//tagIds
    											licenseIds,//licenseIds
    											degreeIds,//degreeIds
    											100,//priceFloor
    											200,//priceCeil
    											(byte) 1,//sex
    											null
    											
    				);
    		String json = JSON.toJSONString(members);
	    	System.out.println(json);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    
    @Autowired
	UbSearchWordsMapper ubSearchWordsMapper;
	
    @Test
	public void insertUbSearchWords(){
		UbSearchWords sw=new UbSearchWords();
		sw.setCreateTime(new Date());
		sw.setMid(4460l);
		sw.setIsDelete((byte)0);
		sw.setWords("好烦啊啊啊啊啊啊");
		ubSearchWordsMapper.insert(sw);
	}
}
