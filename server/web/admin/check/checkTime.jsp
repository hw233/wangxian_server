<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!void doNo() {
	}%>

<%
	String snum = request.getParameter("num");
	if (snum == null || snum.isEmpty()) {
		snum = "";
	} else {
		long num = Long.valueOf(snum);
		long start = System.currentTimeMillis();
		for (long i = 0; i < num; i++) {
			SystemTime.currentTimeMillis();
		}
		long end = System.currentTimeMillis();
		out.print("[运行次数:" + num + "][耗时" + (end - start) + "MS]");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./checkTime.jsp" method="post">
		运行次数[System.currentTimeMillis()] <input type="text" name="num"
			value="<%=snum%>"> <input type="submit" value="OK">
	</form>
</body>
</html>