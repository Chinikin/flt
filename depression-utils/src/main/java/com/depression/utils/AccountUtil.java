package com.depression.utils;

/**
 * 账号生成，格式：AC+16位数字
 * 
 * @author:ziye_huang
 * @date:2016年8月28日
 */

public class AccountUtil
{

	/**
	 * 生成账号 格式：AC+16位数字
	 * 
	 * @return
	 */
	private static IdWorker accountIdWorker = new IdWorker(1);
	public static String generateAccount()
	{
		return "AC" + accountIdWorker.nextId();
	}

	/**
	 * 生成编号(现金收支明细用到)
	 * 
	 * @return
	 */
	private static IdWorker noIdWorker = new IdWorker(2);
	public static String generateNo()
	{
		return noIdWorker.nextId() + "";
	}

}
