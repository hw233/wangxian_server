package com.fy.engineserver.sprite.horse.dateUtil;

import java.util.Calendar;
import java.util.Date;

public class DateFormat {
	
	public static String getSimpleDay(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return year+"-"+month+"-"+day;
	}
}
