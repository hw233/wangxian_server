<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>



<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.seal.data.Seal"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除地图中的所有boss</title>
</head>
<body>
	

	<%
	
		Seal seal = SealManager.getInstance().getSeal();
	
		if(seal != null){
			out.print(seal.getSealCanOpenTime()+"  剩余时间:"+(SystemTime.currentTimeMillis() - seal.getSealCanOpenTime()));
		}
		
		long mid = 10010258l;
		Game[] games = GameManager.getInstance().getGames();
		int i = 0;
		for(Game g : games){
			 LivingObject[] los = g.getLivingObjects();
			
			for(LivingObject lo : los){
				if(lo instanceof Monster){
					Monster m = (Monster)lo;
					if(m.getSpriteCategoryId() == mid){
						g.removeSprite(m);
						i++;
					}
				}
			}
		}
		SpecialEquipmentManager.getInstance().生成boss信息间隔  = 1l*2*30*24*60*60*1000;
		if(SpecialEquipmentManager.getInstance().getFlushBossInfo() != null){
			SpecialEquipmentManager.getInstance().setFlushBossInfo(null);
		}
		out.print("删除game中的怪物"+i);
	
	%>
	
	
	
</body>
</html>