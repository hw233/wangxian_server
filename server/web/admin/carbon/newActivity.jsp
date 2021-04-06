<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.activity.levelUpReward.model.LevelUpRewardModel"%>
<%@page import="com.fy.engineserver.activity.levelUpReward.LevelUpRewardManager"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.activity.dailyTurnActivity.model.DiskFileModel"%>
<%@page import="com.fy.engineserver.activity.dailyTurnActivity.model.PlayerRewardInfo"%>
<%@page import="com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String carbonLevel = request.getParameter("carbonLevel");
		String cateid = request.getParameter("cateid");
		action = action == null ? "" : action;//1
		playerId = playerId == null ? "" :playerId;
		cateid = cateid == null ? "" :cateid;
		carbonLevel = carbonLevel== null ? "" : carbonLevel;
		
		
	%>
	<form action="newActivity.jsp" method="post">
		<input type="hidden" name="action" value="cleanDailyTimes" />角色 名  :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="清空每日转盘次数" />
	</form>
	<br />
	<form action="newActivity.jsp" method="post">
		<input type="hidden" name="action" value="changeDailyChongzhi" />角色 名  :
		<input type="text" name="playerId" value="<%=playerId%>" />金额:
		<input type="text" name="carbonLevel" value="<%=carbonLevel%>" />
		<input type="submit" value="设置每日充值金额记录" />
	</form>
	<br />
	<form action="newActivity.jsp" method="post">
		<input type="hidden" name="action" value="buyLevelUpReward" />角色 名  :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="模拟购买冲级返利" />
	</form>
	<br />
	
	<%
	if("cleanDailyTimes".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		long now = System.currentTimeMillis();
		DiskFileModel model = DailyTurnManager.instance.getDiskFileModel(player, now);
		for (PlayerRewardInfo pri : model.getRewardInfo().values()) {
			pri.reset();
		}
		DailyTurnManager.instance.disk.put(player.getId(), model);
		out.println("ok!");
	} else if ("changeDailyChongzhi".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		int aa = Integer.parseInt(carbonLevel);
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		player.setOne_day_rmb(aa * 100);
		out.println("当日充值金额修改为：" + player.getOne_day_rmb() + "分<br>");
	} else if ("buyLevelUpReward".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		LevelUpRewardModel model2 = LevelUpRewardManager.instance.getModelByPlayer(player);
		String result = LevelUpRewardManager.instance.buyLevelUpReward(player, model2.getType()+"");
		out.println("购买结果:" + result + "<br>");
	}
			
	%>
</body>
</html>
