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
import com.depression.service.EapUserService;
import com.depression.service.MemberAdvisoryService;
import com.depression.service.MemberAuthCodeService;
import com.depression.service.MemberService;
import com.depression.service.MemberUpdateService;
import com.depression.service.PrivilegesService;
import com.depression.service.QiniuService;
import com.depression.service.ServiceStatisticsService;
import com.depression.service.SystemOperationLogService;
import com.depression.service.TestingService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class EapUserControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
	private MemberAuthCodeService memberAuthCodeService;
	@Autowired
	private EapUserService eapUserService;
	@Autowired
	private SystemOperationLogService systemOperationLogService;
	@Autowired
	private EapEnterpriseService eapEnterpriseService;
	@Autowired
	private PrivilegesService privilegesService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void eapUserLogin() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/eapUserLogin.json")
    			.param("eapPassword", "123456")
    			.param("mobilePhone", "15225811360")
    			.param("code", "123456")
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
    public void addUser() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/addEapUser.json")
    			.param("eapUserId", "111112")
    			.param("mobilePhone", "15225811360")
    			.param("eeId", "2")
    			.param("eapPassword", "123456")
    			.param("userType", "11")
    			.param("showName", "test")
    			.param("username", "test")
    			.param("isEnable", "0")
    			.param("isDetele", "0")
    			.param("spareInt", "0")
    			.param("spareChar", "2,3,4,5")
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
    public void modifyUser() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/modifyUser.json")
    			.param("userId", "111112")
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
    public void saveUser() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/saveUser.json")
    			.param("eapUserId", "111112")
    			.param("mobilePhone", "15225811360")
    			.param("eapPassword", "123456")
    			.param("userType", "11")
    			.param("eeId", "1")
    			.param("showName", "test")
    			.param("username", "test")
    			.param("isEnable", "0")
    			.param("isDelete", "0")
    			.param("spareInt", "0")
    			.param("spareChar", "1,2,3,4,5")
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
    public void listUser() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/listUser.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "10")
    			.param("searchKeyWords", "15225850360")
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
    public void modifyPass() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/modifyPass.json")
    			.param("userId", "111112")
    			.param("newPassWord", "1234567")
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
    public void changeStatus() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/changeStatus.json")
    			.param("userId", "111112")
    			.param("isDel", "1")
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
    public void deleteUser() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/eapUser/deleteUser.json")
    			.param("userId", "111112")
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

