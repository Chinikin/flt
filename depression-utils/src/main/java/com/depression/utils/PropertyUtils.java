package com.depression.utils;

public class PropertyUtils {
	
	//检查属性是否有NULL
	static public boolean examineOneNull(Object... properties)
	{
		for(Object p : properties)
		{
			if(p == null) return true;
		}
		return false;
	}
	
	//检查属性都是NULL
	static public boolean examineAllNull(Object... properties)
	{
		for(Object p : properties)
		{
			if(p != null) return false;
		}
		return true;
	}

}
