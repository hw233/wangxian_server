<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.lang.reflect.Field"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.gamegateway.thirdpartner.lenovo.LenovoClientService"%>
<%
	try
	{
		Class c = Class.forName(LenovoClientService.class.getName());
		
		if(c != null)
		{
			Field f = c.getDeclaredField("logger");
			f.setAccessible(true);
			//f.set(null, NewLoginHandler.logger);
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
	out.println("修改前:"+LenovoClientService.GAMEID);
	out.println("修改前:"+LenovoClientService.SECRETKEY);
	
/* 	if(!LenovoClientService.GAMEID.equals("10154") )
	{
		LenovoClientService.GAMEID = "10154";
	}
	
	if(!LenovoClientService.SECRETKEY.equals("8c55352dd2efe601637a") )
	{
		LenovoClientService.SECRETKEY = "8c55352dd2efe601637a";
	}
	
	out.println("修改后:"+LenovoClientService.GAMEID);
	out.println("修改后:"+LenovoClientService.SECRETKEY); */

%>