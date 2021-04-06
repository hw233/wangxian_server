<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.transaction.*,
com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,
com.fy.engineserver.datasource.career.*,
com.fy.engineserver.mail.*,
com.xuanzhi.tools.cache.*,
com.fy.engineserver.mail.service.*,java.sql.Connection,java.sql.*,java.io.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*,com.fy.engineserver.datasource.article.concrete.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>一级物品缓存</title>
<script type="text/javascript">
function subcheck(sm) {
	document.getElementById("sm").value=sm;
	document.form1.submit();
}
</script>
</head>
<body>
<%
DefaultArticleEntityManager amanager = (DefaultArticleEntityManager)ArticleEntityManager.getInstance();
LruMapCache idCache = amanager.getCache();
int idCount = idCache.getNumElements();
int idSize = idCache.getSize();
int maxIdSize = idCache.getMaxSize();
long idHits = idCache.getCacheHits();
long idMisses = idCache.getCacheMisses();
%>
<h2>一级物品缓存</h2>
<div style="padding:6px;">
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" atdgn="center">
	<tr bgcolor="#C2CAF5" align="center">	
		<td>物品数量</td>
		<td>缓存数据大小</td>
		<td>最大缓存值</td>
		<td>比例</td>
		<td>命中数</td>
		<td>错失数</td>
	</tr>
	<tr bgcolor="#ffffff" align="center">
		<td><%=idCount %></td>
		<td><%=idSize %></td>
		<td><%=maxIdSize %></td>
		<td style="padding:5px">
		<td><%=idHits %></td>
		<td><%=idMisses %></td>
	</tr>
</table>
</div>
</body>
</html>
