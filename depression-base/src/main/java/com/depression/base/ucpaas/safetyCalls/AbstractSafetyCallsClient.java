package com.depression.base.ucpaas.safetyCalls;

import java.io.ByteArrayInputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import com.depression.base.ucpaas.EncryptUtil;
import com.depression.base.ucpaas.SSLHttpClient;
import com.depression.base.ucpaas.SysConfig;
import com.depression.base.ucpaas.models.SafetyCalls;


/**
 * 
 * @author xupiao 2016年10月8日
 *
 */
public abstract class AbstractSafetyCallsClient {
	public boolean isTest = Boolean.parseBoolean(SysConfig.getInstance().getProperty("is_test"));
	public String server = SysConfig.getInstance().getProperty("rest_server");
	public String sslIP = SysConfig.getInstance().getProperty("http_ssl_ip");
	public int sslPort = SysConfig.getInstance().getPropertyInt("http_ssl_port");
	public String version = SysConfig.getInstance().getProperty("version");
	private static Logger logger = Logger.getLogger(AbstractSafetyCallsClient.class);

	/**
	 * 双向绑定号码同步接口
	 */
	public abstract String applyNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls);

	/**
	 * 双向绑定号码申请接口
	 */
	public abstract String allotNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls);

	/**
	 * 单向绑定号码申请接口
	 */
	public abstract String chooseNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls);

	/**
	 * 双向绑定关系解除接口
	 */
	public abstract String releaseNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls);

	/**
	 * 单向绑定关系解除接口
	 */
	public abstract String unbindNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls);

	/**
	 * 实时绑定并发数查询接口
	 */
	public abstract String getConcurrent(String version, String accountSid, String authToken, SafetyCalls safetyCalls);

	public DefaultHttpClient getDefaultHttpClient() {
		DefaultHttpClient httpclient = null;
		if (isTest) {
			try {
				SSLHttpClient chc = new SSLHttpClient();
				httpclient = chc.registerSSL(sslIP, "TLS", sslPort, "https");
				HttpParams hParams = new BasicHttpParams();
				hParams.setParameter("https.protocols", "SSLv3,SSLv2Hello");
				httpclient.setParams(hParams);
			} catch (KeyManagementException e) {
				// TODO: handle exception
				logger.error(e);
			} catch (NoSuchAlgorithmException e) {
				// TODO: handle exception
				logger.error(e);
			}
		} else {
			httpclient = new DefaultHttpClient();
		}
		return httpclient;
	}

	public String getSignature(String accountSid, String authToken, String timestamp, EncryptUtil encryptUtil)
			throws Exception {
		String sig = accountSid + authToken + timestamp;
		String signature = encryptUtil.md5Digest(sig);
		return signature;
	}
	
	public StringBuffer getStringBuffer() {
		StringBuffer sb = new StringBuffer("https://");
		sb.append(server);
		return sb;
	}
	
	public HttpResponse get(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,EncryptUtil encryptUtil) throws Exception{
		HttpGet httppost = new HttpGet(url);
		httppost.setHeader("Accept", cType);//
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization",auth);
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
	public HttpResponse post(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,EncryptUtil encryptUtil,String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httppost.setEntity(requestBody);
        // 执行客户端请求
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}

}
