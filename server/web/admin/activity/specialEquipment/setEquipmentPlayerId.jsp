<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置万灵榜装备的playerId</title>
</head>
<body>

	

	<%
		String  namest =  request.getParameter("name");
		String  idst =  request.getParameter("id");
		if(namest == null || idst == null){
	%>
			
		<form action="">
			name:<input type="text"  name="name"/>
			装备名:<input type="text"  name="id"/>
			<input type="submit" value="submit"></input>
		</form>
			
	<%
		}else{
			PlayerManager pm = PlayerManager.getInstance();
			Player player =  pm.getPlayer(namest);

			List<Long> list = SpecialEquipmentManager.getInstance().getSpecialEquipmentBillBoard().getSpeicail1(idst);
			if(list.size() > 0){
				long id = list.get(0);
				ArticleEntity ae= ArticleEntityManager.getInstance().getEntity(id);
				if(ae instanceof Special_1EquipmentEntity){
					Special_1EquipmentEntity se1 = (Special_1EquipmentEntity)ae;
					se1.setPlayerId(player.getId());
				}else{
					out.print(ae.getClass());
				}
			}
			out.print("success");
		}
				
	
	%>
</body>
</html>