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
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><html>
<head>
<title>测试宠物id跟背包id是否一样</title>
</head>
<body>
	
	<%
	
		String petIds = request.getParameter("petIds");
		String values = request.getParameter("values");
		if(petIds == null || petIds.equals("") ||values == null || values.equals("")){
			
		}else{
			
			Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
			out.print(ps.length+"<br/>");
			
			
			
		}
	%>
	
	<form action="">
		
		测试:<input type="text" name="petIds"/><br/>
		多少个人:<input type="text" name="values"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
