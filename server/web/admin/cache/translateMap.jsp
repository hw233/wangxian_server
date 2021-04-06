<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page
	import="com.fy.engineserver.datasource.language.MultiLanguageTranslateManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Map<String, String> map = MultiLanguageTranslateManager.getInstance().getTranslatedMap();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table style="font: 12px;" border="1">
	<%
		for (Iterator<String> iterator = map.keySet().iterator();iterator.hasNext();) {
			String old = iterator.next();
			String newS = map.get(old);
	%>
	<tr>
		<td><%=old %></td>
		<td><%=newS %></td>
	</tr>
	 <%
	 	} 
	 %>
	 </table>
</body>
</html>