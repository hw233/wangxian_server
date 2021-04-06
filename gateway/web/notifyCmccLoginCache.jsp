<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String userId = request.getParameter("userId");
	String p = request.getParameter("p");
	String result = NewLoginHandler.instance.handleCmccLogin(userId, p);
	out.print(result);
%>