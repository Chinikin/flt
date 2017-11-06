package com.depression.base.ucpaas.safetyCalls;

import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.depression.base.ucpaas.DateUtil;
import com.depression.base.ucpaas.EncryptUtil;
import com.depression.base.ucpaas.models.SafetyCalls;
import com.google.gson.Gson;
/**
 * 
 * @author xupiao 2016年10月8日
 *
 */
public class JsonSafetyCallsClient extends AbstractSafetyCallsClient {

	@Override
	public String applyNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls) {
		String result = "";
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			// MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature = getSignature(accountSid, authToken, timestamp, encryptUtil);
			String url = getStringBuffer().append("/").append(version).append("/Accounts/").append(accountSid)
					.append("/safetyCalls/allocNumber").append("?sig=").append(signature).toString();
			String body = "";
			if (safetyCalls != null) {
				Gson gson = new Gson();
				body = gson.toJson(safetyCalls);
			}
			HttpResponse response = post("application/json", accountSid, authToken, timestamp, url, httpclient,
					encryptUtil, body);
			// 获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	@Override
	public String allotNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls) {
		String result = "";
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			// MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature = getSignature(accountSid, authToken, timestamp, encryptUtil);
			String url = getStringBuffer().append("/").append(version).append("/Accounts/").append(accountSid)
					.append("/safetyCalls/allocNumber").append("?sig=").append(signature).toString();
			String body = "";
			if (safetyCalls != null) {
				Gson gson = new Gson();
				body = gson.toJson(safetyCalls);
			}
			HttpResponse response = post("application/json", accountSid, authToken, timestamp, url, httpclient,
					encryptUtil, body);
			// 获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	@Override
	public String chooseNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls) {
		String result = "";
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			// MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature = getSignature(accountSid, authToken, timestamp, encryptUtil);
			String url = getStringBuffer().append("/").append(version).append("/Accounts/").append(accountSid)
					.append("/safetyCalls/chooseNumber").append("?sig=").append(signature).toString();
			String body = "";
			if (safetyCalls != null) {
				Gson gson = new Gson();
				body = gson.toJson(safetyCalls);
			}
			HttpResponse response = post("application/json", accountSid, authToken, timestamp, url, httpclient,
					encryptUtil, body);
			// 获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	@Override
	public String releaseNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls) {
		String result = "";
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			// MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature = getSignature(accountSid, authToken, timestamp, encryptUtil);
			String url = getStringBuffer().append("/").append(version).append("/Accounts/").append(accountSid)
					.append("/safetyCalls/freeNumber").append("?sig=").append(signature).toString();
			String body = "";
			if (safetyCalls != null) {
				Gson gson = new Gson();
				body = gson.toJson(safetyCalls);
			}
			HttpResponse response = post("application/json", accountSid, authToken, timestamp, url, httpclient,
					encryptUtil, body);
			// 获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	@Override
	public String unbindNumber(String version, String accountSid, String authToken, SafetyCalls safetyCalls) {
		String result = "";
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			// MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature = getSignature(accountSid, authToken, timestamp, encryptUtil);
			String url = getStringBuffer().append("/").append(version).append("/Accounts/").append(accountSid)
					.append("/safetyCalls/unbindNumber").append("?sig=").append(signature).toString();
			String body = "";
			if (safetyCalls != null) {
				Gson gson = new Gson();
				body = gson.toJson(safetyCalls);
			}
			HttpResponse response = post("application/json", accountSid, authToken, timestamp, url, httpclient,
					encryptUtil, body);
			// 获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	@Override
	public String getConcurrent(String version, String accountSid, String authToken, SafetyCalls safetyCalls) {
		String result = "";
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			// MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH);
			String signature = getSignature(accountSid, authToken, timestamp, encryptUtil);
			String url = getStringBuffer().append("/").append(version).append("/Accounts/").append(accountSid)
					.append("/safetyCalls/getConcurrent").append("?sig=").append(signature).toString();
			String body = "";
			if (safetyCalls != null) {
				Gson gson = new Gson();
				body = gson.toJson(safetyCalls);
			}
			HttpResponse response = post("application/json", accountSid, authToken, timestamp, url, httpclient,
					encryptUtil, body);
			// 获取响应实体信息
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

}
