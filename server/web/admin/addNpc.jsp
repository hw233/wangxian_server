<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	for (int i = 0; i < 3; i++) {
		Game game = GameManager.getInstance().getGameByName("piaomiaowangcheng", i + 1);
		GameManager gm = GameManager.getInstance();
		MemoryNPCManager nm = (MemoryNPCManager) gm.getNpcManager();
		NPC m = nm.createNPC(600184);//1537 1822
		if (m == null) {
			throw new Exception(game.gi.displayName + ",上的NPC不存在:" + 600184);
		}
		m.setX(1570);
		m.setY(1824);
		game.addSprite(m);
		try {
			out.print("[地图初始化] [放置NPC] [地图:" + game.gi.name + "] [国家:" + (i + 1) + "] [npc:" + 600184 + "/" + m.getName() + "]<BR/>");
			Game.logger.warn("[地图初始化] [放置NPC] [地图:" + game.gi.name + "] [国家:" + (i + 1) + "] [npc:" + 600184 + "/" + m.getName() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			Game.logger.warn("[地图初始化] [异常] [" + e + "]");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>