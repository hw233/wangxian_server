<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.battlefield.jjc.JJCManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>不能匹配bug</title>
</head>
<body>
<%
	Class cl = JJCManager.class;
	Field f = cl.getDeclaredField("timeRecord");
	f.setAccessible(true);
	Map<Long, Long> timeRecord = (Map<Long, Long>)f.get(JJCManager.getInstance());
	for(long id : timeRecord.keySet()){
		out.print("id:"+id+"--value:"+timeRecord.get(new Long(id))+"<br>");
	}
	Map<Long, Long> timeRecord_new =  new LinkedHashMap<Long, Long>(); 
	f.set(JJCManager.getInstance(), timeRecord_new);
	timeRecord = (Map<Long, Long>)f.get(JJCManager.getInstance());
	out.print("============================");
	for(long id : timeRecord.keySet()){
		out.print("id:"+id+"--value:"+timeRecord.get(new Long(id)));
	}
	out.print(timeRecord.size());
%>
</body>
</html>
