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
<title>删除指定的玩家背包</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("name");
		String entityIds = request.getParameter("entityId");
		if(name == null || name.equals("") || entityIds == null || entityIds.equals("")){
			
		}else{
			
			
			long petEnId = Long.parseLong(entityIds);
			Player player = PlayerManager.getInstance().getPlayer(name);
			Knapsack k = player.getKnapsack_common();
			
			Cell[] cells = k.getCells();
			for(Cell c:cells){
				long id = c.getEntityId();
				
				if(id == petEnId){
					c.entityId = -1;
					k.setModified(true);
					Knapsack.logger.error("[后台修改玩家背包] [删除指定道具]  ["+player.getLogString()+"] [物品:"+id+"]");
				}
			}
			
		}
	%>
	
	<form action="">
		人物名:<input type="text" name="name"/><br/>
		指定id:<input type="text" name="entityId"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
