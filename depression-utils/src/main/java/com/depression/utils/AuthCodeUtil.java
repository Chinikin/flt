package com.depression.utils;

import java.util.Random;

/**
 * @author:ziye_huang
 * @date:2016年5月6日
 */

public final class AuthCodeUtil
{

	public static String getAuthCode(int authCodeLength)
	{
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < authCodeLength; i++)
		{
			int num = random.nextInt(10);
			sb.append(num);
		}
		return sb.toString();
	}
	
}
