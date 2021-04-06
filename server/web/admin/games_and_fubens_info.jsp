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
<h2>
<a href="games_and_fubens_info.jsp">刷新</a>
</h2><br><br>
<table>
<%
GameManager gm = GameManager.getInstance();
if(gm != null){
Game games[] = gm.getGames();
%>

<tr class="titlecolor"><td style="width:200px">地图</td><td>人数</td></tr>
<%
if(games != null){
for(int i = 0 ; i < games.length ; i++){
Game game = games[i];
if(game != null){
	%>
	<tr>
	<td><a href="gameinfo.jsp?game=<%=game.getGameInfo().getName() %>"><%=game.getGameInfo().getName() %></a></td>
	<td <%=game.getNumOfPlayer() != 0 ? "bgcolor='#C2CAF5'":"" %>><%=game.getNumOfPlayer() %></td>
	</tr>
	<%
}
}
}
}
%>
<tr class="titlecolor"><td>新手村副本地图</td><td>人数</td></tr>
<%
XinShouChunFuBenManager xscfb = XinShouChunFuBenManager.getInstance();
if(xscfb != null){
	Game games[][] = xscfb.getGames();
	for(int j = 0 ; games != null && j < games.length ; j++){
		for(int i = 0 ; games[j] != null && i < games[j].length ; i++){
			Game game = games[j][i];
			if(game != null){
			%>
			<tr>
			<td><a href="gameinfo.jsp?game=<%=game.getGameInfo().getName() %>"><%=game.getGameInfo().getName() %></a></td>
			<td <%=game.getNumOfPlayer() != 0 ? "bgcolor='#C2CAF5'":"" %>><%=game.getNumOfPlayer() %></td>
			</tr>
			<%
			}
		}
	}
}
%>
<tr class="titlecolor"><td>所有副本地图</td><td>人数</td></tr>
<%
DownCityManager dcm = DownCityManager.getInstance();
if(dcm != null){
DownCity[] dcs = dcm.getAllDownCity();
	ArrayList<DownCityInfo> dciList = dcm.getDciList();
	if(dciList != null){
		for(int i = 0; i < dciList.size(); i++){
			DownCityInfo dci = dciList.get(i);
			if(dci != null){
				ArrayList<DownCity> dcList = new ArrayList<DownCity>();
				if(dcs != null){
					for(DownCity dc : dcs){
						if(dc != null && dc.getGame() != null && dc.getGame().getGameInfo() != null && dc.getGame().getGameInfo().getName() != null && dc.getGame().getGameInfo().getName().equals(dci.getMapName())){
							dcList.add(dc);
						}
					}
				}
				if(dcList.size() != 0){
					for(int j = 0; j < dcList.size(); j++){
						DownCity dc = dcList.get(j);
						if(dc != null){
							%>
							<tr>
							<td><%=dc.getGame()!= null ? (dc.getGame().getGameInfo() != null ? dc.getGame().getGameInfo().getName() : "" ) : "" %></td>
							<td <%=dc.getGame()!= null ? (dc.getGame().getNumOfPlayer() != 0 ? "bgcolor='#C2CAF5'":""):""  %>><%=dc.getGame()!= null ? dc.getGame().getNumOfPlayer():"" %></td>
							</tr>
							<%
						}
					}
				}
			}
		}
	}
}
%>
</table>
</BODY></html>
