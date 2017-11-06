package com.depression.base.wechat.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.depression.base.wechat.constant.ConstantWeChat;
import com.depression.base.wechat.entity.AccessTicket;
import com.depression.base.wechat.entity.AccessToken;

public class WeixinUtil

{
	public static Logger log = Logger.getLogger(WeixinUtil.class);

	public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	public static final String ACCESS_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	public static String access_token;

	public static Date access_token_date;

	public static String access_ticket;

	public static Date access_ticket_date;

	public static AccessToken getAccessToken(String appid, String appsecret)

	{
		AccessToken accessToken = null;

		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET".replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

		if (jsonObject != null)
		{
			try
			{
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e)
			{
				accessToken = null;

				log.error("获取token失败 errcode:" + jsonObject.getInt("errcode") + "，errmsg:" + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	public static AccessTicket getAccessTicket(String accessToken)

	{
		AccessTicket accessTicket = null;

		String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi".replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

		if (jsonObject != null)
		{
			try
			{
				accessTicket = new AccessTicket();
				accessTicket.setTicket(jsonObject.getString("ticket"));
				accessTicket.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e)
			{
				accessToken = null;

				log.error("获取ticket失败 errcode:" + jsonObject.getInt("errcode") + "，errmsg:" + jsonObject.getString("errmsg"));
			}
		}
		return accessTicket;
	}

	public static String getToken()

	{

		if (null == access_token || "".equals(access_token) || null == access_token_date || (new Date().getTime() - access_token_date.getTime()) >= (7000 * 1000))
		{
			try
			{

				AccessToken at = getAccessToken(ConstantWeChat.APPID, ConstantWeChat.APPSECRET);
				access_token = at.getToken();
				access_token_date = new Date();
			} catch (Exception e)
			{
				log.error(e);
			}
		}
		return access_token;
	}

	public static String getTicket()

	{
		String accessToken = getToken();
		if (null == access_ticket || "".equals(access_ticket) || null == access_ticket_date || (new Date().getTime() - access_ticket_date.getTime()) >= (7000 * 1000))
		{
			try
			{

				AccessTicket at = getAccessTicket(accessToken);
				access_ticket = at.getTicket();
				access_ticket_date = new Date();
			} catch (Exception e)
			{
				log.error(e);
			}
		}
		return access_ticket;
	}

	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr)

	{
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try
		{
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
			{
				httpUrlConn.connect();
			}

			if (outputStr != null)
			{
				OutputStream outputStream = httpUrlConn.getOutputStream();

				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null)
			{
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce)
		{
			log.error("server connection timed out.");
		} catch (Exception e)
		{
			log.error("https request error:", e);
		}
		return jsonObject;
	}

	public static String formatTime(String createTime)

	{
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}

}
