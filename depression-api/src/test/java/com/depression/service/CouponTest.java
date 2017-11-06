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

import com.depression.dao.UbViewHomepageMapper;
import com.depression.model.Member;
import com.depression.model.UbViewHomepage;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class CouponTest
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
    @Autowired
    UbViewHomepageMapper ubViewHomepageMapper;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void bestowalCoupon() throws Exception{
    	ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    	BufferedReader bf = null;
		try {
			Resource[] resources=resolver.getResources("classpath*:" + "cdb_list.txt");
			String filePath = resources[0].getURI().getPath();
			FileInputStream in = new FileInputStream(filePath);
	        InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
	        bf = new BufferedReader(inReader);
	        String line;
	        Integer mc = 0;
	        Integer pc = 0;
	        do{
	        	line = bf.readLine();
	        	if(line!=null && line.length() == 11)
	        	{//合法手机号
	        		pc++;
	        		Member member = memberService.selectMemberByMobilePhone(line);
	        		if(member != null && member.getUserType()==1)
	        		{//普通用户
	        			mc ++;
	        			Date today = new Date();
	        			Calendar calendar = Calendar.getInstance();
	        			calendar.add(Calendar.MONTH, 1);
	        			Date nextMonthToday = calendar.getTime();
	        			//capitalCouponService.bestowalCoupon(member.getMid(), 2L, today, nextMonthToday);
	        		}
	        	}
	        }while(line != null);
	        System.out.println(pc);
	        System.out.println(mc);
		} catch (IOException e) {

		}finally{
			bf.close();
		}
    }

	@Test
	public void bestowalCouponAll() throws Exception{

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
        		if(m != null && m.getUserType()==1)
        		{//普通用户
        			mc ++;
        			Date today = new Date();
        			Calendar calendar = Calendar.getInstance();
        			calendar.add(Calendar.DAY_OF_MONTH, 7);
        			Date nextWeekToday = calendar.getTime();
        			capitalCouponService.bestowalCoupon(m.getMid(), 5L, today, nextWeekToday);
        		}
        	}
        }
        System.out.println(mc);

	}
	
	
	@Test
	public void getLatest(){
		UbViewHomepage ubViewHomepage=new UbViewHomepage();
		ubViewHomepage.setViewFrom(2l);
		ubViewHomepage.setViewTo(3l);
		List<UbViewHomepage> list=ubViewHomepageMapper.getLatestUbViewHomepage(ubViewHomepage);
		
		System.out.println(list.size());
	}
}
