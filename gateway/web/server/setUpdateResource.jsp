<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%>
<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("cleanQuDao")) {
			String qudao = request.getParameter("qudao");
			MieshiGatewaySubSystem.qudaoNames.remove(qudao);
		}else if (action.equals("closeA")) {
			MieshiGatewaySubSystem.isOpenResourceUpdate = !MieshiGatewaySubSystem.isOpenResourceUpdate;
		}else if (action.equals("addQuDao")) {
			String qudao = request.getParameter("qudao");
			MieshiGatewaySubSystem.qudaoNames.add(qudao);
		}
	}
%>
<html>
<head>
</head>
<body>
<center>
<h2></h2><br><a href="./setUpdateResource.jsp">刷新此页面</a><br>
<br>

<%
	for (String ll : MieshiGatewaySubSystem.qudaoNames) {
		out.print(ll);
%>
<br>
<%
	}
%>
<br><br><br>
<form name="f1">
		删除渠道
		<input type="hidden" name="action" value="cleanQuDao">
		渠道<input name="qudao">
		<input type="submit" value=<%="确定"%>>
</form>
<br>
<form name="f2">
		加渠道
		<input type="hidden" name="action" value="addQuDao">
		渠道<input name="qudao">
		<input type="submit" value=<%="确定"%>>
</form>
<br>
<form name="f3">
		关闭功能<%=MieshiGatewaySubSystem.isOpenResourceUpdate %>
		<input type="hidden" name="action" value="closeA">
		<input type="submit" value=<%="确定"%>>
</form>

</center>
</body>
</html> 
