package com.depression.base.wechat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormart
{
	private static String YYYYMMDD = "yyyyMMdd";
	private static String YYYY_MM_DD = "yyyy-MM-dd";
	private static String YYYYXMMXDD = "yyyy/MM/dd";
	private static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	private static String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	private static String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	private static String YYYYXMMXDDXHHMMSS = "yyyy/MM/dd HH:mm:ss";

	public static String convertToString(Date date, String str)
	{
		if ((StringUtil.isEmpty(date)) || (StringUtil.isEmpty(str)))
		{
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		return sdf.format(date);
	}

	public static Date pareStrToDate(String strDate, String formatStr)
	{
		if ((StringUtil.isEmpty(strDate)) || (StringUtil.isEmpty(formatStr)))
		{
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try
		{
			date = sdf.parse(strDate);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}

	public static String convertToYYYYMMDD(Date date)
	{
		return convertToString(date, YYYYMMDD);
	}

	public static String convertToYYYY_MM_DD(Date date)
	{
		return convertToString(date, YYYY_MM_DD);
	}

	public static String convertToYYYYXMMXDD(Date date)
	{
		return convertToString(date, YYYYXMMXDD);
	}

	public static String convertToYYYYMMDDHHMMSS(Date date)
	{
		return convertToString(date, YYYYMMDDHHMMSS);
	}

	public static String convertToYYYYMMDDHHMMSSSSS(Date date)
	{
		return convertToString(date, YYYYMMDDHHMMSSSSS);
	}

	public static String convertToYYYY_MM_DD_HHMMSS(Date date)
	{
		return convertToString(date, YYYY_MM_DD_HHMMSS);
	}

	public static String convertToYYYYXMMXDDXHHMMSS(Date date)
	{
		return convertToString(date, YYYYXMMXDDXHHMMSS);
	}

	public static Date paserYYYY_MM_DDToDate(String strDate)
	{
		return pareStrToDate(strDate, YYYY_MM_DD);
	}

	public static Date paserYYYY_MM_DD_HHMMSSToDate(String strDate)
	{
		return pareStrToDate(strDate, YYYY_MM_DD_HHMMSS);
	}

	public static Date paserYYYY_MM_DD_HHMMSSToDate(long longTime)
	{
		Date date = new Date(longTime * 1000L);
		String dt = convertToYYYY_MM_DD_HHMMSS(date);
		return paserYYYY_MM_DD_HHMMSSToDate(dt);
	}

	public static void main(String[] args)
	{
		System.out.println(paserYYYY_MM_DD_HHMMSSToDate(1392365996L));
	}
}
