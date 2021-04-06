<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>销毁</title>
</head>
<body>

	

	<%
		String  namest =  request.getParameter("name");
		String  idst =  request.getParameter("id");
		if(namest == null || idst == null){
	%>
			
		<form action="">
			name:<input type="text"  name="name"/>
			equipmentId:<input type="text"  name="id"/>
			<input type="submit" value="submit"></input>
		</form>
			
	<%
		}else{
			PlayerManager pm = PlayerManager.getInstance();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			Player player =  pm.getPlayer(namest);
			EquipmentEntity ee = (EquipmentEntity)aem.getEntity(Long.parseLong(idst.trim()));
			((EquipmentEntity) ee).destroyEntity(player);
			out.print("success");
		}
				
	
	%>
</body>
</html>