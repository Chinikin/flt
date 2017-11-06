package com.depression.base.wechat.service;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.depression.base.wechat.entity.menu.Menu;
import com.depression.base.wechat.util.WeixinUtil;

public class MenuService
{
	public static Logger log = Logger.getLogger(MenuService.class);

	public static String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	public static Integer createMenu(String jsonMenu)
	{
		String token = WeixinUtil.getToken();
		if (token != null)
		{
			return Integer.valueOf(createMenu(jsonMenu, token));
		}
		return null;
	}

	public static Integer createMenu(Menu menu)
	{
		String token = WeixinUtil.getToken();
		if (token != null)
		{
			return Integer.valueOf(createMenu(menu, token));
		}
		return null;
	}

	public static int createMenu(String jsonMenu, String accessToken)
	{
		int result = 0;

		String url = MENU_CREATE.replace("ACCESS_TOKEN", accessToken);

		JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", jsonMenu);

		if ((jsonObject != null) && (jsonObject.getInt("errcode") != 0))
		{
			result = jsonObject.getInt("errcode");
			log.error("创建菜单失败 errcode:" + jsonObject.getInt("errcode") + "，errmsg:" + jsonObject.getString("errmsg"));
		}

		return result;
	}

	public static int createMenu(Menu menu, String accessToken)
	{
		String jsonMenu = JSONObject.fromObject(menu).toString();
		return createMenu(jsonMenu, accessToken);
	}
}
