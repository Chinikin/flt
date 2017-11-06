package com.depression.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.depression.dao.PushDevTypeMapper;
import com.depression.model.PushDevType;
import com.depression.push.AndroidPushServer;
import com.depression.push.CustomContent;
import com.depression.push.CustomMsgType;
import com.depression.push.IOSPushServer;
import com.depression.push.PushMsg;

/**
 * 提供推送服务，目前支持百度云推送
 * @author caizj
 *
 */
@Service
public class PushService
{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	PushDevTypeMapper pushDevTypeMapper;
	
	/**
	 * 维护用户的设备信息
	 * @param mid 会员id
	 * @param devType 设备类型
	 * @param channelId 设备id
	 * @param userId app端用户标识
	 * @return
	 */
	public Integer maintainDevType(Long mid, Integer devType, String channelId, String userId)
	{
		PushDevType pdt = new PushDevType();
		pdt.setMid(mid);
		List<PushDevType> pdts = pushDevTypeMapper.selectSelective(pdt);
		if(pdts.size() > 0)
		{//已有记录更新
			PushDevType pushDevType = pdts.get(0);
			pushDevType.setDevType(devType);
			pushDevType.setChannelId(channelId);
			pushDevType.setUserId(userId);
			
			return pushDevTypeMapper.updateByPrimaryKeySelective(pushDevType);
		}else
		{//新建记录
			PushDevType pushDevType = new PushDevType();
			pushDevType.setMid(mid);
			pushDevType.setDevType(devType);
			pushDevType.setChannelId(channelId);
			pushDevType.setUserId(userId);
			
			return pushDevTypeMapper.insertSelective(pushDevType);
		}
	}
	
	/**
	 * 获取用户设备信息
	 * @param mid 会员id
	 * @return
	 */
	public PushDevType obtainDevType(Long mid)
	{
		PushDevType pdt = new PushDevType();
		pdt.setMid(mid);
		List<PushDevType> pdts = pushDevTypeMapper.selectSelective(pdt);
		if(pdts.size() > 0)
		{
			return pdts.get(0);
		}
		
		return null;
	}

	private PushMsg setWardmateCommentPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.WARDMATE_COMMENTS_MSG);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("您的心友圈被评论，点击查看");
	    msg.setTitle("心友圈通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setWardmateCommentTOATPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.WARDMATE_COMMENTS_TO_AT);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("心友圈评论被回复，点击查看");
	    msg.setTitle("心友圈评论通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setAnonymityCommentPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.ANONYMITY_COMMENTS_MSG);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("您的心友圈被评论，点击查看");
	    msg.setTitle("心友圈通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setAnonymityCommentTOATPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.ANONYMITY_COMMENTS_TO_AT);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("心友圈评论被回复，点击查看");
	    msg.setTitle("心友圈评论通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setArticleCommentTOATPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.ARTICLE_COMMENTS_MSG_TO_AT);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("文章评论被回复，点击查看");
	    msg.setTitle("文章评论通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setTestCommentTOATPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.ARTICLE_COMMENTS_MSG_TO_AT);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("测试评论被回复，点击查看");
	    msg.setTitle("测试评论通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setAdvisoryCommentPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.ARTICLE_COMMENTS_MSG_TO_AT);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("您的提问被专家回复，点击查看");
	    msg.setTitle("提问评论通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setAdvisoryCommentTOATPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.ARTICLE_COMMENTS_MSG_TO_AT);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("您的提问回复被评论，点击查看");
	    msg.setTitle("提问评论通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setSystemConPush(String cid, String uId)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.SYSTEM_MSG);
	    cus.setUserID(uId);
	    msg.setCustomContent(cus);
	    msg.setDescription("您有新的关注，点击查看");
	    msg.setTitle("系统消息通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setCallBackPush(String cid, String uId, String jsonStr)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.CALL_BACK_MSG);
	    cus.setUserID(uId);
	    cus.setJsonContent(jsonStr);
	    msg.setCustomContent(cus);
	    msg.setDescription("您的通话服务详单，点击查看");
		msg.setTitle("通话服务通知");
	    msg.setUserID(uId);
	    return msg;
	}
	
	public PushMsg setMemberConcernPush(String cid, String uId, String jsonStr)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.MEMBER_CONCERN);
	    cus.setUserID(uId);
	    cus.setJsonContent(jsonStr);
	    msg.setCustomContent(cus);
	    msg.setDescription("您有新的关注，点击查看");
		msg.setTitle("系统消息通知");
	    msg.setUserID(uId);
	    return msg;
	}

	private PushMsg setCustomMsgPush(String cid, String uId, String jsonStr)
	{
	    PushMsg msg = new PushMsg();
	    msg.setChannelId(cid);
	    CustomContent cus = new CustomContent();
	    cus.setMsgType(CustomMsgType.CUSTOM_PUSH_MSG);
	    cus.setUserID(uId);
	    cus.setJsonContent(jsonStr);
	    msg.setCustomContent(cus);
	    msg.setDescription("您的有一条新的消息，点击查看");
		msg.setTitle("自定义推送服务通知");
	    msg.setUserID(uId);
	    return msg;
	}
	
	/**
	 * 构建推送消息
	 * @param msgType 消息类型
	 * @param cid channelId
	 * @param uid UserId
	 * @param jsonStr 额外json字符串
	 * @return
	 */
	private PushMsg buildPushMsg(int msgType, String cid, String uid, String jsonStr)
	{
		PushMsg msg = null;
		switch (msgType)
		{
			case CustomMsgType.WARDMATE_COMMENTS_MSG:
				msg = setWardmateCommentPush(cid, uid);
				break;
			case CustomMsgType.WARDMATE_COMMENTS_TO_AT:
				msg = setWardmateCommentTOATPush(cid, uid);
				break;
			case CustomMsgType.ANONYMITY_COMMENTS_MSG:
				msg = setAnonymityCommentPush(cid, uid);
				break;
			case CustomMsgType.ANONYMITY_COMMENTS_TO_AT:
				msg = setAnonymityCommentTOATPush(cid, uid);
				break;
			case CustomMsgType.ARTICLE_COMMENTS_MSG_TO_AT:
				msg = setArticleCommentTOATPush(cid, uid);
				break;
			case CustomMsgType.TEST_COMMENTS_MSG_TO_AT:
				msg = setTestCommentTOATPush(cid, uid);
				break;
			case CustomMsgType.ADVISORY_COMMENTS_MSG:
				msg = setAdvisoryCommentPush(cid, uid);
				break;
			case CustomMsgType.SYSTEM_MSG:
				msg = setSystemConPush(cid, uid);
				break;
			case CustomMsgType.ADVISORY_COMMENTS_MSG_TO_AT:
				msg = setAdvisoryCommentTOATPush(cid, uid);
				break;
			case CustomMsgType.CALL_BACK_MSG:
				msg = setCallBackPush(cid, uid, jsonStr);
				break;
			case CustomMsgType.MEMBER_CONCERN:
				msg = setMemberConcernPush(cid, uid, jsonStr);
				break;		
			case CustomMsgType.CUSTOM_PUSH_MSG:
				msg = setCustomMsgPush(cid, uid, jsonStr);
				break;			
		}
		
		return msg;
	}
	
	/**
	 * 推送到单个设备
	 * @param msgType
	 * @param mid
	 * @param jsonStr
	 * @return
	 */
	public Integer pushSingleDevice(int msgType, Long mid, String jsonStr)
	{
		PushDevType pushDevType = obtainDevType(mid);
		if(pushDevType == null)
		{//无设备信息
			return 1;
		}
		
		PushMsg msg = buildPushMsg(msgType, pushDevType.getChannelId(), pushDevType.getUserId(), jsonStr);
		if(msg == null)
		{//无对应消息
			return 2;
		}
		try{
		if(pushDevType.getDevType()==0)
			{//安卓
				AndroidPushServer.pushSingleDevice(msg);
			}else if(pushDevType.getDevType()==1)
			{
				IOSPushServer.pushSingleDevice(msg);
			}
		}catch(Exception e)
		{//推送异常
			log.info("Baidu Push failed", e);
			return 3;
		}
		
		return 0;
	}
	
	/**
	 * 推送到所有设备
	 * @param msgType
	 * @param jsonStr
	 * @return
	 */
	public Integer pushAllDevice(int msgType, String jsonStr)
	{
		PushMsg msg = buildPushMsg(msgType, null, null, jsonStr);
		if(msg == null)
		{//无对应消息
			return 2;
		}
		
		try{
			AndroidPushServer.pushMsgToAll(msg);
			IOSPushServer.pushMsgToAll(msg);
		}catch(Exception e)
		{//推送异常
			log.info("Baidu Push failed", e);
			return 3;
		}
		
		return 0;
	}
	
	
}
