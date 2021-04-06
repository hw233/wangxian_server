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
	
		String petIds = request.getParameter("petIds");

		if(petIds == null || petIds.equals("") ){
			
		}else{
			Article[] als = ArticleManager.getInstance().getAllArticles();
			List<PetEggProps> list = new ArrayList<PetEggProps>();
			for(Article a : als){
				if(a instanceof PetEggProps){
					list.add((PetEggProps)a);
				}
			}
			
			for(PetEggProps pe : list){
				out.print("宠物名称:"+pe.getName()+"  宠物职业:"+pe.getCareer()+"  宠物性格:"+pe.getCharacter()+"<br/>");
			}
		}
	%>
	
	<form action="">
		
		查询:<input type="text" name="petIds"/><br/>

		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
