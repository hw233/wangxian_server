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
<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>判断是否是指定boss</title>
</head>
<body>
	
	<%
	
		String monsterId = request.getParameter("monsterId");
		String playerName = request.getParameter("playerName");
	
		if(monsterId == null || playerName == null || monsterId.equals("") ||playerName.equals("")){
			%>
			
	<form action="">
		monsterId:<input type="text" name="monsterId"/><br/>
		playerName:<input type="text" name="playerName"/><br/>
		<input type="submit" value="submit"/>
	</form>
			
			<%
		}else{
			
			
			Player player = PlayerManager.getInstance().getPlayer(playerName);
			Game game = player.getCurrentGame();
			if(game != null){
				
				LivingObject[] los = game.getLivingObjects();
				boolean bln = false;
				for(LivingObject lo : los){
					if(lo instanceof Monster){
						Monster monster = (Monster)lo;
						if(monster.getSpriteCategoryId() == Integer.parseInt(monsterId)){
							SpecialEquipmentManager se = SpecialEquipmentManager.getInstance();

							boolean isSpecialMonster = false;
							int categoryId = monster.getSpriteCategoryId();
							out.print(categoryId+"<br/>");
							for(Map.Entry<String, Integer> en : se.equipmentMonsterIdMap.entrySet()){
								if(en.getValue() == categoryId){
									isSpecialMonster =true;
									out.print(categoryId+"一样"+"<br/>");
									break;
								}else{
									out.print(en.getValue()+"不一样"+"<br/>");
								}
							}
							
							List<String> list = se.downcityEquipmentMap.get(categoryId);
							if(list != null && list.size() > 0){
								isSpecialMonster =true;
								out.print(categoryId+"副本一样"+"<br/>");
							}
							break;
						}
					}
				}
			}else{
				out.print("game null");
			}
		}
		
	
	%>
	
	
	
</body>
</html>