<%@page import="com.fy.engineserver.enterlimit.AutoLimitEnterManager"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	EnterLimitManager.openOfsameIP = false;
	AutoLimitEnterManager.getInstance().autoLimitEnterList .clear();
	out.print("ok");

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关闭自动封外挂</title>
</head>
<body>

</body>
</html>