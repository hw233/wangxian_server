<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Map.*"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager.*"%>

<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<html>
<head>
<title>查看所有称号</title>

</head>
<body>


	<%
		PlayerTitlesManager ptm = PlayerTitlesManager.getInstance();
		
		List<PlayerTitleTemplate> list = PlayerTitlesManager.getInstance().getList(); 
	
		if(list != null){
			
			
			%>
			<table cellspacing="1" cellpadding="2" border="1" >
				<tr>
	 				<th>称号key</th>
	 				<th>称号name</th>
	 				<th>称号颜色</th>
	 				<th>称号类型</th>
	 			</tr>
 			<%
			for(PlayerTitleTemplate en : list){
				
				int color = en.getTitleColor();
				String name = en.getTitleName();
				String key = en.getKey();
				int value = en.getTitleType();
				
				%>
				<tr>
					<td><%=key %> </td>
					<td><%= name %></td>
					<td><%= color %> </td>
					<td><%= value %></td>
				</tr>
				
				<%
			}
 			
			%>
			
			</table>
			<%
		}else{
			out.print(" title null");
		}
	
	%>


</body>
</html>
