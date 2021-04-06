<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.boss.game.service.ServerManager"%>
<%@page import="com.fy.boss.game.model.Server"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	List<Server> lst = ServerManager.getInstance().getServers();
	out.println(JsonUtil.jsonFromObject(lst));
%>