package com.depression.utils;


import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
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
import com.depression.entity.VoipUser;
import com.depression.model.Member;
import com.depression.model.NeteaseUinfoEx;
import com.depression.service.MemberService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class ImTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired   
    MemberService memberService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void neteaseUserCreate() throws Exception{
    	try{
    		System.out.println(IMUtil.neteaseUserCreate("心猫团队", "icon", null));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void neteaseUserUpdateUinfo() throws Exception{
    	try{
    		System.out.println(IMUtil.neteaseUserUpdateUinfo("netease3250553230491648", "cai", "icon", "{'a':'c', 'b':1}"));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void neteaseUserUpdate() throws Exception{
    	try{
    		System.out.println(IMUtil.neteaseUserUpdate("netease3256278198059008", "{'a':'c', 'b':1}"));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void neteaseUserGetUinfo() throws Exception{
    	try{
    		System.out.println(IMUtil.neteaseUserGetUinfos("netease3257378991898624"));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void migrateImAccount() throws Exception{
    	int pageIndex = 1;
    	int pageSize = 1000;
    	while(true)
    	{
	    	Member mq = new Member();
	    	mq.setPageIndex(pageIndex);
	    	mq.setPageSize(pageSize);
	    	List<Member> members = memberService.selectByPage(mq);
	    	
	    	if(members.size()==0)
	    	{
	    		break;
	    	}
	    	
	    	pageIndex++;
	    	
	    	for(Member m : members)
	    	{
	    		//检查网易im
				if(m.getImAccount()!=null && !IMUtil.neteaseIsAccid(m.getImAccount()))
				{//未注册
					// 注册voip账号
					String icon = m.getAvatar()==null?"": m.getAvatar();

					VoipUser voipUser = IMUtil.neteaseUserCreate(m.getNickname(), icon, null);
					if (null == voipUser)
					{
						m.setImAccount("");
						m.setImPsw("");
					}else
					{
						m.setImAccount(voipUser.getVoipAccount());
						m.setImPsw(voipUser.getVoipPwd());
					}
					
					NeteaseUinfoEx uinfoEx = new NeteaseUinfoEx();
					uinfoEx.setMid(m.getMid());
					uinfoEx.setUserType(m.getUserType()==null?1:m.getUserType());
					uinfoEx.setTitle(m.getTitle()==null?"":m.getTitle());
					IMUtil.neteaseUserUpdateUinfo(m.getImAccount(), null, null, JSON.toJSONString(uinfoEx));
					
					Member mu = new Member();
					mu.setMid(m.getMid());
					mu.setImAccount(m.getImAccount());
					mu.setImPsw(m.getImPsw());
					memberService.update(mu);
				}
	    	}
    	}
    }

	@Test
	public void migrateImIcon() throws Exception{
		int pageIndex = 1;
		int pageSize = 1000;
		while(true)
		{
	    	Member mq = new Member();
	    	mq.setPageIndex(pageIndex);
	    	mq.setPageSize(pageSize);
	    	List<Member> members = memberService.selectByPage(mq);
	    	
	    	if(members.size()==0)
	    	{
	    		break;
	    	}
	    	
	    	pageIndex++;
	    	
	    	for(Member m : members)
	    	{
	    		//检查网易im
				if(IMUtil.neteaseIsAccid(m.getImAccount()))
				{//未注册
					// 注册voip账号
					String icon = m.getAvatar()==null?"": m.getAvatar();	
					while(true)
					{
						int ret = IMUtil.neteaseUserUpdateUinfo(m.getImAccount(), null, icon, null);		
						if(ret == 0)
						{
							break;
						}else
						{
							Thread.sleep(1000);
						}
					}
				}
	    	}
		}
	}
}
