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
import com.depression.utils.CallCommunicationUtil;
import com.depression.utils.IMUtil;
import com.depression.utils.SmsUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class QiniuTest
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
    QiniuService qiniuService;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
	public void fetchRecordFile() throws Exception{
    	String recordUrl = "http://www.ucpaas.com/fileserver/record/859733a93fe09cef92688897a82a0341_ecc7b132be5d706178030c5e836fdaaa_20170313";
    	String callid = "ecc7b132be5d706178030c5e836fdaaa";
    	String sigedRecordUrl = CallCommunicationUtil.ucpassRecordUrlSig(recordUrl, callid);
    	String filename = String.format("call_record/%d_%s_%s.mp3", 111L, "yyyy-MM-dd HH:mm:ss", "13888888888");
    	qiniuService.fetchFile(sigedRecordUrl, filename);
	}
}
