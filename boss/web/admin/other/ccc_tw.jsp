<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String serverurl = request.getParameter("serverurl");
	String content = request.getParameter("content");
	String name = request.getParameter("name");
	
	String contentInner = request.getParameter("contentInner");
	String beginTime = request.getParameter("beginTime");
	String endTime = request.getParameter("endTime");
	String hava = request.getParameter("hava");
	String bindSivlerNum = request.getParameter("bindSivlerNum");
	String send = request.getParameter("send");
	HashMap headers = new HashMap();
	if ("send".equals(send)) {
	String contentP = "content="+content+"&name="+name+"&contentInner="+contentInner+"&beginTime="+beginTime+"&endTime="+endTime+"&hava="+hava+"&bindSivlerNum="+bindSivlerNum+"&authorize.username=zhangjianqin&authorize.password=Qinshou9hao";
	byte[] b = HttpUtils.webPost(new URL(serverurl), contentP.getBytes(), headers, 60000, 60000);
	}
%>