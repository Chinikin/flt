package com.depression.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
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
import com.depression.dao.MemberUpdateMapper;
import com.depression.model.MemberUpdate;
import com.depression.service.QiniuService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class HeartmateControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MemberUpdateMapper updateMapper;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void restructUpdateData() throws Exception{
    	MemberUpdate mu = new MemberUpdate();
    	List<MemberUpdate> mus = updateMapper.selectSelective(mu);
    	for(MemberUpdate m : mus)
    	{
    		m.setIsAnony((byte) (m.getUpdateType()==0?1:0));
    		if(m.getEmbraceNum()==null)
    		{
    			m.setEmbraceNum(0L);
    		}
    		updateMapper.updateByPrimaryKeySelective(m);
    	}
    }
    
    @Test
    public void publishUpdate() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/publishUpdate.json")
    			.param("mid", "1")
    			.param("content", "内容")
    			.param("writeLocation", "杭州")
    			.param("imgsJsn", "['a.jpg','b.jpg']")
    			.param("isAnony", "1")
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
    public void searchUpdateListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/searchUpdateList.json")
    			.param("vid", "5906")
    			.param("type", "1")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("releaseFrom", "8")
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
    public void obtainUpdateListForUserV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/obtainUpdateListForUser.json")
    			.param("vid", "9463")
    			.param("mid", "9463")
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
    public void obtainUpdateDetailV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/obtainUpdateDetail.json")
    			.param("vid", "1")
    			.param("updateId", "266")
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
    public void publishCommentV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/publishComment.json")
    			.param("updateId", "266")
    			.param("parentId", "0")
    			.param("mid", "4460")
    			.param("commentContent", "内容")
    			.param("writeLocation", "杭州")
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
    public void obtainCommentListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/obtainCommentList.json")
    			.param("updateId", "266")
    			.param("vid", "1")
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
    public void embraceUpdate() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/embraceUpdate.json")
    			.param("updateId", "266")
    			.param("vid", "1")
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
    public void unembraceUpdate() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Heartmate/V1/unembraceUpdate.json")
    			.param("updateId", "266")
    			.param("vid", "1")
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
