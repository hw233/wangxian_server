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
		Class c = Class.forName(XiaomiClientService.class.getName());
		
		if(c != null)
		{
			Field f = c.getDeclaredField("logger");
			f.setAccessible(true);
			f.set(null, NewLoginHandler.logger);
			out.println(((Logger)f.get(null)).getName());
			
			f = c.getDeclaredField("logger");
			f.setAccessible(true);
			out.println(((Logger)f.get(null)).getName());
		}
		
	}
	catch(Exception e)
	{
		out.println(e.getMessage());
	}
	
	out.println();
	out.println("修改前:"+XiaomiClientService.APPID);
	out.println("修改前:"+XiaomiClientService.APPKEY);
	
	if(!XiaomiClientService.APPID.equals("3087") )
	{
		XiaomiClientService.APPID = "3087";
	}
	
	if(!XiaomiClientService.APPKEY.equals("512e79c0-a81e-c2af-38b7-505af006f12f") )
	{
		XiaomiClientService.APPKEY = "512e79c0-a81e-c2af-38b7-505af006f12f";
	}
	
	out.println("修改后:"+XiaomiClientService.APPID);
	out.println("修改后:"+XiaomiClientService.APPKEY);
%>