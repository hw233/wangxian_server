<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.seal.data.Seal"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_addDamage"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_Silence"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	ExchangeActivityManager eam = ExchangeActivityManager.getInstance();
	List<MaintanceActivity> maintanceActivities = eam.getMaintanceActivities();
	String rate = request.getParameter("rate");
	if (rate != null && !"".equals(rate)) {
		maintanceActivities.get(0).setRate(Double.valueOf(rate));
		out.print("修改折扣为:"+rate);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.cave.MaintanceActivity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改仙府维护打折</title>
<form action="">折扣:<input type="text" name="rate" value="" /> <input
	type="submit" value="修改折扣" /></form>
</head>
<body>

</body>
</html>