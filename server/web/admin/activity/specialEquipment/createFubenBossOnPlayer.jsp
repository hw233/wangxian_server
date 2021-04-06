<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>

<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.core.g2d.Point"%><html>
<head>
<title>生成副本怪物在玩家周围</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			
		}else{
			Player player = PlayerManager.getInstance().getPlayer(name);
			HashMap<Integer, List<String>> map = SpecialEquipmentManager.getInstance().downcityEquipmentMap;
			
			int size = map.size();
			Random r = new Random();
			int num = r.nextInt(size);
			int begin = 0;
			int index = 0;
			for( int i :map.keySet()){
				index = i;
				if(begin == num){
					break;
				}
			}
			List<String> list =  map.get(index);
			if(list == null){
				out.print("取怪物id错误<br/>");
				return;
			}
			Game game = player.getCurrentGame();
			
			Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(index);
			Point bornPoint = new Point(player.getX(), player.getY());
			monster.setBornPoint(bornPoint);
			monster.setX(player.getX());
			monster.setY(player.getY());
			
			game.addSprite(monster);
			out.print("产生怪物成功"+index+"<br/>");
			
		}
	%>
	
	<form action="">
		操作者:<input type="text" name="name"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
