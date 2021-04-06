<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>

<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%><html>
<head>
<title>修改特殊装备在玩家身上的掉落时间</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			
		}else{
		
			SpecialEquipmentManager.产生装备不掉时间 = 1l*60*1000;
			out.print("设置成功<br/>");
		}
	%>
	
	<form action="">
		修改特殊装备在玩家身上保存多长时间不掉落(默认为12小时，修改为1分钟)<br/>
		操作者:<input type="text" name="name"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
