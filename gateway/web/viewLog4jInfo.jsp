<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.apache.log4j.DailyRollingFileAppender"%>
<%@page import="org.apache.log4j.Appender"%>
<%@page import="org.apache.log4j.LogManager"%>
<%@page import="com.xuanzhi.tools.transport.Connection"%>
<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,java.io.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">	
<%
org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Connection.class);
	if(log != null)
		out.println("[connection class logger name]+ " + log.getName());
	else
	{
		out.println("[无法获取connection相关日志信息]");
	}
	
	Enumeration<org.apache.log4j.Logger> em = LogManager.getCurrentLoggers();
	
	while(em.hasMoreElements())
	{
		org.apache.log4j.Logger logg = em.nextElement();
		out.println("<p>[logger名字:"+logg.getName()+"] ["+logg.getEffectiveLevel()+"]<br>");
		Enumeration<Appender>  ema = logg.getAllAppenders();
		while(ema.hasMoreElements())
		{
			Appender aa = ema.nextElement();
			
			if(aa instanceof DailyRollingFileAppender){
				
				DailyRollingFileAppender faa = (DailyRollingFileAppender) aa; 
				out.println("..................[appendername:"+aa.getName()+"] ["+faa.getFile()+"]</p>");
			}
		}
		
		logg.debug("[打印出来]");
		logg.warn("warn");
		logg.info("info");
	}

	
	out.println("<h2>分析slf4j日志</h2>");
	out.println("<div style=\"clear:both;\"></div>");
	out.println("<p>");
	Logger logger = LoggerFactory.getLogger(Connection.class);
	
	if(logger != null)
	{
		out.println("[slf4j logger] [loggername:"+logger.getName()+"]");
	}
	else
	{
		out.println("[slf4j logger] [loggername:无配置]");	
	}
	logger.debug("[sl4j debug]");
	logger.warn("[sl4j warn]");
	logger.info("[sl4j info]");
	
	out.println("</p>");
	
%>	
</html>	