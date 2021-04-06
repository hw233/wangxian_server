<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrentice"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrenticeManager"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrenticeSubSystem"%>
<%@page import="com.xuanzhi.tools.transport.DefaultConnectionSelector"%>
<%@page import="com.xuanzhi.tools.transport.ConnectionSelectorHelper"%>
<%@page import="com.xuanzhi.tools.transport.Connection"%>
<%@page import="com.fy.engineserver.core.Game"%><html>
<%!DefaultConnectionSelector dcs; 
	ConnectionSelectorHelper helper;

	int count = 0;

	List<Long> list = new ArrayList<Long>();
	
	//int countNum = 7*24*60;
		
	public static boolean hasRunning = false;

	Thread 	thread = null;
	
	public void repairMethod(ConnectionSelectorHelper helper){
		try{
		Connection conns[] = helper.getAllConnections();
			if(conns != null){
				for(int i = 0; i < conns.length; i++){
					Connection conn1 = conns[i];
					if(conn1 != null){
						Player player1 = (Player) conn1.getAttachmentData("Player");
						for(int j = 0; j < conns.length; j++){
							Connection conn2 = conns[j];
							if(i != j){
								if(conn2 != null){
									Player player2 = (Player) conn2.getAttachmentData("Player");
									if(player1 == player2){
										if(player1 != null && conn1 != conn2){
											try{
												conn1.close();
												conn2.close();
												Game.logger.warn("[发现疑似单人多连接外挂] ["+(player1 != null ? player1.getLogString() : "")+"] [conn1:"+conn1.getName()+"] [conn2:"+conn2.getName()+"]");
											}catch(Exception e){
												Game.logger.warn("[发现疑似单人多连接外挂] ["+(player1 != null ? player1.getLogString() : "")+"]",e);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception ex){
			Game.logger.error("执行修复线程发生异常",ex);
		}
	}
%>
<%
dcs = (DefaultConnectionSelector)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean("game_server");
Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
helper = new ConnectionSelectorHelper(dcs);%>
<head>
<title>修改单人多连接BUG</title>
</head>
<body>


<%

	
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){
		thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				while(hasRunning){
					
					try{
						Thread.sleep(1000);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
					try{
						count++;
						repairMethod(helper);
					}catch(Throwable e){
						
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
		thread.setName("修改单人多连接bug");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./threadRepairBug.jsp?action=start'>点击这里</a>启动线程! <a href='./threadRepairBug.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./threadRepairBug.jsp?action=stop'>点击这里</a>停止线程! <a href='./threadRepairBug.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
