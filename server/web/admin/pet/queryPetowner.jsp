<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>


<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.xuanzhi.tools.cache.LruMapCache"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.xuanzhi.tools.cache.Cacheable"%><html>
<head>
<title>查询宠物的owner</title>
</head>
<body>

	<%
		LruMapCache cache = PetManager.getInstance().mCache;
		Set<Long> set = cache.keySet();
		for(long e : set){
			Pet pet = (Pet)cache.get(e);
			if(pet.getOwnerId() <= 0){
				out.print("<font color=\"red\">"+pet.getOwnerId()+"</font>"+"<br/>");
			}else{
				out.print("<font color=\"green\">"+pet.getOwnerId()+"</font>"+"<br/>");
			}
			
		}
	
	%>


</body>
</html>
