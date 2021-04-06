<%@page import="com.xuanzhi.tools.transport.DefaultConnectionSelector"%>
<%@page import="com.fy.boss.gm.newfeedback.client.BossGmClientService"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.gm.newfeedback.NewFeedbackManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	BossGmClientService bcs = BossGmClientService.getInstance();
	Class c = bcs.getClass().getSuperclass();
	Field field = c.getDeclaredField("selector");
	field.setAccessible(true);
	DefaultConnectionSelector selecter = (DefaultConnectionSelector)field.get(bcs);
	if(selecter!=null){
		out.print("改之前host为："+selecter.getHost());
	}
	selecter.setHost("211.72.246.23");
	if(selecter!=null){
		out.print("改之后host为："+selecter.getHost());
	}
%>