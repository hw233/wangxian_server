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
<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>杀死指定的怪物</title>
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
						Monster m = (Monster)lo;
						if(m.getSpriteCategoryId() == Integer.parseInt(monsterId)){
							bln = true;
							m.setOwner(player);
							m.setLevel(player.getLevel());
							// m.killed(SystemTime.currentTimeMillis(),(SystemTime.currentTimeMillis() - game.getLastHeartBeatTime()),game);
							m.setHp(-1);
							out.print("杀死");
							break;
						}
					}
				}
				if(!bln){
					out.print("没有指定的id"+monsterId);
				}
			}else{
				out.print("game null");
			}
			
			
			
		}
		
	
	%>
	
	
</body>
</html>