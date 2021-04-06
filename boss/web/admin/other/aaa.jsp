<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String serverurl = request.getParameter("serverurl");
	String content = request.getParameter("content");
	String name = request.getParameter("name");
	String contentInner = request.getParameter("contentInner");
	String send = request.getParameter("send");
	String beginTime = request.getParameter("beginTime");
	String endTime = request.getParameter("endTime");
	String checkeds = request.getParameter("checkeds");
	String selectstat = request.getParameter("selectstat");
	
	HashMap headers = new HashMap();
	if ("send".equals(send)) {
	String contentP = "selectstat="+selectstat+"&checkeds="+checkeds+"&content="+content+"&name="+name+"&contentInner="+contentInner+"&beginTime="+beginTime+"&endTime="+endTime+"&authorize.username=zhangjianqin&authorize.password=Qinshou7hao";
	byte[] b = HttpUtils.webPost(new URL(serverurl), contentP.getBytes(), headers, 60000, 60000);
	out.print(new String(b));
	}
%>