package com.depression.model.api.dto;

import java.util.Date;

public class ApiServiceBroadcastDTO  implements Comparable<Object>
{
	private String phrase;
	
	private Date time;

	public String getPhrase()
	{
		return phrase;
	}

	public void setPhrase(String phrase)
	{
		this.phrase = phrase;
	}

	public Date getTime()
	{
		return time;
	}

	public void setTime(Date time)
	{
		this.time = time;
	}

	@Override
	public int compareTo(Object o)
	{
		// TODO Auto-generated method stub
		ApiServiceBroadcastDTO broadcastDTO = (ApiServiceBroadcastDTO) o;
		return time.compareTo(broadcastDTO.getTime());
	}
	
	
}
