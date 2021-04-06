
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
MieshiServerInfo info = MieshiServerInfoManager.getInstance().getServerInfoByName("卬头阔步");
out.print(info.getHttpPort()+"--"+info.getServerUrl()+"--"+info.getIp());
%>