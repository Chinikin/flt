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
public class PsychoControllerTest
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
    public void obtainPsychosV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/obtainPsychos.json")
    			.param("pageIndex", "1")
    			.param("pageSize", "1")
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
    public void obtainPsychoDetailV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/obtainPsychoDetail.json")
    			.param("pid", "8462")
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
    public void addPsychoV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/addPsycho.json")
    			.param("nickname", "彩珍君")
    			.param("mobilePhone", "13732261120")
    			.param("userPassword", "123456")
    			.param("title", "二级医师")
    			.param("avatar", "avatar")
    			.param("avatarThumbnail", "avatarThumbnail")
    			.param("userPassword", "userPassword")
    			.param("sex", "1")
    			.param("wxAccount", "wxAccount")
    			.param("location", "location")
    			.param("ltid", "1")
    			.param("workYears", "1")
    			.param("caseHours", "1")
    			.param("primaryDomain", "['aaa', 'bb']")
    			.param("candidPhoto", "candidPhoto")
    			.param("profile", "profile")
    			.param("ids", "1","3")
    			.param("photoCertificationDealtRel", "photoCertificationDealtRel")
    			.param("photoCertificationDealtPreviewRel", "photoCertificationDealtPreviewRel")
    			.param("photoIdentityCardDealtRel", "photoIdentityCardDealtRel")
    			.param("photoIdentityCardDealtPreviewRel", "photoIdentityCardDealtPreviewRel")
    			.param("needAudit", "1")
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
    public void modifyPsychoV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/modifyPsycho.json")
    			.param("mid", "9507")
    			.param("nickname", "彩珍君")
    			.param("mobilePhone", "13732261120")
    			.param("userPassword", "123456")
    			.param("title", "二级医师")
    			.param("avatar", "avatar")
    			.param("avatarThumbnail", "avatarThumbnail")
    			.param("userPassword", "userPassword")
    			.param("sex", "1")
    			.param("wxAccount", "wxAccount")
    			.param("location", "location")
    			.param("ltid", "1")
    			.param("workYears", "1")
    			.param("caseHours", "1")
    			.param("primaryDomain", "['aaa', 'bb']")
    			.param("candidPhoto", "candidPhoto")
    			.param("profile", "profile")
    			.param("ids", "1","3")
    			.param("photoCertificationDealtRel", "photoCertificationDealtRel")
    			.param("photoCertificationDealtPreviewRel", "photoCertificationDealtPreviewRel")
    			.param("photoIdentityCardDealtRel", "photoIdentityCardDealtRel")
    			.param("photoIdentityCardDealtPreviewRel", "photoIdentityCardDealtPreviewRel")
    			.param("needAudit", "1")
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
    public void getLicenseTypeListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/getLicenseTypeList.json")
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
    public void obtainPsychoTagListV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/obtainPsychoTagList.json")
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
    public void checkPsychoStatusV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/checkPsychoStatus.json")
    			.param("eeId", "8")
    			.param("mobilePhone", "13732261120")
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
    public void invitePsychoToGroupV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/invitePsychoToGroup.json")
    			.param("eeId", "8")
    			.param("pid", "8462")
    			.param("words", "words")
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
    public void removePsychoFromGroupV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/removePsychoFromGroup.json")
    			.param("eeId", "8")
    			.param("pid", "9507")
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
    public void requestAuditV1() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/requestAudit.json")
    			.param("pid", "9507")
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
    public void obtainOrderList() throws Exception{
    	MvcResult mvcResult = 
    	mockMvc.perform(post("/Psychor/V1/obtainOrderList.json")
    			.param("eeId", "8")
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
}
