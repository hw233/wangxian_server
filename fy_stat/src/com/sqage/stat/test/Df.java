package com.sqage.stat.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Df {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-22");
			Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
			System.out.println(date1.getTime());
			System.out.println(date2.getTime());
		 } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		System.out.println(sf.format(new Date(1451577600000l)));
		                                 
	}
	
}
