<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.base.FateActivity"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateActivityType"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>被邀请人放弃活动</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
	PlayerManager pm = PlayerManager.getInstance();
	FateManager fm = FateManager.getInstance();
	Object obj = session.getAttribute("playerid");
	
	long playerId = 0;
	String error = null;
	String outSt = null;
	Player player = null;
	
	if(obj != null){
		playerId = Long.parseLong(obj.toString());
		try{
			player = pm.getPlayer(playerId);
			fm.cancleActivity(player,(byte)0);
		}catch(Exception e){
			e.printStackTrace();
			error = "玩家不存在";
		}
	}
	
	
	if(error != null){
		out.println(error);
	}
	
	out.println("<br/> <a href="+path+"/admin/fateActivity/queryActivity.jsp>返回</a>");
	%>
</body>
</html>
