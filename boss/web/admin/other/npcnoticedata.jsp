<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.boss.gm.npcnotice.NpcNoticeManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.npcnotice.BoardItem"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	
	long now = System.currentTimeMillis();
	String servername = request.getParameter("servername");
	NpcNoticeManager rbm = NpcNoticeManager.getInstance();
	List<BoardItem> list = rbm.getNotices(servername);
	if(list.size()>0){
		out.print(JsonUtil.jsonFromObject(list));
	}else{
		out.print("[]");
	}
	out.print("servername:"+servername);
%>