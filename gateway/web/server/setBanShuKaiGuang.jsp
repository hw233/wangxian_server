<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.Serializable"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("validateShenfenzheng")) {
				MieshiGatewaySubSystem.getInstance().setValidateShenfenzheng(!MieshiGatewaySubSystem.getInstance().isValidateShenfenzheng());
			}else if (action.equals("showShenfenzheng")) {
				MieshiGatewaySubSystem.getInstance().setShowShenfenzheng(!MieshiGatewaySubSystem.getInstance().isShowShenfenzheng());
			}else if (action.equals("BANHAOS")) {
				String a = request.getParameter("banhao");
				a = a.substring(1, a.length() - 1);
				String[] bhs = a.split(", ");
				MieshiGatewaySubSystem.BANHAOS = bhs;
			}
		}
	%>
	<form name="f1">
		是否验证身份证合不合法-------当前(<%=MieshiGatewaySubSystem.getInstance().isValidateShenfenzheng() %>)
		<input type="hidden" name="action" value="validateShenfenzheng">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<br>
	<form name="f2">
		是否注册时显示输入身份证号-------当前(<%=MieshiGatewaySubSystem.getInstance().isShowShenfenzheng() %>)
		<input type="hidden" name="action" value="showShenfenzheng">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<br>
	<form name="f3">
		版号-------当前(<%=Arrays.toString(MieshiGatewaySubSystem.BANHAOS) %>)
	<br>
		<input type="hidden" name="action" value="BANHAOS">
		<input type="text" name="banhao" value="<%=Arrays.toString(MieshiGatewaySubSystem.BANHAOS) %>">
		<input type="submit" value=<%="确定"%>>
	</form>
</body>
</html>
