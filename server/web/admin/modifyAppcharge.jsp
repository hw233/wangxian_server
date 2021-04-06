<%@page import="com.fy.engineserver.economic.charge.ChargeMode"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<ChargeMode> list = ChargeManager.getInstance().getChannelChargeModes("APPSTORE_MIESHI");
	if (list == null || list.size() == 0) {
		out.print("<H1>没有充值方式</H1>");
		return;
	}
	ChargeMode cm = list.get(0);
	cm.setFootDescription("请选择充值方式");
	out.print("修改完毕!" + cm.toString());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置ChargeMode.FootDescription</title>
</head>
<body>

</body>
</html>