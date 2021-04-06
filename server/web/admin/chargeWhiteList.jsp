<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String op = request.getParameter("op");
	ChargeManager chargeManager = ChargeManager.getInstance();
	List<String> chargeWhiteList = chargeManager.getChargeWhiteList();
	if (op != null && !"".equals(op)) {//需要逻辑判断
		String uname = request.getParameter("uname");
		if (uname != null && !"".equals(uname)) {
			String notice = "";
			if ("add".equals(op)) {//添加
				chargeManager.addChargeWhiteList(uname);
				notice = "<h1>添加充值白名单成功[" + uname + "]</h1>";
			} else if ("del".equals(op)) {
				chargeManager.removeChargeWhiteList(uname);
				notice = "<h1>删除充值白名单成功[" + uname + "]</h1>";
			} else {
				notice = "<h1>无效的参数[op:" + op + "]</h1>";
			}
			out.print(notice);
			ChargeManager.logger.warn(notice);
		} else {
			out.print("<h1>提交的用户名无效[" + uname + "]</h1>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值白名单</title>
</head>
<body style="font-size: 12px">
	<form action="./chargeWhiteList.jsp?op=add" method="post">
	增加充值白名单用户名:<input type="text" name="uname" value="" size="50">
	<input type="submit" value="添加"><BR/>
	<hr/>
	已有白名单:<BR />
		<%
			if (chargeWhiteList != null) {
				for (String s : chargeWhiteList) {
		%>
		<input name="<%=s%>" disabled="disabled" value="<%=s%>" size="50"> <a
			href="./chargeWhiteList.jsp?op=del&uname=<%=s%>">删除</a><br/>
		<%
			}
			} else {
				out.print("暂无白名单");
			}
		%>
	</form>
</body>
</html>