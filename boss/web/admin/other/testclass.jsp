<%@page import="java.lang.reflect.Method"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.boss.gm.gmuser.server.GmSystemNoticeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	GmSystemNoticeManager gmm = new GmSystemNoticeManager();
	Field fs[] = gmm.getClass().getFields();	
	for(Field f : fs){
		out.print(f.getName()+"<br>");
	}
	out.print("---------------");
	Field fs2[]  = gmm.getClass().getDeclaredFields();
	for(Field f : fs2){
		out.print(f.getName()+"<br>");
	}
	out.print("---------------");
	Method fs3[]  = gmm.getClass().getDeclaredMethods();
	for(Method f : fs3){
		out.print(f.getName()+"<br>");
	}
	out.print("---------------");
	Field f = gmm.getClass().getField("logger");
	out.print(f.getName()+"<br>");
	Method m = gmm.getClass().getMethod("获得毫秒数", new Class[]{String.class});
	out.print("black:"+m+"<br>");
	Object result = m.invoke(gmm.getClass(), new Object[]{new String("2013-02-11 11:22:21")});
	out.print("result:"+result+"<br>");
	out.print("1360552942000");
%>