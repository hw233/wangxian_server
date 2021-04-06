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
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentBillBoard"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.billboard.special.FlushBossInfo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置刷新boss的出现boss时间</title>
</head>
<body>
	
	<%
	
		SpecialEquipmentManager sem = SpecialEquipmentManager.getInstance();
	
		FlushBossInfo info = sem.getFlushBossInfo();
		
		if(info != null && info.isEffect()){
			info.setFlushTime(info.getFlushTime()+30*24*60*60*1000);
			//保存disk
			sem.getDisk().put("bossInfo", info);
			SpecialEquipmentManager.logger.error("[后台修改产生boss时间] [时间:"+info.getFlushTime()+"]");
		}else if(info == null){
			SpecialEquipmentManager.生成boss信息间隔  = 30*24*60*60*1000;
			SpecialEquipmentManager.logger.error("[后台修改产生boss时间] [30天]");
			out.print("info null，设置生成boss信息间隔 = 30天");
		}
	%>
	
	
</body>
</html>