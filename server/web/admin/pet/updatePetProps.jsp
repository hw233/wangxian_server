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
<title>修改宠物的道具id</title>
</head>
<body>
	
	<%
	
		String petIds = request.getParameter("petIds");
		String values = request.getParameter("values");
		if(petIds == null || petIds.equals("") ||values == null || values.equals("")){
			
		}else{
			
			long petId = Long.parseLong(petIds);
			Pet pet = PetManager.getInstance().getPet(petId);
			if(pet != null){
				long propsId = Long.parseLong(values);
				pet.setPetPropsId(propsId);
				PetManager.logger.error("[后台设置宠物道具id] ["+pet.getLogString()+"] ["+propsId+"]");
			}else{
				out.print("没有这个宠物"+petId);
			}
			
		}
	%>
	
	<form action="">
		
		宠物id:<input type="text" name="petIds"/><br/>
		宠物道具id:<input type="text" name="values"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
