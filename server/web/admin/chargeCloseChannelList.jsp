<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String op = request.getParameter("op");
	ChargeManager chargeManager = ChargeManager.getInstance();
	List<String> closeChannelList = chargeManager.getCloseChannelList();
	if (op != null && !"".equals(op)) {//需要逻辑判断
		String channel = request.getParameter("channel");
		if (channel != null && !"".equals(channel)) {
			String notice = "";
			if ("add".equals(op)) {//添加
				chargeManager.addCloseChannelList(channel);
				notice = "<h1>添加充值屏蔽渠道成功[" + channel + "]</h1>";
			} else if ("del".equals(op)) {
				chargeManager.removeCloseChannelList(channel);
				notice = "<h1>删除充值屏蔽渠道成功[" + channel + "]</h1>";
			} else {
				notice = "<h1>无效的参数[op:" + op + "]</h1>";
			}
			out.print(notice);
			ChargeManager.logger.warn(notice);
		} else {
			out.print("<h1>提交的渠道无效[" + channel + "]</h1>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值屏蔽渠道</title>
</head>
<body style="font-size: 12px">
	<form action="./chargeCloseChannelList.jsp?op=add" method="post">
	增加充值屏蔽渠道:<input type="text" name="channel" value="" size="50">
	<input type="submit" value="添加"><BR/>
	<hr/>
	已有屏蔽渠道:<BR />
		<%
			if (closeChannelList != null) {
				for (String s : closeChannelList) {
		%>
		<input name="<%=s%>" disabled="disabled" value="<%=s%>" size="50"> <a
			href="./chargeCloseChannelList.jsp?op=del&channel=<%=s%>">删除</a><br/>
		<%
			}
			} else {
				out.print("暂无屏蔽渠道");
			}
		%>
	</form>
</body>
</html>