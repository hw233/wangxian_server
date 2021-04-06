<%@page import="com.fy.engineserver.util.console.MConsole"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.util.console.MConsoleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<MConsole> mconsoles = MConsoleManager.getConsoles();
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台修改属性</title>
</head>
<body>
<center>
	<table style="font-size: 14px;" border="1">
		<tr style="background-color: #6AACD9;color: white;font-weight: bold;text-align: center;">
			<td>类名</td>
			<td>名字</td>
			<td>描述</td>
		</tr>
	<%
		for (MConsole console : mconsoles) {
		%>
			<tr>
				<td><%=console.getClass().getSimpleName() %></td>
				<td><a href="./mconsole.jsp?className=<%=console.getClass().getSimpleName() %>" target="_blank"><%=console.getMConsoleName() %></a></td>
				<td><%=console.getMConsoleDescription() %></td>
			</tr>
		<%
		}
	%>
	</table>
</center>
</body>
</html>