<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.engineserver.uniteserver.DataUnit"%>
<%@page import="com.fy.engineserver.uniteserver.DataCollect"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String forcestopClasses = "";
	String[] forcestopClassNames = null;
	UnitedServerManager unitedServerManager = UnitedServerManager.getInstance();
	boolean isforce = ParamUtils.getBooleanParameter(request, "isForce");
	if(isforce)
	{
		String forcestop = ParamUtils.getParameter(request, "forcestop");
		
		if(forcestop != null && forcestop.trim().length() > 0)
		{
			forcestopClassNames = forcestop.split("\r*\n"); 
			
			Iterator<Class<?>> it = UnitedServerManager.uniteClasses.iterator();
			while(it.hasNext())
			{
				Class<?>  key = it.next();
				if(key != null)
				{
					DataUnit<?>  dc =	UnitedServerManager.dataUnitMap.get(key);
				
					
					
					
					if(dc != null)
					{
						if(dc.running)
						{
							for(String name : forcestopClassNames)
							{
								if(key.getCanonicalName().equals(name))
								{
									dc.running = false;
									out.println("停止["+key.getCanonicalName()+"]的合服进程<br/><br/><br/>");
									break;
								}
							}
						}
					}
					else
					{
						out.println(""+key.getCanonicalName()+":还没有合<br/><br/><br/>");
					}
				}
			}
			
		}
	}

%>

<%
	
	Iterator<Class<?>> it = UnitedServerManager.uniteClasses.iterator();
	
	while(it.hasNext())
	{
		Class<?>  key = it.next();
		if(key != null)
		{
			DataUnit<?>  dc =	UnitedServerManager.dataUnitMap.get(key);
			DataCollect<?> dcl = UnitedServerManager.dataCollectMap.get(key);
			if(dcl != null)
			{
				if(dcl.running)
				{
						out.println("["+key.getCanonicalName()+"]的合服进程还在收集数据（不正常）<br/><br/><br/>");
				}
				else
				{
					out.println("["+key.getCanonicalName()+"]的合服进程已经停止<br/><br/><br/>");
				}
			}
			else
			{
				out.println(""+key.getCanonicalName()+":收集完成<br/><br/><br/>");
			}
			
			if(dc != null)
			{
				out.println(""+key.getCanonicalName()+":"+dc.savedNum+":"+dc.getReceiveTotalNum()+":"+dc.running+"&nbsp;&nbsp;<br/>");
				if(dc.running)
				{
					forcestopClasses+=key.getCanonicalName()+"\r\n";
				}
			}
			else
			{
				out.println(""+key.getCanonicalName()+":没有合<br/>");
			}
		}
	}
	
	


%>
<form method="post">
<input type="hidden"  name="isForce" value="true" /><br/>
<textarea cols="50" rows="50"  name="forcestop"><%=forcestopClasses %></textarea><br/>
<input type="submit" value="提交" />
</form>


</body>
</html>