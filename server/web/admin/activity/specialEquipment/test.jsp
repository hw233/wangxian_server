<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>



<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentBillBoard"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map.Entry"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试</title>
</head>
<body>
	
	<%
	
		SpecialEquipmentManager sem = SpecialEquipmentManager.getInstance();
		SpecialEquipmentBillBoard sbb = sem.getSpecialEquipmentBillBoard();
		Map<String, List<Long>> map = sbb.getSpecial2Map();
		
		for(Map.Entry<String, List<Long>> en : map.entrySet()){
			
			Article a = ArticleManager.getInstance().getArticle(en.getKey());
			if( a != null && a instanceof Equipment){
				out.print(a.getClass()+"   "+a.getName()+"<br/>");
			}else{
				out.print(en.getKey()+"<br/>");
			}
			
		}
		
	%>
	
	
</body>
</html>