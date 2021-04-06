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


<%@page import="com.fy.engineserver.billboard.special.FlushBossInfo"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager.SpecialEquipmentAppearMap"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新刷新条件</title>
</head>
<body>
	
	<%
		out.print("封印多长时间后:"+SpecialEquipmentManager.生成boss信息间隔+"<br/>");
		out.print("第一次通知生成boss信息的时间:"+SpecialEquipmentManager.第一次通知生成boss信息的时间+"<br/>");
		out.print("第二次通知生成boss信息的时间:"+SpecialEquipmentManager.第二次通知生成boss信息的时间+"<br/>");
		out.print("第三次通知生成boss信息的时间:"+SpecialEquipmentManager.第三次通知生成boss信息的时间+"<br/>");
		out.print("<hr/>");
		out.print("<a href=\"realUpdate.jsp\">修改</a>");

	%>
	
</body>
</html>