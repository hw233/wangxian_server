<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>


<%@page import="com.fy.gamegateway.mieshi.waigua.SessionManager"%><head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
out.print(SessionManager.isSessionServerWeiHu+"<br>");
%>
</body>
	
