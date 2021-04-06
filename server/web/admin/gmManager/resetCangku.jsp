<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	String newPWD = request.getParameter("newPWD");
	String pwd = request.getParameter("pwd");
	String action = request.getParameter("action");
	if ("op".equals(action)) {
		if("chongzhicang^&!ku".equals(pwd)) {
			Player player = null;
			try {
				player = GamePlayerManager.getInstance().getPlayer(playerName);
			} catch (Exception e){
				out.print("角色不存在");
				return;
			}
			String oldPwd = player.getCangkuPassword();
			player.setCangkuPassword(newPWD);
			String newPwd = player.getCangkuPassword();
			out.print(player.getLogString() + "新密码设置成功");
			StringBuffer content = new StringBuffer();
			content.append("<table style='font-size: 12px;text-align: center;' border='1'>");
				content.append("<tr style='background-color: black;color: white;font-size: 14px;font-weight: bold;' >");
					content.append("<td>");
						content.append("服务器");
					content.append("</td>");
					content.append("<td>");
						content.append("操作者");
					content.append("</td>");
					content.append("<td>");
						content.append("操作IP");
					content.append("</td>");
					content.append("<td>");
						content.append("角色");
					content.append("</td>");
					content.append("<td>");
						content.append("旧仓库密码");
					content.append("</td>");
					content.append("<td>");
						content.append("新仓库密码");
					content.append("</td>");
				content.append("</tr>");
				content.append("<tr>");
					content.append("<td>");
						content.append(GameConstants.getInstance().getServerName() );
					content.append("</td>");
					content.append("<td>");
						content.append("<B>" + session.getAttribute("authorize.username") + "</B>");
					content.append("</td>");
					content.append("<td>");
						content.append(request.getRemoteAddr());
					content.append("</td>");
					content.append("<td>");
						content.append(player.getLogString());
					content.append("</td>");
					content.append("<td>");
						content.append(oldPwd);
					content.append("</td>");
					content.append("<td>");
						content.append(newPWD);
					content.append("</td>");
				content.append("</tr>");
			content.append("</table>");
			sendMail("[高级GM工具] [修改用户仓库密码] [" + session.getAttribute("authorize.username") + "] [" + GameConstants.getInstance().getServerName() + "]", content.toString());
		}
	} else {
		playerName = "";
		newPWD = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重置仓库密码</title>
</head>
<body>

	<form action="./resetCangku.jsp" method="post">
		角色名称:<input name="playerName" type="text" value="<%=playerName%>"><BR/>
		仓库密码:<input name="newPWD" type="password" value="<%=newPWD%>"><BR/>
		提交密码:<input name="pwd" type="password"><BR/>
		<input type="hidden" value="op" name="action">
		<input type="submit" value="设置密码">
	</form>
	<table style="background-color: black;color: white;font-size: 14px;font-weight: bold;text-align: center;" border="1"></table>
</body>
</html>