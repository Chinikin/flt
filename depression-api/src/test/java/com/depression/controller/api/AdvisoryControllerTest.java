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
import com.depression.dao.MemberAdvisoryCommentMapper;
import com.depression.model.Member;
import com.depression.model.MemberAdvisoryComment;
import com.depression.service.AdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.QiniuService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class AdvisoryControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired    
    private MemberAdvisoryCommentMapper advisoryCommentMapper;
    @Autowired    
    private MemberService memberService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void restructAdvisoryCommentData()throws Exception{
    	MemberAdvisoryComment mac = new MemberAdvisoryComment();
    	List<MemberAdvisoryComment> comments = advisoryCommentMapper.selectSelective(mac);
    	for(MemberAdvisoryComment ac : comments)
    	{
    		Member member = memberService.selectMemberByMid(ac.getMid());
    		if(member!=null && member.getUserType()==2)
    		{
    			ac.setIsAnony((byte) 1);
    		}
    		
    		if(ac.getParentId() != 0)
    		{
    			mac = advisoryCommentMapper.selectByPrimaryKey(ac.getParentId());
    			if(mac == null)
    			{
    				continue;
    			}
    			if(mac.getAncestorId() == 0)
    			{
    				ac.setAncestorId(mac.getCommentId());
    			}else
    			{
    				ac.setAncestorId(mac.getAncestorId());
    			}
    		}
    		
    		advisoryCommentMapper.updateByPrimaryKeySelective(ac);
    	}
    }
    
    @Test
    public void obtainTagListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/obtainTagList.json")
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
    public void publishAdvisoryV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/publishAdvisory.json")
    			.param("mid", "1")
    			.param("title", "标题")
    			.param("content", "今天吃饭了吗")
    			.param("writeLocation", "杭州")
    			.param("imgsJsn", "['img.jpg','pic.jpg']")
    			.param("tagId", "1")
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
    public void searchAdvisoryListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/searchAdvisoryList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("tagId", "1000")
    			.param("vid", "5772")
    			.param("releaseFrom", "32")
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
    public void obtainMyAdvisoryListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/obtainMyAdvisoryList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("mid", "4556")
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
    public void obtainPsychoAnsweredAdvisoryListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/obtainPsychoAnsweredAdvisoryList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "20")
    			.param("pid", "7475")
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
    public void obtainAdvisoryDetailV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/obtainAdvisoryDetail.json")
    			.param("advisoryId", "144")
    			.param("vid", "5176")
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
    public void deleteAdvisoryV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/deleteAdvisory.json")
    			.param("advisoryId", "3")
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
    public void increAvisoryShareCountV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/increAvisoryShareCount.json")
    			.param("advisoryId", "4")
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
		mockMvc.perform(post("/Advisory/V1/publishComment.json")
				.param("advisoryId", "73")
				.param("mid", "6684")
				.param("commentContent", "评论的评论")
				.param("parentId", "53")
				.param("ancestorId", "52")
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
    	mockMvc.perform(post("/Advisory/V1/obtainCommentList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("advisoryId", "73")
    			.param("vid", "5176")
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
    public void praiseCommentV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/praiseComment.json")
    			.param("commentId", "1")
    			.param("vid", "5176")
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
    public void unpraiseCommentV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Advisory/V1/unpraiseComment.json")
    			.param("commentId", "1")
    			.param("vid", "5176")
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
