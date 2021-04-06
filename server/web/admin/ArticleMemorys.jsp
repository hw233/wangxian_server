<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PropsCategory"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%><%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><html>
<head>
<%
DefaultArticleEntityManager aem = (DefaultArticleEntityManager)ArticleEntityManager.getInstance();

%>
<body>
<table>
<tr><td>内存中的aTempTable</td><td>内存中的overlapTempEntityMap</td></tr>
<tr><td><%=aem.getaTempTable().size() %></td><td><%=aem.getOverlapTempEntityMap().size() %></td></tr>
</table>
</body>
</html>