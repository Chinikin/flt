package com.depression.service;


import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.depression.dao.MessagePushMapper;
import com.depression.dao.MessagePushMemberMapper;
import com.depression.model.MessagePush;
import com.depression.model.MessagePushMember;
import com.depression.model.web.vo.MessagePushVO;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath*:applicationContext.xml", "classpath*:spring-mybatis.xml", "classpath*:spring-mvc.xml" })  
@WebAppConfiguration 
public class MessagePushServiceTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private MessagePushMemberMapper messagePushMemberMapper;
    @Autowired
    private IMMessageService imMessageService;
    @Autowired
    private MessagePushMapper messagePushMapper;
    
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void testInsertMessage() throws Exception{
    	/*try{
    		
    		MessagePushVO MessagePushVO=new MessagePushVO();
    		MessagePushVO.setContent("测试内容");
    		MessagePushVO.setImg("/images/preview/20170216/3250239815648256-300x533.jpg");
    		MessagePushVO.setIsAll(MessagePushService.IS_ALL);
    		MessagePushVO.setLinkText("这是一条测试信息");
    		MessagePushVO.setPushLink("xinmao://news/112");
    		Date test=new Date();
    		test.setTime(System.currentTimeMillis()+1200000);
    		MessagePushVO.setPushTime(test);
    		MessagePushVO.setPushType(MessagePushService.PUSH_TYPE_PUSH);
    		MessagePushVO.setTitle("测试标题");
    		
    		List<MessagePushMember> list=new ArrayList<>();
    		MessagePushMember mpm=null;
    		for(Long i=0l;i<10;i++){
    			mpm=new MessagePushMember();
    			mpm.setMemberId(i);
    			mpm.setImAccount(i+"");
    			mpm.setMpId(MessagePushVO.getMpId());
    			list.add(mpm);
    		}
    		MessagePush messagePush=new MessagePush();
    		BeanUtils.copyProperties(MessagePushVO, messagePush);
    		messagePushService.insertMsgPush(messagePush, list);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}*/
    }
    
    
	/**
	 * IM指定推送
	 */
    @Test
	public void neteaseSendBatchMsg(){
		Long mpId=30l;
		String jsonStr="{\"content\":\"测试内容\",\"pushLink\":\"xinmao://news/112\",\"img\":\"http://192.168.0.247:8080/depression-upload//images/"
				+ "preview/20170216/3250239815648256-300x533.jpg\",\"linkTest\":\"这是一条测试信息\"}";
		String type="100";
		
		//获取发送者account
		int pageSize=500;
		int pageIndex=1;
		
		//设置跳出循环参数
		boolean flag=true;
		//批量获取前500条信息,并发送
		while(flag){
			MessagePushMember mpm=new MessagePushMember();
			mpm.setPageSize(pageSize);
			mpm.setPageIndex(pageIndex);
			mpm.setMpId(mpId);
			List<MessagePushMember>mpms=messagePushMemberMapper.selectSelective(mpm);
			List<String> imAccs=new ArrayList<String>();
			for(MessagePushMember m : mpms){
				imAccs.add(m.getImAccount());
			}
			String toAccids=JSONObject.toJSONString(imAccs);
			IMUtil.neteaseSendBatchMsg(
					imMessageService.getMsgPushAdmin(),
					toAccids, 
					type,
					jsonStr,
					null,
					null,
					null);
			//如果查出的结果小于500条 说明到最后了
			if(mpms.size()<=500){
					flag=false;
				}else{
					pageIndex++;
				}
			
			}
		
	}
    
    
    @Test
    public void sendMsg(){
    	Integer type=17; //"netease3257379338371072"
    	switch (type){
    		case 11:
    			 //netease3257379338371072 魏 netease3257378790965248 黄
    			System.out.println(imMessageService.sendRegisterMsg("netease3257379338371072"));
    			System.out.println(imMessageService.sendRegisterMsg("netease3257378790965248"));break;
    		/*case IMMessageService.XM_REPLY_ADV_MESSAGE:
        		System.out.println(imMessageService.sendReplyAdvMsg("不要这样2", 6046l, (byte)1, "netease3257379338371072", 4l));break;
    		case IMMessageService.XM_COMMENT_UPDATE_MESSAGE:
            	System.out.println(imMessageService.sendCommentUpdateMsg("不要这样2", 4460l, (byte)1, "是啊", "netease3257379338371072", 6l));break;
    		case IMMessageService.XM_REPLY_COMMENT_MESSAGE:
            	System.out.println(imMessageService.sendReplyCommentMsg("不要这样2", 4460l, (byte)1, "netease3257379338371072", 6l));break;*/
    		case 15:
    			System.out.println(imMessageService.sendConcernMsg("不要这样2", 4460l, (byte)1, "netease3257378790965248"));
            	System.out.println(imMessageService.sendConcernMsg("不要这样2", 4460l, (byte)1, "netease3257379338371072"));break;
    		case 16:
    			//System.out.println(imMessageService.sendViewMsg("不要这样2", 4460l, (byte)1, "netease3277345964376064"));break;
    			System.out.println(imMessageService.sendViewMsg("不要这样2", 4460l, (byte)1, "netease3257378790965248"));
    			System.out.println(imMessageService.sendViewMsg("不要这样2", 4460l, (byte)1, "netease3257379338371072"));break;
    		case 17:
    			System.out.println(imMessageService.sendWarmMsg("不要这样2", 4460l, (byte)1, "netease3257378790965248","今天天气真是好哈哈即日心好冷哈哈哈哈哈啦啦啦啦啊哈哈哈呜呜呜呜哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈",20l));
            	System.out.println(imMessageService.sendWarmMsg("不要这样2", 4460l, (byte)1, "netease3257379338371072","今天天气真是好哈哈即日心好冷哈哈哈哈哈啦啦啦啦啊哈哈哈呜呜呜呜哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈",167l));break;
    	}
    		
    			
    	//System.out.println(imMessageService.sendCommentUpdateMsg("不要这样2", 4460l, (byte)1,"是啊","netease3257378790965248", 6l));
    	//System.out.println(imMessageService.sendRegisterMsg("netease3257378790965248"));
    	//System.out.println(imMessageService.sendReplyAdvMsg("季春生", 6046l, (byte)1, "netease3257378790965248",4l));
    	//System.out.println(imMessageService.sendConcernMsg("不要这样2", 4460l, (byte)1, "netease3257378790965248"));
    	//System.out.println(imMessageService.sendReplyCommentMsg("不要这样2", 4460l, (byte)1, "netease3257378790965248", 6l));
    	//System.out.println(imMessageService.sendViewMsg("不要这样2", 4460l, (byte)1, "netease3257378790965248"));
      	//System.out.println(imMessageService.sendWarmMsg("不要这样2", 4460l, (byte)1, "netease3257378790965248"));
    }
    
    @Test
    public void updateUser(){
    	MessagePush messagePush=messagePushMapper.selectByPrimaryKey(105l);
    	if(messagePush.getIsEnable() == null || "".equals(messagePush.getIsEnable() )){
			System.out.println(true);
		 }else{
			 System.out.println(false);
		 }
    	
    	//IMUtil.neteaseUserCreate("心猫团队", "images/thumbnail/20170301/3268856781702144.jpg", "");
    	IMUtil.neteaseUserUpdateUinfo("netease3277095716405248", "", "http://192.168.0.247/images/thumbnail/20160720/2951883420910592.jpg", "");
    }
    
}
