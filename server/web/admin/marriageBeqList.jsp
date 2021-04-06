<%@page import="com.fy.engineserver.marriage.MarriageBeq"%>
<%@page import="com.fy.engineserver.marriage.manager.MarriageManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	HashMap<Long, MarriageBeq> underRuleMap = MarriageManager.getInstance().getBeqMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table border="1">

		<tr>
		<td>rb.getId() </td>
		<td>rb.getSendId() </td>
		<td>rb.getToId() </td>
		<td>rb.getState() </td>
		<td>rb.getLevel() </td>
	</tr>

	<%
		for (Iterator<MarriageBeq> iterator = underRuleMap.values().iterator(); iterator.hasNext();) {

			MarriageBeq rb = iterator.next();
	%>
	<tr>
		<td><%=rb.getId() %></td>
		<td><%=rb.getSendId() %></td>
		<td><%=rb.getToId() %></td>
		<td><%=rb.getState() %></td>
		<td><%=rb.getLevel() %></td>
	</tr>
	<%
		}
	%>
	</table>
</body>
</html>