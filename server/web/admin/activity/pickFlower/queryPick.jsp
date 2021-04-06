<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>

<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.activity.pickFlower.FlowerNpc"%>
<%@page import="com.fy.engineserver.activity.pickFlower.Flower"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查看采花状态</title>
</head>

<body>
	
	<%
	
		PickFlowerEntity pfe = PickFlowerManager.getInstance().pickFlowerEntity;
		if(pfe != null){
			
			Game g =  pfe.game;
			if(g != null){
				
				LivingObject[] los = g.getLivingObjects();
				for(LivingObject lo:los){
					if(lo instanceof FlowerNpc){
						FlowerNpc npc = (FlowerNpc)lo;
						Flower flower = npc.getFlower();
						ArticleEntity ae = flower.getAe();
						if(ae != null){
							out.print(ae.getId()+" "+ae.getArticleName()+"<br/>");
						}else{
							out.print("ae null");
						}
					}
				}
				
			}else{
				out.print("game null ");
			}
			             
			
		}else{
			out.print("没有采花活动实体");
		}
	
	%>

</body>
</html>
