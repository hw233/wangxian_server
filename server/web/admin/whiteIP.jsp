<%@page
	import="com.fy.engineserver.economic.GameServerSavingNotifyService"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String op = request.getParameter("op");
	List<String> whiteAddress = GameServerSavingNotifyService.whiteAddress;
	if (op != null && !"".equals(op)) {//需要逻辑判断
		String ip = request.getParameter("ip").trim();
		if (ip != null && !"".equals(ip)) {
			String notice = "";
			if ("add".equals(op)) {//添加
				whiteAddress.add(ip);
				notice = "<h1>添加充值IP白名单成功[" + ip + "]</h1>";
			} else if ("del".equals(op)) {
				whiteAddress.remove(ip);
				notice = "<h1>删除充值IP白名单成功[" + ip + "]</h1>";
			} else {
				notice = "<h1>无效的参数[op:" + op + "]</h1>";
			}
			out.print(notice);
			ChargeManager.logger.warn(notice);
		} else {
			out.print("<h1>提交的IP无效[" + ip + "]</h1>");
		}
	}
	whiteAddress = GameServerSavingNotifyService.whiteAddress;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值IP白名单</title>
</head>
<body style="font-size: 12px">
	<form action="./whiteIP.jsp?op=add" method="post">
		增加充值IP白名单:<input type="text" name="ip" value="" size="30"> <input type="submit" value="添加"><BR/>
		<hr />
		已有IP白名单:<BR />
		<%
			if (whiteAddress != null) {
				for (String s : whiteAddress) {
		%>
		<input name="<%=s%>" disabled="disabled" value="<%=s%>" size="30">
		<a href="./whiteIP.jsp?op=del&ip=<%=s%>">删除</a><br />
		<%
			}
			} else {
				out.print("暂无IP白名单");
			}
		%>
	</form>
</body>
</html>