package com.depression.base.wechat.service;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import com.depression.base.wechat.entity.customer.CustomerBaseMessage;
import com.depression.base.wechat.entity.customer.MediaMessage;
import com.depression.base.wechat.entity.customer.MusicMessage;
import com.depression.base.wechat.entity.customer.NewsMessage;
import com.depression.base.wechat.entity.customer.TextMessage;
import com.depression.base.wechat.entity.customer.VideoMessage;
import com.depression.base.wechat.util.StringUtil;
import com.depression.base.wechat.util.WeixinUtil;

public class CustomService
{
	public static Logger log = Logger.getLogger(CustomService.class);

	private static String CUSTOME_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	public static Map<String, CustomerBaseMessage> bulidMessageMap = new HashMap();

	static
	{
		bulidMessageMap.put("text", new TextMessage());
		bulidMessageMap.put("news", new NewsMessage());
		bulidMessageMap.put("image", new MediaMessage());
		bulidMessageMap.put("voice", new MediaMessage());
		bulidMessageMap.put("video", new VideoMessage());
		bulidMessageMap.put("music", new MusicMessage());
	}

	public static boolean sendCustomerMessage(Object obj)

	{
		boolean bo = false;
		String url = CUSTOME_URL.replace("ACCESS_TOKEN", WeixinUtil.getToken());
		JSONObject json = JSONObject.fromObject(obj);
		System.out.println(json);
		JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", json.toString());
		if ((jsonObject != null) && (StringUtil.isNotEmpty(jsonObject.getString("errcode"))) && (jsonObject.getString("errcode").equals("0")))
		{
			bo = true;
		}

		return bo;
	}

	public static Object bulidCustomerBaseMessage(String toUser, String msgType)
	{
		CustomerBaseMessage message = (CustomerBaseMessage) bulidMessageMap.get(msgType);
		message.setTouser(toUser);
		message.setMsgtype(msgType);
		return message;
	}
}
