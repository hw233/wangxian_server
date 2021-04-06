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
	<title>刷新活动</title>
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
			long Id = player.getActivityId((byte)0);
			if(Id > 0){
				FateActivity fa = fm.getFateActivity(Id);
				if(fa != null){
					if(playerId == fa.getInviteId()){
						fm.flushPlayers(player,FateActivityType.国内情缘.type);
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
						out.println("刷新"+(flushTime+lastTime-now)+"秒");
					}else{
						out.println("出错");
					}
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
	
	out.println("<br/> <a href="+path+"/admin/fateActivity/beginActivity.jsp>返回</a>");
	%>
</body>
</html>
