<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String sId = request.getParameter("id");
	long id = -1;
	if (sId != null && !sId.isEmpty()) {
		id = Long.valueOf(sId);
	}
	if (id == -1) {
		out.print("ID错误");
		return;
	}

	Player player = PlayerManager.getInstance().getPlayer(id);
	if (player == null) {
		out.print("角色不存在 !");
		return;
	}
	Soul soul = player.getCurrSoul();
	out.print("角色[" + player.getName() + "]当前元神类型:" + soul.getSoulType() + ";等级[" + soul.getGrade() + "]<BR/>");

	if (player.getUnusedSoul() != null) {
		for (Soul s : player.getUnusedSoul()) {
			out.print("角色[" + player.getName() + "]其他元神类型:" + s.getSoulType() + ";等级[" + s.getGrade() + "]<BR/>");
		}

	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>