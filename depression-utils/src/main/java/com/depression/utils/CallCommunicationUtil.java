package com.depression.utils;

import java.util.HashMap;
import java.util.Set;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.CCPRestSDK;
import com.depression.base.ucpaas.EncryptUtil;
import com.depression.base.ucpaas.client.JsonReqClient;
import com.depression.model.api.dto.ApiServiceCallDTO;
import com.taobao.api.ApiException;

/**
 * 容联通讯
 * 
 * @author ax
 * 
 */
public class CallCommunicationUtil
{

	private static Logger log = Logger.getLogger(CallCommunicationUtil.class);

	/**
	 * 调用容联接口，拨打双向电话
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ApiServiceCallDTO ronglianCallService(String callerMobilePhone, String calledMobilePhone, Integer maxCallTime)
	{
		// 当订单创建成功，并成功扣款后，进行拨打电话操作
		HashMap<String, Object> result4Call = null;
		ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
		CCPRestSDK restAPI = new CCPRestSDK();
		String appId = "8a216da85582647801559fe151d91ad0";
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount("8a216da854ff8dcc0154ffa84f07005b", "2c36ed07e422442c9da379b55c46a340");
		restAPI.setSubAccount("ab967402694611e6997a6c92bf2c165d", "060c0d3f2c18fa3cf6190a6848630855");
		restAPI.setAppId(appId);
		result4Call = restAPI.callback(callerMobilePhone, calledMobilePhone, 
				Configuration.RONG_LIAN_CALL_PHONE_NUMBER, 
				Configuration.RONG_LIAN_CALL_PHONE_NUMBER, 
				Configuration.RONG_LIAN_CALL_PROMPT_TONE, 
				"true", null, null, maxCallTime.toString(), 
				Configuration.API_SERVICE_SERVER_HOST + "callcore/callback.json", 
				"1", "1", 
				Configuration.RONG_LIAN_CALL_COUNT_DOWN_TIME, 
				Configuration.RONG_LIAN_CALL_COUNT_DOWN_PROMPT);
		log.info("SDKTestCallback result=" + result4Call);
		log.info("SDKTestCallback maxCallTime=" + maxCallTime);
		if ("000000".equals(result4Call.get("statusCode")))
		{
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>)((HashMap<String, Object>) result4Call.get("data")).get("CallBack");
			Set<String> keySet = data.keySet();
			for (String key : keySet)
			{
				Object object = data.get(key);
				log.info(key + " = " + object);
				if (key.equals("callSid"))
				{
					// 获取通话唯一标识符
					callDTO.setCallsid(object.toString());
				} else if (key.equals("orderId"))
				{
					// 获取通话订单id
					callDTO.setOrderId(object.toString());
				}
			}
		} else
		{
			// 异常返回输出错误码和错误信息
			log.error("错误码=" + result4Call.get("statusCode") + " 错误信息= " + result4Call.get("statusMsg"));
			return null;
		}
		return callDTO;
	}
	
	/**
	 * 云之讯电话服务
	 * @param callerMobilePhone
	 * @param calledMobilePhone
	 * @param maxCallTime
	 * @return
	 */
	public static ApiServiceCallDTO ucpaasCallService(String callerMobilePhone, String calledMobilePhone, Integer maxCallTime)
	{
		//时长取整60
		maxCallTime = (maxCallTime/60 + 1) * 60;		
		String accountSid = Configuration.UCPAAS_ACCOUNTSID;// 主账户Id
		String authToken=Configuration.UCPAAS_AUTHTOKEN;
		String appId=Configuration.UCPAAS_APPID;
		String fromClient=Configuration.UCPAAS_CALLBACK_FROMCLIENT;
		String caller=callerMobilePhone;
		String to=calledMobilePhone;
		String fromSerNum=Configuration.UCPAAS_CALLBACK_SHOWNUM;
		String toSerNum=Configuration.UCPAAS_CALLBACK_SHOWNUM;
		String maxallowtime = maxCallTime.toString();
		String ringtoneID = Configuration.UCPAAS_CALLBACK_RINGTONEID;
		String lastReminTime = Configuration.UCPAAS_CALLBACK_LASTREMINTIME;
		String endPromptToneID = Configuration.UCPAAS_CALLBACK_ENDPROMPTTONEID;
		
		String result=new JsonReqClient().callback(accountSid, authToken, appId, fromClient, to,fromSerNum,toSerNum,
				maxallowtime, ringtoneID, lastReminTime, endPromptToneID, caller);
		JSONObject j = JSON.parseObject(result);		
		ApiServiceCallDTO callDTO = new ApiServiceCallDTO();
		if("000000".equals(j.getJSONObject("resp").getString("respCode")))
		{
			callDTO.setCallsid(j.getJSONObject("resp").getJSONObject("callback").getString("callId"));
		}else
		{
			// 异常返回输出错误码和错误信息
			log.error("错误码=" + j.getJSONObject("resp").getString("respCode"));
			return null;
		}
		
		return callDTO;
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
	
	/**
	 * 老版本
	 * 拨打电话，默认调用云之讯，容联暂时无线路可用
	 * @param callerMobilePhone
	 * @param calledMobilePhone
	 * @param maxCallTime
	 * @return
	 */
	public static ApiServiceCallDTO callService(String callerMobilePhone, String calledMobilePhone, Integer maxCallTime)
	{
		ApiServiceCallDTO callDTO = ucpaasCallService(callerMobilePhone, calledMobilePhone, maxCallTime);
		if(callDTO==null)
		{
			callDTO = ronglianCallService(callerMobilePhone, calledMobilePhone, maxCallTime);
		}
		
		return callDTO;
	}
	
	/**
	 * 拨打电话:根据callType来判断是选择网络通话还是隐号直拨（优先阿里）
	 * @param callerMobilePhone
	 * @param calledMobilePhone
	 * @param maxCallTime
	 * @return
	 * @throws ApiException 
	 */
	public static ApiServiceCallDTO callServiceByalidayu(String callerMobilePhone, String calledMobilePhone, Integer maxCallTime,Integer callType) 
	{
		ApiServiceCallDTO callDTO = null;
		//阿里大于隐号直拨
		if(callType==1){
			//默认网络通话
			try {
				callDTO = AliyunIMUtil.createAXB(callerMobilePhone, calledMobilePhone, maxCallTime);
			} catch (ApiException e) {
				e.printStackTrace();
				return null;
			}		 
		}
		//隐号回拨
		if(callType==2){
		 callDTO = AliyunIMUtil.aliyunCall(callerMobilePhone, calledMobilePhone, maxCallTime);		 
		}
				
		return callDTO;
	}

}
