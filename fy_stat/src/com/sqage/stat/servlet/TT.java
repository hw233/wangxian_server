package com.sqage.stat.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.xuanzhi.tools.text.DateUtil;

public class TT {

	
	protected HashMap<String,String> timeStr(String starttimeStr,String endtimeStr) throws ParseException
	{
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   //String currentDate = sdf.format(new Date());
		   Date startDate = sdf.parse(starttimeStr);
		   Date endDate = sdf.parse(endtimeStr);
		   HashMap<String, String> timeMap = new HashMap<String, String>();
		   while(startDate.before(endDate))
		   {
			   
			   String hh_minit=DateUtil.formatDate(startDate, "HH:mm"); 
				System.out.println(hh_minit.replace(":", "-")+ "->"+ hh_minit);
				timeMap.put(hh_minit.replace(":", "-"), hh_minit);
				
			   Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.add(Calendar.MINUTE, 10);
				startDate = cal.getTime();
		   }
		
		return timeMap;
	}
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
       TT t=new TT();
       HashMap<String,String> timeSt=  t.timeStr("2016-08-24 10:00", "2016-08-24 14:01");
	}

}
