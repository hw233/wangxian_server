package com.sqage.stat.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Datetes {
	/** 
	  * @param args 
	  */ 
	public static void main(String[] args) throws Exception { 
	  // TODO 自动生成方法存根 
	  //日期相减算出秒的算法 
	  Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2012-03-03"); 
	  Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2012-02-15"); 
	  
//	  long l = date1.getTime()-date2.getTime()>0 ? date1.getTime()-date2.getTime(): 
//	   date2.getTime()-date1.getTime(); 
	  
	  //日期相减得到相差的日期 
	  long day = (date1.getTime()-date2.getTime())/(24*60*60*1000)>0 ? (date1.getTime()-date2.getTime())/(24*60*60*1000): 
	   (date2.getTime()-date1.getTime())/(24*60*60*1000); 
	  
	  System.out.println("相差的日期: " +day); 
	  
	} 
}
