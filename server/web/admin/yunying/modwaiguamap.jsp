<%@page import="com.fy.engineserver.smith.waigua.ClientMessageHistory"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.smith.waigua.WaiguaManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<%
	WaiguaManager waiguaManager = WaiguaManager.getInstance();
	Hashtable<Long, ClientMessageHistory> hashtable = new Hashtable<Long, ClientMessageHistory>();
	Field f = waiguaManager.getClass().getDeclaredField("playerMessageHistoryMap");
	f.setAccessible(true);
	f.set(waiguaManager,hashtable);
	f = waiguaManager.getClass().getDeclaredField("playerMessageHistoryMap");
	f.setAccessible(true);
	out.println("现在的字段类型为:"+f.get(waiguaManager).getClass().getCanonicalName());
	
	f = waiguaManager.getClass().getDeclaredField("running");
	f.setAccessible(true);
	f.set(waiguaManager, false);
	
	out.println("字段"+f.getName()+"的值为:"+f.get(waiguaManager));
	
	f = waiguaManager.getClass().getDeclaredField("openning");
	f.setAccessible(true);
	f.set(waiguaManager, false);
	
	out.println("字段"+f.getName()+"的值为:"+f.get(waiguaManager));

%>


</body>
</html>