<%@page import="java.util.HashMap"%>
<%@page import="java.net.URL"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.deploy.ServerStatus"%>
<%@page import="com.fy.boss.deploy.ProjectManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.boss.game.service.ServerManager"%>
<%@page import="com.fy.boss.game.model.Server"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%	
	String serverName = null;
	try{
		serverName = request.getParameter("servername");
		Server server = ServerManager.getInstance().getServer(serverName);
		String urlstr = server.getSavingNotifyUrl();
		urlstr = urlstr.split("saving_notifier")[0];
		HashMap headers = new HashMap();
		byte bs [] = HttpUtils.webGet(new URL(urlstr), headers, 5000, 5000);
		if(bs != null && bs.length > 0){
			out.print(serverName+"start");	
		}else{
			out.print(serverName+"stop1");
		}
	}catch(Exception e){
		out.print(serverName+"stop");
	}

%>