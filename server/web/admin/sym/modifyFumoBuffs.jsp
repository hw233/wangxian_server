<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.articleEnchant.model.EnchantModel"%>
<%@page import="com.fy.engineserver.articleEnchant.EnchantManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
<title>test</title>
</head>
<body>

<%
	//
	BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateByName("附魔胸甲-坚硬");
	bt.setTimeType((byte)1);
	bt.setDeadNotdisappear(false);
	bt.setClearSkillPointNotdisappear(false);
	
	BuffTemplate bt2 = BuffTemplateManager.getInstance().getBuffTemplateByName("附魔武器-内功");
	bt2.setTimeType((byte)1);
	bt2.setDeadNotdisappear(false);
	bt2.setClearSkillPointNotdisappear(false);
	
	BuffTemplate bt3 = BuffTemplateManager.getInstance().getBuffTemplateByName("附魔武器-外功");
	bt3.setTimeType((byte)1);
	bt3.setDeadNotdisappear(false);
	bt3.setClearSkillPointNotdisappear(false);
	
	out.println("ok!!!!!");
	
%>


 