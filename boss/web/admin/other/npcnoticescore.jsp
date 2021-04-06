<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.boss.gm.npcnotice.NpcNoticeManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.npcnotice.BoardItem"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="java.net.URL"%><%@page import="java.util.HashMap"%><%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><%
	long now = System.currentTimeMillis();
	String id = request.getParameter("id");
	String type = request.getParameter("which");
	if(id!=null && type!=null){
		NpcNoticeManager rbm = NpcNoticeManager.getInstance();
		BoardItem item = rbm.getNoticesById(Long.parseLong(id));
		if(item!=null){
			if(type!=null && type.equals("0")){
				item.setVoteNormal(item.getVoteNormal()+1);
			}else if(type!=null && type.equals("1")){
				item.setVoteGood(item.getVoteGood()+1);
			}else if(type!=null && type.equals("2")){
				item.setVoteBetter(item.getVoteBetter()+1);
			}
			rbm.updateNotice(item);
			out.print("评分成功!");
		}else{
			out.print("评分失败！");
		}
	}
%>
	
	
