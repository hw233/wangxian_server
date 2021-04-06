<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String servername = request.getParameter("servername");
	String gateurl = request.getParameter("gateurl");
	HashMap headers = new HashMap();
	if (servername!=null && gateurl!=null) {
	String contentP = "servername="+servername+"&authorize.username=wangtianxin&authorize.password=123321";
	byte[] b = HttpUtils.webPost(new URL(gateurl), contentP.getBytes(), headers, 60000, 60000);
	out.print(new String(b).trim());
	}
%>