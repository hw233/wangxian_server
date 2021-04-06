<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.activity.pickFlower.FlowerNpc"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查看所有花npc</title>
</head>

<body>
	
	
	<%
	
		Game[] games = GameManager.getInstance().getGames();
		for(Game g : games){
			LivingObject[] los = g.getLivingObjects();
			for(LivingObject lo : los){
				if( lo instanceof FlowerNpc){
					FlowerNpc npc = (FlowerNpc)lo;
					out.print(npc.getName()+" "+ npc.getGameName()+"<br/>");
				}
			}
			          
		}
	
	
	%>
</body>
</html>
