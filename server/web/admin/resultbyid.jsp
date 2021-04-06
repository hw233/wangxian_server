<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*,com.google.gson.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.google.gson.Gson"%><%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>玩家ID角色页面 </title>
</head>
<body>
<%
	Gson gson = new Gson();
String userName = request.getParameter("text");
PlayerManager mpm = PlayerManager.getInstance();
String playerName = request.getParameter("playerName");
String message = null;
if(playerName != null) {
	int career = Integer.parseInt(request.getParameter("career"));
	int politicalCamp = Integer.parseInt(request.getParameter("politicalCamp"));
	int sex = Integer.parseInt(request.getParameter("sex"));
	try {
		//String username,String name,byte race,byte sex,byte country,byte politicalCamp,int career
		mpm.createPlayer(userName,playerName,(byte)0,(byte)sex,(byte)politicalCamp,(byte)politicalCamp,career);
	} catch(Exception e) {
		e.printStackTrace();
		message = e.getMessage();
	}
}
Player players[] = mpm.getPlayerByUser(userName);
if(players == null || players.length == 0){
	out.println("该用户名下没有能运行的角色");
}else{
%>
<table cellspace='1' border='0' bgcolor='black'>
<tr bgcolor='#C2CAF5' align="center">
<td colspan="5">用户<%=userName %>在游戏中拥有的能运行的角色</td>
</tr>
<tr bgcolor='#C2CAF5'>
<td>角色ID</td><td>角色名</td><td>等级</td><td>性别</td><td>职业</td>
</tr>
<%for(Player player : players){ 
String career = "";
if(player.getCareer() == 0){
	career ="这是啥门派?";
}else if(player.getCareer() == 1){
	career ="修罗";
}else if(player.getCareer() == 2){
	career ="鬼煞";
}else if(player.getCareer() == 3){
	career ="灵尊";
}else if(player.getCareer() == 4){
	career ="巫皇";
}else if (player.getCareer() == 5) {
	career = "兽魁";
}
%>
<tr bgcolor='white'>
<td><a href="player/mod_player_commondata.jsp?action=select_playerId&playerid=<%=player.getId()%>"><%=player.getId() %></a></td>
<td><%=player.getName() %></td>
<td><%=player.getLevel() %></td>
<td><%=player.getSex()==0?"男":"女" %></td>
<td><a href="career.jsp"><%=career %></a></td>
</tr>
<%} }%>
</table>
<form action="resultbyid.jsp">
	角色名：<input type="text" size=15 name=playerName>
	职业:
	<select name="career">
	<option value="1">修罗
	<option value="2">鬼煞
	<option value="3">灵尊
	<option value="4">巫皇
	<option value="5">兽魁
	</select>
	性别:
	<select name="sex">
	<option value="0">男
	<option value="1">女
	</select>
	阵营:
	<select name="politicalCamp">
	<option value="1"><%=CountryManager.得到国家名(1) %>
	<option value="2"><%=CountryManager.得到国家名(2) %>
	<option value="3"><%=CountryManager.得到国家名(3) %>
	</select>
	<input type="hidden" name="text" value="<%=userName%>">
	<input type=submit name=sub1 value=创建新玩家>
</form>
<%=(message==null?"":message)%>
</body>
</html>
