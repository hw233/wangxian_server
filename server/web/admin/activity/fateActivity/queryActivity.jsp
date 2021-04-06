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
	<title>查看活动</title>
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
	FateActivity fa =  FateManager.getInstance().getFateActivity(player,FateActivityType.国外煮酒.type);
	if(fa != null){
		
		out.print("国家:"+fa.getCountry()+"<br/>");
		out.print("邀请者Id:"+fa.getInviteId()+"   "+fa.isInviteArrive()+"<br/>");
		out.print("被邀请者Id:"+fa.getInvitedId()+"   "+fa.isInvitedArrive()+"<br/>");
		if(playerId == fa.getInviteId()){
			if(fa.getState()>=FateActivity.进行状态){
				out.println("正在进行");
			}else if(fa.getState() < FateActivity.选人成功) {
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
				
			}else{
				out.println("<br/> <a href="+path+"/admin/fateActivity/reQueryActivity.jsp>重选</a>");
			}
		}else if(playerId == fa.getInvitedId()){

			if(fa.getState()>=FateActivity.进行状态){
				out.println("正在进行");
			}else {
				out.println("<br/> <a href="+path+"/admin/fateActivity/giveup.jsp>放弃</a>");
			}
		}else{
			out.println("出错");
		}
	}
	
	 %>
</body>
</html>
