package com.depression.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.depression.dao.MessagePushAdminMapper;
import com.depression.dao.MessagePushMapper;
import com.depression.dao.MessagePushMemberMapper;
import com.depression.model.Member;
import com.depression.model.MessagePush;
import com.depression.model.MessagePushMember;
import com.depression.model.Page;
import com.depression.utils.Configuration;

@Service
public class MessagePushService
{
    @Autowired
    MessagePushMapper messagePushMapper;
    @Autowired
    MessagePushMemberMapper messagePushMemberMapper;
    @Autowired
    PushService pushService;
    @Autowired
    MemberService memberService;
    @Autowired
    MessagePushAdminMapper messagePushAdminMapper;
    @Autowired
    IMMessageService imMessageService;
   
    //推送状态
    public static byte PUSH_STATUS_RUNNABLE = (byte) 0;
    public static byte PUSH_STATUS_RUNNING = (byte) 1;
    public static byte PUSH_STATUS_SUCCESS = (byte) 2;
    
    //推送类型
    public static byte PUSH_TYPE_ALL = (byte) 0;
    public static byte PUSH_TYPE_IM = (byte) 1;
    public static byte PUSH_TYPE_PUSH = (byte) 2;
    //是否所有人
    public static byte IS_ALL = 1; //发送所有
    public static byte IS_NOT_ALL = 0;//不发送所有人
    
    public static String IM_CUSTOM_MESSAGE = "100"; //im 自定义消息
    public static String IM_TEXT_MESSAGE = "0"; //im 自定义消息
    
    public static Integer XM_CUSTOM_MESSAGE = 18;	//自定义消息推送
    
    /**
     * 用户点击信息更新
     * @param mpId
     */
	public void transcountClickMsgPush(Long mpId) {
		Date clickTime=new Date();
		MessagePush mp=messagePushMapper.selectByPrimaryKey(mpId);
		//设置发送一周以后的时间
		Calendar cal = Calendar.getInstance();
		cal.setTime(mp.getPushTime());
		cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+7);
		Date date=cal.getTime();
		//判断是否在一周以内 是的话count+1 
		if(clickTime.getTime() <date.getTime()){
			messagePushMapper.countClickMsgPush(mpId);
		}
		
	}
    
    /**
     * 
     * @param request  请求request
     * @param messagePushVO 传递参数
     * @return
     */
    
    public void insertMsgPush( MessagePush messagePush,  HashSet<Long> members){
    	
    	//设置存入数据库的MessagePush的参数
    	messagePush.setClickCount(0l);
    	messagePush.setCreateTime(new Date());
    	messagePush.setPushStatus(PUSH_STATUS_RUNNABLE);
    	messagePushMapper.insertSelective(messagePush);
		
    	 //发送信息，发送所有用户或者非所有用户
    	//1.非所有用户定时器
    	if(members != null ){
    		//保存发送用户的id
    		for(Long mid:members){
    			MessagePushMember mpm=new MessagePushMember();
    			Member m=memberService.selectMemberByMid(mid);
    			if(m != null){
    				mpm.setMemberId(m.getMid());
        			mpm.setMpId(messagePush.getMpId());
        			mpm.setImAccount(m.getImAccount());
        			mpm.setMobilePhone(m.getMobilePhone());
        			messagePushMemberMapper.insertSelective(mpm);
    			}
    		}
    	}
		
	}
    
    /**
     *  获得推送信息
     * @param mpId 推送信息id
     * @return
     */
    public MessagePush obtainMessagePush(Long mpId){
    	return messagePushMapper.selectByPrimaryKey(mpId);
    }
    
    /**
     * 编辑推送消息
     * @param mpId 推送id
     * @return
     */
    public void updateMessagePush(MessagePush messagePush,HashSet<Long> members){
    	
    	//获得原来是否是所有用户推送
    	MessagePush preMessagePush=messagePushMapper.selectByPrimaryKey(messagePush.getMpId());
    	//设置消息推送默认状态
    	messagePush.setPushStatus(PUSH_STATUS_RUNNABLE);
    	messagePush.setUpdateTime(new Date());
    	messagePush.setCreateTime(preMessagePush.getCreateTime());
    	messagePush.setClickCount(0l);
    	messagePushMapper.updateByPrimaryKeyWithBLOBs(messagePush);
    	//推送用户修改
    	if(members.size()>0){
    		//保存发送用户的id
    		//不是推送所有用户先删除原来推送用户信息，如果原来用户是所有直接添加
    		if(preMessagePush.getIsAll() == IS_NOT_ALL){
    		messagePushMemberMapper.deleteByMpId(messagePush.getMpId());
    		}
    		//添加新的用户信息
    		for(Long mid:members){
    			MessagePushMember mpm=new MessagePushMember();
    			Member m=memberService.selectMemberByMid(mid);
    			if(m != null){
    				mpm.setMemberId(m.getMid());
        			mpm.setMpId(messagePush.getMpId());
        			mpm.setImAccount(m.getImAccount());
        			mpm.setMobilePhone(m.getMobilePhone());
        			messagePushMemberMapper.insertSelective(mpm);
    			}
    		}
    	}
    	
    	
    }
    
    /**
     * 取消推送
     * @param mpId 推送信息id
     * @return
     */
    public Integer removeMessagePush(Long mpId){
    	MessagePush messagePush=messagePushMapper.selectByPrimaryKey(mpId);
    	messagePush.setUpdateTime(new Date());
    	messagePush.setPushStatus(PUSH_STATUS_RUNNABLE);
    	return messagePushMapper.updateByPrimaryKeySelective(messagePush);
    }

   

    /**
     * 
     * @param pageIndex
     * @param pageSize 
     * @param keywords 关键字
     * @param pushStatus 发送状态
     * @return
     */
	public List<MessagePush> getMessagePushWithPageDesc(String title, Byte pushStatus,
			Integer pageIndex,Integer pageSize) {
		Page page = new Page();
		page.setPageIndex(pageIndex);
		page.setPageSize(pageSize);
		List<MessagePush> list= messagePushMapper.selectByKeywordsWithPage(title,pushStatus,page.getPageStartNum(),pageSize);
		return list;
	}

	/**
	 * 获取列表总数
	 * @param title 标题
	 * @param pushStatus 状态
	 * @return
	 */
	public Integer countMessagePush(String title, Byte pushStatus) {
		MessagePush entity=new MessagePush();
		entity.setTitle(title);
		entity.setPushStatus(pushStatus);
		Integer count= messagePushMapper.countMessagePush(entity);
		return count;
	}
	
	/**
	 * 启动消息推送
	 * @param mpId
	 * @return
	 */
	public void transStartMessagePush(final Long mpId) {
		
		final MessagePush messagePush = messagePushMapper.selectByPrimaryKey(mpId);
		
			 //设置推送内容json参数
			 final JSONObject jo=new JSONObject();
	    	 jo.put("img", messagePush.getImg());
	    	 jo.put("innerContent", messagePush.getContent());
	    	 jo.put("pushLink", messagePush.getPushLink());
	    	 jo.put("linkText", messagePush.getLinkText());
	    	 jo.put("person", "");
	    	 jo.put("xmMessageType", XM_CUSTOM_MESSAGE);
	    	 jo.put("mpId", messagePush.getMpId());
	    	 final String jsonStr=jo.toJSONString();
	    	 System.out.println(jo.toJSONString());
	    	 final String option="{push:true}";
			Timer timer=new Timer();
			//判断是发送给部分人还是所有人 
			if(messagePush.getIsAll() == IS_NOT_ALL){
				//修改推送状态  正在推送
				messagePush.setUpdateTime(new Date());
				messagePush.setPushStatus(PUSH_STATUS_RUNNING);
				messagePushMapper.updateByPrimaryKey(messagePush);
				//发送给部分人		
				TimerTask toSome=new TimerTask(){
					//区别定时器标志 
					Date tUpdateTime=messagePushMapper.selectByPrimaryKey(messagePush.getMpId()).getUpdateTime();
					@Override
					public void run() {
						//查看该退送消息是否被取消,重新查询数据库
						MessagePush newMessagePush=messagePushMapper.selectByPrimaryKey(mpId);
						if(newMessagePush.getPushStatus() == PUSH_STATUS_RUNNING && tUpdateTime.equals(newMessagePush.getUpdateTime())){
						
							byte pushType=newMessagePush.getPushType();
							MessagePushMember messagePushMember=new MessagePushMember();
							messagePushMember.setMpId(mpId);
							String pushContent=newMessagePush.getContent().length()>15?newMessagePush.getContent().substring(0, 15):newMessagePush.getContent();
							if(pushType == PUSH_TYPE_PUSH){
								jo.put("direct", true);//安卓添加直接跳转
								imMessageService.neteaseSendBatchMsg(IS_NOT_ALL,mpId,jsonStr,IM_CUSTOM_MESSAGE,pushContent,option,"{'direct':true,'jo':"+jo.toJSONString()+'}',null);
			    			}else{
								jo.put("direct", false);//安卓添加跳转
								imMessageService.neteaseSendBatchMsg(IS_NOT_ALL,mpId,jsonStr,IM_CUSTOM_MESSAGE,pushContent,option,"{'direct':false,'jo':"+jo.toJSONString()+'}',null);
			    			}
							//推送完成修改推送状态
							newMessagePush.setPushStatus(PUSH_STATUS_SUCCESS);
							newMessagePush.setUpdateTime(new Date());
							messagePushMapper.updateByPrimaryKey(newMessagePush);
						}
					}
					
				};
				
				timer.schedule(toSome, messagePush.getPushTime().getTime()-new Date().getTime());
				
			}else{
				
				//修改推送状态  正在推送
				messagePush.setUpdateTime(new Date());
				messagePush.setPushStatus(PUSH_STATUS_RUNNING);
				messagePushMapper.updateByPrimaryKey(messagePush);
				//发送给所有人		
				TimerTask toAll=new TimerTask(){
					//区别定时器标志 
					Date tUpdateTime=messagePushMapper.selectByPrimaryKey(messagePush.getMpId()).getUpdateTime();
					@Override
					public void run() {
						//查看该退送消息是否被取消,重新查询数据库
						MessagePush newMessagePush=messagePushMapper.selectByPrimaryKey(mpId);
						
						//当为定时器的标志与最新更新时间相同 状态正确才会执行
						if(newMessagePush.getPushStatus() == PUSH_STATUS_RUNNING && tUpdateTime.equals(newMessagePush.getUpdateTime())){
							String pushContent=newMessagePush.getContent().length()>15?newMessagePush.getContent().substring(0, 15):newMessagePush.getContent();
							byte pushType=newMessagePush.getPushType();
							if(pushType == PUSH_TYPE_PUSH){
			    				//push推送
								jo.put("direct", true);//安卓添加直接跳转
								imMessageService.neteaseSendBatchMsg(IS_ALL,mpId,jsonStr,IM_CUSTOM_MESSAGE,pushContent,option,"{'direct':true,'jo':"+jo.toJSONString()+'}',null);
							}else{
			    				//全推送
								jo.put("direct", false);//安卓添加跳转
								imMessageService.neteaseSendBatchMsg(IS_ALL,mpId,jsonStr,IM_CUSTOM_MESSAGE,pushContent,option,"{'direct':false,'jo':"+jo.toJSONString()+'}',null);
			    			}
							newMessagePush.setPushStatus(PUSH_STATUS_SUCCESS);
							//推送完成修改推送状态
							newMessagePush.setUpdateTime(new Date());
							messagePushMapper.updateByPrimaryKey(newMessagePush);
						}
					}
					
				};
				
				timer.schedule(toAll, messagePush.getPushTime().getTime()-new Date().getTime());
			}
		
	}
	
}





