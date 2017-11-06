package com.depression.utils;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.ApiRuleException;
import com.taobao.api.BaseTaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;

import com.taobao.api.response.AlibabaAliqinSecretAxbGetRecordfileResponse;

/**
 * TOP API: alibaba.aliqin.secret.axb.get.recordfile request
 * 
 * @author top auto create
 * @since 1.0, 2016.07.19
 */
public class AlibabaAliqinSecretAxbGetRecordfileRequest extends BaseTaobaoRequest<AlibabaAliqinSecretAxbGetRecordfileResponse> {
	
	

	/** 
	* 呼叫ID（运营商侧）
	 */
	private String callId;

	/** 
	* 呼叫时间
	 */
	private Date callTime;

	/** 
	* 文件选择类型：｛0：AB号码语音合成；1：A号码语音；2：B号码语音；｝
	 */
	private Long fileSelectType;

	/** 
	* 商户Key
	 */
	private String partnerKey;

	/** 
	* 订单ID
	 */
	private Long subsId;

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCallId() {
		return this.callId;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public Date getCallTime() {
		return this.callTime;
	}

	public void setFileSelectType(Long fileSelectType) {
		this.fileSelectType = fileSelectType;
	}

	public Long getFileSelectType() {
		return this.fileSelectType;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}

	public String getPartnerKey() {
		return this.partnerKey;
	}

	public void setSubsId(Long subsId) {
		this.subsId = subsId;
	}

	public Long getSubsId() {
		return this.subsId;
	}

	public String getApiMethodName() {
		return "alibaba.aliqin.secret.axb.get.recordfile";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("call_id", this.callId);
		txtParams.put("call_time", this.callTime);
		txtParams.put("file_select_type", this.fileSelectType);
		txtParams.put("partner_key", this.partnerKey);
		txtParams.put("subs_id", this.subsId);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<AlibabaAliqinSecretAxbGetRecordfileResponse> getResponseClass() {
		return AlibabaAliqinSecretAxbGetRecordfileResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(callId, "callId");
		RequestCheckUtils.checkNotEmpty(callTime, "callTime");
		RequestCheckUtils.checkNotEmpty(fileSelectType, "fileSelectType");
		RequestCheckUtils.checkNotEmpty(partnerKey, "partnerKey");
		RequestCheckUtils.checkNotEmpty(subsId, "subsId");
	}
	

}