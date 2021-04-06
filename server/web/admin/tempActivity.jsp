<%@page import="com.fy.engineserver.economic.charge.ChargeMode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ChargeManager cm = ChargeManager.getInstance();
	Field f = ChargeManager.class.getDeclaredField("channelChargeModes");
	f.setAccessible(true);
	HashMap<String, List<ChargeMode>> channelChargeModes = (HashMap<String, List<ChargeMode>>)f.get(cm);
	for (List<ChargeMode> list : channelChargeModes.values()) {
		for (ChargeMode chargeMode : list) {
			chargeMode.setFootDescription("充值. 4月3日维护后-4月11日23:59，累计充值达98、498、998、1998、4998、9998、19998元，拿七重大礼，重重逆天，一步登仙！");
		}
	}
	out.print("OK");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刷腾讯充值</title>
</head>
<body>

</body>
</html>