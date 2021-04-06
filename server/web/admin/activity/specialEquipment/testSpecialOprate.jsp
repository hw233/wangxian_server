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
<title>测试装备操作</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("name");
		String ids = request.getParameter("ids");
		
		if(name != null && !name.equals("") && ids != null && !ids.equals("")){
			
			Player player = PlayerManager.getInstance().getPlayer(name);
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.parseLong(ids));
			out.print(ae.getClass()+"<br/>");
			ArticleManager.getInstance().炼器申请(player,(EquipmentEntity)ae,-1l, new byte[0]);
			out.print("over");
			
			return;
		}
		
	%>
	
	<form action="">
		playerName:<input type="text" name="name" ><br/>
		eqId:<input type="text" name="ids" ><br/>
		<input type="submit" value="submit" ><br/>
	
	</form>
	
</body>
</html>