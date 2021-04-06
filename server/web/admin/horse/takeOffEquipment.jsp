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
    
    <title>脱坐骑装备</title>
    
  </head>
  
  <body>
  	<a href="<%= path %>/admin/horse/queryHorse.jsp">返回</a>
  
  
  				<form name="takeOffEquipment" action="takeOffEquipment.jsp"  method="get">
					<input type="hidden" id="takeOffEquipmentId" name="takeOffEquipmentId" />
					<input type="hidden" id="takeOffEquipmentHorseId" name="takeOffEquipmentHorseId" />
				</form>
  
  	<%
	long equipmentId = Long.parseLong(request.getParameter("takeOffEquipmentId"));
	long horseId = Long.parseLong(request.getParameter("takeOffEquipmentHorseId"));
  	PlayerManager pm = PlayerManager.getInstance();
  	Object obj = session.getAttribute("playerid");
	long playerId = Long.parseLong(obj.toString());
	Player player = pm.getPlayer(playerId);
	Horse horse = HorseManager.getInstance().getHorseById(horseId,player);
	if(horse != null){
	  	ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(equipmentId);
	  	if(entity != null){
	  	  	EquipmentEntity ee = null;
	  	  	if(entity instanceof EquipmentEntity){
	  	  		ee = (EquipmentEntity)entity;
	  	  	}
	  	  	int i = 0;
	  	  	boolean bln = false;
			for(long id :horse.getEquipmentIds()){
				if(id == equipmentId){
					if(entity != null){
						bln = true;
						horse.takeOff(i);
						break;
					}
				}
				i++;
			}
			if(!bln){
				out.print("坐骑没有这件装备");
			}
	  	  	
	  	}else{
	  		out.print("装备不存在");
	  	}
	}else{
		out.print("坐骑不存在");
	}

  	%>
  
  </body>
</html>
