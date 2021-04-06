<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询角色信息</title>
</head>
<body>
根据playerName查询对应的playerId,userName
<hr>
<form action="">请输入角色名,每行一个<br>
<textarea rows="10" cols="15" name="playerNames"></textarea><br>
<input type="submit" name="提交"></input></form>
<%
	String playerNames = request.getParameter("playerNames");

	if (playerNames != null && !"".equals(playerNames)) {
		String[] nameArr = playerNames.split("\r\n");
		StringBuffer sbf = new StringBuffer();
		for (String name : nameArr) {
			try {
				Player p = GamePlayerManager.getInstance().getPlayer(name);
				if (p != null) {
					out.print(name + "," + p.getId() + "<br>");
				}
			} catch (Exception e) {
				sbf.append("[" + name + "]");
			}
		}
		if (!"".equals(sbf.toString())) {
			out.print("<hr>");
			out.print("未查到角色信息的玩家:" + sbf.toString() + "<br>");
		}
	}
%>
</body>
</html>