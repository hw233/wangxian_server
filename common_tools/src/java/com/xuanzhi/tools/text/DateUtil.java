package com.xuanzhi.tools.text;

import java.util.*;
import java.text.*;

/**
 * <pre>
 *  Symbol   Meaning                 Presentation        Example
 *  ------   -------                 ------------        -------
 *  G        era designator          (Text)              AD
 *  y        year                    (Number)            1996
 *  M        month in year           (Text &amp; Number)     July &amp; 07
 *  d        day in month            (Number)            10
 *  h        hour in am/pm (1&tilde;12)    (Number)            12
 *  H        hour in day (0&tilde;23)      (Number)            0
 *  m        minute in hour          (Number)            30
 *  s        second in minute        (Number)            55
 *  S        millisecond             (Number)            978
 *  E        day in week             (Text)              Tuesday
 *  D        day in year             (Number)            189
 *  F        day of week in month    (Number)            2 (2nd Wed in July)
 *  w        week in year            (Number)            27
 *  W        week in month           (Number)            2
 *  a        am/pm marker            (Text)              PM
 *  k        hour in day (1&tilde;24)      (Number)            24
 *  K        hour in am/pm (0&tilde;11)    (Number)            0
 *  z        time zone               (Text)              Pacific Standard Time
 *  '        escape for text         (Delimiter)
 *  ''       single quote            (Literal)           '
 * 
 *  The count of pattern letters determine the format. 
 * (Text): 4 or more pattern letters--use full form, &lt; 4--use short or abbreviated form if one exists. 
 * 
 * (Number): the minimum number of digits. Shorter numbers are zero-padded to this amount. Year is handled specially; that is, if the count of 'y' is 2, the Year will be truncated to 2 digits. 
 * 
 * (Text &amp; Number): 3 or over, use text, otherwise use number. 
 * 
 * Any characters in the pattern that are not in the ranges of ['a'..'z'] and ['A'..'Z'] will be treated as quoted text. For instance, characters like ':', '.', ' ', '#' and '@' will appear in the resulting time text even they are not embraced within single quotes. 
 * 
 * A pattern containing any invalid pattern letter will result in a thrown sms.exception during formatting or parsing. 
 * 
 * Examples Using the US Locale: 
 * 
 *  Format Pattern                         Result
 *  --------------                         -------
 *  &quot;yyyy.MM.dd G 'at' hh:mm:ss z&quot;    -&gt;&gt;  1996.07.10 AD at 15:08:56 PDT
 *  &quot;EEE, MMM d, ''yy&quot;                -&gt;&gt;  Wed, July 10, '96
 *  &quot;h:mm a&quot;                          -&gt;&gt;  12:08 PM
 *  &quot;hh 'o''clock' a, zzzz&quot;           -&gt;&gt;  12 o'clock PM, Pacific Daylight Time
 *  &quot;K:mm a, z&quot;                       -&gt;&gt;  0:00 PM, PST
 *  &quot;yyyyy.MMMMM.dd GGG hh:mm aaa&quot;    -&gt;&gt;  1996.July.10 AD 12:08 PM
 * </pre>
 * 
 */
public class DateUtil {

    protected static SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", java.util.Locale.US);

    protected static ParsePosition pos = new ParsePosition(0);

    /**
     * parse a String to a java.util.Date object
     */
    public static synchronized Date parseDate(String date, String format) {
        formatter.applyPattern(format);
        pos.setIndex(0);
        Date ret = formatter.parse(date, pos);
        if (ret == null)
            ret = formatter.parse("2004-01-01 00:00:00", pos);
        return ret;
    }

    /**
     * parse a String to a java.util.Date object
     */
    public static Date parseDateSafely(String date, String format) {
        SimpleDateFormat _formatter = new SimpleDateFormat(format,
                java.util.Locale.US);
        ParsePosition _pos = new ParsePosition(0);
        return _formatter.parse(date, _pos);
    }

    /**
     * format a java.util.Date object to a String
     */
    public static synchronized String formatDate(Date date, String format) {
        formatter.applyPattern(format);
        return formatter.format(date);
    }

    /**
     * format a java.util.Date object to a String
     */
    public static String formatDateSafely(Date date, String format) {
        SimpleDateFormat _formatter = new SimpleDateFormat(format,
                java.util.Locale.US);
        return _formatter.format(date);
    }

    /**
     * 切割时间段
     * @param start
     * @param end
     * @param type 0-天, 1-周, 2-月
     * @return
     */
    public static List<Date[]> splitTime(Date start, Date end , int type) {
    	List<Date[]> list = new ArrayList<Date[]>();
    	if(start.getTime() > end.getTime()) {
    		return list;
    	}
    	if(type == 0) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(start);
    		cal.set(Calendar.HOUR_OF_DAY, 0);
    		cal.set(Calendar.MINUTE, 0);
    		cal.set(Calendar.SECOND, 0);
    		while(!DateUtil.formatDate(cal.getTime(), "yyMMdd").equals(DateUtil.formatDate(end, "yyMMdd"))) {
    			Calendar cal2 = Calendar.getInstance();
    			cal2.setTime(cal.getTime());
        		cal2.set(Calendar.HOUR_OF_DAY, 23);
        		cal2.set(Calendar.MINUTE, 59);
        		cal2.set(Calendar.SECOND, 59);
    			Date pair[] = new Date[]{cal.getTime(), cal2.getTime()};
    			list.add(pair);
    			cal.add(Calendar.DAY_OF_YEAR, 1);
    		}
    		cal.setTime(end);
    		cal.set(Calendar.HOUR_OF_DAY, 0);
    		cal.set(Calendar.MINUTE, 0);
    		cal.set(Calendar.SECOND, 0);
    		Date d1 = cal.getTime();
    		cal.set(Calendar.HOUR_OF_DAY, 23);
    		cal.set(Calendar.MINUTE, 59);
    		cal.set(Calendar.SECOND, 59);
    		Date d2 = cal.getTime();
    		Date pair[] = new Date[]{d1, d2};
    		list.add(pair);
    	} else if(type == 1) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(start);
    		cal.set(Calendar.DAY_OF_WEEK, 1);
    		cal.set(Calendar.HOUR_OF_DAY, 0);
    		cal.set(Calendar.MINUTE, 0);
    		cal.set(Calendar.SECOND, 0);
    		Calendar cal2 = Calendar.getInstance();
    		cal2.setTime(end);
    		cal2.set(Calendar.DAY_OF_WEEK, 1);
    		cal2.set(Calendar.HOUR_OF_DAY, 0);
    		cal2.set(Calendar.MINUTE, 0);
    		cal2.set(Calendar.SECOND, 0);
    		//如果是同一周
    		if(cal.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
    			Date pair[] = new Date[]{start, end};
    			list.add(pair);
    		} else {
    			Date d1 = start;
    			cal.add(Calendar.WEEK_OF_YEAR, 1);
    			Calendar cc = Calendar.getInstance();
    			cc.setTime(cal.getTime());
    			cc.add(Calendar.SECOND, -1);
    			Date d2 = cc.getTime();
    			Date pair[] = new Date[]{d1, d2};
    			list.add(pair);
    			while(cal.get(Calendar.WEEK_OF_YEAR) != cal2.get(Calendar.WEEK_OF_YEAR)) {
    				Date _d1 = cal.getTime();
        			cal.add(Calendar.WEEK_OF_YEAR, 1);
        			Calendar _cc = Calendar.getInstance();
        			_cc.setTime(cal.getTime());
        			_cc.add(Calendar.SECOND, -1);
    				Date _d2 = _cc.getTime();
        			Date _pair[] = new Date[]{_d1, _d2};
        			list.add(_pair);
    			}
    			Date _d1 = cal2.getTime();
    			Date _d2 = end;
    			Date _pair[] = new Date[]{_d1, _d2};
    			list.add(_pair);
    		}
    	} else if(type == 2) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(start);
    		cal.set(Calendar.DAY_OF_MONTH, 1);
    		cal.set(Calendar.HOUR_OF_DAY, 0);
    		cal.set(Calendar.MINUTE, 0);
    		cal.set(Calendar.SECOND, 0);
    		Calendar cal2 = Calendar.getInstance();
    		cal2.setTime(end);
    		cal2.set(Calendar.DAY_OF_MONTH, 1);
    		cal2.set(Calendar.HOUR_OF_DAY, 0);
    		cal2.set(Calendar.MINUTE, 0);
    		cal2.set(Calendar.SECOND, 0);
    		//如果是同一月
    		if(cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
    			Date pair[] = new Date[]{start, end};
    			list.add(pair);
    		} else {
    			Date d1 = start;
    			cal.add(Calendar.MONTH, 1);
    			Calendar cc = Calendar.getInstance();
    			cc.setTime(cal.getTime());
    			cc.add(Calendar.SECOND, -1);
    			Date d2 = cc.getTime();
    			Date pair[] = new Date[]{d1, d2};
    			list.add(pair);
    			while(cal.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
    				Date _d1 = cal.getTime();
        			cal.add(Calendar.MONTH, 1);
        			Calendar _cc = Calendar.getInstance();
        			_cc.setTime(cal.getTime());
        			_cc.add(Calendar.SECOND, -1);
    				Date _d2 = _cc.getTime();
        			Date _pair[] = new Date[]{_d1, _d2};
        			list.add(_pair);
    			}
    			Date _d1 = cal2.getTime();
    			Date _d2 = end;
    			Date _pair[] = new Date[]{_d1, _d2};
    			list.add(_pair);
    		}
    	}
    	return list;
    }
    
    /**
     * 把毫秒值转为字符串输出: 00:00:00
     * @param millis
     * @return
     */
    public static String formatMillis(long millis) {
    	long sec = millis/1000;
    	int hour = (int)(sec/(60*60));
    	sec = sec % (60*60);
    	int min = (int)(sec / 60);
    	sec = sec % 60;
    	StringBuffer sb = new StringBuffer();
    	if(hour < 10) {
    		sb.append("0" + hour);
    	} else {
    		sb.append(hour);
    	}
    	sb.append(":");
    	if(min < 10) {
    		sb.append("0" + min);
    	} else {
    		sb.append(min);
    	}
    	sb.append(":");
    	if(sec < 10) {
    		sb.append("0" + sec);
    	} else {
    		sb.append(sec);
    	}
    	return sb.toString();
    }
    
    public static void main(String args[]) {
    	System.out.println(formatMillis(112333022L));
    }
    
}
