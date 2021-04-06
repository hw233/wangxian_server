<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%
	String serverurl = request.getParameter("serverurl");
	String msgContent = request.getParameter("msgContent");
	String selected = request.getParameter("selected");
	String send = request.getParameter("send");
	HashMap headers = new HashMap();
	if ("send".equals(send)) {
		String contentP = "";
		if(serverurl.contains("116.213.142.189:8080")){
			 contentP = "msgContent="+msgContent+"&selected="+selected+"&authorize.username=wangtianxin&authorize.password=123321";
		}else{
			 contentP = "msgContent="+msgContent+"&selected="+selected+"&authorize.username=wangtianxin&authorize.password=123321";
		}
			
		byte[] b = HttpUtils.webPost(new URL(serverurl), contentP.getBytes(), headers, 60000, 60000);
	}
	
%>
