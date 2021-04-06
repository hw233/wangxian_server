<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.engineserver.sprite.AbstractPlayer"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.guozhan.Constants"%>
<%@page import="com.fy.engineserver.guozhan.GuozhanOrganizer"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>check</title>
</head>
<body>
<% 
Constants cons = GuozhanOrganizer.getInstance().getConstants();
cons.defenderLoseBornMap = "jiuzhoutiancheng";
out.print("ok");
%>
</body>
</html>
