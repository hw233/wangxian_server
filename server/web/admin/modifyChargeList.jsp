<%@page import="com.fy.engineserver.economic.charge.ChargeMode"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	ChargeManager chargeManager = ChargeManager.getInstance();
	Field f = ChargeManager.class.getDeclaredField("channelChargeModes");
	HashMap<String, List<ChargeMode>> channelChargeModes = (HashMap<String, List<ChargeMode>> )f.get(chargeManager) ;
	channelChargeModes

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改pp助手的充值方式</title>
</head>
<body>

</body>
</html>