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
<title>查看某人称号</title>

</head>
<body>

	<%
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			
			%>
			<form action="">
				name:<input type= "text" name="name" />
				<input type="submit" value="submit"/>
			</form>			
			
			<%
			
		}else{
			
			Player player = PlayerManager.getInstance().getPlayer(name);
			
			List<PlayerTitle> list = player.getPlayerTitles();
			if(list != null){
				
				if(list.size() <= 0){
					out.print("没有称号");
				}else{
					
					for(PlayerTitle pt : list){
						out.print("name:"+pt.getTitleName() + "  showName:" +pt.getTitleShowName() + "  type:" +pt.getTitleType() + "  color:"+PlayerTitlesManager.getInstance().getColorByTitleType(pt.getTitleType()) +"<br/>");
					}
				}
			}else{
				out.print(" list null");
			}
			
		}
	
	%>

</body>
</html>
