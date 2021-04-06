<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>


<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%><html>
<head>
<title>创建指定宠物</title>

</head>
<body>

<%
	String egg = request.getParameter("egg");
	String pwd = request.getParameter("pwd");
	String variation = request.getParameter("variation");
	String generation = request.getParameter("generation");
	
	if(egg != null && !egg.equals("")){
		if (!"buzhidaomimajiubiefa".equals(pwd)) {
			out.print("<H1>不知道密码就别发</H1>");
			return;
		}
		Article a = ArticleManager.getInstance().getArticle(egg);
		if(a != null){
			if(a instanceof PetEggProps){
				PetEggProps pep = (PetEggProps)a;
				
				String petArticleS = pep.getPetArticleName();
				Article petArticle = ArticleManager.getInstance().getArticle(petArticleS);
				if(petArticle != null){
					
					if(petArticle instanceof PetProps){
						PetProps pp = (PetProps)petArticle;
						
						Pet pet = PetManager.getInstance().createPetByProps(pep,pp);
						if( pet != null){
							pet.setVariation(Byte.valueOf(variation));
							pet.setGeneration(Byte.valueOf(generation));
							PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().createEntity(petArticle, false, ArticleEntityManager.CREATE_REASON_后台, null, 0, 1, true);
							ppe.setEggArticle(a.getName());
							ppe.setPetId(pet.getId());
							pet.setPetPropsId(ppe.getId());
							out.print(pet.getLogString()+"<br/>");
							out.print("宠物id:"+pet.getPetPropsId()+"宠物道具id:"+ppe.getId());
							out.print("生成宠物成功");
						}else{
							out.print(" pet null");
						}
					}else{
						out.print("petprops null");
					}
					
				}else{
					out.print(" petarticle null");
				}
			}else{
				out.print("蛋:"+a.getClass());
			}
			
		}else{
			out.print(" article null");
		}
		return;
	}
%>

<form action="">
	宠物蛋:<input type="text" name="egg"/>
	密码<input type="password" name="pwd"/>
	<HR>
	选择几代:<BR>
		&nbsp;&nbsp;&nbsp;&nbsp;一代<input type="radio" value="0" name="generation">
		&nbsp;&nbsp;&nbsp;&nbsp;二代<input type="radio" value="1" name="generation">
	<HR>
	变异等级:
		&nbsp;&nbsp;&nbsp;&nbsp;不变异:<input type="radio" name="variation" value="0">
		&nbsp;&nbsp;&nbsp;&nbsp;1级变异:<input type="radio" name="variation" value="1">
		&nbsp;&nbsp;&nbsp;&nbsp;2级变异:<input type="radio" name="variation" value="2">
		&nbsp;&nbsp;&nbsp;&nbsp;3级变异:<input type="radio" name="variation" value="3">
		&nbsp;&nbsp;&nbsp;&nbsp;4级变异:<input type="radio" name="variation" value="4">
		&nbsp;&nbsp;&nbsp;&nbsp;5级变异:<input type="radio" name="variation" value="5">
	<HR>
	<input type="submit" value="submit"/>
</form>


</body>
</html>
