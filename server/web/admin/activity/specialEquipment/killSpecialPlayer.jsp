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
<html>
<head>
<title>杀死指定的玩家</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("name");
		String name2 = request.getParameter("name2");
		if(name == null || name.equals("") || name2 == null || name2.equals("")){
			
		}else{
			Player player = PlayerManager.getInstance().getPlayer(name);
			Player player2 = PlayerManager.getInstance().getPlayer(name2);
			player2.transientEnemyList.add(player);
			player.causeDamage(player2,player.getHp()+100,10,0);
			out.print("操作成功<br/>");
		}
	%>
	
	<form action="">
		俩人要在线才能看见<br/>
		被杀死人物名:<input type="text" name="name"/><br/>
		操作者:<input type="text" name="name2"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
