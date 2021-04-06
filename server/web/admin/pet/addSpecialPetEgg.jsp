<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%><html>
<head>
<title>查询所有的宠物配置表</title>
</head>
<body>
	
	<%
	
		String playerName = request.getParameter("playerName");
		String egg = request.getParameter("eggs");

		if(playerName == null || playerName.equals("") || egg == null || egg.equals("") ){
			
		}else{
			Player p =  PlayerManager.getInstance().getPlayer(playerName);
			Article egga = ArticleManager.getInstance().getArticle(egg);
			if(egga instanceof PetEggProps){
				ArticleEntity ae =  ArticleEntityManager.getInstance().createEntity(egga,false,0,null,0,1,true);
				p.putToKnapsacks(ae,"后台");
			}else{
				out.print("不是指定物品"+egg);
			}
			
		}
	%>
	
	<form action="">
		
		playerName:<input type="text" name="playerName"/><br/>
		蛋:<input type="text" name="eggs"/><br/>

		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
