<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="java.text.DecimalFormat"%>
<%@page
	import="com.fy.engineserver.tournament.data.TournamentRankDataClient"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	TournamentManager tm = TournamentManager.getInstance();
	List<OneTournamentData> list = tm.得到本周按积分排名的前1000名人的数据();
%>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.tournament.data.TournamentRewardDataClient"%>
<%@page
	import="com.fy.engineserver.tournament.data.OneTournamentData"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page
	import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>比武数据查看</title>
</head>
<body>
<HR>
<H2><%=GameConstants.getInstance().getServerName()%></H2>
<HR>
<table border="1" style="font-size: 12px; text-align: center;"
	width="70%">
	<tr style="font-weight: bold; color: white; background-color: black;">
		<td>名次</td>
		<td>角色ID</td>
		<td>名字</td>
		<td>等级</td>
		<td>奖励物品</td>
		<td>奖励物品个数</td>
		<td>奖励银子</td>
		<td>奖励银子倍数</td>
	</tr>
	<%
		int 名次 = 1;
		String[] articleNames = null;
		int 奖励物品数量 = 1;
		long 奖励银子 = 0;
		int 奖励银子倍数 = 1;
		int rank = 10000;
		for (int i = 0; i < list.size(); i++) {
			OneTournamentData otd = list.get(i);
			Player player = PlayerManager.getInstance().getPlayer(otd.getPlayerId());
			rank = i;
			if (otd == null) {
				otd = tm.得到玩家竞技数据(player.getId());
			}

			if (otd != null && !otd.pickReward) {
			} else {
				out.print("[领取竞技奖励] [失败] [" + player.getLogString() + "] [已经领过奖励] [rank:" + rank + "]<br>");
			}
			int index = tm.得到奖励index(rank);
			articleNames = tm.比武奖励物品[index];
			try {
				// 合服打折活动
				CompoundReturn cr = ActivityManagers.getInstance().getValue(3, player);
				if (cr != null && cr.getBooleanValue()) {
					奖励物品数量 = cr.getIntValue();
				}
			} catch (Exception ex) {
				out.print("[领取竞技奖励] [异常] [" + player.getLogString() + "] [rank:" + rank + "]" + ex);
			}
			long rewardYinzi = 0;
			if (tm.rewardYinzis.length > rank) {
				rewardYinzi = tm.rewardYinzis[rank];
			} else {
				rewardYinzi = tm.rewardYinzis[tm.rewardYinzis.length - 1];
			}
			try {
				// 合服打折活动
				CompoundReturn cr = ActivityManagers.getInstance().getValue(3, player);
				if (cr != null && cr.getBooleanValue()) {
					奖励银子倍数 = cr.getIntValue();
				}
				奖励银子 = rewardYinzi * 奖励银子倍数;
			} catch (Exception ex) {
				out.print("[领取竞技奖励] [异常] [" + player.getLogString() + "] [rewardYinzi:" + rewardYinzi + "] [rank:" + rank + "]" + ex);
			}
	%>
	<tr style="background-color: <%=名次 % 2 == 1 ? "#C2FCB4" : "#FCFDE6"%>">
		<td><%=名次++%></td>
		<td><%=player.getId()%></td>
		<td><%=player.getName()%></td>
		<td><%=player.getLevel()%></td>
		<td><%=Arrays.toString(articleNames)%></td>
		<td><%=奖励物品数量%></td>
		<td><%=BillingCenter.得到带单位的银两(奖励银子)%></td>
		<td><%=奖励银子倍数%></td>
	</tr>
	<%
		}
	%>
</table>
</body>
</html>