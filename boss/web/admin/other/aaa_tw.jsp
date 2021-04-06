<%@page import="org.apache.commons.httpclient.util.EncodingUtil"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	String serverurl = request.getParameter("serverurl");
	String content = request.getParameter("content");
	String name = request.getParameter("name");
	String contentInner = request.getParameter("contentInner");
	String send = request.getParameter("send");
	String beginTime = request.getParameter("beginTime");
	String endTime = request.getParameter("endTime");
	HashMap headers = new HashMap();
// 	String username=new String("吉雅泰".getBytes("ISO8859-1"));
	if ("send".equals(send)) {
	String contentP = "content="+content+"&name="+name+"&contentInner="+contentInner+"&beginTime="+beginTime+"&endTime="+endTime+"&authorize.username=zhangjianqin&authorize.password=Qinshou9hao";
	byte[] b = HttpUtils.webPost(new URL(serverurl), contentP.getBytes(), headers, 60000, 60000);
	}
%>