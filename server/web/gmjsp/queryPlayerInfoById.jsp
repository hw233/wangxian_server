<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java"contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>
<%
	String pid = request.getParameter("playerId");
	if (pid == null || pid.isEmpty()) {
		out.print("请输入正确的角色id");
		return;
	}
	pid = URLDecoder.decode(pid, "UTF-8");

	Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(pid));
	if (p == null) {
		out.print("玩家id为:" + pid + "不存在");
		return;
	}
	String result = JsonUtil.jsonFromObject(p);
	out.print(result);
%>
