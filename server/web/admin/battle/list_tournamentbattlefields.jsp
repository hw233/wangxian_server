<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.battlefield.*,
com.fy.engineserver.battlefield.concrete.* " %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.WaittingItem"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
<title>战场</title>
<% 

	TournamentManager tm = TournamentManager.getInstance();
String running = request.getParameter("action");
if(running != null){
	tm.running = Boolean.getBoolean(running);
}
	PlayerManager pm = PlayerManager.getInstance();
	final Hashtable<Long,Integer> currentTournamentPointMap = tm.td.currentTournamentPointMap;

	Hashtable<Long,Integer> lastWeekTournamentPointMap = tm.td.lastWeekTournamentPointMap;

	ArrayList<Long> idList = tm.td.getCurrentSignUpList();
	ArrayList<Long> 积分不为零的玩家ids = new ArrayList<Long>();
	if(!currentTournamentPointMap.isEmpty()){
		for(Long l : currentTournamentPointMap.keySet()){
			if(currentTournamentPointMap.get(l) != null && currentTournamentPointMap.get(l) > 0){
				积分不为零的玩家ids.add(l);
			}
		}
	}
	Long[] ids = 积分不为零的玩家ids.toArray(new Long[0]);
	Arrays.sort(ids, new Comparator<Long>() {
		PlayerManager pm = PlayerManager.getInstance();
		@Override
		public int compare(Long o1, Long o2) {
			try{
				if(currentTournamentPointMap.get(o1).intValue() != currentTournamentPointMap.get(o2).intValue()){
					return currentTournamentPointMap.get(o2).intValue() - currentTournamentPointMap.get(o1).intValue();
				}
				Player p1 = pm.getPlayer(o1);
				Player p2 = pm.getPlayer(o2);
				if(p1 != null && p2 != null){
					if(p1.getLevel() != p2.getLevel()){
						return p2.getLevel() - p1.getLevel();
					}
					if(p1.getExp() != p2.getExp()){
						return (int)((p2.getExp() - p1.getExp())/1000);//避免出现越界
					}
				}
			}catch(Exception ex){
				
			}
			
			return 0;
		}

	});
	
	
%>
</head>
<body>
<h2>积分情况</h2>
<a href="./list_tournamentbattlefields.jsp">刷新此页面</a>
<br>
<br>
<table>
<tr bgcolor="#C2CAF5" align="center">
<td>名次</td>
<td>id</td>
<td>玩家名</td>
<td>积分</td>
</tr>

<%
for(int i = 0; i < ids.length; i++){
%>
	<tr align="center">
<td><%=(i+1) %></td>
<td><%=ids[i] %></td>
<td><%
String p = "此人已经删号";
try{
	p = pm.getPlayer(ids[i]).getName();
}catch(Exception ex){
	
}
out.println(p);
%></td>
<td><%=currentTournamentPointMap.get(ids[i]) %></td>
</tr>
	<%
}
%>
</table>
<table>
<tr bgcolor="#C2CAF5" align="center">
<td>报名人id</td>
<td>玩家名</td>
</tr>

<%
for(int i = 0; i < idList.size(); i++){
%>
	<tr align="center">
<td><%=idList.get(i) %></td>
<td><%
String p = "此人已经删号";
try{
	p = pm.getPlayer(idList.get(i)).getName();
}catch(Exception ex){
	
}
out.println(p);
%></td>
</tr>
<%
}
out.println("开启状况:"+tm.running);
%>
</table>

<form name="f1">
<table>
<tr>
<td><input type="text" name="flag">
<input type="submit" value="提交"></td>
</tr>
</table>
</form>
</body>
</html>
