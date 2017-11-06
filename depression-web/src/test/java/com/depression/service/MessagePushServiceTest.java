package com.depression.service;


import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.depression.dao.MemberMapper;
import com.depression.dao.MessagePushAdminMapper;
import com.depression.dao.MessagePushMapper;
import com.depression.dao.MessagePushMemberMapper;
import com.depression.model.EapEnterprise;
import com.depression.model.Member;
import com.depression.model.MessagePush;
import com.depression.model.MessagePushAdmin;
import com.depression.model.MessagePushMember;
import com.depression.model.ServiceGoods;
import com.depression.model.ServiceOrder;
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
    private MessagePushMemberMapper messagePushMemberMapper;
    @Autowired
    private IMMessageService imMessageService;
    @Autowired
    private MessagePushMapper messagePushMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MessagePushAdminMapper messagePushAdminMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private EapEnterpriseService eapEnterpriseService;
    @Autowired
    private ServiceOrderService serviceOrderService;
    
    
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
    	
    	 /*final JSONObject jo=new JSONObject();

		 jo.put("img", messagePush.getImg());

    	 //jo.put("img", Configuration.UPLOAD_SERVER_HOST+messagePush.getImg());
    	 jo.put("img", messagePush.getImg());
    	 jo.put("innerContent", messagePush.getContent());
    	 jo.put("pushLink", messagePush.getPushLink());
    	 jo.put("linkText", messagePush.getLinkText());
    	 jo.put("person", "");
    	 jo.put("xmMessageType", XM_CUSTOM_MESSAGE);
    	 jo.put("mpId", messagePush.getMpId());
    	 final String jsonStr=jo.toJSONString();
    	
		Long mpId=30l;*/
		String jsonStr="{\"innerContent\":\"测试内容\","
					 + "\"pushLink\":\"xinmao://news/112\","
					 + "\"person\":\"\","
					 + "\"xmMessageType\":\"18\","
					 + "\"mpId\":\"215\","
					 + "\"img\":\"http://192.168.0.247:8080/depression-upload//images/preview/20170216/3250239815648256-300x533.jpg\","
					 + "\"linkText\":\"这是一条测试信息\"}";
		String type="100";
		
		//批量获取前500条信息,并发送
		int pageSize=500;
		int pageIndex=1;
		//设置跳出循环参数
		boolean flag=true;
		//option,"{'direct':true,'jo':"+jo.toJSONString()+'}',null
		String option="{push:true}";
		int size=0;
		//批量获取前500条信息,并发送
		while(flag){
				//推送成功
				//boolean isSuccess=false;
				List<String> imAccs=new ArrayList<String>();
				//mpId档mpId不为空时 为自定义批量发送   需要通过mpId 批量获取推送用户的imAccid
				//为空时 就是全发送  直接批量获取用户的imAccid
				Byte isAll=MessagePushService.IS_ALL;
				
				if(isAll == MessagePushService.IS_NOT_ALL){
					//自定义批量发送
					MessagePushMember mpm=new MessagePushMember();
					mpm.setPageSize(pageSize);
					mpm.setPageIndex(pageIndex);
					mpm.setMpId(215l);
					List<MessagePushMember> mpms=messagePushMemberMapper.selectSelective(mpm);
					size=mpms.size();
					for(MessagePushMember m : mpms){
						imAccs.add(m.getImAccount());
					}
				}else{
					Member member=new Member();
					member.setPageSize(pageSize);
					member.setPageIndex(pageIndex);
					//所有批量发送
					List<Member> members=memberService.selectByPage(member);
					size=members.size();
					for(Member m: members){
						imAccs.add(m.getImAccount());
					}
				}
				//批量获取玩accId 开始发送
				String toAccids=JSONObject.toJSONString(imAccs);
				for(int i=0;i<20;i++){
				Integer num=IMUtil.neteaseSendBatchMsg(
						getMsgPushAdmin(),
						toAccids, 
						type,
						jsonStr,
						null,
						null,
						null
						);
					/*if(num == 0)break;
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
				}
				//如果查出的结果小于500条 说明到最后了
				if(size<500){
						flag=false;
					}else{
						pageIndex++;
					}
			}
		
	}
    
    
    @Test
    public void sendMsg(){
    	
    	
    	Integer type=15; //"netease3257379338371072"
    	switch (type){
    		case 11:
    			 //netease3257379338371072 魏 netease3257378790965248 黄
    			//杨 netease3326922162651136 netease3257379136225280
    			//于 netease3257379341762560
    			//System.out.println(imMessageService.sendRegisterMsg("netease3257379338371072"));
    			System.out.println(imMessageService.sendRegisterMsg("netease3257379341762560"));break;
    		/*case IMMessageService.XM_REPLY_ADV_MESSAGE:
        		System.out.println(imMessageService.sendReplyAdvMsg("不要这样2", 6046l, (byte)1, "netease3257379338371072", 4l));break;
    		case IMMessageService.XM_COMMENT_UPDATE_MESSAGE:
            	System.out.println(imMessageService.sendCommentUpdateMsg("不要这样2", 4460l, (byte)1, "是啊", "netease3257379338371072", 6l));break;
    		case IMMessageService.XM_REPLY_COMMENT_MESSAGE:
            	System.out.println(imMessageService.sendReplyCommentMsg("不要这样2", 4460l, (byte)1, "netease3257379338371072", 6l));break;*/
    		case 15:
    			System.out.println(imMessageService.sendConcernMsg("不要这样2", 4460l, (byte)1, "netease3257379136225280"));break;
            	//System.out.println(imMessageService.sendConcernMsg("不要这样2", 4460l, (byte)1, "netease3257379338371072"));break;
    		case 16:
    			//System.out.println(imMessageService.sendViewMsg("不要这样2", 4460l, (byte)1, "netease3277345964376064"));break;
    			//System.out.println(imMessageService.sendViewMsg("不要这样2", 4460l, (byte)1, "netease3326922162651136"));
    			System.out.println(imMessageService.sendViewMsg("不要这样2", 4460l, (byte)1, "netease3326922162651136"));break;
    		case 17:
    			System.out.println(imMessageService.sendWarmMsg("不要这样2", 4460l, (byte)1, "netease3326922162651136","今天天气真是好哈哈即日心好冷哈哈哈哈哈啦啦啦啦啊哈哈哈呜呜呜呜哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈",20l));
    		case 21:
    			ServiceOrder serviceOrder = serviceOrderService.selectOrderByPrimaryKey(533L);
    			
    			ServiceGoods serviceGoods = serviceOrderService.selectGoodsByPrimaryKey(serviceOrder.getSgid());
    			//String state = JSONObject.toJSONString(so);
    			// 更新被叫人状态
    			Member calledMember = memberService.selectMemberByMid(serviceOrder.getServiceProviderId());
    			// 实时订单推送
				JSONObject obj = new JSONObject();
				obj.put("status", serviceOrder.getStatus());
				obj.put("cashAmount", serviceOrder.getCashAmount());
				obj.put("practicalDuration", serviceOrder.getPracticalDuration());
				int remainingDuration = serviceGoods.getDuration() * 60 - serviceOrder.getPracticalDuration();
				if (remainingDuration < 0)
				{
					remainingDuration = 0;
				}
				obj.put("remainingDuration", remainingDuration);
				obj.put("serviceProviderId", serviceOrder.getServiceProviderId());
				obj.put("orderType",serviceOrder.getOrderType());
				obj.put("orderNo", serviceOrder.getNo());
				obj.put("soid", serviceOrder.getSoid());
				if (null != calledMember)
				{
					obj.put("specialistName", calledMember.getNickname());
					obj.put("specialistTitle", calledMember.getTitle());
					String avatar = calledMember.getAvatar();
					if (!StringUtils.isEmpty(avatar))
					{
						obj.put("specialistAvatar", avatar);
					} else
					{
						obj.put("specialistAvatar", "");
					}
				}
    			
    			//System.out.println(imMessageService.sendOrderMsg(obj.toString(), "netease3257379341762560"));	
    			System.out.println(imMessageService.sendOrderMsg(obj.toString(), "netease3257379136225280"));
    			
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
    public void sendInviteMsg(){
    	
    	Long eeId=1l;
    	Long pid=9508l;
    	Integer type=19;
    	
    	//Integer type=IMMessageService.XM_INVITE_PSY_MESSAGE;
    	//netease3257379338371072 魏 netease3257378790965248 黄
		//杨 netease3326922162651136 netease3325486625898496 何 
    	
    	EapEnterprise enterprise = eapEnterpriseService.obtainEnterpriseByKey(eeId);
		Member psycho = memberService.selectMemberByMid(pid);
		//推送同意后的消息
		//Im 推送
		
		if(type == IMMessageService.XM_INVITE_SUCCESS_MESSAGE){
		
			String state="";
			if(enterprise.getType() == EapEnterpriseService.TYPE_COLLEGE){
			state = 
				String.format("%s老师，您已经同意%s的邀请,成为他们的校园心理专家。你需要免费为其用户提供心理服务。", 
				psycho.getNickname(), enterprise.getName());
			}
			
			if(enterprise.getType() == EapEnterpriseService.TYPE_COMPANY){
			state = 
				String.format("%s老师，您已经同意%s的邀请,成为他们的企业心理专家。你需要免费为其用户提供心理服务。", 
				psycho.getNickname(), enterprise.getName());
			}
			//imMessageService.sendInviteSuccessMsg(state, "netease3257379338371072");
			imMessageService.sendInviteSuccessMsg(state, "netease3257379136225280");
		}
		
		if(type == IMMessageService.XM_INVITE_PSY_MESSAGE){
			String words=String.format("%s老师，听说您在危机干预方面经验丰富，请您加入我们。",psycho.getNickname());
			
			String state="";
			if(enterprise.getType() == EapEnterpriseService.TYPE_COLLEGE){
			state = 
					String.format("%s老师，%s邀请你成为他们的校园心理专家%s 温馨提示：同意后，你需要免费为其用户提供心理服务。", 
					psycho.getNickname(), enterprise.getName(), words==null?"":"："+words);
			
			}
			
			if(enterprise.getType() == EapEnterpriseService.TYPE_COMPANY){
			state = 
					String.format("%s老师，%s邀请你成为他们的企业心理专家%s 温馨提示：同意后，你需要免费为其用户提供心理服务。", 
					psycho.getNickname(), enterprise.getName(), words==null?"":"："+words);
			
			}
			
			//imMessageService.sendInviteSuccessMsg(state, "netease3257379338371072");
			//imMessageService.sendInvitePsyMsg(pid, eeId, null, state, "netease3257379338371072", words);
			imMessageService.sendInvitePsyMsg(pid, eeId, null, state, "netease3257379136225280", words);
		}
		
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
    
    
    //获取管理员im账号
  	public String getMsgPushAdmin(){
  		//获取发送者account
  		List<MessagePushAdmin> mpas= messagePushAdminMapper.selectSelective(null);
  		MessagePushAdmin mpa=mpas.get(0);
  		return mpa.getImAccount();
  	}
    
}
