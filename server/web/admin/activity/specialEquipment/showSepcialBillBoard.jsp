<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.billboard.special.*"%>


<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看混沌万灵榜单</title>
</head>
<body>
	1是鸿天帝宝;2是伏天古宝
	<%
	ArticleManager am = ArticleManager.getInstance();
	ArticleEntityManager aem = ArticleEntityManager.getInstance();
	PlayerManager pm = PlayerManager.getInstance();
	SpecialEquipmentManager	sem = SpecialEquipmentManager.getInstance();
	SpecialEquipmentBillBoard sbb = sem.getSpecialEquipmentBillBoard();
	if(sbb == null){
		out.print("error");
	}else{
		Map<String, List<Long>> map1 = sbb.getSpecial1Map();
		Map<String, List<Long>> map2 = sbb.getSpecial2Map();
		out.print("鸿天帝宝"+map1.size()+"<br/>");
		out.print("伏天古宝"+map2.size()+"<br/>");
		out.print("<hr/>");
		out.print("<h2>鸿天帝宝</h2>");
		out.print("1个数："+map1.entrySet().size()+"<br/>");
		
		for(Map.Entry<String, List<Long>> en:map1.entrySet()){
			String name = en.getKey();
			out.print(name + "   ");
			
			if(en.getValue().size() == 0){
				out.print("????");
				out.print("<br/>");
			}else{
				for(long id : en.getValue()){
					Special_1EquipmentEntity ee = (Special_1EquipmentEntity)aem.getEntity(id);
					long ownerId = ee.getPlayerId();
					try{
						Player player = pm.getPlayer(ownerId);
						out.print(player.getName()+";  ");
					}catch(Exception e){
						out.print(id+"删除");
					}
				}
				
				/*
				Special_1EquipmentEntity ee = (Special_1EquipmentEntity)aem.getEntity(en.getValue().get(0));
				long id = ee.getPlayerId();
				if(id > 0){
					try{
						Player player = pm.getPlayer(id);
						out.print(player.getName());
					}catch(Exception e){
						out.print(id+"删除");
					}
				}
				*/
				out.print("<br/>");
			}
		}
		
		
		out.print("<hr/>");
		out.print("<h2>伏天古宝</h2>");
		out.print("2个数："+map2.entrySet().size()+"<br/>");
		
		for(Map.Entry<String, List<Long>> en:map2.entrySet()){
			out.print(en.getKey() +"   ");

			if(en.getValue().size() == 0){
				out.print("????");
				out.print("<br/>");
			}else{
				for(long id : en.getValue()){
					Special_2EquipmentEntity ee = (Special_2EquipmentEntity)aem.getEntity(id);
					long ownerId = ee.getOwnerId();
					try{
						Player player = pm.getPlayer(ownerId);
						out.print(player.getName()+";  ");
					}catch(Exception e){
						out.print(id+"删除");
					}
				}
				out.print("<br/>");
			}
		}
	}
	
	%>


</body>
</html>