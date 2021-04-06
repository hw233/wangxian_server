<%@page
	import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	long npcid = Long.valueOf(request.getParameter("npcid"));
	long sid = Long.valueOf(request.getParameter("sid"));
	int times = Integer.valueOf(request.getParameter("times"));
	SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(sid);
	if (septStation == null) {
		out.print("驻地不存在");
		return;
	}
	Jiazu jiazu = septStation.getJiazu();
	if (jiazu == null) {
		out.print("家族不存在");
		return;
	}

	Player player = GamePlayerManager.getInstance().getPlayer(JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master).get(0).getPlayerID());
	if (player == null) {
		out.print("族长不存在");
		return;
	}
	SeptBuildingEntity sbe = septStation.getBuildings().get(npcid);
	if (sbe == null) {
		out.print("建筑不存在");
		return;
	}
	for (int i = 0; i < times; i++) {
		sbe.onDeliverTask(player);
	}
	out.print("搞了" + times + "次");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>