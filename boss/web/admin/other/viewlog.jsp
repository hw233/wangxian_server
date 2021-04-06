<%@page import="com.fy.boss.cmd.CMDService"%>
<%@page import="com.fy.boss.game.model.Server"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.boss.game.service.ServerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
	<%
		//根据网关页面传的服务器，找到对应的log进行处理
		int linenum = 3;
		ServerManager smanager = ServerManager.getInstance();
		CMDService cmdService = CMDService.getInstance(); 
		String servername = request.getParameter("servername");
		String logcontent = "";
		if(servername != null) {
			Server server = smanager.getServer(servername);
			String logpath = server.getResinhome() + "/log/" + "game_server/core.log";
			logcontent = cmdService.getServerLog(server.getGameipaddr(), logpath, linenum);
			out.print("logcontent:"+logcontent+"<br>");
		}
	%>
