<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.horse.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.Soul"%><html>
  <head>
    <base href="<%=basePath%>">
    
    <title>查看本尊坐骑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <%
 	String playerIds = request.getParameter("playerId");
 	if(playerIds == null || playerIds.equals("")){
 		%>
 		<form action="">
 			玩家id:<input type="text" name="playerId"/>
 			<input type="submit" value="submit">
 		</form>
 		<%
 	}else{
 		Player player  = PlayerManager.getInstance().getPlayer(Long.parseLong(playerIds));
 		Soul ben  = player.getMainSoul();
 		ArrayList<Long> list = ben.getHorseArr();
 		if(list != null && list.size() > 0){
	 		for(long id : list){
	 			out.print(id +"<br/>");
	 		}
 		}else{
 			out.print("没有坐骑");
 		}
 	}
 	%>
  </body>
</html>
