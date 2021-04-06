<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.platform.CloseFunctionManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>

<head>
<title>test</title>
</head>
<body>
	<%
		CloseFunctionManager.closeRegisterPlayerServers.clear();
		out.print("OK，可以注册了");
	%>
	

</body>
</html>

