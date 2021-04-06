<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.confirm.bean.ConfirmationCode"%>
<%@page import="com.fy.confirm.service.server.DataManager"%>
<%@page import="com.fy.confirm.bean.GameActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String ids = request.getParameter("activityId");
	if (ids == null) {
		out.print("无效的活动");
		return ;
	}
	DataManager dataManager = DataManager.getInstance();
	GameActivity gameActivity = dataManager.getCodeStorer().getGameActivity(Long.valueOf(ids));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/confirmationcode.css"/>
<title>[<%=gameActivity.toSimpleString() %>]</title>
</head>
<body >
<table border="1">
<tr class="head">
	<td>激活码</td>
	<td>创建时间</td>
	<td>兑换时间</td>
	<td>兑换用户</td>
	<td>兑换游戏</td>
	<td>兑换游戏区</td>
	<td>兑换角色</td>
	<td>用户渠道</td>
</tr>
	<% 
	int total = gameActivity.getAllCodes().size();
	int used = 0;
	for (String codeStr :gameActivity.getAllCodes()) {
		ConfirmationCode code = dataManager.getCodeStorer().getConfirmationCode(codeStr);
		if (code != null) {
	 %>
	 <tr <%=code.isValid() ?  "" : "class='used'" %>>
	 	<td><%=codeStr %></td>
		<td><%=sdf.format(code.getCreateTime()) %></td>
		<% if (!code.isValid()) { used++; %>
			<td><%=sdf.format(code.getExchangeTime()) %></td>
			<td><%=code.getUserName() %></td>
			<td><%=code.getGameName() %></td>
			<td><%=code.getGameAreaId() %></td>
			<td><%=code.getPlayerName() %></td>
			<td><%=code.getChannel() %></td>
		<%} else {%>
			<td>--</td>
			<td>--</td>
			<td>--</td>
			<td>--</td>
			<td>--</td>
			<td>--</td>
		<%} %>
	</tr>
	 <%
			}
		}
	out.print("总数:" +total + ",已兑换" + used );
	 %>
</table>
</body>
</html>