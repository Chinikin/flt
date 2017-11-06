package com.depression.push;

/**
 * 自定义推送内容
 * 
 * @author hongqian_li
 * 
 */
public class CustomContent
{
	private String userID;
	private int msgType;
	private String jsonContent;

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public int getMsgType()
	{
		return msgType;
	}

	public void setMsgType(int msgType)
	{
		this.msgType = msgType;
	}

	public String getJsonContent()
	{
		return jsonContent;
	}

	public void setJsonContent(String jsonContent)
	{
		this.jsonContent = jsonContent;
	}

	@Override
	public String toString()
	{
		return "CustomContent [userID=" + userID + ", msgType=" + msgType + ", jsonContent=" + jsonContent + "]";
	}

}
