package com.depression.utils;

import java.util.ResourceBundle;

public class Configuration
{
	public static final String FRONT_SERVER_HOST;// 前端代码服务器地址
	public static final String UPLOAD_SERVICE_SERVER_HOST;// 文件上传服务器地址
	public static final String API_SERVICE_SERVER_HOST;// 接口服务器地址
	public static final String WECHAT_TOKEN;// 微信token
	public static final String WECHAT_APPID;// 微信APPID
	public static final String WECHAT_APPSECRET;// 微信密钥
	public static final String QINIU_ACCESSKEY;// 七牛访问钥匙
	public static final String QINIU_SECRETKEY;// 七牛机密钥匙
	public static final String QINIU_BUCKET_FILE; // 七牛空间名
	public static final String QINIU_BUCKET_PICTURE; // 七牛空间名
	public static final String QINIU_DOMAIN_FILE; // 七牛域名
	public static final String QINIU_DOMAIN_PICTURE; // 七牛域名
	public static final String RONG_LIAN_APPID;// 容联APPID
	public static final String RONG_LIAN_CALL_PHONE_NUMBER; //容联回拨显示号码
	public static final String RONG_LIAN_CALL_PROMPT_TONE; //容联回拨等待提示音乐
	public static final String RONG_LIAN_CALL_COUNT_DOWN_TIME; //容联回拨倒计时秒数
	public static final String RONG_LIAN_CALL_COUNT_DOWN_PROMPT; //容联回拨倒计时提示语
	public static final String UCPAAS_ACCOUNTSID;//云之讯主账号
	public static final String UCPAAS_VERSION;//云之讯版本
	public static final String UCPAAS_PREFIX;//区号前缀
	public static final String UCPAAS_ORRECORD;//是否录音(0:不需要 1：需要)
	public static final String UCPAAS_MIDDLEID;
	public static final String UCPAAS_REGISTER;//注册手机号城市ID
	public static final String UCPAAS_AUTHTOKEN;//云之讯Token
	public static final String UCPAAS_APPID;//云之讯appid
	public static final String UCPAAS_CALLBACK_FROMCLIENT;//云之讯appid下的client
	public static final String UCPAAS_CALLBACK_RINGTONEID;//云之讯提示音id
	public static final String UCPAAS_CALLBACK_ENDPROMPTTONEID;//云之讯结束提示音id
	public static final String UCPAAS_CALLBACK_SHOWNUM;//云之讯显示号码
	public static final String UCPAAS_CALLBACK_LASTREMINTIME;//云之讯结束提示分钟数
	public static final String NETEASE_APPKEY; //网易app key
	public static final String NETEASE_APPSECRET;//网易app secret
	public static final String WECHAT_FRONT_APPLICATION_DISC_URL;// 微信性格测试前端应用url
	public static final String WECHAT_FRONT_APPLICATION_PROFESSIONAL_TEST_URL;// 微信专业测试前端应用url
	public static final String WECHAT_FRONT_APPLICATION_FUNNY_TEST_URL;// 微信趣味测试前端应用url
	public static final String WECHAT_FRONT_APPLICATION_STU_TEST_URL;// 微信学生测试前端应用url(超级课堂)
	public static final String WECHAT_FRONT_APPLICATION_STU_TEST_SELF_URL;// 微信学生测试前端应用url（自己微站使用）
	public static final String PINGXX_APIKEY;//pingxx apiKey
	public static final String PINGXX_APPID;//pingxx appkey
	public static final String PINGXX_OURPRIVATEKEYFILE;//pingxx 商户私钥
	public static final String PINGXX_PINGXXPUBLICKEYFILE;//pingxx 平台公钥
	public static final String ALIDAYU_APPKEY;// 阿里大于APPKEY
	public static final String ALIDAYU_APPSECRET;//阿里大于APPSECRET
	public static final String ALIDAYU_SHOWNUM;//阿里大于显示号码
	public static final String LUCENE_PATH;//lucene 路径
	
	public static final String XINMAO_LOGOPATH;//心猫LOGO
	
	
	static String getProp(ResourceBundle rb, String key)
	{
		String prop = null;
		try{
			prop = rb.getString(key);
		}catch(Exception e)
		{
			prop = "";
		}
		
		return prop;
	}
	
	static
	{
		ResourceBundle rb = ResourceBundle.getBundle("config");

		FRONT_SERVER_HOST = getProp(rb, "front.server.host");
		UPLOAD_SERVICE_SERVER_HOST = getProp(rb, "upload.service.server.host");
		API_SERVICE_SERVER_HOST = getProp(rb, "api.service.server.host");
		WECHAT_TOKEN = getProp(rb, "wechat.token");
		WECHAT_APPID = getProp(rb, "wechat.appId");
		WECHAT_APPSECRET = getProp(rb, "wechat.appSecret");
		QINIU_ACCESSKEY = getProp(rb, "qiniu.accessKey");
		QINIU_SECRETKEY = getProp(rb, "qiniu.secretKey");
		QINIU_BUCKET_FILE = getProp(rb, "qiniu.bucket.file");
		QINIU_BUCKET_PICTURE = getProp(rb, "qiniu.bucket.picture");
		QINIU_DOMAIN_FILE = getProp(rb, "qiniu.domain.file");
		QINIU_DOMAIN_PICTURE = getProp(rb, "qiniu.domain.picture");
		RONG_LIAN_APPID = getProp(rb, "rong.lian.appId");
		RONG_LIAN_CALL_PHONE_NUMBER = getProp(rb, "rong.lian.call.phone.number"); //容联回拨显示号码
		RONG_LIAN_CALL_PROMPT_TONE = getProp(rb, "rong.lian.call.prompt.tone"); //容联回拨等待提示音乐
		RONG_LIAN_CALL_COUNT_DOWN_TIME = getProp(rb, "rong.lian.call.count.down.time"); //容联回拨倒计时秒数
		RONG_LIAN_CALL_COUNT_DOWN_PROMPT = getProp(rb, "rong.lian.call.count.down.prompt"); //容联回拨倒计时提示语
		UCPAAS_ACCOUNTSID = getProp(rb, "ucpaas.accountSid");//云之讯主账号
		UCPAAS_VERSION = getProp(rb, "ucpaas.version");//云之讯版本
		UCPAAS_ORRECORD = getProp(rb, "ucpaas.orrecord");//是否录音
		UCPAAS_PREFIX = getProp(rb, "ucpaas.prefix");//区号前缀
		UCPAAS_MIDDLEID = getProp(rb, "ucpaas.middleId");//中间号绑定ID
		UCPAAS_REGISTER = getProp(rb, "ucpaas.register");//注册手机号城市ID
		UCPAAS_AUTHTOKEN = getProp(rb, "ucpaas.authToken");//云之讯Token
		UCPAAS_APPID = getProp(rb, "ucpaas.appId");//云之讯appid
		UCPAAS_CALLBACK_FROMCLIENT = getProp(rb, "ucpaas.callback.fromClient");//云之讯appid下的client
		UCPAAS_CALLBACK_RINGTONEID = getProp(rb, "ucpaas.callback.ringtoneID");//云之讯提示音id
		UCPAAS_CALLBACK_ENDPROMPTTONEID = getProp(rb, "ucpaas.callback.endPromptToneID");//云之讯结束提示音id
		UCPAAS_CALLBACK_SHOWNUM = getProp(rb, "ucpaas.callback.showNum");
		UCPAAS_CALLBACK_LASTREMINTIME = getProp(rb, "ucpaas.callback.lastReminTime");
		NETEASE_APPKEY = getProp(rb, "netease.appKey");
		NETEASE_APPSECRET = getProp(rb, "netease.appSecret");
		WECHAT_FRONT_APPLICATION_DISC_URL = getProp(rb, "wechat.front.application.disc.url");
		WECHAT_FRONT_APPLICATION_PROFESSIONAL_TEST_URL = getProp(rb, "wechat.front.application.professionalTest.url");
		WECHAT_FRONT_APPLICATION_FUNNY_TEST_URL = getProp(rb, "wechat.front.application.funnyTest.url");
		WECHAT_FRONT_APPLICATION_STU_TEST_URL = getProp(rb, "wechat.front.application.stuTest.url");
		WECHAT_FRONT_APPLICATION_STU_TEST_SELF_URL = getProp(rb, "wechat.front.application.stuTest.self.url");
		PINGXX_APIKEY = getProp(rb, "pingxx.apiKey");
		PINGXX_APPID = getProp(rb, "pingxx.appId");
		PINGXX_OURPRIVATEKEYFILE = getProp(rb, "pingxx.ourPrivateKeyFile");
		PINGXX_PINGXXPUBLICKEYFILE = getProp(rb, "pingxx.pingxxPublicKeyFile");
		LUCENE_PATH = getProp(rb, "lucene.path");
		XINMAO_LOGOPATH =getProp(rb, "xinmao.logoPath");
		ALIDAYU_APPKEY = getProp(rb, "alidayu.appkey");
		ALIDAYU_APPSECRET = getProp(rb, "alidayu.appsecret");
		ALIDAYU_SHOWNUM = getProp(rb, "alidayu.shownum");
	}
}
