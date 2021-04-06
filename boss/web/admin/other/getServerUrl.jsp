<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%@page import="com.fy.boss.game.model.Server"%><%@page import="java.util.List"%><%@page import="com.fy.boss.game.service.ServerManager"%>
<%
	ServerManager smanager = ServerManager.getInstance();
	List<Server> servers = smanager.getServers();
	
	for(Server server:servers){
		out.println(server.getName()+"<br/>");
		//out.print(server.getName()+"mmmm"+server.getSavingNotifyUrl().split("saving_notifier")[0]+"@@##");
	}
%>