package com.depression.controller.eap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
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
import com.depression.dao.EapEnterpriseMapper;
import com.depression.model.EapEmployee;
import com.depression.model.Member;
import com.depression.model.ServiceCustomerStatistics;
import com.depression.model.eap.dto.EapEmployeeDTO;
import com.depression.model.eap.vo.EapEmployeeVO;
import com.depression.service.EapEmployeeService;
import com.depression.service.EapEnterpriseService;
import com.depression.service.MemberAdvisoryService;
import com.depression.service.MemberService;
import com.depression.service.MemberUpdateService;
import com.depression.service.QiniuService;
import com.depression.service.ServiceStatisticsService;
import com.depression.service.TestingService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-*.xml","classpath*:spring-session.xml" })  
@WebAppConfiguration 
public class EapUgcControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
	EapEmployeeService employeeService;
	@Autowired
	ServiceStatisticsService serviceStatService;
	@Autowired
	MemberService memberService;
	@Autowired
	EapEnterpriseService enterpriseService;
	@Autowired
	MemberAdvisoryService memberAdvisoryService;
	@Autowired
	MemberUpdateService memberUpdateService;
	@Autowired
	TestingService testingService;
	@Autowired
	EapEnterpriseMapper eapEnterpriseMapper;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void obtainOrderList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcServiceOrder/obtainOrderList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "100")
    			.param("mid", "5772")
    			.param("eeId", "32")
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
    public void exportServiceOrder() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcServiceOrder/exportServiceOrder.json")
    			.param("eeId", "8")
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
    public void searchAdvisory() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberAdvisory/searchAdvisory.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("eeId", "8")
    			.param("mid", "4462")
    			
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
    public void deleteMemberAdvisory() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberAdvisory/deleteMemberAdvisory.json")
    			.param("advisoryId", "102")
    			
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
    public void obtainAdvisoryComments() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberAdvisory/obtainAdvisoryComments.json")
    			.param("advisoryId", "91")
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
    public void deleteMemberAdvisoryComment() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberAdvisory/deleteMemberAdvisoryComment.json")
    			.param("commentId", "182")
    			.param("eeId", "8")
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
    public void obtainAdvisoryDetail() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberAdvisory/obtainAdvisoryDetail.json")
    			.param("advisoryId", "91")
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
    public void searchUpdate() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberUpdate/searchUpdate.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("eeId", "8")
    			.param("mid", "4462")
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
    public void deleteMemberUpdate() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberUpdate/deleteMemberUpdate.json")
    			.param("updateId", "181")
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
    public void obtainUpdateComments() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberUpdate/obtainUpdateComments.json")
    			.param("updateId", "352")
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
    public void deleteMemberUpdateComment() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcMemberUpdate/deleteMemberUpdateComment.json")
    			.param("commentId", "182")
    			.param("eeId", "8")
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
    public void obtainTestingList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcTesting/obtainTestingList.json")
    			.param("pageIndex", "2")
    			.param("pageSize", "10")
    			.param("eeId", "32")
    			
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    }
    
    
    @Test
    public void exportTestingList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcTesting/exportTesting.json")
    			.param("eeId", "1")
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    }
    
    
    @Test
    public void obtainSearchWordsList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcSearchWords/obtainSearchWordsList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("eeId", "8")
    			.param("mid", "4462")
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    }
    
    
    @Test
    public void exportSearchWordsList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUgcSearchWords/exportSearchWords.json")
    			.param("eeId", "8")
    			)
    	.andReturn();
    	
    	Map<String, String[]>  paramMap = mvcResult.getRequest().getParameterMap();
    	String json = JSON.toJSONString(paramMap, true);
    	System.out.println(json);

    	System.out.println(mvcResult.getResponse().getStatus());
    	System.out.println(mvcResult.getResponse().getContentAsString());
    	
    }
    
    
}
