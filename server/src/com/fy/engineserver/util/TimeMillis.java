package com.fy.engineserver.util;

import java.util.Date;

import com.xuanzhi.tools.text.DateUtil;

/**
 *
 * 
 * @version 创建时间：Aug 8, 2012 9:20:11 AM
 * 
 */
public class TimeMillis {
	public static void main(String arggs[]) {
		String dateStr = "2012-08-09 00:00:00";
		Date date = DateUtil.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
		System.out.println("============" + date.getTime());
		
//		String str = "UUID=bc66943499d14290b2b73e0603beec7b5e60d860MACADDRESS=2a:8f:7a:37:2a:0f";
//		System.out.print("================" + str.split("MACADDRESS")[0]);
	}
}
