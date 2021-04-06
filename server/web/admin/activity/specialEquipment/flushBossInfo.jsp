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


<%@page import="com.fy.engineserver.billboard.special.FlushBossInfo"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager.SpecialEquipmentAppearMap"%>
<%@page import="com.fy.engineserver.sprite.horse.dateUtil.DateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刷新boss信息</title>
</head>
<body>
	
	<%
		FlushBossInfo info = SpecialEquipmentManager.getInstance().getFlushBossInfo();
		if(info == null){
			out.print("还没生成刷新boss信息");
		}else{
			
	
			
			boolean effect = info.isEffect();
			if(effect){
				out.print("还没有生成boss");
				SpecialEquipmentManager.SpecialEquipmentAppearMap map =  info.getAppearMap();
				byte country = info.getCountry();
				int monsterId = info.getMonsterId();

				out.print("怪物Id"+monsterId+"<br/>");
				out.print("country"+country+"<br/>");
				out.print("map name"+map.getMapName()+"<br/>");
				out.print("map x"+map.getX()+"<br/>");
				out.print("map y"+map.getY()+"<br/>");
				
			}else{
				out.print("已过期<br/>");
				SpecialEquipmentManager.SpecialEquipmentAppearMap map =  info.getAppearMap();
				byte country = info.getCountry();
				int monsterId = info.getMonsterId();

				out.print("怪物Id"+monsterId+"<br/>");
				out.print("country"+country+"<br/>");
				out.print("map "+map.getMapName()+"<br/>");
				out.print("map x"+map.getX()+"<br/>");
				out.print("map y"+map.getY()+"<br/>");
				out.print("是否死亡(false没有死)"+info.bossDie+"<br/>");
				out.print("死亡时间:"+DateFormat.getSimpleDay(new Date(info.bossDieTime))+"<br/>");
			}
			
		}
	
	
	%>
	
	
	
</body>
</html>