package com.depression.entity;

import com.depression.utils.Configuration;
import com.depression.utils.IdWorker;

public class Constant
{

	// 流水单号
	public static IdWorker idWorker = new IdWorker(1);

	// 用户会话id
	public static String LOGINED = "userId";
	
	// 管理员会话id
	public static String ADMIN_LOGINED = "adminUserId";
	
	// 管理员级别
	public static String ADMIN_LEVEL = "adminLevel";
	
	// 权限类型authority
	public static String ADMIN_AUTHORITY = "adminAuthority";
	
	//验证码开始时间
	public static String CODE_TIME = "codeTime";
	
	//验证码失效时间
	public static long TIME = 5*60;
	
	//系统初始密码
    public static String INITIALPASSWORD = "123456";
    
    public static String ADMIN_EEID = "eeid";

	// 人格测试分享id
	public static String PERSONALITY_TEST_SHARE_ID = "PersonalityTestShareId";

	// 百度推送 android
	public static String ANDROID_API_KEY = "sKIs5obgFOVCGaelfhlUam16";
	public static String ANDROID_SECRET_KEY = "vN5Lgghp15QoAtCsqUdvQjGkVAvR4ZxO";
	
	// 百度推送 IOS
	public static String IOS_API_KEY = "nTV4OoPZFO3ShNPGZzjXdIOB";
	public static String IOS_SECRET_KEY = "zHFroAFlGTZRzGaPGeIakiOLofWOEdxy";
}
