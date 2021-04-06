<%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	int type = Integer.valueOf(request.getParameter("type"));
	Player player = null;
	try {
		player = GamePlayerManager.getInstance().getPlayer(playerId);
	} catch (Exception e) {
		out.print("error:[角色不存在]" + e.getMessage());
		return;
	}
	try {
	switch (type) {
	case 1://清除家族ID
		player.setJiazuId(0L);
		player.initJiazuTitleAndIcon();
		break;
	case 2://清除宗派名字
		player.setZongPaiName("");
		player.initZongPaiName();
		break;
	}
	} catch (Exception e) {
		out.print("error:[设置失败] [type:" + type + "]" + e.getMessage());
		return;
	}
	out.print("success");
%>