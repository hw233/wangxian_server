<%@page import="com.fy.engineserver.activity.TransitRobbery.RobberyThread"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看渡劫异常线程</title>
</head>
<body>

<%
	TransitRobberyManager manager =	TransitRobberyManager.getInstance();

	Field f = TransitRobberyManager.class.getDeclaredField("robberyThreads");
	f.setAccessible(true);
	RobberyThread[] rts = (RobberyThread[]) f.get(manager);
	for(int i=0; i<rts.length; i++)
	{
		out.println("dijige:"+i+"/total:"+rts.length+"<br/>");
		RobberyThread rt = rts[i];
		if(rt == null)
		{
			out.println("faxiannull:"+i+"<br/>");
		}
		else
		{
			if(rt.forceLeftTimes == null)
			{
				out.println("faxiannullshuxing:"+rt.getName()+"<br/>");
			}
			else
			{
				out.println("normal:"+rt.getName()+"<br/>");
			}
		}
	}

%>

</body>
</html>