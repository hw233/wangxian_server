<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.activity.fateActivity.FateManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>删除活动</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
		FateManager fm = FateManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		
		Object obj = session.getAttribute("playerid");
		long playerId = 0;
		Player player = null;
		
		playerId = Long.parseLong(obj.toString());
		player = pm.getPlayer(playerId);
		
		fm.cancleActivity(player,(byte)0);
		out.print("成功");
	%>
	
	
</body>
</html>
