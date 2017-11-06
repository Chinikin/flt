package com.depression.service;

import java.security.MessageDigest;

import org.springframework.stereotype.Service;

/**
 * 数据加解密
 * 
 * @author ax
 * 
 */
@Service
public class EncryptService
{

	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 计算指定字节数组的MD5值
	 * 
	 * @param bytes
	 * @return
	 */
	public String encodeMd5(byte[] bytes)
	{
		MessageDigest md5;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
			md5.update(bytes);
			byte[] encodedRaw = md5.digest();
			int j = encodedRaw.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++)
			{
				byte byte0 = encodedRaw[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e)
		{
			
		}
		return null;
	}

	/**
	 * 计算指定字符串的MD5值
	 * 
	 * @param value
	 * @return
	 */
	public String encodeMd5(String value)
	{
		MessageDigest md5;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
			md5.update(value.getBytes("utf-8"));
			byte[] encodedRaw = md5.digest();
			int j = encodedRaw.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++)
			{
				byte byte0 = encodedRaw[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e)
		{

		}
		return null;
	}

}
