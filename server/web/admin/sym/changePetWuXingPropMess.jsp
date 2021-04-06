<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>

<%@page import="com.fy.engineserver.datasource.article.data.props.PetWuXingProp"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改物品描述</title>
<%
	String names [] = {"启智丹","启灵丹"};
	
	if(PlatformManager.getInstance().isPlatformOf(Platform.官方)){
		Article[] allArticles = ArticleManager.getInstance().getAllArticles();
		for(Article article : allArticles){
			for(String name : names){
				if(name.equals(article.getName())){
					if(article instanceof PetWuXingProp == false){
						out.print("物品 "+name+" 类型不对！");
						return;
					}
					PetWuXingProp petProp = (PetWuXingProp)article;
					List<String> petNames = new ArrayList<String>(Arrays.asList(petProp.getPetnames()));
					if(!petNames.contains("仙之五行剑奴蛋")){
						petNames.add("仙之五行剑奴蛋");
						petProp.setPetnames(petNames.toArray(new String[]{}));
						out.print(Arrays.toString(petProp.getPetnames())+"<br>");
					}else{
						out.print("已经刷过<br>");
					}
				}
			}
		}
	}else{
		out.print("您无权操作");
	}
	
%>
