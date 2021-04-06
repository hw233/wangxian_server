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
	<title>开始活动</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
	PlayerManager pm = PlayerManager.getInstance();
	Object obj = session.getAttribute("playerid");
	long playerId = 0;
	String error = null;
	String outSt = null;
	Player player = null;
	if(obj != null){
		playerId = Long.parseLong(obj.toString());
		try{
			player = pm.getPlayer(playerId);
			long activityId = player.getActivityId((byte)0);
			if(activityId >0){
				outSt = "不能开始，正在进行";
			}else{
				out.println(activityId);
				FateManager fm = FateManager.getInstance();
				FateActivity fa = fm.beginActivity(player,590l);
				List<Long> undo =  fa.getRandomUndo();
				out.println("<h2>没有做过的人</h2>");
				for(long id:undo){
					Player p1 = pm.getPlayer(id);
					out.println(p1.getName()+"**********"+"<a href="+path+"/admin/fateActivity/choose.jsp?invitedId="+p1.getId()+">选择</a><br/>");
				}
				out.println("<h2>做过的人</h2>");
				List<Long> did = fa.getRandomdo();
				for(long id:did){
					Player p1 = pm.getPlayer(id);
					out.println(p1.getName()+"**********"+"<a href="+path+"/admin/fateActivity/choose.jsp?invitedId="+p1.getId()+">选择</a><br/>");
				}
				long flushTime = fa.getLastFlushTime()/1000;
				long now = System.currentTimeMillis()/1000;
				long lastTime = fa.getTemplate().getFlushInterval();
				long interval = flushTime+lastTime-now;
				if(interval >= 0){
					out.println("刷新"+interval+"秒");
				}else{
					out.println("<a href="+path+"/admin/fateActivity/flush.jsp>刷新</a>");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			error = playerId +"不存在";
		}
	}
	
	if(error != null){
		out.println(error);
	}
	
	if(outSt != null){
		out.println(outSt);
	}
	%>
</body>
</html>
