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
<%@page import="com.fy.boss.authorize.model.Passport"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>取得一个人的passport</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			
		}else{
			
			Player p = PlayerManager.getInstance().getPlayer(name);
			Passport pp = p.getPassport();
			if(pp != null){
				out.print(pp.getRegisterChannel());
			}else{
				out.print("没有渠道号");
			}
			
		}
	%>
	
	<form action="">
		
		人物名:<input type="text" name="name"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
