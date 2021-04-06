<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.sprite.concrete.*"%>



<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.Player"%><html>
  <head>
    <base href="<%=basePath%>">
    
    <title>上马，下马</title>

	<%
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		HorseManager hm = HorseManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		
		
	%>
  </head>
  
  <body>
  
  	<%
  		try{
  		
	  		int upordown = Integer.parseInt(request.getParameter("upOrdown").trim());
	  		long horseId = Long.parseLong(request.getParameter("upordownHorseId").trim());
	  		
	  		long playerId = Long.parseLong(session.getAttribute("playerid").toString().trim());
			Player player  = pm.getPlayer(playerId);
	  		
	  		Horse horse =  hm.getHorseById(horseId,player);
	  		if(upordown == 1){
	  			horse.upHorse(player);
	  		}else if(upordown == 0){
	  			horse.downHorse(player);
	  		}
  	%>
  		<h2>成功</h2>
  		
  	<%
  		}catch(Exception e){
  			e.printStackTrace();
  		}
  	%>
  
 	<a href="<%=path %>/admin/horse/queryHorse.jsp">返回</a>
  
  </body>
</html>
