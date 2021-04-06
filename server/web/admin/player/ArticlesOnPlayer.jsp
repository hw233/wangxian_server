<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.transaction.*,
com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,
com.fy.engineserver.datasource.career.*,
com.fy.engineserver.mail.*,
com.fy.engineserver.mail.service.*,java.sql.Connection,java.sql.*,java.io.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所持物品</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<%
Player player = null;
EquipmentColumn ec = player.getEquipmentColumns();
ArticleEntityManager aem = ArticleEntityManager.getInstance();
%>
<body>
<table>
<tr><td>装备栏</td></tr>
<%
long[] ids = ec.getEquipmentIds();
for(int i = 0; i < ids.length; i++){
	%>
	<tr><td><%
	if(ids[i] > 0){
		ArticleEntity ae = aem.getEntity(ids[i]);
		if(ae != null && ae instanceof EquipmentEntity){
			out.println(ae.getArticleName()+" 星级:"+((EquipmentEntity)ae).getStar()+"颜色:"+ArticleManager.color_equipment_Strings[ae.getColorType()]+" id:"+ids[i]);
		} else if (ae != null && ae instanceof NewMagicWeaponEntity) {
			out.println(ae.getArticleName()+" 星级:"+((NewMagicWeaponEntity)ae).getMstar()+"颜色:"+ArticleManager.color_magicweapon_Strings[ae.getColorType()]+" id:"+ids[i]);
		}else{
			out.println(ids[i]);
		}
	}else{
		out.println("无装备");
	}
	%></td></tr>
	<%
}
%>
</table>
<%
Knapsack[] ks = player.getKnapsacks_common();
for(int i = 0; i < ks.length; i++){
	Knapsack knapsack = ks[i];
	
%>
<table>
<tr></tr>
<%
Cell[] cells = knapsack.getCells();
for(int j = 0; j < cells.length; j++){
	Cell cell = cells[j];
	if(j % 5 == 0){
		out.print("<tr>");
	}
	
	if(cell.entityId > 0){
		ArticleEntity ae = aem.getEntity(cell.entityId);
		if(ae != null){
			if(ae instanceof EquipmentEntity){
				out.println(ae.getArticleName()+" 星级:"+((EquipmentEntity)ae).getStar()+"颜色:"+ArticleManager.color_equipment_Strings[ae.getColorType()]+" id:"+cell.entityId+" count:"+cell.count);	
			}else if(ae instanceof PropsEntity){
				out.println(ae.getArticleName()+"颜色:"+ArticleManager.color_article_Strings[ae.getColorType()]+" id:"+cell.entityId+" count:"+cell.count);
			}else{
				out.println(ae.getArticleName()+"颜色:"+ArticleManager.color_article_Strings[ae.getColorType()]+" id:"+cell.entityId+" count:"+cell.count);
			}
		}else{
			out.println(cell.entityId+" "+cell.count);
		}
	}else{
		out.println("");
	}
	
	if(j == cells.length - 1 || j % 5 == 4){
		out.println("</tr>");
	}
}
%>
</table>
<%} %>
</body>
</html>
