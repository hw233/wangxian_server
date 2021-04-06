
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.datasource.career.Career"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgData"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgManager"%>
<%@page import="com.fy.engineserver.message.GET_SOME4ANDROID_1_RES"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.message.GET_SOME4ANDROID_RES"%>
<%@page import="com.fy.engineserver.validate.ValidateAskManager"%>
<%@page import="com.fy.engineserver.validate.DefaultValidateManager"%>
<%@page import="com.fy.engineserver.trade.requestbuy.service.RequestBuyManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修复一个技能</title>
</head>
<body>
	<%
		Skill s = CareerManager.getInstance().getCareer(3).getSkillById(8212);
		SkillWithoutTraceAndOnTeamMember ss = (SkillWithoutTraceAndOnTeamMember)s;
		out.print(ss.getBuffName()+"<br>");
		ss.setBuffName("");
	%>
</body>
</html>