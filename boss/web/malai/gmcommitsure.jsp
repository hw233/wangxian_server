<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String gmname = request.getParameter("gmname");
	String isvip = request.getParameter("isvip");
	boolean iscommit = NewFeedbackQueueManager.getInstance().getGmWorkNum(isvip, gmname);	
	out.print(iscommit);
%>