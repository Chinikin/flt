package com.depression.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.depression.base.wechat.service.MessageService;
import com.depression.dao.MessagePushAdminMapper;
import com.depression.dao.MessagePushMapper;
import com.depression.dao.MessagePushMemberMapper;
import com.depression.model.Member;
import com.depression.model.MessagePush;
import com.depression.model.MessagePushAdmin;
import com.depression.model.MessagePushMember;
import com.depression.model.Page;
import com.depression.model.PushDevType;
import com.depression.model.UbViewHomepage;
import com.depression.push.AndroidPushServer;
import com.depression.push.CustomMsgType;
import com.depression.push.IOSPushServer;
import com.depression.push.PushMsg;
import com.depression.utils.Configuration;
import com.depression.utils.IMUtil;

@Service
public class IMMessageService
{
    @Autowired
    MessagePushAdminMapper messagePushAdminMapper;
    @Autowired
    MemberService memberService;
    @Autowired
    MessagePushMemberMapper messagePushMemberMapper;
    @Autowired
    UbViewHomepageService ubViewHomepageService;
    
    public final static String IM_CUSTOM_MESSAGE = "100"; //im 自定义消息
    
    public final static Integer XM_REGISTER_MESSAGE = 11; 	 //注册
    /*public final static String XM_REPLY_ADV_MESSAGE = "12";	 //回复提问
    public final static String XM_COMMENT_UPDATE_MESSAGE = "13";		 //评论心友圈
    public final static String XM_REPLY_COMMENT_MESSAGE = "14";//回复*/
    public final static Integer XM_CONCERN_MESSAGE = 15;		 //关注
    public final static Integer XM_VIEW_MESSAGE = 16;  		 //查看主页
    public final static Integer XM_WARM_MESSAGE = 17;   		 //XXX温暖了我
    
    public final static Integer XM_INVITE_PSY_MESSAGE = 19; //邀请咨询师
    public final static Integer XM_INVITE_SUCCESS_MESSAGE = 20;  //邀请成功反馈
    public final static Integer XM_ORDER_MESSAGE = 21; //订单推送
    
    
    public final static Integer XM_CUSTOM_MESSAGE = 18;		 //自定义消息推送   相对其他复杂  有图片 链接
    
    public final static String MSG_OPE_PERSON="0"; //点对点个人消息
    public final static String MSG_OPE_GROUP="1"; //群消息
   
    
	/**
	 * 
	 * @param mpId
	 * @param jsonStr
	 * @param type
	 * @param pushContent
	 * @param option
	 * @param payload
	 * @param ext
	 */
	public void neteaseSendBatchMsg(byte isAll,
									Long mpId,
									String jsonStr,
									String type,
									String pushContent,
									String option,
									String payload,
									String ext){
		int pageSize=100;
		int pageIndex=1;
		//设置跳出循环参数
		boolean flag=true;
		
		
		int size=0;
		//批量获取前500条信息,并发送
		while(flag){
				//推送成功
				//boolean isSuccess=false;
				List<String> imAccs=new ArrayList<String>();
				//mpId档mpId不为空时 为自定义批量发送   需要通过mpId 批量获取推送用户的imAccid
				//为空时 就是全发送  直接批量获取用户的imAccid
				if(isAll == MessagePushService.IS_NOT_ALL){
					//自定义批量发送
					MessagePushMember mpm=new MessagePushMember();
					mpm.setPageSize(pageSize);
					mpm.setPageIndex(pageIndex);
					mpm.setMpId(mpId);
					List<MessagePushMember> mpms=messagePushMemberMapper.selectSelectiveWithPage(mpm);
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
				for(int i=0;i<3;i++){
				Integer num=IMUtil.neteaseSendBatchMsg(
						getMsgPushAdmin(),
						toAccids, 
						type,
						jsonStr,
						option,
						pushContent,
						payload
						);
					if(num == 0)break;
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//如果查出的结果小于500条 说明到最后了
				if(size<100){
						flag=false;
					}else{
						pageIndex++;
					}
			}
		}
    
    
  //获取管理员im账号
  	public String getMsgPushAdmin(){
  		//获取发送者account
  		List<MessagePushAdmin> mpas= messagePushAdminMapper.selectSelective(null);
  		MessagePushAdmin mpa=mpas.get(0);
  		return mpa.getImAccount();
  	}
	
	/**
	 * 注册消息
	 * @param to 接收im账号
	 */
	public Integer sendRegisterMsg( String to){
		//发送第一条消息
		JSONObject jf=new JSONObject();
		jf.put("direct", "false");
		jf.put("person", "");
		jf.put("linkText", "");
		jf.put("pushLink", "xinmao://capitalCoupon");
		jf.put("xmMessageType", XM_REGISTER_MESSAGE);
		jf.put("innerContent","亲爱的， 欢迎来到心猫！" );
		jf.put("img", "");
		jf.put("mpId", "");
		IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jf.toJSONString(),
				null,
				null,
				null);
		//发送第二条信息
		JSONObject jo=new JSONObject();
		jo.put("direct", "false");
		jo.put("person", "");
		jo.put("innerContent", "心猫送您30元优惠券！ ");
		jo.put("pushLink", "xinmao://capitalCoupon");
		jo.put("xmMessageType", XM_REGISTER_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "点击查看");
	    jo.put("mpId", "");
		Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				null,
				null,
				null);
		return result;
		}
	
	/**
	 * XXX回复提问
	 * @param name 成员姓名
	 * @param mid 成员Id
	 * @param userType 成员类型
	 * @param to 接收im账号
	 * @param advisoryId 评论页面
	 * @return
	 */
	/*public Integer sendReplyAdvMsg(String name,
								   Long mid,
								   byte userType,
								   String to,
								   Long advisoryId
								   ){
		JSONObject jo=new JSONObject();
		JSONObject jp=new JSONObject();
		jp.put("mLink", getMLink(mid, userType));
		jp.put("name", name);
		jo.put("person", jp.toJSONString());
		jo.put("innerContent", " 回复了我的问题 ");
		jo.put("pushLink", "xinmao://replyAdv?paramId="+advisoryId);
		jo.put("xmMessageType", XM_REPLY_ADV_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "点击查看");
	    jo.put("mpId", "");
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				null,
				null,
				null);
	    return result;
		}*/
	
	/**
	 * 
	 * @param name 接收姓名
	 * @param mid 用户id
	 * @param userType 用户类型
	 * @param content 消息部分内容
	 * @param to 接收im账号
	 * @param updateId 心友圈id
	 * @return
	 */
	/*public Integer sendCommentUpdateMsg(String name,
										Long mid,
										Byte userType,
										String content, 
										String to,
										Long updateId){
		JSONObject jo=new JSONObject();
		JSONObject jp=new JSONObject();
		jp.put("mLink", getMLink(mid, userType));
		jp.put("name", name);
		jo.put("person", jp.toJSONString());
		jo.put("innerContent", " 回复了  我的心友圈: "+content+"... ");
		String link="xinmao://memberUpdate?paramId="+updateId;
		jo.put("pushLink", link);
		jo.put("xmMessageType", XM_COMMENT_UPDATE_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "点击查看");
	    jo.put("mpId", "");
	    System.out.println(jo.toJSONString());
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				null,
				null,
				null);
		return result;
		}*/

	/**
	 * 
	 * @param name
	 * @param mid
	 * @param userType
	 * @param to
	 * @return
	 */
	/*public Integer sendReplyCommentMsg(String name,
									Long mid,
									Byte userType, 
									String to,
									Long commentId){
		JSONObject jo=new JSONObject();
		JSONObject jp=new JSONObject();
		jp.put("mLink", getMLink(mid, userType));
		jp.put("name", name);
		jo.put("person", jp.toJSONString());
		jo.put("innerContent", " 回复了我的评论 ");
		jo.put("pushLink", "xinmao://comment?paramId="+commentId);
		jo.put("xmMessageType", XM_REPLY_COMMENT_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "点击查看");
	    jo.put("mpId", "");
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				null,
				null,
				null);
		return result;
		}*/
	
	/**
	 * 
	 * @param name 
	 * @param mid
	 * @param userType
	 * @param to
	 * @return
	 */
	public Integer sendConcernMsg(String name,
								  Long mid,
								  Byte userType, 
								  String to){
		JSONObject jo=new JSONObject();
		JSONObject jp=new JSONObject();
		jp.put("name", name);
		jp.put("mLink", getMLink(mid, userType));
		jo.put("person", jp.toJSONString());
		jo.put("innerContent", " 关注了你 ");
		jo.put("pushLink", "xinmao://concern");
		jo.put("xmMessageType", XM_CONCERN_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "点击查看");
	    jo.put("mpId", "");
	    jo.put("direct", "false");
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				"{\"push\":true}",
				name+" 关注了你",
				null);
		return result;
		}
	
	/**
	 * 查看主页
	 * @param name 成员姓名
	 * @param mid 成员id
	 * @param userType 成员类型
	 * @param to 成员im账号
	 * 
	 * @return
	 */
	public Integer sendViewMsg(String name,
							   Long mid,
							   Byte userType, 
							   String to){
		JSONObject jo=new JSONObject();
		JSONObject jp=new JSONObject();
		jp.put("name", name);
		jp.put("mLink", getMLink(mid, userType));
		jo.put("person", jp.toJSONString());
		jo.put("innerContent", " 查看了你的主页 ");
		jo.put("pushLink", "xinmao://view");
		jo.put("xmMessageType", XM_VIEW_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "");
	    jo.put("mpId", "");
	    jo.put("direct", "false");
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				"{\"push\":true}",
				name+" 查看了你的主页",
				null);
		return result;
		}
	
	public void sendViewMsgWaitMoment(String name,
			   Long mid,
			   Byte userType, 
			   String to){
		
		
		
		Member queryMember=new Member();
		queryMember.setImAccount(to);
		
		Member member=memberService.selectByMember(queryMember).get(0);
		
		UbViewHomepage ubViewHomepage=new UbViewHomepage();
		ubViewHomepage.setViewFrom(mid);
		ubViewHomepage.setViewTo(member.getMid());
		
		
		List<UbViewHomepage> list=ubViewHomepageService.getLatestUbViewHomepage(ubViewHomepage);
		//30分钟内连续不发送
		if(list.size()>0){
			Date now=new Date();
			Date latest=list.get(0).getCreateTime();
			long mins=(now.getTime()-latest.getTime())/60000;
			if(mins >= 30){
				sendViewMsg(name,mid,userType,to);
			}
		}else
		{
			sendViewMsg(name,mid,userType,to);
		}
		//保存最新的数据
		ubViewHomepageService.addUbViewHomepage(ubViewHomepage);
	}
	
	
	/**
	 * XXX温暖了我
	 * @param name 接收姓名
	 * @param to 接收im账号
	 */
	public Integer sendWarmMsg(String name,
							   Long mid,
							   Byte userType, 
							   String to,String pushContent,Long paramId){
		String content=pushContent.length()>15?pushContent.substring(0,15)+"... ":pushContent;
		JSONObject jo=new JSONObject();
		JSONObject jp=new JSONObject();
		jp.put("name", name);
		jo.put("direct", "false");
		jp.put("mLink", getMLink(mid, userType));
		jo.put("person", jp.toJSONString());
		jo.put("innerContent", " 赞了你："+content);
		jo.put("pushLink", "xinmao://warm?paramId="+paramId);
		jo.put("xmMessageType", XM_WARM_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "点击查看");
	    jo.put("mpId", "");
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				"{\"push\":true}",
				content,
				null);
		return result;
		}
	
	
	
	/**
	 * 邀请专家
	 * @param name 接收姓名
	 * @param to 接收im账号
	 */
	public Integer sendInvitePsyMsg(
							   Long pid,
							   Long eeId,
							   Byte userType,
							   String state,
							   String to,
							   String words){
		String content=state.length()>15?state.substring(0,15)+"... ":state;
		JSONObject jo=new JSONObject();
		JSONObject jp=new JSONObject();
		jp.put("name", "");
		//0 未阅读   1已阅读
		//jo.put("isRead", "0");
		jp.put("mLink", "");
		jo.put("person", jp.toJSONString());
		jo.put("pid", pid);
		jo.put("eeId", eeId);
		jo.put("innerContent",state);
		jo.put("pushLink", "");
		jo.put("xmMessageType", XM_INVITE_PSY_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", words);
	    jo.put("mpId", "");
		System.out.println(jo.toJSONString());
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				"{\"push\":true}",
				content,
				null);
		return result;
		}
	
	
	
	/**
	 * 专家同意
	 * @param name 接收姓名
	 * @param to 接收im账号
	 */
	public Integer sendInviteSuccessMsg(
							   String state,
							   String to){
		String content=state.length()>15?state.substring(0,15)+"... ":state;
		JSONObject jo=new JSONObject();
		//JSONObject jp=new JSONObject();
		//jp.put("name", "");
		//jp.put("mLink", "");
		jo.put("person", "");
		jo.put("pid", "");
		jo.put("eeId", "");
		jo.put("innerContent",state);
		jo.put("pushLink", "");
		jo.put("xmMessageType", XM_INVITE_SUCCESS_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "");
	    //jo.put("mpId", "");
		System.out.println(jo.toJSONString());
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				"{\"push\":true}",
				content,
				null);
		return result;
		}
	
	
	/**
	 * 订单推送
	 * @param name 接收姓名
	 * @param to 接收im账号
	 */
	public Integer sendOrderMsg(
							   String state,
							   String to){
		//String content=state.length()>15?state.substring(0,15)+"... ":state;
		String content = "您有新的订单信息，请注意查看";
		JSONObject jo=new JSONObject();
		//JSONObject jp=new JSONObject();
		//jp.put("name", "");
		//jp.put("mLink", "");
		jo.put("person", "");
		jo.put("pid", "");
		jo.put("eeId", "");
		jo.put("innerContent",state);
		jo.put("pushLink", "");
		jo.put("xmMessageType", XM_ORDER_MESSAGE);
		jo.put("img", "");
	    jo.put("linkText", "");
	    //jo.put("mpId", "");
		System.out.println(jo.toJSONString());
	    Integer result=IMUtil.neteaseSendMsg(
				getMsgPushAdmin(),
				MSG_OPE_PERSON, 
				to,
				IM_CUSTOM_MESSAGE,
				jo.toJSONString(),
				"{\"push\":true,\"roam\":false,\"history\":false,\"sendersync\":false,\"persistent\":false,\"badge\":false}",
				content,
				null);
		return result;
		}
	
	
	/**
	 * 获得成员跳转链接
	 * @param mid 成员id
	 * @param userType 成员类型
	 * @return
	 */
	public String getMLink(Long mid,Byte userType){
		return "xinmao://member?mid="+mid+"&userType="+userType;
	}

	
}





