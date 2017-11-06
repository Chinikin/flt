package com.depression.base.wechat.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.depression.base.wechat.entity.message.resp.BaseMessage;
import com.depression.base.wechat.entity.message.resp.MediaMessage;
import com.depression.base.wechat.entity.message.resp.MusicMessage;
import com.depression.base.wechat.entity.message.resp.NewsMessage;
import com.depression.base.wechat.entity.message.resp.TextMessage;
import com.depression.base.wechat.entity.message.resp.VideoMessage;
import com.depression.base.wechat.util.MessageUtil;

public class MessageService
{
	public static Map<String, BaseMessage> bulidMessageMap = new HashMap();

	static
	{
		bulidMessageMap.put("text", new TextMessage());
		bulidMessageMap.put("news", new NewsMessage());
		bulidMessageMap.put("image", new MediaMessage());
		bulidMessageMap.put("voice", new MediaMessage());
		bulidMessageMap.put("video", new VideoMessage());
		bulidMessageMap.put("music", new MusicMessage());
	}

	public static Object bulidBaseMessage(Map<String, String> requestMap, String msgType)
	{
		String fromUserName = (String) requestMap.get("FromUserName");

		String toUserName = (String) requestMap.get("ToUserName");

		BaseMessage message = (BaseMessage) bulidMessageMap.get(msgType);
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(msgType);
		message.setFuncFlag(0);
		return message;
	}

	public static String bulidSendMessage(Object obj, String msgType)
	{
		String responseMessage = "";

		if (msgType == "news")
			responseMessage = MessageUtil.newsMessageToXml((NewsMessage) obj);
		else
		{
			responseMessage = MessageUtil.messageToXml(obj);
		}
		return responseMessage;
	}
}
