<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.stat.ArticleStatManager"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>需要统计的物品列表</title>
		<link rel="stylesheet" href="../gm/style.css" />
	</head>
	<body><br>
	<table>
	    <%
	    out.print(new String(Arrays.toString(ArticleManager.color_equipment_Strings).getBytes(),"UTF-8") + "<BR/>");
	    out.print(new String(Arrays.toString(	ArticleManager.color_article_Strings).getBytes(),"UTF-8") + "<BR/>");
		List<String> needStatNames = ArticleStatManager.needStatNames;
		int count = 0;
		for(String name:needStatNames){
			if(count==0){
				out.print("<tr>");
			}
			out.print("<td>"+name+"</td>");
			count++;
			if(count==10){
				out.print("</tr>");
				count=0;
			}
		}
		out.print("<h2>数量："+needStatNames.size()+"</h2>");

	    %>
	    </table>
	</body>
</html>
