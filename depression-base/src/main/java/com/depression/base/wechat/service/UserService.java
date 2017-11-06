package com.depression.base.wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.depression.base.wechat.entity.user.UserWeiXin;
import com.depression.base.wechat.util.DateFormart;
import com.depression.base.wechat.util.StringUtil;
import com.depression.base.wechat.util.WeixinUtil;

public class UserService
{
	public static Logger log = Logger.getLogger(UserService.class);

	public static String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	public static String GET_USER_OPENID_LIST = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

	public static UserWeiXin getUserInfo(String openid)
	{
		String token = WeixinUtil.getToken();

		UserWeiXin user = null;

		if (token != null)
		{
			String url = GET_USER_INFO.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
			JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", null);

			if (jsonObject != null)
			{
				if ((StringUtil.isNotEmpty(jsonObject.get("errcode"))) && (jsonObject.get("errcode") != "0"))
				{
					log.error("获取用户信息失败 errcode:" + jsonObject.getInt("errcode") + "，errmsg:" + jsonObject.getString("errmsg"));
				} else
				{
					user = new UserWeiXin();
					user.setSubscribe(Integer.valueOf(jsonObject.getInt("subscribe")));
					user.setOpenid(jsonObject.getString("openid"));
					user.setNickname(jsonObject.getString("nickname"));
					user.setSex(Integer.valueOf(jsonObject.getInt("sex")));
					user.setCity(jsonObject.getString("city"));
					user.setCountry(jsonObject.getString("country"));
					user.setProvince(jsonObject.getString("province"));
					user.setLanguage(jsonObject.getString("language"));
					user.setHeadimgurl(jsonObject.getString("headimgurl"));
					long subscibeTime = jsonObject.getLong("subscribe_time");
					Date st = DateFormart.paserYYYY_MM_DD_HHMMSSToDate(subscibeTime);
					user.setSubscribe_time(st);
				}
			}

		}

		return user;
	}

	public static List<String> getUserOpenIdList()
	{
		String token = WeixinUtil.getToken();
		List list = null;
		if (token != null)
		{
			String url = GET_USER_OPENID_LIST.replace("ACCESS_TOKEN", token).replace("NEXT_OPENID", "");

			JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", null);

			if (jsonObject != null)
			{
				if ((StringUtil.isNotEmpty(jsonObject.get("errcode"))) && (jsonObject.get("errcode") != "0"))
				{
					log.error("获取关注用户列表失败 errcode:" + jsonObject.getInt("errcode") + "，errmsg:" + jsonObject.getString("errmsg"));
				} else
				{
					list = new ArrayList();
					JSONObject data = jsonObject.getJSONObject("data");
					String openidStr = data.getString("openid");
					openidStr = openidStr.substring(1, openidStr.length() - 1);
					openidStr = openidStr.replace("\"", "");
					String[] openidArr = openidStr.split(",");
					for (int i = 0; i < openidArr.length; i++)
					{
						list.add(openidArr[i]);
					}
				}
			}
		}

		return list;
	}

	public static List<UserWeiXin> getUserList()
	{
		List list = new ArrayList();

		List listStr = getUserOpenIdList();

		if ((listStr == null) || (listStr.size() == 0))
		{
			return null;
		}
		for (int i = 0; i < listStr.size(); i++)
		{
			UserWeiXin user = getUserInfo((String) listStr.get(i));
			if (user != null)
			{
				list.add(user);
			}
		}
		return list;
	}
}
