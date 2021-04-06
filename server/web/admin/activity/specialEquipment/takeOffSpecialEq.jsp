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
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>脱下指定装备</title>
</head>
<body>
	
	<%
	
		String monsterId = request.getParameter("monsterId");
		String playerName = request.getParameter("playerName");
	
		if(monsterId == null || playerName == null || monsterId.equals("") ||playerName.equals("")){
			%>
			
	<form action="">
		装备id:<input type="text" name="monsterId"/><br/>
		playerName:<input type="text" name="playerName"/><br/>
		<input type="submit" value="submit"/>
	</form>
			
			<%
		}else{
			
			
			Player player = PlayerManager.getInstance().getPlayer(playerName);
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.parseLong(monsterId));
			if(ae != null && ae instanceof EquipmentEntity){
				
				EquipmentEntity se1 = (EquipmentEntity)ae;
				
				Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if(a != null && a instanceof Equipment){
					Equipment eq = (Equipment)a;
					byte equipmentType = -1;
					if(eq instanceof Weapon){
						equipmentType = 0;
					}else{
						equipmentType = (byte)eq.getEquipmentType();
					}
					boolean escape = false;
					for(Soul soul :player.getSouls()){
						EquipmentEntity ee = soul.getEc().takeOff(equipmentType, soul.getSoulType());
						if(ee != null){
							escape = true;
							player.putToKnapsacks(ee,"后台脱装备");
							out.print("托成功<br/>");
							break;
						}
					}
					if(!escape){
						out.print("没成功<br/>");
					}
				}
				
			}else{
				out.print("输入id有问题"+(ae != null?ae.getClass():"null"));
			}
			
			
		}
		
	
	%>
	
	
	
</body>
</html>