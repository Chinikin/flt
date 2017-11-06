/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package com.depression.base.ucpaas.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "callback")
public class Callback {

	private String appId;
	private String fromClient;
	private String to;
	private String fromSerNum;
	private String toSerNum;
	private String maxallowtime;
	private String ringtoneID;
	private String lastReminTime;
	private String endPromptToneID;
	private String type;
	private String caller;
	private String record;	
	
	public String getFromSerNum() {
		return fromSerNum;
	}
	public void setFromSerNum(String fromSerNum) {
		this.fromSerNum = fromSerNum;
	}
	public String getToSerNum() {
		return toSerNum;
	}
	public void setToSerNum(String toSerNum) {
		this.toSerNum = toSerNum;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getFromClient() {
		return fromClient;
	}
	public void setFromClient(String fromClient) {
		this.fromClient = fromClient;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMaxallowtime()
	{
		return maxallowtime;
	}
	public void setMaxallowtime(String maxallowtime)
	{
		this.maxallowtime = maxallowtime;
	}
	public String getRingtoneID()
	{
		return ringtoneID;
	}
	public void setRingtoneID(String ringtoneID)
	{
		this.ringtoneID = ringtoneID;
	}
	public String getLastReminTime()
	{
		return lastReminTime;
	}
	public void setLastReminTime(String lastReminTime)
	{
		this.lastReminTime = lastReminTime;
	}
	public String getEndPromptToneID()
	{
		return endPromptToneID;
	}
	public void setEndPromptToneID(String endPromptToneID)
	{
		this.endPromptToneID = endPromptToneID;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getCaller()
	{
		return caller;
	}
	public void setCaller(String caller)
	{
		this.caller = caller;
	}
	public String getRecord()
	{
		return record;
	}
	public void setRecord(String record)
	{
		this.record = record;
	}
	
}
