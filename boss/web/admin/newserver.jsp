<%@page import="java.util.Iterator"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.boss.platform.kunlun.KunlunSavingManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	KunlunSavingManager manager = KunlunSavingManager.getInstance();
	
	Field mapField = KunlunSavingManager.class.getDeclaredField("serverMap");
	mapField.setAccessible(true);
	Map<String,String> serverMap = (Map<String,String>)mapField.get(manager);
	serverMap.put("守望仙風", "474030");
	
	mapField.set(manager, serverMap);
	out.print("<HR>");
	for (Iterator<String> itor = serverMap.keySet().iterator();itor.hasNext();) {
		String key = itor.next();
		out.print(key + ":" + serverMap.get(key) + "<BR>");
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>