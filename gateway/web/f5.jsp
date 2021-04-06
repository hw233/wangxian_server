<%@page import="java.util.Iterator"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.Session"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.SessionManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
SessionManager sm = SessionManager.getInstance();
Session s = sm.getSession("kdbTwj6v9DNvRd1cK3NQ3216S0oqQvbv");
Iterator it = sm.sessionCache.keySet().iterator();
while(it.hasNext()){
	String key = (String)it.next();
	Session see = (Session)sm.sessionCache.get(key);
	out.print(see.getUsername()+"--"+see.getSessionID()+"--"+see.getClientID()+"--"+see.getCreateTime()+"<br>");
}


out.print(sm.sessionCache.getSize()+"<br>");
if (s == null) {
	out.print("session 不存在");
	return;
}
if (!s.getUsername().equals("chentao880")) {
	out.print("用户名不匹配");
	return;
}
if (!s.getClientID().equals("79017506371125481814")) {
	out.print("clientID不匹配");
	return;
}
out.print("匹配");
%>
