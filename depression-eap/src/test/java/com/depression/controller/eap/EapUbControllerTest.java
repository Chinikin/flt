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
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-*.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class EapUbControllerTest
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
    public void obtainEmployeeList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/obtainEmployeeList.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("eeId", "8")
    			.param("sortTag", "12")
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
    public void EapEnterprise() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapEnterprise/exportEmployee.json")
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
    public void obtainEnterprise() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapEnterprise/obtainEnterprise.json")
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
    public void obtainEmployeeData() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/obtainEmployeeData.json")
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
    public void obtainEapAttributes() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/obtainEapAttributes.json")
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
    public void updateEmployee() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/updateEmployee.json")
    			.param("eemId", "426")
    			.param("eeId", "8")
    			.param("name", "测试时的")
    			.param("phoneNum", "13323556985")
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
    public void newEmployee() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/newEmployee.json")
    			.param("name", "张三三撒三")
    			.param("phoneNum", "13564587954")
    			.param("number", "00445789")
    			.param("remark1", "XXXX公司")
    			.param("remark2", "研发补补")
    			.param("eeId", "1")
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
    public void newEmployee1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/newEmployee.json")
    			.param("name", "张三三撒三")
    			.param("phoneNum", "13564587954")
    			.param("number", "00445789")
    			.param("grade", "2014")
    			.param("college", "计算机学院")
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
    public void exportEmployee() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/exportEmployee.json")
    			.param("eeId", "8")
    			.param("mid", "4460")
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
    public void entireImportEmployee() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/entireImportEmployee.json")
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
    public void enableEmployee() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/EapUb/enableEmployee.json")
    			.param("ids", "5,6,7")
    			.param("isEnable", "1")
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
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("eeId", "8")
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
