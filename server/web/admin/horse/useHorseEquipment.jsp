<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%><html>
  <head>
    <base href="<%=basePath%>">
    
    <title>使用坐骑的装备</title>
    
  </head>
  
  <body>
  	<a href="<%= path %>/admin/horse/queryHorse.jsp">返回</a>
  
  	<%
		long equipmentId = Long.parseLong(request.getParameter("horseEquipmentId"));
		long horseId = Long.parseLong(request.getParameter("horseId"));
	  	PlayerManager pm = PlayerManager.getInstance();
	  	Object obj = session.getAttribute("playerid");
		long playerId = Long.parseLong(obj.toString());
		Player player = pm.getPlayer(playerId);
		ArticleManager am = ArticleManager.getInstance();
	  	ArticleEntity entity = player.getArticleEntity(equipmentId);
	  	EquipmentEntity ee = null;
	  	if(entity instanceof EquipmentEntity){
	  		ee = (EquipmentEntity)entity;
	  	}
		Article a = am.getArticle(ee.getArticleName());
		Horse horse = HorseManager.getInstance().getHorseById(horseId,player);
		horse.putOn((EquipmentEntity)entity,false);
  	%>
  
  </body>
</html>
