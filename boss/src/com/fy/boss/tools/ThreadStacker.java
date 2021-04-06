package com.fy.boss.tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fy.boss.tools.ThreadStacker;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;

public class ThreadStacker implements Runnable  {
	//Logger logger  = Logger.getLogger(ThreadStacker.class);
	
	public static void main(String args[]) {
		//String threadId = args[0];
		String threadId = "280";
		ThreadStacker stacker = new ThreadStacker(threadId);
		stacker.start();
	}
	
	private String threadId;
	
	public ThreadStacker(String threadId) {
		this.threadId = threadId;
	}
	
	public void start() {
		Thread t = new Thread(this, "ThreadStacker");
		t.start();
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(2000);
				String content = getStackTrace();
				System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " ===================================================================================\n" + content + "\n");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getStackTrace() {
		String url = "http://116.213.192.194:8001/thread/threadRun.jsp?thread_id="+threadId+"&s=&n=&authorize.username=innerapp&authorize.password=innerapp123";
		String content = getPageContent(url);
		String regex = "Thread\\(.*?</blockquote></blockquote>";
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(content);
		if(m.find()) {
			String s = m.group(0);
			s = s.replaceAll("<BR>", "\n");
			return s;
		} else {
			return "no match";
		}
	}
	
	public static String getPageContent(String url) {
		try {
			byte result[] = HttpUtils.webGet(new URL(url), new HashMap(), 10000, 10000);
			return new String(result);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
