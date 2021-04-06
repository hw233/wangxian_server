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

<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.sprite.horse.dateUtil.DateFormat"%><html>
<head>
<title>修改副本boss掉落cd</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			long time = SpecialEquipmentManager.getInstance().downcityLastCreateEEtime;
			if(time > 0 ){
				out.print(DateFormat.getSimpleDay(new Date(time)));
			}else{
				out.print("还没有生成副本bosscd");
			}
		}else{
		
			SpecialEquipmentManager.副本cd = 60*1000;
			out.print("设置成功<br/>");
			return;
		}
	%>
	
	
	
	<form action="">
		修改副本boss掉落cd(默认为7天，修改为1分钟)<br/>
		操作者:<input type="text" name="name"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
