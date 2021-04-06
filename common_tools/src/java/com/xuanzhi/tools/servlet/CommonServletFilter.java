package com.xuanzhi.tools.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*; 

import java.net.*;

import java.lang.reflect.*;
import com.xuanzhi.tools.text.StringUtil;
import org.apache.log4j.*;

/**
 * Servlet Fileter
 */
public class CommonServletFilter implements Runnable,Filter
{
	private static int m_iDumpThreadTimes = 0;
	private static long m_iCurrentKeyID = 0;
	
	private static boolean m_bDumpThreadEnabled = false;
	private static boolean m_bDumpThread = false;
	
	private static long m_maxThreadRunningTime = 300000L;
	
    private static Object oMBean = null;
    
	static
	{
		String strVersion = System.getProperty("java.vm.version");
		
		if (strVersion != null && strVersion.compareTo("1.5") >= 0)
		{
			m_bDumpThreadEnabled = true;
			
			try
			{
				Class oMFactClass = Class.forName("java.lang.management.ManagementFactory");
				Method oMethod = oMFactClass.getMethod("getThreadMXBean", new Class[0]);
				
				oMBean = oMethod.invoke(oMFactClass, new Object[0]);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static boolean dumpThreadOn()
	{
		return dumpThreadOn(100);
	}
	
	public static boolean dumpThreadOn(int iDumpThreadTimes)
	{
		m_bDumpThread = m_bDumpThreadEnabled;
		if (m_bDumpThread)
		{
			m_iDumpThreadTimes = iDumpThreadTimes;
		}
		
		return m_bDumpThread;
	}
	
	public static boolean dumpThreadOff()
	{
		m_bDumpThread = false;
		
		return m_bDumpThread;
	}
	
	public static boolean getDumpThread()
	{
		return m_bDumpThread;
	}
	
	public static void setMaxThreadRunningTime(long max){
		m_maxThreadRunningTime = max;
	}
	
	public static long getMaxThreadRunningTime(){
		return m_maxThreadRunningTime;
	}
	
	protected static synchronized long getNextKeyID()
	{
		return m_iCurrentKeyID++;
	}
	
	// log
	private Logger m_logger = null;
	
	private FilterConfig filterConfig;
	
	private Hashtable m_htRunningThreads;

	private Thread thread;
	
	public void logger(String s)
	{
		if(m_logger == null)
		{
			String name = System.getProperty("filter.log.name",getClass().getName());
			m_logger = Logger.getLogger(name);
		}

		if(m_logger != null)
			m_logger.info(s);
	}
	
	public void logger(String s,Throwable t)
	{
		if(m_logger == null)
		{
			String name = System.getProperty("filter.log.name",getClass().getName());
			m_logger = Logger.getLogger(name);
		}
		if(m_logger != null)
			m_logger.warn(s,t);
	}
	
	public void init(FilterConfig config)
	{
		if(this.filterConfig == null)
		{
		   this.filterConfig = config;
	       
	       thread = new Thread(this,"Filter Check Thread");
	       thread.setDaemon(true);
	       thread.start();
		   
		   m_htRunningThreads = new Hashtable();
		}
	}

	public void destroy()
	{
		if(thread != null)
			thread.interrupt();
		
		m_htRunningThreads.clear();
	}
	
	public void setFilterConfig(FilterConfig config)
	{
		if(this.filterConfig == null)
		{
	       this.filterConfig = config;
	       
	       thread = new Thread(this,"Filter Check Thread");
	       thread.setDaemon(true);
	       thread.start();
		   
		   m_htRunningThreads = new Hashtable();
		}  
	}

	/**
	 * Return the FilterConfig for this Filter.
	 */
	public FilterConfig getFilterConfig()
	{
	     return this.filterConfig ;
	}
	 
	 /**
	  * The doFilter method of the Filter is called by the container 
	  * each time a request/response pair is passed through the chain 
	  * due to a client request for a resource at the end of the chain. 
	  * The FilterChain passed in to this method allows the Filter to pass on 
	  * the request and response to the next entity in the chain.
	  * 
	  */
	 public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
		 throws IOException ,ServletException
	 {
		 doFilter((HttpServletRequest)request,(HttpServletResponse)response,chain);
	 }
	 
	 protected final static String NO_ADDR = "0.0.0.0";

	 protected final static String UNKNOWN = "unknown";
	 
	 protected final static String CHANNEL = "channel=";

	 protected String getAccessUser(HttpServletRequest request){
		 String userStr = (String)request.getSession().getAttribute("userId");
		 if(userStr == null) userStr = "#游客#";
		 return userStr;
	 }
	 
	 protected String getAccessChannel(HttpServletRequest request){
		 String channel = (String)request.getSession().getAttribute("access.channel");
		 if(channel != null) return channel;
		 String s = request.getQueryString();
		 if(s != null && s.indexOf(CHANNEL) > -1){
			 s = s.substring(s.indexOf(CHANNEL)+CHANNEL.length());
			 if(s.indexOf('&')>-1){
				 s = s.substring(0,s.indexOf('&'));
			 }
			 if(s.length() > 0){
				 request.getSession().setAttribute("access.channel",s);
				 return s;
			 }
		 }
		 return "NOCHAN";
	 }
	 
	 protected String getReferer(HttpServletRequest request){
		 String referer = (String)request.getSession().getAttribute("access.referer");
		 if(referer != null) return referer;
		 String s = request.getHeader("Referer");
		 if(s != null && s.length() > 0){
				try{
				 s = URLDecoder.decode(new String(s.getBytes("ISO-8859-1"),"gbk"),"gbk");
				}catch(Exception e){
					e.printStackTrace();
				}
				 request.getSession().setAttribute("access.referer",s);
				 return s;
		 }
		 return "-";
	 }

	 
	 /**
	  * The doFilter method of the Filter is called by the container 
	  * each time a request/response pair is passed through the chain 
	  * due to a client request for a resource at the end of the chain. 
	  * The FilterChain passed in to this method allows the Filter to pass on 
	  * the request and response to the next entity in the chain.
	  */
     public void doFilter(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
		 throws IOException ,ServletException
	 {
		String queryString = request.getQueryString();
		if(queryString == null) queryString = "";
		 
		final String url = request.getRequestURI() + "?" + URLDecoder.decode(new String(queryString.getBytes("ISO-8859-1"),"gbk"),"gbk");
		final String sessionId = request.getSession().getId();
		 
		String addr = request.getRemoteAddr();
		if (addr == null) addr = NO_ADDR ; 
			
		String accessUser = getAccessUser(request);
		
		//String accessChannel = getAccessChannel(request);
		
		//String referer = getReferer(request);
		
		Long oKey = new Long(getNextKeyID());
		
		ThreadWrapper oWrapper = new ThreadWrapper();
		
		oWrapper.m_strInfo = "["+accessUser+"] ["+addr+"] ["+sessionId+"] ["+url+"]";
		oWrapper.m_oThread = Thread.currentThread();
		oWrapper.currentTimeMillis = System.currentTimeMillis();
		
		m_htRunningThreads.put(oKey, oWrapper);
		
		logger("[enter] [" + m_htRunningThreads.size() + "] [#######] [" + (Runtime.getRuntime().totalMemory() / 1048576) + "M] " + oWrapper.m_strInfo);
		
		try
		{
			chain.doFilter(request,response);
		}
		catch(ServletException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new ServletException(e);
		}
		finally
		{
			m_htRunningThreads.remove(oKey);
			
			String ss = StringUtil.addcommas(System.currentTimeMillis() - oWrapper.currentTimeMillis);
			for (int i = ss.length(); i < 7; i++)
			{
				ss += " ";
			}
			
			logger("[exit#] [" + m_htRunningThreads.size() + "] ["+ss+"] [" + (Runtime.getRuntime().totalMemory() / 1048576) + "M] " + oWrapper.m_strInfo);
		}
	 }
	 
	 public void run()
	 {
	 	while (thread.isInterrupted() == false)
	 	{
	 		try
	 		{
	 			Thread.sleep(1000);
			}
			catch(Exception e){}	
 			
 			Enumeration oEnum = m_htRunningThreads.keys();
 			while (oEnum.hasMoreElements())
 			{
 				Long oKey = (Long)oEnum.nextElement();
 				
 				ThreadWrapper oWrapper = (ThreadWrapper)m_htRunningThreads.get(oKey);
 				
 				if (oWrapper == null)
 				{
 					continue;
 				}
 				
				long l = System.currentTimeMillis() - oWrapper.currentTimeMillis;
				
				if (l > 5000)
				{
					String ss = StringUtil.addcommas(l);
					
					String strStateInfo = " [N/A]";
					
					if (oMBean != null)
					{
						try
						{
							Method oMethod = Thread.class.getMethod("getId", new Class[0]);
							Object oThreadID = oMethod.invoke(oWrapper.m_oThread, new Object[0]);
							
							Class oClass = Class.forName("java.lang.management.ThreadMXBean");
							oMethod = oClass.getMethod("getThreadInfo", new Class[] {Long.TYPE});
                            Object oThreadInfo = oMethod.invoke(oMBean, new Object[] {oThreadID});
                            
                            oClass = Class.forName("java.lang.management.ThreadInfo");
                            oMethod = oClass.getMethod("getThreadState", new Class[0]);
                            
							Object oState = oMethod.invoke(oThreadInfo, new Object[0]);
							
							strStateInfo = " [" + oThreadID.toString() + "-" + oState + "]";
						}
						catch (Throwable e)
						{
							e.printStackTrace();
						}
					}
					
					if (m_bDumpThread || (m_bDumpThreadEnabled && l > 60000))
					{
						if (m_bDumpThread && --m_iDumpThreadTimes <= 0)
						{
							m_bDumpThread = false;
						}
						
						try
						{
							Method oMethod = Thread.class.getMethod("getStackTrace", new Class[0]);
							Object oRet = oMethod.invoke(oWrapper.m_oThread, new Object[0]);
							StackTraceElement[] oTraceArr = (StackTraceElement[])oRet;
							
							int iLength = oTraceArr == null ? 0 : oTraceArr.length;
							
							StringBuffer oBuf = new StringBuffer();
							for (int i = 0; i < iLength; i++)
							{
								oBuf.append("\n\tat ").append(oTraceArr[i].toString());
							}
							
							strStateInfo += oBuf.toString();
						}
						catch (Throwable e)
						{
							e.printStackTrace();
						}
					}
					
					logger("[WARN#] [" + m_htRunningThreads.size() + "] ["+ss+"] [" + (Runtime.getRuntime().totalMemory() / 1048576) + "M] " + oWrapper.m_strInfo + strStateInfo);
					
					if (l > m_maxThreadRunningTime)
					{
						m_htRunningThreads.remove(oKey);
						
						logger(" ****************************************************************\n   prepare to stop this thread ["+oWrapper.m_oThread.getName()+"] with key ["+oWrapper.m_strInfo+"]... ...");
						long k = System.currentTimeMillis();
						
						try
						{
							oWrapper.m_oThread.interrupt();
							oWrapper.m_oThread.stop();
						}
						catch(Exception e)
						{
							logger("stop one death loop thread error:\n",e);
						}
						
						k = System.currentTimeMillis() - k;
						
						logger(" ****************************************************************\n   stop this thread ["+oWrapper.m_oThread.getName()+"] with key ["+oWrapper.m_strInfo+"] cost " + k);
					}
				}
			}
		} 
	}
	
	protected class ThreadWrapper
	{
		protected long currentTimeMillis ;
		protected String m_strInfo = null;
		protected Thread m_oThread = null;
	}
}
