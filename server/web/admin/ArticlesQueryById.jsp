<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.boss.game.GameConstants"%><%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>物品</title>
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){
	String articleIds = request.getParameter("articleId");
	if(articleIds != null){
		String[] aIDs = articleIds.split(",");
		for (String i : aIDs) {
			ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(Long.parseLong(i));
			if (entity != null) {
				out.print(entity.getId() + "---物品名字" + entity.getArticleName() + "<br>");
			}else {
				out.print("没有----" + i + "<br>");
			}
		}
	}
}else{
	out.println("服务器不能改数据");
}
%>
</head>
<body>
<form name="f1">
物品id:<input name="articleId"><br>
<input type="submit" value="查询物品数据库">
</form>
</body>
</html>
