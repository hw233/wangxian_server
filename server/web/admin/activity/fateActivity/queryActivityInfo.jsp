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
<%@page import="java.util.List"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查看活动详情</title>
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
	if(obj == null) return;
	playerId = Long.parseLong(obj.toString());
	player = pm.getPlayer(playerId);
	long activityId = player.getActivityId((byte)0);
	if(activityId >0){
		FateActivity fa = fm.getFateActivity(activityId);
		if(fa != null){
			%>
			
			<table border="1px">
				<tr><td>活动id</td><td><%=fa.getId() %></td></tr>
				<tr><td>模板名</td><td><%=fa.getActivityName() %></td></tr>
				<tr><td>地图名</td><td><%=fa.getMapName() %></td></tr>
				<tr><td>区域名</td><td><%=fa.getTemplate().getRegionName()%></td></tr>
				<tr><td>邀请id</td><td><%=fa.getInviteId() %></td></tr>
				<tr><td>被邀请id</td><td><%=fa.getInvitedId() %></td></tr>
				<tr><td>级别</td><td><%=fa.getLevel() %></td></tr>
				<tr><td>状态</td><td><%=fa.getState() %></td></tr>
				<tr><td>同意次数</td><td><%=fa.getSuccessNum()%></td></tr>
			</table>
			
			<%
		}else{
			out.println("任务为null");
		}
	}else{
		out.println("这个人没有任务");
	}
	
	 %>
</body>
</html>
