<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<% 
	ChargeManager.useNewInterfaceServer.add("神魂不灭");
ChargeManager.useNewInterfaceServer.add("域外之战");
ChargeManager.useNewInterfaceServer.add("天道再临");
ChargeManager.useNewInterfaceServer.add("仙道独尊");

%>

</body>
</html>