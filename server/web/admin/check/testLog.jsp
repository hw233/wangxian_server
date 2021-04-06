<%@page import="com.fy.engineserver.util.MemoryControler"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String strTimes = request.getParameter("times");
	String string = request.getParameter("string");
	String random = request.getParameter("random");
	if (strTimes == null || strTimes.isEmpty()) {
		strTimes = "0";
		string = "abc";
		random = "true";
	} else {
		out.print("执行次数:" + strTimes + "<BR/>");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试log对内存的消耗</title>
</head>
<body>
	<form action="./testLog.jsp" method="post">
		执行次数:<input name="times" type="text" value="<%=strTimes%>">
		随机的字符串:<input name="string" type="text" value="<%=string%>">
		是否生成新的字符串打印:<input name="random" type="text" value="<%=random%>">
		<input type="submit" value="提交">
	</form>
</body>
</html>