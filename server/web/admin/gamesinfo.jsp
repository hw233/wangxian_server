<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.downcity.*"%><%! 
	
	String aaa(String s,int len){
		StringBuffer sb = new StringBuffer();
		char chars[] = s.toCharArray();
		int c = 0;
		for(int i = 0 ; i < chars.length ; i++){
			sb.append(chars[i]);
			c++;
			if( c >= len && (chars[i] == ',' || chars[i] == '{' || chars[i] == '}' || chars[i] == ':')){
				sb.append("<br/>");
				c = 0;
			}
		}
		return sb.toString();
	}
%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2></h2><br><br>
<%
GameManager gm = GameManager.getInstance();
if(gm != null){
Game games[] = gm.getGames();
%>
<table>
<tr class="titlecolor"><td>地图名</td>
<td>尺寸</td>
<td>数据大小</td>
<td>怪和NPC种类</td>
<td>坐骑限制</td>
<td>飞行限制</td>
<td>PK限制</td>
<td>决斗限制</td>
<td>最后一次心跳</td>
<td>传送点</td>
</tr>
<%
for(int i = 0 ; i < games.length ; i++){
Game game = games[i];
if(game != null){
	%>
	<tr>
	<td><a href="gameinfo.jsp?game=<%=game.getGameInfo().getName() %>"><%=game.getGameInfo().getName() %></a></td>
	<td></td>
	<td></td>
	<td><%= gm.getAllSpriteTypeOnGame(game.getGameInfo().getMapName()).length %></td>
	
	<td><%=game.getGameInfo().isLimitMOUNT()?"限制":"不限制" %></td>
	<td><%=game.getGameInfo().isLimitFLY()?"限制":"不限制" %></td>
	<td><%=game.getGameInfo().isLimitPVP()?"限制":"不限制" %></td>
	<td><%=game.getGameInfo().isLimitQIECUO()?"限制":"不限制" %></td>
	<td><%=(System.currentTimeMillis() - game.getLastHeartBeatTime())+"毫秒前" %></td>
	<%
	StringBuffer sb = new StringBuffer();
	sb.append("</br>");
	TransportData[] tds = game.getGameInfo().getTransports();
	for(int m=0 ;m < tds.length; m++){
		TransportData td = tds[m];
		sb.append("传送到地图 " + td.getTargetMapName() + " X坐标 " + td.getTargetMapX() + " Y坐标 " + td.getTargetMapY() + "</br>");
	}
	%>
	<td><%=sb.toString() %></td>
	</tr>
	<%
}
}
%>
</table>
<%
}else{
	out.println("instance error");
}
%>
</BODY></html>
