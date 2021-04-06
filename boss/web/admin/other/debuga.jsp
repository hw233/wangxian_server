<%@page import="com.fy.boss.finance.service.PlatformSavingCenter"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	java.util.Enumeration names = request.getHeaderNames();
	while(names.hasMoreElements())
	{
		String name = (String) names.nextElement();
	PlatformSavingCenter.logger.info(name + ":" + request.getHeader(name));
	}
	
	
	Map req = request.getParameterMap();
	
	PlatformSavingCenter.logger.info("请求内容:"+req);
	
	
%>
</body>
</html>