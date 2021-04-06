<%@page
	import="com.fy.engineserver.newtask.service.MastLineTaskOpenNextTaskEventTransact"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String[] names = MastLineTaskOpenNextTaskEventTransact.getInstance().getMastLineTaskName();
	for (String s : names) {
		out.print(s + "<BR/>");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>