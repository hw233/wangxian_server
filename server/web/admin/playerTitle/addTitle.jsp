<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Map.*"%>

<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>

<%@page import="com.fy.engineserver.playerTitles.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<title>角色称号操作</title>

</head>
<body>

	<%
		String name = request.getParameter("name");
		String title = request.getParameter("title");
		String option = request.getParameter("option");
		boolean isRemove = "remove".equals(option);
		if(name == null || name.equals("") || title == null || title.equals("")){
			%>
			<form action="">
				name:<input type= "text" name="name" /><br/>
				称号:<input type= "text" name="title" /><br/>
				增加:<input type="radio" name="option" value="add" <%=(isRemove) ? "": "checked" %>>
				删除<input type="radio"  name="option" value="remove" <%=(isRemove) ? "checked": "" %>><BR/>
				<input type="submit" value="submit"/>
			</form>			
			<%
		}else{
			Player player = PlayerManager.getInstance().getPlayer(name);
			boolean result = false;
			out.print("isRemove?" + isRemove + "</HR>");
			if (isRemove) {
				result = PlayerTitlesManager.getInstance().removeTitle(player,title);
				player.setTitle("");
			} else {
				result = PlayerTitlesManager.getInstance().addTitle(player,title,true);
			}
			SocialManager.logger.warn("isRemove?" + isRemove);
			if(result){
				out.print("success");
			}else{
				out.print("fail");
			}
		}
	%>

</body>
</html>
