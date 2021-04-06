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
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>重选</title>
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
	
	playerId = Long.parseLong(obj.toString());
	player = pm.getPlayer(playerId);
	long activityId = player.getActivityId((byte)0);
	if(activityId >0){
		FateActivity fa = fm.getFateActivity(activityId);
		if(fa != null){
			if(playerId == fa.getInviteId()){
				List<Long> undo =  fa.getRandomUndo();
				out.println("<h2>没有做过的人");
				for(long id:undo){
					Player p1 = pm.getPlayer(id);
					out.println(p1.getName()+"**********"+"<a href="+path+"/admin/fateActivity/choose.jsp?invitedId="+p1.getId()+">选择</a><br/>");
				}
				out.println("<h2>做过的人");
				List<Long> did = fa.getRandomdo();
				for(long id:did){
					Player p1 = pm.getPlayer(id);
					out.println(p1.getName()+"**********"+"<a href="+path+"/admin/fateActivity/choose.jsp?invitedId="+p1.getId()+">选择</a><br/>");
				}
				long flushTime = fa.getLastFlushTime()/1000;
				long now = System.currentTimeMillis()/1000;
				long lastTime = fa.getTemplate().getFlushInterval();
				out.println("刷新"+(flushTime+lastTime-now)+"秒");
			}else{
				out.println("出错");
			}
			
		}else{
			out.println("任务为null");
		}
	}else{
		out.println("这个人没有任务");
	}
	
	 %>
</body>
</html>
