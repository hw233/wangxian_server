<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.activity.ServerActivity"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page
	import="com.fy.engineserver.activity.newserver.NewServerActivityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	HashMap<Integer, String> spPrizeTW = new HashMap<Integer, String>();
	spPrizeTW.put(10, Translate.炼狱神匣);
	spPrizeTW.put(20, Translate.炼狱宝匣);
	NewServerActivityManager.getInstance().putIntoMap(new ServerActivity(Platform.台湾, "蜀山之道", "2013-03-12 00:00:00", "2013-03-26 00:00:00", spPrizeTW));
	out.print("ok");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>