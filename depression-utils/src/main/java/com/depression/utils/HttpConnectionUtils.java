package com.depression.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http连接
 * 
 * @author ax
 * 
 */
public class HttpConnectionUtils
{

	public static String getJson(String urlStr)
	{
		try
		{
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			InputStream inputStream = null;
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				inputStream = conn.getInputStream();
				ByteArrayOutputStream byteArrOutputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				String jsonStr = "";
				while ((len = inputStream.read(buffer)) != -1)
				{
					byteArrOutputStream.write(buffer, 0, len);
				}
				jsonStr = byteArrOutputStream.toString();
				byteArrOutputStream.close();
				inputStream.close();
				return jsonStr;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
