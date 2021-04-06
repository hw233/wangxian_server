<%@page import="com.fy.engineserver.economic.charge.ChargeMode"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String channel = request.getParameter("channel");
	List<ChargeMode>  list = ChargeManager.getInstance().getSpecialChargeMode(channel);
	out.print("渠道:" + channel + "<BR/>");
	if (list == null) {
		out.print("无");
	} else {
		for (ChargeMode cm : list) {
			out.print(cm.getModeName() + "</BR>");
		}
	}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值信息</title>
</head>
<body>
	<form action="./checkCharge.jsp" method="post">
		要查询的渠道<input type="text" name="channel">
		<input type="submit" value="查询">
	</form>
</body>
</html>