package com.depression.utils;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.utils.DateUtil;
import com.depression.base.ucpaas.EncryptUtil;
import com.depression.base.ucpaas.models.SafetyCalls;
import com.depression.base.ucpaas.safetyCalls.JsonSafetyCallsClient;
import com.depression.model.api.dto.ApiServiceCallDTO;

public class HideCallUtil {

	private static Logger log = Logger.getLogger(HideCallUtil.class);

	/**
	 * 隐号通话服务
	 * 
	 * @param callerMobilePhone
	 * @param calledMobilePhone
	 * @param maxCallTime
	 * @return
	 */
	public static ApiServiceCallDTO ucpaasCallService(String callerMobilePhone,
			String calledMobilePhone, Integer maxCallTime) {
		// 时长取整60
		maxCallTime = (maxCallTime / 60 + 1) * 60;
		SafetyCalls safetyCalls = new SafetyCalls();
		// 时长取整60
		maxCallTime = (maxCallTime / 60 + 1) * 60;

		String version = Configuration.UCPAAS_VERSION;
		;
		String accountSid = Configuration.UCPAAS_ACCOUNTSID;// 主账户Id
		String authToken = Configuration.UCPAAS_AUTHTOKEN;
		String appId = Configuration.UCPAAS_APPID;
		String mobilePrefix = Configuration.UCPAAS_PREFIX;
		String orRecord = Configuration.UCPAAS_ORRECORD;
		String registerPrefix = Configuration.UCPAAS_REGISTER;
		String nowTime = DateUtil.dateToStr(new Date(),5);
		String bindId = UUID.randomUUID().toString();
		safetyCalls.setRequestId(nowTime+"@"+bindId);
		Integer cacheMaxAge = maxCallTime + 30;
		safetyCalls.setAppId(appId);
		safetyCalls.setCaller(mobilePrefix + callerMobilePhone);
		safetyCalls.setCallee(mobilePrefix + calledMobilePhone);
		String dstVirtualNum = mobilePrefix + 0 + calledMobilePhone;
		safetyCalls.setDstVirtualNum(dstVirtualNum);
		// 生产唯一标示符
		safetyCalls.setMaxAge(cacheMaxAge.toString());
		safetyCalls.setCityId(registerPrefix);
		safetyCalls.setMaxAllowTime(maxCallTime.toString());
		// 允许录音
		safetyCalls.setRecord(orRecord);
		String result = new JsonSafetyCallsClient().applyNumber(version,
				accountSid, authToken, safetyCalls);

		JSONObject j = JSON.parseObject(result);

		ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
		if (!"".equals(j.getString("dstVirtualNum"))&&j.getString("dstVirtualNum")!=null) {
			callDTO.setCallsid(bindId);
			//构造orderId(手机号+当前时间)
			String orderId = callerMobilePhone+String.valueOf(new Date().getTime());
			callDTO.setOrderId(orderId);
			callDTO.setCaller(callerMobilePhone);
			callDTO.setCalled(calledMobilePhone);
			callDTO.setServiceOrderId(Long.valueOf(j.getString("dstVirtualNum")));
		} else {
			// 异常返回输出错误码和错误信息
			log.error("错误码=" + j.getString("errorCode"));
			return null;
		}

		return callDTO;
	}

	public static boolean ucpaasDeleteCallService(
			String callerMobilePhone, String calledMobilePhone,String midId) {
		boolean flag = false;
		String version = Configuration.UCPAAS_VERSION;
		;
		String accountSid = Configuration.UCPAAS_ACCOUNTSID;// 主账户Id
		String authToken = Configuration.UCPAAS_AUTHTOKEN;
		String appId = Configuration.UCPAAS_APPID;
		// 双方号码＋中间号绑定ID
		String bindId = callerMobilePhone + calledMobilePhone +midId;
		String mobilePrefix = Configuration.UCPAAS_PREFIX;
		String registerPrefix = Configuration.UCPAAS_REGISTER;
		SafetyCalls safetyCalls = new SafetyCalls();
		safetyCalls.setAppId(appId);
		safetyCalls.setBindId(bindId);
		safetyCalls.setCityId(mobilePrefix + registerPrefix);
		String result = new JsonSafetyCallsClient().releaseNumber(version,
				accountSid, authToken, safetyCalls);
		JSONObject j = JSON.parseObject(result);
		if ("000000".equals(j.getString("errorCode"))) {
			flag = true;
		} else {
			// 异常返回输出错误码和错误信息
			log.error("错误码=" + j.getString("errorCode"));
			return false;
		}
		return flag;
	}
	
	/**
	 * 生成签名的录音文件下载链接
	 * @param recordUrl
	 * @param callid
	 * @return
	 */
	public static String ucpassRecordUrlSig(String recordUrl, String callid)
	{
		String accountSid = Configuration.UCPAAS_ACCOUNTSID;// 主账户Id
		String authToken=Configuration.UCPAAS_AUTHTOKEN;
		EncryptUtil encryptUtil = new EncryptUtil();
		try
		{
			String signature = encryptUtil.md5Digest(accountSid + callid + authToken);
			return String.format("%s?sig=%s", recordUrl, signature);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static ApiServiceCallDTO aliCallService(){
		ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
		return callDTO;
	}
}
