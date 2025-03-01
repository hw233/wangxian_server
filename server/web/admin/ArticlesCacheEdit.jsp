<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.cache.*,com.xuanzhi.tools.transaction.*,com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,com.xuanzhi.gameresource.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,com.fy.engineserver.mail.service.*,java.sql.Connection,java.sql.*,java.io.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所持物品</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<%
String aid = request.getParameter("aid"); 
if(aid != null && aid.matches("\\d+")) {
	LruMapCache cache = DefaultArticleEntityManager.getCache();
	ArticleEntity entity = (ArticleEntity)cache.get(Long.parseLong(aid));
	if(entity != null) {
		cache.remove(Long.parseLong(aid));
		out.println("已从cache中清除了" + aid);
	}
}
%>
<form action="ArticleCacheEdit.jsp">
	需要从cache内清除的物品id:<input type=text size=20 name=aid>
	<input type=submit name=sub1 value="提交">
</form>
</html>
