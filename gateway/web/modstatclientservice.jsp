<%@page import="com.xuanzhi.stat.client.StatClientService"%>
<%@page import="com.fy.gamegateway.thirdpartner.xiaomi.XiaomiClientService"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.lang.reflect.Field"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.gamegateway.thirdpartner.lenovo.LenovoClientService"%>
<%
	try
	{
		StatClientService.logger = NewLoginHandler.logger;
		
		
	}
	catch(Exception e)
	{
		out.println(e.getMessage());
	}

%>