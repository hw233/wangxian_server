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
	if ("true".equals(request.getParameter("ref"))) {
		TournamentManager.getInstance().刷新本周按积分排名的前100名人的数据_客户端();
	}
	List<TournamentRankDataClient> list = TournamentManager.getInstance().得到本周按积分排名的前100名人的数据_客户端();
	DecimalFormat df1 = new DecimalFormat("##.00%");
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
		<td>分数</td>
		<td>等级</td>
		<td>国家</td>
		<td>职业</td>
		<td>胜利 | 失败</td>
		<td>胜率</td>
		<td>奖励物品</td>
		<td>奖励物品个数</td>
		<td>奖励银子</td>
		<td>奖励银子倍数</td>
	</tr>
	<%
		int 名次 = 1;
		Map<Integer, Integer> careerLostMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> careerWinMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> careerNumMap = new HashMap<Integer, Integer>();
		int totalWin = 0;
		int totalLost = 0;
		int totalNum = 0;
		String[] articleNames = null;
		int 奖励物品数量 = 1;
		long 奖励银子 = 0;
		int 奖励银子倍数 = 1;
		for (TournamentRankDataClient td : list) {
			if (td == null) {
				continue;
			}
			totalNum++;
			totalWin += td.getWin();
			totalLost += td.getLost();
			if (!careerWinMap.containsKey(Integer.valueOf(td.getCareer()))) {
				careerWinMap.put(Integer.valueOf(td.getCareer()), 0);
			}
			careerWinMap.put(Integer.valueOf(td.getCareer()), careerWinMap.get(Integer.valueOf(td.getCareer())) + td.getWin());
			if (!careerLostMap.containsKey(Integer.valueOf(td.getCareer()))) {
				careerLostMap.put(Integer.valueOf(td.getCareer()), 0);
			}
			careerLostMap.put(Integer.valueOf(td.getCareer()), careerLostMap.get(Integer.valueOf(td.getCareer())) + td.getLost());

			if (!careerNumMap.containsKey(Integer.valueOf(td.getCareer()))) {
				careerNumMap.put(Integer.valueOf(td.getCareer()), 0);
			}
			careerNumMap.put(Integer.valueOf(td.getCareer()), careerNumMap.get(Integer.valueOf(td.getCareer())) + 1);
			Player player = PlayerManager.getInstance().getPlayer(td.id);
			int rank = 10000;
			boolean canPickReward = false;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).id == player.getId()) {
					rank = i;
					canPickReward = true;
					break;
				}
			}
			if (canPickReward) {
				OneTournamentData otd = tm.onePlayerTournamentDataMap.get(player.getId());
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
				//} else {
				//	out.print("[领取竞技奖励] [失败] [" + player.getLogString() + "] [已经领过奖励] [rank:" + rank + "]<br>");
				//}
			}
	%>
	<tr style="background-color: <%=名次 % 2 == 1 ? "#C2FCB4" : "#FCFDE6"%>">
		<td><%=名次++%></td>
		<td><%=td.getId()%></td>
		<td><%=td.getName()%></td>
		<td><%=td.getPoint()%></td>
		<td><%=td.getLevel()%></td>
		<td><%=CountryManager.得到国家名(td.getCountry()) + " | " + td.getCountry()%></td>
		<td><%=CareerManager.getInstance().careerNameByType(td.getCareer()) + " | " + td.getCareer()%></td>
		<td><%=td.getWin() + " | " + td.getLost()%></td>
		<td><%=df1.format(((double) td.getWin()) / (td.getWin() + td.getLost()))%></td>
		<td><%=Arrays.toString(articleNames)%></td>
		<td><%=奖励物品数量%></td>
		<td><%=BillingCenter.得到带单位的银两(奖励银子)%></td>
		<td><%=奖励银子倍数%></td>
	</tr>
	<%
		}
	%>
</table>
<HR>
<h2>前100名各个职业数据</h2>
<HR>
<table border="1" style="font-size: 12px; text-align: center;"
	width="70%">
	<tr style="color: red; font-weight: bold;">
		<td style="background-color: #A2D3F4">数据类型</td>
		<%
			for (int i = 1; i < CareerManager.careerNames.length; i++) {
		%>
		<td><%=CareerManager.careerNames[i]%></td>
		<%
			}
		%>
	</tr>

	<tr>
		<td style="background-color: #A2D3F4">人数分布(<%=totalNum%>)</td>
		<%
			for (int i = 1; i < CareerManager.careerNames.length; i++) {
		%>
		<td><%=careerNumMap.get(i) == null ? "00.00%" : df1.format(((double) careerNumMap.get(Integer.valueOf(i))) / totalNum)%></td>
		<%
			}
		%>
	</tr>

	<tr>
		<td style="background-color: #A2D3F4">胜利场数(<%=totalWin%>)</td>
		<%
			for (int i = 1; i < CareerManager.careerNames.length; i++) {
		%>
		<td><%=careerWinMap.get(i) == null ? "00.00%" : df1.format(((double) careerWinMap.get(Integer.valueOf(i))) / totalWin)%></td>
		<%
			}
		%>
	</tr>

	<tr>
		<td style="background-color: #A2D3F4">失败场数(<%=totalLost%>)</td>
		<%
			for (int i = 1; i < CareerManager.careerNames.length; i++) {
		%>
		<td><%=careerLostMap.get(i) == null ? "00.00%" : df1.format(((double) careerLostMap.get(Integer.valueOf(i))) / totalLost)%></td>
		<%
			}
		%>
	</tr>
	<tr>
		<td style="background-color: #A2D3F4">职业胜利比例</td>
		<%
			for (int i = 1; i < CareerManager.careerNames.length; i++) {
		%>
		<td><%=careerWinMap.get(Integer.valueOf(i)) == null ? "00.00%" : df1.format(((double) careerWinMap.get(Integer.valueOf(i))) / (careerWinMap.get(Integer.valueOf(i)) + careerLostMap.get(Integer.valueOf(i))))%></td>
		<%
			}
		%>
	</tr>
</table>
</body>
</html>