<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改物品描述</title>
<%
	if(PlatformManager.getInstance().isPlatformOf(Platform.韩国)){
		Article a = ArticleManager.getInstance().getArticle("완미혼백");
		out.print("修改前："+a.getDescription()+"<br>");
		a.setDescription("삼혼 과 칠백 합이위일.“귀역군계도감”*1과 교환가능");
		out.print("修改后："+a.getDescription()+"<br>");
	}
%>