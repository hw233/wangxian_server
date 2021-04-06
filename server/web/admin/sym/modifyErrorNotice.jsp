<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_addDamage"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_Silence"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	out.print("修改之前的活动数量："+ActivityManagers.getInstance().infos.size()+"<br>");
	ActivityManagers.getInstance().infos.clear();	
	ActivityManagers.getInstance().vipMesses.clear();	
	out.print("删除之前的："+ActivityManagers.getInstance().infos.size()+"<br>");
	ActivityManagers.getInstance().initActivityShowInfo();
	out.print("修改之后的活动数量："+ActivityManagers.getInstance().infos.size()+"<br>");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改错误的描述</title>
</head>
<body>

</body>
</html>