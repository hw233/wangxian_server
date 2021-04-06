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
<%@page import="com.fy.engineserver.sprite.Player"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改排行榜数据</title>
</head>
<body>
	<%
		String eqIds = request.getParameter("eqId");
		if(eqIds != null && !eqIds.equals("")){
			
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.parseLong(eqIds));
			if(ae !=null && ae instanceof Special_1EquipmentEntity){
				out.print(ae.getArticleName());
				SpecialEquipmentManager.getInstance().getSpecialEquipmentBillBoard().putSpecial1(ae.getArticleName(),ae.getId());
				out.print("over");
			}
			return;
		}
	%>
	
	<form action="">
		装备id:<input type="text" name="eqId"/>
		<input type="submit" value="submit"/>
	</form>
	
	
</body>
</html>