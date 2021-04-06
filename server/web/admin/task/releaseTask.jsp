<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	CountryManager.getInstance().data.reReleasePeektask();
	CountryManager.getInstance().data.reReleaseBricktask();
	out.print("OK");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重新发布任务</title>
</head>
<body>

</body>
</html>