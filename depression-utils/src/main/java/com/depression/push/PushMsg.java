package com.depression.push;

/**
 * android 推送消息
 * 
 * @author hongqian_li
 * 
 */
public class PushMsg
{

	private String channelId;
	private String userID;

	/**
	 * 通知标题，可以为空；如果为空则设为appid对应的应用名;
	 */
	private String title;
	/**
	 * 通知文本内容，不能为空;
	 */
	private String description; // 必选
	/**
	 * android客户端自定义通知样式，如果没有设置默认为0;
	 */
	private String notificationBuilderId; // 可选
	/**
	 * 只有notification_builder_id为0时有效，可以设置通知的基本样式包括(响铃：0x04;振动：0x02;可清除：0x01;),这是一个flag整形，每一位代表一种样式,如果想选择任意两种或三种通知样式，notification_basic_style的值即为对应样式数值相加后的值。
	 */
	private int notificationBasicStyle; // 可选
	/**
	 * 点击通知后的行为(1：打开Url; 2：自定义行为；);
	 */
	private int openType; // 可选
	/**
	 * 需要打开的Url地址，open_type为1时才有效;
	 */
	private String url; // 可选
	/**
	 * open_type为2时才有效，Android端SDK会把pkg_content字符串转换成Android Intent,通过该Intent打开对应app组件，所以pkg_content字符串格式必须遵循Intent uri格式，最简单的方法可以通过Intent方法toURI()获取e为2时才有效，Android端SDK会把pkg_content字符串转换成Android
	 * Intent,通过该Intent打开对应app组
	 */
	private String pkgContent; // 可选
	/**
	 * 自定义内容，键值对，Json对象形式(可选)；在android客户端，这些键值对将以Intent中的extra进行传递。
	 */
	private CustomContent customContent;

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getNotificationBuilderId()
	{
		return notificationBuilderId;
	}

	public void setNotificationBuilderId(String notificationBuilderId)
	{
		this.notificationBuilderId = notificationBuilderId;
	}

	public int getNotificationBasicStyle()
	{
		return notificationBasicStyle;
	}

	public void setNotificationBasicStyle(int notificationBasicStyle)
	{
		this.notificationBasicStyle = notificationBasicStyle;
	}

	public int getOpenType()
	{
		return openType;
	}

	public void setOpenType(int openType)
	{
		this.openType = openType;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getPkgContent()
	{
		return pkgContent;
	}

	public void setPkgContent(String pkgContent)
	{
		this.pkgContent = pkgContent;
	}

	public CustomContent getCustomContent()
	{
		return customContent;
	}

	public void setCustomContent(CustomContent customContent)
	{
		this.customContent = customContent;
	}

	@Override
	public String toString()
	{
		return "AndroidPushMsg [channelId=" + channelId + ", userID=" + userID + ", title=" + title + ", description=" + description + ", notificationBuilderId=" + notificationBuilderId
				+ ", notificationBasicStyle=" + notificationBasicStyle + ", openType=" + openType + ", url=" + url + ", pkgContent=" + pkgContent + ", customContent=" + customContent + "]";
	}

}
