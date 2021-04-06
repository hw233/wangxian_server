<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	out.print(application.getRealPath("/"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>启动信息</title>
</head>
<body>
	<%
	out.print(System.getProperty("user.dir") + "<BR/>");
		out.print(TaskManager.notices.toString());
	%>
</body>
</html>