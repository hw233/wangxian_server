<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	PetProps[] allPetProps = ArticleManager.getInstance().allPetProps;
	for(int i=0,len=allPetProps.length;i<len;i++){
		PetProps pp = allPetProps[i];
		if(pp!=null){
			if(pp.getName().equals("神●灵仙之魂")){
				out.print("修改前："+pp.getName()+"--"+pp.getIconId()+"<br>");
				pp.setIconId("nvwu");
				out.print("修改后："+pp.getName()+"--"+pp.getIconId()+"<br>");
				out.print("<hr>");
			}
		}
	}

	Map<String, Article> articleNameMaps = new Hashtable<String, Article>();
	Map<String, Article> articleNameCnnMaps = new Hashtable<String, Article>();
	for (Article a : ArticleManager.getInstance().getAllArticles()) {
		if(a instanceof PetProps){
			if (a.getName().equals("神●灵仙之魂")) {
				a.setIconId("nvwu");
			}
			articleNameMaps.put(a.getName(), a);
			articleNameCnnMaps.put(a.getName_stat(), a);
		}
	}
	
	Class<?> c = ArticleManager.class;
	Field f_allArticleNameMap = c.getDeclaredField("allArticleNameMap");
	f_allArticleNameMap.setAccessible(true);
	Field f_allArticleCNNameMap = c.getDeclaredField("allArticleCNNameMap");
	f_allArticleCNNameMap.setAccessible(true);
	f_allArticleNameMap.set(ArticleManager.getInstance(), articleNameMaps);
	f_allArticleCNNameMap.set(ArticleManager.getInstance(), articleNameCnnMaps);
	out.print("ok");
%>

<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.lang.reflect.Field"%><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
