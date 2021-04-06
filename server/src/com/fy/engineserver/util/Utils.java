package com.fy.engineserver.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fy.engineserver.datasource.language.Translate;

public class Utils {

	public static String toString(int[] a){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; a != null && i < a.length ; i++){
			sb.append(a[i]);
			if(i < a.length -1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public static String toString(short[] a){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; a != null && i < a.length ; i++){
			sb.append(a[i]);
			if(i < a.length -1){
				sb.append(",");
			}
		}
		return sb.toString();	
	}
	
	public static String toString(String[] a){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; a != null && i < a.length ; i++){
			sb.append(a[i]);
			if(i < a.length -1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 格式化时间显示，输入是多少毫秒，输出是多少小时，多少分钟，多少秒
	 * @param millisTime
	 * @return
	 */
	public static String formatTimeDisplay(long millisTime){
		StringBuffer sb = new StringBuffer();
		float l = 1.0f * millisTime/1000;
		if(l >= 3600){
			long k = (long)l/3600;
			sb.append(k+"h");
			l = l - 3600 * k;
		}
		if(l >= 60){
			long k = (long)l/60;
			sb.append(k+"m");
			l = l - 60 * k;
		}
		if(l > 0){
			if(l > 5){
				sb.append(((int)l)+"s");
			}else{
				sb.append(l+"s");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 格式化时间显示，输入是多少毫秒，输出是多少小时，多少分钟，多少秒
	 * @param millisTime
	 * @return
	 */
	public static String formatTimeDisplay2(long millisTime){
		StringBuffer sb = new StringBuffer();
		int r = (int)(millisTime/1000);
		if(r * 1000 == millisTime){
			if(r >= 3600){
				int k = r/3600;
				sb.append(k+Translate.text_146);
				r = r - 3600 * k;
			}
			if(r >= 60){
				int k = r/60;
				sb.append(k+Translate.text_48);
				r = r - 60 * k;
			}
			if(r > 0){
				sb.append(r+Translate.text_49);
			}
		}else{
			float l = 1.0f * millisTime/1000;
			if(l >= 3600){
				long k = (long)l/3600;
				sb.append(k+Translate.text_146);
				l = l - 3600 * k;
			}
			if(l >= 60){
				long k = (long)l/60;
				sb.append(k+Translate.text_48);
				l = l - 60 * k;
			}
			if(l > 0){
				sb.append(l+Translate.text_49);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取距离
	 * @param x
	 * @param y
	 * @param x1
	 * @param y1
	 * @return
	 */
	public static int getDistanceA2B(float x,float y, float x1, float y1){
		float dx = x-x1;
		float dy = y-y1;
		int dis = (int) Math.ceil(Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2)));
		return dis;
	}
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 判断俩个时间是否是同一天
	 */
	public static boolean checkSameDay(long endTime,long beginTime){
		
		String end = sdf.format(new Date(endTime));
		String begin = sdf.format(new Date(beginTime));

		return end.equals(begin);
		
	}
	
	public static boolean isSameDay(long endTime,long beginTime){
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(endTime);
		int day1 = cal.get(Calendar.DAY_OF_YEAR);
		
		cal.setTimeInMillis(beginTime);
		int day2 = cal.get(Calendar.DAY_OF_YEAR);
		
		return day1 == day2 ? true:false;
	}
	
	//是不是指定一天
	public static boolean isSpecialSameDay(int year,int month,int day){
		
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.YEAR) == year){
			if(cal.get(Calendar.MONTH) == month){
				if(cal.get(Calendar.DAY_OF_MONTH) == day){
					return true;
				}
			}
		}
		return false;
	}
	
	//是不是指定的时间精确到分钟
	public static boolean isSpecialSameTime(int hour,int minute){
		
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.HOUR_OF_DAY) == hour){
			if(cal.get(Calendar.MINUTE) == minute){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 判断字符串中是否有无法存入到mysql中得字符
	 * UTF-8以字节为单位对Unicode进行编码。从Unicode到UTF-8的编码方式如下： 
	　　Unicode编码(16进制)　║　UTF-8 字节流(二进制) 
	　　000000 - 00007F　║　0xxxxxxx 
	　　000080 - 0007FF　║　110xxxxx 10xxxxxx 
	　　000800 - 00FFFF　║　1110xxxx 10xxxxxx 10xxxxxx 
	　　010000 - 10FFFF　║　11110xxx 10xxxxxx 10xxxxxx 10xxxxxx 
	* 将字节解析成utf8 怎样来定多少字节合成一个utf8字符 是根据字节的前导字符来的
		 * 几个1 就代表需要几个字节表达
		 *   7	U+0000	U+007F	1	0xxxxxxx
			11	U+0080	U+07FF	2	110xxxxx	10xxxxxx
			16	U+0800	U+FFFF	3	1110xxxx	10xxxxxx	10xxxxxx
			21	U+10000	U+1FFFFF	4	11110xxx	10xxxxxx	10xxxxxx	10xxxxxx
			26	U+200000	U+3FFFFFF	5	111110xx	10xxxxxx	10xxxxxx	10xxxxxx	10xxxxxx
			31	U+4000000	U+7FFFFFFF	6	1111110x	10xxxxxx	10xxxxxx	10xxxxxx	10xxxxxx	10xxxxxx
	 * @param str
	 * @return
	 */
	public static boolean isValidatedUTF8ForMysql(String str)
	{
		try
		{
			byte[] bytes = str.getBytes("utf8");
			
			for(int i=0;i<bytes.length;i++)
			{
				byte a = bytes[i];
				
				if((((a >> 4) & 0xF) - 0xE) > 0)
				{
					return false;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	public static void main(String[] args) {
	
//		System.err.println(Utils.isSameDay(1348034464110l, System.currentTimeMillis()));
		
		System.out.println("isValidate4MysqlUtf8:"+isValidatedUTF8ForMysql("#￥#￥%#￥T#$HWhw为二位Θ◎██▆™▦♫♫◥📤"));

	}

}
