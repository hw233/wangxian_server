<%@page import="com.fy.gamegateway.language.Translate"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.lang.reflect.Field"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.gamegateway.thirdpartner.lenovo.LenovoClientService"%>
<%
	try
	{
		Class c = Class.forName(Translate.class.getName());
		Translate t = (Translate)c.newInstance();
		
		if(c != null)
		{
			Field[] fields = c.getFields();
			
			for(Field f : fields)
			{
				f.setAccessible(true);
				//f.set(null, NewLoginHandler.logger);
				out.print("字段名称:"+f.getName()+"\t字段值:"+f.get(t)+"<br/>");
				

			}
		}
		
	}
	catch(Exception e)
	{
		out.println(e.getMessage());
	}
	
	out.println();
	out.println("修改前:"+LenovoClientService.GAMEID);
	out.println("修改前:"+LenovoClientService.SECRETKEY);
	

	
	out.println("修改后:"+LenovoClientService.GAMEID);
	out.println("修改后:"+LenovoClientService.SECRETKEY);

%>