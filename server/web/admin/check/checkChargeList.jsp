<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.fy.engineserver.bourn.BournManager"%>
<%@page import="com.fy.engineserver.core.NewPlayerLeadDataManager"%>
<%@page import="com.fy.engineserver.activity.CheckManager"%>
<%@page
	import="com.fy.engineserver.activity.peekandbrick.PeekAndBrickManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ChargeManager manager = ChargeManager.getInstance();
	out.print(CheckManager.check(manager));
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>