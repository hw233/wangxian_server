<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<title>修改答题文采值</title>
</head>
<body>
<%! 
	int count = 0;

	int countNum = 10*60*24*10;
	
	Thread thread = null;
	
	public static boolean hasRunning = false;
	long sleepTime = 60*1000;
%><%
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){

	thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				while(hasRunning && count < countNum){
					
					try{
						Thread.sleep(sleepTime);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
					try{
						count++;
						Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
						
						for(Player player : ps){
							player.setDirty(true,"culture");
						}
						
					}catch(Exception e){
						QuizManager.logger.error("[后台设置玩家文采值异常]",e);
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("修改文采值");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./updatePlayerCulture.jsp?action=start'>点击这里</a>启动线程! <a href='./updatePlayerCulture.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./updatePlayerCulture.jsp?action=stop'>点击这里</a>停止线程! <a href='./updatePlayerCulture.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
