<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
<title>test</title>
</head>
<body>

<%
	String sql = "currentLevel=?";
	Object[] objs = new Object[]{9};
	long count = TransitRobberyEntityManager.em.count(TransitRobberyEntity.class, "currentLevel=9");
	int count_220Lv = 0;
	if (count > 0) {	
		List<TransitRobberyEntity> list = TransitRobberyEntityManager.em.query(TransitRobberyEntity.class, sql, objs, "", 1, count+1);
		for (int i=0; i<list.size(); i++) {
			Player p = ((GamePlayerManager)GamePlayerManager.getInstance()).em.find(list.get(i).getId());
			if (p != null && p.getLevel() == 220) {
				count_220Lv += 1;
			}
		}
	}
	String serverName = GameConstants.getInstance().getServerName();
	out.println("[服务器:" + serverName + "] [渡过九劫人数:" + count + "] [其中220玩家数:" + count_220Lv + "]<br>");
	
%>

 