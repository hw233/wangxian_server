<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	if ("update".equals(request.getParameter("option"))) {
		CoreSubSystem.closeCharge = "true".equals(request.getParameter("closeCharge"));
		CoreSubSystem.closeChargeNotice = request.getParameter("closeChargeNotice");
	}

	out.print("<H1>当前充值已：" + (CoreSubSystem.closeCharge ? "<font color=red>关闭" : "<font color=green>开放") + "</font></H1>");
	out.print("<H3>当前充值提示:"+ CoreSubSystem.closeChargeNotice +"</H3>");
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值屏蔽</title>
</head>
<body>

	<form action="./closeCharge.jsp" method="post">
	
		是否屏蔽[true/false]: <input type="text" name="closeCharge" value="<%=CoreSubSystem.closeCharge%>"><br/>
		关闭充值给用户的提示&nbsp;：<input type="text" size="120px" name="closeChargeNotice" value="<%=CoreSubSystem.closeChargeNotice%>"></BR>
		<input type="hidden" name="option" value="update">
		<input value="提交" type="submit">
	</form>
	
</body>
</html>