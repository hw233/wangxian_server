package com.fy.boss.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static boolean validateIp(String ip,String[]needIps)
	{
		for(String needip : needIps)
		{
			if(ip.equals(needip))
				return true;
		}
		
		
		return false;
	}
	
	//转换req中的map的string数组为string
	/**
	 * 当string数组的值不止一个的时候忽略它，所以这个map中是不包含数组长度大于一的参数的
	 * @param request
	 * @return
	 */
	public static Map<String,String> convertReqMap(HttpServletRequest request)
	{
		Map<String,String> convertedMap = new HashMap<String, String>();
		try
		{
			Map map =	 request.getParameterMap();
			
			Iterator<String> it = map.keySet().iterator();
			
			while(it.hasNext())
			{
				String key = it.next();
				if(key != null)
				{
					String convertedValue = "";
					if(map.get(key) instanceof String[])
					{
						String[] value = (String[])map.get(key);
						
						if(value.length == 1)
						{
							convertedValue = value[0];
						}
					}
					else
					{
						continue;
					}
					
					convertedMap.put(key, convertedValue);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return convertedMap;
	}
	
}
