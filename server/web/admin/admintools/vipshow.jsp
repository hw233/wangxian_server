<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.vip.VipManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>


<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.horse.dateUtil.DateFormat"%>
<%@page import="com.fy.engineserver.chuangong.ChuanGongManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
<title>修改传功</title>
</head>
<body>
<%! 
	int count = 0;
	long nowtime = SystemTime.currentTimeMillis();
	
	Thread thread = null;
	public static boolean hasRunning = false;
	
%><%
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){

	thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				while(hasRunning){
					try {
						Thread.sleep(5000L);
					} catch (Exception e) {
						
					}
					Player [] players = GamePlayerManager.getInstance().getOnlinePlayers();
					for (Player player : players) {
						player.vipDisplay = true;
						player.setVipLevel(player.getVipLevel());
						VipManager.getInstance().设置玩家的vip属性(player);
					}
					count++;
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("VIP显示");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./vipshow.jsp?action=start'>点击这里</a>启动线程! <a href='./vipshow.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./vipshow.jsp?action=stop'>点击这里</a>停止线程! <a href='./vipshow.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
