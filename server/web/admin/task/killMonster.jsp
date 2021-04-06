<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<%
	String player = request.getParameter("player");
	String monster = request.getParameter("monster");
	String num = request.getParameter("num");
	if (player == null || monster == null || num == null) {
		player = "";
		monster = "";
		num = "1";
	} else {
		Player p = PlayerManager.getInstance().getPlayer(player);
		Monster m = new Monster();
		m.setName(monster);
		m.setLevel(p.getSoulLevel());
		if (p != null) {
			for (int i = 0; i < Integer.valueOf(num); i++) {
				p.killOneSprite(m);
			}
			out.print("杀死了怪物:" + monster + "," + num + "个.<BR/>");
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../task/css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./killMonster.jsp" method="post">
		<table>
			<tr class="head">
				<td>角色名</td>
				<td>要杀的怪</td>
				<td>杀怪数量</td>
				<td>写完了</td>
			</tr>
			<tr>
				<td><input name="player" type="text" value="<%=player%>">
				</td>
				<td><input name="monster" type="text" value="<%=monster%>">
				</td>
				<td><input name="num" type="text" value="<%=num%>">
				</td>
				<td><input name="OK" type="submit">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>