<%@page
	import="com.fy.engineserver.activity.RefreshSpriteManager.RefreshSpriteData"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.activity.RefreshSpriteManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Map<String, RefreshSpriteData> map = RefreshSpriteManager.getInstance().getConfigedDatas();
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>所有刷新活动</title>
</head>
<body>
<table>
	<%
		for (Iterator<String> itor = map.keySet().iterator();itor.hasNext();) {
			String name = itor.next();
			%>
			<tr><%=name %></tr>
			<%
		}
	%>
</table>
</body>
</html>