<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="java.util.Collection"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String action = request.getParameter("action");
	String pwd = request.getParameter("pwd");
	if ("do".equals(action)) {
		if ("pwdoffengyin".equals(pwd)) {
			Collection<Cave> caves = FaeryManager.getInstance().getCaveIdmap().values();
			for (Cave cave : caves) {
				cave.khatam(cave.getFaery(),-1);
			}
			out.print("<封印仙府执行完毕>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>封印所有仙府</title>
</head>
<body>
	<form action="./khatam.jsp">
	
		输入密码:<input name="pwd" type="password">
		<input name="action" type="text" value="do">
		<input type="submit" value="封印">
	
	</form>
</body>
</html>