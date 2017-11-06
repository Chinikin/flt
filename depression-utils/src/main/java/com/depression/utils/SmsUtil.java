package com.depression.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cloopen.rest.sdk.CCPRestSDK;

public class SmsUtil
{
	public static void main(String[] args)
	{
		sendSms("13867451910", "140581", "菜");
		sendSms("13867451910", "140583", "菜","相片不清楚");
		sendSms("13867451910", "149441", "365211", "5");
	}
	
	/**
	 * 发送短信
	 * @param mobilePhone
	 * @param templeId	模板id
	 * @param datas	参数列表
	 * @return
	 */
	public static boolean sendSms(String mobilePhone, String templeId, String... datas)
	{
		HashMap map = null;
		String appId = "8a216da85582647801559fe151d91ad0";
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");
		// 初始化服务器地址和端口，沙盒环境配置成sandboxapp.cloopen.com，生产环境配置成app.cloopen.com，端口都是8883.
		restAPI.setAccount("8a216da854ff8dcc0154ffa84f07005b", "2c36ed07e422442c9da379b55c46a340");
		// 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在"控制台-应用"中看到开发者主账号ACCOUNT SID和 主账号令牌AUTH TOKEN。
		restAPI.setAppId(appId);
		// 初始化应用ID，如果是在沙盒环境开发，请配置"控制台-应用-测试DEMO"中的APPID。
		// 如切换到生产环境，请使用自己创建应用的APPID
		// result = restAPI.sendTemplateSMS("号码1,号码2等", "模板Id", new String[] { "模板内容1", "模板内容2" });
		List<String> datasList = new ArrayList<String>();
		for(String s : datas)
		{
			datasList.add(s);
		}
		map = restAPI.sendTemplateSMS(mobilePhone, templeId, datasList.toArray(new String[datasList.size()]));
		
		return "000000".equals(map.get("statusCode"));
	}
}
