<%@page import="java.util.Arrays"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLingChallenge"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLingChallengeThread"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLingBillBoardData"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLing"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLingChallengeManager"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLingManager"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
table{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
text-align:left;
}
</style>
</head>
<body>
	<form action="">
		<input type="hidden" name="opType" value="showBillBoard" /> 
		<!-- 活动id:<input type="text" name="activityId" /> (不填默认为查看当前服务器中显示的排行榜) -->
		<input type="submit" value="查看排行榜" />
	</form>
	<%
		XianLingManager xm = XianLingManager.instance;
		XianLingChallengeManager xcm = XianLingChallengeManager.instance;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String opType = request.getParameter("opType");
		if (opType != null && !"".equals(opType)) {
			if (opType.equals("showBillBoard")) {
				List<XianLingBillBoardData> billboardList = new ArrayList<XianLingBillBoardData>();
				List<XianLingBillBoardData> crossBillboardList = new ArrayList<XianLingBillBoardData>();
				String activityId = request.getParameter("activityId");
				if(activityId != null && !"".equals(activityId)){
					
				}else{
					xm.updateSelfServerCrossRank();
					billboardList = xm.map2OrderList(xm.billboardMap, xm.getActivitykey());
					crossBillboardList = xm.crossBillboardList;
				}
				%>
				
				
				<%if(!xm.isCrossServer()){ %>
					<table style="font-size: 12px" border="1">
					<tr><th colspan="7">本服排行榜 活动id:<%=xm.getActivitykey() %></th></tr>
					<tr><td>名次</td><td>跨服名次</td><td>用户名</td><td>id</td><td>角色名</td><td>分数</td><td>上次更新积分时间</td></tr>
					<%
						for(int i = 0; i<billboardList.size(); i++){
									XianLingBillBoardData data = billboardList.get(i);
					%>
						<tr><td><%=i+1 %></td><td><%=data.getCrossServerRank()+1 %></td><td><%=data.getUserName() %></td><td><%=data.getId() %></td><td><%=data.getPlayerName() %></td><td><%=data.getScore() %></td><td><%=sdf.format(data.getLastUpdateTime()) %></td></tr>
						<%
					}
					%>
					</table>
					<br>
					<table style="font-size: 12px;" border="1">
					<tr><th colspan="7">单服跨服排行榜</th></tr>
					<tr><td>跨服名次</td><td>服务器</td><td>用户名</td><td>id</td><td>角色名</td><td>分数</td><td>上次更新积分时间</td></tr>
					<%
					for(int i = 0; i<xm.csBillboardList.size(); i++){
						XianLingBillBoardData data = xm.csBillboardList.get(i);
						%>
						<tr><td><%=data.getCrossServerRank()+1 %></td><td><%=data.getServerName() %></td><td><%=data.getUserName() %></td><td><%=data.getId() %></td><td><%=data.getPlayerName() %></td><td><%=data.getScore() %></td><td><%=sdf.format(data.getLastUpdateTime()) %></td></tr>
						<%
					}
					%>
					</table>
				<%} %>
					<br>
					<%
						if(xm.isCrossServer()){
							//xm.refreshCrossBillBoard();
							xm.updateCrossServerRank();
							out.print("上次刷新时间："+sdf.format(xm.lastRefreshTime)+"<br>");
							%>
							<table style="font-size: 12px;" border="1">
								<tr><th colspan="7">跨服排行榜</th></tr>
								<tr><td>跨服名次</td><td>服务器</td><td>用户名</td><td>id</td><td>角色名</td><td>分数</td><td>上次更新积分时间</td></tr>
								<%
								for(int i = 0; i<crossBillboardList.size(); i++){
									XianLingBillBoardData data = crossBillboardList.get(i);
									%>
									<tr><td><%=data.getCrossServerRank()+1 %></td><td><%=data.getServerName() %></td><td><%=data.getUserName() %></td><td><%=data.getId() %></td><td><%=data.getPlayerName() %></td><td><%=data.getScore() %></td><td><%=sdf.format(data.getLastUpdateTime()) %></td></tr>
									<%
								}
								%>
								</table>
							<%
						}
					
			}
		}
	%>
	<hr>
	<form action="">
		<input type="hidden" name="opType" value="showThreads" /> 
		<input type="submit" value="查看线程" />
	</form>	
	<%
	if (opType != null && !"".equals(opType)) {
		if (opType.equals("showThreads")) {
			%>
			<table style="font-size: 12px;" border="1">
			<tr><td colspan="9">线程数量:<%=xcm.threads.size() %></td></tr>
			<tr><td rowspan="2">线程名</td><td rowspan="2">玩家数量</td><td colspan="7">副本数据</td></tr>
			<tr><td>挑战者</td><td>怪物</td><td>是否已刷怪</td><td>捕捉次数</td><td>挑战结果</td><td>所在game</td><td>进入前地图名</td></tr>
			<%
			XianLingChallengeThread[] threads = xcm.threads.toArray(new XianLingChallengeThread[xcm.threads.size()]);
				for(XianLingChallengeThread t:threads){
					int xcNum = t.getGameList().size();
					if(xcNum>0){
						if(t.getName().equals("仙灵挑战线程-2")){
							t.run();
						}
					%>
					<tr><td rowspan="<%=xcNum%>"><%=t.getName() %></td><td rowspan="<%=xcNum%>"><%=xcNum %></td>
					<%
						XianLingChallenge xc = t.getGameList().get(0);
						out.print(toString(xc));
						out.print("</tr>");
						if(xcNum>1){
							for(int i = 1; i < xcNum; i++){
								XianLingChallenge xc2 = t.getGameList().get(i);
								out.print("<tr>"+toString(xc2)+"</tr>");
							}
						}
					}else{
						out.print("<tr><td>"+t.getName()+"</td><td>"+xcNum+"</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>");
					}
				}
			%>
			</table>
			<%
		}
	}
	%>
	<%!
		String toString(XianLingChallenge xc){
			StringBuffer sbf = new StringBuffer();
			long playerId = xc.getPlayerId();
			try{
				Player player = PlayerManager.getInstance().getPlayer(playerId);
				if(player!=null){
					sbf.append("<td>").append(player.getUsername()).append(",").append(player.getName()).append(",").append(playerId).append("</td>");
				}
			}catch (Exception e){
					sbf.append("<td>").append(playerId).append("</td>");
			}
			Monster monster = MemoryMonsterManager.getMonsterManager().getMonster(xc.getTargetId());
			if(monster!=null){
				sbf.append("<td>").append("categoryId:").append(xc.getCategoryId()).append(",").append("targetId:").append(xc.getTargetId()).append(",").append("name:").append(monster.getName()).append("</td>");
			}else{
				sbf.append("<td>").append("categoryId:").append(xc.getCategoryId()).append(",").append("targetId:").append(xc.getTargetId()).append("</td>");
			}
			sbf.append("<td>").append(xc.isRefreshMonster()).append("</td>").append("<td>").append(xc.getCatchTimes()).append("</td>");
			if(xc.getResult()==(byte)0){
				sbf.append("<td>挑战中</td>");
			}else if(xc.getResult()==(byte)1){
				sbf.append("<td>成功</td>");
			}else if(xc.getResult()==(byte)-1){
				sbf.append("<td>怪物死亡</td>");
			}else if(xc.getResult()==(byte)-2){
				sbf.append("<td>玩家死亡</td>");
			}else{
				sbf.append("<td>失败</td>");
			}
			sbf.append("<td>").append(xc.getGame().gi.name).append("</td>").append("<td>").append(xc.getPreMapName()).append("</td>");
			return sbf.toString();
	}
	%>
	
	<form action="">
		<input type="hidden" name="opType" value="clearDead" /> 
		<input type="submit" value="移除卡死玩家" />
	</form>
	<%
	if (opType != null && !"".equals(opType)) {
		if (opType.equals("clearDead")) {
			List<XianLingChallenge> removeList = new ArrayList<XianLingChallenge>();
			List<Long> removePId = new ArrayList<Long>();
			XianLingChallengeThread[] threads = xcm.threads.toArray(new XianLingChallengeThread[xcm.threads.size()]);
			for (XianLingChallengeThread t : threads) {
				out.print(t.getName() + "<br>");

				Map<Long, Long> timeMaps = t.timeMaps;

				out.print("timeMaps.size:" + timeMaps.size() + "<br>");
				for (Long id : timeMaps.keySet()) {
					//out.print("id:" + id + ",time:" + sdf.format(timeMaps.get(id)) + "<br>");
					if (System.currentTimeMillis() - timeMaps.get(id) > 6 * 60 * 1000) {
						removePId.add(id);
					}
				}
				
				List<XianLingChallenge> gameList = t.getGameList();
				if (gameList.size() > 0) {
					for (XianLingChallenge xc : gameList) {
						out.print(xc.getPlayerId() + "<br>");
						try {
							if (removePId.contains(xc.getPlayerId())) {
								removeList.add(xc);
								timeMaps.remove(xc.getPlayerId());
								Player p = PlayerManager.getInstance().getPlayer(xc.getPlayerId());
								p.xl_chanllengeFlag = 0;
							}
						} catch (Exception e) {
							XianLingManager.logger.error("[仙灵] [XianLingChallengeManager] [异常]", e);
							e.printStackTrace();
						}
					}
					//gameList.remove(remove);
					if (removeList.size() > 0) {
						gameList.removeAll(removeList);
						out.print("后台把玩家移出线程：" + Arrays.toString(removeList.toArray()) + "<br>");
						if (XianLingManager.instance.logger.isWarnEnabled()) {
							XianLingManager.instance.logger.warn("[后台把玩家移出线程] [" + Arrays.toString(removeList.toArray()) + "]");
						}
						
					}
				}
			}
		}
	}
	%>
</body>
</html>