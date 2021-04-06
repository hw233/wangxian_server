<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<head>
<link rel="stylesheet" href="../css/style.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<%
	int matchSuccNum = 0;
	Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
	List<MatchData> matchData = CrossServerManager.getInstance().getMatchData();
%>
<form>
<table>
	<tr><th>匹配的人</th><th>匹配的人等级</th><th>挑战者</th><th>挑战者等级</th></tr>
	<%
		for(MatchData d : matchData){
			if(d == null || d.getPlayerA()==null || d.getPlayerB()==null){
				continue;
			}
			matchSuccNum++;
	%>		
		<tr><td><%=d.playerNameA %></td><td><%=d.getPlayerA()!=null?d.getPlayerA().getLevel():"nul" %></td><td><%=d.playerNameB %></td><td><%=d.getPlayerB()!=null?d.getPlayerB().getLevel():"nul" %></td></tr>
	<%		
		}
	%>
	
</table>
<table>
	<tr><th>总在线人数</th><th>匹配到的人数</th></tr>
	<tr><td><%=ps.length %></td><td><%=matchSuccNum*2 %></td></tr>
</table>
</form>

</body>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.across.battle.MatchData"%>
<%@page import="com.fy.engineserver.activity.across.battle.CrossServerManager"%></html>
