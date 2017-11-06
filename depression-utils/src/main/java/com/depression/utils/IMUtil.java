package com.depression.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.CCPRestSDK;
import com.depression.base.neteaseim.MainTest;
import com.depression.base.neteaseim.NIMPost;
import com.depression.base.neteaseim.UUIDUtil;
import com.depression.entity.Constant;
import com.depression.entity.VoipUser;

public class IMUtil
{
	private static Logger logger = Logger.getLogger(MainTest.class);

	public static VoipUser ronglianAccountRegister(String accountName)
	{
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("8a216da854ff8dcc0154ffa84f07005b", "2c36ed07e422442c9da379b55c46a340");// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId(Configuration.RONG_LIAN_APPID);// 初始化应用ID
		result = restAPI.createSubAccount(accountName);

		System.out.println("SDKTestCreateSubAccount result=" + result);

		if ("000000".equals(result.get("statusCode")))
		{
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet)
			{
				Object object = data.get(key);

				return ronglianGetVoipUser(object.toString());
			}
		} else
		{
			// 异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
		}
		return null;
	}

	private static VoipUser ronglianGetVoipUser(String data)
	{
		data = data.replace("[{", "");
		data = data.replace("}]", "");
		String[] splits = data.split(",");
		VoipUser user = new VoipUser();
		for (String split : splits)
		{
			String[] sp = split.split("=");
			String key = sp[0].trim();
			String value = sp[1].trim();

			if ("subAccountSid".equals(key))
			{
				user.setSubAccountSid(value);
			} else if ("subToken".equals(key))
			{
				user.setSubToken(value);
			} else if ("voipAccount".equals(key))
			{
				user.setVoipAccount(value);
			} else if ("voipPwd".equals(key))
			{
				user.setVoipPwd(value);
			} else if ("dateCreated".equals(key))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dateCreated;
				try
				{
					dateCreated = sdf.parse(value);
					user.setDateCreated(dateCreated);
				} catch (ParseException e)
				{
					e.printStackTrace();
				}
			}
		}
		return user;
	}
	
	public static String netease_prefix = "netease";
	public static IdWorker neteaseIdWorker = new IdWorker(0);
	
	/**
	 * 生成网易账号
	 * @return
	 */
	public static String neteaseGenAccid()
	{
		return netease_prefix + neteaseIdWorker.nextId();
	}
	/**
	 * 检测是否是网易账号
	 * @param accid
	 * @return
	 */
	public static boolean neteaseIsAccid(String accid)
	{
		if(accid==null) return false;
		return accid.startsWith(netease_prefix);
	}
	
	/**
	 * 注册网易账号
	 * @param name 昵称
	 * @param icon 
	 * @return
	 */
	public static VoipUser neteaseUserCreate(String name, String icon, String props)
	{
		String url = "https://api.netease.im/nimserver/user/create.action";
		String accid = neteaseGenAccid();
		String token = UUIDUtil.getUUID();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("accid", accid));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("icon", icon));
        if(props!=null)
        {
        	params.add(new BasicNameValuePair("props", props));
        }
        params.add(new BasicNameValuePair("token", token));

        JSONObject resJson;
		try
		{
			//UTF-8编码,解决中文问题
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			String res = NIMPost.postNIMServer(url, entity, Configuration.NETEASE_APPKEY, Configuration.NETEASE_APPSECRET);
			resJson = JSON.parseObject(res);
			
			VoipUser voipUser = new VoipUser();
			if(resJson.getInteger("code").equals(200))
			{
				voipUser.setVoipAccount(accid);
				voipUser.setVoipPwd(token);
				return voipUser;
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return null;
		}
					
       return null;
	}
	
	/**
	 * 更新网易账号
	 * @param accid 云信ID
	 * @param props json属性
	 * @return
	 */
	public static Integer neteaseUserUpdate(String accid, String props)
	{
		String url = "https://api.netease.im/nimserver/user/update.action";
		List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("accid", accid));
        if(props!=null)
        {
        	params.add(new BasicNameValuePair("props", props));
        }

        JSONObject resJson;
		try
		{
			//UTF-8编码,解决中文问题
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			String res = NIMPost.postNIMServer(url, entity, Configuration.NETEASE_APPKEY, Configuration.NETEASE_APPSECRET);
			resJson = JSON.parseObject(res);
			
			if(resJson.getInteger("code").equals(200))
			{
				return 0;
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return 1;
		}
			
       return 2;
	}
	
	/**
	 * 更新网易用户名片
	 * @param accid 网易id
	 * @param name 昵称
	 * @param icon 头像
	 * @return
	 */
	public static Integer neteaseUserUpdateUinfo(String accid, String name, String icon, String ex)
	{
		String url = "https://api.netease.im/nimserver/user/updateUinfo.action";
		List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("accid", accid));
        if(name!=null)
        {
        	params.add(new BasicNameValuePair("name", name));
        }
        if(icon!=null)
        {
        	params.add(new BasicNameValuePair("icon", icon));
        }
        if(ex!=null)
        {
        	params.add(new BasicNameValuePair("ex", ex));
        }
        JSONObject resJson;
		try
		{
			//UTF-8编码,解决中文问题
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			String res = NIMPost.postNIMServer(url, entity, Configuration.NETEASE_APPKEY, Configuration.NETEASE_APPSECRET);
			resJson = JSON.parseObject(res);
			
			if(resJson.getInteger("code").equals(200))
			{
				return 0;
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return 1;
		}
		
       return 2;
	}
	
	/**
	 * 获取网易用户名片
	 * @param accid
	 * @return
	 */
	public static Integer neteaseUserGetUinfos(String accid)
	{
		String url = "https://api.netease.im/nimserver/user/getUinfos.action";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		List<String> sa = new ArrayList<String>();
		sa.add(accid);
		String accids = JSONArray.toJSONString(sa);
        params.add(new BasicNameValuePair("accids", accids));

        JSONObject resJson;
		try
		{
			//UTF-8编码,解决中文问题
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			String res = NIMPost.postNIMServer(url, entity, Configuration.NETEASE_APPKEY, Configuration.NETEASE_APPSECRET);
			resJson = JSON.parseObject(res);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return 1;
		}
		
		if(resJson.getInteger("code").equals(200))
		{
			return 0;
		}
			
       return 2;
	}

	/**
	 * 
	 * @param from 发送者accid
	 * @param ope 0：点对点个人消息，1：群消息，
	 * @param to ope==0是表示accid即用户id，ope==1表示tid即群id
	 * @param type 0 表示文本消息
	 * @param body 消息字段
	 * @param option
	 * @param pushcontent
	 * @param payload
	 * @return
	 */
	public static Integer neteaseSendMsg(String from,String ope,String to,String type,String body,
			String option,String pushcontent,String payload){
		String url = "https://api.netease.im/nimserver/msg/sendMsg.action";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from", from));
        params.add(new BasicNameValuePair("ope", ope));
        params.add(new BasicNameValuePair("to", to));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("body", body));
        if(option != null){
        	params.add(new BasicNameValuePair("option", option));
        }
        if(pushcontent != null){
        	params.add(new BasicNameValuePair("pushcontent", pushcontent));
        }
        if(payload != null){
        	params.add(new BasicNameValuePair("payload", payload));
        }
        
        JSONObject resJson;
		try{
			//UTF-8编码,解决中文问题
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			String res = NIMPost.postNIMServer(url, entity, Configuration.NETEASE_APPKEY, Configuration.NETEASE_APPSECRET);
			resJson = JSON.parseObject(res);
			
		} catch (Exception e){
			logger.error(e.getMessage());
			return 1;
		}
		
		if(resJson.getInteger("code").equals(200)){
			return 0;
		}
			
       return 2;
	}
	
	/**
	 * 
	 * @param fromAccid
	 * @param toAccids
	 * @param type
	 * @param body
	 * @param option
	 * @param pushcontent
	 * @param payload
	 * @return
	 */
	public static Integer neteaseSendBatchMsg(String fromAccid,String toAccids,String type,String body,String option,
			String pushcontent,String payload){
		String url = "https://api.netease.im/nimserver/msg/sendBatchMsg.action";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fromAccid", fromAccid));
        params.add(new BasicNameValuePair("toAccids", toAccids));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("body", body));
        if(option != null){
        	params.add(new BasicNameValuePair("option", option));
        }
        if(pushcontent != null){
        	params.add(new BasicNameValuePair("pushcontent", pushcontent));
        }
        if(payload != null){
        	params.add(new BasicNameValuePair("payload", payload));
        }
        JSONObject resJson;
		try{
			//UTF-8编码,解决中文问题
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			String res = NIMPost.postNIMServer(url, entity, Configuration.NETEASE_APPKEY, Configuration.NETEASE_APPSECRET);
			logger.info("logging--------------"+res);
			
			resJson = JSON.parseObject(res==null?"{code:500}":res);
			
		} catch (Exception e){
			logger.error(e.getMessage());
			return 1;
		}
		
		if(resJson.getInteger("code").equals(200)){
			return 0;
		}
			
       return 2;
	}
	
}
