package com.sqage.stat.test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.Date;

import com.xuanzhi.tools.text.DateUtil;

public class JIE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String startnum="7,8";
//		String dateStr[]=startnum.split(",");
//		Long tt=(DateUtil.parseDate("2011-12-30", "yyyy-MM-dd").getTime())+Long.parseLong(dateStr[0])*24*3600*1000;
//		
//		Date targetdate=new Date(tt);
//		String dateStr2=DateUtil.formatDate(targetdate,"yyyy-MM-dd");
//		
//		String dateStr3=DateUtil.formatDate(new Date(),"yyyy-MM-dd");
//		
//		//DateUtil.parseDate("2011-12-30", "yyyy-MM-dd");
//		System.out.println(dateStr2+dateStr3);
//		
//		
//		BigDecimal b1 = new BigDecimal("1");
//		BigDecimal b2 = new BigDecimal("3");
////		BigDecimal b=b1.divide(b2, new MathContext(3));
////		
////		BigDecimal b100 = new BigDecimal("100");
////	        BigDecimal c=b100.multiply(b);
//	        
//	        
//	        
//	        BigDecimal b100 = new BigDecimal("100");
//            BigDecimal b11=b100.multiply(b1);
//			BigDecimal b=b11.divide(b2, new MathContext(3));
//		System.out.println(b);

//		float   f0=1.234f; 
//		java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
//		String   new_f0   =   df.format(f0); 
//		System.out.print(new_f0);
		
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse("2007-12-24");
			endDate= format.parse("2007-12-24");    
			long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
			System.out.println("相隔的天数="+day);  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
}
