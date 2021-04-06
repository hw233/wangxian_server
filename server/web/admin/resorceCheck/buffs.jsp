<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	BuffTemplate[] buffArr = BuffTemplateManager.getInstance().getAllBuffTemplates();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	for (BuffTemplate buffTemplate : buffArr) {
		out.print(buffTemplate.getName());		
		out.print("[getGroupId:"+buffTemplate.getGroupId()+"]");		
		out.print("[getId:"+buffTemplate.getId()+"]");		
		out.print("[getIconId:"+buffTemplate.getIconId()+"]");		
		out.print("<BR/>");		
	}
%>
</body>
</html>