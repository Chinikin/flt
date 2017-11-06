package com.depression.base.wechat.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.depression.base.wechat.util.StringUtil;
import com.depression.base.wechat.util.WeixinUtil;

public class QRcodeService
{
	public static Logger log = Logger.getLogger(QRcodeService.class);

	static String QRCODE_ACTION = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";

	static String QRCODE_IMG_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";

	static String QRCODE_SCENE = "QR_SCENE";

	static String QRCODE_LIMIT_SCENE = "QR_LIMIT_SCENE";

	public static String getTicket(String actionName, int sceneId)
	{
		String url = QRCODE_ACTION.replace("TOKEN", WeixinUtil.getToken());

		String ticket = "";

		String qrdata = "{\"action_name\": \"" + actionName + "\", \"action_info\": {\"scene\": {\"scene_id\": " + sceneId + "}}}";

		JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", qrdata);
		if (jsonObject != null)
		{
			if ((StringUtil.isNotEmpty(jsonObject.get("errcode"))) && (jsonObject.get("errcode") != "0"))
				log.error("二维码ticket请求失败，errcode:" + jsonObject.getInt("errcode") + "，errmsg:" + jsonObject.getString("errmsg"));
			else
			{
				ticket = jsonObject.getString("ticket");
			}
		}
		return ticket;
	}

	public static String getQrCodeImgURL(String ticket)
	{
		try
		{
			ticket = URLEncoder.encode(ticket, "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return QRCODE_IMG_URL.replace("TICKET", ticket);
	}
}
