<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String uName = "89114850";
	Passport p=BossClientService.getInstance().getPassportByUserName(uName);
	
	out.print(p.getRegisterChannel());
	out.print("<BR/>");
	out.print(p.getUcPassword());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>